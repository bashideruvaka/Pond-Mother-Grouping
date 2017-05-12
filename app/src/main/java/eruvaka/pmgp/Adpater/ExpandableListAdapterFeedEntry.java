package eruvaka.pmgp.Adpater;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import eruvaka.pmgp.Database.DBHelper;
import eruvaka.pmgp.R;
import eruvaka.pmgp.classes.ChildFeedEntry;
import eruvaka.pmgp.classes.GroupFeedEntry;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.common.Utillity;
import eruvaka.pmgp.common.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eruvaka on 10-03-2017.
 */

public class ExpandableListAdapterFeedEntry extends BaseExpandableListAdapter {
    private Context contextfeedentry;
    private List<GroupFeedEntry> listDataHeaderFeedEntry; // header titles
    // child data in format of header title, child title
    private HashMap<GroupFeedEntry, List<ChildFeedEntry>> listDataChildFeedEntry;
    ArrayList<HashMap<String, String>> schedules_list=new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String,String>> update_arraylist=new ArrayList<HashMap<String, String>>();
    DBHelper helper;
    SQLiteDatabase database;
    SQLiteStatement st;
    ArrayList<HashMap<String, String>> group_array = new ArrayList<HashMap<String, String>>();
    LinearLayout rowLayout;
    ArrayList<Integer> ar = new ArrayList<Integer>();
    ArrayList<String> ar_str = new ArrayList<String>();
    String timezone;
    UserSession session;
    public
    ExpandableListAdapterFeedEntry(Context context, List<GroupFeedEntry> listDataHeader1,
                                   HashMap<GroupFeedEntry, List<ChildFeedEntry>> listChildData1) {
        this.contextfeedentry = context;
        this.listDataHeaderFeedEntry = listDataHeader1;
        this.listDataChildFeedEntry = listChildData1;
    }
    @Override
    public ChildFeedEntry getChild(int groupPosition, int childPosititon) {
        return this.listDataChildFeedEntry.get(this.listDataHeaderFeedEntry.get(groupPosition))
                .get(childPosititon);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
         try {
             //final String childText = (String) getChild(groupPosition, childPosition);
             final ChildFeedEntry childfeedentry = getChild(groupPosition, childPosition);
             final String pondid = childfeedentry.get_feeder_id();
             final String schedule_id = childfeedentry.get_mschedule_id();
             final String schedule_time = childfeedentry.get_schedule_times();
             final String original_feed = childfeedentry.get_original_feed();
             final String one_cycle_feed = childfeedentry.get_one_cycle_feed();
             final String feed_gap = childfeedentry.get_feed_gap();
             final String dispensed_feed = childfeedentry.get_dispensed_feed();
             final String from_time=childfeedentry.get_from_time();
             final String to_time=childfeedentry.get_to_time();
             final String status=childfeedentry.get_status();
             final String totalTime = childfeedentry.get_totalTime();
             final String mode=childfeedentry.get_mode();

             if (convertView == null) {
                 LayoutInflater infalInflater = (LayoutInflater) this.contextfeedentry.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 convertView = infalInflater.inflate(R.layout.schedule_list, null);
             }

             final TableLayout t1=(TableLayout)convertView.findViewById(R.id.feedentrytableupdate);
             t1.setVerticalScrollBarEnabled(true);
             t1.removeAllViewsInLayout();

             final LinearLayout tablerow= new LinearLayout(contextfeedentry);
             tablerow.setOrientation(LinearLayout.HORIZONTAL);
             LinearLayout.LayoutParams paramas11 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                     LinearLayout.LayoutParams.WRAP_CONTENT,1);
            // tablerow.setPadding(5,5,5,5);
             tablerow.setLayoutParams(paramas11);
             LinearLayout.LayoutParams paramas91 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                     LinearLayout.LayoutParams.WRAP_CONTENT,1);

             LinearLayout.LayoutParams paramas2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                     LinearLayout.LayoutParams.WRAP_CONTENT,1);
             paramas2.setMargins(5, 0, 5, 0);

             final EditText et_from_time=new EditText(contextfeedentry);
             et_from_time.setLayoutParams(paramas2);
             et_from_time.setText(from_time);
             et_from_time.setTextColor(Color.BLACK);
             et_from_time.setTextSize(15);
             et_from_time.setFocusable(false);
             et_from_time.setGravity(Gravity.CENTER);
             et_from_time.setFreezesText(true);
             et_from_time.setBackgroundResource(R.drawable.rectangle);
             et_from_time.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
             et_from_time.setKeyListener(null);
            // et_from_time.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);

             final EditText et_to_time=new EditText(contextfeedentry);
             et_to_time.setLayoutParams(paramas2);
             et_to_time.setText(to_time);
             et_to_time.setTextColor(Color.BLACK);
             et_to_time.setTextSize(15);
             et_to_time.setFocusable(false);
             et_to_time.setGravity(Gravity.CENTER);
             et_to_time.setFreezesText(true);
             et_to_time.setBackgroundResource(R.drawable.rectangle2);
             et_to_time.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
             //et_to_time.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
             et_to_time.setFreezesText(true);
             et_to_time.setKeyListener(null);
             if(mode.equals("79")){
                 tablerow.addView(et_from_time);
                 tablerow.addView(et_to_time);
                 // tablerow2.addView(add_btn);
             }
             final EditText et_tf=new EditText(contextfeedentry);
             et_tf.setLayoutParams(paramas91);
             et_tf.setText(original_feed);
             et_tf.setTextColor(Color.BLACK);
             et_tf.setTextSize(15);
             et_tf.setHint("Kgs");
             et_tf.setKeyListener(null);
            // et_tf.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
             et_tf.setGravity(Gravity.CENTER);
             et_tf.setFreezesText(true);
             tablerow.addView(et_tf);

             final EditText et_ocf=new EditText(contextfeedentry);
             et_ocf.setLayoutParams(paramas91);
             et_ocf.setText(one_cycle_feed);
             et_ocf.setTextColor(Color.BLACK);
             et_ocf.setTextSize(15);
             et_ocf.setHint("gms");
             et_ocf.setKeyListener(null);
             //et_ocf.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
             et_ocf.setGravity(Gravity.CENTER);
             et_ocf.setFreezesText(true);
             tablerow.addView(et_ocf);

             final EditText et_fg=new EditText(contextfeedentry);
             et_fg.setLayoutParams(paramas91);
             et_fg.setText(feed_gap);
             et_fg.setTextColor(Color.BLACK);
             et_fg.setTextSize(15);
             et_fg.setHint("mins");
             et_fg.setKeyListener(null);
             //et_fg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
             et_fg.setGravity(Gravity.CENTER);
             et_fg.setFreezesText(true);
             tablerow.addView(et_fg);

             final EditText et_df=new EditText(contextfeedentry);
             et_df.setLayoutParams(paramas91);
             et_df.setText(dispensed_feed);
             et_df.setTextColor(Color.BLACK);
             et_df.setTextSize(15);
             et_df.setHint("Kgs");
             et_df.setKeyListener(null);
             //et_df.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
             et_df.setGravity(Gravity.CENTER);
             et_df.setFreezesText(true);
             tablerow.addView(et_df);
             t1.addView(tablerow);
             /*  et_from_time.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                 }

                 @Override
                 public void onTextChanged(CharSequence s, int start, int before, int count) {

                 }

                 @Override
                 public void afterTextChanged(Editable s) {
                     childfeedentry.set_from_time(s.toString());
                     try{
                         helper = new DBHelper(contextfeedentry);
                         database = helper.getReadableDatabase();
                         ContentValues cv = new ContentValues();
                         cv.put(DBHelper.FROM_TIME, s.toString());
                         database.update(DBHelper.TABLE1, cv, "SCHDID= ? and FID= ?", new String[] { schedule_id,pondid });
                         database.close();
                     }catch(Exception e){
                         e.printStackTrace();

                     }
                 }
             });

            et_tf.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                 }

                 @Override
                 public void onTextChanged(CharSequence s, int start, int before, int count) {

                 }

                 @Override
                 public void afterTextChanged(Editable s) {
                     childfeedentry.set_original_feed(s.toString());
                     try{
                         helper = new DBHelper(contextfeedentry);
                         database = helper.getReadableDatabase();
                         ContentValues cv = new ContentValues();
                         cv.put(DBHelper.TF, s.toString());
                         database.update(DBHelper.TABLE1, cv, "SCHDID= ? and FID= ?", new String[] { schedule_id,pondid });
                         database.close();
                     }catch(Exception e){
                         e.printStackTrace();

                     }
                 }
             });
             et_ocf.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                 }

                 @Override
                 public void onTextChanged(CharSequence s, int start, int before, int count) {

                 }

                 @Override
                 public void afterTextChanged(Editable s) {
                     childfeedentry.set_one_cycle_feed(s.toString());
                     try{
                         helper = new DBHelper(contextfeedentry);
                         database = helper.getReadableDatabase();
                         ContentValues cv = new ContentValues();
                         cv.put(DBHelper.OCF, s.toString());
                         database.update(DBHelper.TABLE1, cv, "SCHDID= ? and FID= ?", new String[] { schedule_id,pondid });
                         database.close();
                     }catch(Exception e){
                         e.printStackTrace();

                     }
                 }
             });
             et_fg.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                 }

                 @Override
                 public void onTextChanged(CharSequence s, int start, int before, int count) {

                 }

                 @Override
                 public void afterTextChanged(Editable s) {
                     childfeedentry.set_feed_gap(s.toString());
                     try{
                         helper = new DBHelper(contextfeedentry);
                         database = helper.getReadableDatabase();
                         ContentValues cv = new ContentValues();
                         cv.put(DBHelper.FG, s.toString());
                         database.update(DBHelper.TABLE1, cv, "SCHDID= ? and FID= ?", new String[] { schedule_id,pondid });
                         database.close();
                     }catch(Exception e){
                         e.printStackTrace();

                     }
                 }
             });*/


         }catch (Exception e){
             e.printStackTrace();
         }

        return convertView;
    }@Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChildFeedEntry.get(this.listDataHeaderFeedEntry.get(groupPosition)).size();
    }

    @Override
    public GroupFeedEntry getGroup(int groupPosition) {
        return this.listDataHeaderFeedEntry.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeaderFeedEntry.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
        // String headerTitle = (String) getGroup(groupPosition);
        try {
            final GroupFeedEntry groupfeed = getGroup(groupPosition);
            final String feeder_Id = groupfeed.get_feederSno();
            final String feeder_sno=groupfeed.get_feeder_id();
            final String schedule_date = groupfeed.get_date();
            final String  last_update_time=groupfeed.get_last_update_time();
            final String modee=groupfeed.get_mode();
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.contextfeedentry.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.header_layout, null);
            }
            final TextView tv_feeder_Id = (TextView) convertView.findViewById(R.id.tv_feederId);
            tv_feeder_Id.setTypeface(null, Typeface.BOLD);
            tv_feeder_Id.setText(feeder_Id);
            final TextView tv_schedule_Date = (TextView) convertView.findViewById(R.id.tv_last_date);
            tv_schedule_Date.setTypeface(null, Typeface.BOLD);
            tv_schedule_Date.setText(schedule_date);
            final TextView tetxview_edit=(TextView)convertView.findViewById(R.id.tetxview_edit);
            tetxview_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        schedules_list.clear();
                        helper=new DBHelper(contextfeedentry);
                        database=helper.getReadableDatabase();
                        String query = ("select * from feedentry  where  FID ='" + feeder_sno + "'");
                        Cursor cursor = database.rawQuery(query, null);
                        if(cursor != null){
                            if(cursor.moveToFirst()){
                                do{
                                    final HashMap<String, String> map1 = new HashMap<String, String>();
                                    String FID = cursor.getString(cursor.getColumnIndex("FID"));
                                    String HEXID = cursor.getString(cursor.getColumnIndex("HEXID"));
                                    String TF = cursor.getString(cursor.getColumnIndex("TF"));
                                    String TOTAL_TIME = cursor.getString(cursor.getColumnIndex("TOTAL_TIME"));
                                    String FROM_TIME = cursor.getString(cursor.getColumnIndex("FROM_TIME"));
                                    String TO_TIME = cursor.getString(cursor.getColumnIndex("TO_TIME"));
                                    String STATUS = cursor.getString(cursor.getColumnIndex("STATUS"));
                                    String OCF = cursor.getString(cursor.getColumnIndex("OCF"));
                                    String FG = cursor.getString(cursor.getColumnIndex("FG"));
                                    String DF = cursor.getString(cursor.getColumnIndex("DF"));
                                    String SCHDID = cursor.getString(cursor.getColumnIndex("SCHDID"));
                                    String MODE = cursor.getString(cursor.getColumnIndex("MODE"));
                                    String device_totalfeed = cursor.getString(cursor.getColumnIndex("DEVICE_TF"));
                                    String device_ocf = cursor.getString(cursor.getColumnIndex("DEVICE_OCF"));
                                    String device_feedgap = cursor.getString(cursor.getColumnIndex("DEVICE_FG"));
                                    String kg_feed_disp_time = cursor.getString(cursor.getColumnIndex("KG_DSIP"));
                                    map1.put("schedule_id", SCHDID);
                                    map1.put("from_time", FROM_TIME);
                                    map1.put("to_time", TO_TIME);
                                    map1.put("original_feed",TF);
                                    map1.put("one_cycle_feed", OCF);
                                    map1.put("feed_gap", FG);
                                    map1.put("totalTime", "00:00");
                                    map1.put("kg_feed_disp_time", kg_feed_disp_time);
                                    map1.put("mode", MODE);
                                    map1.put("status", STATUS);
                                    map1.put("dispensed_feed", DF);
                                    map1.put("device_totalfeed", device_totalfeed);
                                    map1.put("device_ocf", device_ocf);
                                    map1.put("device_feedgap", device_feedgap);
                                    schedules_list.add(map1);
                                }	while(cursor.moveToNext());
                            }
                        }
                    }catch(Exception e){
                    }
                    if(modee.equals("79")){
                        final Dialog dialog = new Dialog(contextfeedentry);
                        dialog.setContentView(R.layout.group_schedule_layout);
                        dialog.setTitle("Schedule Edit");
                        dialog.show();
                      //  dialog.setCancelable(false);
                        rowLayout = (LinearLayout)dialog.findViewById(R.id.updatetbl1);
                        showSchedules(modee,  schedule_date,  last_update_time);
                   /*      Button dialog_update=(Button)dialog.findViewById(R.id.group_update);
                        dialog_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                update_arraylist.clear();
                                for (int i = 0; i < group_array.size(); i++) {
                                    try{
                                        final HashMap<String, String> map = group_array.get(i);
                                        final HashMap<String, String> map1 = new HashMap<String, String>();
                                        JSONObject jObject = new JSONObject();
                                        map1.put("schedule_id",map.get("schedule_id"));
                                        map1.put("from_time",map.get("from_time"));
                                        map1.put("to_time",map.get("to_time"));
                                        map1.put("original_feed",map.get("total_feed"));
                                        map1.put("one_cycle_feed",map.get("ocf"));
                                        map1.put("feed_gap",map.get("fg"));
                                        map1.put("totalTime",map.get("totalTime"));
                                        map1.put("kg_feed_disp_time",map.get("kg_feed_disp_time"));
                                        map1.put("mode",map.get("mode"));
                                        map1.put("status",map.get("status"));
                                        map1.put("dispensed_feed",map.get("dispensed_feed"));
                                      //  map1.put("device_totalfeed",map.get("device_totalfeed"));
                                      //  map1.put("device_feedgap",map.get("device_feedgap"));
                                        update_arraylist.add(map1);
                                    }catch (Exception e){
                                    }
                                }
                                basic_mode_upadte_schedules(groupfeed.get_feeder_id());
                            }
                        });
                        Button group_cancel=(Button)dialog.findViewById(R.id.group_cancel);
                        group_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });*/
                    }else if(modee.equals("78")){
                        final Dialog dialog = new Dialog(contextfeedentry);
                        dialog.setContentView(R.layout.custom_dailog);
                        dialog.setTitle("Pond Feeding ");
                        dialog.show();
                        final EditText totalfeed_et=(EditText)dialog.findViewById(R.id.totalfeed);
                        EditText ocf_et=(EditText)dialog.findViewById(R.id.ocf);
                        EditText feedgap_et=(EditText)dialog.findViewById(R.id.feedgap);totalfeed_et.setSelection(totalfeed_et.getText().length());
                        ocf_et.setSelection(ocf_et.getText().length());
                        feedgap_et.setSelection(feedgap_et.getText().length());
                        update_arraylist.clear();
                        for (int j = 0; j < schedules_list.size(); j++) {
                            final HashMap<String, String> map = schedules_list.get(j);
                            final HashMap<String, String> map1 = new HashMap<String, String>();
                            final String original_feed = map.get("original_feed");
                            final String one_cycle_feed = map.get("one_cycle_feed");
                            final String feed_gap = map.get("feed_gap");
                            totalfeed_et.setText(original_feed);
                            ocf_et.setText(one_cycle_feed);
                            feedgap_et.setText(feed_gap);
                            map1.put("schedule_id",map.get("schedule_id"));
                            map1.put("from_time",map.get("from_time"));
                            map1.put("to_time",map.get("to_time"));
                            map1.put("original_feed",totalfeed_et.getText().toString());
                            map1.put("one_cycle_feed",ocf_et.getText().toString());
                            map1.put("feed_gap",feedgap_et.getText().toString());
                            map1.put("totalTime",map.get("totalTime"));
                            map1.put("kg_feed_disp_time",map.get("kg_feed_disp_time"));
                            map1.put("mode",map.get("mode"));
                            map1.put("status",map.get("status"));
                            map1.put("dispensed_feed",map.get("dispensed_feed"));
                            map1.put("device_totalfeed",map.get("device_totalfeed"));
                            map1.put("device_feedgap",map.get("device_feedgap"));
                            update_arraylist.add(map1);

                            totalfeed_et.setSelection(totalfeed_et.getText().length());
                            ocf_et.setSelection(ocf_et.getText().length());
                            feedgap_et.setSelection(feedgap_et.getText().length());
                            totalfeed_et.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    map1.put("original_feed",s.toString());
                                }
                            });
                            ocf_et.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    map1.put("one_cycle_feed",s.toString());
                                }
                            });
                            feedgap_et.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    map1.put("feed_gap",s.toString());
                                }
                            });

                        }

                        Button group_update=(Button)dialog.findViewById(R.id.group_basic_update);
                        group_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                basic_mode_upadte_schedules(groupfeed.get_feeder_id());
                            }
                        });

                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    private void basic_mode_upadte_schedules(String feedersno) {
        session = new UserSession(contextfeedentry);
        Utils util = new Utils(contextfeedentry);
        String  user_id=session.get("user_id");
        timezone=session.get("timezone");
        JsonObject object = new JsonObject();
        object.addProperty("user_id", user_id);
        object.addProperty("feederSno", feedersno);
        final Calendar calender1=new GregorianCalendar(TimeZone.getTimeZone(timezone));
        final Date date1 = new Date(calender1.getTimeInMillis());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        format2.setTimeZone(TimeZone.getTimeZone(timezone));
        String currenttimestr1=format2.format(date1).toString().trim();
        object.addProperty("schedule_start_date", currenttimestr1);
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
            String fromTime = map.get("from_time").toString().trim();
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
       // util.showProgressDialog();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
                if (response.isSuccessful()) {
                    // processResponse(response.body());
                    Log.e("Single Feeder Update ",response.body().toString());
                    //Utils util = new Utils(contextfeedentry);
                    //util.dismissDialog();
                } else {

                }
               // Utils util = new Utils(contextfeedentry);
               // util.dismissDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("--", "onFailure : ");
                t.printStackTrace();
                Utils util = new Utils(contextfeedentry);
                util.dismissDialog();
            }

        });
    }

    private void showSchedules(final String main_mode, final String schedule_date, final String last_update_time) {
        rowLayout.setVerticalScrollBarEnabled(true);
        rowLayout.removeAllViewsInLayout();
        final String data_interval=Utillity.get_data_interval();
        ar.clear();
        ar_str.clear();
        session = new UserSession(contextfeedentry);
        timezone=session.get("timezone");
        for (int j = 0; j < schedules_list.size(); j++) {
            final HashMap<String, String> map = schedules_list.get(j);
            final HashMap<String, String> map1 = new HashMap<String, String>();
            final String schedule_id = map.get("schedule_id");
            final String totalTime = map.get("totalTime");
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

            View itemView = LayoutInflater.from(contextfeedentry).inflate(R.layout.shecdule_list_item, null);
            final EditText fromtime=(EditText)itemView.findViewById(R.id.shedule_list_item_et_from_time);
            final EditText totime=(EditText)itemView.findViewById(R.id.shedule_list_item_et_to_time);
            final EditText total_feed=(EditText)itemView.findViewById(R.id.shedule_list_item_et_total_feed);
            final EditText ocf=(EditText)itemView.findViewById(R.id.shedule_list_item_et_ocf_feed);
            final EditText fg=(EditText)itemView.findViewById(R.id.shedule_list_item_et_fg);
            final EditText df=(EditText)itemView.findViewById(R.id.shedule_list_item_et_df);
            final TextView sub=(TextView) itemView.findViewById(R.id.shedule_list_item_tv_delete_item);
            total_feed.setText(original_feed);
            total_feed.setSelection(total_feed.getText().length());
            ocf.setText(one_cycle_feed);
            ocf.setSelection(ocf.getText().length());
            fg.setText(feed_gap);
            fg.setSelection(fg.getText().length());
            df.setText(dispensed_feed);
            df.setSelection(df.getText().length());
            df.setEnabled(false);
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
                        sub.setBackgroundResource(R.drawable.none);
                        Toast.makeText(contextfeedentry, " Cant be Removed This Record", Toast.LENGTH_SHORT).show();
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
            try{
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
                                        //final Calendar calender=new GregorianCalendar();
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
                                            // String selected_date = start_date.getText().toString().trim();
                                            String selected_date = schedule_date.toString().trim();
                                            String current_date = format2.format(System.currentTimeMillis());
                                            if(current_date.equals(selected_date)){
                                                fromtime.setText(dt2);
                                                Toast.makeText(contextfeedentry, "Past Time not Allowed", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                    try{

                                        if(status.equals("running")||status.equals("paused")){
                                            String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                    ocf.getText().toString().trim(),fg.getText().toString().trim());
                                            String end_time=Utillity.get_to_time12(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
                                            totime.setText(end_time);
                                            map1.put("to_time", totime.getText().toString());
                                            System.out.println(status);
                                        }else if(status.equals("to_be_run")){
                                            String last_update_time="00:00".toString().trim();
                                            String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                    ocf.getText().toString().trim(),fg.getText().toString().trim());
                                            String end_time=Utillity.get_to_time22(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
                                            totime.setText(end_time);
                                            map1.put("to_time", totime.getText().toString());
                                        }

                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            };

                            TimePickerDialog tpd=new TimePickerDialog(contextfeedentry, timesetlistener, fhour, fminute,  DateFormat.is24HourFormat(contextfeedentry));
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
                                System.out.println(totime.getText().toString());
                                try{
                                      if(status.equals("running")||status.equals("paused")){
                                     String last_update_time2="00:00".toString().trim();
                                        String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                ocf.getText().toString().trim(),fg.getText().toString().trim());
                                        String end_time=Utillity.get_to_time12(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
                                        totime.setText(end_time);
                                        map1.put("to_time", totime.getText().toString());


                                   }else if(status.equals("to_be_run")){
                                        String last_update_time="00:00".toString().trim();
                                        System.out.println(totime.getText().toString());
                                        String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                ocf.getText().toString().trim(),fg.getText().toString().trim());
                                        String end_time=Utillity.get_to_time22(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
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

                                    String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                            ocf.getText().toString().trim(),fg.getText().toString().trim());
                                    String end_time=Utillity.get_to_time12(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
                                    totime.setText(end_time);
                                    map1.put("to_time", totime.getText().toString());
                                    System.out.println(status);

                                }else if(status.equals("to_be_run")){
                                    String last_update_time="00:00".toString().trim();
                                    String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                            ocf.getText().toString().trim(),fg.getText().toString().trim());
                                    String end_time=Utillity.get_to_time22(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
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
                                        String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                ocf.getText().toString().trim(),fg.getText().toString().trim());
                                        String end_time=Utillity.get_to_time12(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
                                        totime.setText(end_time);
                                        map1.put("to_time", totime.getText().toString());

                                    }else if(status.equals("to_be_run")){
                                        String last_update_time="00:00".toString().trim();
                                        String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                ocf.getText().toString().trim(),fg.getText().toString().trim());
                                        String end_time=Utillity.get_to_time22(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
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
                e.printStackTrace();
            }
        }
        ///
        View itemView = LayoutInflater.from(contextfeedentry).inflate(R.layout.status_view, null);
        final TextView start_tv=(TextView) itemView.findViewById(R.id.start);
        final TextView pause_tv=(TextView) itemView.findViewById(R.id.pause);
        final TextView stop_tv=(TextView) itemView.findViewById(R.id.stop);
        final TextView last_update_time_tv=(TextView) itemView.findViewById(R.id.last_update_time);
        final TextView  add_new=(TextView)itemView.findViewById(R.id.add_new);
        rowLayout.addView(itemView);
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, String> map1 = new HashMap<String, String>();
                final String dt2= Utillity.getCurrenttime2(contextfeedentry);
                System.out.println(dt2);
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
try {
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
    start_tv.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            try {
                if (ar.size() > 0) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map = schedules_list.get(ar.get(0));
                    System.out.println(map);
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
                    System.out.println(map);
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
                    System.out.println(map);
                    map.put("status", "mcompleted");
                    start_tv.setBackgroundResource(R.drawable.s1);
                    start_tv.setVisibility(View.VISIBLE);
                    stop_tv.setVisibility(View.INVISIBLE);
                    pause_tv.setVisibility(View.INVISIBLE);
                    //final Calendar calender = new GregorianCalendar();
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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
