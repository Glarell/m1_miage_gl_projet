package gl.commands;

import gl.application.Application;
import gl.application.User;

import java.util.Scanner;

public class QuitChoice extends ChoicesAbstract {

    public QuitChoice() {

    }

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("Arrêt de l'application \n");
        Application.currentClient = null;
        return Application.RETURN_QUIT;
    }
}
