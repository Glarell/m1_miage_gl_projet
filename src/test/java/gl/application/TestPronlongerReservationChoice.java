package gl.application;

import gl.commands.client.CreateContractChoice;
import gl.commands.client.DonnerReleveMensuel;
import gl.commands.client.ProlongerReservationChoice;
import gl.commands.client.VerifDispoChoice;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.ClientDAO;
import gl.database.dao.ReservationDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;

public class TestPronlongerReservationChoice {
    private MockedStatic<Application> applicationMock;
    private MockedStatic<ClientDAO> clientDAOMock;
    private MockedStatic<ReservationDAO> reservationDAOMock;
    private MockedStatic<DonnerReleveMensuel> donnerReleveMensuelMock;

    @BeforeEach
    public void init() {
        applicationMock = mockStatic(Application.class);
        clientDAOMock = mockStatic(ClientDAO.class);
        donnerReleveMensuelMock = mockStatic(DonnerReleveMensuel.class);
    }
    @Test
    public void testProlongerSuccess() {


    }

    @Test
    public void testProlongerFailed() {
    }

    @AfterEach
    public void end() {
        applicationMock.close();
        clientDAOMock.close();
        donnerReleveMensuelMock.close();
    }
}
