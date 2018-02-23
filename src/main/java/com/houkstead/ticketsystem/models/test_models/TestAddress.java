package com.houkstead.ticketsystem.models.test_models;

public class TestAddress {

    private String address1;    // address line 1

    private String address2;    // address line 2

    private String city;        // city

    private String state;       // state (consider state table or something)

    private String zip;         // zip

    // Constructors -----------------------------------------------------------

    public TestAddress(){};

    public TestAddress(String address1, String address2, String city, String state,
                       String zip){
        setAddress1(address1);
        setAddress2(address2);
        setCity(city);
        setState(state);
        setZip(zip);
    }

    // Start of Getters and Setters -------------------------------------------
    // Address 1
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    // Address 2
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    // City
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // State
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Zip
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

}
