package eruvaka.pmgp.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import eruvaka.pmgp.Activitys.SingleFeederActivty;
import eruvaka.pmgp.Database.DBHelper;
import eruvaka.pmgp.R;
import eruvaka.pmgp.classes.ScheduleSaveData;
import eruvaka.pmgp.common.MapComparator;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.common.Utillity;
import eruvaka.pmgp.common.Utils;
import eruvaka.pmgp.serverconnect.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedEditFragment extends Fragment implements  View.OnClickListener  {
    ActionBar bar;
    private static String TAG = "Feed Edit";
    View v;
    ArrayList<HashMap<String, String>> mylist=new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> schedules_list=new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> schedules_list1=new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> pondArrList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> group_array = new ArrayList<HashMap<String, String>>();
    ArrayList<Integer> ar = new ArrayList<Integer>();
    ArrayList<String> ar_str = new ArrayList<String>();
    Spinner feededit_pond_spinner;
    UserSession session;
    String PondName,user_id,ownerid,pond_sno;
    Utils util;
    LinearLayout  rowLayout,demo,feeder_rowLayout;
    DBHelper helper;
    SQLiteDatabase database;
    SQLiteStatement st;
    ArrayList<String> feederSno_array=new ArrayList<String>();
    ArrayList<String> feederSno_array2=new ArrayList<String>();
    ArrayList<String> hex_id_arrays=new ArrayList<String>();
    //TextView tetxview_edit;
    String timezone;
    View schedule_layout,basic_layout,header_layout;
    EditText totalfeed_et,ocf_et,feedgap_et,total_time,okdtime,dispense_feed_ed;
    int count = 0;
    Button group_update,group_basic_update;
    TextView pondname,dayfeed,alerts,start_date,tvstatus,past_schedule;
    LinearLayout total_layout,past_linear;
    ImageButton start,pause,stop;
    View v_line,view_line_header;
    private int mStartDay = 0;
    private int mStartMonth = 0;
    private int mStartYear = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.feededit, container, false);
        feededit_pond_spinner=(Spinner)v.findViewById(R.id.feededit_pond_spinner);
        feeder_rowLayout = (LinearLayout)v.findViewById(R.id.feeder_show);
        rowLayout = (LinearLayout)v.findViewById(R.id.updatetbl1);
        demo = (LinearLayout)v.findViewById(R.id.updatehome);
        schedule_layout = (View) v.findViewById(R.id.group_schedule_layout);
        basic_layout=(View)v.findViewById(R.id.group_basic_layout);
        header_layout=(View)v.findViewById(R.id.shedule_feed_header);
        total_layout=(LinearLayout)v.findViewById(R.id.total_layout);
        totalfeed_et=(EditText)v.findViewById(R.id.totalfeed);
        ocf_et=(EditText)v.findViewById(R.id.ocf);
        feedgap_et=(EditText)v.findViewById(R.id.feedgap);
        group_update=(Button)v.findViewById(R.id.group_basic_update);
        pondname=(TextView)v.findViewById(R.id.pond_name);
        dayfeed=(TextView)v.findViewById(R.id.day_feed);
        alerts=(TextView)v.findViewById(R.id.alerts);
        v_line=(View)v.findViewById(R.id.view_line);
        view_line_header=(View)v.findViewById(R.id.view_line_header);
        start_date = (TextView) v.findViewById(R.id.start_date);
        total_time=(EditText)v.findViewById(R.id.total_time);
        okdtime=(EditText)v.findViewById(R.id.okdtime);
        dispense_feed_ed=(EditText)v.findViewById(R.id.dispensedfeed);
        past_schedule = (TextView)v.findViewById(R.id.p_schedule);
        past_schedule.setVisibility(View.GONE);
        past_linear = (LinearLayout)v.findViewById(R.id.past_linear);
        group_basic_update=(Button)v.findViewById(R.id.group_basic_update);
        group_basic_update.setOnClickListener(this);
        tvstatus = (TextView)v.findViewById(R.id.tvstatus);
        start = (ImageButton)v.findViewById(R.id.start);
        pause = (ImageButton)v.findViewById(R.id.pause);
        stop = (ImageButton)v.findViewById(R.id.stop);
        //get utils dataview_line
        util = new Utils(getActivity());
        session = new UserSession(getActivity());
        user_id = session.get("user_id");
        ownerid = session.get("ownerid");
        PondName=session.get("pond_name");
        pond_sno=session.get("pond_sno");
        timezone=session.get("timezone");
        createStartDate();
        start_date.setOnClickListener(this);
        // add ponds to spinarray
        try {
            String pondarray= session.get("ponds_array");
            JSONArray ponds=new JSONArray(pondarray);
            pondArrList.clear();
             for (int i = 0; i <ponds.length(); i++) {
                HashMap<String, String> mapping = new HashMap<String, String>();
                JSONObject jObject = ponds.getJSONObject(i);
                mapping.put("pond_name", jObject.getString("pond_name"));
                mapping.put("pond_sno", jObject.getString("pond_sno"));
                pondArrList.add(mapping);
            }
            if(pondArrList.size()>0){
                pondsDisplay();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_date:
                startdialog();
                break;
            case R.id.group_basic_update:
                try {
                    String tfeed = totalfeed_et.getText().toString().trim();
                    String ocfeed = ocf_et.getText().toString().trim();
                    String fgp = feedgap_et.getText().toString().trim();
                   for (int j = 0; j < group_array.size(); j++) {
                        HashMap<String, String> map = group_array.get(j);
                        map.put("total_feed",tfeed);
                        map.put("ocf",ocfeed);
                        map.put("fg",fgp);
                     }
                    final Calendar end = Calendar.getInstance();
                    final Date date2 = new Date(end.getTimeInMillis() - (24 * 60 * 60 * 1000));
                    SimpleDateFormat dfDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    dfDate.setTimeZone(TimeZone.getTimeZone(timezone));
                    String str_date1 = start_date.getText().toString().trim();
                    java.util.Date d1 = dfDate.parse(str_date1);
                    String current_date = dfDate.format(System.currentTimeMillis());
                    String dispensedfeed = dispense_feed_ed.getText().toString().trim();
                    float tf = 0;
                    float df = 0;
                    String zero = "0".toString().trim();
                    try {
                        tf = Float.parseFloat(tfeed);
                        df = Float.parseFloat(dispensedfeed);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (tfeed.isEmpty() || ocfeed.isEmpty() || fgp.isEmpty() || tfeed.equals(zero) || ocfeed.equals(zero) || fgp.equals(zero)) {
                        Toast.makeText(getActivity(), R.string.nullvalues, Toast.LENGTH_SHORT).show();
                    } else if (tf < df) {
                        Toast.makeText(getActivity(), R.string.dispensedfeed, Toast.LENGTH_SHORT).show();
                    } else if (d1.before(date2)) {
                        Toast.makeText(getActivity(), R.string.pastdate, Toast.LENGTH_SHORT).show();
                    } else {
                       if(group_array.size()>0){
                         basicModeUpdateData();
                       }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
    }
    private void basicModeUpdateData() {
        try{
            ScheduleSaveData.add(mylist,group_array);
            feederSno_array.clear();
            hex_id_arrays.clear();
            JsonObject object =new JsonObject();
            object.addProperty("user_id",user_id);
            JSONObject tfObject = new JSONObject();
            JSONObject ocfObject = new JSONObject();
            JSONObject fgObject = new JSONObject();
            JSONObject scidObject = new JSONObject();
            JSONObject totalTimeObject = new JSONObject();
            JSONObject fromTimeObject = new JSONObject();
            JSONObject toTimeObject = new JSONObject();
            JSONObject statusObject = new JSONObject();
            JSONObject modeObject = new JSONObject();
            JSONObject onTimeObject = new JSONObject();
            JSONObject offtimeObject = new JSONObject();
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
                JSONArray mode_array=new JSONArray();
                JSONArray ontime_array=new JSONArray();
                JSONArray offtime_array=new JSONArray();
                final JSONArray jsonArray = new JSONArray(schedules);
                 if(jsonArray.length()>0){
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jObject = jsonArray.getJSONObject(x);
                        String schedule_id = jObject.getString("schedule_id");
                        scid_array.put(schedule_id);
                        scidObject.put(feederSno,scid_array);
                    }
                }else{
                     scid_array.put("0");
                     scidObject.put(feederSno,scid_array);
                 }
                for (int j = 0; j < group_array.size(); j++) {
                    HashMap<String, String> map = group_array.get(j);
                    String schedule_id = map.get("schedule_id").toString().trim();
                    System.out.println(schedule_id);
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
                    tfObject.put(feederSno,tf_array);
                    ocfObject.put(feederSno,ocf_array);
                    fgObject.put(feederSno,fg_array);
                    //scidObject.put(feederSno,scid_array);
                    totalTimeObject.put(feederSno,total_time_array);
                    fromTimeObject.put(feederSno,from_time_array);
                    toTimeObject.put(feederSno,to_time_array);
                    statusObject.put(feederSno,status_array);
                    modeObject.put(feederSno,mode_array);
                    onTimeObject.put(feederSno,ontime_array);
                    offtimeObject.put(feederSno,offtime_array);
                }

            }

            object.addProperty("original_feed",  tfObject.toString());
            object.addProperty("one_cycle_feed", ocfObject.toString());
            object.addProperty("feed_gap", fgObject.toString());
            object.addProperty("schedule_id",  scidObject.toString());
            object.addProperty("total_time",  totalTimeObject.toString());
            object.addProperty("from_time",  fromTimeObject.toString());
            object.addProperty("to_time", toTimeObject.toString());
            object.addProperty("status", statusObject.toString());
            object.addProperty("mode",  modeObject.toString());
            object.addProperty("on_time", onTimeObject.toString());
            object.addProperty("off_time",  offtimeObject.toString());
            object.addProperty("feederSno",  feederSno_array.toString());
            object.addProperty("feeder_hexid",  hex_id_arrays.toString());
            System.out.println(object);
            String currenttimestr1 = getCurrentDateInStringFormat("yyyy-MM-dd");
            object.addProperty("schedule_start_date", currenttimestr1);
            object.addProperty("schedule_end_date", currenttimestr1);
            Call<JsonObject> call = util.getBaseClassService_update_schedules().update(object);
            util.showProgressDialog();
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
                    if (response.isSuccessful()) {
                        feedEditResponse(response.body());
                        util.dismissDialog();
                    } else {
                    }
                    util.dismissDialog();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("--", "onFailure : ");
                    t.printStackTrace();
                    System.out.println(t.toString());
                    util.dismissDialog();
                }

            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @NonNull
    private String getCurrentDateInStringFormat(String format) {
        final Calendar calender1=new GregorianCalendar(TimeZone.getTimeZone(timezone));
        final Date date1 = new Date(calender1.getTimeInMillis());
        SimpleDateFormat format2 = new SimpleDateFormat(format, Locale.getDefault());
        format2.setTimeZone(TimeZone.getTimeZone(timezone));
        return format2.format(date1).toString().trim();
    }
    private void startdialog() {
        try {
            DatePickerStartFragment date = new DatePickerStartFragment();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            String from_date = start_date.getText().toString().trim();
            Date f_date = dateFormat.parse(from_date);
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
            format2.setTimeZone(TimeZone.getTimeZone(timezone));
            String str_date = (format2.format(f_date)).toString().trim();
            String[] split = str_date.split("-");
            final int day = Integer.valueOf(split[0]);
            final int month = Integer.valueOf(split[1]);
            final int year = Integer.valueOf(split[2]);
            Bundle args = new Bundle();
            args.putInt("year", year);
            args.putInt("month", month - 1);
            args.putInt("day", day);
            date.setArguments(args);
            date.setCallBack(ondate);
            date.show(getChildFragmentManager(), "Date Picker");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            try {
                final Calendar c = Calendar.getInstance();
                c.set(selectedYear, selectedMonth, selectedDay);
                mStartDay = selectedDay;
                mStartMonth = selectedMonth;
                mStartYear = selectedYear;
                final Calendar start = Calendar.getInstance();
                start.set(mStartYear, mStartMonth, mStartDay, 0, 0, 0);
                start.setTimeZone(TimeZone.getTimeZone(timezone));
                //long dt = start.getTimeInMillis();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                try {
                    SimpleDateFormat dfDate = new SimpleDateFormat("dd MMM yyyy");
                    dfDate.setTimeZone(TimeZone.getTimeZone(timezone));
                    java.util.Date d = null;
                    java.util.Date d1 = null;
                    final Calendar end = Calendar.getInstance();
                    final Date date2 = new Date(end.getTimeInMillis());
                    final Date date = new Date(start.getTimeInMillis());
                    String str_date = start_date.getText().toString().trim();
                    Calendar cal = Calendar.getInstance();
                    try {
                        d = dfDate.parse(str_date);
                        d1 = dfDate.parse(dfDate.format(cal.getTime()));
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    int diffInDays = (int) ((d1.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
                    int compare = -7;
                    if (diffInDays > compare) {
                        start_date.setText(dateFormat.format(date));
                        //LoadData();
                    } else {
                        start_date.setText(dateFormat.format(d1));
                        Toast.makeText(getActivity(), R.string.daterange, Toast.LENGTH_SHORT).show();
                    }
                     // Dateset();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private void createStartDate() {
        final Calendar calender = Calendar.getInstance();
        final Date date = new Date(calender.getTimeInMillis());
        mStartDay = calender.get(Calendar.DATE);
        mStartMonth = calender.get(Calendar.MONTH);
        mStartYear = calender.get(Calendar.YEAR);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        if (start_date != null) {
            start_date.setText(dateFormat.format(date));

        }
    }
    private void checkGroupEdit() {
        final ArrayList<String> basic_mode=new ArrayList<>();
        ArrayList<String> schedule_mode=new ArrayList<>();
        ArrayList<String> error_check=new ArrayList<>();
        ArrayList<String> schedules_check=new ArrayList<>();
        int count;
        for (int i = 0; i < mylist.size(); i++) {
            try {
            final HashMap<String, String> map = mylist.get(i);
            final String modee=map.get("mode").toString().trim();
            if(modee.equals("78")){
                basic_mode.add(modee);
            }else if(modee.equals("79")){
                schedule_mode.add(modee);
            }
            final String schedules = map.get("schedules").toString().trim();
                final JSONArray jsonArray = new JSONArray(schedules);
                count=jsonArray.length();
                if(jsonArray.length()>0){
                }else{
                    schedules_check.add("0");
                }
                int count2;
                error_check.clear();
                for(int j=1;j<mylist.size();j++){
                    final HashMap<String, String> map2 = mylist.get(j);
                    final String schedules2 = map2.get("schedules").toString().trim();
                    final JSONArray jsonArray2= new JSONArray(schedules2);
                    count2=jsonArray2.length();
                    if(count==count2){
                        String result=Utillity.check_schedules(jsonArray,jsonArray2);
                        if(result.equals("nullvalue")) {
                            error_check.add("not equal");
                        }else if(result.equals("equal")){
                        }else if(result.equals("not equal")){
                            error_check.add("not equal");
                        }
                    } else{
                        error_check.add("not equal");
                        break;
                    }


                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        int a=mylist.size();
        int b=basic_mode.size();
        int s=schedule_mode.size();
        int ar_sh=schedules_check.size();
        if(a==ar_sh){
            error_check.clear();
        }
        if(error_check.size()>0){
            schedule_layout.setVisibility(View.GONE);
            basic_layout.setVisibility(View.GONE);
            v_line.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Pond editing disabled due to feeder schedules mismatch",Toast.LENGTH_SHORT).show();
        }else{
            if(a==b){
                schedule_layout.setVisibility(View.GONE);
                basic_layout.setVisibility(View.VISIBLE);
                header_layout.setVisibility(View.GONE);
                view_line_header.setVisibility(View.GONE);
                basicModeGroupData();
            }else if(a==s){
                basic_layout.setVisibility(View.GONE);
                schedule_layout.setVisibility(View.VISIBLE);
                header_layout.setVisibility(View.VISIBLE);
                view_line_header.setVisibility(View.VISIBLE);
                scheduleModeGroupData();
            }else{
                schedule_layout.setVisibility(View.GONE);
                basic_layout.setVisibility(View.GONE);
                v_line.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"Pond editing disabled due to feeder schedules mismatch",Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void scheduleModeGroupData() {
               int size=mylist.size();
               Utillity.add_mylist_size(size);
            try{
                final HashMap<String, String> map = mylist.get(0);
                final String feederName=map.get("feederSno").toString().trim();
                final String feederId=map.get("feederId").toString().trim();
                final String hex_id=map.get("hex_id").toString().trim();
                final String schedule_date=map.get("schedule_date").toString().trim();
                final String last_update_time=map.get("last_update_time").toString().trim();
                final String modee=map.get("mode").toString().trim();
                final String data_interval=map.get("data_interval".toString().trim());
                Utillity.add_data_interval(data_interval);
                final String schedules=map.get("schedules").toString().trim();
                final JSONArray jsonArray = new JSONArray(schedules);
                schedules_list.clear();
                if(jsonArray.length()>0){
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jObject = jsonArray.getJSONObject(j);
                        final HashMap<String, String> map1 = new HashMap<String, String>();
                        final String schedule_times = jObject.getString("schedule_times");
                        final String original_feed = jObject.getString("original_feed");
                        float tf_fl = Float.parseFloat(original_feed);
                        int s = Utillity.get_mylist_size();
                        float size_fl = Float.parseFloat(Integer.toString(s));
                        float result=(tf_fl*size_fl);
                        String[] separated = schedule_times.split("-");
                        String from_time = separated[0];
                        String to_time = separated[1];
                        map1.put("schedule_id", jObject.getString("schedule_id"));
                        map1.put("from_time", from_time);
                        map1.put("to_time", to_time);
                        map1.put("original_feed",Float.toString(result));
                        map1.put("one_cycle_feed", jObject.getString("one_cycle_feed"));
                        map1.put("feed_gap", jObject.getString("feed_gap"));
                        map1.put("totalTime", "00:00");
                        map1.put("kg_feed_disp_time", jObject.getString("kg_feed_disp_time"));
                        map1.put("mode", jObject.getString("mode"));
                        map1.put("status", jObject.getString("status"));
                        map1.put("dispensed_feed", jObject.getString("dispensed_feed"));
                        map1.put("device_totalfeed", jObject.getString("device_totalfeed"));
                        map1.put("device_ocf", jObject.getString("device_ocf"));
                        map1.put("device_feedgap", jObject.getString("device_feedgap"));
                        schedules_list.add(map1);
                    }
                }else{
                    final HashMap<String, String> map1 = new HashMap<String, String>();
                    final String dt2= Utillity.getCurrenttime(getActivity());
                    map1.put("schedule_id", "0");
                    map1.put("from_time", dt2);
                    map1.put("to_time", dt2);
                    map1.put("original_feed"," ");
                    map1.put("one_cycle_feed"," ");
                    map1.put("feed_gap"," ");
                    map1.put("totalTime", "00:00");
                    map1.put("kg_feed_disp_time", "0");
                    map1.put("mode",modee);
                    map1.put("status", "to_be_run");
                    map1.put("dispensed_feed", "0");
                    map1.put("device_totalfeed", "0");
                    map1.put("device_ocf", "0");
                    map1.put("device_feedgap", "0");
                    schedules_list.add(map1);
                }
               // displayFeedHeader();
                showSchedules(modee,  schedule_date,  last_update_time);
            }catch (Exception e){
                e.printStackTrace();
            }

         }
    private void displayFeedHeader() {
        try {
            //add headerview
            View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.shedule_feed_header, null);
            demo.setVisibility(View.VISIBLE);
             demo.addView(headerView);
            View v = new View(getActivity());
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            v.setBackgroundResource(R.drawable.seprator2);
            demo.addView(v);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showSchedules(final String main_mode,final String schedule_date,final String last_update_time) {
        try{
           rowLayout.setVerticalScrollBarEnabled(true);
           rowLayout.removeAllViewsInLayout();
            final String data_interval=Utillity.get_data_interval();
            group_array.clear();
               for (int j = 0; j < schedules_list.size(); j++) {
                    final HashMap<String, String> map = schedules_list.get(j);
                    final HashMap<String, String> map1 = new HashMap<String, String>();
                    final String schedule_id = map.get("schedule_id");
                    final String totalTime =map.get("totalTime");
                    final String original_feed = map.get("original_feed");
                    final String one_cycle_feed = map.get("one_cycle_feed");
                    final String feed_gap = map.get("feed_gap");
                    final String kg_feed_disp_time = map.get("kg_feed_disp_time");
                    final String mode = map.get("mode");
                    final String status = map.get("status");
                    final String dispensed_feed = map.get("dispensed_feed");
                    final String device_totalfeed = map.get("device_totalfeed");
                    final String device_ocf = map.get("device_ocf");
                    final String device_feedgap = map.get("device_feedgap");
                    final String from_time = map.get("from_time");
                    final String to_time = map.get("to_time");
                    View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.shecdule_list_item, null);
                    final EditText fromtime=(EditText)itemView.findViewById(R.id.shedule_list_item_et_from_time);
                    final EditText totime=(EditText)itemView.findViewById(R.id.shedule_list_item_et_to_time);
                    final EditText total_feed=(EditText)itemView.findViewById(R.id.shedule_list_item_et_total_feed);
                    final EditText ocf=(EditText)itemView.findViewById(R.id.shedule_list_item_et_ocf_feed);
                    final EditText fg=(EditText)itemView.findViewById(R.id.shedule_list_item_et_fg);
                    final EditText df=(EditText)itemView.findViewById(R.id.shedule_list_item_et_df);
                    final TextView sub=(TextView) itemView.findViewById(R.id.shedule_list_item_tv_delete_item);
                   total_feed.setText(original_feed);
                   ocf.setText(one_cycle_feed);
                   fg.setText(feed_gap);
                   df.setText(dispensed_feed);
                   df.setEnabled(false);
                   View v=new View(getActivity());
                   v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                   v.setBackgroundResource(R.drawable.seprator2);
                   v.setPadding(0, 0, 0, 0);
                   sub.setId(j);
                   if (sub.getId() == 0) {
                       sub.setBackgroundResource(R.drawable.none);
                   } else {
                       sub.setBackgroundResource(R.drawable.rsub);
                   }
                   sub.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if (sub.getId() == 0) {
                               sub.setBackgroundResource(R.drawable.rsub);
                               Toast.makeText(getActivity(), " Cant  Removed This Record", Toast.LENGTH_SHORT).show();
                           } else {
                               try {
                                   schedules_list.remove(sub.getId());
                                   rowLayout.removeViewAt(sub.getId());
                                   showSchedules(main_mode,schedule_date,last_update_time);
                               } catch (Exception e) {
                                   e.printStackTrace();
                                   System.out.println(e.toString());
                               }
                           }
                       }
                   });
                   String to_date = "00:00";
                   Date t_date = null;
                   try {
                       final SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
                       dateFormat1.setTimeZone(TimeZone.getTimeZone(timezone));
                       Date f_date = dateFormat1.parse(from_time);
                       t_date = dateFormat1.parse(to_time);
                       SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
                        format2.setTimeZone(TimeZone.getTimeZone(timezone));
                       String str_date = (format2.format(f_date)).toString().trim();
                       to_date = (format2.format(t_date)).toString().trim();
                       fromtime.setText(str_date);
                       totime.setText(to_date);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                  try {

                      String currenttimestr =getCurrentDateInStringFormat("HH:mm");
                      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                       simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                      Date ftime = simpleDateFormat.parse(fromtime.getText().toString());
                      Date ctime = simpleDateFormat.parse(currenttimestr);
                      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                      sdf.setTimeZone(TimeZone.getTimeZone(timezone));
                      final String dt1 = sdf.format(ftime);
                      final String dt2 = sdf.format(ctime);
                      final String dt3 = sdf.format(t_date);
                      final String str = "0".toString().trim();
                      if (schedule_id.equals(str)) {
                          fromtime.setEnabled(true);
                          df.setVisibility(View.GONE);
                      } else {
                          if (dt1.compareTo(dt2) <= 0) {
                              fromtime.setEnabled(false);
                              fromtime.setBackgroundResource(R.drawable.roundedshape2);
                              sub.setBackgroundResource(R.drawable.none);
                              sub.setEnabled(false);
                          }
                          if (dt3.compareTo(dt2) <= 0) {
                              fromtime.setEnabled(false);
                              fromtime.setBackgroundResource(R.drawable.roundedshape2);
                              sub.setBackgroundResource(R.drawable.none);
                              sub.setEnabled(false);
                              total_feed.setEnabled(false);
                              ocf.setEnabled(false);
                              fg.setEnabled(false);
                          }
                      }
                      Animation anim = new AlphaAnimation(0.0f, 1.0f);
                      anim.setDuration(50);
                      anim.setStartOffset(20);
                      anim.setRepeatMode(Animation.REVERSE);
                      anim.setRepeatCount(Animation.INFINITE);
                      if (status.equals("to_be_run")) {
                          if (dt1.compareTo(dt2) <= 0) {
                          } else {
                              sub.setBackgroundResource(R.drawable.rsub);
                              sub.setEnabled(true);
                              df.setVisibility(View.GONE);
                          }

                      } else if (status.equals("running")) {
                          df.setTextColor(Color.parseColor("#24890d"));
                          df.startAnimation(anim);
                          if (dt3.compareTo(dt2) <= 0) {
                              totime.setText(to_date);
                          } else {

                          }
                      } else if (status.equals("changed")) {

                      } else if (status.equals("paused")) {
                          df.setTextColor(Color.parseColor("#24890d"));
                          df.startAnimation(anim);
                      } else if (status.equals("completed")) {
                          // count++;
                          total_feed.setEnabled(false);
                          ocf.setEnabled(false);
                          fg.setEnabled(false);
                          fromtime.setEnabled(false);
                          fromtime.setBackgroundResource(R.drawable.roundedshape2);
                          sub.setEnabled(false);
                      }
                      // if totime check with currennt time
                      if (dt3.compareTo(dt2) <= 0) {
                      } else {
                          if (status.equals("running")) {
                              try {
                                  ar.add(j);
                                  ar_str.add("running");
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                          if (status.equals("paused")) {
                              try {
                                  ar.add(j);
                                  ar_str.add("paused");
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                      }
                   // show only schedule mode are equal
                   if (main_mode.equals(mode)) {
                       rowLayout.addView(itemView);
                       //rowLayout.addView(v);
                       map1.put("schedule_id", schedule_id);
                       map1.put("from_time", fromtime.getText().toString());
                       map1.put("to_time", to_date);
                       map1.put("total_feed", total_feed.getText().toString());
                       map1.put("ocf", ocf.getText().toString());
                       map1.put("fg", fg.getText().toString());
                       map1.put("totalTime", totalTime);
                       map1.put("kg_feed_disp_time", kg_feed_disp_time);
                       map1.put("mode", mode);
                       map1.put("status", status);
                       map1.put("dispensed_feed", dispensed_feed);
                       map1.put("device_totalfeed", device_totalfeed);
                       map1.put("device_ocf", device_ocf);
                       map1.put("device_feedgap", device_feedgap);
                       group_array.add(map1);
                   }
                   // fromtime click event
                   fromtime.setOnClickListener(new View.OnClickListener() {

                       @Override
                       public void onClick(View v) {
                           // TODO Auto-generated method stub
                           try{
                               Date time=null;
                               try{
                                   final SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm",Locale.getDefault());
                                   timeformat.setTimeZone(TimeZone.getTimeZone(timezone));
                                   time=timeformat.parse(fromtime.getText().toString());
                                   map1.put("from_time", fromtime.getText().toString());

                               }catch(Exception e){
                                   e.printStackTrace();
                               }

                               final int fhour =time.getHours();
                               final  int fminute = time.getMinutes();

                               TimePickerDialog.OnTimeSetListener timesetlistener=new TimePickerDialog.OnTimeSetListener() {

                                   @Override
                                   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                       // TODO Auto-generated method stub
                                       String h1,m1;

                                       if(String.valueOf(hourOfDay).length()==1){
                                           h1=("0"+String.valueOf(hourOfDay));

                                       }else{
                                           h1=String.valueOf(hourOfDay);
                                       }

                                       if((String.valueOf(minute).length()==1)){
                                           m1=("0"+String.valueOf(minute));
                                       }else{
                                           m1=(String.valueOf(minute));
                                       }

                                       fromtime.setText(h1 + ":" +m1);
                                       map1.put("from_time", fromtime.getText().toString());

                                       try{
                                          final Calendar calender=new GregorianCalendar(TimeZone.getTimeZone(timezone));
                                              final Date date = new Date(calender.getTimeInMillis());
                                          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                           simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                                           String currenttimestr=simpleDateFormat.format(date).toString().trim();
                                           Date ctime = simpleDateFormat.parse(currenttimestr);

                                           Date ftime = simpleDateFormat.parse(fromtime.getText().toString());
                                           SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                           sdf.setTimeZone(TimeZone.getTimeZone(timezone));
                                           String stime=sdf.format(ftime);
                                           final String dt2=sdf.format(ctime);
                                           if(dt2.compareTo(stime)<=0){

                                           }else{
                                               final SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy",Locale.getDefault());
                                               format2.setTimeZone(TimeZone.getTimeZone(timezone));
                                              //String selected_date = start_date.getText().toString().trim();
                                               String selected_date = schedule_date.toString().trim();
                                               String current_date = format2.format(System.currentTimeMillis());
                                               if(current_date.equals(selected_date)){
                                                   fromtime.setText(dt2);
                                                   Toast.makeText(getActivity(), "Past Time not Allowed", Toast.LENGTH_SHORT).show();
                                               }

                                           }
                                       }catch(Exception e){
                                           e.printStackTrace();
                                       }
                                       try{

                                           if(status.equals("running")||status.equals("paused")){
                                               String reman_time=Utillity.set_Dispensedfeed(getActivity(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                       ocf.getText().toString().trim(),fg.getText().toString().trim());
                                               String end_time=Utillity.get_to_time(getActivity(),fromtime.getText().toString(),reman_time,timezone);
                                               totime.setText(end_time);
                                               map1.put("to_time", totime.getText().toString());

                                               }else if(status.equals("to_be_run")){
                                               String last_update_time="00:00".toString().trim();
                                               String reman_time=Utillity.set_Dispensedfeed(getActivity(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                       ocf.getText().toString().trim(),fg.getText().toString().trim());
                                               String end_time=Utillity.get_to_time2(getActivity(),fromtime.getText().toString(),reman_time,timezone);
                                               totime.setText(end_time);
                                               map1.put("to_time", totime.getText().toString());
                                           }else{

                                           }

                                       }catch(Exception e){
                                           e.printStackTrace();
                                       }
                                   }
                               };

                               TimePickerDialog tpd=new TimePickerDialog(getActivity(), timesetlistener, fhour, fminute,  DateFormat.is24HourFormat(getActivity()));
                               tpd.show();

                           }catch(Exception e){
                               e.printStackTrace();
                           }


                       }
                   });
                   //
                   total_feed.addTextChangedListener(new TextWatcher() {

                       @Override
                       public void onTextChanged(CharSequence s, int start, int before, int count) {
                           // TODO Auto-generated method stub

                       }

                       @Override
                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                           // TODO Auto-generated method stub

                       }

                       @Override
                       public void afterTextChanged(Editable s) {
                           // TODO Auto-generated method stub
                           map1.put("total_feed", s.toString());
                           try{
                               if(dt3.compareTo(dt2)<=0){
                               map1.put("to_time", totime.getText().toString());
                           }else{
                                 try{
                                   if(status.equals("running")||status.equals("paused")){
                                       String reman_time=Utillity.set_Dispensedfeed(getActivity(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                               ocf.getText().toString().trim(),fg.getText().toString().trim());
                                       String end_time=Utillity.get_to_time(getActivity(),fromtime.getText().toString(),reman_time,timezone);
                                       totime.setText(end_time);
                                       map1.put("to_time", totime.getText().toString());
                                  }else if(status.equals("to_be_run")){
                                       String last_update_time="00:00".toString().trim();
                                       System.out.println(data_interval);
                                       String reman_time=Utillity.set_Dispensedfeed(getActivity(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                               ocf.getText().toString().trim(),fg.getText().toString().trim());
                                       String end_time=Utillity.get_to_time2(getActivity(),fromtime.getText().toString(),reman_time,timezone);
                                       totime.setText(end_time);
                                       map1.put("to_time", totime.getText().toString());

                                   }
                               }catch(Exception e){
                                   e.printStackTrace();
                               }
                          }
                           }catch(Exception e){
                               e.printStackTrace();
                           }
                       }
                   });
                   ocf.addTextChangedListener(new TextWatcher() {

                       @Override
                       public void onTextChanged(CharSequence s, int start, int before, int count) {
                           // TODO Auto-generated method stub

                       }

                       @Override
                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                           // TODO Auto-generated method stub

                       }

                       @Override
                       public void afterTextChanged(Editable s) {
                           // TODO Auto-generated method stub
                           map1.put("ocf", s.toString());
                           try{if(dt3.compareTo(dt2)<=0){
                               map1.put("to_time", totime.getText().toString());
                           }else{
                               try{

                                   if(status.equals("running")||status.equals("paused")){
                                       String reman_time=Utillity.set_Dispensedfeed(getActivity(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                               ocf.getText().toString().trim(),fg.getText().toString().trim());
                                       String end_time=Utillity.get_to_time(getActivity(),fromtime.getText().toString(),reman_time,timezone);
                                       totime.setText(end_time);
                                       map1.put("to_time", totime.getText().toString());
                                   }else if(status.equals("to_be_run")){
                                       String last_update_time="00:00".toString().trim();
                                       String reman_time=Utillity.set_Dispensedfeed(getActivity(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                               ocf.getText().toString().trim(),fg.getText().toString().trim());
                                       String end_time=Utillity.get_to_time2(getActivity(),fromtime.getText().toString(),reman_time,timezone);
                                       totime.setText(end_time);
                                       map1.put("to_time", totime.getText().toString());
                                   }

                               }catch(Exception e){
                                   e.printStackTrace();
                               }


                           }


                           }catch(Exception e){
                               e.printStackTrace();
                           }
                       }
                   });
                   fg.addTextChangedListener(new TextWatcher() {

                       @Override
                       public void onTextChanged(CharSequence s, int start, int before, int count) {
                           // TODO Auto-generated method stub

                       }

                       @Override
                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                           // TODO Auto-generated method stub

                       }

                       @Override
                       public void afterTextChanged(Editable s) {
                           // TODO Auto-generated method stub
                           map1.put("fg", s.toString());
                           try{
                               if(dt3.compareTo(dt2)<=0){
                                   map1.put("to_time", totime.getText().toString());
                               }else{
                                   try{

                                       if(status.equals("running")||status.equals("paused")){
                                           String reman_time=Utillity.set_Dispensedfeed(getActivity(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                   ocf.getText().toString().trim(),fg.getText().toString().trim());
                                           String end_time=Utillity.get_to_time(getActivity(),fromtime.getText().toString(),reman_time,timezone);
                                           totime.setText(end_time);
                                           map1.put("to_time", totime.getText().toString());

                                       }else if(status.equals("to_be_run")){
                                           String last_update_time="00:00".toString().trim();
                                           String reman_time=Utillity.set_Dispensedfeed(getActivity(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                   ocf.getText().toString().trim(),fg.getText().toString().trim());
                                           String end_time=Utillity.get_to_time2(getActivity(),fromtime.getText().toString(),reman_time,timezone);
                                           totime.setText(end_time);
                                           map1.put("to_time", totime.getText().toString());
                                       }

                                   }catch(Exception e){
                                       e.printStackTrace();
                                   }


                               }


                           }catch(Exception e){
                               e.printStackTrace();
                           }
                       }
                   });
                  }catch (Exception e){
                  }
                 }
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.status_view, null);
            final TextView start_tv=(TextView) itemView.findViewById(R.id.start);
            final TextView pause_tv=(TextView) itemView.findViewById(R.id.pause);
            final TextView stop_tv=(TextView) itemView.findViewById(R.id.stop);
            final TextView last_update_time_tv=(TextView) itemView.findViewById(R.id.last_update_time);
            final TextView  add_new=(TextView)itemView.findViewById(R.id.add_new);
            final TextView  group_schedule_update=(TextView)itemView.findViewById(R.id.group_schedule_update);
            rowLayout.addView(itemView);
            add_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final HashMap<String, String> map1 = new HashMap<String, String>();
                    final String dt2= Utillity.getCurrenttime(getActivity());
                    map1.put("schedule_id", "0");
                    map1.put("from_time", dt2);
                    map1.put("to_time", dt2);
                    map1.put("total_feed","0");
                    map1.put("ocf","0");
                    map1.put("fg","0");
                    map1.put("totalTime", "00:00");
                    map1.put("kg_feed_disp_time", "0");
                    map1.put("mode",main_mode);
                    map1.put("status", "to_be_run");
                    map1.put("dispensed_feed", "0");
                    map1.put("device_totalfeed", "0");
                    map1.put("device_ocf", "0");
                    map1.put("device_feedgap", "0");
                    schedules_list.add(map1);
                    showSchedules(main_mode,schedule_date,last_update_time);
                }
            });
            group_schedule_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scheduleModeCheckData();
                    //schedule_mode_update_data();
                }
            });
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            Date f_date = dateFormat.parse(schedule_date);
            final SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone(timezone));
            String schedule_last_date = format2.format(f_date);
            String current_date = format2.format(System.currentTimeMillis());
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(50); //You can manage the time of the blink with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            if (last_update_time.isEmpty()) {

            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Date ftime = simpleDateFormat.parse(last_update_time);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                sdf.setTimeZone(TimeZone.getTimeZone(timezone));
                String dt1 = sdf.format(ftime);
                if (current_date.equals(schedule_last_date)) {
                    last_update_time_tv.setText(dt1);
                    last_update_time_tv.setTextColor(Color.parseColor("#24890d"));
                    //last_update_time_tv.startAnimation(anim);
                } else {
                    last_update_time_tv.setText("Last Updated At: " + schedule_last_date + " " + dt1);

                }
            }
            //
            start_tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        if (ar.size() > 0) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map = schedules_list.get(ar.get(0));

                            map.put("status", "running");
                            stop_tv.setVisibility(View.VISIBLE);
                            pause_tv.setVisibility(View.VISIBLE);
                            start_tv.setVisibility(View.INVISIBLE);
                            pause_tv.setBackgroundResource(R.drawable.p1);
                            stop_tv.setBackgroundResource(R.drawable.stop1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            pause_tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        if (ar.size() > 0) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map = schedules_list.get(ar.get(0));
                            map.put("status", "paused");
                            start_tv.setVisibility(View.VISIBLE);
                            stop_tv.setVisibility(View.VISIBLE);
                            pause_tv.setVisibility(View.INVISIBLE);
                            start_tv.setBackgroundResource(R.drawable.s1);
                            stop_tv.setBackgroundResource(R.drawable.stop1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            stop_tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        if (ar.size() > 0) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map = schedules_list.get(ar.get(0));
                            map.put("status", "mcompleted");
                            start_tv.setBackgroundResource(R.drawable.s1);
                            start_tv.setVisibility(View.VISIBLE);
                            stop_tv.setVisibility(View.INVISIBLE);
                            pause_tv.setVisibility(View.INVISIBLE);
                            final Calendar calender = new GregorianCalendar(TimeZone.getTimeZone(timezone));
                            calender.add(Calendar.MINUTE, 1);
                            final Date date = new Date(calender.getTimeInMillis());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                            String currenttimestr = simpleDateFormat.format(date).toString().trim();
                            map.put("to_time", currenttimestr);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //
            if (ar.size() > 0) {
                int get_id = ar.get(0);
                String str=ar_str.get(0);
                if (get_id == 99) {
                    pause_tv.setKeyListener(null);
                    stop_tv.setKeyListener(null);
                    start_tv.setKeyListener(null);
                } else {
                    if (str.equals("running")) {
                        pause_tv.setBackgroundResource(R.drawable.p1);
                        stop_tv.setBackgroundResource(R.drawable.stop1);
                    }else if(str.equals("paused")){
                        start_tv.setBackgroundResource(R.drawable.s1);
                        stop_tv.setBackgroundResource(R.drawable.stop1);
                    }
                    }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void scheduleModeCheckData() {
        try{
            if (group_array.size() > 0) {
                ArrayList<String> error = new ArrayList<String>();
                for (int i = 0; i < group_array.size(); i++) {
                    HashMap<String, String> map = group_array.get(i);
                    String schedule_id = map.get("schedule_id").toString().trim();
                    String from_time = map.get("from_time").toString().trim();
                    String to_time = map.get("to_time").toString().trim();
                    String totalTime = map.get("totalTime").toString().trim();
                    //String kg_feed_disp_time=map.get("kg_feed_disp_time").toString().trim();
                    String mode = map.get("mode").toString().trim();
                    String status = map.get("status").toString().trim();
                    String dispensed_feed = map.get("dispensed_feed").toString().trim();
                    String total_feed = map.get("total_feed").toString().trim();
                    String ocf = map.get("ocf").toString().trim();
                    String fg = map.get("fg").toString().trim();
                    float tf = 0;
                    float df = 0;
                    String to_be_run = "to_be_run".toString().trim();
                    String running = "running".toString().trim();
                    try {
                        df = Float.parseFloat(dispensed_feed);
                        tf = Float.parseFloat(total_feed);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (from_time.isEmpty() || from_time.equals("0") || to_time.isEmpty() || to_time.equals("0") || total_feed.equals("0") || total_feed.isEmpty() || ocf.equals("0") || ocf.isEmpty() || fg.isEmpty() || fg.equals("0")) {
                        error.add(" Empty or Zero Values not Allowed in Schedule" + (i + 1));
                    } else if (status.equals(to_be_run) || status.equals(running)) {
                        if (tf < df) {
                            error.add("Total Feed Should  be Greater  Than Dispensed Feed at Schedule" + (i + 1) + " df " + dispensed_feed);
                        }

                    }
                    String mmcompleted = "mcompleted".toString().trim();
                    if (status.equals(mmcompleted)) {
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        map2 = group_array.get(i);
                        final Calendar calender = new GregorianCalendar(TimeZone.getTimeZone(timezone));
                        //calender.add(Calendar.MINUTE, 1);
                        final Date date = new Date(calender.getTimeInMillis());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                        String currenttimestr = simpleDateFormat.format(date).toString().trim();
                        map2.put("status", "completed");
                        map2.put("to_time", currenttimestr);
                        if (status.equals(to_be_run)) {
                            final Calendar calender2 = new GregorianCalendar(TimeZone.getTimeZone(timezone));
                            calender2.add(Calendar.MINUTE, 1);
                            final Date date2 = new Date(calender2.getTimeInMillis());
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                            simpleDateFormat2.setTimeZone(TimeZone.getTimeZone(timezone));
                            String currenttimestr2 = simpleDateFormat2.format(date).toString().trim();
                            map2.put("from_time", currenttimestr);
                        }
                    }
                }
                final Calendar end = Calendar.getInstance();
                final Date date2 = new Date(end.getTimeInMillis() - (24 * 60 * 60 * 1000));
                SimpleDateFormat dfDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                dfDate.setTimeZone(TimeZone.getTimeZone(timezone));
                String str_date1 =start_date.getText().toString().trim();
                java.util.Date d1 = dfDate.parse(str_date1);
                if (error.size() > 0) {
                    Toast.makeText(getActivity(), error.get(0), Toast.LENGTH_SHORT).show();
                } else if (d1.before(date2)) {
                    Toast.makeText(getActivity(), R.string.pastdate, Toast.LENGTH_SHORT).show();

                } else {
                    if (updateSchedules()) {
                        scheduleModeUpdateData();

                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private boolean updateSchedules() {

        try {
            Collections.sort(group_array, new MapComparator("from_time"));
            if (group_array.size() > 0) {
                for (int j = 0; j < group_array.size(); j++) {
                    for (int x = j + 1; x < group_array.size(); x++) {
                        final HashMap<String, String> map1 = group_array.get(j);
                        final HashMap<String, String> map2 = group_array.get(x);
                        String from_time_str = map1.get("from_time").toString().trim();
                        String to_time_str = map1.get("to_time").toString().trim();
                        String from_time_str2 = map2.get("from_time").toString().trim();
                        String status = map1.get("status").toString().trim();
                        String completed = "completed".toString().trim();
                        if (status.equals(completed)) {

                        } else {
                            if (compareValues(from_time_str, to_time_str) == true) {
                                //check schedules fromtime ovelapping

                                Toast.makeText(getActivity(), "Please Check Schedule" + (j + 1) + "Timings !!!", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            if (compareValues(from_time_str, from_time_str2) == true) {
                                // fromtime with fromtime overlapping
                                Toast.makeText(getActivity(), "Schedule  " + (x + 1) + " FromTime OverLapping With Schedule " + (j + 1) + " From Time !!!", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            if (compareValues(to_time_str, from_time_str2) == true) {
                                // fromtime with totime overlapping

                                Toast.makeText(getActivity(), "Schedule  " + (x + 1) + "  From Time OverLapping With Schedule " + (j + 1) + " To Time !!!", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }


                    }


                }

            }else{

                Toast.makeText(getActivity(),"schedule are empty please try again",Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  true;
    }
    boolean compareValues(String fromTime, String toTime) {
        try {
            final SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
            timeformat.setTimeZone(TimeZone.getTimeZone(timezone));
            Date time1 = timeformat.parse(fromTime);
            int fromhour = time1.getHours();
            int fromminute = time1.getMinutes();
            Date time2 = timeformat.parse(toTime);
            int tohour = time2.getHours();
            int tominute = time2.getMinutes();
            //Log.e(TAG, "compareValues:: fromhour:"+fromhour + ":fromminute:"+fromminute+":tohour:"+tohour+":tominute:"+tominute);
            // do not comment on this time and accept it as correct time. Here Toast message is not required to show. so return false.
            if ((23 == tohour) && (59 == tominute) && (fromhour == 23) && (fromminute == 59)) {
                return false;
            }
            // wrong time. FromHour > ToHour. return true to show Toast message to user.
            if (fromhour > tohour) {
                return true;
            }
            // wrong time. FromHour = ToHour, but FromMins >= ToMins. return true to show Toast message to user.
            else if ((fromhour == tohour) && (fromminute >= tominute)) {
                return true;
            }

        } catch (Exception e) {
            //Log.e(TAG, "compareValues:: Exception "+e);
        }
        // correct time
        return false;
    }
    private void scheduleModeUpdateData() {
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
                     // scid_array.put( schedule_id);
                      ids_array.add(schedule_id);

                  }
                    System.out.println(ids_array);
                  }

                for (int j = 0; j < group_array.size(); j++) {
                    HashMap<String, String> map = group_array.get(j);
                    String schedule_id = map.get("schedule_id").toString().trim();
                    //System.out.println("second"+schedule_id);
                      scid_array.put(schedule_id);
                     loginJson4.put(feederSno,scid_array);
                    HashSet<String> hashSet2 = new HashSet<String>();
                    hashSet2.addAll(ids_array);
                    ids_array.clear();
                    ids_array.addAll(hashSet2);
                    for(int y=0;y<ids_array.size();y++){
                     //   scid_array.put(ids_array.get(y));
                      //  loginJson4.put(feederSno,scid_array);
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
                      feedEditResponse(response.body());
                        util.dismissDialog();
                    } else {
                    }
                    util.dismissDialog();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("--", "onFailure : ");
                    t.printStackTrace();
                    System.out.println(t.toString());
                    util.dismissDialog();
                }

            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void basicModeGroupData() {
        schedules_list1.clear();
        final String dt2 = Utillity.getCurrenttime(getActivity());
        schedules_list1=Utillity.getJosnarray(mylist,dt2);
        group_array.clear();
        try {
            for(int i=0;i<schedules_list1.size();i++) {
                final HashMap<String, String> map = schedules_list1.get(i);
                final String schedule_id = map.get("schedule_id").toString().trim();
                final String original_feed = map.get("original_feed").toString().trim();
                final String one_cycle_feed = map.get("one_cycle_feed").toString().trim();
                final String feed_gap = map.get("feed_gap").toString().trim();
                final String mode = map.get("mode").toString().trim();
                final String kg_feed_disp_time = map.get("kg_feed_disp_time").toString().trim();
                final String status = map.get("status").toString().trim();
                final String dispensed_feed = map.get("dispensed_feed").toString().trim();
                final String device_totalfeed = map.get("device_totalfeed").toString().trim();
                final String device_ocf = map.get("device_ocf").toString().trim();
                final String device_feedgap = map.get("device_feedgap").toString().trim();
                final String totalTime=map.get("totalTime").toString().trim();
                final String schedule_times=map.get("schedule_times").toString().trim();
                totalfeed_et.setText(original_feed);
                totalfeed_et.setSelection(totalfeed_et.getText().length());
                ocf_et.setText(one_cycle_feed);
                ocf_et.setSelection(ocf_et.getText().length());
                feedgap_et.setText(feed_gap);
                feedgap_et.setSelection(feedgap_et.getText().length());
                try {
                    String tf = totalfeed_et.getText().toString().trim();
                    String df = dispensed_feed.toString().trim();
                    String cf = ocf_et.getText().toString().trim();
                    String fg = feedgap_et.getText().toString().trim();
                    float f_tf = Float.parseFloat(tf);
                    float f_df = Float.parseFloat(df);
                    float f_cf = Float.parseFloat(cf);
                    float f_fg = Float.parseFloat(fg);
                    float cycles = ((f_tf - f_df) * 1000) / f_cf;
                    float ttl_time = cycles * f_fg;
                    int tt = (int) ttl_time;
                    int hours = tt / 60;
                    int minutes = tt % 60;
                    total_time.setText(hours + " : " + minutes);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (status.equals("to_be_run")) {
                    start.setBackgroundResource(R.drawable.s1);
                    pause.setVisibility(View.INVISIBLE);
                    stop.setVisibility(View.INVISIBLE);
                    tvstatus.setText("to_be_run");
                } else if (status.equals("running")) {
                    pause.setBackgroundResource(R.drawable.p1);
                    stop.setBackgroundResource(R.drawable.stop1);
                    start.setVisibility(View.INVISIBLE);
                    tvstatus.setText("running");

                } else if (status.equals("changed")) {
                    pause.setBackgroundResource(R.drawable.p1);
                    stop.setBackgroundResource(R.drawable.stop1);
                    start.setVisibility(View.INVISIBLE);
                    tvstatus.setText("changed");
                } else if (status.equals("paused")) {
                    start.setBackgroundResource(R.drawable.s1);
                    stop.setBackgroundResource(R.drawable.stop1);
                    pause.setVisibility(View.INVISIBLE);
                    tvstatus.setText("paused");
                } else {
                    start.setBackgroundResource(R.drawable.s1);
                    stop.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.INVISIBLE);
                    tvstatus.setText("completed");
                }
                final HashMap<String, String> map1 = new HashMap<String, String>();
                start.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        pause.setBackgroundResource(R.drawable.p1);
                        stop.setBackgroundResource(R.drawable.stop1);
                        stop.setVisibility(View.VISIBLE);
                        pause.setVisibility(View.VISIBLE);
                        start.setVisibility(View.INVISIBLE);
                        tvstatus.setText("running");
                        map1.put("status", tvstatus.getText().toString().trim());
                    }
                });
                pause.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        start.setBackgroundResource(R.drawable.s1);
                        stop.setBackgroundResource(R.drawable.stop1);
                        start.setVisibility(View.VISIBLE);
                        stop.setVisibility(View.VISIBLE);
                        pause.setVisibility(View.INVISIBLE);
                        tvstatus.setText("paused");
                        map1.put("status", tvstatus.getText().toString().trim());
                    }
                });
                stop.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        start.setBackgroundResource(R.drawable.s1);
                        start.setVisibility(View.VISIBLE);
                        stop.setVisibility(View.INVISIBLE);
                        pause.setVisibility(View.INVISIBLE);
                        tvstatus.setText("completed");
                        map1.put("status", tvstatus.getText().toString().trim());

                    }
                });
                group_array.clear();

                final String status1 = tvstatus.getText().toString().trim();
                if (status1.equals("completed")) {
                    map1.put("schedule_id", "0");
                    map1.put("schedule_times", "00:00");
                    map1.put("totalTime", "00:00");
                    map1.put("from_time", "00:00");
                    map1.put("to_time", "00:00");
                    map1.put("mode", mode);
                    map1.put("dispensed_feed",dispensed_feed);
                    tvstatus.setVisibility(View.VISIBLE);
                    //tvstatus.setText("running");
                    map1.put("status", tvstatus.getText().toString().trim());
                    totalfeed_et.setText(" ");
                    ocf_et.setText("");
                    feedgap_et.setText("");
                    okdtime.setText(" ");
                    total_time.setText(" ");
                    dispense_feed_ed.setText(dispensed_feed);
                  //  past_schedule.setVisibility(View.VISIBLE);
                   // past_linear.setVisibility(View.VISIBLE);
                    map1.put("total_feed",original_feed);
                    map1.put("ocf",one_cycle_feed);
                    map1.put("fg",feed_gap);
                    View itemView_home = LayoutInflater.from(getActivity()).inflate(R.layout.pastschedule_home, null);
                    View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.pastschedule_show, null);
                    final TextView l_stime=(TextView)itemView.findViewById(R.id.last_stime);
                    final TextView l_tf=(TextView)itemView.findViewById(R.id.last_tf);
                    final TextView l_fg=(TextView)itemView.findViewById(R.id.last_fg);
                    final TextView l_ocf=(TextView)itemView.findViewById(R.id.last_ocf);
                    final TextView l_df=(TextView)itemView.findViewById(R.id.disp_feed);
                    l_stime.setText(schedule_times);
                    l_tf.setText(original_feed + " Kg");
                    l_fg.setText(feed_gap + " min");
                    l_ocf.setText(one_cycle_feed + " gms");
                    l_df.setText(dispensed_feed + " Kg");
                    //past_linear.addView(itemView_home);
                    //past_linear.addView(itemView);
                    group_array.add(map1);
                } else {
                    map1.put("schedule_id", schedule_id);
                    String[] separated = schedule_times.split("-");
                    map1.put("schedule_times", schedule_times);
                    map1.put("from_time", separated[0]);
                    map1.put("to_time", separated[1]);
                    map1.put("totalTime", totalTime);
                    map1.put("mode", mode);
                    map1.put("dispensed_feed",dispensed_feed);
                    map1.put("status", tvstatus.getText().toString().trim());
                    map1.put("total_feed",totalfeed_et.getText().toString());
                    map1.put("ocf",ocf_et.getText().toString());
                    map1.put("fg",feedgap_et.getText().toString());
                    //past_schedule.setVisibility(View.VISIBLE);
                    //past_linear.setVisibility(View.VISIBLE);
                    group_array.add(map1);
                }
                totalfeed_et.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                        try{

                                String tf=totalfeed_et.getText().toString().trim();
                                String df=dispensed_feed.toString().trim();
                                String cf=ocf_et.getText().toString().trim();
                                String fg=feedgap_et.getText().toString().trim();
                                float f_tf=Float.parseFloat(tf);
                                float f_df=Float.parseFloat(df);
                                float f_cf=Float.parseFloat(cf);
                                float f_fg=Float.parseFloat(fg);
                                float cycles = ((f_tf - f_df) * 1000 )/ f_cf;
                                float ttl_time = cycles * f_fg;
                                int  tt=(int)ttl_time;
                                int hours = tt / 60;
                                int minutes = tt % 60;
                                total_time.setText(hours+" : "+minutes);
                                try{
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
                                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                                    String Current_time = simpleDateFormat.format(System.currentTimeMillis());
                                    String[] separated = Current_time.split(":");
                                    String string1=separated[0];
                                    String string2=separated[1];
                                    int hours2=Integer.parseInt(string1);
                                    int minute2=Integer.parseInt(string2);
                                    int ttl_minute=minutes+minute2;
                                    int h = ttl_minute / 60;
                                    int m = ttl_minute % 60 ;
                                    int ttl_hour=hours+hours2+h;
                                    map1.put("to_time",ttl_hour+":"+m);
                                    map1.put("total_feed",tf);
                                    map1.put("ocf",cf);
                                    map1.put("fg",fg);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }





                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                ocf_et.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        try{

                                String tf=totalfeed_et.getText().toString().trim();
                                String df=dispensed_feed.toString().trim();
                                String cf=ocf_et.getText().toString().trim();
                                String fg=feedgap_et.getText().toString().trim();
                                float f_tf=Float.parseFloat(tf);
                                float f_df=Float.parseFloat(df);
                                float f_cf=Float.parseFloat(cf);
                                float f_fg=Float.parseFloat(fg);
                                float cycles = ((f_tf - f_df) * 1000 )/ f_cf;
                                float ttl_time = cycles * f_fg;
                                int  tt=(int)ttl_time;
                                int hours = tt / 60;
                                int minutes = tt % 60;
                                total_time.setText(hours+" : "+minutes);
                                try{
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
                                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                                    String Current_time = simpleDateFormat.format(System.currentTimeMillis());
                                    String[] separated = Current_time.split(":");
                                    String string1=separated[0];
                                    String string2=separated[1];
                                    int hours2=Integer.parseInt(string1);
                                    int minute2=Integer.parseInt(string2);

                                    int ttl_minute=minutes+minute2;
                                    int h = ttl_minute / 60;
                                    int m = ttl_minute % 60 ;
                                    int ttl_hour=hours+hours2+h;
                                    map1.put("to_time",ttl_hour+":"+m);
                                    map1.put("to_time",ttl_hour+":"+m);
                                    map1.put("total_feed",tf);
                                    map1.put("ocf",cf);
                                    map1.put("fg",fg);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                feedgap_et.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        try{

                                String tf=totalfeed_et.getText().toString().trim();
                                String df=dispensed_feed.toString().trim();
                                String cf=ocf_et.getText().toString().trim();
                                String fg=feedgap_et.getText().toString().trim();
                                float f_tf=Float.parseFloat(tf);
                                float f_df=Float.parseFloat(df);
                                float f_cf=Float.parseFloat(cf);
                                float f_fg=Float.parseFloat(fg);
                                float cycles = ((f_tf - f_df) * 1000 )/ f_cf;
                                float ttl_time = cycles * f_fg;
                                int  tt=(int)ttl_time;
                                int hours = tt / 60;
                                int minutes = tt % 60;
                                total_time.setText(hours+" : "+minutes);
                                try{
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
                                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                                    String Current_time = simpleDateFormat.format(System.currentTimeMillis());
                                    String[] separated = Current_time.split(":");
                                    String string1=separated[0];
                                    String string2=separated[1];
                                    int hours2=Integer.parseInt(string1);
                                    int minute2=Integer.parseInt(string2);

                                    int ttl_minute=minutes+minute2;
                                    int h = ttl_minute / 60;
                                    int m = ttl_minute % 60 ;
                                    int ttl_hour=hours+hours2+h;
                                    map1.put("to_time",ttl_hour+":"+m);
                                    map1.put("to_time",ttl_hour+":"+m);
                                    map1.put("total_feed",tf);
                                    map1.put("ocf",cf);
                                    map1.put("fg",fg);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void feederShow(){
        try {
            feeder_rowLayout.setVerticalScrollBarEnabled(true);
            feeder_rowLayout.removeAllViewsInLayout();
            for (int i = 0; i < mylist.size(); i++) {
                final HashMap<String, String> map = mylist.get(i);
                final String feederSno = map.get("feederSno").toString().trim();
                final String feederId=map.get("feederId").toString().trim();
                final String hex_id = map.get("hex_id").toString().trim();
                final String schedule_date = map.get("schedule_date").toString().trim();
                final String feeder_mode = map.get("mode").toString().trim();
                final String schedules = map.get("schedules").toString().trim();
                final String total_actual_feed = map.get("total_actual_feed").toString().trim();
                final String total_dispensed_feed = map.get("total_dispensed_feed").toString().trim();
                final String last_update_time=map.get("last_update_time").toString().trim();

                View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.feeders_layout, null);
               // itemView.setBackgroundResource(R.drawable.roundededitcorner);
                final TextView feeder_Id=(TextView) itemView.findViewById(R.id.feeder_Id);
                final TextView day_feed=(TextView) itemView.findViewById(R.id.day_feed);
                final TextView day_dispensed_feed=(TextView) itemView.findViewById(R.id.day_dispensed_feed);

                feeder_Id.setText(feederId);
                day_feed.setText(total_dispensed_feed+" / "+total_actual_feed);
                day_dispensed_feed.setText("Alerts");
                View v=new View(getActivity());
                v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                v.setBackgroundResource(R.drawable.seprator2);
                v.setPadding(0, 0, 0, 0);
                feeder_rowLayout.addView(v);
                feeder_rowLayout.addView(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                        final JSONArray jsonArray = new JSONArray(schedules);
                        schedules_list.clear();
                        if (jsonArray.length() > 0) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jObject = jsonArray.getJSONObject(j);
                                final HashMap<String, String> map1 = new HashMap<String, String>();
                                final String schedule_times = jObject.getString("schedule_times");
                                final String original_feed = jObject.getString("original_feed");
                                String[] separated = schedule_times.split("-");
                                String from_time = separated[0];
                                String to_time = separated[1];
                                map1.put("schedule_id", jObject.getString("schedule_id"));
                                map1.put("from_time", from_time);
                                map1.put("to_time", to_time);
                                map1.put("original_feed", original_feed);
                                map1.put("schedule_times",schedule_times);
                                map1.put("one_cycle_feed", jObject.getString("one_cycle_feed"));
                                map1.put("feed_gap", jObject.getString("feed_gap"));
                                map1.put("totalTime", jObject.getString("totalTime"));
                                map1.put("kg_feed_disp_time", jObject.getString("kg_feed_disp_time"));
                                map1.put("mode",feeder_mode);
                                map1.put("status",  jObject.getString("status"));
                                map1.put("dispensed_feed", jObject.getString("dispensed_feed"));
                                map1.put("device_totalfeed", jObject.getString("device_totalfeed"));
                                map1.put("device_ocf", jObject.getString("device_ocf"));
                                map1.put("device_feedgap", jObject.getString("device_feedgap"));
                                schedules_list.add(map1);
                            }
                        } else {
                            final HashMap<String, String> map1 = new HashMap<String, String>();
                            final String dt2 = Utillity.getCurrenttime(getActivity());
                            map1.put("schedule_id", "0");
                            map1.put("from_time", dt2);
                            map1.put("schedule_times", dt2+"-"+dt2);
                            map1.put("to_time", dt2);
                            map1.put("original_feed", " ");
                            map1.put("one_cycle_feed", " ");
                            map1.put("feed_gap", " ");
                            map1.put("totalTime", "00:00");
                            map1.put("kg_feed_disp_time", "0");
                            map1.put("mode", feeder_mode);
                            map1.put("status", "to_be_run");
                            map1.put("dispensed_feed", "0");
                            map1.put("device_totalfeed", "0");
                            map1.put("device_ocf", "0");
                            map1.put("device_feedgap", "0");
                            schedules_list.add(map1);
                        }
                           try{
                                Intent viewpager=new Intent(getActivity(),SingleFeederActivty.class);
                                Bundle bundle = new Bundle();
                                viewpager.putExtras(bundle);
                                viewpager.putExtra("feederSno", feederSno);
                                viewpager.putExtra("feederId", feederId);
                                viewpager.putExtra("feeder_mode", feeder_mode);
                                viewpager.putExtra("schedule_date", schedule_date);
                                viewpager.putExtra("last_update_time", last_update_time);
                                viewpager.putExtra("single_feeder_data_list", schedules_list);
                                viewpager.putExtra("pond_feeders_data", mylist);
                                 session.put("pond_name", pondname.getText().toString().trim());
                                startActivity(viewpager);
                           }catch(Exception e){
                                e.printStackTrace();
                            }
                    }catch (Exception e){
                            e.printStackTrace();
                        }
                        }
                });
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void pondsDisplay() {
        ArrayList<String> al=new ArrayList<>();
        for (int i = 0; i < pondArrList.size(); i++) {
            Map<String, String> map = pondArrList.get(i);
            String pond_name = map.get("pond_name").toString().trim();
            al.add(pond_name);
            String pond_sno = map.get("pond_sno").toString().trim();
        }
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2, al);
        ad.setDropDownViewResource(R.layout.spinner_dropdown);
        feededit_pond_spinner.setAdapter(ad);
         int position = ad.getPosition(PondName);
        feededit_pond_spinner.setSelection(position);
        feededit_pond_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> av, View v, int position, long d) {
                // TODO Auto-generated method stub
                String location = av.getItemAtPosition(position).toString().trim();
                Map<String, String> map = pondArrList.get(position);
                String pond_name = map.get("pond_name").toString().trim();
                String pond_sno = map.get("pond_sno").toString().trim();
                pondname.setText(pond_name);
                session.put("pond_name", pond_name);
                session.put("pond_sno", pond_sno);
                if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
                    retrieveFeedersClassData(pond_sno);
                   // String response=Model.getResponse(pond_sno,user_id,timezone,getActivity());
                   // processResponse(response,pond_sno);
                } else {
                    // TODO Auto-generated method stub
                    Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }
    private void retrieveFeedersClassData(final String pondsno) {
        try {
            JsonObject object = new JsonObject();
            object.addProperty("user_id", user_id);
            object.addProperty("pondsno", pondsno);
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
                        processResponse(response.body(),pondsno);
                        Log.e("FEEDER DETAILS", response.body().toString());
                    } else {
                    }
                    util.dismissDialog();
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("--", "onFailure : ");
                    t.printStackTrace();
                    util.dismissDialog();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void processResponse(JsonObject jsn,String pondsno) {
        try{
            String result = jsn.toString();
            JSONObject jsnobj = new JSONObject(result);
            String status = jsnobj.getString("status");
            String zero = "0".toString().trim();
            if (status.equals(zero)) {
                String error=jsnobj.getString("error");
                total_layout.setVisibility(View.GONE);
                util.showAlertDialog(getActivity(),"Response",error,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

            }else{
                total_layout.setVisibility(View.VISIBLE);
                String data=jsnobj.getString("data");
                JSONArray json_ary=jsnobj.getJSONArray("data");
                mylist.clear();
                for(int i=0;i<json_ary.length();i++){
                    JSONObject jObject = json_ary.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("pond_sno",pondsno);
                    map.put("feederId",jObject.getString("feederId"));
                    map.put("hex_id",jObject.getString("hex_id"));
                    map.put("feederSno",jObject.getString("feederSno"));
                    map.put("kg_feed_disp_time",jObject.getString("kg_feed_disp_time"));
                    map.put("mode",jObject.getString("mode"));
                    map.put("schedule_date",jObject.getString("schedule_date"));
                    map.put("schedule_count",jObject.getString("schedule_count"));
                    map.put("last_update_time",jObject.getString("last_update_time"));
                    map.put("total_actual_feed",jObject.getString("total_actual_feed"));
                    map.put("total_dispensed_feed",jObject.getString("total_dispensed_feed"));
                    map.put("data_interval",jObject.getString("data_interval"));
                    map.put("schedules",jObject.getJSONArray("schedules").toString());
                    mylist.add(map);
                }
                if(mylist!=null){
                    // check grouping edit
                     checkGroupEdit();
                    //show schedules on Listview individual feedershow
                    feederShow();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void feedEditResponse(JsonObject jsnobj) {
        try{
            String result = jsnobj.toString();
            JSONObject jsn = new JSONObject(result);
            String status = jsn.getString("status");
            String zero = "0".toString().trim();
            if (status.equals(zero)) {
                String error = jsn.getString("error");
                util.showAlertDialog(getActivity(), "Schedules Response ", error, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            } else {
                JSONObject jsnonb = jsn.getJSONObject("data");
                String response=jsnonb.getString("response");
                Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    }
