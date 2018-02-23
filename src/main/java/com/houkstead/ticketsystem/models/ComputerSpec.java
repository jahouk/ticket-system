package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "COMPUTER_SPEC",uniqueConstraints = @UniqueConstraint(columnNames = "computer_spec_id", name = "COMPUTER_SPEC_PK_CONSTRAINT"))
public class ComputerSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "computer_spec_id")
    private int id;             // autonumber

    @ManyToOne
    private Computer computer;  // owning computer

    @NotEmpty
    @Length(max = 40)
    @Column(name = "name", nullable = false)
    private String specName;    // name of spec

    @NotEmpty
    @Length(max = 40)
    @Column(name = "value", nullable = false)
    private String specValue;   // value of spec

    @NotEmpty
    @Column(name = "sort", nullable = false)
    private int sortValue;      // sort value for displaying results (grouping)

    // Constructors -----------------------------------------------------------

    public ComputerSpec(){}

    public ComputerSpec(Computer computer, String name, String value,
                        int sort){
        setComputer(computer);
        setSpecName(name);
        setSpecValue(value);
        setSortValue(sort);
    }

    public ComputerSpec(Computer computer, String name, String value){
        this(computer, name, value, 100);
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Computer
    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    // SpecName
    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    // SpecValue
    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }

    public String getSpecValue() {
        return specValue;
    }

    // SortValue
    public int getSortValue() {
        return sortValue;
    }

    public void setSortValue(int sortValue) {
        this.sortValue = sortValue;
    }
}

