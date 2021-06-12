package com.model;

public class Product {
	
    private String type;
    private long inStock; 
    private long soldOut; 
    private Double unitPrice; 

    public Product() {
    }

    public Product(String type, long inStock, long soldOut, Double unitPrice) {
        this.type = type;
        this.inStock = inStock;
        this.soldOut = soldOut;
        this.unitPrice = unitPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getInStock() {
        return inStock;
    }

    public void setInStock(long inStock) {
        this.inStock = inStock;
    }

    public long getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(long soldOut) {
        this.soldOut = soldOut;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
