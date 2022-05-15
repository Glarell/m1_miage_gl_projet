package gl.application;

import gl.commands.client.VerifDispoChoice;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("VerifDispoChoice")
public class TestVerifDispoChoice {

    @Test
    public void testVerifDispoChoiceIncorrectDate() {
        MockedStatic<Application> mock = mockStatic(Application.class);
        VerifDispoChoice vdc = new VerifDispoChoice();
        mock.when(() -> Application.askForLine("Saisir la date (dd-mm-yyyy) : ")).thenReturn("14-05-2022");
        mock.when(() -> Application.askForLine("Saisir l'intervalle de début (hh:mm) : ")).thenReturn("16:00");
        mock.when(() -> Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ")).thenReturn("16:00");
        assertThat(vdc.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED))).isEqualTo(Application.RETURN_FAILED);
        mock.when(() -> Application.askForLine("Saisir l'intervalle de début (hh:mm) : ")).thenReturn("17:00");
        assertThat(vdc.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED))).isEqualTo(Application.RETURN_FAILED);
        mock.close();
    }

    @Test
    public void testCorrectDate(){
        MockedStatic<Application> mock = mockStatic(Application.class);
        VerifDispoChoice vdc = new VerifDispoChoice();
        mock.when(() -> Application.askForLine("Saisir la date (dd-mm-yyyy) : ")).thenReturn("14-05-2022");
        mock.when(() -> Application.askForLine("Saisir l'intervalle de début (hh:mm) : ")).thenReturn("10:00");
        mock.when(() -> Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ")).thenReturn("13:00");
        assertThat(vdc.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED))).isEqualTo(Application.RETURN_SUCCESS);
    }
}
