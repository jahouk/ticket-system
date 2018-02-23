package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "OFFICE",uniqueConstraints = @UniqueConstraint(columnNames = "office_id", name = "OFFICE_PK_CONSTRAINT"))
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "office_id")
    private int id;             // auto-number

    @ManyToOne
    @JoinColumn(name="site_id")
    @NotNull
    private Site site;          // Physical Location of the building

    @NotEmpty(message="*Location Description/Name is required")
    @NotNull
    @Column(name="office")
    private String office;    // Physical Location of Office by Description or Room Number

    @OneToMany
    @JoinColumn(foreignKey=@ForeignKey(name="FK_COMPUTER_OFFICE"))
    private List<Computer> computers = new ArrayList<>(); // List of computers in this office


    // Constructors -----------------------------------------------------------

    public Office(){}

    public Office(Site site, String office){
        setSite(site);
        setOffice(office);
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Site
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    // Location
    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    // List of Computers
    public List<Computer> getComputers() {
        return computers;
    }

}
