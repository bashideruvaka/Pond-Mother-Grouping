package eruvaka.pmgp.classes;

/**
 * Created by eruvaka on 10-03-2017.
 */
public class GroupFeedEntry {
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
}
