package retrofit;

public class LoginResponse {

    private String message;
    private Data data;

    // Getters and setters


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private User user;
        private String token;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public static class User {
            private int id;
            private String name;
            private String email;
            private String phonenumber;
            private String token;

            // Getters and setters
            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhonenumber() {
                return phonenumber;
            }

            public void setPhonenumber(String phonenumber) {
                this.phonenumber = phonenumber;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                " message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
