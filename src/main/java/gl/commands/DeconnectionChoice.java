package gl.commands;

import gl.application.Application;
import gl.application.User;

import java.util.Scanner;

public class DeconnectionChoice extends ChoicesAbstract{
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("Me déconnecter");
        user.setState(Application.STATE_ANONYMOUS);
        return Application.RETURN_SUCCESS;
    }
}
