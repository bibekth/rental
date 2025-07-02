package retrofit;

import com.google.gson.annotations.SerializedName;

public class CommentRequest {

    @SerializedName("product_id")
    private int productId;

    @SerializedName("content")
    private String content;

    public CommentRequest(int productId, String content) {
        this.productId = productId;
        this.content = content;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
