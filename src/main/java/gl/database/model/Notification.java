package gl.database.model;

public class Notification {

    private int id_notification = -1;
    private String contenu;
    private int id_client;
    private String id_typeNotification;

    public Notification() {
    }

    public Notification(int id_notification, String contenu, int id_client, String id_typeNotification) {
        this.id_notification = id_notification;
        this.contenu = contenu;
        this.id_client = id_client;
        this.id_typeNotification = id_typeNotification;
    }

    public Notification(String contenu, int id_client, String id_typeNotification) {
        this.contenu = contenu;
        this.id_client = id_client;
        this.id_typeNotification = id_typeNotification;
    }

    public int getId_notification() {
        return id_notification;
    }

    public void setId_notification(int id_notification) {
        this.id_notification = id_notification;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public String getId_typeNotification() {
        return id_typeNotification;
    }

    public void setId_typeNotification(String id_typeNotification) {
        this.id_typeNotification = id_typeNotification;
    }

    @Override
    public String toString() {
        return contenu;
    }
}
