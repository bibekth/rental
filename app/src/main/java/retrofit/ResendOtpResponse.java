package retrofit;

public class ResendOtpResponse {
    private String message;
    private boolean success;

    // Constructor
    public ResendOtpResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // Default constructor
    public ResendOtpResponse() {}

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
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

    @Override
    public String toString() {
        return "ResendOtpResponse{" +
                "message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
