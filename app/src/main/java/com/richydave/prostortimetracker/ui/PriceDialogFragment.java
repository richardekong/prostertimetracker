package com.richydave.prostortimetracker.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.richydave.prostortimetracker.data.DataBaseHelper;
import com.richydave.prostortimetracker.R;
import com.richydave.prostortimetracker.api.PriceChangeListener;
import com.richydave.prostortimetracker.api.PriceDialogClickListener;

/**
 * Created by Adminstrator on 3/15/2018.
 */

public class PriceDialogFragment extends DialogFragment {
    //instance variable
    private PriceDialogClickListener priceDialogClickListener;
    private PriceChangeListener priceChangeListener;
    private final String COUNTRY_PICKER_TAG = "COUNTRY_PICKER";

    public Dialog onCreateDialog(Bundle saveInstanceState) {

        View priceInputView = getActivity().getLayoutInflater()
                .inflate(R.layout.price_input_dialog, null);
        AlertDialog.Builder priceDialog = new AlertDialog.Builder(getActivity());
        priceDialog.setTitle(R.string.price_dialog_title)
                .setMessage(R.string.price_dialog_message)
                .setView(priceInputView);
        EditText priceInput = priceInputView.findViewById(R.id.price_input);
        priceDialog.setPositiveButton(R.string.ok_button, (dialog, id) ->{
            double price = Double.parseDouble(priceInput.getText().toString());
            if (isPriceZero()){
                //use the priceDialogClickListener
                priceDialogClickListener.setOnPriceDialogClick(price);
            } else {
                //use the PriceChangeListener since price has been initially set
                priceChangeListener.setOnPriceChange(price);
            }
        })
                .setNegativeButton(R.string.cancel_button, (dialog, id) -> dialog.dismiss());
        return priceDialog.create();
    }

    public boolean isPriceZero(){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        //determine if price has been set initially
        return (dataBaseHelper.getService().getPrice() == 0.0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            priceDialogClickListener = ((PriceDialogClickListener) getActivity());
            priceChangeListener = (PriceChangeListener)getActivity();
        } catch (ClassCastException e) {
            Log.e("onAttach:", e.getMessage());
        }
    }
}
