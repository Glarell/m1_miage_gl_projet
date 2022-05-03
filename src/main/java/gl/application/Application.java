package gl.application;

import gl.ihm.Choices;
import gl.ihm.ChoicesAbstract;

import java.util.Scanner;

public class Application {

    private Scanner scanner;
    private Choices choices = new Choices();

    public Application() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            choices.displayChoicesAnonymous();
            int line = askForIntegerLine("Veuillez saisir le numéro de l'action que vous souhaitez effectuer : \n_");
            parseOptions(line);
        }
    }

    public String askForLine(String param) {
        System.out.println(param);
        return scanner.nextLine();
    }

    public int askForIntegerLine(String param) {
        System.out.println(param);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void parseOptions(int line) {
        ChoicesAbstract choice = this.choices.getFunction(line);
        if (choice != null) {
            choice.execute(scanner);
        }
    }
}
