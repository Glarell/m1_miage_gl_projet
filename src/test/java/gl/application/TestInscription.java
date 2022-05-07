package gl.application;

import gl.database.dao.ClientDAO;
import gl.database.model.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Inscription")
public class TestInscription {

    @Test
    public void inscriptionSucceded() throws SQLException {
        Client client = new Client("testNom","testPrenom","1 rue du test","0601567561","test@letest.fr","test","1234567812345678");
        ClientDAO.registrerClient(client);
        client = ClientDAO.getClientByEmailPwd("test@letest.fr", "test");
        assertThat(!client.isNotReal());

    }

    @Test
    public void inscriptionFailed1() {
        assertThatThrownBy(() -> {
            Client client = new Client("tondon","cesar","1 rue du test","101010","test@letest2.fr","test","1234567812345678");
            ClientDAO.registrerClient(client);
            ClientDAO.getClientByEmailPwd("test@letest2.fr", "test");
        }).isInstanceOf(PSQLException.class);
    }

    @Test
    public void inscriptionFailed2() {
        assertThatThrownBy(() -> {
            Client client = new Client("tondon","cesar","1 rue du test","0102030405","test@letest2.fr","test","18");
            ClientDAO.registrerClient(client);
            ClientDAO.getClientByEmailPwd("test@letest2.fr", "test");
        }).isInstanceOf(PSQLException.class);
    }
}
