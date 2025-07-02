package retrofit;

import com.google.gson.annotations.SerializedName;
import model.Category;

public class ProductResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ProductData data; // This should be a single ProductData object, not a list

    @SerializedName("status")
    private String status;
    private boolean isLiked;


    public boolean isLiked() {
        return isLiked;
    }
    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductData getData() {
        return data; // Make sure this is returning a single ProductData object
    }

    public void setData(ProductData data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ProductData {

        @SerializedName("id")
        private int id;

        @SerializedName("user_id")
        private int userId;

        @SerializedName("name")
        private String name;

        @SerializedName("description")
        private String description;

        @SerializedName("amount")
        private double amount;

        @SerializedName("photo")
        private String photo;

        @SerializedName("purchase_date")
        private String purchaseDate;

        @SerializedName("category_id")
        private int categoryId;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("category")
        private Category category;

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(String purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
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

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        @Override
        public String toString() {
            return "ProductData{" +
                    "id=" + id +
                    ", userId=" + userId +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", amount=" + amount +
                    ", photo='" + photo + '\'' +
                    ", purchaseDate='" + purchaseDate + '\'' +
                    ", categoryId=" + categoryId +
                    ", createdAt='" + createdAt + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    ", category=" + category +
                    '}';
        }
    }
}
