package eruvaka.pmgp.fragments;

 
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerStartFragment extends DialogFragment {
	 OnDateSetListener ondateSet;

	 public DatePickerStartFragment() {
	 }

	 public void setCallBack(OnDateSetListener ondate) {
	  ondateSet = ondate;
	 }

	 private int mStartYear, mStartMonth, mStartDay;

	 @Override
	 public void setArguments(Bundle args) {
	  super.setArguments(args);
	  mStartYear = args.getInt("year");
	  mStartMonth = args.getInt("month");
	  mStartDay = args.getInt("day");
	 }

	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
		  
	  return new DatePickerDialog(getActivity(), ondateSet, mStartYear, mStartMonth, mStartDay);
	   
	 }

	public DatePicker getDatePicker() {
		// TODO Auto-generated method stub
		 
		return null;
	}

	 
	}  
