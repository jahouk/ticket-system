package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Table(name = "OFFICE",uniqueConstraints = @UniqueConstraint(columnNames = "office_id", name = "OFFICE_PK_CONSTRAINT"))
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "office_id")
    private int id;             // auto-number

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "site_id",foreignKey=@ForeignKey(name="FK_SITE_OFFICE"))
    private Site site;          // Physical Location of the building

    @NotEmpty(message="*Location Description/Name is required")
    @NotNull
    @Column(name="office")
    private String office;    // Physical Location of Office by Description or Room Number

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Asset> assets; // List of assets in this office


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

    // SitesController
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

    // List of Assets
    public List<Asset> getAssets() {
        return assets;
    }

    public void addAsset(Asset asset){
        assets.add(asset);
    }

}
