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

package com.houkstead.ticketsystem.utilities;

import com.houkstead.ticketsystem.models.*;
import com.houkstead.ticketsystem.models.test_models.TestAddress;
import com.houkstead.ticketsystem.repository.*;

import java.util.List;

public class SetupUtilities {

    public static Role seedUserRole(Role role, RoleRepository roleRepository) {
        roleRepository.save(role);
        System.out.println("ROLE: " + role.getRole() + " added to database");
        return role;
    }

    public static Status seedTicketStatus(Status status, StatusRepository statusRepository) {
        statusRepository.save(status);
        System.out.println("STATUS: " + status.getStatus() + " added to database");
        return status;
    }


    public static Company createCompany(Company company, CompanyRepository companyRepository) {
        companyRepository.save(company);
        System.out.println("COMPANY: " + company.getId() + " added to database");
        return company;
    }


    public static CompanyInfo createCompanyInfo(CompanyInfo companyInfo, CompanyInfoRepository companyInfoRepository){
        companyInfoRepository.save(companyInfo);
        System.out.println("COMPANY_INFO: " + companyInfo.getName() + " for comapany "
                + companyInfo.getCompany().getId());
        return companyInfo;
    }


    public static Address createAddress(Address address, AddressRepository addressRepository) {

        addressRepository.save(address);
        System.out.println("ADDRESS: " + address.getAddress1() + " for company " + address.getCompany().getId()
                + " added to database");
        return address;
    }

    public static Site createSite(Site site, SiteRepository siteRepository) {
        siteRepository.save(site);
        System.out.println("SITE: " + site.getSite() + " for company " + site.getCompany().getId()
                + " added to database");
        return site;
    }

    public static Office createOffice(Office office, OfficeRepository officeRepository) {
        officeRepository.save(office);
        System.out.println("OFFICE: " + office.getOffice() + " for company " + office.getSite().getCompany().getId()
                + " added to database");
        return office;
    }

    public static UserInfo createUserInfo(UserInfo userInfo, UserInfoRepository userInfoRepository){
        userInfoRepository.save(userInfo);
        System.out.println("USER_INFO: " + userInfo.getFname() + " " + userInfo.getLname() + " added to database");
        return userInfo;
    }


    public static Address duplicateAddressCheck(
            TestAddress testAddress,
            Company company,
            AddressRepository addressRepository,
            CompanyRepository companyRepository){

        // Return Value
        List<Address> addressList = addressRepository.findByCompany(company);
        Address returnAddress = null;

        if(!addressList.isEmpty()) {
            // Check the addresses
            for (Address address : addressList) {
                // Test if everything but address2 is the same
                if (address.getAddress1().equalsIgnoreCase(testAddress.getAddress1()) &&
                        address.getCity().equalsIgnoreCase(testAddress.getCity()) &&
                        address.getState().equalsIgnoreCase(testAddress.getState()) &&
                        address.getZip().equalsIgnoreCase(testAddress.getZip())) {
                    // see if address 2 are both null
                    if(address.getAddress2() == null && testAddress.getAddress2() == null){
                        returnAddress = address;
                        break;
                    }
                    // else see if they are both not null and are equal
                    else if(address.getAddress2() != null  &&
                            testAddress.getAddress2() != null &&
                            address.getAddress2().equals(testAddress.getAddress2())
                            ){
                        returnAddress = address;
                        break;
                    }
                }
            }
        }

        if(returnAddress==null){
            returnAddress = createAddress(
                    new Address(
                            testAddress.getAddress1(),
                            testAddress.getAddress2(),
                            testAddress.getCity(),
                            testAddress.getState(),
                            testAddress.getZip(),
                            company),
                    addressRepository);
        }
        else{
            System.out.println("ADDRESS: " + returnAddress.getAddress1() +
                    " for company " + returnAddress.getCompany().getId()
                    + " was previously created");
        }

        return returnAddress;
    }

}
