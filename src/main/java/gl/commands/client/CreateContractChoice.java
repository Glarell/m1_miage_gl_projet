package gl.commands.client;

import gl.application.Application;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class CreateContractChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner) {
        System.out.println("Conclure un contrat mensuel");
        return Application.RETURN_SUCCESS;
    }
}
