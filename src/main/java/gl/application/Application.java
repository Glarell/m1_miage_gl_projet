package gl.application;

import gl.ihm.Choices;
import gl.ihm.ChoicesAbstract;

import java.text.NumberFormat;
import java.util.Scanner;
import java.util.logging.Logger;

public class Application {

    private Logger logger = Logger.getLogger(Application.class.getName());

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
        String asking = "Veuillez saisir le numéro de l'action que vous souhaitez effectuer.";
        int line = 0;
        Boolean condition = true;
        while (condition) {
            switch(this.state){
                case(Application.STATE_ANONYMOUS):
                    choices.display(STATE_ANONYMOUS);
                    line = askForIntegerLine(asking);
                    condition = parseOptions(line);
                    break;
                case(Application.STATE_CONNECTED):
                    choices.display(STATE_CONNECTED);
                    line = askForIntegerLine(asking);
                    condition = parseOptions(line);
                    break;
                case(Application.STATE_MANAGER):
                    choices.display(STATE_MANAGER);
                    line = askForIntegerLine(asking);
                    condition = parseOptions(line);
                    break;
            }
        }
    }

    public String askForLine(String param) {
        System.out.println(param);
        return scanner.nextLine();
    }

    public int askForIntegerLine(String param) {
        System.out.println(param);
        while(true){
            try{
                int res = Integer.parseInt(scanner.nextLine());
                return res;
            }catch(NumberFormatException e){
                System.out.println("Veuillez saisir un entier !");
            }
        }
    }

    public Boolean parseOptions(int line) {
        ChoicesAbstract choice = this.choices.getFunction(line, state);
        if (choice != null) {
            choice.execute(scanner);
            return true;
        }
        System.out.println("Aucune option trouvée, veuillez réésayez svp");
        return true;
    }
}
