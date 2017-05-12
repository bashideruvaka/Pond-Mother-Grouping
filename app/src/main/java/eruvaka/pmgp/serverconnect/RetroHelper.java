package eruvaka.pmgp.serverconnect;


import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroHelper {
      //login http://52.77.24.190/PondLogs_CD/mobile/pondlogs /login
    public static Retrofit getAdapter(Context context, String serverUrl, HashMap<String, String> headers) {
        String url = "http://52.77.24.190/PondLogs_CD/mobile/pondlogs/" + serverUrl;
       // https://pondlogs.com/mobile/pondlogs/login
        Log.e("RetroHelper", "url : " + url);
        Retrofit retrofit;
            OkHttpClient client = getRequestInterceptor(headers);
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        return retrofit;
    }
    //getponddetails
    public static Retrofit getAdapter_pond(Context context, String serverUrl, HashMap<String, String> headers) {
        String url = "http://52.77.24.190/PondLogs_CD/mobile/pondlogs/" + serverUrl;
        // https://pondlogs.com/mobile/pondlogs/login
        Log.e("RetroHelper", "url : " + url);
        Retrofit retrofit;
        OkHttpClient client = getRequestInterceptor(headers);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
    //pond_feeders
    public static Retrofit getAdapter_feeders(Context context, String serverUrl, HashMap<String, String> headers) {
        String url = "http://52.77.24.190/PondLogs_CD/mobile/pondlogs/" + serverUrl;

        Log.e("RetroHelper", "url : " + url);
        Retrofit retrofit;
        OkHttpClient client = getRequestInterceptor(headers);
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    // update schedules
    public static Retrofit getAdapter_schedule_update(Context context,String serverUrl,HashMap<String,String> headres){
        String url="http://52.77.24.190/PondLogs_CD/mobile/pondlogs/"+serverUrl;
        Log.e("RetrotHelper","url:"+url);
         Retrofit retrofit;
        OkHttpClient client=getRequestInterceptor(headres);
        retrofit =new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .client(client).build();

        return  retrofit;
    }
    //single feeder upadte /get_singlefeeder
    // update schedules
    public static Retrofit getAdapter_singlefeeder_update(Context context,String serverUrl,HashMap<String,String> headres){
        String url="http://52.77.24.190/mobile/pondmother_basicmodes/"+serverUrl;
        Log.e("RetrotHelper","url:"+url);
        Retrofit retrofit;
        OkHttpClient client=getRequestInterceptor(headres);
        retrofit =new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
        return  retrofit;
    }
    public static Retrofit getAdapter_feederlogs(Context context,String serverUrl,HashMap<String,String> headres){
        String url="http://52.77.24.190/eruvaka_live/mobile/pondmother_basicmodes/"+serverUrl;
        Retrofit retrofit;
        OkHttpClient client=getRequestInterceptor(headres);
        retrofit =new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
        return retrofit;
    }
    public static Retrofit getAdapter_feederSettings(Context context,String serverUrl,HashMap<String,String> headres){
        String url="http://52.77.24.190/eruvaka_live/mobile/pondmother_basicmodes/"+serverUrl;
        Retrofit retrofit;
        OkHttpClient client=getRequestInterceptor(headres);
        retrofit =new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
        return retrofit;
    }
    private static OkHttpClient getRequestInterceptor(final HashMap<String, String> headers) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(logging);
        if(headers!=null) {
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    Iterator<Map.Entry<String, String>> entrysList = headers.entrySet().iterator();
                    while (entrysList.hasNext()) {
                        Map.Entry<String, String> entry = entrysList.next();
                        requestBuilder.addHeader(entry.getKey(), entry.getValue()).build();
                    }
                    return chain.proceed(requestBuilder.build());
                }
            });
        }
        return httpClient.build();
    }

}
