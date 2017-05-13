package eruvaka.pmgp.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import org.json.JSONException;

import org.json.JSONObject;
import java.util.HashMap;
import eruvaka.pmgp.R;
import eruvaka.pmgp.serverconnect.RetroHelper;
import eruvaka.pmgp.serverconnect.ServiceHelper;


/**
 * Created by bashid on 02/27/2016.
 */

public class Utils {
    private Context ctx;
    private static final String TAG = "Utils";
    MaterialDialog ringProgressDialog = null;

    public Utils(Context ctx) {
        this.ctx = ctx;
    }
    public static void showAlertDialog(final Context context , String title , String message, DialogInterface.OnClickListener onClickListener){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // setting Dialog title
        alertDialog.setTitle(title);
        // setting Dialog message
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", onClickListener);
        alertDialog.show();

    }
    public void showProgressDialog() {
        if (ringProgressDialog == null) {
            ringProgressDialog = new MaterialDialog.Builder(ctx)
                    //.title(ctx.getResources().getString(R.string.app_name))
                    .content("Please wait.... ")
                    .progress(true, 0)
                    .theme(Theme.LIGHT)
                    .cancelable(false)
                    .show();
            ringProgressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    try {
                        ProgressBar v = (ProgressBar) ringProgressDialog.findViewById(android.R.id.progress);
                        v.getIndeterminateDrawable().setColorFilter(ctx.getResources().getColor(R.color.colorPrimaryDark),
                                android.graphics.PorterDuff.Mode.MULTIPLY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void dismissDialog() {
        if (ringProgressDialog != null) {
            if (ringProgressDialog.isShowing()) {
                ringProgressDialog.dismiss();
                ringProgressDialog = null;
            }
        }
    }
    /* Adapter Classes */
    public ServiceHelper getBaseClassService() {
        return new RetroHelper().getAdapter(ctx, "", null).create(ServiceHelper.class);
    }
    public ServiceHelper getBaseClassService_pond() {
        return new RetroHelper().getAdapter_pond(ctx, "", null).create(ServiceHelper.class);
    }
    public ServiceHelper getBaseClassService_feders() {
        return new RetroHelper().getAdapter_feeders(ctx, "", null).create(ServiceHelper.class);
    }
    public ServiceHelper getBaseClassService_update_schedules(){
        return new RetroHelper().getAdapter_schedule_update(ctx,"",null).create(ServiceHelper.class);
    }
    public ServiceHelper getAdapter_singlefeeder_update(){
        return new RetroHelper().getAdapter_singlefeeder_update(ctx,"",null).create(ServiceHelper.class);
    }
    public ServiceHelper getAdapter_feederLogs(){
        return new RetroHelper().getAdapter_feederlogs(ctx,"",null).create(ServiceHelper.class);
    }
    public ServiceHelper getAdapter_feederSetting_update(){
        return new RetroHelper().getAdapter_feederSettings(ctx,"",null).create(ServiceHelper.class);
    }
    /*  String Declaration  */
    private static String mpond_sno;
    public static void add_pondsSno(String pond_sno) {
        mpond_sno=pond_sno;
    }
    public static String  get_pond_Sno() {
        return mpond_sno;
    }
    private static String mfeeder_sno;
    public static void add_feederSno(String feeder_sno) {
        mfeeder_sno=feeder_sno;
    }
    public static String  get_feederSno_Sno() {
        return mfeeder_sno;
    }
}
