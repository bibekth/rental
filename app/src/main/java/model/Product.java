package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import retrofit.WishlistResponse;

public class Product implements Parcelable {
    private int id;
    private int userId;
    private String name;
    private String description;
    private String photo;
    private double amount;
    private double rate;

    private int categoryId;
    private int category_id;
    private String purchaseDate;
    private String purchase_date;
    private Category category;

    public Product() {
    }

    public Product(int id, int userId, String name, String description, String photo, double amount, int category_id, String purchase_date, Category category, double rate) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.amount = amount;
        this.rate = rate;
        this.category_id = category_id;
        this.purchase_date = purchase_date;
        this.category = category;
    }

    public Product(int id, int userId, String name, String description, double amount, String photo, int category_id, String purchase_date, Category category) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.photo = photo;
        this.category_id = category_id;
        this.purchase_date = purchase_date;
        this.category = category;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    // Constructor with all parameters, including Category object
//    public Product(int id, int userId, String name, String description, double amount, String photo, int categoryId, String purchaseDate, Category category) {
//        this.id = id;
//        this.userId = userId;
//        this.name = name;
//        this.description = description;
//        this.amount = amount;
//        this.photo = photo;
//        this.categoryId = categoryId;
//        this.purchaseDate = purchaseDate;
//        this.category = category;
//    }

    // Constructor for mapping from WishlistResponse
    public Product(WishlistResponse wishlistResponse) {
        this.id = wishlistResponse.getId(); // ID from WishlistResponse
        this.userId = wishlistResponse.getUserId(); // User ID from WishlistResponse

        // Extracting product details from the nested Product object in WishlistResponse
        Product nestedProduct = wishlistResponse.getProduct();
        if (nestedProduct != null) {
            this.name = nestedProduct.getName(); // Product name
            this.description = nestedProduct.getDescription(); // Product description
            this.amount = nestedProduct.getAmount(); // Product price/amount
            this.photo = nestedProduct.getPhoto(); // Product photo URL
            this.categoryId = nestedProduct.getCategoryId(); // Category ID
            this.purchaseDate = nestedProduct.getPurchaseDate(); // Purchase date
            this.category = nestedProduct.getCategory(); // Category object
        }
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    @SerializedName("photo")
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    @SerializedName("category_id")
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    @SerializedName("purchase_date")
    public String getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }

    // Parcelable implementation
    protected Product(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        name = in.readString();
        description = in.readString();
        amount = in.readDouble();
        photo = in.readString();
        categoryId = in.readInt();
        purchaseDate = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(amount);
        dest.writeString(photo);
        dest.writeInt(categoryId);
        dest.writeString(purchaseDate);
        dest.writeParcelable(category, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
