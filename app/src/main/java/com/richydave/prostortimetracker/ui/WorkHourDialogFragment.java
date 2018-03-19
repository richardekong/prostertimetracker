package com.richydave.prostortimetracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.richydave.prostortimetracker.R;
import com.richydave.prostortimetracker.api.NumberPickListener;


public class WorkHourDialogFragment extends DialogFragment {
    //instance variable declaration
    private NumberPickListener onNumberPick;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //local instance for hour and minute pickers
        final int MAX_HOUR = 48;
        final int MAX_MINUTE = 59;
        //use a builder class to construct a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //create a layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //create View
        // Inflate and Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.work_hour_dialog, null);
        //reference the number pickers from the view
        NumberPicker hourPicker = view.findViewById(R.id.hour_number_picker);
        NumberPicker minutePicker = view.findViewById(R.id.minutes_number_picker);
        hourPicker.setMaxValue(MAX_HOUR);
        minutePicker.setMaxValue(MAX_MINUTE);

        //set the layout for the dialog
        builder.setView(view);
        builder.setPositiveButton(R.string.ok_button, (dialog, id) -> {

            double hour = hourPicker.getValue();
            double minute = minutePicker.getValue();
            double TotalHours = hour + (minute / 60);
            onNumberPick.setOnNumberPickedListener(TotalHours);

        })
                .setNegativeButton(R.string.cancel_button, (dialog, id) -> dialog.dismiss());
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onNumberPick = ((NumberPickListener) getActivity());
        } catch (ClassCastException e) {
            Log.e("onAttach:", e.getMessage());
        }
    }
}
