package retrofit;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("data")
    private Data data;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    // Getters and Setters
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data {
        @SerializedName("user_id")
        private int userId;

        @SerializedName("product_id")
        private int productId;

        @SerializedName("quantity")
        private int quantity;

        @SerializedName("order_date")
        private String orderDate;

        @SerializedName("status")
        private boolean status;

        @SerializedName("offer")
        private String offer;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        // Getters and Setters
        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getOffer() {
            return offer;
        }

        public void setOffer(String offer) {
            this.offer = offer;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
