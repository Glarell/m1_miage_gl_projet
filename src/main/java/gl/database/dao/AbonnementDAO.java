package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Abonnement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AbonnementDAO {

    public static Abonnement getAbonnementById(int id) {
        Abonnement transaction = new Abonnement();
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM abonnement where id_abonnement=?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setAbonnementAttributes(res, transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static Abonnement registrerAbonnement(Abonnement abonnement) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Abonnement (date_abonnement,debut_intervalle,fin_intervalle,id_client,id_borne) VALUES (?,?,?,?,?)");
        stmt.setDate(1, abonnement.getDate_abonnement());
        stmt.setTime(2, abonnement.getDebut_intervalle());
        stmt.setTime(3, abonnement.getFin_intervalle());
        stmt.setInt(4, abonnement.getId_client());
        stmt.setInt(5, abonnement.getId_borne());
        stmt.executeUpdate();
        return abonnement;
    }

    private static void setAbonnementAttributes(ResultSet res, Abonnement abonnement) throws SQLException {
        abonnement.setId_abonnement(res.getInt(1));
        abonnement.setDate_abonnement(res.getDate(2));
        abonnement.setDebut_intervalle(res.getTime(3));
        abonnement.setFin_intervalle(res.getTime(4));
        abonnement.setId_client(res.getInt(5));
        abonnement.setId_borne(res.getInt(6));
    }
}
