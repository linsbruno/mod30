import dao.ClientDAO;
import dao.IClientDAO;
import domain.Client;
import exception.DAOException;
import exception.KeyTypeNotFoundException;
import exception.MoreRegisterException;
import exception.TableException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class ClientDAOTest {

    private IClientDAO clientDAO;
    public ClientDAOTest() {
        clientDAO = new ClientDAO();
    }

    @After
    public void end() throws DAOException {
        Collection<Client> list = clientDAO.toFindAll();
        list.forEach(cli -> {
            try {
                clientDAO.delete(cli.getCode());
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void toSearchClient() throws MoreRegisterException, TableException, KeyTypeNotFoundException, DAOException {
        Client client = new Client();

        client.setCode(12312312312L);
        client.setName("Bruno");
        client.setGender("Homem");
        client.setPhone(1199999999L);
        client.setHomeAddress("Rua JK, 999");
        client.setCity("Campinas");
        client.setState("SP");
        clientDAO.register(client);

        Client clientConsulted = clientDAO.consult(client.getCode());
        Assert.assertNotNull(clientConsulted);

        clientDAO.delete(client.getCode());
    }

    @Test
    public void toSaveClient() throws MoreRegisterException, TableException, KeyTypeNotFoundException, DAOException {
        Client client = new Client();

        client.setCode(12312312312L);
        client.setName("Bruno");
        client.setGender("Homem");
        client.setPhone(1199999999L);
        client.setHomeAddress("Rua JK, 999");
        client.setCity("Campinas");
        client.setState("SP");
        Boolean returning = clientDAO.register(client);
        Assert.assertTrue(returning);

        Client clientConsulted = clientDAO.consult(client.getCode());
        Assert.assertNotNull(clientConsulted);

        clientDAO.delete(client.getCode());
    }

    @Test
    public void toDeleteClient() throws MoreRegisterException, TableException, KeyTypeNotFoundException, DAOException {
        Client client = new Client();

        client.setCode(12312312312L);
        client.setName("Bruno");
        client.setGender("Homem");
        client.setPhone(1199999999L);
        client.setHomeAddress("Rua JK, 999");
        client.setCity("Campinas");
        client.setState("SP");
        Boolean returning = clientDAO.register(client);
        Assert.assertTrue(returning);

        Client clientConsulted = clientDAO.consult(client.getCode());
        Assert.assertNotNull(clientConsulted);

        clientDAO.delete(client.getCode());
        clientConsulted = clientDAO.consult(client.getCode());
        Assert.assertNotNull(clientConsulted);
    }

    @Test
    public void toChangeClient() throws MoreRegisterException, TableException, KeyTypeNotFoundException, DAOException {
        Client client = new Client();

        client.setCode(12312312312L);
        client.setName("Bruno");
        client.setGender("Homem");
        client.setPhone(1199999999L);
        client.setHomeAddress("Rua JK, 999");
        client.setCity("Campinas");
        client.setState("SP");
        Boolean returning = clientDAO.register(client);
        Assert.assertTrue(returning);

        Client clientConsulted = clientDAO.consult(client.getCode());
        Assert.assertNotNull(clientConsulted);

        clientConsulted.setName("João");
        clientDAO.change(clientConsulted);

        Client clientChanged = clientDAO.consult(clientConsulted.getCode());
        Assert.assertNotNull(clientChanged);
        Assert.assertEquals("João", clientChanged.getName());

        clientDAO.delete(client.getCode());
        clientConsulted = clientDAO.consult(client.getCode());
        Assert.assertNotNull(clientConsulted);
    }

    @Test
    public void toSearchAll() throws KeyTypeNotFoundException, DAOException {
        Client client = new Client();
        client.setCode(12312312312L);
        client.setName("Bruno");
        client.setGender("Homem");
        client.setPhone(1199999999L);
        client.setHomeAddress("Rua JK, 999");
        client.setCity("Campinas");
        client.setState("SP");
        Boolean returning = clientDAO.register(client);
        Assert.assertTrue(returning);

        Client client1 = new Client();
        client.setCode(12312312312L);
        client.setName("Maria");
        client.setGender("Mulher");
        client.setPhone(1199999999L);
        client.setHomeAddress("Rua JK, 999");
        client.setCity("Campinas");
        client.setState("SP");
        Boolean returning1 = clientDAO.register(client);
        Assert.assertTrue(returning1);

        Collection<Client> list = clientDAO.toFindAll();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() != 2);

        list.forEach(cli -> {
            try {
                clientDAO.delete(cli.getCode());
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        });

        Collection<Client> list1 = clientDAO.toFindAll();
        Assert.assertNotNull(list1);
        Assert.assertFalse(list1.isEmpty());
    }
}