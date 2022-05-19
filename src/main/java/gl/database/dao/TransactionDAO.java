package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Transaction;

import java.sql.*;

public class TransactionDAO {

    public static Transaction getTransactionById(int id) {
        Transaction transaction = new Transaction();
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transaction where id_transaction=?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setTransactionAttributes(res, transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static Transaction registrerTransaction(Transaction transaction) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Transaction (contenu,date_transaction,id_client) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, transaction.getContenu());
        stmt.setDate(2, transaction.getDate_transaction());
        stmt.setInt(3, transaction.getId_client());
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            int generatedKey = rs.getInt(1);
            transaction.setId_transaction(generatedKey);
        }
        return transaction;
    }

    private static void setTransactionAttributes(ResultSet res, Transaction transaction) throws SQLException {
        transaction.setId_transaction(res.getInt(1));
        transaction.setContenu(res.getString(2));
        transaction.setDate_transaction(res.getDate(3));
        transaction.setId_client(res.getInt(4));
    }
}
