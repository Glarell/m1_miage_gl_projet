package gl.commands.manager;

import gl.application.Application;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class PriceChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner) {
        System.out.println("Définir le montant des frais supplémentaires");
        return Application.RETURN_SUCCESS;
    }
}
