package retrofit;

import model.Product;
import java.util.List;

public class ProductRequest {
    private List<Product> data;
    private String status;

    // Getter for 'data'
    public List<Product> getData() {
        return data;
    }

    // Setter for 'data'
    public void setData(List<Product> data) {
        this.data = data;
    }

    // Getter for 'status'
    public String getStatus() {
        return status;
    }

    // Setter for 'status'
    public void setStatus(String status) {
        this.status = status;
    }
}
