package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Table(name = "COMPUTER",uniqueConstraints = @UniqueConstraint(columnNames = "computer_id", name = "COMPUTER_PK_CONSTRAINT"))
public class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "computer_id")
    private int id;                     // Auto Number

    @NotEmpty(message="*Computer's name is required")
    @Length(max = 40)
    @Column(name = "name", nullable = false)
    private String name;                // Workstation Name or ID

    @OneToMany
    @JoinColumn(foreignKey=@ForeignKey(name="FK_COMPUTER_SPEC_COMPUTER"))
    private List<ComputerSpec> specs;   // list of specs about the computer

    @OneToMany
    @JoinColumn(foreignKey=@ForeignKey(name="FK_TICKET_COMPUTER"))
    private List<Ticket> tickets;

    @ManyToOne
    private Company company;            // what company owns the computer

    @ManyToOne
    private Office office;              // where the computer is physically located

    // Constructors -----------------------------------------------------------

    public Computer(){}

    public Computer(String name, Office office){
        setName(name);
        setOffice(office);
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Office
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    // Specs
    public List<ComputerSpec> getSpecs() {
        return specs;
    }

    public void setSpec(ComputerSpec computerSpec) {
        specs.add(computerSpec);
    }

    // Tickets

    public List<Ticket> getTickets() {
        return tickets;
    }
}
