package gl.database.model;

import java.sql.Date;

public class Transaction {

    private int id_transaction;
    private String contenu;
    private Date date_transaction;
    private int id_client;

    public Transaction() { }

    public Transaction(int id_transaction, String contenu, Date date_transaction, int id_client) {
        this.id_transaction = id_transaction;
        this.contenu = contenu;
        this.date_transaction = date_transaction;
        this.id_client = id_client;
    }

    public int getId_transaction() {
        return id_transaction;
    }

    public void setId_transaction(int id_transaction) {
        this.id_transaction = id_transaction;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate_transaction() {
        return date_transaction;
    }

    public void setDate_transaction(Date date_transaction) {
        this.date_transaction = date_transaction;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }
}
