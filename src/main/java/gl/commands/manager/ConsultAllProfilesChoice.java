package gl.commands.manager;

import gl.application.Application;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class ConsultAllProfilesChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner) {
        System.out.println("Consulter les profils des clients");
        return Application.RETURN_SUCCESS;
    }
}
