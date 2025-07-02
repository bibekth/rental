package retrofit;

import com.google.gson.annotations.SerializedName;

public class OrderDetailResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("order_id")
    private int orderId;

    @SerializedName("delivery_address")
    private String deliveryAddress;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("mobile_number")
    private String mobileNumber;

    @SerializedName("discount")
    private Double discount;

    @SerializedName("statuspayment")
    private Boolean statusPayment;

    @SerializedName("deliverystatus")
    private Boolean deliveryStatus;

    // Additional fields as required by the response

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Boolean getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(Boolean statusPayment) {
        this.statusPayment = statusPayment;
    }

    public Boolean getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
