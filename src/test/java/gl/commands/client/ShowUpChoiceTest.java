package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.ReservationDAO;
import gl.database.model.Abonnement;
import gl.database.model.Reservation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;

public class ShowUpChoiceTest {

    private MockedStatic<Application> applicationMock;
    private MockedStatic<ReservationDAO> reservationDAOMock;
    private MockedStatic<AbonnementDAO> abonnementDAOMock;
    private MockedStatic<ShowUpChoice> showUpChoiceMock;

    @BeforeEach
    public void init() {
        applicationMock = mockStatic(Application.class);
        reservationDAOMock = mockStatic(ReservationDAO.class);
        abonnementDAOMock = mockStatic(AbonnementDAO.class);
        showUpChoiceMock = mockStatic(ShowUpChoice.class);
    }

    @Test
    public void testShowUpChoiceWithExistingReservation() {
        reservationDAOMock.when(() -> ReservationDAO.getReservationFromCurrentDate(anyInt())).thenReturn(new Reservation(10, null, null, null, 0, false, -1, -1));

        ShowUpChoice showUpChoice = new ShowUpChoice();
        assertThat(showUpChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_SUCCESS);
    }

    @Test
    public void testShowUpChoiceWithExistingAbonnement() {
        reservationDAOMock.when(() -> ReservationDAO.getReservationFromCurrentDate(anyInt())).thenReturn(new Reservation());
        abonnementDAOMock.when(() -> AbonnementDAO.getAbonnementFromCurrentDate(anyInt())).thenReturn(new Abonnement(10, null, null, null, -1, -1));

        ShowUpChoice showUpChoice = new ShowUpChoice();
        assertThat(showUpChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_SUCCESS);
    }

    @Test
    public void testShowUpChoiceWithInexistingReservationAndAbonnement() {
        reservationDAOMock.when(() -> ReservationDAO.getReservationFromCurrentDate(anyInt())).thenReturn(new Reservation());
        abonnementDAOMock.when(() -> AbonnementDAO.getAbonnementFromCurrentDate(anyInt())).thenReturn(new Abonnement());

        ShowUpChoice showUpChoice = new ShowUpChoice();
        assertThat(showUpChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
    }

    @AfterEach
    public void end() {
        applicationMock.close();
        reservationDAOMock.close();
        abonnementDAOMock.close();
        showUpChoiceMock.close();
    }
}
