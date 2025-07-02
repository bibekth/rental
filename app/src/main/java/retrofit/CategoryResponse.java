package retrofit;

import java.util.List;
import model.Category;

public class CategoryResponse {

    private String message;
    private List<Category> data;

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for data (List of categories)
    public List<Category> getData() {
        return data;
    }

    // Setter for data (List of categories)
    public void setData(List<Category> data) {
        this.data = data;
    }
}
