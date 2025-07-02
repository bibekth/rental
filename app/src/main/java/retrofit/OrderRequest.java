package retrofit;

import com.google.gson.annotations.SerializedName;

public class OrderRequest {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("order_date")
    private String orderDate;

    public OrderRequest(String productId, String quantity, String orderDate) {
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
