package gl.database.model;

import java.sql.Date;
import java.sql.Time;

public class Abonnement {

    private int id_abonnement;
    private Date date_abonnement;
    private Time debut_intervalle;
    private Time fin_intervalle;
    private int id_client;
    private int id_borne;
    private boolean inProgress;

    private float prix;

    public Abonnement() {
        this.id_abonnement = -1;
    }

    public Abonnement(Date date_abonnement, Time debut_intervalle, Time fin_intervalle, int id_client, int id_borne) {
        this.date_abonnement = date_abonnement;
        this.debut_intervalle = debut_intervalle;
        this.fin_intervalle = fin_intervalle;
        this.id_client = id_client;
        this.id_borne = id_borne;
    }

    public Abonnement(int id_abonnement, Date date_abonnement, Time debut_intervalle, Time fin_intervalle, int id_client, int id_borne) {
        this.id_abonnement = id_abonnement;
        this.date_abonnement = date_abonnement;
        this.debut_intervalle = debut_intervalle;
        this.fin_intervalle = fin_intervalle;
        this.id_client = id_client;
        this.id_borne = id_borne;
    }

    public int getId_abonnement() {
        return id_abonnement;
    }

    public void setId_abonnement(int id_abonnement) {
        this.id_abonnement = id_abonnement;
    }

    public Date getDate_abonnement() {
        return date_abonnement;
    }

    public void setDate_abonnement(Date date_abonnement) {
        this.date_abonnement = date_abonnement;
    }

    public Time getDebut_intervalle() {
        return debut_intervalle;
    }

    public void setDebut_intervalle(Time debut_intervalle) {
        this.debut_intervalle = debut_intervalle;
    }

    public Time getFin_intervalle() {
        return fin_intervalle;
    }

    public void setFin_intervalle(Time fin_intervalle) {
        this.fin_intervalle = fin_intervalle;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_borne() {
        return id_borne;
    }

    public void setId_borne(int id_borne) {
        this.id_borne = id_borne;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    @Override
    public String toString() {
        return String.format("'%s', '%s', '%s', '%s', '%s', '%s'", id_abonnement, date_abonnement, debut_intervalle, fin_intervalle, id_client, id_borne);
    }

}
