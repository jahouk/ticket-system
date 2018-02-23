package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.sql.Timestamp;

import static org.apache.tomcat.jni.Time.now;

@Entity
@Table(name = "TICKET_UPDATE",uniqueConstraints = @UniqueConstraint(columnNames = "ticket_update_id", name = "TICKET_UPDATE_PK_CONSTRAINT"))
public class TicketUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ticket_update_id")
    private int id;                     // autonumber

    @ManyToOne
    private Ticket ticket;              // owning ticket

    @ManyToOne
    @JoinColumn(foreignKey=@ForeignKey(name="FK_USER_TICKET_UPDATE"))
    private User user;                  // user who is doing update

    @Column(name = "timestamp")
    private Timestamp timestamp;        // timestamp of update

    @Column(name = "title")
    @NotEmpty(message = "*Update Title Required")
    private String updateTitle;         // One line title of update

    @Column(name = "description")
    @NotEmpty(message = "*Description / Response Required")
    private String updateDescription;   // Full text of update

    // Constructors -----------------------------------------------------------

    public TicketUpdate(){}

    public TicketUpdate(Ticket ticket, User user, String title,
                        String description ){
        setTicket(ticket);
        setUser(user);
        setTimestamp(new Timestamp(now()));
        setUpdateTitle(title);
        setUpdateDescription(description);
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Ticket
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    // User
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // TimeStamp
    public Timestamp getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    // Update Title
    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }

    // Update Description
    public String getUpdateDescription() {
        return updateDescription;
    }

    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription;
    }
}

