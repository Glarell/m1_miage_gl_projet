package gl.application;

import gl.commands.ChoicesAbstract;
import gl.database.model.Client;
import gl.ihm.Choices;

import java.util.Scanner;
import java.util.logging.Logger;

public class Application {

    public final static int STATE_ANONYMOUS = 0;
    public final static int STATE_CONNECTED = 1;
    public final static int STATE_MANAGER = 2;
    public final static int RETURN_SUCCESS = 0;
    public final static int RETURN_FAILED = 1;
    public final static int RETURN_QUIT = 2;
    public static Client currentClient;
    private static Scanner scanner;
    private final Logger logger = Logger.getLogger(Application.class.getName());
    private final Choices choices = new Choices();
    private final User currentUser;

    public Application() {
        scanner = new Scanner(System.in);
        this.currentUser = new User(STATE_ANONYMOUS);
    }

    public static String askForLine(String param) {
        System.out.println(param);
        while (true) {
            String line = scanner.nextLine();
            if (!line.isBlank() && !line.isEmpty()) {
                System.out.println("--------");
                return line;
            }
            System.out.println("Veuillez saisir des caractères !");
        }
    }

    public static int askForIntegerLine(String param) {
        System.out.println(param);
        while (true) {
            try {
                System.out.println("---------");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez saisir un entier !");
            }
        }
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

    public int parseOptions(int line) {
        ChoicesAbstract choice = this.choices.getFunction(line, currentUser.getState());
        if (choice != null) {
            return choice.execute(scanner, currentUser);
        } else {
            System.out.println("Aucune option trouvée, veuillez réésayez svp");
            return RETURN_FAILED;
        }
    }

    public static int getCurrentClientId() {
        return currentClient.getId_client();
    }
}
