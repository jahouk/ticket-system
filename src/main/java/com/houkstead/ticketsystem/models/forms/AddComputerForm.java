package com.houkstead.ticketsystem.models.forms;

import com.houkstead.ticketsystem.models.Computer;
import com.houkstead.ticketsystem.models.Office;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class AddComputerForm {

    @NotEmpty(message="*Computer's name is required")
    @Length(max = 40)
    private String name;                // Workstation Name or ID

    private Office office;

    public AddComputerForm() {
    }

    public AddComputerForm(Computer computer){
        setName(computer.getName());
        setOffice(computer.getOffice());
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
