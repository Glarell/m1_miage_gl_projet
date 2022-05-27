package gl.application;

import gl.commands.client.CreateContractChoice;
import gl.commands.client.DonnerReleveMensuel;
import gl.commands.client.ProlongerReservationChoice;
import gl.commands.client.VerifDispoChoice;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.ClientDAO;
import gl.database.dao.ReservationDAO;
import gl.database.model.Reservation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;

public class TestPronlongerReservationChoice {
    private MockedStatic<Application> applicationMock;
    private MockedStatic<ClientDAO> clientDAOMock;

    private MockedStatic<ReservationDAO> reservationDAOMock;
    private MockedStatic<ProlongerReservationChoice> pronlongerReservationChoiceMock;

    private ArrayList<Reservation> list = new ArrayList<>();


    @BeforeEach
    public void init() {
        applicationMock = mockStatic(Application.class);
        clientDAOMock = mockStatic(ClientDAO.class);
        pronlongerReservationChoiceMock = mockStatic(ProlongerReservationChoice.class);
        reservationDAOMock=mockStatic(ReservationDAO.class);
        for (int i=1;i<4;i++) {
            Reservation reservation = ReservationDAO.getReservationById(i);
            list.add(reservation);
        }
    }
    @Test
    public void testProlongerSuccess() {
        System.out.println(list);
        reservationDAOMock.when(() -> ReservationDAO.getAllReservationByIdClient(anyInt())).thenReturn(list);
        applicationMock.when(() -> Application.askForLine("Entrer l'identifiant de la réservation :")).thenReturn("1");
        applicationMock.when(() -> Application.askForLine("Saisir durée supplémentaire (hh:mm) : ")).thenReturn("16:00");
        ProlongerReservationChoice prolongerReservationChoice = new ProlongerReservationChoice();
        assertThat(prolongerReservationChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_SUCCESS);
    }

    @Test
    public void testProlongerFailed() {
        reservationDAOMock.when(() -> ReservationDAO.getAllReservationByIdClient(anyInt())).thenReturn(new ArrayList<>());
        ProlongerReservationChoice prolongerReservationChoice = new ProlongerReservationChoice();
        assertThat(prolongerReservationChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
    }

    @AfterEach
    public void end() {
        applicationMock.close();
        clientDAOMock.close();
        pronlongerReservationChoiceMock.close();
        reservationDAOMock.close();
    }
}
