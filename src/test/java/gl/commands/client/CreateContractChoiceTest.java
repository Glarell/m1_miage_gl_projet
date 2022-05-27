package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.ClientDAO;
import gl.database.model.Abonnement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;

public class CreateContractChoiceTest {

    private MockedStatic<Application> applicationMock;
    private MockedStatic<ClientDAO> clientDAOMock;
    private MockedStatic<AbonnementDAO> abonnementDAOMock;
    private MockedStatic<CreateContractChoice> createContractChoiceMock;

    @BeforeEach
    public void init() {
        applicationMock = mockStatic(Application.class);
        clientDAOMock = mockStatic(ClientDAO.class);
        abonnementDAOMock = mockStatic(AbonnementDAO.class);
        createContractChoiceMock = mockStatic(CreateContractChoice.class);
    }

    @Test
    public void testCreateContractWithLimitReached() {
        clientDAOMock.when(() -> ClientDAO.getNbReservationAndAbonnement(anyInt())).thenReturn(3);
        CreateContractChoice createContractChoice = new CreateContractChoice();
        assertThat(createContractChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
    }

    @Test
    public void testCreateContractWithIncorrectTime() {
        clientDAOMock.when(() -> ClientDAO.getNbReservationAndAbonnement(anyInt())).thenReturn(0);

        CreateContractChoice createContractChoice = new CreateContractChoice();
        assertThat(createContractChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
    }

    @Test
    public void testCreateContractWithExistingAbonnementAndDontMerge() {
        clientDAOMock.when(() -> ClientDAO.getNbReservationAndAbonnement(anyInt())).thenReturn(0);
        abonnementDAOMock.when(() -> AbonnementDAO.hasExistingAbonnementFromSameUser(anyInt(), any())).thenReturn(true);

        CreateContractChoice createContractChoice = new CreateContractChoice();
        assertThat(createContractChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
    }

    @Test
    public void testCreateContractWithExistingAbonnementAndMerge() {
        clientDAOMock.when(() -> ClientDAO.getNbReservationAndAbonnement(anyInt())).thenReturn(0);
        createContractChoiceMock.when(() -> CreateContractChoice.isIntervalInvalid(any(Abonnement.class))).thenReturn(false);
        createContractChoiceMock.when(() -> CreateContractChoice.doesUserWantToMergeAbonnement(any())).thenReturn(true);
        abonnementDAOMock.when(() -> AbonnementDAO.hasExistingAbonnementFromSameUser(anyInt(), any())).thenReturn(true);

        CreateContractChoice createContractChoice = new CreateContractChoice();
        assertThat(createContractChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_SUCCESS);
    }

    @Test
    public void testCreateContractWithInvalidBorne() {
        clientDAOMock.when(() -> ClientDAO.getNbReservationAndAbonnement(anyInt())).thenReturn(0);
        createContractChoiceMock.when(() -> CreateContractChoice.isIntervalInvalid(any(Abonnement.class))).thenReturn(false);
        createContractChoiceMock.when(() -> CreateContractChoice.getAllValidBorne(any(Abonnement.class))).thenReturn(Collections.emptyList());
        abonnementDAOMock.when(() -> AbonnementDAO.hasExistingAbonnementFromSameUser(anyInt(), any())).thenReturn(false);

        CreateContractChoice createContractChoice = new CreateContractChoice();
        assertThat(createContractChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
    }

    @AfterEach
    public void end() {
        applicationMock.close();
        clientDAOMock.close();
        abonnementDAOMock.close();
        createContractChoiceMock.close();
    }
}
