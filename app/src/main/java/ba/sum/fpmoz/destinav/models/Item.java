package ba.sum.fpmoz.destinav.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Item {
    private String id;
    public String name;
    public String location;
    public String category;
    public String price;
    public String image;

    public Item() {
    }

    public Item( String id, String name, String location, String category, String price, String image) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.category = category;
        this.price = price;
        this.image = image;
    }

    public String getId() {
        return id;
    }


    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setDescription(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}