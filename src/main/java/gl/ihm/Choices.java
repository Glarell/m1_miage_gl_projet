package gl.ihm;

import gl.application.Application;

import java.util.HashMap;

public class Choices{

    private HashMap<Integer,String> choicesAnonymous = new HashMap<>();
    private HashMap<Integer,String> choicesConnected = new HashMap<>();
    private HashMap<Integer,String> choicesManager = new HashMap<>();
    private HashMap<Integer, ChoicesAbstract> commandsAnonymous = new HashMap<>();
    private HashMap<Integer, ChoicesAbstract> commandsConnected = new HashMap<>();
    private HashMap<Integer, ChoicesAbstract> commandsManager = new HashMap<>();


    public Choices(){
        choicesAnonymous.put(1, "1 - M'inscrire");
        choicesAnonymous.put(2, "2 - Me connecter");
        choicesAnonymous.put(3, "3 - Quitter l'application");
        commandsAnonymous.put(1, new InscriptionChoice());
        commandsAnonymous.put(2, new ConnexionChoice());
        choicesConnected = new HashMap<>();
        choicesManager = new HashMap<>();
        commandsConnected = new HashMap<>();
        commandsManager = new HashMap<>();
    }

    public void display(int state){
        StringBuilder strBuilder = new StringBuilder();
        switch(state){
            case(Application.STATE_ANONYMOUS):
                choicesAnonymous.forEach((k,v) ->{
                    strBuilder.append(v).append("\n");
                });
                break;
            case(Application.STATE_CONNECTED):
                choicesConnected.forEach((k,v) -> {
                    strBuilder.append(v).append("\n");
                });
                break;
            case(Application.STATE_MANAGER):
                choicesManager.forEach((k,v) -> {
                    strBuilder.append(v).append("\n");
                });
                break;
        }
        System.out.println(strBuilder.toString());
    }

    public ChoicesAbstract getFunction(int line, int state){
        switch(state){
            case(Application.STATE_ANONYMOUS):
                return commandsAnonymous.getOrDefault(line,null);
            case(Application.STATE_CONNECTED):
                return commandsConnected.getOrDefault(line,null);
            case(Application.STATE_MANAGER):
                return commandsManager.getOrDefault(line, null);
            default:
                return null;
        }
    }
}

