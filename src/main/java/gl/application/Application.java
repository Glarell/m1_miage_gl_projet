package gl.application;

import gl.database.model.Client;
import gl.ihm.Choices;
import gl.commands.ChoicesAbstract;

import java.util.Scanner;
import java.util.logging.Logger;

public class Application {

    public final static int STATE_ANONYMOUS = 0;
    public final static int STATE_CONNECTED = 1;
    public final static int STATE_MANAGER = 2;
    public final static int RETURN_SUCCESS = 0;
    public final static int RETURN_FAILED = 1;
    public final static int RETURN_QUIT = 2;
    private final Logger logger = Logger.getLogger(Application.class.getName());
    private static Scanner scanner;
    private final Choices choices = new Choices();
    private final User currentUser;
    public static Client currentClient;

    public Application() {
        scanner = new Scanner(System.in);
        this.currentUser = new User(STATE_ANONYMOUS);
    }

    public void start() {
        String asking = "Veuillez saisir le numéro de l'action que vous souhaitez effectuer : ";
        int line;
        int condition = RETURN_SUCCESS;
        while (condition != RETURN_QUIT) {
            switch (this.currentUser.getState()) {
                case (Application.STATE_ANONYMOUS):
                    choices.display(STATE_ANONYMOUS);
                    line = askForIntegerLine(asking);
                    condition = parseOptions(line);
                    break;
                case (Application.STATE_CONNECTED):
                    choices.display(STATE_CONNECTED);
                    line = askForIntegerLine(asking);
                    condition = parseOptions(line);
                    break;
                case (Application.STATE_MANAGER):
                    choices.display(STATE_MANAGER);
                    line = askForIntegerLine(asking);
                    condition = parseOptions(line);
                    break;
            }
        }
    }

    public static String askForLine(String param) {
        System.out.println(param);
        return scanner.nextLine();
    }

    public static int askForIntegerLine(String param) {
        System.out.println(param);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez saisir un entier !");
            }
        }
    }

    public int parseOptions(int line) {
        ChoicesAbstract choice = this.choices.getFunction(line, currentUser.getState());
        if (choice != null) {
            return choice.execute(scanner, currentUser);
        }else{
            System.out.println("Aucune option trouvée, veuillez réésayez svp");
            return RETURN_FAILED;
        }
    }
}
