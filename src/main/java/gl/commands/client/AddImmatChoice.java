package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class AddImmatChoice extends ChoicesAbstract {

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("Ajouter une immatriculation");
        return Application.RETURN_SUCCESS;
    }
}
