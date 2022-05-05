package gl.commands;

import gl.application.Application;

import java.util.Scanner;

public class QuitChoice extends ChoicesAbstract{

    public QuitChoice(){

    }

    @Override
    public int execute(Scanner scanner){
        System.out.println("Arrêt de l'application \n");
        return Application.RETURN_QUIT;
    }
}
