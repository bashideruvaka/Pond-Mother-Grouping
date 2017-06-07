package eruvaka.pmgp.Activitys;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.util.Locale;
import java.util.TimeZone;

import eruvaka.pmgp.R;
import eruvaka.pmgp.common.MapComparator;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.common.Utillity;
import eruvaka.pmgp.common.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by eruvaka on 26-04-2017.
 */
public class SingleFeederActivty extends AppCompatActivity implements View.OnClickListener {
    ArrayList<HashMap<String, String>> schedules_list =new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> send_list=new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String,String>> group_array=new ArrayList<>();
    ArrayList<HashMap<String,String>> update_arraylist=new ArrayList<>();
    LinearLayout rowLayout,demo;
    android.support.v7.app.ActionBar bar;
    String feederSno,feederId,schedule_date,feeder_mode,last_update_time,user_id,PondName,timezone;
    Utils util;
    UserSession session;
    ArrayList<Integer> ar = new ArrayList<Integer>();
    ArrayList<String> ar_str = new ArrayList<String>();
    Context context;
    View basic_layout;
    LinearLayout liner_table,past_linear;
    EditText totalfeed_et,ocf_et,feedgap_et,total_time,okdtime,dispense_feed_ed;
    Button group_basic_update,group_schedule_update;
    TextView txtStockEntryDate,tvstatus,past_schedule;
    ImageButton start,pause,stop;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_feeder);
        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
        bar=getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
       // bar.setIcon(android.R.color.transparent);
        basic_layout = (View)findViewById(R.id.test3);
        liner_table=(LinearLayout)findViewById(R.id.liner_table);
        basic_layout.setVisibility(View.GONE);
        totalfeed_et=(EditText)findViewById(R.id.totalfeed);
        ocf_et=(EditText)findViewById(R.id.ocf);
        feedgap_et=(EditText)findViewById(R.id.feedgap);
        total_time=(EditText)findViewById(R.id.total_time);
        okdtime=(EditText)findViewById(R.id.okdtime);
        dispense_feed_ed=(EditText)findViewById(R.id.dispensedfeed);
        past_schedule = (TextView)findViewById(R.id.p_schedule);
        past_linear = (LinearLayout)findViewById(R.id.past_linear);
        group_basic_update=(Button)findViewById(R.id.group_basic_update);
        group_schedule_update=(Button)findViewById(R.id.group_schedule_update);
        group_basic_update.setOnClickListener(this);
        group_schedule_update.setOnClickListener(this);
        rowLayout = (LinearLayout)findViewById(R.id.updatetbl1);
        demo = (LinearLayout)findViewById(R.id.updatehome);
        txtStockEntryDate = (TextView)findViewById(R.id.start_date);
        tvstatus = (TextView)findViewById(R.id.tvstatus);
        start = (ImageButton)findViewById(R.id.start);
        pause = (ImageButton)findViewById(R.id.pause);
        stop = (ImageButton)findViewById(R.id.stop);
        util = new Utils(SingleFeederActivty.this);
        session = new UserSession(SingleFeederActivty.this);
        context=getApplicationContext();
        user_id = session.get("user_id");
        PondName=session.get("pond_name");
        timezone=session.get("timezone");
        schedules_list= (ArrayList<HashMap<String, String>>)getIntent().getSerializableExtra("single_feeder_data_list");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             feederSno = extras.getString("feederSno");
             feederId = extras.getString("feederId");
              bar.setTitle(feederId);
             feeder_mode = extras.getString("feeder_mode");
             schedule_date = extras.getString("schedule_date");
             last_update_time = extras.getString("last_update_time");
            if(feeder_mode.equals("78")){
                basic_layout.setVisibility(View.VISIBLE);
                liner_table.setVisibility(View.GONE);
                 basicModeData();
            }else if(feeder_mode.equals("79")){
                liner_table.setVisibility(View.VISIBLE);
                basic_layout.setVisibility(View.GONE);
                displayFeedHeader();
                View v=(View)findViewById(R.id.v_line);
                v.setVisibility(View.GONE);
                LinearLayout liner=(LinearLayout)findViewById(R.id.sch_liner);
                liner.setVisibility(View.GONE);
                showSchedules(feeder_mode,schedule_date,last_update_time);
            }
        }
        setStartDate();
        txtStockEntryDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(0);
            }
        });
    }
    @Override

    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(SingleFeederActivty.this, datePickerListener, year, month, day);
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            final Calendar c = Calendar.getInstance();
            c.set(selectedYear, selectedMonth, selectedDay);
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;

            if (txtStockEntryDate != null) {
                final Date date = new Date(c.getTimeInMillis());
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                txtStockEntryDate.setText(dateFormat.format(date));
            }
        }

    };
    private void setStartDate() {
        final Calendar calender = Calendar.getInstance();
        final Date date = new Date(calender.getTimeInMillis());
        day = calender.get(Calendar.DATE);
        month = calender.get(Calendar.MONTH);
        year = calender.get(Calendar.YEAR);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        if (txtStockEntryDate != null) {
            txtStockEntryDate.setText(dateFormat.format(date));
        }
    }
    private void basicModeData() {
        try {
             for(int i=0;i<schedules_list.size();i++) {
                 final HashMap<String, String> map = schedules_list.get(i);
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
                     try {
                         final Calendar calender1 = new GregorianCalendar(TimeZone.getTimeZone(timezone));
                         final Date date1 = new Date(calender1.getTimeInMillis());
                         //last update time
                         final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
                         simpleDateFormat1.setTimeZone(TimeZone.getTimeZone(timezone));
                         if (last_update_time.isEmpty()) {

                         } else {
                             final Date ltime = simpleDateFormat1.parse(last_update_time);
                             long diff = date1.getTime() - ltime.getTime();
                             long diffMinutes = diff / (60 * 1000) % 60;
                             long diffHours = diff / (60 * 60 * 1000) % 24;
                             long diffMintues2 = diffHours * 60;
                             long total_mintues = diffMinutes + diffMintues2;
                             int tt = (int) total_mintues;

                         }


                     } catch (Exception e) {
                         e.printStackTrace();
                     }
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
                     try {
                         final Calendar calender1 = new GregorianCalendar(TimeZone.getTimeZone(timezone));
                         final Date date1 = new Date(calender1.getTimeInMillis());
                         //last update time
                         final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
                         simpleDateFormat1.setTimeZone(TimeZone.getTimeZone(timezone));
                         final Date ltime = simpleDateFormat1.parse(last_update_time);
                         long diff = date1.getTime() - ltime.getTime();
                         long diffMinutes = diff / (60 * 1000) % 60;
                         long diffHours = diff / (60 * 60 * 1000) % 24;
                         long diffMintues2 = diffHours * 60;
                         long total_mintues = diffMinutes + diffMintues2;
                         int tt = (int) total_mintues;

                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 } else {
                     start.setBackgroundResource(R.drawable.s1);
                     stop.setVisibility(View.INVISIBLE);
                     pause.setVisibility(View.INVISIBLE);
                     tvstatus.setText("completed");
                 }
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
                     }
                 });
                 send_list.clear();
                 final HashMap<String, String> map1 = new HashMap<String, String>();
                 final String status1 = tvstatus.getText().toString().trim();
                 if (status1.equals("completed")) {
                     map1.put("schedule_id", "0");
                     map1.put("schedule_times", "00:00");
                     map1.put("totalTime", "00:00");
                     map1.put("fromTime", "00:00");
                     map1.put("to_time", "00:00");
                     map1.put("mode", mode);
                     tvstatus.setText("running");
                     feedgap_et.setSelection(feedgap_et.getText().length());
                     okdtime.setText(" ");
                     total_time.setText(" ");
                     dispense_feed_ed.setText(dispensed_feed);
                     past_schedule.setVisibility(View.VISIBLE);
                     past_linear.setVisibility(View.VISIBLE);
                    View itemView_home = LayoutInflater.from(SingleFeederActivty.this).inflate(R.layout.pastschedule_home, null);
                     View itemView = LayoutInflater.from(SingleFeederActivty.this).inflate(R.layout.pastschedule_show, null);
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
                     past_linear.addView(itemView_home);
                     past_linear.addView(itemView);
                 } else {
                     map1.put("schedule_id", schedule_id);
                     String[] separated = schedule_times.split("-");
                     map1.put("schedule_times", schedule_times);
                     map1.put("fromTime", separated[0]);
                     map1.put("to_time", separated[1]);
                     map1.put("totalTime", totalTime);
                     map1.put("mode", mode);
                 }
                 send_list.add(map1);
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

                             if(status1.equals("completed")){
                                 String tf=totalfeed_et.getText().toString().trim();
                                 String df="0".toString();
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
                             }else{
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

                                 }catch(Exception e){
                                     e.printStackTrace();
                                 }


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
                             if(status1.equals("completed")){
                                 String tf=totalfeed_et.getText().toString().trim();
                                 String df="0".toString();
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
                             }else{
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

                                 }catch(Exception e){
                                     e.printStackTrace();
                                 }
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
                             if(status1.equals("completed")){
                                 String tf=totalfeed_et.getText().toString().trim();
                                 String df="0".toString();
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
                             }else{
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

                                 }catch(Exception e){
                                     e.printStackTrace();
                                 }
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
    private void displayFeedHeader() {
        try {
            //add headerview
            View headerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.shedule_feed_header, null);
            demo.addView(headerView);
            View v = new View(getApplicationContext());
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
            final String data_interval= Utillity.get_data_interval();
            group_array.clear();
            for (int j = 0; j <schedules_list .size(); j++) {
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
                View itemView = LayoutInflater.from(SingleFeederActivty.this).inflate(R.layout.shecdule_list_item, null);
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
                View v=new View(SingleFeederActivty.this);
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
                            Toast.makeText(SingleFeederActivty.this, " Cant  Removed This Record", Toast.LENGTH_SHORT).show();
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

                    final Calendar calender = new GregorianCalendar(TimeZone.getTimeZone(timezone));
                    final Date date = new Date(calender.getTimeInMillis());
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
                    String currenttimestr = dateFormat.format(date).toString().trim();
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
                    anim.setDuration(50); //You can manage the time of the blink with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    if (status.equals("to_be_run")) {
                        // start.setBackgroundResource(R.drawable.s1);
                        // stop.setBackgroundResource(R.drawable.none);
                        if (dt1.compareTo(dt2) <= 0) {

                        } else {
                            sub.setBackgroundResource(R.drawable.rsub);
                            sub.setEnabled(true);
                            df.setVisibility(View.GONE);
                        }

                    } else if (status.equals("running")) {
                        // start.setBackgroundResource(R.drawable.p1);
                        // stop.setBackgroundResource(R.drawable.stop1);

                        df.setTextColor(Color.parseColor("#24890d"));
                        df.startAnimation(anim);
                        if (dt3.compareTo(dt2) <= 0) {
                            totime.setText(to_date);
                        } else {
                            //  start.setBackgroundResource(R.drawable.p1);
                            //  stop.setBackgroundResource(R.drawable.stop1);
                        }
                    } else if (status.equals("changed")) {
                        // start.setBackgroundResource(R.drawable.p1);
                        // stop.setBackgroundResource(R.drawable.stop1);
                    } else if (status.equals("paused")) {
                        // start.setBackgroundResource(R.drawable.s1);
                        //stop.setBackgroundResource(R.drawable.stop1);
                        df.setTextColor(Color.parseColor("#24890d"));
                        df.startAnimation(anim);
                    } else if (status.equals("completed")) {
                        // count++;
                        total_feed.setEnabled(false);
                        ocf.setEnabled(false);
                        fg.setEnabled(false);
                        fromtime.setEnabled(false);
                        fromtime.setBackgroundResource(R.drawable.roundedshape2);
                        // start.setBackgroundResource(R.drawable.s1);
                        // stop.setBackgroundResource(R.drawable.none);
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
                                    final SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm", Locale.getDefault());
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
                                                    Toast.makeText(SingleFeederActivty.this, "Past Time not Allowed", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                        try{

                                            if(status.equals("running")||status.equals("paused")){
                                                String reman_time=Utillity.set_Dispensedfeed2(context,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                        ocf.getText().toString().trim(),fg.getText().toString().trim());
                                                String end_time=Utillity.get_to_time12(context,fromtime.getText().toString(),reman_time,timezone);
                                                totime.setText(end_time);
                                                map1.put("to_time", totime.getText().toString());

                                            }else if(status.equals("to_be_run")){
                                                String last_update_time="00:00".toString().trim();
                                                String reman_time=Utillity.set_Dispensedfeed2(context,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                        ocf.getText().toString().trim(),fg.getText().toString().trim());
                                                String end_time=Utillity.get_to_time22(context,fromtime.getText().toString(),reman_time,timezone);
                                                totime.setText(end_time);
                                                map1.put("to_time", totime.getText().toString());
                                            }else{

                                            }

                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                TimePickerDialog tpd=new TimePickerDialog(getApplicationContext(), timesetlistener, fhour, fminute,  DateFormat.is24HourFormat(context));
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

                                    System.out.println(status);
                                    try{
                                        if(status.equals("running")||status.equals("paused")){

                                            String reman_time=Utillity.set_Dispensedfeed2(getApplicationContext(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                    ocf.getText().toString().trim(),fg.getText().toString().trim());
                                            String end_time=Utillity.get_to_time12(getApplicationContext(),fromtime.getText().toString(),reman_time,timezone);
                                            totime.setText(end_time);
                                            map1.put("to_time", totime.getText().toString());


                                        }else if(status.equals("to_be_run")){
                                            String last_update_time="00:00".toString().trim();
                                            System.out.println(data_interval);
                                            String reman_time=Utillity.set_Dispensedfeed2(getApplicationContext(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                    ocf.getText().toString().trim(),fg.getText().toString().trim());
                                            String end_time=Utillity.get_to_time22(getApplicationContext(),fromtime.getText().toString(),reman_time,timezone);
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
                                        String reman_time=Utillity.set_Dispensedfeed2(getApplicationContext(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                ocf.getText().toString().trim(),fg.getText().toString().trim());
                                        String end_time=Utillity.get_to_time12(getApplicationContext(),fromtime.getText().toString(),reman_time,timezone);
                                        totime.setText(end_time);
                                        map1.put("to_time", totime.getText().toString());
                                    }else if(status.equals("to_be_run")){
                                        String last_update_time="00:00".toString().trim();
                                        String reman_time=Utillity.set_Dispensedfeed2(getApplicationContext(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                ocf.getText().toString().trim(),fg.getText().toString().trim());
                                        String end_time=Utillity.get_to_time22(getApplicationContext(),fromtime.getText().toString(),reman_time,timezone);
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
                                            String reman_time=Utillity.set_Dispensedfeed2(getApplicationContext(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                    ocf.getText().toString().trim(),fg.getText().toString().trim());
                                            String end_time=Utillity.get_to_time12(getApplicationContext(),fromtime.getText().toString(),reman_time,timezone);
                                            totime.setText(end_time);
                                            map1.put("to_time", totime.getText().toString());

                                        }else if(status.equals("to_be_run")){
                                            String last_update_time="00:00".toString().trim();
                                            String reman_time=Utillity.set_Dispensedfeed2(getApplicationContext(),last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                    ocf.getText().toString().trim(),fg.getText().toString().trim());
                                            String end_time=Utillity.get_to_time22(getApplicationContext(),fromtime.getText().toString(),reman_time,timezone);
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
            View itemView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.status_view, null);
            final TextView start_tv=(TextView) itemView.findViewById(R.id.start);
            final TextView pause_tv=(TextView) itemView.findViewById(R.id.pause);
            final TextView stop_tv=(TextView) itemView.findViewById(R.id.stop);
            final TextView last_update_time_tv=(TextView) itemView.findViewById(R.id.last_update_time);
            final TextView  add_new=(TextView)itemView.findViewById(R.id.add_new);
            final TextView group_schedule_update=(TextView)itemView.findViewById(R.id.group_schedule_update);
            group_schedule_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     scheduleModeCheckData();
                }
            });

            rowLayout.addView(itemView);
            add_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final HashMap<String, String> map1 = new HashMap<String, String>();
                    final String dt2= Utillity.getCurrenttime(SingleFeederActivty.this);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
              case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_basic_update:
                  basicModeCheckData();
                break;
            case R.id.group_schedule_update:
                scheduleModeCheckData();
                break;
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
                String str_date1 = txtStockEntryDate.getText().toString().trim();
                java.util.Date d1 = dfDate.parse(str_date1);
                if (error.size() > 0) {
                    Toast.makeText(SingleFeederActivty.this, error.get(0), Toast.LENGTH_SHORT).show();
                } else if (d1.before(date2)) {
                    Toast.makeText(SingleFeederActivty.this, R.string.pastdate, Toast.LENGTH_SHORT).show();

                } else {
                    if (updateSchedules()) {
                      //schedule_mode_update_toserver();
                        update_arraylist.clear();
                        System.out.println(group_array);
                        for (int i = 0; i < group_array.size(); i++) {
                            HashMap<String, String> map1 = new HashMap<String, String>();
                            final HashMap<String, String> map = group_array.get(i);
                            String schedule_id=  map.get("schedule_id").toString().trim();
                            String from_time= map.get("from_time").toString().trim();
                            String to_time	=map.get("to_time").toString().trim();
                            String totalTime   =map.get("to_time").toString().trim();
                            String original_feed=     map.get("total_feed").toString().trim();
                            String one_cycle_feed   =     map.get("ocf").toString().trim();
                            String  feed_gap=   map.get("fg").toString().trim();
                            String   kg_feed_disp_time=  map.get("kg_feed_disp_time").toString().trim();
                            String mode   =   map.get("mode").toString().trim();
                            String  status =   map.get("status").toString().trim();
                            String dispensed_feed= map.get("dispensed_feed").toString().trim();
                            map1.put("schedule_id", schedule_id);
                            map1.put("totalTime", totalTime);
                            map1.put("fromTime", from_time);
                            map1.put("to_time", to_time);
                            map1.put("mode", mode);
                            map1.put("dispensed_feed", dispensed_feed);
                            map1.put("original_feed", original_feed);
                            map1.put("one_cycle_feed", one_cycle_feed);
                            map1.put("feed_gap", feed_gap);
                            map1.put("status", status);
                            update_arraylist.add(map1);
                        }
                        if(update_arraylist.size()>0){
                            basicModeUpadteSchedules(feederSno);
                        }
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

                                    Toast.makeText(SingleFeederActivty.this, "Please Check Schedule" + (j + 1) + "Timings !!!", Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                if (compareValues(from_time_str, from_time_str2) == true) {
                                    // fromtime with fromtime overlapping
                                    Toast.makeText(SingleFeederActivty.this, "Schedule  " + (x + 1) + " FromTime OverLapping With Schedule " + (j + 1) + " From Time !!!", Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                if (compareValues(to_time_str, from_time_str2) == true) {
                                    // fromtime with totime overlapping

                                    Toast.makeText(SingleFeederActivty.this, "Schedule  " + (x + 1) + "  From Time OverLapping With Schedule " + (j + 1) + " To Time !!!", Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            }


                        }


                    }

                }else{

                    Toast.makeText(SingleFeederActivty.this,"schedule are empty please try again",Toast.LENGTH_SHORT).show();
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
    //basic mode update data
    private void  basicModeCheckData(){
        update_arraylist.clear();
        HashMap<String, String> map1 = new HashMap<String, String>();
        for (int i = 0; i < send_list.size(); i++) {
            HashMap<String, String> map = send_list.get(i);
            final String schedule_id = map.get("schedule_id").toString().trim();
            final String totalTime = map.get("totalTime").toString().trim();
            final String fromTime = map.get("fromTime").toString().trim();
            final String to_time = map.get("to_time").toString().trim();
            final String mode = map.get("mode").toString().trim();
            map1.put("schedule_id", schedule_id);
            map1.put("totalTime", totalTime);
            map1.put("fromTime", fromTime);
            map1.put("to_time", to_time);
            map1.put("mode", mode);
        }
        try {
            String status = tvstatus.getText().toString().trim();
            String tfeed = totalfeed_et.getText().toString().trim();
            String ocfeed = ocf_et.getText().toString().trim();
            String fgp = feedgap_et.getText().toString().trim();
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

            final Calendar end = Calendar.getInstance();
            final Date date2 = new Date(end.getTimeInMillis() - (24 * 60 * 60 * 1000));
            SimpleDateFormat dfDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            dfDate.setTimeZone(TimeZone.getTimeZone(timezone));
            String str_date1 = txtStockEntryDate.getText().toString().trim();
            java.util.Date d1 = dfDate.parse(str_date1);
            String current_date = dfDate.format(System.currentTimeMillis());

            if (tfeed.isEmpty() || ocfeed.isEmpty() || fgp.isEmpty() || tfeed.equals(zero) || ocfeed.equals(zero) || fgp.equals(zero)) {
                Toast.makeText(SingleFeederActivty.this, R.string.nullvalues, Toast.LENGTH_SHORT).show();
            } else if (tf < df) {
                Toast.makeText(SingleFeederActivty.this, R.string.dispensedfeed, Toast.LENGTH_SHORT).show();
            } else if (d1.before(date2)) {
                Toast.makeText(SingleFeederActivty.this, R.string.pastdate, Toast.LENGTH_SHORT).show();
            } else {
                map1.put("dispensed_feed", dispensedfeed);
                map1.put("original_feed", tfeed);
                map1.put("one_cycle_feed", ocfeed);
                map1.put("feed_gap", fgp);
                map1.put("status", status);
                update_arraylist.add(map1);
                if (update_arraylist.size() > 0) {
                    basicModeUpadteSchedules(feederSno);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void basicModeUpadteSchedules(String feedersno) {
        JsonObject object = new JsonObject();
        object.addProperty("user_id", user_id);
        object.addProperty("feederSno", feedersno);
        final Calendar calender1=new GregorianCalendar(TimeZone.getTimeZone(timezone));
        final Date date1 = new Date(calender1.getTimeInMillis());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        format2.setTimeZone(TimeZone.getTimeZone(timezone));
        String currenttimestr1=format2.format(date1).toString().trim();
        object.addProperty("schedule_date", currenttimestr1);
        object.addProperty("schedule_end_date", currenttimestr1);
        // arrays
        ArrayList<String> total_feed_array=new ArrayList<>();
        ArrayList<String> one_cycle_feed_array=new ArrayList<>();
        ArrayList<String> feed_gap_array=new ArrayList<>();
        ArrayList<String> status_array=new ArrayList<>();
        ArrayList<String> schedule_id_array=new ArrayList<>();
        ArrayList<String> totalTime_array=new ArrayList<>();
        ArrayList<String> mode_array=new ArrayList<>();
        ArrayList<String> fromTime_array=new ArrayList<>();
        ArrayList<String> toTime_array=new ArrayList<>();
        ArrayList<String> onTime_array=new ArrayList<>();
        ArrayList<String> offTime_array=new ArrayList<>();

        for (int i = 0; i < update_arraylist.size(); i++) {
            HashMap<String, String> map = update_arraylist.get(i);
            String schedule_id = map.get("schedule_id").toString().trim();
            String fromTime = map.get("fromTime").toString().trim();
            String toTime = map.get("to_time").toString().trim();
            String totalTime = map.get("totalTime").toString().trim();
            //String kg_feed_disp_time=map.get("kg_feed_disp_time").toString().trim();
            String mode = map.get("mode").toString().trim();
            String status = map.get("status").toString().trim();
            String dispensed_feed = map.get("dispensed_feed").toString().trim();
            String tfeed = map.get("original_feed").toString().trim();
            String ocfeed = map.get("one_cycle_feed").toString().trim();
            String fgp = map.get("feed_gap").toString().trim();
            total_feed_array.add("\"" + tfeed + "\"");
            one_cycle_feed_array.add("\"" + ocfeed + "\"");
            feed_gap_array.add("\"" + fgp + "\"");
            status_array.add("\"" + status + "\"");
            schedule_id_array.add("\"" + schedule_id + "\"");
            totalTime_array.add("\"" + totalTime + "\"");
            mode_array.add("\"" + mode + "\"");
            fromTime_array.add("\"" + fromTime + "\"");
            toTime_array.add("\"" + toTime + "\"");
            onTime_array.add("\"" + "00:00" + "\"");
            offTime_array.add("\"" + "00:00" + "\"");
        }
        object.addProperty("original_feed", total_feed_array.toString());
        object.addProperty("one_cycle_feed", one_cycle_feed_array.toString());
        object.addProperty("status", status_array.toString());
        object.addProperty("feed_gap", feed_gap_array.toString());
        object.addProperty("schedule_id", schedule_id_array.toString());
        object.addProperty("total_time", totalTime_array.toString());
        object.addProperty("mode", mode_array.toString());
        object.addProperty("from_time", fromTime_array.toString());
        object.addProperty("to_time", toTime_array.toString());
        object.addProperty("on_time", onTime_array.toString());
        object.addProperty("off_time", offTime_array.toString());

        Call<JsonObject> call = util.getAdapter_singlefeeder_update().singlefeeder(object);
         util.showProgressDialog();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
                if (response.isSuccessful()) {
                     processResponse(response.body());
                    Log.e("Single Feeder Update ",response.body().toString());
                    //Utils util = new Utils(contextfeedentry);
                    util.dismissDialog();
                } else {

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("--", "onFailure : ");
                t.printStackTrace();

                util.dismissDialog();
            }

        });
    }
    private void processResponse(JsonObject jsn) {
        try{
            String result=  jsn.toString();
            JSONObject jsnobj = new JSONObject(result);
            String status = jsnobj.getString("status");
            String zero = "0".toString().trim();
            if (status.equals(zero)) {
                String error=jsnobj.getString("error");
                util.showAlertDialog(this,"Response ",error,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }else{
                String success = jsnobj.getString("data");
                JSONObject jsn2 = new JSONObject(success);
                String response2 = jsn2.getString("response");
                util.showAlertDialog(this,"Response ",response2,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
