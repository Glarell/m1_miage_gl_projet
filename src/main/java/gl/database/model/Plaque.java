package gl.database.model;

public class Plaque {

    private String id_plaque;

    public Plaque() {
    }

    public Plaque(String id_plaque) {
        this.id_plaque = id_plaque;
    }

    public String getId_plaque() {
        return id_plaque;
    }

    public void setId_plaque(String id_plaque) {
        this.id_plaque = id_plaque;
    }

    @Override
    public String toString() {
        return String.format("'%s'", id_plaque);
    }
}
