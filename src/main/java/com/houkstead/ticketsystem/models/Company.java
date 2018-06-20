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

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMPANY",uniqueConstraints = @UniqueConstraint(columnNames = "company_id", name = "COMPANY_PK_CONSTRAINT"))
public class Company {
    //todo

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    private int id;                     // autonumber

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey=@ForeignKey(name="FK_COMPANY_INFO_COMPANY"))
    private CompanyInfo companyInfo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Address> addresses = new ArrayList<Address>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Asset> assets = new ArrayList<Asset>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Site> sites = new ArrayList<Site>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Ticket> tickets = new ArrayList<Ticket>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<User> users = new ArrayList<User>();

    // Constructors -----------------------------------------------------------

    public Company(){}

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // CompanyInfo
    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    // Addresses
    public List<Address> getAddresses(){
        return this.addresses;
    }

    public void addAddress(Address address){
        addresses.add(address);
    }

    // Assets
    public List<Asset> getAssets(){
        return assets;
    }

    public void addAsset(Asset asset){
        assets.add(asset);
    }

    // Sites
    public List<Site> getSites(){
        return sites;
    }

    public void addSite(Site site){
        sites.add(site);
    }

    // Tickets
    public List<Ticket> getTickets(){
        return tickets;
    }

    public void addTicket(Ticket ticket){
        tickets.add(ticket);
    }

    // Users
    public List<User> getUsers(){
        return users;
    }

    public void addUser(User user){
        users.add(user);
    }

}
