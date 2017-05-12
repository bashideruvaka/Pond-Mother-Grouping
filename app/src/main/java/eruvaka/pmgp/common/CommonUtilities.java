package eruvaka.pmgp.common;

import android.content.Context;
import android.content.Intent;



public class CommonUtilities {

    // Google project id
    public  static final String SENDER_ID = "1022112979788";
        /**
     * Tag used on log messages.
     */
    public static final String TAG = "Android GCM";

    public static final String DISPLAY_MESSAGE_ACTION ="eruvaka.pondguard.DISPLAY_MESSAGE";

    public  static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }


}
