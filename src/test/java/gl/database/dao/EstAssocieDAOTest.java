package gl.database.dao;

import gl.database.model.Client;
import gl.database.model.EstAssocie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.notIn;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class EstAssocieDAOTest {

    @BeforeAll
    static void init() throws SQLException {
        end();
        Client client = new Client("test", "test", "test", "test", "test", "test", "test");
        client.setId_client(999);
        ClientDAO.registrerClientWithId(client);

        PlaqueDAO.insertNewPlaque("plaque_test");
        PlaqueDAO.insertNewPlaque("plaque_test2");
        PlaqueDAO.insertNewPlaque("plaque_test3");
    }

    @Test
    public void testInsertNewEstAssocie() throws SQLException {
        EstAssocie estAssocie = new EstAssocie(999, "plaque_test");
        EstAssocieDAO.insertNewEstAssocie(estAssocie);
        assertThat(EstAssocieDAO.getEstAssocieByClient(999))
                .extracting(EstAssocie::getId_plaque)
                .contains("plaque_test")
                .hasSize(1);
        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test");
    }

    @Test
    public void testInsertNewEstAssocieWithUnknowClient() {
        assertThatThrownBy(() -> {
            EstAssocie estAssocie = new EstAssocie(998, "plaque_test");
            EstAssocieDAO.insertNewEstAssocie(estAssocie);
        }).isInstanceOf(PSQLException.class);
    }

    @Test
    public void testInsertNewEstAssocieWithUnknowPlaque() {
        assertThatThrownBy(() -> {
            EstAssocie estAssocie = new EstAssocie(999, "plaque_test4");
            EstAssocieDAO.insertNewEstAssocie(estAssocie);
        }).isInstanceOf(PSQLException.class);
    }

    @Test
    public void testInsertNewEstAssocieTemporaire() throws SQLException {
        EstAssocie estAssocie = new EstAssocie(999, "plaque_test");
        EstAssocieDAO.insertNewEstAssocieTemporaire(estAssocie);
        assertThat(EstAssocieDAO.getEstAssocieByClient(999))
                .filteredOn(EstAssocie::getId_plaque, "plaque_test")
                .filteredOn(EstAssocie::isTemporaire, true)
                .hasSize(1);

        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test");
    }

    @Test
    public void testDeleteEstAssocie() throws SQLException {
        EstAssocie estAssocie = new EstAssocie(999, "plaque_test");
        EstAssocieDAO.insertNewEstAssocieTemporaire(estAssocie);
        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test");

        List<EstAssocie> listOfestAssocie = EstAssocieDAO.getAllEstAssocie();
        assertThat(listOfestAssocie)
                .filteredOn(EstAssocie::getId_client, notIn(999))
                .filteredOn(EstAssocie::getId_plaque, notIn("plaque_test"))
                .hasSizeGreaterThanOrEqualTo(0);
    }

    @Test
    public void testGetAllEstAssocie() throws SQLException {
        EstAssocie estAssocie = new EstAssocie(999, "plaque_test");
        EstAssocie estAssocie2 = new EstAssocie(999, "plaque_test2");
        EstAssocie estAssocie3 = new EstAssocie(999, "plaque_test3");
        EstAssocieDAO.insertNewEstAssocie(estAssocie);
        EstAssocieDAO.insertNewEstAssocie(estAssocie2);
        EstAssocieDAO.insertNewEstAssocie(estAssocie3);

        List<EstAssocie> listOfEstAssocie = EstAssocieDAO.getAllEstAssocie();
        assertThat(listOfEstAssocie)
                .extracting(EstAssocie::getId_plaque)
                .contains("plaque_test")
                .contains("plaque_test2")
                .contains("plaque_test3")
                .hasSizeGreaterThanOrEqualTo(3);

        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test");
        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test2");
        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test3");
    }

    @Test
    public void testGetEstAssocieByClient() throws SQLException {
        EstAssocie estAssocie = new EstAssocie(999, "plaque_test");
        EstAssocie estAssocie2 = new EstAssocie(999, "plaque_test2");
        EstAssocie estAssocie3 = new EstAssocie(999, "plaque_test3");
        EstAssocieDAO.insertNewEstAssocie(estAssocie);
        EstAssocieDAO.insertNewEstAssocie(estAssocie2);
        EstAssocieDAO.insertNewEstAssocie(estAssocie3);

        List<EstAssocie> listOfEstAssocie = EstAssocieDAO.getEstAssocieByClient(999);
        assertThat(listOfEstAssocie)
                .extracting(EstAssocie::getId_plaque)
                .contains("plaque_test")
                .contains("plaque_test2")
                .contains("plaque_test3")
                .hasSize(3);

        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test");
        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test2");
        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test3");
    }

    @Test
    public void testGetEstAssocieByPlaque() throws SQLException {
        EstAssocie estAssocie = new EstAssocie(999, "plaque_test");
        EstAssocieDAO.insertNewEstAssocie(estAssocie);

        List<EstAssocie> listOfEstAssocie = EstAssocieDAO.getEstAssocieByPlaque("plaque_test");
        assertThat(listOfEstAssocie)
                .filteredOn(EstAssocie::getId_client, 999)
                .filteredOn(EstAssocie::getId_plaque, "plaque_test")
                .hasSize(1);

        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test");
    }

    @Test
    public void testgetEstAssocieTemporaire() throws SQLException {
        EstAssocie estAssocie = new EstAssocie(999, "plaque_test");
        EstAssocieDAO.insertNewEstAssocieTemporaire(estAssocie);
        EstAssocie estAssocieTemporaire = EstAssocieDAO.getEstAssocieTemporaire(999, "plaque_test");

        assertThat(estAssocieTemporaire.getId_client()).isEqualTo(estAssocie.getId_client());
        assertThat(estAssocieTemporaire.getId_plaque()).isEqualTo(estAssocie.getId_plaque());
        assertThat(estAssocieTemporaire.isTemporaire()).isTrue();
        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test");
    }

    @AfterAll
    static void end() throws SQLException {
        ClientDAO.deleteClient(999);

        PlaqueDAO.deleteOldPlaque("plaque_test");
        PlaqueDAO.deleteOldPlaque("plaque_test2");
        PlaqueDAO.deleteOldPlaque("plaque_test3");
    }
}
