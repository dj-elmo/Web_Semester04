package de.hsrm.mi.web.projekt.benutzer.ui;

import de.hsrm.mi.web.projekt.validation.GeeigneteLosung;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BenutzerFormular {
    @NotBlank(message = "Name darf nicht leer sein")
    @Size(min = 3, max = 60, message = "{benutzer.name.validation}")
    private String name = "";
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{benutzer.email.validation}")
    private String digitalpostanschrift = "";
    private int vegetarizitaet = 0;
    private String rolle = "";
    @GeeigneteLosung(message = "{benutzer.losung.validation}")
    private String losung = "";
    @GeeigneteLosung(message = "{benutzer.losungwh.validation}")
    private String losungWiederh = "";

    public BenutzerFormular() {
        this.losung = "";
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

    public int getVegetarizitaet() {
        return vegetarizitaet;
    }

    public void setVegetarizitaet(int vegetarizitaet) {
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

    public String getLosungWiederh() {
        return losungWiederh;
    }

    public void setLosungWiederh(String losungWiederh) {
        this.losungWiederh = losungWiederh;
    }

    @Override
    public String toString() {
        return "BenutzerFormular{" +
                "name='" + name + '\'' +
                ", Digitalpostanschrift='" + digitalpostanschrift + '\'' +
                ", vegetarizitaet=" + vegetarizitaet +
                ", rolle='" + rolle + '\'' +
                ", losung='" + losung + '\'' +
                ", losungWiederh='" + losungWiederh + '\'' +
                '}';
    }
}
