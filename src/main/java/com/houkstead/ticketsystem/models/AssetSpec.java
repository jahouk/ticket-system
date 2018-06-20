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

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(name = "ASSET_SPEC",uniqueConstraints = @UniqueConstraint(columnNames = "asset_spec_id", name = "ASSET_SPEC_PK_CONSTRAINT"))
public class AssetSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "asset_spec_id")
    private int id;             // autonumber

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "asset_id", foreignKey=@ForeignKey(name="FK_ASSET_ASSET_SPEC"))
    private Asset asset;  // owning asset

    @NotEmpty
    @Length(max = 40)
    @Column(name = "name", nullable = false)
    private String specName;    // name of spec

    @NotEmpty
    @Length(max = 40)
    @Column(name = "value", nullable = false)
    private String specValue;   // value of spec


    @Column(name = "sort", nullable = false)
    private int sortValue;      // sort value for displaying results (grouping)

    // Constructors -----------------------------------------------------------

    public AssetSpec(){}

    public AssetSpec(Asset asset, String name, String value,
                     int sort){
        setAsset(asset);
        setSpecName(name);
        setSpecValue(value);
        setSortValue(sort);
    }

    public AssetSpec(Asset asset, String name, String value){
        this(asset, name, value, 100);
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Asset
    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
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

