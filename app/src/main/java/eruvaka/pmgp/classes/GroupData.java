package eruvaka.pmgp.classes;

/**
 * Created by eruvaka on 21-04-2017.
 */
public class GroupData {
    private String mfeeder_id;
    private String madate;
    private String mfeederSno;
    private String mSchduleDate;
    private String mlast_update_time;
    private String m_mode;

    public void set_feeder_id(String feeder_id) {
        // TODO Auto-generated method stub
        mfeeder_id=feeder_id;
    }
    public String get_feeder_id() {
        // TODO Auto-generated method stub
        return mfeeder_id;
    }
    public void set_date(String date) {
        // TODO Auto-generated method stub
        madate=date;
    }
    public String get_date() {
        // TODO Auto-generated method stub
        return madate;
    }
    public void set_feederSno(String feederSno) {
        // TODO Auto-generated method stub
        mfeederSno=feederSno;
    }
    public String get_feederSno() {
        // TODO Auto-generated method stub
        return mfeederSno;
    }

    public void set_scheduleDate(String SchduleDate) {
        mSchduleDate=SchduleDate;
    }
    public String get_scheduleDate() {
        // TODO Auto-generated method stub
        return mSchduleDate;
    }

    public void set_last_update_time(String last_update_time) {
        mlast_update_time = last_update_time;
    }
    public String get_last_update_time() {
        // TODO Auto-generated method stub
        return mlast_update_time;
    }

    public void set_mode(String _mode) {
        this.m_mode = _mode;
    }
    public  String get_mode(){
        return m_mode;
    }
    private String mday_feed;
    public void set_dayfeed(String dayfeed) {
        this.mday_feed = dayfeed;
    }
    public  String get_dayfeed(){
        return mday_feed;
    }
    private String mdispensed_feed;
    public void set_dispensed_feed(String dispensefeed) {
        this.mdispensed_feed = dispensefeed;
    }
    public  String get_dispensed_feed(){
        return mdispensed_feed;
    }
    private String mpondname;
    public void set_pondname(String pondname) {
        this.mpondname = pondname;
    }
    public  String get_pondname(){
        return mpondname;
    }
}
