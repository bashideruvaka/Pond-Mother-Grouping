package eruvaka.pmgp.common;

import android.content.Context;
import android.util.Log;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eruvaka on 10-05-2017.
 */
public class Model {
    private static  String m_response;
    public static  String getResponse(final String pond_sno, String user_id, String timezone,Context getActivity) {
        try {
            Utils  util = new Utils(getActivity);
            JsonObject object = new JsonObject();
            object.addProperty("user_id", user_id);
            object.addProperty("pondsno", pond_sno);
            final Calendar calender1 = new GregorianCalendar(TimeZone.getTimeZone(timezone));
            final Date date1 = new Date(calender1.getTimeInMillis());
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone(timezone));
            String currenttimestr1 = format2.format(date1).toString().trim();
            object.addProperty("schedule_start_date", currenttimestr1);
            Call<JsonObject> call = util.getBaseClassService_feders().feedersdata(object);
            util.showProgressDialog();
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
                    if (response.isSuccessful()) {
                        m_response =  response.body().toString();
                        Log.e("FEEDER DETAILS", response.body().toString());
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("--", "onFailure : ");
                    t.printStackTrace();

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return  m_response;
    }
}
