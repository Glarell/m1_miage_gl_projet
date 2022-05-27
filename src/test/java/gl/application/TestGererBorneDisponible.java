package gl.application;

import gl.commands.client.CreateContractChoice;
import gl.commands.manager.GererBorneIndisponible;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.BorneDAO;
import gl.database.dao.ClientDAO;
import gl.database.model.Abonnement;
import gl.database.model.Borne;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;

public class TestGererBorneDisponible {

    private MockedStatic<Application> applicationMock;
    private MockedStatic<GererBorneIndisponible> gererBorneIndisponibleMock;

    private ArrayList<Borne> list = new ArrayList<>();

    private MockedStatic<BorneDAO> borneDAOMock;

    @BeforeEach
    public void init() {
        applicationMock = mockStatic(Application.class);
        gererBorneIndisponibleMock = mockStatic(GererBorneIndisponible.class);
        borneDAOMock = mockStatic(BorneDAO.class);
        for (int i=0; i<3; i++) {
            list.add(new Borne(i,"indisponible"));
        }
    }

    @Test
    public void testGererBorneSucceded() {
        borneDAOMock.when(() -> BorneDAO.getAllBorneFromState("indisponible")).thenReturn(list);
        applicationMock.when(() -> Application.askForIntegerLine("Saisir l'identifiant de la borne à gérer : ")).thenReturn(1);
        borneDAOMock.when(() -> BorneDAO.deleteAllReservationsToABorne(anyInt())).then(x -> null);
        GererBorneIndisponible gererBorneIndisponible = new GererBorneIndisponible();
        assertThat(gererBorneIndisponible.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_SUCCESS);
    }

    @Test
    public void testGererBorneFailed() {
        borneDAOMock.when(() -> BorneDAO.getAllBorneFromState("indisponible")).thenReturn(new ArrayList<>());
        GererBorneIndisponible gererBorneIndisponible = new GererBorneIndisponible();
        assertThat(gererBorneIndisponible.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_FAILED);
    }

    @AfterEach
    public void end() {
        applicationMock.close();
        gererBorneIndisponibleMock.close();
        borneDAOMock.close();
    }
}
