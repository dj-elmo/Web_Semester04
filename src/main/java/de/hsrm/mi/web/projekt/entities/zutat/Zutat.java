package de.hsrm.mi.web.projekt.entities.zutat;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Zutat implements Serializable{
    @Id
    @Size(min = 13, max = 13, message = "EAN muss 13 Zeichen lang sein")
    private String ean;

    @Version
    private long version;

    @NotBlank(message = "Name darf nicht leer sein")
    private String name;

    @NotNull
    private Integer vegetarizitaet = 0;

    public Zutat(){

    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVegetarizitaet() {
        return vegetarizitaet;
    }

    public void setVegetarizitaet(Integer vegetarizitaet) {
        this.vegetarizitaet = vegetarizitaet;
    }

    @Override
    public String toString() {
        return "Zutat [name=" + name + "]";
    }
}
