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

    public static List<EtatBorne> GetEtatBorne() {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<EtatBorne> listOfEtatBorne = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM EtatBorne");
            while (res.next()) {
                EtatBorne etatBorne = new EtatBorne();
                etatBorne.setId_etatBorne(res.getString(1));
                listOfEtatBorne.add(etatBorne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfEtatBorne;
    }
}
