package de.hsrm.mi.web.projekt.entities.benutzer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.hsrm.mi.web.projekt.entities.doener.Doener;
import de.hsrm.mi.web.projekt.validation.GeeigneteLosung;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Benutzer implements Serializable {
    @Id
    private String loginName;

    @Version
    private long version;

    @NotBlank(message = "Name darf nicht leer sein")
    @Size(min = 3, max = 60, message = "{benutzer.name.validation}")
    private String name = "";

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{benutzer.email.validation}")
    private String digitalpostanschrift = "";

    @NotNull
    private Integer vegetarizitaet = 0;

    @NotBlank
    private String rolle = "";

    @NotBlank
    private String losung = "";

    @ManyToMany
    List<Doener> entlieheneDoener = new LinkedList<>();

    public Benutzer() {
    }

    public List<Doener> getEntlieheneDoener() {
        return entlieheneDoener;
    }

    public Benutzer(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDigitalpostanschrift() {
        return digitalpostanschrift;
    }

    public void setDigitalpostanschrift(String digitalpostanschrift) {
        this.digitalpostanschrift = digitalpostanschrift;
    }

    public Integer getVegetarizitaet() {
        return vegetarizitaet;
    }

    public void setVegetarizitaet(Integer vegetarizitaet) {
        this.vegetarizitaet = vegetarizitaet;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public String getLosung() {
        return losung;
    }

    public void setLosung(String losung) {
        this.losung = losung;
    }

    @Override
    public String toString() {
        return "Benutzer [loginName=" + loginName + ", name=" + name + ", digitalpostanschrift=" + digitalpostanschrift
                + ", vegetarizitaet=" + vegetarizitaet + ", rolle=" + rolle + ", losung=" + losung + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Referenzgleichheit
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Typprüfung
        }

        Benutzer benutzer = (Benutzer) obj;
        return loginName != null && loginName.equals(benutzer.loginName); // Vergleich des Primärschlüssels
    }

}
