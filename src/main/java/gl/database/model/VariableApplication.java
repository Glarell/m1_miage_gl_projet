package gl.database.model;

public class VariableApplication {

    public final static String WAITING_TIME = "délai attente";

    private int id_variableApplication = -1;
    private String nom;
    private int valeur;

    public VariableApplication() {
    }

    public VariableApplication(String nom, int valeur) {
        this.nom = nom;
        this.valeur = valeur;
    }

    public int getId_variableApplication() {
        return id_variableApplication;
    }

    public void setId_variableApplication(int id_variableApplication) {
        this.id_variableApplication = id_variableApplication;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return String.format("'%s', '%s', '%s'", id_variableApplication, nom, valeur);
    }
}
