package gl.database.model;

public class Borne {

    private int id_borne;
    private String id_etatBorne;

    public Borne() {
    }

    public Borne(int id_borne) {
        this.id_borne = id_borne;
    }

    public int getId_borne() {
        return id_borne;
    }

    public void setId_borne(int id_borne) {
        this.id_borne = id_borne;
    }

    public String getId_etatBorne() {
        return id_etatBorne;
    }

    public void setId_etatBorne(String id_etatBorne) {
        this.id_etatBorne = id_etatBorne;
    }

    @Override
    public String toString() {
        return String.format("'%s', '%s'", id_borne, id_etatBorne);
    }
}
