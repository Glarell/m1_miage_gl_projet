package gl.application;

import gl.commands.anonymous.InscriptionChoice;
import gl.commands.client.VerifDispoChoice;
import gl.ihm.Choices;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Choices")
public class TestChoices {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testGetFunction() {
        User user = new User(Application.STATE_ANONYMOUS);
        Choices choices = new Choices();
        assertThat(choices.getFunction(1, user.getState())).hasSameClassAs(new InscriptionChoice());
        user.setState(Application.STATE_CONNECTED);
        assertThat(choices.getFunction(1, user.getState())).hasSameClassAs(new VerifDispoChoice());
    }

    @Test
    public void testDisplay() {
        User user = new User(Application.STATE_ANONYMOUS);
        Choices choices = new Choices();
        choices.display(user.getState());
        assertThat(outContent.toString().startsWith("1 - M'inscrire"));
        assertThat(outContent.toString().endsWith("3 - Quitter l'application"));
        user.setState(Application.STATE_CONNECTED);
        choices.display(user.getState());
        assertThat(outContent.toString().startsWith("1 - Vérifier la disponibilité des bornes"));
        assertThat(outContent.toString().endsWith("10 - Quitter l'application"));
        user.setState(Application.STATE_MANAGER);
        choices.display(user.getState());
        assertThat(outContent.toString().startsWith("1 - Définir le délai d'attente"));
        assertThat(outContent.toString().endsWith("5 - Quitter l'application"));
    }
}
