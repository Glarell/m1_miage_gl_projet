package gl.application;

import gl.database.dao.ClientDAO;
import gl.database.model.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void inscriptionFailed() throws SQLException {
        Client client = new Client("1001","teDSDSP123renom","1 rue du test","faux!","test@letest2.fr","test","1567812345678");
        ClientDAO.registrerClient(client);
        client = ClientDAO.getClientByEmailPwd("test@letest2.fr", "test");
        assertThat(client.isNotReal());
    }
}
