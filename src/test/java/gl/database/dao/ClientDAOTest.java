package gl.database.dao;

import gl.database.model.Client;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientDAOTest {

    @Test
    public void testGetListClient() {
        ArrayList<Client> list = (ArrayList<Client>) ClientDAO.getListClient();
        assertThat(list).hasSize(7);
    }

    @Test
    public void testGetClient() {
        Client client = ClientDAO.getClient(1);
        assertThat(client.getEmail()).isEqualTo("Admin@gmail.com");
        assertThat(client.getAdresse()).isEqualTo("Admin");
        assertThat(client.getPrenom()).isEqualTo("Admin");
        assertThat(client.getNom()).isEqualTo("Admin");
        assertThat(client.getCarte()).isEqualTo("Admin");
    }

    @Test
    public void testGetClientByEmailPwd() {
        Client client = ClientDAO.getClientByEmailPwd("Admin@gmail.com","root");
        assertThat(client.getEmail()).isEqualTo("Admin@gmail.com");
        assertThat(client.getAdresse()).isEqualTo("Admin");
        assertThat(client.getPrenom()).isEqualTo("Admin");
        assertThat(client.getNom()).isEqualTo("Admin");
        assertThat(client.getCarte()).isEqualTo("Admin");
    }

    @Test
    public void testDeleteClient() throws SQLException {
        ClientDAO.deleteClient(7);
        Client client = ClientDAO.getClient(7);
        assertThat(client.getId_client()).isEqualTo(-1);
    }

    @Test
    public void testRegistrerClientWithId() throws SQLException {
        Client client = new Client(7,"","","","","","","");
        ClientDAO.registrerClientWithId(client);
        client = ClientDAO.getClient(7);
        assertThat(client.getId_client()).isNotEqualTo(-1);
    }

    @Test
    public void testRegistrerClient() throws SQLException {
        Client client = new Client("","","","","test","","");
        client = ClientDAO.registrerClient(client);
        assertThat(client.getEmail()).isEqualTo("test");
    }

    @Test
    public void testIsEmailAlreadyExist() {
        assertThat(ClientDAO.isEmailAlreadyExist("test")).isTrue();
        assertThat(ClientDAO.isEmailAlreadyExist("dsqdeaeazea")).isFalse();
    }
}
