package com.example.snehamishra.smartshopping;

/**
 * Created by CJ on 3/4/2018.
 */

public class ItemData
{
    private int id;
    private String name;
    private String creator;
    private double price;
    private String size;
    private String store;
    private String description;
    private String image;
    private String color;

    // Constructors
    // Default constructor
    ItemData()
    {
        id = 0;
        name = "";
        creator = "";
        price = 0;
        size = "";
        store = "";
        description = "";
        image = "";
        color = "";
    } // end default constructor

    // Parameter constructor
    ItemData(String tempName, String tCreator, double tPrice, String tImage)
    {
        id = 0;
        name = tempName;
        creator = tCreator;
        price = tPrice;
        size = "";
        store = "";
        description = "";
        image = tImage;
        color = "";
    } // end parameter constructor

    // Parameter constructor
    ItemData(int tId, String tempName, String tCreator, double tPrice, String tSize, String tStore, String tDesc, String tColor, String tImage)
    {
        id = tId;
        name = tempName;
        creator = tCreator;
        price = tPrice;
        size = tSize;
        store = tStore;
        description = tDesc;
        image = tImage;
        color = tColor;
    } // end parameter constructor

    // Accessors
    public int getId()
    {
        return id;
    } // end getID

    // Returns the name of the list item
    String getName()
    {
        return name;
    } // end getName

    public String getCreator()
    {
        return creator;
    } // end getCreator

    public double getPrice()
    {
        return price;
    } // end

    public String getSize()
    {
        return size;
    } // end

    public String getStore()
    {
        return store;
    } // end

    public String getDescription()
    {
        return description;
    } // end

    public String getImage()
    {
        return image;
    } // end

    public String getColor()
    {
        return color;
    } // end

    // Prints the name of the list item
    public String toString()
    {
        return(getName());
    } // end toString
} // end class ItemData
