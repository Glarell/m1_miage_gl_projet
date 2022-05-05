package gl.commands.anonymous;

import gl.application.Application;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;

public class InscriptionChoice extends ChoicesAbstract {

    public InscriptionChoice(){

    }

    @Override
    public int execute(Scanner scanner) {
        System.out.println("Inscription \n");
        return Application.RETURN_SUCCESS;
    }
}
