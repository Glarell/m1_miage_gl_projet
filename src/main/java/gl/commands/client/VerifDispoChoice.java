package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;


import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.logging.Logger;

public class VerifDispoChoice extends ChoicesAbstract {

    private final Logger logger = Logger.getLogger(VerifDispoChoice.class.getName());

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Disponibilité des bornes------]\n");
        String dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
        boolean condition = true;
        while(condition){
            if (dateString.matches("\\d{2}-\\d{2}-\\d{4}")){
                try{
                    Date date = new Date(new SimpleDateFormat("dd-MM-yyyy").parse(dateString).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("La date n'est pas valide");
                    dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
                }
            }else{
                System.out.println("La date saisie ne respecte pas le format dd-mm-yyyy");
                dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
            }
        }
        String int1 = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
        condition = true;
        while(condition){
            if (int1.matches("\\d{2}:\\d{2}")){
                try{
                    Time time = new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString + " " + int1).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("L'intervalle n'est pas valide");
                    int1 = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
                }
            }else{
                System.out.println("L'intervalle saisi ne respecte pas le format hh:mm");
                dateString = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
            }
        }
        return Application.RETURN_SUCCESS;
    }
}
