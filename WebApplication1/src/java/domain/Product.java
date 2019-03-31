package domain;

import java.time.LocalDateTime;

public class Product {
    
    // Attributes
    private String brand, name, size, imgurl, producturl, price;
    private LocalDateTime timestamp;

    // Default constructor
    public Product() { }
            
    public Product(LocalDateTime timestamp, String brand, String name, String size, String price, String imgurl, String producturl) {
        this.timestamp = timestamp;
        this.brand = brand;
        this.name = name;
        this.size = size;
        this.imgurl = imgurl;
        this.producturl = producturl;
        this.price = price;
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

    public String getSize() {
        return size;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getProducturl() {
        return producturl;
    }

    public String getPrice() {
        return price;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    @Override
    public String toString() {
        return "[ timestamp: " + timestamp + ", brand: " + brand + ", name: " 
                + name + ", size: " + size + ", imgurl: " + imgurl
                + ", price: " + price + ", producturl: " + producturl + "]";
    }
}
