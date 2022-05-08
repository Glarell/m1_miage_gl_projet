package gl.database.model;

public class EstAssocie {

    private int id_estAssocie = -1;
    private boolean isTemporaire;
    private int id_client;
    private String id_plaque;

    public EstAssocie() {
    }

    public EstAssocie(int id_estAssocie, boolean isTemporaire, int id_client, String id_plaque) {
        this.id_estAssocie = id_estAssocie;
        this.isTemporaire = isTemporaire;
        this.id_client = id_client;
        this.id_plaque = id_plaque;
    }

    public int getId_estAssocie() {
        return id_estAssocie;
    }

    public void setId_estAssocie(int id_estAssocie) {
        this.id_estAssocie = id_estAssocie;
    }

    public boolean isTemporaire() {
        return isTemporaire;
    }

    public void setTemporaire(boolean temporaire) {
        isTemporaire = temporaire;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public String getId_plaque() {
        return id_plaque;
    }

    public void setId_plaque(String id_plaque) {
        this.id_plaque = id_plaque;
    }

    @Override
    public String toString() {
        return String.format("'%s', '%s', '%s', '%s'", id_estAssocie, isTemporaire, id_client, id_plaque);
    }
}
