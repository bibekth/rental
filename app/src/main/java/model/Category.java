package model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    private int id;
    private String name;
    private String photo;
    private String color;
    private String createdAt;
    private String updatedAt;

    // Constructor
    public Category(int id, String name, String photo, String color, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.color = color;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Override toString to display the category name in dropdowns and other UI elements
    @Override
    public String toString() {
        return name; // This displays the category name instead of the object reference
    }

    // Parcelable implementation

    // Constructor used when re-constructing object from a Parcel
    protected Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
        photo = in.readString();
        color = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    // Creator constant for the Parcelable interface
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    // Write object data to the Parcel for serialization
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(color);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    // Describe the contents for the Parcelable interface (usually return 0)
    @Override
    public int describeContents() {
        return 0;
    }
}
