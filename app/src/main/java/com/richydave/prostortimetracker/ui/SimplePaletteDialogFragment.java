package com.richydave.prostortimetracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.richydave.prostortimetracker.R;
import com.richydave.prostortimetracker.api.ColoredBoxClickListener;


public class SimplePaletteDialogFragment extends DialogFragment {
    //constant
    private final String PALETTE_TAG = "SIMPLE COLOR PALETTE TAG";
    //instance variable
    private String paletteTag;
    private ColoredBoxClickListener coloredBoxClickListener;

    //onCreate method
    public Dialog onCreateDialog(Bundle saveInstancestate) {
        //instantiate an AlertDialog builder
        AlertDialog.Builder paletteDialog = new AlertDialog.Builder(getActivity());
        //create layout inflater
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        // set up the palette UI
        View view = layoutInflater.inflate(R.layout.simple_palette, null);
        paletteDialog.setView(view);
        View pinkBox = view.findViewById(R.id.pink_box);
        View yellowBox = view.findViewById(R.id.yellow_box);
        View blueBox = view.findViewById(R.id.blue_box);
        View darkbox = view.findViewById(R.id.dark_box);
        //set click listener for view
        View[] views = {pinkBox, yellowBox, blueBox, darkbox};
        for (View coloredBox : views) {
            coloredBox.setOnClickListener((clickedBox) -> {
                //the color of the selected view
                ColorDrawable drawable =(ColorDrawable) coloredBox.getBackground();
                //set the ClickColoredBoxListener
                coloredBoxClickListener.setOnClickColoredBox(drawable);
                //dismiss
                dismiss();
            });
        }

        return paletteDialog.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            coloredBoxClickListener = (ColoredBoxClickListener) getActivity();
        } catch (ClassCastException e) {
            Log.e("ClassCastException", e.getMessage());
        }
    }

    @Override
    public void onResume() {
        int width = getResources().getDimensionPixelSize(R.dimen.color_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.color_dialog_height);
        getDialog().getWindow().setLayout(width,height);
        super.onResume();

    }
}

