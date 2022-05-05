package gl.commands;

import gl.application.Application;

import java.util.Scanner;

public class DeconnectionChoice extends ChoicesAbstract{
    @Override
    public int execute(Scanner scanner) {
        System.out.println("Me déconnecter");
        return Application.RETURN_SUCCESS;
    }
}
