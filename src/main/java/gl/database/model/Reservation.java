package gl.database.model;

import java.sql.Date;
import java.sql.Time;

public class Reservation {

    private int id_reservation;
    private Date date_reservation;
    private Time debut_intervalle;
    private Time fin_intervalle;
    private int nb_prolongement;
    private boolean isSupplement;
    private boolean isTemporaire;
    private int id_estAssocie;
    private int id_borne;

    private Time arrivee_client;

    private Time depart_client;

    private float prix;

    public Reservation() {
        this.id_reservation = -1;
    }

    public Reservation(int id_reservation, Date date_reservation, Time debut_intervalle, Time fin_intervalle, int nb_prolongement, boolean isSupplement, int id_estAssocie, int id_borne) {
        this.id_reservation = id_reservation;
        this.date_reservation = date_reservation;
        this.debut_intervalle = debut_intervalle;
        this.fin_intervalle = fin_intervalle;
        this.nb_prolongement = nb_prolongement;
        this.isSupplement = isSupplement;
        this.id_estAssocie = id_estAssocie;
        this.id_borne = id_borne;
    }

    public Reservation(Date date_reservation, Time debut_intervalle, Time fin_intervalle, int nb_prolongement, boolean isSupplement, int id_estAssocie, int id_borne) {
        this.date_reservation = date_reservation;
        this.debut_intervalle = debut_intervalle;
        this.fin_intervalle = fin_intervalle;
        this.nb_prolongement = nb_prolongement;
        this.isSupplement = isSupplement;
        this.id_estAssocie = id_estAssocie;
        this.id_borne = id_borne;
    }


    public int getId_reservation() {
        return id_reservation;
    }

    public void setId_reservation(int id_reservation) {
        this.id_reservation = id_reservation;
    }

    public Date getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(Date date_reservation) {
        this.date_reservation = date_reservation;
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

    public int getNb_prolongement() {
        return nb_prolongement;
    }

    public void setNb_prolongement(int nb_prolongement) {
        this.nb_prolongement = nb_prolongement;
    }

    public boolean isSupplement() {
        return isSupplement;
    }

    public void setSupplement(boolean supplement) {
        isSupplement = supplement;
    }

    public int getId_estAssocie() {
        return id_estAssocie;
    }

    public void setId_estAssocie(int id_estAssocie) {
        this.id_estAssocie = id_estAssocie;
    }

    public int getId_borne() {
        return id_borne;
    }

    public void setId_borne(int id_borne) {
        this.id_borne = id_borne;
    }

    public boolean isTemporaire() {
        return isTemporaire;
    }

    public void setTemporaire(boolean temporaire) {
        isTemporaire = temporaire;
    }

    public Time getArrivee_client() {
        return arrivee_client;
    }

    public void setArrivee_client(Time arrivee_client) {
        this.arrivee_client = arrivee_client;
    }

    public Time getDepart_client() {
        return depart_client;
    }

    public void setDepart_client(Time depart_client) {
        this.depart_client = depart_client;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return String.format("Réservation (%s) :\ndate: %s\nintervalle: %s -> %s\nborne: %s", id_reservation, date_reservation, debut_intervalle, fin_intervalle, id_borne);
    }
}
