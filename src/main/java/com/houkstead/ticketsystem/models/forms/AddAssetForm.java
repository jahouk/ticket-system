package com.houkstead.ticketsystem.models.forms;

import com.houkstead.ticketsystem.models.Asset;
import com.houkstead.ticketsystem.models.Office;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class AddAssetForm {

    @NotEmpty(message="*Asset's name is required")
    @Length(max = 40)
    private String name;                // Workstation Name or ID

    private Office office;

    public AddAssetForm() {
    }

    public AddAssetForm(Asset asset){
        setName(asset.getName());
        setOffice(asset.getOffice());
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
