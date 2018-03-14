package com.houkstead.ticketsystem.models.forms;

import com.houkstead.ticketsystem.models.Asset;
import com.houkstead.ticketsystem.models.User;
import org.hibernate.validator.constraints.NotEmpty;

public class AddTicketForm {

        @NotEmpty(message = "*Title is required")
        private String title;           // One Line Description

        @NotEmpty(message = "*Title is required")
        private String description;     // Full Description

        private Asset asset;      // The affected asset which then tells the site and company

        private User owner;

        // Constructors -----------------------------------------------------------
    public AddTicketForm() {
    }


    // Start of Getters and Setters -------------------------------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}


