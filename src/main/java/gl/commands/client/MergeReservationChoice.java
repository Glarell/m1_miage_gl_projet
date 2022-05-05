package gl.commands.client;

import gl.application.Application;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class MergeReservationChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner) {
        System.out.println("Fusionner des réservations");
        return Application.RETURN_SUCCESS;
    }
}
