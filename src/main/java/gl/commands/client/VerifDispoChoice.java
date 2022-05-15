package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.BorneDAO;
import gl.database.model.Borne;


import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class VerifDispoChoice extends ChoicesAbstract {

    private final Logger logger = Logger.getLogger(VerifDispoChoice.class.getName());

    private String dateString;
    private Date date;
    private Time int1;
    private Time int2;

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Disponibilité des bornes------]");
        getDate();
        getInt1();
        getInt2();
        if (int1.compareTo(int2) >= 0) {
            System.out.println("Les intervalles ne sont pas bien renseignés !");
            return Application.RETURN_FAILED;
        }
        ArrayList<Integer> bornes_dispos = (ArrayList<Integer>) BorneDAO.getAllBorneFromDateDispo(date,int1,int2);
        if (bornes_dispos.size() > 0){
            System.out.println("Voici la ou les borne(s) disponible(s) :");
            bornes_dispos.forEach(x -> System.out.printf("-- Borne N°%d%n", x));
        }else {
            System.out.println("Aucune borne n'est disponible, essayez sur d'autres créneaux :)");
        }
        return Application.RETURN_SUCCESS;
    }

    public void getDate(){
        this.dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
        boolean condition = true;
        while(condition){
            if (dateString.matches("\\d{2}-\\d{2}-\\d{4}")){
                try{
                    this.date = new Date(new SimpleDateFormat("dd-MM-yyyy").parse(dateString).getTime());
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
    }

    public void getInt1(){
        String int1 = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
        boolean condition = true;
        while(condition){
            if (int1.matches("\\d{2}:\\d{2}")){
                try{
                    this.int1 = new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString + " " + int1).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("L'intervalle n'est pas valide");
                    int1 = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
                }
            }else{
                System.out.println("L'intervalle saisi ne respecte pas le format hh:mm");
                int1 = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
            }
        }
    }

    public void getInt2(){
        String int2 = Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ");
        boolean condition = true;
        while(condition){
            if (int2.matches("\\d{2}:\\d{2}")){
                try{
                    this.int2 = new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString + " " + int2).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("L'intervalle n'est pas valide");
                    int2 = Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ");
                }
            }else{
                System.out.println("L'intervalle saisi ne respecte pas le format hh:mm");
                int2 = Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ");
            }
        }
    }
}
