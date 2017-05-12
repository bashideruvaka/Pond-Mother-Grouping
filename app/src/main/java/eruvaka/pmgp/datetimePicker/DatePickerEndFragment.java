package eruvaka.pmgp.datetimePicker;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerEndFragment extends DialogFragment {
	 OnDateSetListener ondateSet1;

	 public DatePickerEndFragment() {
	 }

	 public void setCallBack(OnDateSetListener ondate) {
	  ondateSet1 = ondate;
	 }

	 private int mEndYear, mEndMonth, mEndDay;
	 @Override
	 public void setArguments(Bundle args) {
	  super.setArguments(args);
	  mEndYear = args.getInt("year");
	  mEndMonth = args.getInt("month");
	  mEndDay = args.getInt("day");
	 }
	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
	  return new DatePickerDialog(getActivity(), ondateSet1, mEndYear, mEndMonth, mEndDay);
	 }
}
