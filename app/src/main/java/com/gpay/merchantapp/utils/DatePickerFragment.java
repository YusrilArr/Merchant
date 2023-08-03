package com.gpay.merchantapp.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.gpay.merchantapp.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

    int _YEAR = 0;
    int _MOUNTH = 0;
    int _DAY = 0;

    int _YEARMIN = 0;
    int _MONTHMIN = 0;
    int _DAYMIN = 0;

    Calendar c, cMin;
    int year = 0, month = 0, day = 0;
    int year_min = 0, month_min = 0, day_min = 0;
    onDateInterface listener;

    @SuppressLint("ValidFragment")
    public DatePickerFragment() {

    }

    @SuppressLint("ValidFragment")
    public DatePickerFragment(int year, int mounth, int day) {

        this._YEAR = year;
        this._MOUNTH = mounth;
        this._DAY = day;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*DatePickerDialog dialog = null;
        dialog = new DatePickerDialog(getActivity(), this, _YEAR, _MOUNTH, _DAY);
        dialog.getDatePicker().setMaxDate((long) (Calendar.getInstance().getTimeInMillis() - (1000 * 60 * 60 * 24 * 365.25 * 17)));
        return dialog;*/

        if (getArguments() != null) {
            year = getArguments().getInt("year");
            month = getArguments().getInt("month");
            day = getArguments().getInt("day");
            c = Calendar.getInstance();
            c.set(year, month, day);
        } else {
            c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            Log.d("else", "else");

            Date today = new Date();
            cMin = Calendar.getInstance();
            cMin.setTime(today);
            cMin.add( Calendar.MONTH, -3 );
            cMin.add(Calendar.DAY_OF_MONTH, 2);
        }

        DatePickerDialog picker = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                this, year, month, day);
        picker.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_datepicker_corners));
       /* picker.getDatePicker().setMaxDate(c.getTime().getTime());
        picker.getDatePicker().setMinDate(cMin.getTime().getTime());*/

        Log.d("picker timestamp", c.getTime().getTime() + "");
        return picker;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String format_ddMMyyyyy = sdf.format(c.getTime());

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String format_yyyyMMdd = sdf2.format(c.getTime());

        listener.onDateListener(format_ddMMyyyyy, format_yyyyMMdd);
    }

    public void setListener(onDateInterface listener) {
        this.listener = listener;
    }

    public interface onDateInterface {
        public void onDateListener(String format, String format2);
    }

}
