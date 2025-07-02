package retrofit;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WishlistApiResponse {

    @SerializedName("data")
    private List<WishlistResponse> wishlist;

    // Handle cases where a string is returned instead of the expected array
    @SerializedName("message")
    private String message;

    public List<WishlistResponse> getWishlist() {
        return wishlist;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return message != null;
    }
}
