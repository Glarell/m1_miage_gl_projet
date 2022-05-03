package gl.application;

import gl.ihm.Choices;
import gl.ihm.ChoicesAbstract;

import java.util.Scanner;

public class Application {

    public final static int STATE_ANONYMOUS = 0;
    public final static int STATE_CONNECTED = 1;
    public final static int STATE_MANAGER = 2;

    private Scanner scanner;
    private Choices choices = new Choices();
    private int state;

    public Application() {
        this.scanner = new Scanner(System.in);
        this.state = STATE_ANONYMOUS;
    }

    public void start() {
        while (true) {
            switch(this.state){
                case(Application.STATE_ANONYMOUS):
                    choices.display(STATE_ANONYMOUS);
                    break;
                case(Application.STATE_CONNECTED):
                    choices.display(STATE_CONNECTED);
                    break;
                case(Application.STATE_MANAGER):
                    choices.display(STATE_MANAGER);
                    break;
            }
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
        ChoicesAbstract choice = this.choices.getFunction(line, state);
        if (choice != null) {
            choice.execute(scanner);
        }
    }
}
