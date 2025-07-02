package model;


import com.google.gson.annotations.SerializedName;

public class OrderModel {

    @SerializedName("product_id")
    private int productId;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("product_price")
    private double productPrice;

    @SerializedName("mobile_number")
    private String mobileNumber;

    @SerializedName("delivery_address")
    private String deliveryAddress;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("sub_total")
    private double subTotal;

    @SerializedName("grand_total")
    private double grandTotal;

    public OrderModel(int productId, String productName, double productPrice, String mobileNumber, String deliveryAddress, String paymentMethod, double subTotal, double grandTotal) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.mobileNumber = mobileNumber;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.subTotal = subTotal;
        this.grandTotal = grandTotal;
    }

    // Getters and Setters for each field
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }
}
