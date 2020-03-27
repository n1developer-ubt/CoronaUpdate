package com.n1developer.covid19counter;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class AllData {
    private ArrayList<CountryData> CountryData = new ArrayList<>();
    private String TotalCases;
    private String NewCases;
    private String TotalDeaths;
    private String NewDeaths;
    private String TotalRecovered;
    private String ActiveCases;
    private String Serious;
    private String TotCases;

    public ArrayList<com.n1developer.covid19counter.CountryData> getCountryData() {
        return CountryData;
    }

    public void addCountryData(CountryData data)
    {
        CountryData.add(data);
    }
    public void setCountryData(ArrayList<com.n1developer.covid19counter.CountryData> countryData) {
        CountryData = countryData;
    }

    public String getTotalCases() {
        return TotalCases;
    }

    public void setTotalCases(String totalCases) {
        TotalCases = totalCases;
    }

    public String getNewCases() {
        return NewCases;
    }

    public void setNewCases(String newCases) {
        NewCases = newCases;
    }

    public String getTotalDeaths() {
        return TotalDeaths;
    }

    public void setTotalDeaths(String totalDeaths) {
        TotalDeaths = totalDeaths;
    }

    public String getNewDeaths() {
        return NewDeaths;
    }

    public void setNewDeaths(String newDeaths) {
        NewDeaths = newDeaths;
    }

    public String getTotalRecovered() {
        return TotalRecovered;
    }

    public void setTotalRecovered(String totalRecovered) {
        TotalRecovered = totalRecovered;
    }

    public String getActiveCases() {
        return ActiveCases;
    }

    public void setActiveCases(String activeCases) {
        ActiveCases = activeCases;
    }

    public String getSerious() {
        return Serious;
    }

    public void setSerious(String serious) {
        Serious = serious;
    }

    public String getTotCases() {
        return TotCases;
    }

    public void setTotCases(String totCases) {
        TotCases = totCases;
    }

    public void loadFromJson(JSONObject obj) throws Exception
    {
        JSONArray countryData = obj.getJSONArray("CountryData");

        for(int x =0; x < countryData.length(); x++)
        {
            CountryData newData = new CountryData();
            newData.loadFromJson((JSONObject)countryData.get(x));
            addCountryData(newData);
        }

        TotalCases = obj.getString("TotalCases");
        NewCases = obj.getString("NewCases");
        TotalDeaths = obj.getString("TotalDeaths");
        NewDeaths = obj.getString("NewDeaths");
        TotalRecovered = obj.getString("TotalRecovered");
        ActiveCases = obj.getString("ActiveCases");
        Serious = obj.getString("Serious");
        TotCases = obj.getString("TotCases");
    }
}
