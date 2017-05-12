package eruvaka.pmgp.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eruvaka.pmgp.Activitys.MainActivity;
import eruvaka.pmgp.R;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.common.Utils;
import eruvaka.pmgp.serverconnect.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsFragment extends Fragment {
	ArrayList<HashMap<String, String>> update_list = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> schedule_list = new ArrayList<HashMap<String, String>>();
	private static final int TIMEOUT_MILLISEC = 0;
    AlertDialog.Builder build ;
	UserSession userPreferenceData;
	String user_id,timezone,feederSno;
	EditText feedername,okdtime;
	ProgressDialog myDialog;
	Utils util;
	UserSession session;
	String ownerid,PondName;
	Spinner ponds_sp, mode_sp;
	ArrayList<HashMap<String, String>> pondArrList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String,String>> data_list=new ArrayList<>();
	ArrayAdapter<String> ad_sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View v = inflater.inflate(R.layout.settings, container, false);
	    try{
			userPreferenceData = new UserSession(getActivity());
			user_id = userPreferenceData.get("user_id");
			util = new Utils(getActivity());
			session = new UserSession(getActivity());
			build = new AlertDialog.Builder(getActivity());
			user_id = session.get("user_id");
			ownerid = session.get("ownerid");
			PondName=session.get("pond_name");
			timezone=session.get("timezone");
			String ponds_array= session.get("ponds_array");
			mode_sp=(Spinner)v.findViewById(R.id.mode_sp);
			feedername=(EditText)v.findViewById(R.id.feedername);
			okdtime=(EditText)v.findViewById(R.id.okdtime);
			ArrayList<String> mode_array=new ArrayList<>();
			mode_array.add("Basic Mode");
			mode_array.add("Schedule Mode");
			mode_array.add("Automatic Mode");
			ad_sp = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2, mode_array);
			ad_sp.setDropDownViewResource(R.layout.spinner_dropdown);
			mode_sp.setAdapter(ad_sp);
			ponds_sp=(Spinner)v.findViewById(R.id.pondsId);
			pondArrList.clear();
			data_list.clear();
			JSONArray ponds=new JSONArray(ponds_array);
			for (int i = 0; i < ponds.length(); i++) {
				HashMap<String, String> mapping = new HashMap<String, String>();
				JSONObject jObject = ponds.getJSONObject(i);
				mapping.put("pond_name", jObject.getString("pond_name"));
				mapping.put("pond_sno", jObject.getString("pond_sno"));
				pondArrList.add(mapping);
			}
			if(pondArrList.size()>0){
				pondsDisplay();
			}else{
				Toast.makeText(getActivity(),"pond array is empty",Toast.LENGTH_SHORT).show();
			}

  		Button dialogupdate=(Button)v.findViewById(R.id.update_settings);
			dialogupdate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String fName=feedername.getText().toString();
					String DTIme=okdtime.getText().toString();
					String mode=mode_sp.getSelectedItem().toString().trim();
					String s_mode="";
					 if(mode.equals("Basic Mode")){
						 s_mode="78".toString();
					 }else if(mode.equals("Schedule Mode")){
						 s_mode="79".toString();
					 }else if(mode.equals("Automatic Mode")){
						 s_mode="ff".toString();
					 }

					try{
      						 if(fName.isEmpty()||DTIme.isEmpty()|| s_mode.isEmpty()){
      							Toast.makeText(getActivity(), R.string.nullvalues, Toast.LENGTH_SHORT).show(); 
      						 }else{
      							 try{
      						     update_DeviceData(s_mode);
      							 }catch(Exception e){
      								 e.printStackTrace();
      							 }
      						 }
	        					
	        					 }catch(Exception e){
	        						 e.printStackTrace();
	        					 }

      				 
				}
			});
		  	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return v;
	}

	private void pondsDisplay() {ArrayList<String> al=new ArrayList<>();
		for (int i = 0; i < pondArrList.size(); i++) {
			Map<String, String> map = pondArrList.get(i);
			String pond_name = map.get("pond_name").toString().trim();
			al.add(pond_name);
			String pond_sno = map.get("pond_sno").toString().trim();
		}
		ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2, al);
		ad.setDropDownViewResource(R.layout.spinner_dropdown);
		ponds_sp.setAdapter(ad);
		int position = ad.getPosition(PondName);
		ponds_sp.setSelection(position);
		ponds_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> av, View v, int position, long d) {
				// TODO Auto-generated method stub
				String location = av.getItemAtPosition(position).toString().trim();
				Map<String, String> map = pondArrList.get(position);
				String pond_name = map.get("pond_name").toString().trim();
				String pond_sno = map.get("pond_sno").toString().trim();
				if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
					//retrieveFeedersClassData(pond_sno);
				} else {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_SHORT).show();
				}

			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

	}

	private void update_DeviceData(String mode) {
		JsonObject object = new JsonObject();
		object.addProperty("user_id", user_id);
		object.addProperty("feeder_sno", util.get_feederSno_Sno().toString());
		object.addProperty("feeder_alias_name", feedername.getText().toString());
		object.addProperty("feed_disptime",okdtime.getText().toString());
		object.addProperty("sch_mode",mode);
		Call<JsonObject> call = util.getAdapter_feederSetting_update().feedersettings(object);
		 util.showProgressDialog();
		call.enqueue(new Callback<JsonObject>() {
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
				Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
				if (response.isSuccessful()) {
					processResponse(response.body());
					Log.e("Single Feeder Update ",response.body().toString());
				 	util.dismissDialog();
				} else {
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

	private void processResponse(JsonObject jsn) {
		try {
			String result = jsn.toString();
			JSONObject jsnobj = new JSONObject(result);
			String status = jsnobj.getString("status");
			String zero = "0".toString().trim();
			if (status.equals(zero)) {
			} else {
				String data=jsnobj.getString("data");
				build.setTitle("Settings");
				build.setMessage(data);
				build.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int which) {
								dialog.cancel();


							}
						});
				build.show();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}


}
