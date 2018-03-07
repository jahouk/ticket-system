package com.houkstead.ticketsystem.models;

import com.houkstead.ticketsystem.models.forms.AddTicketForm;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.apache.tomcat.jni.Time.now;

@Entity
@Table(name = "TICKET",uniqueConstraints = @UniqueConstraint(columnNames = "ticket_id", name = "TICKET_PK_CONSTRAINT"))
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ticket_id")
    private int id;                 // Auto number

    @Column(name="created")
    @NotNull
    private Timestamp created;      // Created Timestamp

    @Column(name="closed")
    private Timestamp closed;       // Timestamp of when end users closes ticket

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "status_id", foreignKey=@ForeignKey(name="FK_STATUS_TICKET"))
    private Status status;          // Status that ticket belongs to

    @Column(name="title")
    @NotEmpty(message = "*Title is required")
    @NotNull
    private String title;           // One Line Description

    @Column(name="description")
    @NotEmpty(message = "*Title is required")
    @NotNull
    private String description;     // Full Description

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "computer_id", foreignKey=@ForeignKey(name="FK_COMPUTER_TICKET"))
    private Computer computer;      // The affected computer which then tells the site and company

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<TicketUpdate> updates = new ArrayList<>();     // Updates by tech or customer

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", foreignKey=@ForeignKey(name="FK_USER_TICKET"))
    private User owner;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "company_id", foreignKey=@ForeignKey(name="FK_COMPANY_TICKET"))

    private Company company;


    // Constructors -----------------------------------------------------------

    public Ticket(){}

    public Ticket(User owner, Status status, String title, String description,
                  Computer computer){

        setOwner(owner);
        setStatus(status);
        setTitle(title);
        setDescription(description);
        setComputer(computer);

        setCreated(new Timestamp(System.currentTimeMillis()));
    }

    public Ticket(AddTicketForm addTicketForm, Status status){
        this(addTicketForm.getOwner(),
                status,
                addTicketForm.getTitle(),
                addTicketForm.getDescription(),
                addTicketForm.getComputer());
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Created
    public Timestamp getCreated() {
        return created;
    }

    private void setCreated(Timestamp created) {
        this.created = created;
    }

    // Closed
    public Timestamp getClosed() {
        return closed;
    }

    public void setClosed(Timestamp closed) {
        this.closed = closed;
    }

    // Status
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Computer
    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    // Updates
    public List<TicketUpdate> getUpdates() {
        return updates;
    }

    public void addUpdate(TicketUpdate update){
        updates.add(update);
    }

    // Owner
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

}

