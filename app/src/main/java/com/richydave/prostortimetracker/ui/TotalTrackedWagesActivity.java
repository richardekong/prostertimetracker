package com.richydave.prostortimetracker.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mukesh.countrypicker.fragments.CountryPicker;
import com.richydave.prostortimetracker.api.ColoredBoxClickListener;
import com.richydave.prostortimetracker.model.Service;
import com.richydave.prostortimetracker.model.Wage;
import com.richydave.prostortimetracker.data.DataBaseHelper;
import com.richydave.prostortimetracker.R;
import com.richydave.prostortimetracker.api.NumberPickListener;
import com.richydave.prostortimetracker.api.PriceChangeListener;
import com.richydave.prostortimetracker.api.PriceDialogClickListener;
import com.richydave.prostortimetracker.api.ResetListener;
import com.richydave.prostortimetracker.api.SettingListener;

import java.util.Locale;

public class TotalTrackedWagesActivity extends AppCompatActivity implements NumberPickListener, PriceDialogClickListener,
        SettingListener, PriceChangeListener, ResetListener, ColoredBoxClickListener {

    //instance variable declaration
    private final String DIALOG_FRAGMENT_TAG = "WORK HOUR DIALOG FRAGMENT";
    private final String PRICE_DIALOG_FRAGMENT_TAG = "PRICE DIALOG FRAGMENT";
    public static Wage wage;
    private static Service serviceDetails;
    private final String COUNTRY_PICKER_TAG = "COUNTRY_PICKER";
    private TextView trackedWage;
    private TextView detailsLink;
    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private DataBaseHelper dataBaseHelper;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_tracked_wages);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        trackedWage = findViewById(R.id.total_tracked_Wage);
        detailsLink = findViewById(R.id.wages_Details);
        linearLayout = findViewById(R.id.total_tracked_Wage_layout);
        init();
        detailsLink.setOnClickListener((view) -> openDetailsActivity());
        fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            //determine if the database contains details
            if (isServiceRecordEmpty() && serviceDetails.getCurrencyCode() == null) {
                //display the price input dialog and obtain the price from the price input dialog
                PriceDialogFragment priceDialogFragment = new PriceDialogFragment();
                priceDialogFragment.show(getFragmentManager(), PRICE_DIALOG_FRAGMENT_TAG);
            } else {
                //create and display a fragment dialog
                WorkHourDialogFragment dialogFragment = new WorkHourDialogFragment();
                dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_total_tracked_wages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final String SETTING_DIALOG = "SETTING DIALOG";
            SettingDialogFragment dialogFragment = new SettingDialogFragment();
            dialogFragment.show(getFragmentManager(), SETTING_DIALOG);
            return true;
        }
        if (id == R.id.action_reset) {
            final String RESET_DIALOG = "RESET DIALOG";
            ResetDialogFragment resetDialogFragment = new ResetDialogFragment();
            resetDialogFragment.show(getFragmentManager(), RESET_DIALOG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        dataBaseHelper = new DataBaseHelper(this);
        //instantiate the wage and service objects
        wage = new Wage();
        serviceDetails = new Service();
        String countryCode = "";
        double totalWage = 0.0;
        double price = 0.0;
        //determine the condition of all database resource
        if (!(isServiceRecordEmpty() && isWageRecordEmpty())) {
            //extract the total wage from the database
            totalWage = dataBaseHelper.getTotalWage();
            //extract the currency code from the database
            Cursor cursor = dataBaseHelper.getServiceDetail();
            if (cursor.moveToFirst()) {
                do {
                    price = cursor.getDouble(0);
                    countryCode = cursor.getString(1);
                } while (cursor.moveToNext());
            }
            //set service detail properties
            serviceDetails.setPrice(price);
            serviceDetails.setCurrencyCode(countryCode);
            trackedWage.setText(String.format(Locale.US, "%.2f %s", totalWage,
                    countryCode));
            //make the details link visible
            if (totalWage > 0.0) {
                detailsLink.setText(R.string.Details_label);
            } else {
                detailsLink.setText("");
            }
        } else {
            trackedWage.setText(String.format(Locale.US, "%.2f %s", totalWage,
                    countryCode));
        }
        //load and set background color
        if (!(dataBaseHelper.isColorRecordEmpty())) {
            String hex = dataBaseHelper.getColor();
            hex = String.format(Locale.US, "#%s", hex);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(hex));
            linearLayout.setBackground(colorDrawable);
            toolbar.setBackground(colorDrawable);
        }
    }

    private boolean isWageRecordEmpty() {

        Cursor cursor = dataBaseHelper.getAllWageDetails();
        return (cursor.getCount() < 1);
    }

    private boolean isServiceRecordEmpty() {
        //create a cursor object
        try (Cursor cursor = dataBaseHelper.getServiceDetail()) {
            return (cursor.getCount() < 1);
        }
    }

    public void saveWageDetails(Wage wageToSave) {
        //insert wage into the database
        dataBaseHelper.addWageDetails(wageToSave);
    }

    public void saveServiceDetails(Service detailsToSave) {
        //insert service details into the database
        dataBaseHelper.addServiceDetails(detailsToSave);
    }

    @Override
    public void setOnNumberPickedListener(double hours) {

        double computedWage = serviceDetails.getPrice() * hours;
        Log.d("hours =", String.valueOf(hours));
        Log.d("serviceDetails =", String.valueOf(serviceDetails.getPrice()));
        //set the computed wage and hours
        wage.setWage(computedWage);
        wage.setHours(hours);
        //save wage details to the database
        saveWageDetails(wage);
        //obtain the total computed wage from the database
        double totalWages = dataBaseHelper.getTotalWage();
        trackedWage.setText(String.format(Locale.US, "%.2f %s", totalWages, serviceDetails.getCurrencyCode()));
        //make the details link visible and set a click listener
        detailsLink.setText(R.string.Details_label);
    }

    public void openDetailsActivity() {
        //set on click listener for details text view
        detailsLink.setOnClickListener((view) -> {
            Intent toDetailsActivity =
                    new Intent(TotalTrackedWagesActivity.this,
                            WageDetailsActivity.class);

            //start the intended activity
            startActivity(toDetailsActivity);
        });
    }

    @Override
    public void setOnPriceDialogClick(double price) {
        //set the price of the wage object
        serviceDetails.setPrice(price);
        //create the country picker
        final CountryPicker countryPicker = CountryPicker.newInstance("Choose a Country");
        countryPicker.show(getSupportFragmentManager(), COUNTRY_PICKER_TAG);
        countryPicker.setListener((name, code, dialCode, flagDrawableResID) -> {
            String countryCode = CountryPicker.getCurrencyCode(code).toString();
            //set the country code
            serviceDetails.setCurrencyCode(countryCode);
            wage.setWage(serviceDetails.getPrice() * wage.getHours());
            trackedWage.setText(String.format(Locale.US, "%.2f %s", wage.getWage(), serviceDetails.getCurrencyCode()));
            Log.d("Selected Country code", serviceDetails.getCurrencyCode());
            //insert service details in the database
            saveServiceDetails(serviceDetails);
            //close the country picker
            countryPicker.dismiss();
        });
    }

    @Override
    public void onSettingClick(CountryPicker countryPicker) {
        countryPicker.show(getSupportFragmentManager(), COUNTRY_PICKER_TAG);
        countryPicker.setListener((name, code, dialCode, flagDrawableResId) -> {
            String countryCode = CountryPicker.getCurrencyCode(code).toString();
            //get service from the database
            Service service = dataBaseHelper.getService();
            //update the currency code
            dataBaseHelper.updateCountryCode(service, countryCode);
            //reflect changes
            init();
            countryPicker.dismiss();
        });
    }

    @Override
    public void setOnPriceChange(double price) {
        //update the price
        Log.d("new Price", String.valueOf(price));
        Service service = dataBaseHelper.getService();
        dataBaseHelper.updateServicePrice(service, price);
        //reflect changes
        init();
    }

    @Override
    public void setOnClickColoredBox(ColorDrawable color) {
        linearLayout.setBackground(color);
        toolbar.setBackground(color);
        //save or overwrite color information in the database
        String hex = Integer.toHexString(color.getColor());
        if (dataBaseHelper.isColorRecordEmpty()) {
            dataBaseHelper.addColor(hex);
        } else {
            dataBaseHelper.updateColor(hex);
        }
        if (fab.getBackground() == color) {
            fab.setBackgroundTintList(ColorStateList.
                    valueOf(getResources().getColor(R.color.add_fab_color2))); /*fix*/
        }
    }

    @Override
    public void setOnReset() {
        //reflect changes
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBaseHelper.close();
    }
}