package eruvaka.pmgp.Activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eruvaka.pmgp.R;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.login.Login;

/**
 * Created by eruvaka on 28-02-2017.
 */
public class UserProfileActivity extends ActionBarActivity {

    private static final int TIMEOUT_MILLISEC = 0;
    SharedPreferences ApplicationDetails;
    SharedPreferences.Editor ApplicationDetailsEdit;
    android.support.v7.app.ActionBar bar;
    EditText fname,lname,mobile,oldpws,newpwd,emailid;
    UserSession userPreferenceData;
    Button update;
    Context context;
    ProgressDialog myDialog;
    String user_id;
    /* (non-Javadoc)
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.signinupshape));
        bar = getSupportActionBar();
        // bar.setTitle(getResources().getString(R.string.signup));
        bar.setTitle("User Details");
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        context=getApplicationContext();
        userPreferenceData = new UserSession(getApplicationContext());
        user_id = userPreferenceData.get("user_id");
        String FirstName=userPreferenceData.get("firstname");
        String lastname=userPreferenceData.get("lastname");
        String mobilenumber=userPreferenceData.get("mobilenumber");
        String emailId = userPreferenceData.get("emailid");
        fname=(EditText)findViewById(R.id.user_fname);
        fname.setText(FirstName);
        mobile=(EditText)findViewById(R.id.user_mobile);
        mobile.setText(mobilenumber);
        lname=(EditText)findViewById(R.id.user_lname);
        lname.setText(lastname);
        oldpws=(EditText)findViewById(R.id.oldpwd);
        newpwd=(EditText)findViewById(R.id.newpwd);
        emailid=(EditText)findViewById(R.id.user_email);
        emailid.setText(emailId);

        update=(Button)findViewById(R.id.user_update);

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try{
                    String email=emailid.getText().toString().trim();
                    String firstname=fname.getText().toString().trim();
                    String lastname=lname.getText().toString().trim();
                    String mobilenumber=mobile.getText().toString().trim();
                    String old_pwd=oldpws.getText().toString().trim();
                    String new_pwd=newpwd.getText().toString().trim();

                    if(  mobilenumber.isEmpty() || old_pwd.isEmpty() || new_pwd.isEmpty() || firstname.isEmpty() ||lastname.isEmpty()  || email.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), R.string.enterdata,Toast.LENGTH_SHORT).show();
                    }else if(mobilenumber.length() > 16 || mobilenumber.length() < 7){
                        Toast.makeText(getApplicationContext(), "Please Enter Valid Mobile Number to verify ",Toast.LENGTH_SHORT).show();
                    }else if(!(oldpws.getText().toString().matches("[a-zA-Z0-9._@]+") && oldpws.length() >= 6 )){
                        Toast.makeText(getApplicationContext(), "Enter Password Atleast 6 to 30 Characters with a Combination of Lowercase Letters,Numbers & Special Charecters(such as @,., and _)",Toast.LENGTH_SHORT).show();

                    }else if(!(newpwd.getText().toString().matches("[a-zA-Z0-9._@]+") && newpwd.length() >= 6 )){
                        Toast.makeText(getApplicationContext(), "Enter Password Atleast 6 to 30 Characters with a Combination of Lowercase Letters,Numbers & Special Charecters(such as @,., and _)",Toast.LENGTH_SHORT).show();
                    }
                    else if(!(ValidName(fname.getText().toString()) && fname.length()>=3 && fname.length()<25)){
                        Toast.makeText(getApplicationContext(), "Please Enter First Name Atleast 3 to 25 Without Any Special Characters. Accepts Only Alphabetics", Toast.LENGTH_SHORT).show();
                    }else if(!(ValidName(lname.getText().toString()) && lname.length()>=1 && lname.length()<25)){
                        Toast.makeText(getApplicationContext(), "Please Enter Last Name Atleast 1 to 25 Without Any Special Characters. Accepts Only Alphabetics", Toast.LENGTH_SHORT).show();

                    }else if(!(ValidEmail(emailid.getText().toString())&& emailid.length()>0)){

                        Toast.makeText(getApplicationContext(), "please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                    }else{

                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    private void loginSaveData(String s) {
        try {
            String json_result = s.toString();
            JSONObject json = new JSONObject(json_result);
            String status = json.getString("status");
            String zero = "0".toString().trim();
            if (status.equals(zero)) {
                String error = json.getString("error");
              /// Utility.hideLoader();
            }else{
                try {
                    String response = json.getString("response");
                    String user_data = json.getString("data");


                    JSONObject jsn2 = new JSONObject(user_data);
                    String FirstName = jsn2.getString("firstname");
                    String lastname = jsn2.getString("lastname");
                    String mobilenumber = jsn2.getString("mobilenumber");
                    String emailid = jsn2.getString("emailid");

                    userPreferenceData.put("firstname",FirstName);
                    userPreferenceData.put("lastname",lastname);
                    userPreferenceData.put("mobilenumber",mobilenumber);
                    userPreferenceData.put("emailid",emailid);


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfileActivity.this);
                    alertDialog.setMessage(response);
                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {
                                    dialog.cancel();


                                }
                            });
                    alertDialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private boolean ValidName(String firstName) {
        String PATTERN = "[a-zA-Z ]+";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(firstName);
        return matcher.matches();
    }
    private boolean ValidEmail(String email) {
        String PATTERN = "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(UserProfileActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
