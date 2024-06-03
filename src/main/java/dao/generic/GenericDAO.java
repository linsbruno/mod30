package dao.generic;

import anotation.KeyType;
import anotation.Table;
import anotation.TableColumn;
import dao.CommonClass;
import dao.generic.jdbc.ConnectionFactory;
import exception.DAOException;
import exception.KeyTypeNotFoundException;
import exception.MoreRegisterException;
import exception.TableException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class GenericDAO<T extends CommonClass, E extends Serializable> implements IGenericDAO<T,E>{

    public abstract Class<T> getClassType();

    public abstract void updateData(T entity, T entityRegistered);

    protected abstract String getQueryInsert();
    protected abstract String getQueryDelete();
    protected abstract String getQueryUpdate();

    protected abstract void setParamsQueryInsert(PreparedStatement stmInsert, T entity) throws SQLException;
    protected abstract void setParamsQueryDelete(PreparedStatement stmDelete, E value) throws SQLException;
    protected abstract void setParamsQueryUpdate(PreparedStatement stmUpdate, T entity) throws SQLException;
    protected abstract void setParamsQuerySelect(PreparedStatement stmSelect, E value) throws SQLException;

    public GenericDAO() {

    }

    public E getKey(T entity ) throws KeyTypeNotFoundException {
        Field[] fields = entity.getClass().getDeclaredFields();
        E returnValue = null;

        for (Field field : fields) {
            if (field.isAnnotationPresent(KeyType.class)) {
                KeyType keyType = field.getAnnotation(KeyType.class);
                String methodName = keyType.value();

                try {
                    Method method = entity.getClass().getMethod(methodName);
                    returnValue = (E) method.invoke(entity);
                    return returnValue;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new KeyTypeNotFoundException( "CHAVE PRINCIPAL DO OBJETO " + entity.getClass() + " NÃO ENCONTRADA", e);
                }
            }
        }
        String msg = "CHAVE PRINCIPAL DO OBJETO " + entity.getClass() + " NÃO ENCONTRADA";
        System.out.println("**** ERRO ****" + msg);
        throw new KeyTypeNotFoundException(msg);
    }

    @Override
    public Boolean register(T entity) throws KeyTypeNotFoundException, DAOException {
        Connection connection = null;
        PreparedStatement stm = null;

        try {
            connection = getConnection();
            stm = connection.prepareStatement(getQueryInsert(), Statement.RETURN_GENERATED_KEYS);
            setParamsQueryInsert(stm, entity);
            int rowsAffected = stm.executeUpdate();

            if(rowsAffected > 0) {
                try (ResultSet rs = stm.getGeneratedKeys()){
                    if (rs.next()) {
                        ((CommonClass) entity).setId(rs.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO CADASTRANDO OBJETO", e);
        } finally {
            closeConnection(connection, stm, null);
        }
        return false;
    }


    @Override
    public void delete(E value) throws DAOException {
        Connection connection = getConnection();
        PreparedStatement stm = null;

        try {
            stm = connection.prepareStatement(getQueryDelete());
            setParamsQueryDelete(stm, value);
            int rowsAffected = stm.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ERRO EXCLUINDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }


    @Override
    public void change(T entity) throws KeyTypeNotFoundException, DAOException {
        Connection connection = getConnection();
        PreparedStatement stm = null;

        try {
            stm = connection.prepareStatement(getQueryUpdate());
            setParamsQueryUpdate(stm, entity);
            int rowsAffected = stm.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ERRO ALTERANDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, null);
        }

    }


    public T consult(E value) throws MoreRegisterException, TableException, DAOException {

        try {
            validateMoreRegister(value);
            Connection connection = getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM " + getTableName() + " WHERE " + getFieldKeyName(getClassType()) + " = ?");
            setParamsQuerySelect(stm, value);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                T entity = getClassType().getConstructor(null).newInstance(null);
                Field[] fields = entity.getClass().getDeclaredFields();

                for (Field field : fields) {
                    if (field.isAnnotationPresent(TableColumn.class)) {
                        TableColumn column = field.getAnnotation(TableColumn.class);
                        String dbName = column.dbName();
                        String javaSetName = column.setJavaName();
                        Class<?> classField = field.getType();

                        try {
                            Method method = entity.getClass().getMethod(javaSetName, classField);
                            setValueByType(entity, method, classField, rs, dbName);
                        } catch (NoSuchMethodException | IllegalAccessException |
                                 InvocationTargetException | KeyTypeNotFoundException e) {
                            throw new DAOException("ERRO CONSULTANDO OBJETO ", e);
                        }
                    }
                }
                return entity;
            }
        } catch (SQLException |
                 InstantiationException |
                 IllegalAccessException |
                 IllegalArgumentException |
                 InvocationTargetException |
                 NoSuchMethodException |
                 SecurityException |
                 KeyTypeNotFoundException e) {
            throw new DAOException("ERRO CONSULTANDO OBJETO ", e);
        }
        return null;
    }

    private String getTableName() throws TableException {
        if (getClassType().isAnnotationPresent(Table.class)) {
            Table table = getClassType().getAnnotation(Table.class);
            return table.value();
        } else {
            throw new TableException("TABELA NO TIPO " + getClassType().getName() + " NÃO FOI ENCONTRADA");
        }
    }

    private String getFieldKeyName(Class clazz) throws KeyTypeNotFoundException {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field: fields) {
            if (field.isAnnotationPresent(KeyType.class) && field.isAnnotationPresent(TableColumn.class)) {
                TableColumn column = field.getAnnotation(TableColumn.class);
                return column.dbName();
            }
        }
        return null;
    }


    private void setValueByType(T entity, Method method, Class<?> classField, ResultSet rs, String fieldName)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, SQLException, KeyTypeNotFoundException {

        if (classField.equals(Integer.class)) {
            Integer val = rs.getInt(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Long.class)) {
            Long val = rs.getLong(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Double.class)) {
            Double val = rs.getDouble(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(Short.class)) {
            Short val = rs.getShort(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(BigDecimal.class)) {
            BigDecimal val = rs.getBigDecimal(fieldName);
            method.invoke(entity, val);
        } else if (classField.equals(String.class)) {
            String val = rs.getString(fieldName);
            method.invoke(entity, val);
        } else {
            throw new KeyTypeNotFoundException("TIPO DE CLASSE NÃO CONHECIDO: " + classField);
        }
    }


    public Object getValueByType(Class<?> typeField, ResultSet rs, String fieldName) throws SQLException, KeyTypeNotFoundException {
        if (typeField.equals(Integer.TYPE)) {
            return rs.getInt(fieldName);
        } else if (typeField.equals(Long.TYPE)) {
            return rs.getLong(fieldName);
        } else if (typeField.equals(Double.TYPE)) {
            return rs.getDouble(fieldName);
        } else if (typeField.equals(Short.TYPE)) {
            return rs.getShort(fieldName);
        } else if (typeField.equals(BigDecimal.class)) {
            return rs.getBigDecimal(fieldName);
        } else if (typeField.equals(String.class)) {
            return rs.getString(fieldName);
        } else {
            throw new KeyTypeNotFoundException("TIPO DE CLASSE NÃO CONHECIDO: " + typeField);
        }
    }


    private void validateMoreRegister (E value) throws MoreRegisterException, TableException, KeyTypeNotFoundException, DAOException {
        Connection connection = getConnection();
        PreparedStatement stm = null;
        ResultSet rs = null;
        Long count = null;

        try {
            stm = connection.prepareStatement("SELECT count(*) FROM " + getTableName() + " WHERE " + getFieldKeyName(getClassType()) + " = ?");
            setParamsQuerySelect(stm, value);
            rs = stm.executeQuery();

            if (rs.next()) {
                count = rs.getLong(1);
                if (count > 1) {
                    throw new MoreRegisterException("ENCONTRADO MAIS DE UM REGISTRO DE " + getTableName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, stm, rs);
        }
    }

    public void closeConnection(Connection connection, PreparedStatement stm, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            } if (stm != null && !stm.isClosed()) {
                stm.close();
            } if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }


    public Collection<T> toFindAll() throws DAOException {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            stm = connection.prepareStatement("SELECT * FROM " + getTableName());
            rs = stm.executeQuery();

            while (rs.next()) {
                T entity = getClassType().getConstructor(null).newInstance(null);
                Field[] fields = entity.getClass().getDeclaredFields();

                for (Field field : fields) {
                    if (field.isAnnotationPresent(TableColumn.class)) {
                        TableColumn column = field.getAnnotation(TableColumn.class);
                        String dbName = column.dbName();
                        String javaSetName = column.setJavaName();
                        Class<?> classField = field.getType();

                        try {
                            Method method = entity.getClass().getMethod(javaSetName, classField);
                            setValueByType(entity, method, classField, rs, dbName);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                                 KeyTypeNotFoundException e) {
                            throw new DAOException("ERRO LISTANDO OBJETOS ", e);
                        }
                    }
                }
                list.add(entity);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | TableException e) {
            throw new DAOException("ERRO LISTANDO OBJETOS ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return list;
    }

    protected Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new DAOException("ERRO ABRINDO CONEXÃO COM O BANCO DE DADOS ", e);
        }
    }
}