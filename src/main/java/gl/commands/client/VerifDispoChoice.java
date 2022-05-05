package gl.commands.client;

import gl.application.Application;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class VerifDispoChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner) {
        System.out.println("Vérifier la disponibilité des bornes");
        return Application.RETURN_SUCCESS;
    }
}
