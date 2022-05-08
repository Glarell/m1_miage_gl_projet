package gl.database.dao;

import gl.database.model.Plaque;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PlaqueDAOTest {

    @Test
    public void testInsertNewPlaque() throws SQLException {
        PlaqueDAO.insertNewPlaque("plaque_test");
        assertThat(PlaqueDAO.getPlaqueByID("plaque_test")
                .getId_plaque()).isEqualTo("plaque_test");
        PlaqueDAO.deleteOldPlaque("plaque_test");
    }

    @Test
    public void testInsertExistingPlaque() throws SQLException {
        assertThatThrownBy(() -> {
            PlaqueDAO.insertNewPlaque("plaque_test");
            PlaqueDAO.insertNewPlaque("plaque_test");
        }).isInstanceOf(PSQLException.class);

        PlaqueDAO.deleteOldPlaque("plaque_test");
    }

    @Test
    public void testDeletePlaque() throws SQLException {
        PlaqueDAO.insertNewPlaque("plaque_test");
        PlaqueDAO.deleteOldPlaque("plaque_test");

        List<Plaque> listOfPlaque = PlaqueDAO.getAllPlaque();
        assertThat(listOfPlaque)
                .extracting(Plaque::getId_plaque)
                .doesNotContain("plaque_test")
                .hasSizeGreaterThanOrEqualTo(0);
    }

    @Test
    public void testGetAllPlaque() throws SQLException {
        PlaqueDAO.insertNewPlaque("plaque_test");
        List<Plaque> listOfPlaque = PlaqueDAO.getAllPlaque();
        assertThat(listOfPlaque)
                .extracting(Plaque::getId_plaque)
                .contains("plaque_test")
                .hasSizeGreaterThanOrEqualTo(1);

        PlaqueDAO.deleteOldPlaque("plaque_test");
    }

    @Test
    public void testGetUnknowPlaque() {
        assertThat(PlaqueDAO.getPlaqueByID("plaque_test").getId_plaque())
                .isNull();
    }
}
