package com.finalsproject.finalsproject;

import java.sql.Date;

public class ProductMod {

    private Integer id;
    private String productId;
    private String productName;
    private String category;
    private Integer stock;
    private Double price;
    private String status;
    private String image;
    private Date date;
    private Integer quantity;

    public ProductMod(Integer id, String productId,
                       String productName, String category, Integer stock,
                       Double price, String status, String image, Date date) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.status = status;
        this.image = image;
        this.date = date;
        }
    public ProductMod(Integer id, String productId, String productName,
                       String category, Integer quantity, Double price, String image, Date date){
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.image = image;
        this.date = date;
        this.quantity = quantity;
    }



    public Integer getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory(){
        return category;
    }

    public Integer getStock() {
        return stock;
    }

    public Double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public Integer getQuantity(){
        return quantity;
    }
}
