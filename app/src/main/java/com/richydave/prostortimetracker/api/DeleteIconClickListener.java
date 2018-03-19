package com.richydave.prostortimetracker.api;

import com.richydave.prostortimetracker.data.DataBaseHelper;
import com.richydave.prostortimetracker.model.Wage;
import com.richydave.prostortimetracker.ui.WageDetailAdapter;

import java.util.List;

/**
 * Created by Adminstrator on 3/19/2018.
 */

public interface DeleteIconClickListener {
    void setOnDeleteIconClick(int position, List<Wage> wageDetails, WageDetailAdapter adapter,
                              DataBaseHelper dataBaseHelper);
}
