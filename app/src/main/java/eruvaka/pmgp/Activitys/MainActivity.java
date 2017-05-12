package eruvaka.pmgp.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import eruvaka.pmgp.Adpater.ExpandableListAdapterFeedEntry;
import eruvaka.pmgp.Adpater.ExpandableListAdapterShow;
import eruvaka.pmgp.Database.DBHelper;
import eruvaka.pmgp.R;
import eruvaka.pmgp.classes.ChildData;
import eruvaka.pmgp.classes.ChildFeedEntry;
import eruvaka.pmgp.classes.GroupData;
import eruvaka.pmgp.classes.GroupFeedEntry;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.common.Utillity;
import eruvaka.pmgp.common.Utils;
import eruvaka.pmgp.serverconnect.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
    TableLayout t1;
    ArrayList<String> al = new ArrayList<String>();
    private Context context;
    private Utils util;
    UserSession session;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> pondArrList = new ArrayList<HashMap<String, String>>();
    String user_id,ownerid;
    AlertDialog.Builder build;
    String timezone;
    ExpandableListView expListView;
    ExpandableListAdapterShow listAdaptershow;
    List<GroupData> listFeedGroupData;
    HashMap<GroupData,List<ChildData>> listFeedChildData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        util = new Utils(context);
        build=new AlertDialog.Builder(getApplicationContext());
        session = new UserSession(getApplicationContext());
        user_id = session.get("user_id");
        ownerid = session.get("ownerid");
        timezone = session.get("timezone");
        expListView=(ExpandableListView)findViewById(R.id.main_lvExp);
        expListView.setGroupIndicator(null);
        //getting ponds data from server
        getPondDetailsClassData(user_id);
    }
    private void getPondDetailsClassData(String user_id) {
        JsonObject object =new JsonObject();
        object.addProperty("user_id",user_id);
        Call<JsonObject> call = util.getBaseClassService_pond().pondsdata(object);
        util.showProgressDialog();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
                if (response.isSuccessful()) {
                    processResponse_getponddeatils(response.body());
                } else {

                }
                util.dismissDialog();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("--", "onFailure : ");
                t.printStackTrace();
                Toast.makeText(MainActivity.this,"unable  to get data please try again",Toast.LENGTH_SHORT).show();
                util.dismissDialog();
            }
        });
    }
    private void processResponse_getponddeatils(JsonObject jsn) {
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
                JSONArray ponds_array=jsnobj.getJSONArray("data");
                session.put("ponds_array",ponds_array.toString());
                pondArrList.clear();
                for (int i = 0; i < ponds_array.length(); i++) {
                    HashMap<String, String> mapping = new HashMap<String, String>();
                    JSONObject jObject = ponds_array.getJSONObject(i);
                    mapping.put("pond_name", jObject.getString("pond_name"));
                    mapping.put("pond_sno", jObject.getString("pond_sno"));
                    mapping.put("day_feed",("10"));
                    mapping.put("dispensed_feed", ("0"));
                    mapping.put("date", ("Today"));
                    pondArrList.add(mapping);
                }
                if(pondArrList.size()>0){
                    pondsDisplay();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void pondsDisplay() {
        try {
            listFeedGroupData = new ArrayList<GroupData>();
            List<ChildData> childdata = null;
            listFeedChildData = new HashMap<GroupData, List<ChildData>>();
            for (int i = 0; i < pondArrList.size(); i++) {
                childdata = new ArrayList<ChildData>();
                Map<String, String> map = pondArrList.get(i);
                final GroupData groupdata = new GroupData();
                groupdata.set_pondname(map.get("pond_name").toString().trim());
                groupdata.set_feederSno(map.get("pond_sno").toString().trim());
                groupdata.set_dayfeed(map.get("day_feed").toString().trim());
                groupdata.set_dispensed_feed(map.get("dispensed_feed").toString().trim());
                groupdata.set_date(map.get("date").toString().trim());
                listFeedGroupData.add(groupdata);
                if (mylist.size() > 0) {
                    final HashMap<String, String> map2 = mylist.get(0);
                    final String str2 = map2.get("pond_sno").toString().trim();
                    final String str = map.get("pond_sno").toString().trim();
                    if (str.equals(str2)) {
                        for (int j = 0; j < mylist.size(); j++) {
                            final HashMap<String, String> map1 = mylist.get(j);
                            final String feederName = map1.get("feederSno").toString().trim();
                            final String hex_id = map1.get("hex_id").toString().trim();
                            final String schedule_date = map1.get("schedule_date").toString().trim();
                            final String modee = map1.get("mode").toString().trim();
                            final ChildData childfeedentry = new ChildData();
                            //childfeedentry.set_feeder_id(map.get("pond_name").toString().trim());
                            childfeedentry.set_feeder_id(map1.get("feederSno").toString().trim());
                            childfeedentry.set_mdispensed_feed(map1.get("mode").toString().trim());
                            childdata.add(childfeedentry);
                            listFeedChildData.put(listFeedGroupData.get(i), childdata);
                        }
                    } else {
                        final ChildData childfeedentry = new ChildData();
                        //childfeedentry.set_feeder_id(map.get("pond_name").toString().trim());
                        childfeedentry.set_feeder_id(map.get("pond_sno").toString().trim());
                        childfeedentry.set_mdispensed_feed(map.get("dispensed_feed").toString().trim());
                        childdata.add(childfeedentry);
                        listFeedChildData.put(listFeedGroupData.get(i), childdata);
                    }
                } else {
                    final ChildData childfeedentry = new ChildData();
                    //childfeedentry.set_feeder_id(map.get("pond_name").toString().trim());
                    childfeedentry.set_feeder_id(map.get("pond_sno").toString().trim());
                    childfeedentry.set_mdispensed_feed(map.get("dispensed_feed").toString().trim());
                    childdata.add(childfeedentry);
                    listFeedChildData.put(listFeedGroupData.get(i), childdata);
                }
            }
            listAdaptershow = new ExpandableListAdapterShow(MainActivity.this, listFeedGroupData, listFeedChildData);
            listAdaptershow.notifyDataSetChanged();
            expListView.setAdapter(listAdaptershow);
           }catch (Exception e){
            e.printStackTrace();

           }
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                Map<String, String> map = pondArrList.get(groupPosition);
                String str=map.get("pond_sno").toString().trim();
                String pondname=map.get("pond_name".toString().trim());
               // getfeeders_inpond(user_id,str,timezone,groupPosition);
                try{
                    Intent viewpager=new Intent(MainActivity.this,ViewpagerActivity.class);
                    Bundle bundle = new Bundle();
                    viewpager.putExtras(bundle);
                    // viewpager.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   // Map<String, String> map = pondArrList.get(pos);
                   // String str=map.get("pond_sno").toString().trim();
                   // String pondname=map.get("pond_name").toString().trim();
                    viewpager.putExtra("pond_sno", str);
                    viewpager.putExtra("pond_name", pondname);
                  //  viewpager.putExtra("pond_feeders_data", mylist);
                    session.put("pond_name", pondname);
                    startActivity(viewpager);

                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try{
             getMenuInflater().inflate(R.menu.main, menu);
             MenuItem menuItem = menu.findItem(R.id.login_in1);
             String user_name = session.get("firstname");
             menuItem.setTitle("User : "+user_name);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getPondDetailsClassData(user_id);
                return true;
            case R.id.action_settings1:
                try{
                    try{
                        util.showAlertDialog(this,"Logout","Would you like to logout?",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.logoutUser();
                                finishAffinity();
                            }
                        });

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.login_in1:
                Intent user_intent = new Intent(MainActivity.this,UserProfileActivity.class);
                user_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(user_intent);
                return true;

            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
