package gl.commands.client;

import gl.application.Application;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class ModifReservationChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner) {
        System.out.println("Modifier / Annuler ma réservation");
        return Application.RETURN_SUCCESS;
    }
}
