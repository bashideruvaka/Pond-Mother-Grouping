package eruvaka.pmgp.classes;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by NetApp on 13/05/17.
 */

public class ScheduleSaveData {

   /* "user_id": "20",
            "original_feed": "{\"45\":[\"10.0\"],\"46\":[\"10.0\"],\"47\":[\"10.0\"]}",
            "one_cycle_feed": "{\"45\":[\"500\"],\"46\":[\"500\"],\"47\":[\"500\"]}",
            "feed_gap": "{\"45\":[\"2\"],\"46\":[\"2\"],\"47\":[\"2\"]}",
            "schedule_id": "{\"45\":[\"23477\"],\"46\":[\"23478\"],\"47\":[\"23479\"]}",
            "total_time": "{\"45\":[\"00:00\"],\"46\":[\"00:00\"],\"47\":[\"00:00\"]}",
            "from_time": "{\"45\":[\"11:24\"],\"46\":[\"11:24\"],\"47\":[\"11:24\"]}",
            "to_time": "{\"45\":[\"00:00\"],\"46\":[\"00:00\"],\"47\":[\"00:00\"]}",
            "status": "{\"45\":[\"running\"],\"46\":[\"running\"],\"47\":[\"running\"]}",
            "mode": "{\"45\":[\"78\"],\"46\":[\"78\"],\"47\":[\"78\"]}",
            "on_time": "{\"45\":[\"0\"],\"46\":[\"0\"],\"47\":[\"0\"]}",
            "off_time": "{\"45\":[\"0\"],\"46\":[\"0\"],\"47\":[\"0\"]}",
            "feederSno": "[\"45\", \"46\", \"47\"]",
            "feeder_hexid": "[\"FEEDER00007\", \"A1CF9E\", \"FEEDER00009\"]",
            "schedule_start_date": "2017-05-13",
            "schedule_end_date": "2017-05-13"*/

    @SerializedName("serialNo")
    String user_id;

    @SerializedName("original_feed")
    HashMap<String,String[]> originalFeed;
    @SerializedName("one_cycle_feed")
    HashMap<String,String[]> one_cycle_feed;
    @SerializedName("feed_gap")
    HashMap<String,String[]> feed_gap;
    @SerializedName("schedule_id")
    HashMap<String,String[]> schedule_id;
    @SerializedName("total_time")
    HashMap<String,String[]> total_time;
    @SerializedName("from_time")
    HashMap<String,String[]> from_time;
    @SerializedName("to_time")
    HashMap<String,String[]> to_time;
    @SerializedName("status")
    HashMap<String,String[]> status;
    @SerializedName("mode")
    HashMap<String,String[]> mode;
    @SerializedName("on_time")
    HashMap<String,String[]> on_time;
    @SerializedName("off_time")
    HashMap<String,String[]> off_time;
    @SerializedName("feederSno")
    HashMap<String,String[]> feederSno;
    @SerializedName("hex_id")
    HashMap<String,String []> hex_id;
    @SerializedName("start_date")
    String  start_date;
    @SerializedName("end_date")
    String  end_date;
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getStartDate(){
        return start_date;
    }

    public void setStartDate(String start_date){
        this.start_date=start_date;
    }
    public String getEnd_date(){
        return end_date;
    }
    public void setEndDate(String end_date){
        this.end_date=end_date;
    }
    public HashMap<String, String[]> getOriginalFeed() {
        return originalFeed;
    }

    public void setOriginalFeed(HashMap<String, String[]> originalFeed) {
        this.originalFeed = originalFeed;
    }
    public HashMap<String,String[]> getonecyclefeed(){
        return one_cycle_feed;
    }
    public  void setOne_cycle_feed(HashMap<String,String[]> one_cycle_feed){

    }
    public  HashMap<String,String[]>  getfeed_gap(){
      return feed_gap;
        }
    public  void setFeedGap(HashMap<String,String[]> feed_gap){
        this.feed_gap=feed_gap;

    }
    public HashMap<String,String[]> getSchedule_id(){
        return schedule_id;

    }
    public void setschdule_id(HashMap<String,String[]> schedule_id){
        this.schedule_id=schedule_id;

    }
    public HashMap<String,String[]> gettotal_time(){
      return total_time;
    }
    public void setTotal_time(HashMap<String,String[]> total_time){
        this.total_time=total_time;
    }
    public HashMap<String,String[]> getfrom_time(){
        return from_time;

    }
    public void setFrom_time(HashMap<String,String[]> from_time){
        this.from_time=from_time;
    }
    public HashMap<String,String[]> getTo_time(){
        return to_time;
    }
    public void setTo_time(HashMap<String,String[]> to_time){
        this.to_time=to_time;
    }
    public HashMap<String,String[]> getstatus(){
        return status;
    }
    public void setStatus(HashMap<String,String[]> status){
        this.status=status;
    }
    public HashMap<String,String[]>   getmode(){
      return mode;
   }
    public void setMode(HashMap<String,String[]> mode){
        this.mode=mode;
    }

    public HashMap<String,String[]> getontime(){
        return on_time;
    }
    public void setOn_time(HashMap<String,String[]> on_time){
        this.on_time=on_time;
    }
    public  HashMap<String,String[]> getofftime(){
        return off_time;
    }
    public void setOff_time(HashMap<String,String[]> off_time){
        this.off_time=off_time;
    }
    public HashMap<String,String[]> getfeedersno(){
        return feederSno;
    }
    public void setFeederSno(HashMap<String,String[]> feederSno){
        this.feederSno=feederSno;
    }
    public HashMap<String,String[]> gethex_id(){
        return hex_id;
    }
    public void setHex_id(HashMap<String,String[]> hex_id){
        this.hex_id=hex_id;
    }
    static ArrayList<String> feederSno_array=new ArrayList<String>();
    static ArrayList<String> hex_id_arrays=new ArrayList<String>();
    public static void add(ArrayList<HashMap<String, String>> mylist, ArrayList<HashMap<String, String>> group_array) {
        ArrayList<String> ids_array=new ArrayList<>();
        for (int i = 0; i < mylist.size(); i++) {
            try {
                final HashMap<String, String> map1 = mylist.get(i);
                final String feederSno = map1.get("feederSno").toString().trim();
                final String feederId = map1.get("feederId").toString().trim();
                final String hex_id = map1.get("hex_id").toString().trim();
                final String schedules = map1.get("schedules").toString().trim();
                feederSno_array.add("\"" + feederSno + "\"");
                hex_id_arrays.add("\"" + hex_id + "\"");
                HashSet<String> hashSet = new HashSet<String>();
                hashSet.addAll(feederSno_array);
                feederSno_array.clear();
                feederSno_array.addAll(hashSet);
                JSONArray tf_array = new JSONArray();
                JSONArray scid_array = new JSONArray();
                JSONArray ocf_array = new JSONArray();
                JSONArray fg_array = new JSONArray();
                JSONArray df_array = new JSONArray();
                JSONArray from_time_array = new JSONArray();
                JSONArray to_time_array = new JSONArray();
                JSONArray total_time_array = new JSONArray();
                JSONArray status_array = new JSONArray();
                JSONArray mode_array = new JSONArray();
                JSONArray ontime_array = new JSONArray();
                JSONArray offtime_array = new JSONArray();
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
                        System.out.println("main"+schedule_id);
                    }
                }
                for (int j = 0; j < group_array.size(); j++) {
                    HashMap<String, String> map = group_array.get(j);
                      for(int y=0;y<ids_array.size();y++){
                        scid_array.put(ids_array.get(y));
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

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
