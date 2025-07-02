package retrofit;

import java.util.List;

public class OtpVerificationResponse {
    private List<String> message;  // List to hold multiple messages
    private boolean success;
    private String token;  // Token, if provided in the response

    // Constructor
    public OtpVerificationResponse(List<String> message, boolean success, String token) {
        this.message = message;
        this.success = success;
        this.token = token;
    }

    // Getter for message
    public List<String> getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(List<String> message) {
        this.message = message;
    }

    // Getter for success
    public boolean isSuccess() {
        return success;
    }

    // Setter for success
    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Getter for token
    public String getToken() {
        return token;
    }

    // Setter for token
    public void setToken(String token) {
        this.token = token;
    }

    // Override the toString method to display the response contents
    @Override
    public String toString() {
        return "OtpVerificationResponse{" +
                "message=" + message +
                ", success=" + success +
                ", token='" + token + '\'' +
                '}';
    }
}
