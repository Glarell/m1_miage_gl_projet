package gl.commands.client;

import gl.application.Application;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class getUserDataUtils {

    public static Date getDate() {
        Date date_reservation = null;
        String dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
        boolean condition = true;
        while (condition) {
            if (dateString.matches("\\d{2}-\\d{2}-\\d{4}")) {
                try {
                    date_reservation = new Date(new SimpleDateFormat("dd-MM-yyyy").parse(dateString).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("La date n'est pas valide");
                    dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
                }
            } else {
                System.out.println("La date saisie ne respecte pas le format dd-mm-yyyy");
                dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
            }
        }
        return date_reservation;
    }

    public static Time getDebutIntervalle(String date) {
        Time debut_intervalle = null;
        String startTime = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
        boolean condition = true;
        while (condition) {
            if (startTime.matches("\\d{2}:\\d{2}")) {
                try {
                    debut_intervalle = new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date + " " + startTime).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("L'intervalle n'est pas valide");
                    startTime = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
                }
            } else {
                System.out.println("L'intervalle saisi ne respecte pas le format hh:mm");
                startTime = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
            }
        }
        return debut_intervalle;
    }

    public static Time getFinIntervalle(String date) {
        Time fin_intervalle = null;
        String endTime = Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ");
        boolean condition = true;
        while (condition) {
            if (endTime.matches("\\d{2}:\\d{2}")) {
                try {
                    fin_intervalle = new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date + " " + endTime).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("L'intervalle n'est pas valide");
                    endTime = Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ");
                }
            } else {
                System.out.println("L'intervalle saisi ne respecte pas le format hh:mm");
                endTime = Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ");
            }
        }
        return fin_intervalle;
    }
}
