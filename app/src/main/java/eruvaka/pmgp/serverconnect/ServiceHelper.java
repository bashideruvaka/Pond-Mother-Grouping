package eruvaka.pmgp.serverconnect;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;



public interface ServiceHelper {

    @POST(UrlData.URL_LOGIN)
    public Call<JsonObject> login(@Body JsonObject obj);

    @POST(UrlData.URL_PONDS)
    public Call<JsonObject> pondsdata(@Body JsonObject obj);

    @POST(UrlData.URL_FEEDERS)
    public Call<JsonObject> feedersdata(@Body JsonObject obj);

    @POST(UrlData.URL_UPDATE)
    public Call<JsonObject> update(@Body JsonObject obj);

    @POST(UrlData.URL_SINGLE_UPDATE)
    public Call<JsonObject> singlefeeder(@Body JsonObject obj);

    @POST(UrlData.URL_FEEDERS_LOGS)
    public Call<JsonObject> feederlogs(@Body JsonObject obj);

    @POST(UrlData.URL_FEEDERS_SETTINGS)
    public Call<JsonObject> feedersettings(@Body JsonObject obj);

    @POST(UrlData.URL_UPDATE_PROFILE)
    public Call<JsonObject> updateprofile(@Body JsonObject obj);

}


