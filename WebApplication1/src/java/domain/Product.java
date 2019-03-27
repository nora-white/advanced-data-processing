package domain;

import java.time.LocalDateTime;

public class Product {
    
    // Attributes
    private String brand,name, sizeoz, sizeg, imgurl, producturl;
    private String[] colour;
    private float price;
    private LocalDateTime timestamp;

    // Default constructor
    public Product() {}
    
    public Product(LocalDateTime timestamp, String brand, String name, String sizeoz, String sizeg, float price, String[] colour, String imgurl, String producturl) {
        this.timestamp = timestamp;
        this.brand = brand;
        this.name = name;
        this.sizeoz = sizeoz;
        this.sizeg = sizeg;
        this.imgurl = imgurl;
        this.producturl = producturl;
        this.price = price;
        this.colour = colour;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return name;
    }

    public String getSizeoz() {
        return sizeoz;
    }

    public String getSizeg() {
        return sizeg;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getProducturl() {
        return producturl;
    }

    public String[] getColour() {
        return colour;
    }

    public float getPrice() {
        return price;
    }
}
