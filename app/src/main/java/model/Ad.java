package model;

public class Ad {
    private String name;
    private String image;
    private double price;
    private String category;
    private boolean negotiable;
    private int purchaseYear;
    private String description;

    public Ad(String name, String image, double price, String category, boolean negotiable, int purchaseYear, String description) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.category = category;
        this.negotiable = negotiable;
        this.purchaseYear = purchaseYear;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isNegotiable() {
        return negotiable;
    }

    public int getPurchaseYear() {
        return purchaseYear;
    }

    public String getDescription() {
        return description;
    }
}
