package de.hsrm.mi.web.projekt.entities.doener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hsrm.mi.web.projekt.entities.zutat.Zutat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
public class Doener implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;

    @NotBlank(message = "Bezeichnung darf nicht leer sein")
    @Size(min = 1, max = 40)
    private String bezeichnung = "";

    @PositiveOrZero(message = "Preis darf nicht negativ sein")
    private double preis;

    @NotNull
    private Integer vegetarizitaet = 0;

    @PositiveOrZero(message = "Bestand darf nicht negativ sein")
    private int bestand;

    @ManyToMany
    private List<Zutat> zutaten = new ArrayList<>();

    @Transient
    private int entliehen = 0;

    public Doener() {

    }

    public Doener(String name, List<Zutat> zutaten, int vegetarizitaet, int preis, int bestand) {
        this.bezeichnung = name;
        this.zutaten = zutaten;
        this.vegetarizitaet = vegetarizitaet;
        this.preis = preis;
        this.bestand = bestand;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public int getVerfuegbar() {
        return bestand - entliehen;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public Integer getVegetarizitaet() {
        return vegetarizitaet;
    }

    public void setVegetarizitaet(Integer vegetarizitaet) {
        this.vegetarizitaet = vegetarizitaet;
    }

    public int getBestand() {
        return bestand;
    }

    public void setBestand(int bestand) {
        this.bestand = bestand;
    }

    public List<Zutat> getZutaten() {
        return zutaten;
    }

    public void addZutat(Zutat zutat) {
        zutaten.add(zutat);
    }

    public void setZutaten(List<Zutat> zutaten) {
        this.zutaten = zutaten;
    }

    @Override
    public String toString() {
        return "Doener [id=" + id + ", version=" + version + ", bezeichnung=" + bezeichnung + ", preis=" + preis
                + ", vegetarizitaet=" + vegetarizitaet + ", bestand=" + bestand + ", zutaten=" + zutaten + "]";
    }

    public int getEntliehen() {
        return entliehen;
    }

    public void setEntliehen(int entliehen) {
        this.entliehen = entliehen;
    }

}
