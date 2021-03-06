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

import com.houkstead.ticketsystem.models.forms.AddAssetForm;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "ASSET",uniqueConstraints = @UniqueConstraint(columnNames = "asset_id", name = "ASSET_PK_CONSTRAINT"))
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "asset_id")
    private int id;                     // Auto Number

    @NotEmpty(message="*Asset's name is required")
    @Length(max = 40)
    @Column(name = "name", nullable = false)
    private String name;                // Workstation Name or ID

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<AssetSpec> specs = new ArrayList<AssetSpec>();   // list of specs about the computer

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Ticket> tickets = new ArrayList<Ticket>();

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "company_id", foreignKey=@ForeignKey(name="FK_COMPANY_ASSET"))
    private Company company;            // what company owns the computer

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "office_id", foreignKey=@ForeignKey(name="FK_OFFICE_ASSET"))
    private Office office;              // where the computer is physically located

    // Constructors -----------------------------------------------------------

    public Asset(){}

    public Asset(String name, Office office){
        setName(name);
        setOffice(office);
    }

    public Asset(AddAssetForm addAssetForm){
        this(addAssetForm.getName(), addAssetForm.getOffice());
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
    public List<AssetSpec> getSpecs() {
        return specs;
    }

    public void addAssetSpec(AssetSpec assetSpec){
        specs.add(assetSpec);
    }

    // Tickets

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void addTicket(Ticket ticket){
        tickets.add(ticket);
    }
}
