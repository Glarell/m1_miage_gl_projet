package gl.ihm;

import java.util.HashMap;

public class Choices{

    private HashMap<Integer,String> choices;
    private HashMap<Integer, ChoicesAbstract> commands;

    public Choices(){
        choices = new HashMap<>();
        commands = new HashMap<>();
        choices.put(1, "1 - M'inscrire");
        commands.put(1, new InscriptionChoice());
        choices.put(2, "2 - Me connecter");
        commands.put(2, new ConnexionChoice());
        choices.put(3, "3 - Quitter l'application");
    }

    public void displayChoicesAnonymous(){
        StringBuilder strBuilder = new StringBuilder();
        choices.forEach((k,v) ->{
            strBuilder.append(v).append("\n");
        });
        System.out.println(strBuilder.toString());
    }

    public ChoicesAbstract getFunction(int line){
        return this.commands.getOrDefault(line, null);
    }
}

