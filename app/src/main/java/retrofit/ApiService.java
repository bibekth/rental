package retrofit;

import com.example.rental.Advertisement;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {

    // Login and Registration
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("register")
    Call<RegistrationResponse> register(@Body RegistrationRequest registrationRequest);

    // Category Fetching
    @GET("categories")
    Call<CategoryResponse> getCategories(@Header("Authorization") String token);

    // OTP Verification and Resending
    @POST("verify")
    Call<OtpVerificationResponse> verifyOtp(@Body OtpVerificationRequest otpVerificationRequest);

    @POST("resend")
    Call<OtpVerificationResponse> resendOtp(@Body OtpVerificationRequest resendOtpRequest);

    // Password Reset Operations
    @POST("forgot-password")
    Call<OtpVerificationResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("send-reset-password-otp")
    Call<OtpVerificationResponse> sendResetPasswordOtp(@Body OtpVerificationRequest request);

    @POST("resend-reset-password-otp")
    Call<OtpVerificationResponse> resendResetPasswordOtp(@Body OtpVerificationRequest request);

    @POST("reset-password")
    Call<OtpVerificationResponse> resetPassword(@Body ResetPasswordRequest request);

    // Product Handling
    @Multipart
    @POST("upload-product")
    Call<ProductResponse> addProduct(@Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("amount") RequestBody amount,
            @Part("category_id") RequestBody categoryId,
            @Part("purchase_date") RequestBody purchaseDate,
            @Part MultipartBody.Part photo
    );

    @GET("products")
    Call<ProductRequest> getProducts(@Header("Authorization") String token);

    @GET("my-products")
    Call<ProductRequest> getMyProducts(@Header("Authorization") String token);

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Header("Authorization") String token, @Path("id") int productId);


    @GET("products/{productId}")
    Call<ProductResponse> getProductDetails(@Header("Authorization") String token, @Path("productId") int productId);

    // Fetch comments for a product (identified by productId)
    @GET("show-comments/{id}")
    Call<CommentListResponse> getComments(
            @Header("Authorization") String authHeader,
            @Path("id") int productId
    );

    // Post a new comment
    @POST("post-comment")
    Call<ResponseBody> postComment(
            @Header("Authorization") String token,
            @Body CommentRequest commentRequest
    );

    // Delete a comment
    @DELETE("delete-comment/{id}")
    Call<ResponseBody> deleteComment(
            @Header("Authorization") String authHeader,
            @Path("id") int commentId
    );

    // Update a comment
    @FormUrlEncoded
    @POST("update-comment/{commentId}")
    Call<Void> updateComment(@Header("Authorization") String token, @Path("commentId") int commentId, @Field("comment_text") String commentText);

    // Wishlist Handling
    @FormUrlEncoded
    @POST("wishlist-store")
    Call<Void> addToWishlist(@Header("Authorization") String token, @Field("product_id") Integer productId);

    @DELETE("wishlist-delete/{productId}")
    Call<Void> removeFromWishlist(@Header("Authorization") String token, @Path("productId") int productId);

    // Fetch saved wishlist items
    @GET("wishlist-show")
    Call<List<WishlistResponse>> getSavedItems(@Header("Authorization") String token);

    // Order Handling
    @POST("place-order")
    Call<OrderResponse> submitOrder(
            @Header("Authorization") String token,
            @Body OrderRequest orderRequest
    );


    @GET("show-orders")
    Call<List<OrderResponse>> getOrders();

    @FormUrlEncoded
    @POST("update-order/{id}")
    Call<OrderResponse> updateOrder(
            @Path("id") int orderId,
            @Field("quantity") int quantity,
            @Field("order_date") String orderDate
    );

    @DELETE("delete-order/{id}")
    Call<ResponseBody> deleteOrder(@Path("id") int orderId);

    // Order Details Handling

    @POST("order-details-store")
    Call<OrderDetailResponse> submitOrderDetail(
            @Header("Authorization") String token, @Body OrderDetailRequest orderDetailRequest);

    @GET("order-details-show/{id}")
    Call<OrderDetailResponse> getOrderDetail(@Header("Authorization") String token, @Path("id") int orderDetailId);

    @FormUrlEncoded
    @POST("order-details-update/{id}")
    Call<OrderDetailResponse> updateOrderDetail(
            @Path("id") int orderDetailId,
            @Field("delivery_address") String deliveryAddress,
            @Field("payment_method") String paymentMethod,
            @Field("mobile_number") String mobileNumber,
            @Field("discount") Double discount,
            @Field("statuspayment") Boolean statusPayment,
            @Field("deliverystatus") Boolean deliveryStatus
    );

    @DELETE("order-details-destroy/{id}")
    Call<ResponseBody> deleteOrderDetail(@Path("id") int orderDetailId);

    // Advertisements and Saved Items
    @GET("advertisements")
    Call<List<Advertisement>> getAdvertisements();

    @GET("check-wishlist")
    Call<ProductResponse> checkWishlist(@Header("Authorization") String token, @Query("product_id") Integer product_id);

    @POST("product-rented")
    Call<ProductResponse> rentProduct(@Header("Authorization") String token, @Query("product_id") Integer product_id, @Query("month") String month);
}
