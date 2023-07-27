package rest;

import java.sql.*;

public class Products {

	 	private int id;
	    private String barcode;
	    private String description;
	    private String price;
	    private String category;
	    private Blob image;
	    private String status;

	    //constructure
	    public Products(int id, String barcode, String description, String price, String category, Blob image, String status) {
	        this.id = id;
	        this.barcode = barcode;
	        this.description = description;
	        this.price = price;
	        this.category = category;
	        this.image = image;
	        this.status = status;
	    }

	    //getter
	    public int getId() {
	        return id;
	    }
	    
	    public String getBarcode(){
	        return barcode;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public String getPrice() {
	        return price;
	    }

	    public String getCategory() {
	        return category;
	    }

	    public Blob getImage() {
	        return image;
	    }

	    public String getStatus() {
	        return status;
	    }
	
}
