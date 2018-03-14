package com.houkstead.ticketsystem.models.forms;

import com.houkstead.ticketsystem.models.Status;
import org.hibernate.validator.constraints.NotEmpty;

public class AddTicketUpdateForm {

    @NotEmpty(message = "*Update Title Required")
    private String updateTitle;         // One line title of update

    @NotEmpty(message = "*Description / Response Required")
    private String updateDescription;   // Full text of update

    private Status status;

    // Constructor

    public AddTicketUpdateForm() {
    }

    // Getters and Setters

    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }

    public String getUpdateDescription() {
        return updateDescription;
    }

    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
