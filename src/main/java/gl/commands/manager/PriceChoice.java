package gl.commands.manager;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.VariableApplicationDAO;
import gl.database.model.VariableApplication;

import java.sql.SQLException;
import java.util.Scanner;

public class PriceChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Définir le montant des frais supplémentaires------]\n");
        VariableApplication variableApplication = VariableApplicationDAO.getVariableApplicationByName(VariableApplication.ADDITIONAL_COST);
        System.out.println("le montant actuelle des frais supplémentaires est de " + variableApplication.getValeur() + "euros");

        int newValeur = Application.askForIntegerLine("Saisir le nouveau montant :");
        while (newValeur < 1) {
            newValeur = Application.askForIntegerLine("Mauvaise saisie !\nSaisir le nouveau montant :");
        }

        try {
            VariableApplicationDAO.updateVariableApplicationValue(variableApplication, newValeur);
        } catch (SQLException e) {
            System.out.println("Erreur : champs incompatables selon saisies !");
            this.execute(scanner, user);
        }

        return Application.RETURN_SUCCESS;
    }
}
