package eruvaka.pmgp.fragments;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import eruvaka.pmgp.R;
import eruvaka.pmgp.common.UserSession;
import eruvaka.pmgp.common.Utils;
import eruvaka.pmgp.datetimePicker.DatePickerEndFragment;
import eruvaka.pmgp.datetimePicker.DatePickerStartFragment;
import eruvaka.pmgp.serverconnect.ConnectionDetector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment implements OnClickListener{

	TextView txtStockEntryDate;
	TextView txtEndDate;
	private int mStartDay = 0;
	private int mStartMonth = 0;
	private int mStartYear = 0;
	private int mEndDay=0;
	private int mEndMonth=0;
	private int mEndYear=0;

	private static final int TIMEOUT_MILLISEC = 0;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> data_list = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> updatelist = new ArrayList<HashMap<String, String>>();
	String user_id,timezone,feederSno;
	TableLayout td;
	AlertDialog.Builder build;
	ArrayList<HashMap<String, String>> pondArrList = new ArrayList<HashMap<String, String>>();
	UserSession session;
	ProgressDialog myDialog;
	Spinner pond_spinner ;
	String ownerid,PondName;
	Utils util;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View v = inflater.inflate(R.layout.history, container, false);
	    try{
			session = new UserSession(getActivity());
			user_id = session.get("user_id");
			ownerid = session.get("ownerid");
			PondName=session.get("pond_name");
			System.out.println("hist"+PondName);
			timezone=session.get("timezone");
			String pondarray= session.get("ponds_array");
			util=new Utils(getActivity());
			pond_spinner=(Spinner)v.findViewById(R.id.PondsId);
			//feederSno_spinner=(Spinner)v.findViewById(R.id.feederId2);
			try {
				pondArrList.clear();
				JSONArray ponds=new JSONArray(pondarray);
				for (int i = 0; i < ponds.length(); i++) {
					HashMap<String, String> mapping = new HashMap<String, String>();
					JSONObject jObject = ponds.getJSONObject(i);
					mapping.put("pond_name", jObject.getString("pond_name"));
					mapping.put("pond_sno", jObject.getString("pond_sno"));
					pondArrList.add(mapping);
				}
				if(pondArrList.size()>0){
					showPonds();
				}else{
					Toast.makeText(getActivity(),"pond array is empty",Toast.LENGTH_SHORT).show();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			/*data_list = (ArrayList<HashMap<String, String>>) getActivity().getIntent().getSerializableExtra("pond_feeders_data");
			ArrayList<String> al=new ArrayList<>();
			for (int i = 0; i < data_list.size(); i++) {
				final HashMap<String, String> map = data_list.get(i);
				final String feederName=map.get("feederSno").toString().trim();
				final String feederId=map.get("feederId").toString().trim();
				final String hex_id=map.get("hex_id").toString().trim();
				al.add(feederId);
				}
			ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2, al);
			ad.setDropDownViewResource(R.layout.spinner_dropdown);
			feederSno_spinner.setAdapter(ad);

			feederSno_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> av, View v, int position, long d) {
					// TODO Auto-generated method stub
					try {
						String location = av.getItemAtPosition(position).toString().trim();
						System.out.println(position);
					//	for(int j=0;j<mylist.size();j++){
							Map<String, String> map = data_list.get(position);
							final String feederSno = map.get("feederSno").toString().trim();
							final String feederId = map.get("feederId").toString().trim();
							final String hex_id = map.get("hex_id").toString().trim();
							Utils.add_feederSno(feederSno);
							//System.out.println(map);
						//}

					}catch (Exception e){
						e.printStackTrace();
					}
				if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
						//retrieveFeedersClassData(feederSno);
					} else {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "no internet connection", Toast.LENGTH_SHORT).show();
					}

				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}

			});*/
		    build = new AlertDialog.Builder(getActivity());
		 	txtStockEntryDate=(TextView)v.findViewById(R.id.txtstartdate);
			txtEndDate=(TextView)v.findViewById(R.id.txtEndDate);
			TextView load=(TextView)v.findViewById(R.id.loadbttnn);
			td=(TableLayout)v.findViewById(R.id.tblhist);
			 initializeUI();
			 intializeUI2();
			 load.setOnClickListener(this);
			 txtStockEntryDate.setOnClickListener(this);
			 txtEndDate.setOnClickListener(this);
				
				 TableLayout t1=(TableLayout)v.findViewById(R.id.tblhistdemo);
					t1.setVerticalScrollBarEnabled(true);
			   	 final LinearLayout tablerow=new LinearLayout(getActivity());
			   	  tablerow.setOrientation(LinearLayout.HORIZONTAL);
	              LinearLayout.LayoutParams paramas11 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
	                      LinearLayout.LayoutParams.WRAP_CONTENT,1);
	              tablerow.setPadding(5,5,5,5);
	              tablerow.setLayoutParams(paramas11);

			    	final TextView feederid=new TextView(getActivity());
			    	LinearLayout.LayoutParams paramas91 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
		                     LinearLayout.LayoutParams.WRAP_CONTENT,1);
			    	feederid.setLayoutParams(paramas91);
			    	feederid.setPadding(5,0,5,0);
			    	      feederid.setText("S.Time");
			    	      feederid.setTextColor(Color.parseColor("#14648d"));
			    	      feederid.setKeyListener(null);
			    	      feederid.setTextSize(17);
				      //feedkg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
			    	      feederid.setGravity(Gravity.START);
			    	      feederid.setFreezesText(true);
			    	      feederid.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
			    	      
			    	  	final TextView actuvalfeed=new TextView(getActivity());
			    	  	actuvalfeed.setLayoutParams(paramas91);
			    	  	actuvalfeed.setPadding(5,0,5,0);
			    	  	actuvalfeed.setText("T.F");
			    	  	actuvalfeed.setTextColor(Color.parseColor("#14648d"));
			    	  	actuvalfeed.setKeyListener(null);
			    	  	actuvalfeed.setTextSize(17);
					      //feedkg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
			    	  	actuvalfeed.setGravity(Gravity.CENTER);
			    	  	actuvalfeed.setFreezesText(true);
			    	  	actuvalfeed.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);  
			    	 
			    	  	final TextView modifiedfeed=new TextView(getActivity());
			    	  	modifiedfeed.setLayoutParams(paramas91);
			    	  	modifiedfeed.setPadding(5,0,5,0);
			    	  	modifiedfeed.setText("D.F");
			    	  	modifiedfeed.setTextColor(Color.parseColor("#14648d"));
			    	  	modifiedfeed.setKeyListener(null);
			    	  	modifiedfeed.setTextSize(17);
					      //feedkg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
			    	  	modifiedfeed.setGravity(Gravity.CENTER);
			    	  	modifiedfeed.setFreezesText(true);
			    	  	modifiedfeed.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI); 

			    	tablerow.addView(feederid); 
			       	tablerow.addView(actuvalfeed);
			       
			        tablerow.addView(modifiedfeed);	
			         t1.addView(tablerow);	
			         View v2=new View(getActivity());
			         //v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			         TableLayout.LayoutParams lp12 = 
				       			new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				         	int leftMargin12=0;
				            int topMargin12=0;
				            int rightMargin12=0;
				            int bottomMargin12=0;
				       			lp12.setMargins(leftMargin12, topMargin12, rightMargin12, bottomMargin12);             
				       			v2.setLayoutParams(lp12);
				       		 
			         v2.setBackgroundResource(R.drawable.seperator);
				        t1.addView(v2);
				        TextView tab_text_bottom=(TextView)v.findViewById(R.id.tab_text_bottom);
				       tab_text_bottom.setText("S.Time = Schedules Time,T.F = Total Feed,  D.F = Dispensed Feed");
			 }catch(Exception e){
				 e.printStackTrace();
			 }
	    return v;
	}
	private void showPonds() {
		ArrayList<String> al=new ArrayList<>();
		for (int i = 0; i < pondArrList.size(); i++) {
			Map<String, String> map = pondArrList.get(i);
			String pond_name = map.get("pond_name").toString().trim();
			al.add(pond_name);
			String pond_sno = map.get("pond_sno").toString().trim();
		}
	    ArrayAdapter<String> ad = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2, al);
		ad.setDropDownViewResource(R.layout.spinner_dropdown);
		pond_spinner.setAdapter(ad);
		int position = ad.getPosition(PondName);
		pond_spinner.setSelection(position);
		pond_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> av, View v, int position, long d) {
				// TODO Auto-generated method stub
				String location = av.getItemAtPosition(position).toString().trim();
				Map<String, String> map = pondArrList.get(position);
				String pond_name = map.get("pond_name").toString().trim();
				String pond_sno = map.get("pond_sno").toString().trim();
				if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
					//retrieveFeedersClassData(pond_sno);
					String str="46".toString();
					//getHistorydetails(str);
					Utils.add_feederSno(pond_sno);
				//	System.out.println("history"+pond_name);
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
	private void initializeUI() {
	   	Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(timezone));
		//Calendar calendar = new GregorianCalendar();
		//DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z"); 
		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");    
		formatter.setTimeZone(TimeZone.getTimeZone(timezone));
	    txtStockEntryDate.setText(formatter.format(calendar.getTime()));
	 	   
}
	private void intializeUI2() {
		// TODO Auto-generated method stub
		Calendar calendar = new GregorianCalendar();
		//DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");    
		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");    
		//formatter.setTimeZone(TimeZone.getTimeZone(timezone));
	  	txtEndDate.setText(formatter.format(calendar.getTime()));
		 
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
        case R.id.txtstartdate:
        	startdialog();
        	break;
        case R.id.txtEndDate:
        	enddialog();
              break;
        case R.id.loadbttnn:
			updatelist.clear();
			mylist.clear();
        	 try{
        		//current date
        		 Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone(timezone));
				 DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");    
					formatter.setTimeZone(TimeZone.getTimeZone(timezone));  
				   String cdate=formatter.format(calendar.getTime());
				   //from and to date
        	 final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
			  dateFormat.setTimeZone(TimeZone.getTimeZone(timezone)); 
			  String from_date=txtStockEntryDate.getText().toString().trim();
			  String to_date=txtEndDate.getText().toString().trim();
				 Date f_date = dateFormat.parse(from_date);
				 Date t_date = dateFormat.parse(to_date);
				 Date c_date=dateFormat.parse(cdate);
				 SimpleDateFormat format2  = new SimpleDateFormat("yyyy-MM-dd");
				 format2.setTimeZone(TimeZone.getTimeZone(timezone)); 
  			 if(f_date.after(c_date)){
					 Toast.makeText(getActivity(), "Future Date are Not Allowed in Start Date", Toast.LENGTH_SHORT).show();
				 }else if(t_date.after(c_date)){
					 Toast.makeText(getActivity(), "Future Date  are Not Allowed in End Date", Toast.LENGTH_SHORT).show();
				 }else if(f_date.after(t_date)){
					 Toast.makeText(getActivity(), "End Date is Greater than Start Date", Toast.LENGTH_SHORT).show();
				 }else{
					String str=Utils.get_feederSno_Sno().toString();
					 if(str.isEmpty()){
						 Toast.makeText(getActivity(), "Feeder Id is Empty Please Try Again", Toast.LENGTH_SHORT).show();
					 }else{
						 getHistorydetails(str);
					 }
				 }
				 
        	 }catch (Exception e) {
				// TODO: handle exception
        		 e.printStackTrace();
			}
        	break;
		}
	}
	private void startdialog() {
		  DatePickerStartFragment date = new DatePickerStartFragment();
		  try{
				  final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
				  dateFormat.setTimeZone(TimeZone.getTimeZone(timezone)); 
				  String from_date=txtStockEntryDate.getText().toString().trim();
					 Date f_date = dateFormat.parse(from_date);
					 SimpleDateFormat format2  = new SimpleDateFormat("dd-MM-yyyy");
					 format2.setTimeZone(TimeZone.getTimeZone(timezone));
					 String str_date=(format2.format(f_date)).toString().trim();
				   String[] split = str_date.split("-");
				   final int day = Integer.valueOf(split[0]);
				   final  int month = Integer.valueOf(split[1]);
				   final  int year = Integer.valueOf(split[2]);
				   Bundle args = new Bundle();
					args.putInt("year", year);
					args.putInt("month", month-1);
					args.putInt("day", day);
				    date.setArguments(args);
						 
				  }catch(Exception e){
					  e.printStackTrace();
				  }
				
			  /**
			   * Set Call back to capture selected date
			   */
			  date.setCallBack(ondate);
			  date.show(getChildFragmentManager(), "Date Picker");
		  
		   
		 }
	 OnDateSetListener ondate = new OnDateSetListener() {
		  @Override
		  public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,int selectedDay) {
			  final Calendar c = new GregorianCalendar(TimeZone.getTimeZone(timezone));
				 c.set(selectedYear, selectedMonth, selectedDay);
				mStartDay = selectedDay;
				mStartMonth = selectedMonth;
				mStartYear = selectedYear;
				if (txtStockEntryDate != null) {
					final Date date = new Date(c.getTimeInMillis());
					//SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
					sdf.setTimeZone(TimeZone.getTimeZone(timezone)); 
				 	txtStockEntryDate.setText(sdf.format(date));
				}
			  }
		 };
		//end date
		 private void enddialog() {
			  DatePickerEndFragment enddate = new DatePickerEndFragment();
			  /**
			   * Set Up Current Date Into dialog
			   */
			  try{
				   
				  final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
				  dateFormat.setTimeZone(TimeZone.getTimeZone(timezone)); 
				  String from_date=txtEndDate.getText().toString().trim();
					 Date f_date = dateFormat.parse(from_date);
					 SimpleDateFormat format2  = new SimpleDateFormat("dd-MM-yyyy");
					 format2.setTimeZone(TimeZone.getTimeZone(timezone)); 
					 String str_date=(format2.format(f_date)).toString().trim();
				  String[] split = str_date.split("-");
				   final int day = Integer.valueOf(split[0]);
				   final  int month = Integer.valueOf(split[1]);
				   final  int year = Integer.valueOf(split[2]);
				   Bundle args = new Bundle();
					args.putInt("year", year);
					args.putInt("month", month-1);
					args.putInt("day", day);
					enddate.setArguments(args);
				  }catch(Exception e){
					  e.printStackTrace();
				  }
				
			  /**
			   * Set Call back to capture selected date
			   */
			  enddate.setCallBack(ondate1);
			  enddate.show(getChildFragmentManager(), "Date Picker");
			 }
		//end date
		 OnDateSetListener ondate1 = new OnDateSetListener() {
			  @Override
			  public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,int selectedDay) {
			         final Calendar c = new GregorianCalendar(TimeZone.getTimeZone(timezone));
					 c.set(selectedYear, selectedMonth, selectedDay);
					 mEndDay = selectedDay;
					 mEndMonth = selectedMonth;
					 mEndYear = selectedYear;
					if (txtEndDate != null) {
						final Date date = new Date(c.getTimeInMillis());
						//SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
						SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
						sdf.setTimeZone(TimeZone.getTimeZone(timezone)); 
						txtEndDate.setText(sdf.format(date));
					 	 
						}
			  }
			 };


	private void getHistorydetails(String feederSno) {
		try {
			 final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
			dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
			String from_date=txtStockEntryDate.getText().toString().trim();
			String to_date=txtEndDate.getText().toString().trim();
			Date f_date = dateFormat.parse(from_date);
			Date t_date = dateFormat.parse(to_date);
			SimpleDateFormat format2  = new SimpleDateFormat("yyyy-MM-dd");
			format2.setTimeZone(TimeZone.getTimeZone(timezone));
			String startdate=(format2.format(f_date)).toString().trim();
			String enddate=(format2.format(t_date)).toString().trim();
			JsonObject object = new JsonObject();
			object.addProperty("user_id", user_id);
			//object.addProperty("from_date", startdate);
			//object.addProperty("to_date", enddate);
			//object.addProperty("feeder_id", feederSno);
			String str=Utils.get_feederSno_Sno().toString();
			object.addProperty("pondsno", str);
			object.addProperty("schedule_start_date", startdate);
			object.addProperty("schedule_end_date", enddate);
			Call<JsonObject> call = util.getBaseClassService_feders().feedersdata(object);
			 util.showProgressDialog();
			call.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					Log.e("--", "onResponse : " + response.code() + "--" + response.isSuccessful());
					if (response.isSuccessful()) {
						processResponse(response.body());
						Log.e("FeederLogs  Data ",response.body().toString());
						util.dismissDialog();
					} else {

					}

					util.dismissDialog();
				}

				@Override
				public void onFailure(Call<JsonObject> call, Throwable t) {
					Log.e("--", "onFailure : ");
					t.printStackTrace();
					util.dismissDialog();
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updated(){
					try{
						td.setVerticalScrollBarEnabled(true);
						td.removeAllViewsInLayout();
					   	 for(int i=0; i<mylist.size(); i++) {
					   		 final HashMap<String, String> map = mylist.get(i);
				   		   final TextView datetv=new TextView(getActivity());
					   		 LinearLayout.LayoutParams paramas41 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				                     LinearLayout.LayoutParams.WRAP_CONTENT,1);
					   		datetv.setLayoutParams(paramas41);
							    datetv.setTextColor(Color.parseColor("#14648d"));
							    datetv.setKeyListener(null);
							    datetv.setTextSize(16);
							    
						        //feedkg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
							    datetv.setGravity(Gravity.START);
							    datetv.setFreezesText(true);
							    datetv.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
							    //datetv.setBackgroundResource(R.drawable.rounded_green);   
							  try {
								   String sdate1 = map.get("schedule_date").toString().trim();
								SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
								 Date dt;
									dt = form.parse(sdate1);
									long mill = dt.getTime();
								//	ApplicationData.addmill(mill);
								} catch (java.text.ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							   	 
							    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy",Locale.getDefault());
						       // long milk=  ApplicationData.getmill();
						        //Date date = new Date(milk);
						       // String datetime = formatter.format(date).toString().trim();
						 
						        long today = System.currentTimeMillis();
						        SimpleDateFormat presentdate = new SimpleDateFormat("dd MMM yy",Locale.getDefault());
						        String present = presentdate.format(today).toString().trim();
						       // if(present .equals( datetime )) {
						        //	String todaydispaly = ("Today").toString().trim();
						        //	datetv.setText(todaydispaly);
						        	 	        	
						       // } else {
						       //
						        //	datetv.setText(datetime);
						       // }
						       // td.addView(datetv);
					   	  		 
					   		 
						        float a=0,b=0; 
					   	    final String schedules=map.get("schedules").toString().trim();
					   	 JSONArray jsonArray = new JSONArray(schedules);
					 	 for (int j = 0; j < jsonArray.length(); j++) {
					 		JSONObject jObject = jsonArray.getJSONObject(j);
			  				final String dispensed_feed=jObject.getString("dispensed_feed");
			  				final String schedule_times=jObject.getString("schedule_times");
			  				final String original_feed=jObject.getString("original_feed");
			  				
		      				 final LinearLayout tablerow1=new LinearLayout(getActivity());
		      				 tablerow1.setOrientation(LinearLayout.HORIZONTAL);
				              LinearLayout.LayoutParams paramas11 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				                      LinearLayout.LayoutParams.WRAP_CONTENT,1);
				              tablerow1.setPadding(5,0,5,0);
				              tablerow1.setLayoutParams(paramas11);
						        		  
					      		      try{
					      		    	final TextView fromtime1=new TextView(getActivity());
							       		 LinearLayout.LayoutParams paramas1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
							                     LinearLayout.LayoutParams.WRAP_CONTENT,1);
							       		fromtime1.setLayoutParams(paramas1);
							       		fromtime1.setPadding(5,0,5,0);
							       		String sp[]=schedule_times.split("-");
										 String sp1=sp[0];
										 String sp2=sp[1];
										 String tsp=sp1+" - "+sp2;
					      		      String schedule=(tsp).toString().trim();
					      		      fromtime1.setText(schedule);
					      		      fromtime1.setKeyListener(null);
					      		      fromtime1.setTextColor(Color.BLACK);
					      		      fromtime1.setTextSize(15);
									  fromtime1.setGravity(Gravity.START);
					      		      fromtime1.setFreezesText(true);
					      		      fromtime1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

					      		    	final TextView actuval_feed=new TextView(getActivity());
					      		    	 actuval_feed.setPadding(5,0,5,0);
					      		    	actuval_feed.setLayoutParams(paramas1);
										   float actuval_float=Float.parseFloat(original_feed);
										   String str1_original_feed=  String.format("%.2f", actuval_float);
										   actuval_feed.setText(str1_original_feed);
										   actuval_feed.setTextColor(Color.BLACK);
										   actuval_feed.setKeyListener(null);
										   actuval_feed.setTextSize(15);
										   //feedkg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
										   actuval_feed.setGravity(Gravity.CENTER);
										   actuval_feed.setFreezesText(true);
										   actuval_feed.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
					      		  				      			 
									   
									   final TextView disfeedkg=new TextView(getActivity());
									  
									   float dispensed_float=Float.parseFloat(dispensed_feed);
									   String str1_dispensed_feed=  String.format("%.2f", dispensed_float);
									   disfeedkg.setText(str1_dispensed_feed);
									   disfeedkg.setPadding(5,0,5,0);
									   disfeedkg.setLayoutParams(paramas1);
									   disfeedkg.setTextColor(Color.BLACK);
									   disfeedkg.setKeyListener(null);
									   disfeedkg.setTextSize(15);
									  
									   disfeedkg.setGravity(Gravity.CENTER);
									   disfeedkg.setFreezesText(true);
									   disfeedkg.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);	
									   
							       		tablerow1.addView(fromtime1);
							       		tablerow1.addView(actuval_feed);
							       		 
							       		tablerow1.addView(disfeedkg);
							       	
							       		td.addView(tablerow1);
							       		try{
							       	    	
								       	    float flfeed=Float.parseFloat(original_feed);
								       	          a+=flfeed;
								       	       //int feedint=Math.round(a);
								       	       //int feedint=Math.round(a);
								       	       String str1=  String.format("%.2f", a);
								       	   // String str= String.format("%.2f", s1);
								       	       // ApplicationData.addfeedtotal(str1);
								       	        
								       	    float dsfeed=Float.parseFloat(dispensed_feed);
								       	         b+=dsfeed;
								       	         //int dsfeedint=Math.round(b);
								       	        // String str2=Integer.toString(dsfeedint);
								       	    String str2=  String.format("%.2f", b);
								       	      //String s2=Float.toString(b);
								       	     //String str2= String.format("%.2f", s2);
								       	        // ApplicationData.adddisfeedtotal(str2);
								       	         
								       	       
								       	    }catch(Exception e){
								       	    	
								       	    }
							       		
							       	
					      		    }catch(Exception e){
					      		    	  e.printStackTrace();
					      		      }
					 	}
						try{
				       		   final LinearLayout tablerow3=new LinearLayout(getActivity());	
				       		 tablerow3.setOrientation(LinearLayout.HORIZONTAL);
				              LinearLayout.LayoutParams paramas31 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				                      LinearLayout.LayoutParams.WRAP_CONTENT,1);
				              tablerow3.setPadding(5,0,5,0);
				               tablerow3.setLayoutParams(paramas31);
				               
				              final TextView totalfeed=new TextView(getActivity());
				              LinearLayout.LayoutParams paramas1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					                     LinearLayout.LayoutParams.WRAP_CONTENT,1);
				               totalfeed.setLayoutParams(paramas1);
								//String str1=ApplicationData.getfeedtotal().toString().trim();
							 //	totalfeed.setText(str1);
							 	totalfeed.setPadding(5,0,5,0);
							 	totalfeed.setLayoutParams(paramas1);
							 	totalfeed.setTextColor(Color.parseColor("#368306"));
							 	totalfeed.setKeyListener(null);
							 	totalfeed.setTextSize(16);
							 	totalfeed.setGravity(Gravity.CENTER);
							 	totalfeed.setFreezesText(true);
							 	totalfeed.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
								
								final TextView dispensefeedtotal=new TextView(getActivity());
								dispensefeedtotal.setLayoutParams(paramas1);
								//String str11=ApplicationData.getdisfeedtotal().toString().trim();
							   // dispensefeedtotal.setText(str11);
							    dispensefeedtotal.setPadding(5,0,5,0);
							    dispensefeedtotal.setLayoutParams(paramas1);
							    dispensefeedtotal.setTextColor(Color.parseColor("#368306"));
							    dispensefeedtotal.setKeyListener(null);
							    dispensefeedtotal.setTextSize(16);
								dispensefeedtotal.setGravity(Gravity.CENTER);
							    dispensefeedtotal.setFreezesText(true);
							    dispensefeedtotal.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);	
															
								
								tablerow3.addView(datetv);
								tablerow3.addView(totalfeed);
								tablerow3.addView(dispensefeedtotal);
								td.addView(tablerow3);
				       		}catch(Exception e){
				       			e.printStackTrace();
				       		}
					 	
					    View v=new View(getActivity());
		                v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		                v.setBackgroundResource(R.drawable.seprator2);
		                td.addView(v);
					   	 }
					
					}catch(Exception e){
						e.printStackTrace();
					}
				}
	private void showDevices(String s) {
		try{
			String json_result = s.toString();
			JSONObject json = new JSONObject(json_result);
			String status = json.getString("status");
			String zero = "0".toString().trim();
			if (status.equals(zero)) {
				String error = json.getString("error");
				//Utility.showAlertDialog(getActivity(), "Login", error);
				//Utility.hideLoader();
			}else {
				String data=json.getString("data");
				JSONObject jObject2 = new JSONObject(data);
				//Get Feeder Details
				JSONArray feeder_details = jObject2.getJSONArray("schedule_data");
				mylist.clear();
				for (int i = 0; i < feeder_details.length(); i++) {
					try{
						HashMap<String, String> map = new HashMap<String, String>();
						JSONObject jObject = feeder_details.getJSONObject(i);
						map.put("schedule_date", jObject.getString("schedule_date"));
						map.put("schedule_count", jObject.getString("schedule_count"));
						//map.put("last_update_time", jObject.getString("last_update_time"));
						JSONArray schedules = jObject.getJSONArray("schedules");
						map.put("schedules", schedules.toString());
						mylist.add(map);
					}catch(Exception e){
						e.printStackTrace();
					}
				}

				if(mylist!=null){
					updated();
				}
			}

           }catch (Exception e){
		       e.printStackTrace();
		}
		}
	private void processResponse(JsonObject jsn) {
		try{
			String result=  jsn.toString();
			JSONObject jsnobj = new JSONObject(result);
			String status = jsnobj.getString("status");
			String zero = "0".toString().trim();
			if (status.equals(zero)) {
				String error=jsnobj.getString("error");
				td.setVerticalScrollBarEnabled(true);
				td.removeAllViewsInLayout();
				util.showAlertDialog(getActivity(),"Response",error,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

			}else{
				String data=jsnobj.getString("data");
				JSONArray json_ary=jsnobj.getJSONArray("data");
				mylist.clear();
				for(int i=0;i<json_ary.length();i++){
					JSONObject jObject = json_ary.getJSONObject(i);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("feederId",jObject.getString("feederId"));
					map.put("hex_id",jObject.getString("hex_id"));
					map.put("feederSno",jObject.getString("feederSno"));
					map.put("kg_feed_disp_time",jObject.getString("kg_feed_disp_time"));
					map.put("mode",jObject.getString("mode"));
					map.put("schedule_date",jObject.getString("schedule_date"));
					map.put("schedule_count",jObject.getString("schedule_count"));
					map.put("last_update_time",jObject.getString("last_update_time"));
					map.put("total_actual_feed",jObject.getString("total_actual_feed"));
					map.put("total_dispensed_feed",jObject.getString("total_dispensed_feed"));
					map.put("data_interval",jObject.getString("data_interval"));
					map.put("schedules",jObject.getJSONArray("schedules").toString());

					mylist.add(map);
				}
               if(mylist!=null){
				   Update();
			   }
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	private void Update(){
		try{
			td.setVerticalScrollBarEnabled(true);
			td.removeAllViewsInLayout();
			for (int i = 0; i < mylist.size(); i++) {
				final HashMap<String, String> map = mylist.get(i);
				final String feederSno = map.get("feederSno").toString().trim();
				final String feederId = map.get("feederId").toString().trim();
				final String hex_id = map.get("hex_id").toString().trim();
				final String schedule_date = map.get("schedule_date").toString().trim();
				final String feeder_mode = map.get("mode").toString().trim();
				final String schedules = map.get("schedules").toString().trim();
				final String total_actual_feed = map.get("total_actual_feed").toString().trim();
				final String total_dispensed_feed = map.get("total_dispensed_feed").toString().trim();
				final String last_update_time = map.get("last_update_time").toString().trim();

				final LinearLayout tablerow=new LinearLayout(getActivity());
				tablerow.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams paramas22 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT,1);
				tablerow.setPadding(5,0,5,0);
				tablerow.setLayoutParams(paramas22);
				final TextView feeder_tv=new TextView(getActivity());
				feeder_tv.setText(feederId);
				feeder_tv.setTextColor(Color.parseColor("#14648d"));
				feeder_tv.setKeyListener(null);
				feeder_tv.setTextSize(16);
				//feedkg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
				feeder_tv.setGravity(Gravity.START);
				feeder_tv.setFreezesText(true);
				feeder_tv.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

				final TextView tfdf=new TextView(getActivity());
				tfdf.setText(total_dispensed_feed+" / "+total_actual_feed);
				tfdf.setTextColor(Color.parseColor("#14648d"));
				tfdf.setKeyListener(null);
				tfdf.setTextSize(16);
				//feedkg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
				tfdf.setGravity(Gravity.CENTER);
				tfdf.setFreezesText(true);
				tfdf.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
				tfdf.setLayoutParams(paramas22);

				final TextView datetv=new TextView(getActivity());
				LinearLayout.LayoutParams paramas41 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT,1);
				datetv.setLayoutParams(paramas41);
				feeder_tv.setLayoutParams(paramas41);
				datetv.setTextColor(Color.parseColor("#14648d"));
				datetv.setKeyListener(null);
				datetv.setTextSize(16);

				//feedkg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
				datetv.setGravity(Gravity.CENTER);
				datetv.setFreezesText(true);
				datetv.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
				//datetv.setBackgroundResource(R.drawable.rounded_green);
				try {
					String sdate1 = map.get("schedule_date").toString().trim();
					datetv.setText(sdate1);
					SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
					Date dt;
					dt = form.parse(sdate1);
					long mill = dt.getTime();
					//	ApplicationData.addmill(mill);
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy",Locale.getDefault());
				// long milk=  ApplicationData.getmill();
				//Date date = new Date(milk);
				// String datetime = formatter.format(date).toString().trim();

				long today = System.currentTimeMillis();
				SimpleDateFormat presentdate = new SimpleDateFormat("dd MMM yy",Locale.getDefault());
				String present = presentdate.format(today).toString().trim();
				// if(present .equals( datetime )) {
				//	String todaydispaly = ("Today").toString().trim();
				//	datetv.setText(todaydispaly);

				// } else {
				//
				//	datetv.setText(datetime);
				// }
				// td.addView(datetv);
				tablerow.addView(feeder_tv);
				tablerow.addView(tfdf);
				tablerow.addView(datetv);
				td.addView(tablerow);
				float a=0,b=0;
				final JSONArray jsonArray = new JSONArray(schedules);
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject jObject = jsonArray.getJSONObject(j);
					final HashMap<String, String> map1 = new HashMap<String, String>();
					final String schedule_times = jObject.getString("schedule_times");
					final String original_feed = jObject.getString("original_feed");
					String[] separated = schedule_times.split("-");
					String from_time = separated[0];
					String to_time = separated[1];
					map1.put("schedule_id", jObject.getString("schedule_id"));
					map1.put("from_time", from_time);
					map1.put("to_time", to_time);
					map1.put("original_feed", original_feed);
					map1.put("schedule_times",schedule_times);
					map1.put("one_cycle_feed", jObject.getString("one_cycle_feed"));
					map1.put("feed_gap", jObject.getString("feed_gap"));
					map1.put("totalTime", jObject.getString("totalTime"));
					map1.put("kg_feed_disp_time", jObject.getString("kg_feed_disp_time"));
					map1.put("mode",feeder_mode);
					map1.put("status",  jObject.getString("status"));
					map1.put("dispensed_feed", jObject.getString("dispensed_feed"));
					map1.put("device_totalfeed", jObject.getString("device_totalfeed"));
					map1.put("device_ocf", jObject.getString("device_ocf"));
					map1.put("device_feedgap", jObject.getString("device_feedgap"));

					final LinearLayout tablerow1=new LinearLayout(getActivity());
					tablerow1.setOrientation(LinearLayout.HORIZONTAL);
					LinearLayout.LayoutParams paramas11 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT,1);
					tablerow1.setPadding(5,0,5,0);
					tablerow1.setLayoutParams(paramas11);

					try{
						final TextView fromtime1=new TextView(getActivity());
						LinearLayout.LayoutParams paramas1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT,1);
						fromtime1.setLayoutParams(paramas1);
						fromtime1.setPadding(5,0,5,0);
						String sp[]=schedule_times.split("-");
						String sp1=sp[0];
						String sp2=sp[1];
						String tsp=sp1+" - "+sp2;
						String schedule=(tsp).toString().trim();
						fromtime1.setText(schedule);
						fromtime1.setKeyListener(null);
						fromtime1.setTextColor(Color.BLACK);
						fromtime1.setTextSize(15);
						fromtime1.setGravity(Gravity.START);
						fromtime1.setFreezesText(true);
						fromtime1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

						final TextView actuval_feed=new TextView(getActivity());
						actuval_feed.setPadding(5,0,5,0);
						actuval_feed.setLayoutParams(paramas1);
						float actuval_float=Float.parseFloat(original_feed);
						String str1_original_feed=  String.format("%.2f", actuval_float);
						actuval_feed.setText(str1_original_feed);
						actuval_feed.setTextColor(Color.BLACK);
						actuval_feed.setKeyListener(null);
						actuval_feed.setTextSize(15);
						//feedkg.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_FLAG_DECIMAL);
						actuval_feed.setGravity(Gravity.CENTER);
						actuval_feed.setFreezesText(true);
						actuval_feed.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);


						final TextView disfeedkg=new TextView(getActivity());

						float dispensed_float=Float.parseFloat(jObject.getString("dispensed_feed"));
						String str1_dispensed_feed=  String.format("%.2f", dispensed_float);
						disfeedkg.setText(str1_dispensed_feed);
						disfeedkg.setPadding(5,0,5,0);
						disfeedkg.setLayoutParams(paramas1);
						disfeedkg.setTextColor(Color.BLACK);
						disfeedkg.setKeyListener(null);
						disfeedkg.setTextSize(15);

						disfeedkg.setGravity(Gravity.CENTER);
						disfeedkg.setFreezesText(true);
						disfeedkg.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

						tablerow1.addView(fromtime1);
						tablerow1.addView(actuval_feed);

						tablerow1.addView(disfeedkg);

						td.addView(tablerow1);
						try{

							float flfeed=Float.parseFloat(original_feed);
							a+=flfeed;
							//int feedint=Math.round(a);
							//int feedint=Math.round(a);
							String str1=  String.format("%.2f", a);
							// String str= String.format("%.2f", s1);
							// ApplicationData.addfeedtotal(str1);

							float dsfeed=Float.parseFloat(jObject.getString("dispensed_feed"));
							b+=dsfeed;
							//int dsfeedint=Math.round(b);
							// String str2=Integer.toString(dsfeedint);
							String str2=  String.format("%.2f", b);
							//String s2=Float.toString(b);
							//String str2= String.format("%.2f", s2);
							// ApplicationData.adddisfeedtotal(str2);


						}catch(Exception e){

						}


					}catch(Exception e){
						e.printStackTrace();
					}
				}
				try{
					final LinearLayout tablerow3=new LinearLayout(getActivity());
					tablerow3.setOrientation(LinearLayout.HORIZONTAL);
					LinearLayout.LayoutParams paramas31 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT,1);
					tablerow3.setPadding(5,0,5,0);
					tablerow3.setLayoutParams(paramas31);

					final TextView totalfeed=new TextView(getActivity());
					LinearLayout.LayoutParams paramas1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT,1);
					totalfeed.setLayoutParams(paramas1);
					//String str1=ApplicationData.getfeedtotal().toString().trim();
					//	totalfeed.setText(str1);
					totalfeed.setPadding(5,0,5,0);
					totalfeed.setLayoutParams(paramas1);
					totalfeed.setTextColor(Color.parseColor("#368306"));
					totalfeed.setKeyListener(null);
					totalfeed.setTextSize(16);
					totalfeed.setGravity(Gravity.CENTER);
					totalfeed.setFreezesText(true);
					totalfeed.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

					final TextView dispensefeedtotal=new TextView(getActivity());
					dispensefeedtotal.setLayoutParams(paramas1);
					//String str11=ApplicationData.getdisfeedtotal().toString().trim();
					// dispensefeedtotal.setText(str11);
					dispensefeedtotal.setPadding(5,0,5,0);
					dispensefeedtotal.setLayoutParams(paramas1);
					dispensefeedtotal.setTextColor(Color.parseColor("#368306"));
					dispensefeedtotal.setKeyListener(null);
					dispensefeedtotal.setTextSize(16);
					dispensefeedtotal.setGravity(Gravity.CENTER);
					dispensefeedtotal.setFreezesText(true);
					dispensefeedtotal.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);


					tablerow3.addView(datetv);
					tablerow3.addView(totalfeed);
					tablerow3.addView(dispensefeedtotal);
					td.addView(tablerow3);
				}catch(Exception e){
					e.printStackTrace();
				}

				View v=new View(getActivity());
				v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				v.setBackgroundResource(R.drawable.seprator2);
				td.addView(v);
				}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
