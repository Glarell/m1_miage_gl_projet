package gl.database.model;

public class EtatBorne {

    public final static String STATE_AVAILABLE = "disponible";
    public final static String STATE_UNAVAILABLE = "indisponible";
    public final static String STATE_RESERVED = "réservée";
    public final static String STATE_BUSY = "occupée";

    private String id_etatBorne;

    public EtatBorne() {
    }

    public String getId_etatBorne() {
        return id_etatBorne;
    }

    public void setId_etatBorne(String id_etatBorne) {
        this.id_etatBorne = id_etatBorne;
    }

    @Override
    public String toString() {
        return String.format("'%s'", id_etatBorne);
    }
}
