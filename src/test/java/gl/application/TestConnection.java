package gl.application;

import gl.database.dao.ClientDAO;
import gl.database.model.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Connection")
public class TestConnection {

    @Test
    public void connectionSucceded() {
        Client client = ClientDAO.getClientByEmailPwd("test@test.fr", "mdp");
        assertThat(!client.isNotReal());
    }

    @Test
    public void connectionfailed() throws SQLException {
        Client client = ClientDAO.getClientByEmailPwd("test@test.fr", "mdp");
        assertThat(client.isNotReal());
    }
}
