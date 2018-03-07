package com.houkstead.ticketsystem.models.forms;

import org.hibernate.validator.constraints.NotEmpty;

public class EditOfficeForm {
    @NotEmpty(message="*Location Description/Name is required")
    private String office;

    public EditOfficeForm() {
    }

    public EditOfficeForm(String office) {
        setOffice(office);
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}
