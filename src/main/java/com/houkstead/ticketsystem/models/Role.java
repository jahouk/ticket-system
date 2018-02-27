package com.houkstead.ticketsystem.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ROLE",uniqueConstraints = @UniqueConstraint(columnNames = "role_id", name = "ROLE_PK_CONSTRAINT"))
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private int id;             // autonumber

    @Column(name = "role")
    private String role;        // role name (i.e. user, admin)

    @Column(name = "description")
    private String description; // description in case roles become complex

    @Column(name = "sort_order")
    private int sortOrder;      // Sort order for forms

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey=@ForeignKey(name="FK_USER_ROLE"))
    private Set<User> Users;


    // Constructors -----------------------------------------------------------

    public Role(){}

    public Role(String role, String description, int sortOrder){
        setRole(role);
        setDescription(description);
        setSortOrder(sortOrder);
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Sort Order
    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Set<User> getUsers() {
        return Users;
    }

}

