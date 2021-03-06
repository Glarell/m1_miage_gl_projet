package gl.database.dao;

import gl.database.model.Client;
import gl.database.model.EstAssocie;
import gl.database.model.Reservation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationDAOTest {

    @BeforeAll
    static void init() throws SQLException {
        end();
        Client client = new Client("test", "test", "test", "test", "test", "test", "test");
        client.setId_client(999);
        ClientDAO.registrerClientWithId(client);

        PlaqueDAO.insertNewPlaque("plaque_test");
        EstAssocieDAO.insertNewEstAssocie(new EstAssocie(999, "plaque_test"));
    }

    @Test
    public void testGetReservationFromCurrentDate() throws SQLException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        Date date_reservation = new Date(System.currentTimeMillis());

        Reservation reservation = new Reservation(999, date_reservation,
                Time.valueOf(LocalDateTime.now().minusMinutes(5).toLocalTime()),
                Time.valueOf(LocalDateTime.now().plusMinutes(5).toLocalTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        assertThat(ReservationDAO.getReservationFromCurrentDate(999).getId_reservation())
                .isEqualTo(999);

        ReservationDAO.deleteOldReservation(999);
    }

    @Test
    public void testRegistrerReservationWithId() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        assertThat(ReservationDAO.getReservationById(999).getDate_reservation().getTime())
                .isEqualTo(date_reservation.getTime());

        ReservationDAO.deleteOldReservation(999);
    }

    @Test
    public void testGetAllReservationInProgressByIdClient() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        assertThat(ReservationDAO.getAllReservationInProgressByIdClient(999))
                .hasSize(0);

        ReservationDAO.updateArriveeReservation(reservation);

        assertThat(ReservationDAO.getAllReservationInProgressByIdClient(999))
                .filteredOn(Reservation::getId_reservation, 999)
                .hasSize(1);

        ReservationDAO.deleteOldReservation(999);
    }

    @Test
    public void testDeleteOldReservation() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        ReservationDAO.deleteOldReservation(999);

        assertThat(ReservationDAO.getReservationById(999).getDate_reservation())
                .isNull();
    }

    @Test
    public void testIsExistingReservationFromSameUserEqualFalse() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        assertThat(ReservationDAO.hasExistingReservationFromSameUser(999, reservation))
                .isFalse();

        ReservationDAO.deleteOldReservation(999);
    }

    @Test
    public void testIsExistingReservationFromSameUserEqualTrue() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);
        Reservation reservation2 = new Reservation(998, date_reservation,
                new Time(format.parse("01-01-2022 9:00").getTime()),
                new Time(format.parse("01-01-2022 9:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation2);

        assertThat(ReservationDAO.hasExistingReservationFromSameUser(999, reservation))
                .isTrue();

        ReservationDAO.deleteOldReservation(999);
        ReservationDAO.deleteOldReservation(998);
    }

    @Test
    public void testGetExistingReservationFromSameUser() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 9:00").getTime()),
                new Time(format.parse("01-01-2022 9:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        Reservation reservation2 = new Reservation(998, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);

        assertThat(ReservationDAO.getExistingReservationFromSameUser(999, reservation2))
                .extracting(Reservation::getId_reservation)
                .isEqualTo(999);

        ReservationDAO.deleteOldReservation(999);
    }

    @Test
    public void testIsExistingReservationEqualTrue() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        assertThat(ReservationDAO.hasExistingReservation(date_reservation, new Time(format.parse("01-01-2022 09:50").getTime()),
                new Time(format.parse("01-01-2022 10:10").getTime()), 1))
                .isTrue();

        ReservationDAO.deleteOldReservation(999);
    }

    @Test
    public void testIsExistingReservationEqualFalse() throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-1999 00:00").getTime());

        assertThat(ReservationDAO.hasExistingReservation(date_reservation, new Time(format.parse("01-01-1999 09:50").getTime()),
                new Time(format.parse("01-01-1999 10:10").getTime()), 1))
                .isFalse();
    }

    @Test
    public void testUpdateMergeReservation() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);
        ReservationDAO.updateMergeReservation(reservation, new Time(format.parse("01-01-2022 13:30").getTime()));

        assertThat(ReservationDAO.getReservationById(999).getFin_intervalle().toLocalTime())
                .isEqualTo(new Time(format.parse("01-01-2022 13:30").getTime()).toLocalTime());

        ReservationDAO.deleteOldReservation(999);
    }

    @Test
    public void testUpdatePriceReservation() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        ReservationDAO.updatePriceReservation(reservation, 50);

        assertThat(ReservationDAO.getReservationById(999).getPrix())
                .isEqualTo(50);

        ReservationDAO.deleteOldReservation(999);
    }

    @Test
    public void testUpdateArriveeReservation() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        assertThat(ReservationDAO.getReservationById(999).getArrivee_client())
                .isNull();

        ReservationDAO.updateArriveeReservation(reservation);

        assertThat(ReservationDAO.getReservationById(999).getArrivee_client())
                .isNotNull();

        ReservationDAO.deleteOldReservation(999);
    }

    @Test
    public void testUpdateDepartReservation() throws SQLException, ParseException {
        EstAssocie estAssocie = EstAssocieDAO.getEstAssocieByClient(999).get(0);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date_reservation = new Date(format.parse("01-01-2022 00:00").getTime());

        Reservation reservation = new Reservation(999, date_reservation,
                new Time(format.parse("01-01-2022 10:00").getTime()),
                new Time(format.parse("01-01-2022 10:30").getTime()),
                0, false, estAssocie.getId_estAssocie(), 1);
        ReservationDAO.registrerReservationWithId(reservation);

        assertThat(ReservationDAO.getReservationById(999).getArrivee_client())
                .isNull();

        ReservationDAO.updateDepartReservation(reservation);

        assertThat(ReservationDAO.getReservationById(999).getDepart_client())
                .isNotNull();

        ReservationDAO.deleteOldReservation(999);
    }

    @AfterAll
    static void end() throws SQLException {
        NotificationDAO.deleteOldNotificationByClient(999);
        EstAssocieDAO.deleteOldEstAssocie(999, "plaque_test");
        ClientDAO.deleteClient(999);
        PlaqueDAO.deleteOldPlaque("plaque_test");
    }
}
