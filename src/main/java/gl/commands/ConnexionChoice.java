package gl.commands;

import gl.application.Application;
import gl.application.User;

import java.util.Scanner;

public class ConnexionChoice extends ChoicesAbstract {

    public ConnexionChoice() {

    }

    @Override
    public int execute(Scanner scanner, User user) {
        user.setState(Application.STATE_CONNECTED);
        return Application.RETURN_SUCCESS;
    }
}
