package gl.commands.client;

import gl.application.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

public class GetUserDataUtilsTest {

    private MockedStatic<Application> applicationMock;

    @BeforeEach
    public void init() {
        applicationMock = mockStatic(Application.class);
    }

    @Test
    public void testGetDate() throws ParseException {
        applicationMock.when(() -> Application.askForLine("Saisir la date (dd-mm-yyyy) : ")).thenReturn("14-05-2022");

        assertThat(GetUserDataUtils.getDate())
                .isEqualTo(new Date(new SimpleDateFormat("dd-MM-yyyy").parse("14-05-2022").getTime()));
    }

    @Test
    public void testGetDebutIntervalle() throws ParseException {
        applicationMock.when(() -> Application.askForLine("Saisir l'intervalle de début (hh:mm) : ")).thenReturn("10:00");

        assertThat(GetUserDataUtils.getDebutIntervalle("14-05-2022"))
                .isEqualTo(new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("14-05-2022 10:00").getTime()));
    }

    @Test
    public void testGetFinIntervalle() throws ParseException {
        applicationMock.when(() -> Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ")).thenReturn("10:30");

        assertThat(GetUserDataUtils.getFinIntervalle("14-05-2022"))
                .isEqualTo(new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("14-05-2022 10:30").getTime()));
    }

    @AfterEach
    public void end() {
        applicationMock.close();
    }
}
