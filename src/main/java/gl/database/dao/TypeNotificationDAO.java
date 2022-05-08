package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.TypeNotification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TypeNotificationDAO {

    /**
     * Récupération de tous les types possibles d'une notification présents dans la BDD
     *
     * @return la liste des types possibles
     */
    public static List<TypeNotification> getTypeNotification() {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<TypeNotification> listOfTypeNotification = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM TypeNotification");
            while (res.next()) {
                TypeNotification typeNotification = new TypeNotification();
                setTypeNotificationAttributes(res, typeNotification);
                listOfTypeNotification.add(typeNotification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfTypeNotification;
    }

    /**
     * Association des résultats d'une requête au type d'une notification
     *
     * @param res              la requête contenant les données
     * @param typeNotification le type d'une notification
     * @throws SQLException renvoie une exception
     */
    private static void setTypeNotificationAttributes(ResultSet res, TypeNotification typeNotification) throws SQLException {
        typeNotification.setId_typeNotification(res.getString(1));
    }
}
