package eruvaka.pmgp.classes;

import java.util.ArrayList;

/**
 * Created by eruvaka on 21-04-2017.
 */
public class ChildData {
    private String mschedule_id;
    private String mschedule_times;
    private String moriginal_feed;
    private String mone_cycle_feed;
    private String mfeed_gap;
    private String mdispensed_feed;
    private String mtotalTime;

    public void set_schedule_id(String schedule_id) {
        // TODO Auto-generated method stub
        mschedule_id=schedule_id;
    }
    public String get_mschedule_id() {
        // TODO Auto-generated method stub
        return mschedule_id;
    }
    public void set_schedule_times(String schedule_times) {
        // TODO Auto-generated method stub
        mschedule_times=schedule_times;
    }
    public String get_schedule_times() {
        // TODO Auto-generated method stub
        return mschedule_times;
    }
    public void set_original_feed(String original_feed) {
        // TODO Auto-generated method stub
        moriginal_feed=original_feed;
    }
    public String get_original_feed() {
        // TODO Auto-generated method stub
        return moriginal_feed;
    }
    public void set_one_cycle_feed(String one_cycle_feed) {
        // TODO Auto-generated method stub
        mone_cycle_feed=one_cycle_feed;
    }
    public String get_one_cycle_feed() {
        // TODO Auto-generated method stub
        return mone_cycle_feed;
    }
    public void set_feed_gap(String feed_gap) {
        // TODO Auto-generated method stub
        mfeed_gap=feed_gap;
    }
    public String get_feed_gap() {
        // TODO Auto-generated method stub
        return mfeed_gap;
    }
    public void set_mdispensed_feed(String dispensed_feed) {
        // TODO Auto-generated method stub
        mdispensed_feed=dispensed_feed;
    }
    public String get_dispensed_feed() {
        // TODO Auto-generated method stub
        return mdispensed_feed;
    }
    //mtotalTime
    public void set_totalTime(String totalTime) {
        // TODO Auto-generated method stub
        mtotalTime=totalTime;
    }
    public String get_totalTime() {
        // TODO Auto-generated method stub
        return mtotalTime;
    }
    private String mfeeder_id;
    public void set_feeder_id(String feeder_id) {
        // TODO Auto-generated method stub
        mfeeder_id=feeder_id;
    }
    public String get_feeder_id() {
        // TODO Auto-generated method stub
        return mfeeder_id;
    }
    private String mfeeder_sno;
    public void set_feeder_sno(String feeder_sno) {
        // TODO Auto-generated method stub
        mfeeder_sno=feeder_sno;
    }
    public String get_feeder_sno() {
        // TODO Auto-generated method stub
        return mfeeder_sno;
    }
    //set_schedule_date
    private String mschedule_date;
    public void set_schedule_date(String schedule_date) {
        // TODO Auto-generated method stub
        mschedule_date=schedule_date;
    }
    public String get_schedule_date() {
        // TODO Auto-generated method stub
        return mschedule_date;
    }
    private String mfrom_time;
    public void set_from_time(String from_time){
        mfrom_time=from_time;
    }
    public String get_from_time(){
        return mfrom_time;
    }
    private String mto_time;
    public void set_to_time(String to_time){
        mto_time=to_time;
    }
    public String get_to_time(){
        return mto_time;
    }
    private String mstatus;
    public void set_status(String status){
        mstatus=status;
    }
    public String get_status(){
        return mstatus;
    }
    public String m_mode;
    public void set_mode(String mode){
        m_mode=mode;
    }
    public String get_mode(){
        return m_mode;
    }
    private String m_schedules;
    public void set_schedules(String schedules){
        m_schedules=schedules;
    }
    public String get_schedules(){
        return m_schedules;
    }
   private String mlast_updatetime;
    public void set_last_updatetime(String _last_updatetime) {
        this.mlast_updatetime = _last_updatetime;
    }
    public String getlast_updatetime(){
        return mlast_updatetime;
    }

}
