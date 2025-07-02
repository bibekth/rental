package retrofit;

public class MyResponseWrapper<T> {
    private T data;

    // Getter and setter
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
