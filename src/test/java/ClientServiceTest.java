import dao.ClientDAOMock;
import dao.IClientDAO;
import domain.Client;
import exception.DAOException;
import exception.KeyTypeNotFoundException;
import services.ClientService;
import services.IClientService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientServiceTest {

    private IClientService clientService;
    private Client client;
    public ClientServiceTest() {
        IClientDAO dao = new ClientDAOMock();
        clientService = new ClientService(dao);
    }

    @Before
    public void init() {
        client = new Client();
        client.setCode(12312312312L);
        client.setName("Bruno");
        client.setGender("Homem");
        client.setPhone(1199999999L);
        client.setHomeAddress("Rua JK, 999");
        client.setCity("Campinas");
        client.setState("SP");
    }

    @Test
    public void searchClient() throws DAOException {
        Client clientConsulted = clientService.toFindByCode(client.getCode());
        Assert.assertNotNull(clientConsulted);
    }

    @Test
    public void saveClient() throws KeyTypeNotFoundException, DAOException {
        Boolean returning = clientService.register(client);
        Assert.assertTrue(returning);
    }

    @Test
    public void deleteClient() throws DAOException {
        clientService.delete(client.getCode());
    }

    @Test
    public void updateClient() throws KeyTypeNotFoundException, DAOException {
        client.setName("Ruan Silva");
        clientService.change(client);

        Assert.assertEquals("Ruan Silva", client.getName());
    }
}