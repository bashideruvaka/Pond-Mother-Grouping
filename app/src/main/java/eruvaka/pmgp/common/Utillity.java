package eruvaka.pmgp.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by eruvaka on 31-03-2017.
 */

public class Utillity {
    public static String mcurrenttime;
    public static String mcurrenttime2;
    public static ProgressDialog dialog;
    public static String timezone;

    public static String last_update_time_str;
    public static String disp_feed;
    public static String disp_feed2;
    public static String m_end_time12;
    public static  UserSession session;
    public static String getCurrenttime(FragmentActivity activity) {
        try {
            session = new UserSession(activity);
            timezone = session.get("timezone");
            final Calendar calender = new GregorianCalendar( );
            calender.add(Calendar.MINUTE, 5);
            final Date date = new Date(calender.getTimeInMillis());
            final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            String currenttimestr = dateFormat.format(date).toString().trim();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
           simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            Date ctime = simpleDateFormat.parse(currenttimestr);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone(timezone));
            mcurrenttime = sdf.format(ctime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mcurrenttime;
    }
    public static String set_Dispensedfeed(FragmentActivity activity, String last_update_time, String dispensed_feed,
                                           String device_ocf, String device_fg,String timezone,String data_interval2,String device_total_feed
            ,String total_feed,String ocf,String fg) {
        // TODO Auto-generated method stub
        try{



            final Calendar calender1=new GregorianCalendar(TimeZone.getTimeZone(timezone));
            final Date date1 = new Date(calender1.getTimeInMillis());

            //last update time
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
             simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            final  Date ltime2 = simpleDateFormat.parse(last_update_time);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone(timezone));
            String dt1=sdf.format(ltime2);
            final Date ltime=sdf.parse(dt1);
            String curenttime=sdf.format(date1);
            String curenttime2=sdf.format(ltime);

            long diff = date1.getTime() - ltime.getTime();

            int data_interval3=(Integer.parseInt(data_interval2)/60);

            int data_interval=data_interval3+2;

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffMintues2=diffHours*60;
            long total_mintues=diffMinutes+diffMintues2;
            int  tt=(int)total_mintues;

            float disp_feed_f=Float.parseFloat(dispensed_feed);

            if(disp_feed_f>0){
                if(tt>data_interval){
                    float df=(Float.parseFloat(Integer.toString(tt))*Float.parseFloat(device_ocf));
                    float t_df=df/Float.parseFloat(device_fg);
                    float t_df2=(t_df/1000);
                    float last_dsp=(disp_feed_f+t_df2);
                    String estimate_disp_feed2=Float.toString(last_dsp);

                    String str=  get_total_time(total_feed,estimate_disp_feed2,ocf,fg);

                    disp_feed=str;


                }else{
                    String str=  get_total_time(total_feed,dispensed_feed,ocf,fg);

                    disp_feed=str;

                }
            }else{

                String str=  get_total_time(total_feed,dispensed_feed,ocf,fg);

                disp_feed=str;


            }

        }catch(Exception e){
            e.printStackTrace();
        }

       return disp_feed;
    }
    public static String set_Dispensedfeed2(Context contextfeedentry, String last_update_time, String dispensed_feed,
                                           String device_ocf, String device_fg,String timezone,String data_interval2,String device_total_feed
            ,String total_feed,String ocf,String fg) {
        // TODO Auto-generated method stub
        try{
            final Calendar calender1=new GregorianCalendar(TimeZone.getTimeZone(timezone));
            final Date date1 = new Date(calender1.getTimeInMillis());

            //last update time
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            //simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            final  Date ltime2 = simpleDateFormat.parse(last_update_time);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone(timezone));
            String dt1=sdf.format(ltime2);
            final Date ltime=sdf.parse(dt1);
            String curenttime=sdf.format(date1);
            String curenttime2=sdf.format(ltime);

            long diff = date1.getTime() - ltime.getTime();

            int data_interval3=(Integer.parseInt(data_interval2)/60);

            int data_interval=data_interval3+2;

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffMintues2=diffHours*60;
            long total_mintues=diffMinutes+diffMintues2;
            int  tt=(int)total_mintues;

            float disp_feed_f=Float.parseFloat(dispensed_feed);

            if(disp_feed_f>0){
                if(tt>data_interval){
                    float df=(Float.parseFloat(Integer.toString(tt))*Float.parseFloat(device_ocf));
                    float t_df=df/Float.parseFloat(device_fg);
                    float t_df2=(t_df/1000);
                    float last_dsp=(disp_feed_f+t_df2);
                    String estimate_disp_feed2=Float.toString(last_dsp);
                    System.out.println("estimate_disp"+estimate_disp_feed2);
                    String str=  get_total_time(total_feed,estimate_disp_feed2,ocf,fg);

                    disp_feed2=str;


                }else{
                    String str=  get_total_time(total_feed,dispensed_feed,ocf,fg);

                    disp_feed2=str;

                }
            }else{

                String str=  get_total_time(total_feed,dispensed_feed,ocf,fg);

                disp_feed2=str;


            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return disp_feed2;
    }
    public static String total_cal_time;
    private static String  get_total_time(String total_feed, String estimate_disp_feed2, String ocf, String fg) {
        // TODO Auto-generated method stub
        System.out.println(total_feed);
        int hours=0;
        int minutes=0;
        float f_tf=Float.parseFloat(total_feed);
        float f_df=Float.parseFloat(estimate_disp_feed2);
        float f_cf=Float.parseFloat(ocf);
        float f_fg=Float.parseFloat(fg);
        float cycles = ((f_tf - f_df) * 1000 )/ f_cf;
        System.out.println("cycles"+cycles);
        float ttl_time = cycles * f_fg;
        int  total_time=(int)ttl_time;
        hours = total_time / 60;
        minutes = total_time % 60;
        String send_total_time=String.valueOf(hours)+":"+String.valueOf(minutes);

        total_cal_time=send_total_time;
        System.out.println("end time"+total_cal_time);
        return total_cal_time;
    }
    public static String m_end_time;
    public static String  get_to_time(FragmentActivity activity, String fromtime, String reman_time,String timezone) {
        // TODO Auto-generated method stub

        final Calendar calender1=new GregorianCalendar(TimeZone.getTimeZone(timezone));
        final Date date1 = new Date(calender1.getTimeInMillis());
        final SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        dateFormat1.setTimeZone(TimeZone.getTimeZone(timezone));
        String currenttimestr1=dateFormat1.format(date1).toString().trim();
        String[] separated = currenttimestr1.split(":");
        String hour_str=separated[0];
        String min_str=separated[1];
        int hours=Integer.parseInt(hour_str);
        int mint_int=Integer.parseInt(min_str);

        String[] separated1 = reman_time.split(":");
        int r_hour_int=Integer.parseInt(separated1[0]);
        int r_mint_int=Integer.parseInt(separated1[1]);
        int hour_endtime=(r_hour_int+hours);
        int mint_endtime=(mint_int+r_mint_int);
        int hours_mint = mint_endtime / 60;
        int last_hour=hour_endtime+hours_mint;
        int minutesToDisplay = mint_endtime - (hours_mint * 60);
        String m_end_time=Integer.toString(last_hour)+":"+Integer.toString(minutesToDisplay);
        if(m_end_time.length()>5){
            m_end_time="00:00";
        }

        return m_end_time;
    }
    public static String  get_to_time12(Context contextfeedentry, String fromtime, String reman_time,String timezone) {
        // TODO Auto-generated method stub
        final Calendar calender1=new GregorianCalendar(TimeZone.getTimeZone(timezone));
        final Date date1 = new Date(calender1.getTimeInMillis());
        final SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        dateFormat1.setTimeZone(TimeZone.getTimeZone(timezone));
        String currenttimestr1=dateFormat1.format(date1).toString().trim();
        String[] separated = currenttimestr1.split(":");
        String hour_str=separated[0];
        String min_str=separated[1];
        int hours=Integer.parseInt(hour_str);
        int mint_int=Integer.parseInt(min_str);

        String[] separated1 = reman_time.split(":");
        int r_hour_int=Integer.parseInt(separated1[0]);
        int r_mint_int=Integer.parseInt(separated1[1]);
        int hour_endtime=(r_hour_int+hours);
        int mint_endtime=(mint_int+r_mint_int);
        int hours_mint = mint_endtime / 60;
        int last_hour=hour_endtime+hours_mint;
        int minutesToDisplay = mint_endtime - (hours_mint * 60);
        String m_end_time12=Integer.toString(last_hour)+":"+Integer.toString(minutesToDisplay);
        if(m_end_time12.length()>5){
            m_end_time12="00:00";
        }

        return m_end_time12;
    }
    public static String m_end_time2;
    public static String  get_to_time2(FragmentActivity activity, String fromtime, String reman_time,String timezone) {
        // TODO Auto-generated method stub
        String[] separated = fromtime.split(":");
        String hour_str=separated[0];
        String min_str=separated[1];
        int hours=Integer.parseInt(hour_str);
        int mint_int=Integer.parseInt(min_str);
        String[] separated1 = reman_time.split(":");
        int r_hour_int=Integer.parseInt(separated1[0]);
        int r_mint_int=Integer.parseInt(separated1[1]);
        int hour_endtime=(r_hour_int+hours);
        int mint_endtime=(mint_int+r_mint_int);
        int hours_mint = mint_endtime / 60;
        int last_hour=hour_endtime+hours_mint;
        int minutesToDisplay = mint_endtime - (hours_mint * 60);
        if(minutesToDisplay>0){
            int length = (int)(Math.log10(minutesToDisplay)+1);
            if(length==1){
                m_end_time2=Integer.toString(last_hour)+":0"+Integer.toString(minutesToDisplay);

            }else{
                m_end_time2=Integer.toString(last_hour)+":"+Integer.toString(minutesToDisplay);

            }
        }else{
            m_end_time2=Integer.toString(last_hour)+":"+Integer.toString(minutesToDisplay);

        }
        if(m_end_time2.length()>5){
            m_end_time2="00:00";
        }

        return m_end_time2;
    }
    public static String m_end_time22;
    public static String  get_to_time22(Context contextfeedentry, String fromtime, String reman_time,String timezone) {
        // TODO Auto-generated method stub
        String[] separated = fromtime.split(":");
        String hour_str=separated[0];
        String min_str=separated[1];
        int hours=Integer.parseInt(hour_str);
        int mint_int=Integer.parseInt(min_str);
        String[] separated1 = reman_time.split(":");
        int r_hour_int=Integer.parseInt(separated1[0]);
        int r_mint_int=Integer.parseInt(separated1[1]);
        int hour_endtime=(r_hour_int+hours);
        int mint_endtime=(mint_int+r_mint_int);
        int hours_mint = mint_endtime / 60;
        int last_hour=hour_endtime+hours_mint;
        int minutesToDisplay = mint_endtime - (hours_mint * 60);
        if(minutesToDisplay>0){
            int length = (int)(Math.log10(minutesToDisplay)+1);
            if(length==1){
                m_end_time22=Integer.toString(last_hour)+":0"+Integer.toString(minutesToDisplay);

            }else{
                m_end_time22=Integer.toString(last_hour)+":"+Integer.toString(minutesToDisplay);

            }
        }else{
            m_end_time22=Integer.toString(last_hour)+":"+Integer.toString(minutesToDisplay);

        }
        if(m_end_time22.length()>5){
            m_end_time22="00:00";
        }

        return m_end_time22;
    }
    private static String mdata_interval;
    public static void add_data_interval(String data_interval) {
        mdata_interval=data_interval;
    }

    public static String get_data_interval() {
        return mdata_interval;
    }

    public static String getCurrenttime2(Context contextfeedentry) {
        try {
            session = new UserSession(contextfeedentry);
            timezone = session.get("timezone");
            Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(timezone));
            calendar.add(Calendar.MINUTE, 5);
            final Date date = new Date(calendar.getTimeInMillis());
            final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            String currenttimestr = dateFormat.format(date).toString().trim();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
             simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
            Date ctime = simpleDateFormat.parse(currenttimestr);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
             sdf.setTimeZone(TimeZone.getTimeZone(timezone));
            mcurrenttime2 = sdf.format(ctime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mcurrenttime2;
    }

       private static String m_result="nullvalue";
    public static String check_schedules(JSONArray jsonArray, JSONArray jsonArray2) {
        try{
            if(jsonArray.length()>0||jsonArray2.length()>0){
                for (int j = 0; j < jsonArray.length(); j++) {
                    try {
                        JSONObject jObject1 = jsonArray.getJSONObject(j);
                        final String schedule_times = jObject1.getString("schedule_times");
                        final String status = jObject1.getString("status");
                        final String original_feed = jObject1.getString("original_feed");
                        final String one_cycle_feed = jObject1.getString("one_cycle_feed");
                        final String feed_gap = jObject1.getString("feed_gap");
                        JSONObject jObject2 = jsonArray2.getJSONObject(j);
                        final String schedule_times2 = jObject2.getString("schedule_times");
                        final String original_feed2 = jObject2.getString("original_feed");
                        final String one_cycle_feed2 = jObject2.getString("one_cycle_feed");
                        final String feed_gap2 = jObject2.getString("feed_gap");
                        if (status.equals("completed")) {
                            m_result="equal".toString();
                        } else {
                            if(schedule_times.equals(schedule_times2)){
                                m_result="equal".toString();

                            }else if(original_feed.equals(original_feed2)){
                                m_result="equal".toString();

                            } else if (one_cycle_feed.equals(one_cycle_feed2)) {
                                m_result="equal".toString();

                            }else{
                                m_result="not equal".toString();
                                break;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }catch (Exception e){

        }
        return m_result;
    }
    private static String m_eroor_check;
    public static void error_check(String disable) {
        m_eroor_check=disable;
    }

    public static String  geterror_check() {
        return m_eroor_check;
    }
    private static int m_size=0;
    public static void add_mylist_size(int i) {
        m_size=i;
    }
    public  static  int get_mylist_size(){
        return m_size;
    }

    private static ArrayList<String> m_array;
    public static void add_feed_array(ArrayList<String> s) {
        m_array=s;
    }
    public static ArrayList<String> get_feed_array() {
        return m_array;
    }
 private static ArrayList<HashMap<String,String>> return_mylist=new ArrayList<>();
    public static ArrayList<HashMap<String,String>> getJosnarray(ArrayList<HashMap<String, String>> mylist,String dt2) {
        try{
            final HashMap<String, String> map = mylist.get(0);
            final String feeder_mode=map.get("mode").toString().trim();
            final String schedules=map.get("schedules").toString().trim();
            final JSONArray jsonArray = new JSONArray(schedules);
            if (jsonArray.length() > 0) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jObject = jsonArray.getJSONObject(j);
                    final HashMap<String, String> map1 = new HashMap<String, String>();
                    final String schedule_times = jObject.getString("schedule_times");
                    final String original_feed = jObject.getString("original_feed");
                    float tf_fl = Float.parseFloat(original_feed);
                    int size = mylist.size();
                    float size_fl = Float.parseFloat(Integer.toString(size));
                    float result=(tf_fl*size_fl);
                    map1.put("original_feed", Float.toString(result));
                    String[] separated = schedule_times.split("-");
                    String from_time = separated[0];
                    String to_time = separated[1];
                    map1.put("schedule_id", jObject.getString("schedule_id"));
                    map1.put("from_time", from_time);
                    map1.put("to_time", to_time);
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
                    return_mylist.add(map1);
                }
            } else {
                final HashMap<String, String> map1 = new HashMap<String, String>();
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
                return_mylist.add(map1);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return  return_mylist;
    }


}
