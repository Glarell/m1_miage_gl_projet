package gl.database.dao;

import gl.database.model.Abonnement;
import gl.database.model.Client;
import gl.database.model.EstAssocie;
import gl.database.model.Reservation;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        Client client = ClientDAO.getClientByEmailPwd("Admin@gmail.com", "root");
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
        Client client = new Client(7, "", "", "", "", "", "", "");
        ClientDAO.registrerClientWithId(client);
        client = ClientDAO.getClient(7);
        assertThat(client.getId_client()).isNotEqualTo(-1);
    }

    @Test
    public void testRegistrerClient() throws SQLException {
        Client client = new Client("", "", "", "", "test", "", "");
        client = ClientDAO.registrerClient(client);
        assertThat(client.getEmail()).isEqualTo("test");
    }

    @Test
    public void testIsEmailAlreadyExist() {
        assertThat(ClientDAO.isEmailAlreadyExist("test")).isTrue();
        assertThat(ClientDAO.isEmailAlreadyExist("dsqdeaeazea")).isFalse();
    }

    @Test
    public void testGetNbReservationAndAbonnement() throws SQLException, ParseException {
        Client client = new Client("test", "test", "test", "test", "test", "test", "test");
        client.setId_client(999);
        ClientDAO.registrerClientWithId(client);

        PlaqueDAO.insertNewPlaque("plaque_test");
        EstAssocieDAO.insertNewEstAssocie(new EstAssocie(999, "plaque_test"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date(format.parse("01-01-2025 00:00").getTime());

        Abonnement abonnement = new Abonnement(date,
                new Time(format.parse("01-01-2025 10:00").getTime()),
                new Time(format.parse("01-01-2025 10:30").getTime()),
                999, 1);
        AbonnementDAO.registrerAbonnement(abonnement);

        Reservation reservation = new Reservation(999, date,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, EstAssocieDAO.getEstAssocieByClient(999).get(0).getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        assertThat(ClientDAO.getNbReservationAndAbonnement(999))
                .isEqualTo(2);

        ReservationDAO.deleteOldReservation(999);
        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test");
        AbonnementDAO.deleteOldAbonnementByClient(999);
        ClientDAO.deleteClient(999);
        PlaqueDAO.deleteOldPlaque("plaque_test");
    }

    @Test
    public void testGetNbReservationAndAbonnementFromUnknowClient() {
        assertThat(ClientDAO.getNbReservationAndAbonnement(999))
                .isEqualTo(0);
    }
}
