package retrofit;
import com.google.gson.annotations.SerializedName;
public class ProductDetailResponse {
    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
