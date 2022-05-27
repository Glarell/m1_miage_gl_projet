package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.NotificationDAO;
import gl.database.dao.ReservationDAO;
import gl.database.model.Notification;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class DonnerReleveMensuel extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Affichage du relevé mensuel------]");
        try {
            double[] releve = ReservationDAO.getReleveMensuel();
            if (releve[1] < 0 || releve[0] < 0) {
                return Application.RETURN_FAILED;
            }
            LocalDate date = LocalDate.now();
            String content = String.format("\t[Relevé du mois : %s]\n" +
                    "\tLe nombre de réservations au cours de ce mois est de : %s\n" +
                    "\tMontant total à payer : %s\n" +
                    "[\\------------------------/]\n",date.getMonth(),releve[1],releve[0]);
            System.out.println(content);
            Notification notification = new Notification(content, Application.getCurrentClientId(), "Mail");
            NotificationDAO.insertNewNotification(notification);
            return Application.RETURN_SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Application.RETURN_FAILED;
    }
}
