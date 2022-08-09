package com.volumetree.newswasthyaingitopd.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.volumetree.newswasthyaingitopd.model.responseData.comman.DashboardConsultationsReportData;

import java.util.ArrayList;

public class ClaimsXAxisValueFormatter extends ValueFormatter {

    ArrayList<DashboardConsultationsReportData> datesList;

    public ClaimsXAxisValueFormatter(ArrayList<DashboardConsultationsReportData> arrayOfDates) {
        this.datesList = arrayOfDates;
    }


    @Override
    public String getAxisLabel(float value, AxisBase axis) {

        int position = Math.round(value);

        if (position>-1 && position < datesList.size())
            return datesList.get(position).getLabel();
        return "";
    }
}
