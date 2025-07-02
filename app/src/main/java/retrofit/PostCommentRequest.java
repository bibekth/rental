package retrofit;

public class PostCommentRequest {
    private String content; // Align with backend field name

    public PostCommentRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
