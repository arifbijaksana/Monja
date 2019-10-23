package com.haerul.monja.utils;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BindingAdapterUtil {

    public final static Calendar myCalendar = Calendar.getInstance();

    @BindingAdapter(value = {"inputDate", "errorMessage"}, requireAll = false)
    public static void setInputDate(TextView editText, String dated, boolean isErorMessage) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (isErorMessage) {
                    setDate2(editText);
                } else {
                    setDate(editText);
                }
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private static void setDate(TextView et) {
        String myFormat = Constants.DATE_ONLY_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String date = sdf.format(myCalendar.getTime());
        et.setText(date.toUpperCase());
    }

    private static void setDate2(TextView et) {
        String myFormat = Constants.DATE_ONLY_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        String date = sdf.format(myCalendar.getTime());
        et.setText(date.toUpperCase());
        et.setError(null);
    }
    
}
