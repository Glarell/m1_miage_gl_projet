package gl.application;

import gl.commands.client.GetUserDataUtils;
import gl.commands.client.ModifReservationChoice;
import gl.commands.client.VerifDispoChoice;
import gl.database.dao.*;
import gl.database.model.Client;
import gl.database.model.EstAssocie;
import gl.database.model.Plaque;
import gl.database.model.Reservation;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.ErrorCollector;
import org.mockito.MockedStatic;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

@DisplayName("ModifReservationChoice")

public class TestModifReservationChoice {

    @Rule
    public ErrorCollector collector = new ErrorCollector();
    private MockedStatic<Application> applicationMock;
    private MockedStatic<GetUserDataUtils> getuserdatautilsMock;
    private MockedStatic<VerifDispoChoice> verifDispoChoiceMock;

    private Client client;
    private EstAssocie estAssocie;
    private Reservation reservation;

    @BeforeEach
    public void init(){
        applicationMock = mockStatic(Application.class);
        getuserdatautilsMock = mockStatic(GetUserDataUtils.class);
        verifDispoChoiceMock = mockStatic(VerifDispoChoice.class);
        try{
            PlaqueDAO.insertNewPlaque("AA-123-BB");
            this.client = new Client("test", "test", "test", "test", "test", "test", "test");
            this.client = ClientDAO.registrerClient(client);
            EstAssocieDAO.insertNewEstAssocie(new EstAssocie(client.getId_client(),"AA-123-BB"));
            List<EstAssocie> temp = EstAssocieDAO.getEstAssocieByClientAndPlaque(client.getId_client(), "AA-123-BB");
            this.estAssocie = temp.get(0);
            SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat time = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            this.reservation = new Reservation(new Date(date.parse("28-05-2050").getTime()),new Time(time.parse("28-05-2050 10:00").getTime()),new Time(time.parse("28-05-2050 12:00").getTime()),0,false, temp.get(0).getId_estAssocie(), 1);
            this.reservation.setId_reservation(ReservationDAO.registrerReservation(this.reservation));
        }catch (SQLException | ParseException e){
            collector.addError(e);
        }
    }

    @AfterEach
    public void after(){
        applicationMock.close();
        verifDispoChoiceMock.close();
        getuserdatautilsMock.close();
        try{
            ReservationDAO.deleteReservationById(reservation.getId_reservation());
            EstAssocieDAO.deleteOldEstAssocie(client.getId_client(), "AA-123-BB");
            PlaqueDAO.deleteOldPlaque("AA-123-BB");
            ClientDAO.deleteClient(client.getId_client());
            EstAssocieDAO.deleteOldEstAssocie(client.getId_client(), "AA-123-AA");
            PlaqueDAO.deleteOldPlaque("AA-123-AA");
        }catch (SQLException e){
            collector.addError(e);
        }
    }

    @Test
    public void testDeleteReservation(){
        applicationMock.when(Application::getCurrentClientId).thenReturn(client.getId_client());
        applicationMock.when(() -> Application.askForIntegerLine("Veuiller saisir le numéro de la réservation que vous souhaitez modifier : ")).thenReturn(reservation.getId_reservation());
        applicationMock.when(() -> Application.askForIntegerLine("Veuillez saisir le numéro du choix que vous souhaitez effectuer : ")).thenReturn(1);
        ModifReservationChoice modifReservationChoice = new ModifReservationChoice();
        assertThat(modifReservationChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED))).isEqualTo(Application.RETURN_SUCCESS);
    }

    @Test
    public void testModifyDateReservation(){
        applicationMock.when(Application::getCurrentClientId).thenReturn(client.getId_client());
        applicationMock.when(() -> Application.askForIntegerLine("Veuiller saisir le numéro de la réservation que vous souhaitez modifier : ")).thenReturn(reservation.getId_reservation());
        applicationMock.when(() -> Application.askForIntegerLine("Veuillez saisir le numéro du choix que vous souhaitez effectuer : ")).thenReturn(2);
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat time = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try{
            getuserdatautilsMock.when(GetUserDataUtils::getDate).thenReturn(new Date(date.parse("28-05-2050").getTime()));
            getuserdatautilsMock.when(() -> GetUserDataUtils.getDebutIntervalle("28-05-2050")).thenReturn(new Time(time.parse("28-05-2050 11:00").getTime()));
            getuserdatautilsMock.when(() -> GetUserDataUtils.getFinIntervalle("28-05-2050")).thenReturn(new Time(time.parse("28-05-2050 14:00").getTime()));
            applicationMock.when(() -> Application.askForLine("Saissisez le nom de la borne à réserver !")).thenReturn("2");
            ModifReservationChoice modifReservationChoice = new ModifReservationChoice();
            assertThat(modifReservationChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED))).isEqualTo(Application.RETURN_SUCCESS);
        }catch (ParseException e){
            collector.addError(e);
        }
    }

    @Test
    public void testModifyPlaque(){
        applicationMock.when(Application::getCurrentClientId).thenReturn(client.getId_client());
        applicationMock.when(() -> Application.askForIntegerLine("Veuiller saisir le numéro de la réservation que vous souhaitez modifier : ")).thenReturn(reservation.getId_reservation());
        applicationMock.when(() -> Application.askForIntegerLine("Veuillez saisir le numéro du choix que vous souhaitez effectuer : ")).thenReturn(3);
        verifDispoChoiceMock.when(VerifDispoChoice::isVoitureEmprunteOuLoue).thenReturn(false);
        try{
            PlaqueDAO.insertNewPlaque("AA-123-AA");
            EstAssocieDAO.insertNewEstAssocie(new EstAssocie(client.getId_client(),"AA-123-AA"));
            applicationMock.when(() -> Application.askForLine("Saissisez le nom de la plaque")).thenReturn("AA-123-AA");
            ModifReservationChoice modifReservationChoice = new ModifReservationChoice();
            assertThat(modifReservationChoice.execute(new Scanner(System.in),new User(Application.STATE_CONNECTED))).isEqualTo(Application.RETURN_SUCCESS);
        }catch (SQLException e){
            collector.addError(e);
        }
    }

    @Test
    public void testQuit(){
        applicationMock.when(Application::getCurrentClientId).thenReturn(client.getId_client());
        System.out.println(reservation.getId_reservation());
        applicationMock.when(() -> Application.askForIntegerLine("Veuiller saisir le numéro de la réservation que vous souhaitez modifier : ")).thenReturn(reservation.getId_reservation());
        applicationMock.when(() -> Application.askForIntegerLine("Veuillez saisir le numéro du choix que vous souhaitez effectuer : ")).thenReturn(4);
        ModifReservationChoice modifReservationChoice = new ModifReservationChoice();
        assertThat(modifReservationChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED))).isEqualTo(Application.RETURN_SUCCESS);
    }
}
