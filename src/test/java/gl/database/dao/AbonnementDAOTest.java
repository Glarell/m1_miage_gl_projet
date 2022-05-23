package gl.database.dao;

import gl.database.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    static void init() throws SQLException {
        end();
        Client client = new Client("test", "test", "test", "test", "test", "test", "test");
        client.setId_client(999);
        ClientDAO.registrerClientWithId(client);
    }

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
        abonnement = AbonnementDAO.registrerAbonnement(abonnement);
        assertThat(abonnement.getId_abonnement()).isEqualTo(AbonnementDAO.getAbonnementById(abonnement.getId_abonnement()).getId_abonnement());
    }

    @Test
    public void testGetAbonnementByClient() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_abonnement = new Date(format.parse("01-01-1999 00:00").getTime());

        Abonnement abonnement = new Abonnement(date_abonnement,
                new Time(format.parse("01-01-1999 10:00").getTime()),
                new Time(format.parse("01-01-1999 10:30").getTime()),
                999, 1);
        AbonnementDAO.registrerAbonnement(abonnement);

        assertThat(AbonnementDAO.getAbonnementByClient(999))
                .extracting(Abonnement::getDate_abonnement)
                .isNotNull()
                .contains(date_abonnement)
                .hasSize(1);

        AbonnementDAO.deleteOldAbonnementByClient(999);
    }

    @Test
    public void testHasExistingAbonnementFromSameUserEqualFalse() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_abonnement = new Date(format.parse("01-01-1999 00:00").getTime());

        Abonnement abonnement = new Abonnement(date_abonnement,
                new Time(format.parse("01-01-1999 10:00").getTime()),
                new Time(format.parse("01-01-1999 10:30").getTime()),
                999, 1);
        AbonnementDAO.registrerAbonnement(abonnement);

        assertThat(AbonnementDAO.hasExistingAbonnementFromSameUser(999, abonnement))
                .isFalse();

        AbonnementDAO.deleteOldAbonnementByClient(999);
    }

    @Test
    public void testHasExistingAbonnementFromSameUserEqualTrue() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_abonnement = new Date(format.parse("01-01-1999 00:00").getTime());

        Abonnement abonnement = new Abonnement(date_abonnement,
                new Time(format.parse("01-01-1999 9:00").getTime()),
                new Time(format.parse("01-01-1999 9:30").getTime()),
                999, 1);
        AbonnementDAO.registrerAbonnement(abonnement);
        Abonnement abonnement2 = new Abonnement(date_abonnement,
                new Time(format.parse("01-01-1999 10:00").getTime()),
                new Time(format.parse("01-01-1999 10:30").getTime()),
                999, 1);

        assertThat(AbonnementDAO.hasExistingAbonnementFromSameUser(999, abonnement2))
                .isTrue();

        AbonnementDAO.deleteOldAbonnementByClient(999);
    }

    @Test
    public void testGetExistingAbonnementFromSameUser() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_abonnement = new Date(format.parse("01-01-1999 00:00").getTime());

        Abonnement abonnement = new Abonnement(date_abonnement,
                new Time(format.parse("01-01-1999 9:00").getTime()),
                new Time(format.parse("01-01-1999 9:30").getTime()),
                999, 1);
        AbonnementDAO.registrerAbonnement(abonnement);
        Abonnement abonnement2 = new Abonnement(date_abonnement,
                new Time(format.parse("01-01-1999 10:00").getTime()),
                new Time(format.parse("01-01-1999 10:30").getTime()),
                999, 1);

        assertThat(AbonnementDAO.getExistingAbonnementFromSameUser(999, abonnement2))
                .extracting(Abonnement::getId_client)
                .isEqualTo(999);

        AbonnementDAO.deleteOldAbonnementByClient(999);
    }

    @Test
    public void testHasExistingAbonnementEqualTrue() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_abonnement = new Date(format.parse("01-01-1999 00:00").getTime());

        Abonnement abonnement = new Abonnement(date_abonnement,
                new Time(format.parse("01-01-1999 10:00").getTime()),
                new Time(format.parse("01-01-1999 10:30").getTime()),
                999, 1);
        AbonnementDAO.registrerAbonnement(abonnement);

        assertThat(AbonnementDAO.hasExistingAbonnement(date_abonnement, new Time(format.parse("01-01-1999 09:50").getTime()),
                new Time(format.parse("01-01-1999 10:10").getTime()), 1))
                .isTrue();

        AbonnementDAO.deleteOldAbonnementByClient(999);
    }

    @Test
    public void testHasExistingAbonnementEqualFalse() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_abonnement = new Date(format.parse("01-01-1999 00:00").getTime());

        assertThat(ReservationDAO.hasExistingReservation(date_abonnement, new Time(format.parse("01-01-1999 09:50").getTime()),
                new Time(format.parse("01-01-1999 10:10").getTime()), 1))
                .isFalse();
    }

    @Test
    public void testUpdateMergeAbonnement() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_abonnement = new Date(format.parse("01-01-1999 00:00").getTime());

        Abonnement abonnement = new Abonnement(date_abonnement,
                new Time(format.parse("01-01-1999 10:00").getTime()),
                new Time(format.parse("01-01-1999 10:30").getTime()),
                999, 1);
        AbonnementDAO.registrerAbonnement(abonnement);
        AbonnementDAO.updateMergeAbonnement(AbonnementDAO.getAbonnementByClient(999).get(0), new Time(format.parse("01-01-1999 13:30").getTime()));

        assertThat(AbonnementDAO.getAbonnementByClient(999).get(0).getFin_intervalle().toLocalTime())
                .isEqualTo(new Time(format.parse("01-01-1999 13:30").getTime()).toLocalTime());

        AbonnementDAO.deleteOldAbonnementByClient(999);
    }

    @Test
    public void testDeleteOldAbonnementByClient() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_abonnement = new Date(format.parse("01-01-1999 00:00").getTime());

        Abonnement abonnement = new Abonnement(date_abonnement,
                new Time(format.parse("01-01-1999 10:00").getTime()),
                new Time(format.parse("01-01-1999 10:30").getTime()),
                999, 1);
        AbonnementDAO.registrerAbonnement(abonnement);
        AbonnementDAO.deleteOldAbonnementByClient(999);

        assertThat(AbonnementDAO.getAbonnementByClient(999))
                .isNotNull()
                .hasSize(0);
    }

    @AfterAll
    static void end() throws SQLException {
        ClientDAO.deleteClient(999);
    }
}
