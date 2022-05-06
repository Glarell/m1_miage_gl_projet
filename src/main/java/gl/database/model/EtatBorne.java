package gl.database.model;

public class EtatBorne {

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
        return "EtatBorne{" +
                "id_etatBorne='" + id_etatBorne + '\'' +
                '}';
    }
}
