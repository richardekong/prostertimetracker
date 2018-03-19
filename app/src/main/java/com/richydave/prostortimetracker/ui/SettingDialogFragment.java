package com.richydave.prostortimetracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.mukesh.countrypicker.fragments.CountryPicker;
import com.richydave.prostortimetracker.R;
import com.richydave.prostortimetracker.api.SettingListener;


public class SettingDialogFragment extends DialogFragment {
    //instance variable declaration
    private final String COUNTRY_PICKER_TITLE = "Select a Country";
    private final String SIMPLE_PALETTE_TAG = "SIMPLE PALETTE";
    private SettingListener settingListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder settingsDialog = new AlertDialog.Builder(getActivity());
        String[] items = {getString(R.string.setting_change_color), getString(R.string.setting_change_price),
                getString(R.string.setting_change_currency)
        };
        //set title
        settingsDialog.setTitle(R.string.setting_title)
                .setItems(items, (dialog, id) -> {
                    if (id == 0) {
                        //call the change color dialog
                        SimplePaletteDialogFragment paletteDialogFragment =
                                new SimplePaletteDialogFragment();
                        paletteDialogFragment.show(getFragmentManager(),SIMPLE_PALETTE_TAG);
                    } else if (id == 1) {
                        //call the change price dialog
                        PriceDialogFragment priceDialogFragment = new PriceDialogFragment();
                        priceDialogFragment.show(getFragmentManager(), getString(R.string.setting_change_price));
                    } else if (id == 2) {
                        //call the change currency dialog
                        CountryPicker countryPicker = CountryPicker.newInstance(COUNTRY_PICKER_TITLE);
                        settingListener.onSettingClick(countryPicker);
                    }
                });
        //create AlertDialog builder instance
        return settingsDialog.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            settingListener = (SettingListener) getActivity();
        } catch (ClassCastException e) {
            Log.e("ClassCastException", e.getMessage());
        }
    }
}
