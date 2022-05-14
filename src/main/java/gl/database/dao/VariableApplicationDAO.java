package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.VariableApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VariableApplicationDAO {

    /**
     * Récupération de toutes les variables d'application présentes dans la BDD
     *
     * @return la liste des variables d'application
     */
    public static List<VariableApplication> getAllVariableApplication() {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<VariableApplication> listOfVariableApplication = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM VariableApplication");
            while (res.next()) {
                VariableApplication variableApplication = new VariableApplication();
                setVariableApplicationAttributes(res, variableApplication);
                listOfVariableApplication.add(variableApplication);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfVariableApplication;
    }

    /**
     * Récupération de la variable d'application pour un nom donné dans la BDD
     *
     * @param nom le nom de la variable d'application recherché
     * @return la variable d'application
     */
    public static VariableApplication getVariableApplicationByName(String nom) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        VariableApplication variableApplication = new VariableApplication();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM VariableApplication WHERE nom = ?");
            stmt.setString(1, nom);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setVariableApplicationAttributes(res, variableApplication);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return variableApplication;
    }

    /**
     * Mise à jour de la valeur d'une variable d'application dans la BDD
     *
     * @param variableApplication la variable d'application à modifier
     * @param newValeur           la nouvelle valeur de la variable d'application
     * @throws SQLException renvoie une exception
     */
    public static void updateVariableApplicationValue(VariableApplication variableApplication, int newValeur) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE VariableApplication SET valeur = ? WHERE nom = ?;");
        getVariableApplicationAttributes(stmt, variableApplication, newValeur);
        stmt.executeUpdate();
    }

    /**
     * Association des résultats d'une requête à une variable d'application
     *
     * @param res                 la requête contenant les données
     * @param variableApplication la variable d'application
     * @throws SQLException renvoie une exception
     */
    private static void setVariableApplicationAttributes(ResultSet res, VariableApplication variableApplication) throws SQLException {
        variableApplication.setId_variableApplication(res.getInt(1));
        variableApplication.setNom(res.getString(2));
        variableApplication.setValeur(res.getInt(3));
    }

    /**
     * Affectation des données dans la requête
     *
     * @param stmt                la requête
     * @param variableApplication la variable d'application
     * @throws SQLException renvoie une exception
     */
    private static void getVariableApplicationAttributes(PreparedStatement stmt, VariableApplication variableApplication, int newValeur) throws SQLException {
        stmt.setInt(1, newValeur);
        stmt.setString(2, variableApplication.getNom());
        variableApplication.setValeur(newValeur);
    }
}
