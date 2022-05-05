package gl.commands;

import gl.application.Application;

import java.util.Scanner;

public class ConnexionChoice extends ChoicesAbstract {

    public ConnexionChoice() {

    }

    @Override
    public int execute(Scanner scanner) {
        System.out.println("Connexion \n");
        return Application.RETURN_SUCCESS;
    }
}
