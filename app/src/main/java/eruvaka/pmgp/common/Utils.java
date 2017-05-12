package eruvaka.pmgp.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

import eruvaka.pmgp.R;
import eruvaka.pmgp.serverconnect.RetroHelper;
import eruvaka.pmgp.serverconnect.ServiceHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by bashid on 02/27/2016.
 */

public class Utils {
    private Context ctx;
    private static final String TAG = "Utils";
    MaterialDialog ringProgressDialog = null;
    Utils   util;
    public Utils(Context ctx) {
        this.ctx = ctx;
         util = new Utils(ctx);
    }
    public static void showAlertDialog(final Context context , String title , String message, DialogInterface.OnClickListener onClickListener){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // setting Dialog title
        alertDialog.setTitle(title);
        // setting Dialog message
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", onClickListener);
        alertDialog.show();

    }
    public void showProgressDialog() {
        if (ringProgressDialog == null) {
            ringProgressDialog = new MaterialDialog.Builder(ctx)
                    //.title(ctx.getResources().getString(R.string.app_name))
                    .content("Please wait.... ")
                    .progress(true, 0)
                    .theme(Theme.LIGHT)
                    .cancelable(false)
                    .show();
            ringProgressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    try {
                        ProgressBar v = (ProgressBar) ringProgressDialog.findViewById(android.R.id.progress);
                        v.getIndeterminateDrawable().setColorFilter(ctx.getResources().getColor(R.color.colorPrimaryDark),
                                android.graphics.PorterDuff.Mode.MULTIPLY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void dismissDialog() {
        if (ringProgressDialog != null) {
            if (ringProgressDialog.isShowing()) {
                ringProgressDialog.dismiss();
                ringProgressDialog = null;
            }
        }
    }
    /* Adapter Classes */
    public ServiceHelper getBaseClassService() {
       return new RetroHelper().getAdapter(ctx, "", null).create(ServiceHelper.class);
    }
    public ServiceHelper getBaseClassService_pond() {
        return new RetroHelper().getAdapter_pond(ctx, "", null).create(ServiceHelper.class);
    }
    public ServiceHelper getBaseClassService_feders() {
        return new RetroHelper().getAdapter_feeders(ctx, "", null).create(ServiceHelper.class);
    }
     public ServiceHelper getBaseClassService_update_schedules(){
         return new RetroHelper().getAdapter_schedule_update(ctx,"",null).create(ServiceHelper.class);
     }
    public ServiceHelper getAdapter_singlefeeder_update(){
        return new RetroHelper().getAdapter_singlefeeder_update(ctx,"",null).create(ServiceHelper.class);
    }
    public ServiceHelper getAdapter_feederLogs(){
        return new RetroHelper().getAdapter_feederlogs(ctx,"",null).create(ServiceHelper.class);
    }
    public ServiceHelper getAdapter_feederSetting_update(){
        return new RetroHelper().getAdapter_feederSettings(ctx,"",null).create(ServiceHelper.class);
    }
    /*  String Declaration  */
    private static String mpond_sno;
    public static void add_pondsSno(String pond_sno) {
        mpond_sno=pond_sno;
    }
    public static String  get_pond_Sno() {
        return mpond_sno;
    }
    private static String mfeeder_sno;
    public static void add_feederSno(String feeder_sno) {
        mfeeder_sno=feeder_sno;
    }
    public static String  get_feederSno_Sno() {
        return mfeeder_sno;
    }
    private void basic_mode_update_data(ArrayList<HashMap<String,String>> mylist,ArrayList<HashMap<String,String>> group_array) {
        Utils   util = new Utils(ctx);
        UserSession session = new UserSession(ctx);
       String  user_id = session.get("user_id");
       String timezone=session.get("timezone");
        ArrayList<String> feederSno_array=new ArrayList<String>();
        ArrayList<String> hex_id_arrays=new ArrayList<String>();
        try{
            feederSno_array.clear();
            hex_id_arrays.clear();
            JsonObject object =new JsonObject();
            object.addProperty("user_id",user_id);
            JSONObject loginJson1 = new JSONObject();
            JSONObject loginJson2 = new JSONObject();
            JSONObject loginJson3 = new JSONObject();
            JSONObject loginJson4 = new JSONObject();
            JSONObject loginJson5 = new JSONObject();
            JSONObject loginJson6 = new JSONObject();
            JSONObject loginJson7 = new JSONObject();
            JSONObject loginJson8 = new JSONObject();
            JSONObject loginJson9 = new JSONObject();
            JSONObject loginJson10 = new JSONObject();
            JSONObject loginJson11 = new JSONObject();
            JSONObject loginJson12 = new JSONObject();
            ArrayList<String> ids_array=new ArrayList<>();
            for (int i = 0; i < mylist.size(); i++) {
                final HashMap<String, String> map1 = mylist.get(i);
                final String feederSno = map1.get("feederSno").toString().trim();
                final String feederId=map1.get("feederId").toString().trim();
                final String hex_id = map1.get("hex_id").toString().trim();
                final String schedules=map1.get("schedules").toString().trim();
                feederSno_array.add("\"" + feederSno + "\"");
                hex_id_arrays.add("\"" + hex_id + "\"");
                HashSet<String> hashSet = new HashSet<String>();
                hashSet.addAll(feederSno_array);
                feederSno_array.clear();
                feederSno_array.addAll(hashSet);
                JSONArray tf_array=new JSONArray();
                JSONArray scid_array=new JSONArray();
                JSONArray ocf_array=new JSONArray();
                JSONArray fg_array=new JSONArray();
                JSONArray df_array=new JSONArray();
                JSONArray from_time_array=new JSONArray();
                JSONArray to_time_array=new JSONArray();
                JSONArray total_time_array=new JSONArray();
                JSONArray status_array=new JSONArray();
                JSONArray hex_id_array=new JSONArray();
                JSONArray mode_array=new JSONArray();
                JSONArray ontime_array=new JSONArray();
                JSONArray offtime_array=new JSONArray();
                final JSONArray jsonArray = new JSONArray(schedules);
                ids_array.clear();
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jObject = jsonArray.getJSONObject(x);
                    String schedule_id=jObject.getString("schedule_id");
                    String status_new=jObject.getString("status");
                    if(status_new.equals("completed")){
                        ids_array.add("0");
                    }else{
                        ids_array.add(schedule_id);

                    }
                }

                for (int j = 0; j < group_array.size(); j++) {
                    HashMap<String, String> map = group_array.get(j);
                    //String schedule_id = map.get("schedule_id").toString().trim();
                    //System.out.println("second"+schedule_id);
                    //ids_array.add(schedule_id);
                    HashSet<String> hashSet2 = new HashSet<String>();
                    hashSet2.addAll(ids_array);
                    ids_array.clear();
                    ids_array.addAll(hashSet2);
                    for(int y=0;y<ids_array.size();y++){
                        scid_array.put(ids_array.get(y));
                        loginJson4.put(feederSno,scid_array);
                    }
                    String from_time = map.get("from_time").toString().trim();
                    String to_time = map.get("to_time").toString().trim();
                    //String totalTime = map.get("totalTime").toString().trim();
                    String totalTime="00:00".toString().trim();
                    //String kg_feed_disp_time=map.get("kg_feed_disp_time").toString().trim();
                    String mode = map.get("mode").toString().trim();
                    String status = map.get("status").toString().trim();
                    // String dispensed_feed = map.get("dispensed_feed").toString().trim();
                    String total_feed2 = map.get("total_feed").toString().trim();
                    try {
                        float tf_fl = Float.parseFloat(total_feed2);
                        int s = mylist.size();
                        float size_fl = Float.parseFloat(Integer.toString(s));
                        float result = (tf_fl / size_fl);
                        String total_feed=Float.toString(result);
                        tf_array.put(total_feed);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    String ocf = map.get("ocf").toString().trim();
                    String fg = map.get("fg").toString().trim();
                    String to_be_run = "to_be_run".toString().trim();
                    String running = "running".toString().trim();
                    total_time_array.put(totalTime);
                    from_time_array.put(from_time);
                    to_time_array.put(to_time);
                    status_array.put(status);
                    ocf_array.put(ocf);
                    fg_array.put(fg);
                    mode_array.put(mode);
                    ontime_array.put("0");
                    offtime_array.put("0");
                    loginJson1.put(feederSno,tf_array);
                    loginJson2.put(feederSno,ocf_array);
                    loginJson3.put(feederSno,fg_array);
                    loginJson6.put(feederSno,total_time_array);
                    loginJson7.put(feederSno,from_time_array);
                    loginJson8.put(feederSno,to_time_array);
                    loginJson9.put(feederSno,status_array);
                    loginJson10.put(feederSno,mode_array);
                    loginJson11.put(feederSno,ontime_array);
                    loginJson12.put(feederSno,offtime_array);
                }
                object.addProperty("original_feed",  loginJson1.toString());
                object.addProperty("one_cycle_feed", loginJson2.toString());
                object.addProperty("feed_gap", loginJson3.toString());
                object.addProperty("schedule_id",  loginJson4.toString());
                object.addProperty("total_time",  loginJson6.toString());
                object.addProperty("from_time",  loginJson7.toString());
                object.addProperty("to_time", loginJson8.toString());
                object.addProperty("status", loginJson9.toString());
                object.addProperty("mode",  loginJson10.toString());
                object.addProperty("on_time", loginJson11.toString());
                object.addProperty("off_time",  loginJson12.toString());
            }

            object.addProperty("feederSno",  feederSno_array.toString());
            object.addProperty("feeder_hexid",  hex_id_arrays.toString());
            System.out.println(object);
            final Calendar calender1=new GregorianCalendar(TimeZone.getTimeZone(timezone));
            final Date date1 = new Date(calender1.getTimeInMillis());
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone(timezone));
            String currenttimestr1=format2.format(date1).toString().trim();
            object.addProperty("schedule_start_date", currenttimestr1);
            object.addProperty("schedule_end_date", currenttimestr1);
            Call<JsonObject> call = util.getBaseClassService_update_schedules().update(object);
            util.showProgressDialog();
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
                    if (response.isSuccessful()) {
                      //  feededit_Response(response.body());
                        Utils   util = new Utils(ctx);
                        util.dismissDialog();
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("--", "onFailure : ");
                    t.printStackTrace();
                    Utils   util = new Utils(ctx);
                    util.dismissDialog();
                }

            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void feededit_Response(JsonObject jsnobj) {
        try{
            String result = jsnobj.toString();
            JSONObject jsn = new JSONObject(result);
            String status = jsn.getString("status");
            String zero = "0".toString().trim();
            if (status.equals(zero)) {
                String error = jsn.getString("error");
                util.showAlertDialog(ctx, "Schedule Edit Response ", error, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            } else {
                JSONObject jsnonb = jsn.getJSONObject("data");
                String response=jsnonb.getString("response");
                Toast.makeText(ctx,response,Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
