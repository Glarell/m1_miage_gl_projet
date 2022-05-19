package gl.database.dao;

import gl.database.model.Transaction;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionDAOTest {

    @Test
    public void testGetTransactionById() {
        Transaction transaction = TransactionDAO.getTransactionById(1);
        assertThat(transaction.getDate_transaction()).isEqualTo("2021-08-30");
        assertThat(transaction.getId_client()).isEqualTo(1);
    }

    @Test
    public void testRegistrerTransaction() throws SQLException {
        Transaction transaction = new Transaction("contenu",new Date(0),1);
        TransactionDAO.registrerTransaction(transaction);
        assertThat(transaction.getId_transaction()).isNotEqualTo(-1);
    }

}
