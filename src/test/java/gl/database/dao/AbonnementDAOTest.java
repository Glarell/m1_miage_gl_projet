package gl.database.dao;

import gl.database.model.Abonnement;
import gl.database.model.Borne;
import gl.database.model.EtatBorne;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AbonnementDAOTest {

    @Test
    public void testGetAbonnementById() {
        Abonnement abonnement = AbonnementDAO.getAbonnementById(1);
        Abonnement abonnement2 = AbonnementDAO.getAbonnementById(2);

        assertThat(abonnement.getId_client()).isEqualTo(5);
        assertThat(abonnement.getId_borne()).isEqualTo(1);
        assertThat(abonnement2.getId_client()).isEqualTo(1);
        assertThat(abonnement2.getId_borne()).isEqualTo(3);
    }

    @Test
    public void testAddAbonnement() throws SQLException {
        Abonnement abonnement = new Abonnement(new Date(0), new Time(0), new Time(1), 1, 1);
        abonnement=AbonnementDAO.registrerAbonnement(abonnement);
        assertThat(abonnement.getId_abonnement()).isEqualTo(AbonnementDAO.getAbonnementById(abonnement.getId_abonnement()).getId_abonnement());
    }
}
