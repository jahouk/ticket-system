/*
 * MIT License
 *
 * Copyright (c) 2018 Jason Houk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.houkstead.ticketsystem.models;

import com.houkstead.ticketsystem.models.forms.AddTicketUpdateForm;
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

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="ticket_id", foreignKey=@ForeignKey(name="FK_TICKET_TICKET_UPDATE"))
    private Ticket ticket;              // owning ticket

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id", foreignKey=@ForeignKey(name="FK_USER_TICKET_UPDATE"))
    private User user;                  // users who is doing update

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
        setTimestamp(new Timestamp(System.currentTimeMillis()));
        setUpdateTitle(title);
        setUpdateDescription(description);
    }

    public TicketUpdate(Ticket ticket, User user, AddTicketUpdateForm addTicketUpdateForm){
        setTicket(ticket);
        setUser(user);
        setTimestamp(new Timestamp(System.currentTimeMillis()));
        setUpdateTitle(addTicketUpdateForm.getUpdateTitle());
        setUpdateDescription(addTicketUpdateForm.getUpdateDescription());
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

