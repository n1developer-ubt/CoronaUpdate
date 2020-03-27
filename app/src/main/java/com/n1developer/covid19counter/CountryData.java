package com.n1developer.covid19counter;

import org.json.JSONObject;

public class CountryData {
    private String Name;
    private int TotalCases;
    private int NewCases;
    private int TotalDeaths;
    private int NewDeaths;
    private int TotalRecovered;
    private int ActiveCases;
    private int Serious;
    private double TotCases;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getTotalCases() {
        return TotalCases;
    }

    public void setTotalCases(int totalCases) {
        TotalCases = totalCases;
    }

    public int getNewCases() {
        return NewCases;
    }

    public void setNewCases(int newCases) {
        NewCases = newCases;
    }

    public int getTotalDeaths() {
        return TotalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        TotalDeaths = totalDeaths;
    }

    public int getNewDeaths() {
        return NewDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        NewDeaths = newDeaths;
    }

    public int getTotalRecovered() {
        return TotalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        TotalRecovered = totalRecovered;
    }

    public int getActiveCases() {
        return ActiveCases;
    }

    public void setActiveCases(int activeCases) {
        ActiveCases = activeCases;
    }

    public int getSerious() {
        return Serious;
    }

    public void setSerious(int serious) {
        Serious = serious;
    }

    public double getTotCases() {
        return TotCases;
    }

    public void setTotCases(double totCases) {
        TotCases = totCases;
    }

    public void loadFromJson(JSONObject obj) throws Exception
    {
        Name = obj.getString("Name");
        TotalCases = Integer.parseInt(obj.getString("TotalCases"));
        NewCases = Integer.parseInt(obj.getString("NewCases"));
        TotalDeaths = Integer.parseInt(obj.getString("TotalDeaths"));
        NewDeaths = Integer.parseInt(obj.getString("NewDeaths"));
        TotalRecovered = Integer.parseInt(obj.getString("TotalRecovered"));
        ActiveCases = Integer.parseInt(obj.getString("ActiveCases"));
        Serious = Integer.parseInt(obj.getString("Serious"));
        TotCases = Float.parseFloat(obj.getString("TotCases"));
    }

}
