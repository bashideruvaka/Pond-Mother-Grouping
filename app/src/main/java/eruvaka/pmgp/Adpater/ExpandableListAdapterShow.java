package eruvaka.pmgp.Adpater;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import eruvaka.pmgp.R;
import eruvaka.pmgp.classes.ChildData;
import eruvaka.pmgp.classes.GroupData;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.common.Utillity;
import eruvaka.pmgp.common.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eruvaka on 10-03-2017.
 */

public class ExpandableListAdapterShow extends BaseExpandableListAdapter {
    private Context contextfeedentry;
    private List<GroupData> listDataHeaderFeedEntry; // header titles
    // child data in format of header title, child title
    private HashMap<GroupData, List<ChildData>> listDataChildFeedEntry;
    Utils util;
    UserSession  session;
    LinearLayout rowLayout,demo;
    Button dialog_update;
    String user_id,timezone;
    ArrayList<HashMap<String,String>> schedules_list=new ArrayList< HashMap<String,String>>();

    ArrayList<HashMap<String,String>> update_arraylist=new ArrayList< HashMap<String,String>>();
    ArrayList<HashMap<String, String>> group_array = new ArrayList<HashMap<String, String>>();
    ArrayList<Integer> ar = new ArrayList<Integer>();
    ArrayList<String> ar_str = new ArrayList<String>();
    ArrayList<String > sp_ar=new ArrayList<>();
    ArrayAdapter<String> ad;
    int count;
    public ExpandableListAdapterShow(Context context, List<GroupData> listDataHeader1,
                                   HashMap<GroupData, List<ChildData>> listChildData1) {
        this.contextfeedentry = context;
         session = new UserSession(contextfeedentry);
        this.listDataHeaderFeedEntry = listDataHeader1;
        this.listDataChildFeedEntry = listChildData1;
        util = new Utils(contextfeedentry);
        session = new UserSession(contextfeedentry);
        user_id = session.get("user_id");
        timezone=session.get("timezone");
        sp_ar.clear();

    }
    @Override
    public ChildData getChild(int groupPosition, int childPosititon) {
        return this.listDataChildFeedEntry.get(this.listDataHeaderFeedEntry.get(groupPosition))
                .get(childPosititon);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        try {
            final ChildData childfeedentry = getChild(groupPosition, childPosition);
            final String feeder_Id = childfeedentry.get_feeder_id();
            final String schedule_date = childfeedentry.get_schedule_date();
            final String original_feed = childfeedentry.get_original_feed();
            final String dispensed_feed = childfeedentry.get_dispensed_feed();
            final String schedules=childfeedentry.get_schedules();
            final String last_updatetime=childfeedentry.getlast_updatetime();
            final String mode=childfeedentry.get_mode();
            final String feederSno=childfeedentry.get_feeder_sno();
            util = new Utils(contextfeedentry);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.contextfeedentry.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.schedule_list, null);
            }
            final TableLayout t1=(TableLayout)convertView.findViewById(R.id.feedentrytableupdate);
            t1.setVerticalScrollBarEnabled(true);
            t1.removeAllViewsInLayout();

            LinearLayout.LayoutParams paramas4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,1);

            LinearLayout.LayoutParams paramas2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,1);
            paramas2.setMargins(5, 0, 5, 0);
            LinearLayout.LayoutParams paramas3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,1);
            paramas3.setMargins(5, 0, 5, 0);

            final LinearLayout tr= new LinearLayout(contextfeedentry);
            tr.setOrientation(LinearLayout.HORIZONTAL);

            final Spinner sp=new Spinner(contextfeedentry);
            sp.setGravity(Gravity.CENTER);
            sp.setLayoutParams(paramas2);
            ad = new ArrayAdapter<String>(contextfeedentry, R.layout.spinner_item2, Utillity.get_feed_array());
            ad.setDropDownViewResource(R.layout.spinner_dropdown);
            sp.setAdapter(ad);
            tr.addView(sp);

            final TextView pond_feeding=new TextView(contextfeedentry);
            pond_feeding.setHeight(50);
            pond_feeding.setLayoutParams(paramas2);
            pond_feeding.setTextColor(Color.BLACK);
            pond_feeding.setPadding(30,0,0,0);
            pond_feeding.setTextSize(16);
            pond_feeding.setFocusable(false);
            pond_feeding.setGravity(Gravity.CENTER);
            pond_feeding.setFreezesText(true);
            pond_feeding.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            pond_feeding.setKeyListener(null);
            tr.addView(pond_feeding);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String select=sp.getSelectedItem().toString();
                    if(select.equals("Select All")){
                        pond_feeding.setText("Pond Feeding");
                    }else {
                        pond_feeding.setText(select);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final TextView edit=new TextView(contextfeedentry);
            edit.setTextColor(Color.BLACK);
            edit.setTextSize(16);
            pond_feeding.setFocusable(false);
            edit.setGravity(Gravity.CENTER);
            edit.setFreezesText(true);
            edit.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            edit.setBackgroundResource(R.drawable.edit);
            tr.addView(edit);
            String error_check=Utillity.geterror_check();
           if(error_check.equals("disable")){
               edit.setKeyListener(null);
            }else{
                if(childPosition==0){
                    t1.addView(tr);
                }
           }
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("basic_mode".equals(Utillity.geterror_check())){
                        basic_mode_GroupData(schedules);
                    }else if("schedule_mode".equals(Utillity.geterror_check())){
                        String select=sp.getSelectedItem().toString();
                        if(select.equals("Select All")){
                            schedule_Mode(schedules,schedule_date,mode,last_updatetime);
                        }else{
                           // here chage feeder sno pending
                            edit_single_feeder(schedules,mode);
                            show_single_feeder_schedules(mode,schedule_date,last_updatetime,feederSno);
                        }

                    }
                }
            });
            // It Shows Individual feeder schedules
            final LinearLayout tablerow= new LinearLayout(contextfeedentry);
            tablerow.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams paramas11 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,1);
            tablerow.setLayoutParams(paramas11);
            final TextView et_from_time=new TextView(contextfeedentry);
            et_from_time.setHeight(50);
            et_from_time.setLayoutParams(paramas2);
            et_from_time.setText(feeder_Id);
            et_from_time.setTextColor(Color.BLACK);
            et_from_time.setTextSize(16);
            et_from_time.setFocusable(false);
            et_from_time.setGravity(Gravity.CENTER);
            et_from_time.setFreezesText(true);
            et_from_time.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            et_from_time.setKeyListener(null);
            tablerow.addView(et_from_time);

            final TextView et_to_time=new TextView(contextfeedentry);
            et_to_time.setLayoutParams(paramas3);
            et_to_time.setText(dispensed_feed+" / "+original_feed);
            et_to_time.setTextColor(Color.BLACK);
            et_to_time.setTextSize(16);
            et_to_time.setFocusable(false);
            et_to_time.setGravity(Gravity.CENTER);
            et_to_time.setFreezesText(true);
            et_to_time.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            et_to_time.setFreezesText(true);
            et_to_time.setKeyListener(null);
            tablerow.addView(et_to_time);

            final TextView et_tf=new TextView(contextfeedentry);
            et_tf.setLayoutParams(paramas4);
            et_tf.setText(schedule_date);
            et_tf.setTextColor(Color.BLACK);
            et_tf.setTextSize(16);
            et_tf.setHint("Kgs");
            et_tf.setKeyListener(null);
            et_tf.setGravity(Gravity.CENTER);
            et_tf.setFreezesText(true);
            tablerow.addView(et_tf);

            final TextView edit2=new TextView(contextfeedentry);
            edit2.setTextColor(Color.BLACK);
            edit2.setTextSize(16);
            edit2.setGravity(Gravity.CENTER);
            edit2.setFreezesText(true);
            edit2.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            edit2.setKeyListener(null);
            edit2.setBackgroundResource(R.drawable.edit);
            tablerow.addView(edit2);
            if(error_check.equals("disable")){
                edit.setKeyListener(null);
                t1.addView(tablerow);
            }else {
                if (childPosition == 0) {
                    //t1.addView(tablerow);
                }else{
                  //  t1.addView(tablerow);
                }
            }
            edit2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // select single feeder  edit
                     edit_single_feeder(schedules,mode);
                     show_single_feeder_schedules(mode,schedule_date,last_updatetime,feederSno);
                    // check modes to show schedules
                }
            });
            //array schedules
            JSONArray jsonArray = new JSONArray(schedules);
            if(jsonArray.length()>0){
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jObject = jsonArray.getJSONObject(j);
                    final String schedule_id=jObject.getString("schedule_id");
                    final String schedule_times = jObject.getString("schedule_times");
                    final String totalTime=jObject.getString("totalTime");
                    final String original_feed2 = jObject.getString("original_feed");
                    final String one_cycle_feed=jObject.getString("one_cycle_feed");
                    final String feed_gap=jObject.getString("feed_gap");
                    final String kg_feed_disp_time=jObject.getString("kg_feed_disp_time");
                    final String modee = jObject.getString("mode");
                    final String status = jObject.getString("status");
                    final String dispensed_feed2 = jObject.getString("dispensed_feed");
                    final LinearLayout tablerow2= new LinearLayout(contextfeedentry);
                    tablerow2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams paramas22 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,1);
                    paramas2.setMargins(5, 0, 5, 0);
                    final TextView et_from_time2=new TextView(contextfeedentry);
                    et_from_time2.setHeight(50);
                    et_from_time2.setLayoutParams(paramas22);
                    et_from_time2.setText(schedule_times);
                    et_from_time2.setTextColor(Color.BLACK);
                    et_from_time2.setTextSize(16);
                    et_from_time2.setFocusable(false);
                    et_from_time2.setGravity(Gravity.CENTER);
                    et_from_time2.setFreezesText(true);
                    et_from_time2.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                    et_from_time2.setKeyListener(null);
                    tablerow2.addView(et_from_time2);

                    final TextView et_to_time2=new TextView(contextfeedentry);
                    et_to_time2.setLayoutParams(paramas3);
                    et_to_time2.setText(dispensed_feed2+" / "+original_feed2);
                    et_to_time2.setTextColor(Color.BLACK);
                    et_to_time2.setTextSize(16);
                    et_to_time2.setFocusable(false);
                    et_to_time2.setGravity(Gravity.CENTER);
                    et_to_time2.setFreezesText(true);
                    et_to_time2.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                    et_to_time2.setFreezesText(true);
                    et_to_time2.setKeyListener(null);
                    tablerow2.addView(et_to_time2);

                    final TextView et_tf2=new TextView(contextfeedentry);
                    et_tf2.setLayoutParams(paramas4);
                    et_tf2.setText(status);
                    et_tf2.setTextColor(Color.BLACK);
                    et_tf2.setTextSize(16);
                    et_tf2.setKeyListener(null);
                    et_tf2.setGravity(Gravity.CENTER);
                    et_tf2.setFreezesText(true);
                    tablerow2.addView(et_tf2);
                    if(error_check.equals("disable")){
                        edit.setKeyListener(null);
                    }else {
                        if (childPosition == 0) {
                            t1.addView(tablerow2);
                        }
                    }
                    }
                }

        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    private void show_single_feeder_schedules(String mode, String schedule_date, String last_updatetime,final String feederSno) {
        try {
            if(mode.equals("79")){
                final Dialog dialog = new Dialog(contextfeedentry);
                dialog.setContentView(R.layout.group_schedule_layout);
                dialog.setTitle("Schedule Edit");
                dialog.show();
                dialog.setCancelable(false);
                rowLayout = (LinearLayout)dialog.findViewById(R.id.updatetbl1);
                showSchedules(mode,  schedule_date,  last_updatetime);
               /* Button group_cancel=(Button)dialog.findViewById(R.id.group_cancel);
                group_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button dialog_update=(Button)dialog.findViewById(R.id.group_update);
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
                        basic_mode_upadte_schedules(feederSno);
                    }
                });
            }else if(mode.equals("78")){
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
                Button group_cancel=(Button)dialog.findViewById(R.id.group_basic_cancel);
                Button group_update=(Button)dialog.findViewById(R.id.group_basic_update);
                group_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        basic_mode_upadte_schedules(feederSno);
                    }
                });
                group_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });*/
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void edit_single_feeder(String schedules,String mode) {
        try{
            JSONArray jsonArray=new JSONArray(schedules);
            if(jsonArray.length()>0) {
                schedules_list.clear();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jObject = jsonArray.getJSONObject(j);
                    final HashMap<String, String> map1 = new HashMap<String, String>();
                    final String schedule_id = jObject.getString("schedule_id");
                    final String schedule_times = jObject.getString("schedule_times");
                    final String totalTime = jObject.getString("totalTime");
                    final String original_feed = jObject.getString("original_feed");
                                    /*float tf_fl = Float.parseFloat(original_feed);
                                    int size = Utillity.get_mylist_size();
                                    float size_fl = Float.parseFloat(Integer.toString(size));
                                    float result = (tf_fl * size_fl);*/
                    final String one_cycle_feed = jObject.getString("one_cycle_feed");
                    final String feed_gap = jObject.getString("feed_gap");
                    final String kg_feed_disp_time = jObject.getString("kg_feed_disp_time");
                    final String status = jObject.getString("status");
                    final String dispensed_feed = jObject.getString("dispensed_feed");
                    final String device_totalfeed = jObject.getString("device_totalfeed");
                    final String device_ocf = jObject.getString("device_ocf");
                    final String device_feedgap = jObject.getString("device_feedgap");
                    String[] separated = schedule_times.split("-");
                    String from_time = separated[0];
                    String to_time = separated[1];
                    map1.put("schedule_id", schedule_id);
                    map1.put("from_time", from_time);
                    map1.put("to_time", to_time);
                    map1.put("original_feed",original_feed);
                    map1.put("one_cycle_feed", one_cycle_feed);
                    map1.put("feed_gap", feed_gap);
                    map1.put("totalTime", "00:00");
                    map1.put("kg_feed_disp_time", kg_feed_disp_time);
                    map1.put("mode", mode);
                    map1.put("status", status);
                    map1.put("dispensed_feed", dispensed_feed);
                    map1.put("device_totalfeed", device_totalfeed);
                    map1.put("device_ocf", device_ocf);
                    map1.put("device_feedgap", device_feedgap);
                    schedules_list.add(map1);
                }
            }else{
                final HashMap<String, String> map1 = new HashMap<String, String>();
                final String dt2= Utillity.getCurrenttime2(contextfeedentry);
                map1.put("schedule_id", "0");
                map1.put("from_time", dt2);
                map1.put("to_time", dt2);
                map1.put("original_feed"," ");
                map1.put("one_cycle_feed"," ");
                map1.put("feed_gap"," ");
                map1.put("totalTime", "00:00");
                map1.put("kg_feed_disp_time", "0");
                map1.put("mode",mode);
                map1.put("status", "to_be_run");
                map1.put("dispensed_feed", "0");
                map1.put("device_totalfeed", "0");
                map1.put("device_ocf", "0");
                map1.put("device_feedgap", "0");
                schedules_list.add(map1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void basic_mode_GroupData(final String schedules) {
        try{
            final Dialog dialog = new Dialog(contextfeedentry);
            dialog.setContentView(R.layout.custom_dailog);
            dialog.setTitle(" Pond Feeding ");
            dialog.show();
            final EditText totalfeed_et=(EditText)dialog.findViewById(R.id.totalfeed);
            final EditText ocf_et=(EditText)dialog.findViewById(R.id.ocf);
            final EditText feedgap_et=(EditText)dialog.findViewById(R.id.feedgap);
            final JSONArray jsonArray = new JSONArray(schedules);
            if(jsonArray.length()>0){
                JSONObject jObject = jsonArray.getJSONObject(0);
                final HashMap<String, String> map1 = new HashMap<String, String>();
                final String schedule_id = jObject.getString("schedule_id");
                final String original_feed = jObject.getString("original_feed");
                final String one_cycle_feed = jObject.getString("one_cycle_feed");
                final String feed_gap = jObject.getString("feed_gap");
                float tf_fl = Float.parseFloat(original_feed);
                int size = Utillity.get_mylist_size();
                float size_fl = Float.parseFloat(Integer.toString(size));
                float result=(tf_fl*size_fl);
                totalfeed_et.setText(Float.toString(result));
                ocf_et.setText(one_cycle_feed);
                feedgap_et.setText(feed_gap);

            }else{
            }
            totalfeed_et.setSelection(totalfeed_et.getText().length());
            ocf_et.setSelection(ocf_et.getText().length());
            feedgap_et.setSelection(feedgap_et.getText().length());

            Button group_update=(Button)dialog.findViewById(R.id.group_basic_update);

            group_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String total_feed = totalfeed_et.getText().toString().trim();
                    if(total_feed.isEmpty()){
                     Toast.makeText(contextfeedentry,"Feed is empty please try again",Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            float tf_fl = Float.parseFloat(total_feed);
                            int size = Utillity.get_mylist_size();
                            float size_fl = Float.parseFloat(Integer.toString(size));
                            float result=tf_fl/size_fl;
                            String each_total_feed = String.format("%.1f", result);
                            final JSONArray jsonArray1 = new JSONArray(schedules);
                            if(jsonArray1.length()>0) {
                                JSONObject jObject = jsonArray1.getJSONObject(0);
                                JSONObject jObject2 = new JSONObject();
                                jObject2.put("original_feed", each_total_feed);
                                jObject2.put("one_cycle_feed", ocf_et.getText().length());
                                jObject2.put("feed_gap", feedgap_et.getText().length());
                                jObject2.put("schedule_id", jObject.getString("schedule_id"));
                                jObject2.put("schedule_times", jObject.getString("schedule_times"));
                                jObject2.put("totalTime", jObject.getString("totalTime"));
                                jObject2.put("kg_feed_disp_time", jObject.getString("kg_feed_disp_time"));
                                jObject2.put("onTime", jObject.getString("onTime"));
                                jObject2.put("offTime", jObject.getString("offTime"));
                                jObject2.put("mode", jObject.getString("mode"));
                                jObject2.put("status", jObject.getString("status"));
                                jObject2.put("dispensed_feed", jObject.getString("dispensed_feed"));
                                jObject2.put("device_feedgap", jObject.getString("device_feedgap"));
                                jObject2.put("device_kgdisptime", jObject.getString("device_kgdisptime"));
                                jObject2.put("device_ocf", jObject.getString("device_ocf"));
                                jObject2.put("device_totalfeed", jObject.getString("device_totalfeed"));

                            }else{

                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }catch (Exception e){

        }
    }

    private void schedule_Mode(String schedules,String schedule_date,String mode,String last_update_time) {
        try{
            final Dialog dialog = new Dialog(contextfeedentry);
            dialog.setContentView(R.layout.group_schedule_layout);
            dialog.setTitle("Pond Feeding");
            dialog.show();
            dialog.setCancelable(false);
            rowLayout = (LinearLayout)dialog.findViewById(R.id.updatetbl1);
            demo = (LinearLayout) dialog.findViewById(R.id.updatehome);
           /* dialog_update=(Button)dialog.findViewById(R.id.group_update);
            Button group_cancel=(Button)dialog.findViewById(R.id.group_cancel);
            group_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });*/
            final JSONArray jsonArray = new JSONArray(schedules);
            schedules_list.clear();
            if(jsonArray.length()>0){
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jObject = jsonArray.getJSONObject(j);
                    final HashMap<String, String> map1 = new HashMap<String, String>();
                    final String schedule_id = jObject.getString("schedule_id");
                    final String schedule_times = jObject.getString("schedule_times");
                    final String totalTime = jObject.getString("totalTime");
                    final String original_feed = jObject.getString("original_feed");
                    float tf_fl = Float.parseFloat(original_feed);
                    int size = Utillity.get_mylist_size();
                    float size_fl = Float.parseFloat(Integer.toString(size));
                    float result=(tf_fl*size_fl);
                    final String one_cycle_feed = jObject.getString("one_cycle_feed");
                    final String feed_gap = jObject.getString("feed_gap");
                    final String kg_feed_disp_time = jObject.getString("kg_feed_disp_time");
                    final String status = jObject.getString("status");
                    final String dispensed_feed = jObject.getString("dispensed_feed");
                    final String device_totalfeed = jObject.getString("device_totalfeed");
                    final String device_ocf = jObject.getString("device_ocf");
                    final String device_feedgap = jObject.getString("device_feedgap");
                    String[] separated = schedule_times.split("-");
                    String from_time = separated[0];
                    String to_time = separated[1];
                    map1.put("schedule_id", schedule_id);
                    map1.put("from_time", from_time);
                    map1.put("to_time", to_time);
                    map1.put("original_feed",Float.toString(result));
                    map1.put("one_cycle_feed", one_cycle_feed);
                    map1.put("feed_gap", feed_gap);
                    map1.put("totalTime", "00:00");
                    map1.put("kg_feed_disp_time", kg_feed_disp_time);
                    map1.put("mode", mode);
                    map1.put("status", status);
                    map1.put("dispensed_feed", dispensed_feed);
                    map1.put("device_totalfeed", device_totalfeed);
                    map1.put("device_ocf", device_ocf);
                    map1.put("device_feedgap", device_feedgap);
                    schedules_list.add(map1);
                }
            }else{
                count = 0;
                final HashMap<String, String> map1 = new HashMap<String, String>();
               final String dt2= Utillity.getCurrenttime2(contextfeedentry);
                map1.put("schedule_id", "0");
                map1.put("from_time", dt2);
                map1.put("to_time", dt2);
                map1.put("original_feed"," ");
                map1.put("one_cycle_feed"," ");
                map1.put("feed_gap"," ");
                map1.put("totalTime", "00:00");
                map1.put("kg_feed_disp_time", "0");
                map1.put("mode",mode);
                map1.put("status", "to_be_run");
                map1.put("dispensed_feed", "0");
                map1.put("device_totalfeed", "0");
                map1.put("device_ocf", "0");
                map1.put("device_feedgap", "0");
                schedules_list.add(map1);
            }
            displayFeedHeader();
            showSchedules(mode,  schedule_date,  last_update_time);
           /* dialog_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(group_array);
                }
            });*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getfeeders_inpond(String user_id,String pondsno,String timezone) {
        try{
            JsonObject object =new JsonObject();
            object.addProperty("user_id",user_id);
            object.addProperty("pondsno",pondsno);
            final Calendar calender1=new GregorianCalendar(TimeZone.getTimeZone(timezone));
            final Date date1 = new Date(calender1.getTimeInMillis());
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone(timezone));
            String currenttimestr1=format2.format(date1).toString().trim();
            object.addProperty("schedule_start_date",currenttimestr1);
            Call<JsonObject> call = util.getBaseClassService_feders().feedersdata(object);
           // util.showProgressDialog();
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
                    if (response.isSuccessful()) {
                        //feeder_details_response(response.body());
                       // util.dismissDialog();

                    }
                   // util.dismissDialog();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("--", "onFailure : ");
                    t.printStackTrace();
                   // util.dismissDialog();
                }

            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChildFeedEntry.get(this.listDataHeaderFeedEntry.get(groupPosition)).size();
    }

    @Override
    public GroupData getGroup(int groupPosition) {
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
            final GroupData groupfeed = getGroup(groupPosition);
            final String day_feed=groupfeed.get_dayfeed();
            final String dispensed_feed=groupfeed.get_dispensed_feed();
            final String  pondname=groupfeed.get_pondname();
            final String get_date=groupfeed.get_date();
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.contextfeedentry.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.header_layout, null);
            }
            final TextView tv_feeder_Id = (TextView) convertView.findViewById(R.id.tv_feederId);
           // tv_feeder_Id.setTypeface(null, Typeface.BOLD);
            tv_feeder_Id.setText(pondname);
            final TextView tv_schedule_Date = (TextView) convertView.findViewById(R.id.tv_last_date);
            //tv_schedule_Date.setTypeface(null, Typeface.BOLD);
            tv_schedule_Date.setText(dispensed_feed+" / "+day_feed);
            final TextView tetxview_edit=(TextView)convertView.findViewById(R.id.tetxview_edit);
            //tetxview_edit.setTypeface(null, Typeface.BOLD);
             tetxview_edit.setText("Alerts");

        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    private void displayFeedHeader() {
        try {
            //add headerview
            View headerView = LayoutInflater.from(contextfeedentry).inflate(R.layout.shedule_feed_header, null);
            demo.addView(headerView);
            View v = new View(contextfeedentry);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            v.setBackgroundResource(R.drawable.seprator2);
            demo.addView(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showSchedules(final String main_mode,final String schedule_date,final String last_update_time) {
     try {
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
             View itemView = LayoutInflater.from(contextfeedentry).inflate(R.layout.shecdule_list_item, null);
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
                         Toast.makeText(contextfeedentry, " Cant  Removed This Record", Toast.LENGTH_SHORT).show();
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
                      count++;
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

                                         }else if(status.equals("to_be_run")){
                                             String last_update_time="00:00".toString().trim();
                                             String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                     ocf.getText().toString().trim(),fg.getText().toString().trim());
                                             String end_time=Utillity.get_to_time22(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
                                             totime.setText(end_time);
                                             map1.put("to_time", totime.getText().toString());
                                         }else{

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
                                 try{
                                     if(status.equals("running")||status.equals("paused")){

                                         String reman_time=Utillity.set_Dispensedfeed2(contextfeedentry,last_update_time,dispensed_feed,device_ocf,device_feedgap,timezone,data_interval,device_totalfeed,total_feed.getText().toString().trim(),
                                                 ocf.getText().toString().trim(),fg.getText().toString().trim());
                                         String end_time=Utillity.get_to_time12(contextfeedentry,fromtime.getText().toString(),reman_time,timezone);
                                         totime.setText(end_time);
                                         map1.put("to_time", totime.getText().toString());


                                     }else if(status.equals("to_be_run")){
                                         String last_update_time="00:00".toString().trim();
                                         System.out.println(data_interval);
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

             }
         }
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
                 if (group_array.size() - count > 7) {
                     Toast.makeText(contextfeedentry, " Only 8 Schedules are Allowed Per Day ", Toast.LENGTH_SHORT).show();
                 }else{
                     final HashMap<String, String> map1 = new HashMap<String, String>();
                     final String dt2= Utillity.getCurrenttime2(contextfeedentry);
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
}
