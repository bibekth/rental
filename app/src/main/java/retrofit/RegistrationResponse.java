package retrofit;

import java.util.Map;

public class RegistrationResponse {
    private String message;
    private Map<String, String> data; // Assuming data is a key-value pair object
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        if (data != null) {
            return data.get("userId"); // Adjust based on how data is structured
        }
        return null;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RegistrationResponse{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", status='" + status + '\'' +
                '}';
    }
}
