package igor.com.br.tccrestwsandroid;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Igor on 12/02/2017.
 */

public class RetrofitUtil {
    public static final String BASE_URL = "http://192.168.0.10:8080/TccRestWS/rest/";

    public Retrofit createRetrofit(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        // Trailing slash is needed
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }



}
