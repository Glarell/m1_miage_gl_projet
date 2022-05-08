package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.EtatBorne;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EtatBorneDAO {

    /**
     * Récupération de tous les états possible d'une borne présents dans la BDD
     * @return la liste des états possible
     */
    public static List<EtatBorne> getEtatBorne() {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<EtatBorne> listOfEtatBorne = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM EtatBorne");
            while (res.next()) {
                EtatBorne etatBorne = new EtatBorne();
                setEtatBorneAttributes(res, etatBorne);
                listOfEtatBorne.add(etatBorne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfEtatBorne;
    }

    /**
     * Association des résultats d'une requête à un état de borne
     * @param res la requête contenant les données
     * @param etatBorne l'état de la borne
     * @throws SQLException renvoie une exception
     */
    private static void setEtatBorneAttributes(ResultSet res, EtatBorne etatBorne) throws SQLException {
        etatBorne.setId_etatBorne(res.getString(1));
    }
}
