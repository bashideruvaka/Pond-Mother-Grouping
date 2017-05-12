package eruvaka.pmgp.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import eruvaka.pmgp.Activitys.MainActivity;
import eruvaka.pmgp.R;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.common.Utils;
import eruvaka.pmgp.serverconnect.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> updatelist = new ArrayList<HashMap<String, String>>();
    android.support.v7.app.ActionBar bar;
    private Context context;
    private Utils util;
    private EditText edtUserName, edtPassword;
    private Button btnLogin;
    UserSession session;
    String timezone;
    private BroadcastReceiver approvedReceiver;
    private String mRegToken;
    private static final String TAG = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = Login.this;
        util = new Utils(context);
        session = new UserSession(getApplicationContext());
        //registering  gcm_id
        approvedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: " + "msg received");
                if (intent.getAction().equals("REG_TOKEN")) {
                    mRegToken = intent.getStringExtra("reg_token");
                    Log.d("receiver", "Got message: " + mRegToken);
                    System.out.println(mRegToken);
                }

            }
        };
        //checking user login
        if (session.isUserLoggedIn()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            edtUserName = (EditText) findViewById(R.id.loginusername1);
            edtPassword = (EditText) findViewById(R.id.loginpasswd1);
            btnLogin = (Button) findViewById(R.id.login_loginbutton1);
            CheckBox box = (CheckBox)findViewById(R.id.tv_show_password);
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT); actionBar = getSupportActionBar();
            actionBar.hide();
            box.setChecked(true);
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                    if(checked){
                        edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                    else{
                        edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    }
                }
            });
            try {
                btnLogin.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        String LoginUserNameString = edtUserName.getText().toString().trim();
                        String LoginUserPasswordString = edtPassword.getText().toString().trim();
                        if (LoginUserNameString.isEmpty() || LoginUserPasswordString.isEmpty()) {
                            Toast.makeText(Login.this, "enter userid and password", Toast.LENGTH_SHORT).show();
                        } else {
                            //check networkaviable
                            if (new ConnectionDetector(context).isConnectingToInternet()) {
                                mylist.clear();
                                updatelist.clear();
                                retrieveLoginClassData();
                            } else {
                                // TODO Auto-generated method stub
                                Toast.makeText(Login.this, "no internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void retrieveLoginClassData() {
        final String username = edtUserName.getText().toString().toLowerCase().trim();
        final String password = edtPassword.getText().toString().trim();
        JsonObject object =new JsonObject();
        object.addProperty("username",username);
        object.addProperty("password",password);
        Call<JsonObject> call = util.getBaseClassService().login(object);
        util.showProgressDialog();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
                if (response.isSuccessful()) {
                          login_Response(response.body());
                    util.dismissDialog();
                }


            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("--", "onFailure : ");
                t.printStackTrace();
                util.dismissDialog();
            }

        });
    }

    private void login_Response(JsonObject jsnobj) {
        try {
            String result=  jsnobj.toString();
            JSONObject jsn = new JSONObject(result);
            String status = jsn.getString("status");
            String zero = "0".toString().trim();
            if (status.equals(zero)) {
                String error=jsn.getString("error");
                util.showAlertDialog(this," ERROR ",error,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }else{
             try{
                 JSONObject jsnonb = jsn.getJSONObject("data");
                 session.put("user_id", jsnonb.getString("user_sno"));
                 session.put("firstname",jsnonb.getString("firstname"));
                 session.put("lastname",jsnonb.getString("lastname"));
                 session.put("mobilenumber",jsnonb.getString("mobilenumber"));
                 session.put("emailid",jsnonb.getString("emailid"));
                 session.put("timezone",jsnonb.getString("timezone"));
                 session.put("Device","Device");
                 session.createUserLoginSession(edtUserName.getText().toString(), edtPassword.getText().toString());
                 Intent intent = new Intent(Login.this, MainActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
                 finish();
             }catch (Exception e){
                 e.printStackTrace();
             }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
