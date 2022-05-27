package gl.application;

import gl.commands.client.DonnerReleveMensuel;
import gl.database.dao.ClientDAO;
import gl.database.dao.NotificationDAO;
import gl.database.dao.ReservationDAO;
import gl.database.model.Notification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

public class TestDonnerReleveMensuel {

    private MockedStatic<Application> applicationMock;
    private MockedStatic<ClientDAO> clientDAOMock;
    private MockedStatic<ReservationDAO> reservationDAOMock;
    private MockedStatic<DonnerReleveMensuel> donnerReleveMensuelMock;

    private MockedStatic<NotificationDAO> notificationDAOMock;

    @BeforeEach
    public void init() {
        applicationMock = mockStatic(Application.class);
        clientDAOMock = mockStatic(ClientDAO.class);
        donnerReleveMensuelMock = mockStatic(DonnerReleveMensuel.class);
        reservationDAOMock = mockStatic(ReservationDAO.class);
        notificationDAOMock = mockStatic(NotificationDAO.class);
    }

    @Test
    public void testDonnerReleveSuccess() {
        reservationDAOMock.when(ReservationDAO::getReleveMensuel).thenReturn(new double[]{175.0, 2.0});
        notificationDAOMock.when(() -> NotificationDAO.insertNewNotification(any(Notification.class))).then(x -> null);
        DonnerReleveMensuel donnerReleveMensuel = new DonnerReleveMensuel();
        assertThat(donnerReleveMensuel.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_SUCCESS);
    }

    @Test
    public void testDonnerReleveFailed() {
        reservationDAOMock.when(ReservationDAO::getReleveMensuel).thenReturn(new double[]{-175.0, 2.0});
        DonnerReleveMensuel donnerReleveMensuel = new DonnerReleveMensuel();
        notificationDAOMock.when(() -> NotificationDAO.insertNewNotification(any(Notification.class))).then(x -> null);
        assertThat(donnerReleveMensuel.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
        reservationDAOMock.when(ReservationDAO::getReleveMensuel).thenReturn(new double[]{175.0, -2.0});
        donnerReleveMensuel = new DonnerReleveMensuel();
        assertThat(donnerReleveMensuel.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
        reservationDAOMock.when(ReservationDAO::getReleveMensuel).thenReturn(new double[]{-175.0, -2.0});
        donnerReleveMensuel = new DonnerReleveMensuel();
        assertThat(donnerReleveMensuel.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
    }

    @AfterEach
    public void end() {
        applicationMock.close();
        clientDAOMock.close();
        donnerReleveMensuelMock.close();
    }
}
