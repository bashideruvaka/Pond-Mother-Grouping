package eruvaka.pmgp.classes;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public HashMap<String, String[]> getOriginalFeed() {
        return originalFeed;
    }

    public void setOriginalFeed(HashMap<String, String[]> originalFeed) {
        this.originalFeed = originalFeed;
    }
}
