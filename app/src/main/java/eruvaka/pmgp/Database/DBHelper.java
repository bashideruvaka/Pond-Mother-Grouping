package eruvaka.pmgp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eruvaka on 20-03-2017.
 */

 public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE="pondmother.2";
    public static final int VERSION=1;

    public static final String TABLE1="feedentry";
    public static final String FID="FID";
    public static final String MODE="MODE";
    public static final String SCHDID="SCHDID";
    public static final String TF="TF";
    public static final String OCF="OCF";
    public static final String FG="FG";
    public static final String DF="DF";
    public static final String HEXID="HEXID";
    public static final String FROM_TIME="FROM_TIME";
    public static final String TO_TIME="TO_TIME";
    public static final String TOTAL_TIME="TOTAL_TIME";

    public static final String DEVICE_TF="DEVICE_TF";
    public static final String DEVICE_OCF="DEVICE_OCF";
    public static final String DEVICE_FG="DEVICE_FG";


    public DBHelper(Context context) {
         super(context, DATABASE, null, VERSION);
        // TODO Auto-generated constructor stub
}

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE feedentry (FID text," +
                "MODE TEXT, SCHDID TEXT, TF TEXT, OCF TEXT, FG TEXT, " +
                "DF TEXT, HEXID TEXT, TOTAL_TIME TEXT,FROM_TIME TEXT,TO_TIME TEXT,STATUS TEXT,DEVICE_TF TEXT,DEVICE_OCF TEXT,DEVICE_FG TEXT,KG_DSIP TEXT)" );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
