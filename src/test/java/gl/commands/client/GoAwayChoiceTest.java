package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.ReservationDAO;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;

public class GoAwayChoiceTest {

    private MockedStatic<Application> applicationMock;
    private MockedStatic<ReservationDAO> reservationDAOMock;
    private MockedStatic<AbonnementDAO> abonnementDAOMock;
    private MockedStatic<GoAwayChoice> goAwayChoiceMock;

    @BeforeEach
    public void init() {
        applicationMock = mockStatic(Application.class);
        reservationDAOMock = mockStatic(ReservationDAO.class);
        abonnementDAOMock = mockStatic(AbonnementDAO.class);
        goAwayChoiceMock = mockStatic(GoAwayChoice.class);
    }

    @Test
    public void testGoAwayChoiceWithExistingReservation() {
        reservationDAOMock.when(() -> ReservationDAO.getAllReservationInProgressByIdClient(anyInt())).thenReturn(Lists.list(1));
        goAwayChoiceMock.when(GoAwayChoice::isReservationInProgress).thenReturn(true);

        GoAwayChoice goAwayChoice = new GoAwayChoice();
        assertThat(goAwayChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_SUCCESS);
    }

    @Test
    public void testGoAwayChoiceWithExistingAbonnement() {
        abonnementDAOMock.when(() -> AbonnementDAO.getAbonnementInProgressByClient(anyInt())).thenReturn(Lists.list(1));
        goAwayChoiceMock.when(GoAwayChoice::isReservationInProgress).thenReturn(false);

        GoAwayChoice goAwayChoice = new GoAwayChoice();
        assertThat(goAwayChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_SUCCESS);
    }

    @Test
    public void testGoAwayChoiceWithInexistingReservationAndAbonnement() {
        abonnementDAOMock.when(() -> AbonnementDAO.getAbonnementInProgressByClient(anyInt())).thenReturn(Collections.emptyList());
        goAwayChoiceMock.when(GoAwayChoice::isReservationInProgress).thenReturn(false);

        GoAwayChoice goAwayChoice = new GoAwayChoice();
        assertThat(goAwayChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
    }

    @AfterEach
    public void end() {
        applicationMock.close();
        reservationDAOMock.close();
        abonnementDAOMock.close();
        goAwayChoiceMock.close();
    }
}
