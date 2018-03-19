package com.richydave.prostortimetracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.richydave.prostortimetracker.data.DataBaseHelper;
import com.richydave.prostortimetracker.R;
import com.richydave.prostortimetracker.api.ResetListener;


/**
 * Created by Adminstrator on 3/17/2018.
 */

public class ResetDialogFragment extends DialogFragment {
    private final String TITLE = "Reset";
    private ResetListener resetListener;

    //onCreateDialog method
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        //create a an instance of AlertDialog Builder
        AlertDialog.Builder resetDialog = new AlertDialog.Builder(getActivity());
        //customize the AlertDialog instance
        resetDialog.setTitle(TITLE)
                .setMessage(R.string.reset_message)
                .setPositiveButton(R.string.ok_button, (dialog, id) -> {
                    //reset the wage delete record in the database
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
                    dataBaseHelper.resetWageDetails();
                    //reflect changes
                    resetListener.setOnReset();
                })
                .setNegativeButton(R.string.cancel_button, (dialog, id) -> dialog.dismiss());
        return resetDialog.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            resetListener = (ResetListener) getActivity();
        } catch (ClassCastException e) {
            Log.e("ClassCastException", e.getMessage());
        }
    }
}
