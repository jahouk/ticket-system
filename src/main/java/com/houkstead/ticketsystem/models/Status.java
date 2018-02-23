package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STATUS",uniqueConstraints = @UniqueConstraint(columnNames = "status_id", name = "STATUS_PK_CONSTRAINT"))
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "status_id")
    private int id;                 // autonumber

    @NotEmpty
    @Column(name = "status", nullable = false)
    private String status;          // the status name

    @NotNull
    @Column(name = "sort_order")
    private int sortOrder;          // sort order for displaying

    @OneToMany
    @JoinColumn(foreignKey=@ForeignKey(name="FK_TICKET_STATUS"))
    private List<Ticket> tickets = new ArrayList<>();   // Tickets that belong to this status

    // Constructors -----------------------------------------------------------

    public Status(){}

    public Status(String status, int sortOrder){
        setStatus(status);
        setSortOrder(sortOrder);
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // SortOrder
    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}

