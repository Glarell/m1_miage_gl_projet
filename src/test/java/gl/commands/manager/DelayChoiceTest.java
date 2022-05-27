package gl.commands.manager;

import gl.application.Application;
import gl.application.User;
import gl.database.dao.VariableApplicationDAO;
import gl.database.model.VariableApplication;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;

public class DelayChoiceTest {

    @Test
    public void testChangeWaitingTime() {
        MockedStatic<VariableApplicationDAO> variableApplicationDAOMock = mockStatic(VariableApplicationDAO.class);
        variableApplicationDAOMock.when(() -> VariableApplicationDAO.getVariableApplicationByName(anyString())).thenReturn(new VariableApplication());

        MockedStatic<Application> applicationMock = mockStatic(Application.class);
        applicationMock.when(() -> Application.askForIntegerLine("Saisir le nouveau délai d'attente :")).thenReturn(5);

        DelayChoice delayChoice = new DelayChoice();
        assertThat(delayChoice.execute(new Scanner(System.in), new User(Application.STATE_CONNECTED)))
                .isEqualTo(Application.RETURN_SUCCESS);
    }
}
