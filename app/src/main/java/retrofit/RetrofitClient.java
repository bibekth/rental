package retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.android.volley.BuildConfig;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static final String IMAGE_BASE_URL = "https://rental.techenfield.com/storage/";
    private static final String BASE_URL = "https://rental.techenfield.com/api/";
    private static final String BASIC_URL = "https://rental.techenfield.com/";

    private static final String PREFS_NAME = "SecondHandAppPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_NAME = "name";
    private static final String PREF_PHONENUMBER = "phonenumber";
    private static final String PREF_USER_ID = "user_id";

    private RetrofitClient() {
        // Private constructor to prevent instantiation
    }

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new ChuckerInterceptor(context))
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .build();

                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static ApiService getApiService(Context context) {
        return getClient(context).create(ApiService.class);
    }

    // Method to get the full image URL for product images
    public static String getProductImageUrl(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            String fullPath = IMAGE_BASE_URL + imagePath;
            Log.d("RetrofitClient", "Constructed Image URL: " + fullPath);
            return fullPath;
        } else {
            String placeholderUrl = BASIC_URL + "images/placeholder.png";
            Log.e("RetrofitClient", "Invalid imagePath: " + imagePath + ", returning placeholder URL");
            return placeholderUrl;
        }
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_TOKEN, null);
    }

    public static String getBearerToken(String token) {
        return "Bearer " + token;
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    public static void clearToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PREF_TOKEN);
        editor.apply();
    }

}
