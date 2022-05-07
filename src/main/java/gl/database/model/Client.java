package gl.database.model;

public class Client {

    private int id_client = -1;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;
    private String mdp;
    private String carte;

    public Client() {}

    public Client(String nom, String prenom, String adresse, String telephone, String email, String mdp, String carte) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.mdp = mdp;
        this.carte = carte;
    }

    public boolean isNotReal() {
        return this.id_client==-1;
    }

    public String getNom() {
        return nom;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCarte() {
        return carte;
    }

    public void setCarte(String carte) {
        this.carte = carte;
    }

    @Override
    public String toString() {
        return String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s'",nom,prenom,adresse,telephone,email,mdp,carte);
    }
}
