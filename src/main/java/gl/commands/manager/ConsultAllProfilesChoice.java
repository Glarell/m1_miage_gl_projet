package gl.commands.manager;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class ConsultAllProfilesChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("Consulter les profils des clients");
        return Application.RETURN_SUCCESS;
    }
}
