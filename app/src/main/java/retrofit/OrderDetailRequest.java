package retrofit;

import com.google.gson.annotations.SerializedName;

public class OrderDetailRequest {

    @SerializedName("order_id")
    private int orderId;

    @SerializedName("deliveryaddress")
    private String deliveryAddress;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("mobilenumber")
    private String mobileNumber;

    @SerializedName("discount")
    private Double discount;

    @SerializedName("statuspayment")
    private Boolean statusPayment;

    @SerializedName("deliverystatus")
    private Boolean deliveryStatus;

    public OrderDetailRequest(int orderId, String deliveryAddress, String paymentMethod, String mobileNumber, Double discount, Boolean statusPayment, Boolean deliveryStatus) {
        this.orderId = orderId;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.mobileNumber = mobileNumber;
        this.discount = discount;
        this.statusPayment = statusPayment;
        this.deliveryStatus = deliveryStatus;
    }

    // Getters and Setters
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
