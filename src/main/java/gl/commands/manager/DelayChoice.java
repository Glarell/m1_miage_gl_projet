package gl.commands.manager;

import gl.application.Application;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class DelayChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner) {
        System.out.println("Définir le délai d'attente");
        return Application.RETURN_SUCCESS;
    }
}
