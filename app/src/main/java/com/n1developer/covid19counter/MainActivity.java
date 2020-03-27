package com.n1developer.covid19counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Switch swtChangeTheme;
    ImageButton btnCountry;
    TextView tvCountryCases;
    TextView tvCountryDeaths;
    TextView tvCountryRecovered;
    TextView tvNewCases;
    TextView tvAllCases;
    TextView tvAllDeaths;
    TextView tvNewDeaths;
    TextView tvNewRecovered;
    TextView tvAllRecovered;
    TextView tvCountryName;
    EditText edSearch;
    Spinner spSort;
    ListView lvCountriesData;
    TextView tvMain;
    ConstraintLayout mainLayout;
    TextView tvCasesLbl ;
    TextView tvRecoveredLbl ;
    TextView tvDeathsLbl ;
    TextView tvOtherCountriesLbl;
    LinearLayout lytCountries;
    LinearLayout lytFilter;
    LinearLayout lytActions;
    TextView tvSelectedCountryRecoveredLbl;
    TextView tvSelectedCountryCasesLbl;
    TextView tvSelectedCountryDeathsLbl;
    TextView tvSelectedCountryName;
    ImageView ivSelectedCountryFlag;
    boolean Dark = false;
    private String selectCountryName;

    AllData currentData = null;
    Random r = new Random();
    String[] flags;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void loadAdd()
    {
//        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
//                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//                    @Override
//                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                        // Show the ad.
//                    }
//                })
//                .withAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(int errorCode) {
//                        // Handle the failure by logging, altering the UI, and so on.
//                    }
//                })
//                .withNativeAdOptions(new NativeAdOptions.Builder()
//                        // Methods in the NativeAdOptions.Builder class can be
//                        // used here to specify individual options settings.
//                        .build())
//                .build();
//
//        adLoader.loadAd(new AdRequest.Builder().build());

        AdView a = findViewById(R.id.adView2);
        a.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitViews();//Initialize all views
        InitListeners();//ini
        addData();

        loadAdd();
        try {
            flags = getAssets().list("flags");
        }
        catch (Exception x)
        {
            flags = null;
        }

        setSelectedCountry();

        try {
            Dark = TemporaryData.readFromFile(getApplicationContext(),"dark.txt").trim().equals("0")?false:true;
            swtChangeTheme.setChecked(Dark);
        }
        catch (Exception ex)
        {
            setDark();
        }
    }

    private void setSelectedCountry()
    {
        Bundle extras = getIntent().getExtras();
        selectCountryName = extras.getString("name");
        Drawable flg = getFlag(selectCountryName);

        tvSelectedCountryName.setText(SelectCountry.capitalize(selectCountryName));

        if(flg != null)
        {
            btnCountry.setImageDrawable(flg);
            ivSelectedCountryFlag.setImageDrawable(getFlag(selectCountryName));
        }
    }

    int getIntColor(int id)
    {
        return ContextCompat.getColor(getApplicationContext(), id);
    }

    private void SetHtmlText(TextView tv, String txt, String sub, int colorId)
    {
        tv.setText(Html.fromHtml(String.format("<small>%s<small><sup>%s</sup></small></small>",txt,sub)));

        if(colorId == -1)
            return;
        tv.setTextColor(getIntColor(colorId));
    }

    private void InitViews()
    {
        tvAllCases = findViewById(R.id.tvTotalCases);
        tvAllDeaths = findViewById(R.id.tvTotalDeaths);
        tvAllRecovered = findViewById(R.id.tvTotalRecovered);
        tvNewDeaths= findViewById(R.id.tvTotalNewDeathsNew);
        tvNewRecovered=  findViewById(R.id.tvTotalNewRecoveredNew);
        tvNewCases = findViewById(R.id.tvTotalNewCasesNew);
        tvCountryName= findViewById(R.id.tvCountryName);
        tvCountryCases= findViewById(R.id.tvCountryCases);
        tvCountryRecovered= findViewById(R.id.tvCountryRecovered);
        tvCountryDeaths= findViewById(R.id.tvCountryDeaths);
        btnCountry = findViewById(R.id.btnCountry);
        swtChangeTheme = findViewById(R.id.swtChangeTheme);
        lvCountriesData = findViewById(R.id.lvCountriesData);
        edSearch = findViewById(R.id.edSearch);
        swtChangeTheme = findViewById(R.id.swtChangeTheme);
        spSort = findViewById(R.id.spSort);
        tvMain = findViewById(R.id.tvMain);
        mainLayout = findViewById(R.id.main_activity);
        tvDeathsLbl = findViewById(R.id.tvDeathsLbl);
        tvRecoveredLbl = findViewById(R.id.tvRecoveredLbl);
        tvCasesLbl= findViewById(R.id.tvCasesLbl);
        tvOtherCountriesLbl= findViewById(R.id.tvOtherCountriesLbl);
        lytCountries = findViewById(R.id.lytCountries);
        lytFilter = findViewById(R.id.lytFilter);
        lytActions = findViewById(R.id.lytActions);

        tvSelectedCountryCasesLbl = findViewById(R.id.tvSelectedCountryCasesLbl);
        tvSelectedCountryDeathsLbl = findViewById(R.id.tvSelectedCountryDeathsLbl);
        tvSelectedCountryRecoveredLbl = findViewById(R.id.tvSelectedCountryRecoveredLbl);

        tvSelectedCountryName = findViewById(R.id.tvSelectedCountryName);

        ivSelectedCountryFlag = findViewById(R.id.ivSelectedCountryFlag);

        lvCountriesData.setDivider(null);
        lvCountriesData.setDividerHeight(30);
    }
    final int sdk = android.os.Build.VERSION.SDK_INT;
    private void setDark()
    {
        int ForeColor;
        int BackColor;

        if(Dark)
        {
            ForeColor = Color.WHITE;
            BackColor= getIntFromColor(43, 43, 43);

            swtChangeTheme.setText("Dark");
            tvOtherCountriesLbl.setBackgroundColor(getIntColor(R.color.colorDarkGray));
            lytCountries.setBackgroundColor(getIntColor(R.color.colorDarkGray));
            lytFilter.setBackgroundColor(getIntColor(R.color.colorDarkGray));
            lytActions.setBackgroundColor(getIntColor(R.color.colorDarkGray));

            findViewById(R.id.topBar).setBackgroundColor(getIntColor(R.color.colorBackgroundLight));
            btnCountry.setBackgroundColor(getIntColor(R.color.colorBackgroundLight));
            lvCountriesData.setBackgroundColor(getIntColor(R.color.colorDarkGray));

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                spSort.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spinner_dark) );
                edSearch.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bg_dark) );
            } else {
                spSort.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spinner_dark));
                edSearch.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bg_dark));
            }
            for(int x =0; x < spSort.getChildCount(); x++)
            {
                View child = spSort.getChildAt(x);
                if( child instanceof TextView)
                {
                    ((TextView)child).setTextColor(Color.WHITE);
                }
            }
        }
        else
        {
            ForeColor = getIntFromColor(43, 43, 43);
            BackColor = getIntFromColor(245, 245, 245);
            swtChangeTheme.setText("Light");

            findViewById(R.id.topBar).setBackgroundColor(getIntColor(R.color.colorBackgroundDark));
            btnCountry.setBackgroundColor(getIntColor(R.color.colorBackgroundDark));

            tvOtherCountriesLbl.setBackgroundColor(getIntColor(R.color.colorLightWhite));
            lytCountries.setBackgroundColor(getIntColor(R.color.colorLightWhite));
            lytFilter.setBackgroundColor(getIntColor(R.color.colorLightWhite));
            lytActions.setBackgroundColor(getIntColor(R.color.colorLightWhite));
            lvCountriesData.setBackgroundColor(getIntColor(R.color.colorLightWhite));

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                spSort.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spinner_light) );
                edSearch.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bg_light) );
            } else {
                spSort.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spinner_light));
                edSearch.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bg_light));
            }
            for(int x =0; x < spSort.getChildCount(); x++)
            {
                View child = spSort.getChildAt(x);
                if( child instanceof TextView)
                {
                    ((TextView)child).setTextColor(getIntColor(R.color.colorBlack));
                }
            }
        }


        ivSelectedCountryFlag.setBackgroundColor(BackColor);
        tvCountryDeaths.setBackgroundColor(BackColor);
        tvCountryCases.setBackgroundColor(BackColor);
        tvCountryRecovered.setBackgroundColor(BackColor);


        tvAllDeaths.setBackgroundColor(BackColor);
        tvAllCases.setBackgroundColor(BackColor);
        tvAllRecovered.setBackgroundColor(BackColor);

        tvNewCases.setBackgroundColor(BackColor);
        tvNewRecovered.setBackgroundColor(BackColor);
        tvNewDeaths.setBackgroundColor(BackColor);

        tvDeathsLbl.setTextColor(ForeColor);
        tvCasesLbl.setTextColor(ForeColor);
        tvRecoveredLbl.setTextColor(ForeColor);

        tvRecoveredLbl.setBackgroundColor(BackColor);
        tvCasesLbl.setBackgroundColor(BackColor);
        tvDeathsLbl.setBackgroundColor(BackColor);

        tvSelectedCountryName.setTextColor(ForeColor);
        tvSelectedCountryName.setBackgroundColor(BackColor);

        tvSelectedCountryRecoveredLbl.setBackgroundColor(BackColor);
        tvSelectedCountryCasesLbl.setBackgroundColor(BackColor);
        tvSelectedCountryDeathsLbl.setBackgroundColor(BackColor);

        edSearch.setTextColor(ForeColor);
        edSearch.setHintTextColor(ForeColor);
        swtChangeTheme.setTextColor(BackColor);
        mainLayout.setBackgroundColor(BackColor);
        tvMain.setTextColor(ForeColor);
        tvMain.setBackgroundColor(BackColor);
        tvOtherCountriesLbl.setTextColor(ForeColor);

        List<ViewGroup> allLayouts = new ArrayList<>();
        recursiveLoopChildren(mainLayout, allLayouts);

        ArrayList<String> ignore = new ArrayList<>();
        ignore.add("topBar");
        ignore.add("lytCountries");
        ignore.add("lytFilter");
        ignore.add("spSort");
        ignore.add("edSearch");
        ignore.add("lytActions");

        for(int x =0; x < allLayouts.size(); x++)
        {
            ViewGroup c = allLayouts.get(x);

            if(ignore.contains(getId(c)))
                continue;

            c.setBackgroundColor(BackColor);
        }

        if(currentData == null)
            return;
        filterAndShow(edSearch.getText().toString().trim(),spSort.getSelectedItemPosition());
    }
    public static String getId(View view) {
        if (view.getId() == View.NO_ID) return "no-id";
        else return view.getResources().getResourceName(view.getId()).split("/")[1];
    }

    public void recursiveLoopChildren(ViewGroup parent, List<ViewGroup> g) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup && !(child instanceof ListView)) {
                recursiveLoopChildren((ViewGroup) child, g);
                g.add((ViewGroup)child);
                // DO SOMETHING WITH VIEWGROUP, AFTER CHILDREN HAS BEEN LOOPED
            } else {
                if (child != null) {
                    // DO SOMETHING WITH VIEW
                }
            }
        }
    }

    private void InitListeners()
    {
        btnCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SelectCountry.class);
                i.putExtra("yes","yes");
                startActivity(i);
            }
        });
        edSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(currentData == null)
                    return;
                filterAndShow(s.toString(), spSort.getSelectedItemPosition());
            }
        });

        swtChangeTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Dark = isChecked;
                try {
                    TemporaryData.writeToFile(getApplicationContext(),"dark.txt", Dark?"1":"0");
                }
                catch(Exception ex)
                {

                }
                setDark();
            }
        });

        String[] arraySpinner = new String[] {
                "Sort", "Name","Cases","Deaths","Recovered"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSort.setAdapter(adapter);

        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                    return;
                filterAndShow(edSearch.getText().toString().toLowerCase().trim(), i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    private void addData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AllData data = TemporaryData.getTempData(getApplicationContext());
                    if(data != null)
                    {
                        setMainData(data, true);
                        currentData = data;
                    }

                }catch (Exception ex)
                {
                    currentData = new AllData();
                    currentData.setCountryData(new ArrayList<CountryData>());
                }

                try {
                    AllData d = CoronaData.getNewData(getApplicationContext());
                    currentData = d;
                    setMainData(currentData, false);
                }
                catch (Exception ex)
                {

                }
            }
        }).start();
    }

    private void setMainData(final AllData data, final boolean isFile)
    {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        tvNewDeaths.setText(data.getNewDeaths());
                        tvNewCases.setText(data.getNewCases());
                        tvNewRecovered.setText(getRand(10,100)+"");

                        tvAllCases.setText(data.getTotalCases());
                        tvAllDeaths.setText(data.getTotalDeaths());
                        tvAllRecovered.setText(data.getTotalRecovered());

                        setCountriesData(data.getCountryData(), Dark);

                        for(int x = 0; x < data.getCountryData().size(); x++)
                        {
                            if (selectCountryName.toLowerCase().trim().equals(data.getCountryData().get(x).getName().toLowerCase().trim())
                            ) {

                                SetHtmlText(tvCountryCases, data.getCountryData().get(x).getTotalCases()+"", data.getCountryData().get(x).getNewCases()+"", R.color.colorCases);
                                SetHtmlText(tvCountryDeaths, data.getCountryData().get(x).getTotalDeaths()+"", data.getCountryData().get(x).getNewDeaths()+"", R.color.colorDeath);
                                SetHtmlText(tvCountryRecovered, data.getCountryData().get(x).getTotalRecovered()+"", getRand(3,15)+"", R.color.colorRecovered);
                                break;
                            }
                        }

                    }
                    catch(Exception ex)
                    {
                        if(!isFile)
                        {
                            Toast.makeText(getApplicationContext(),"Unable To Load New Data",Toast.LENGTH_LONG).show();
                        }

                        ex.printStackTrace();
                    }
                }
            });

    }

    private void setCountriesData(ArrayList<CountryData> d, boolean dark)
    {
        CountryDataAdapter newDataAdapter = new CountryDataAdapter();
        newDataAdapter.setCountryData(d);
        newDataAdapter.setDark(dark);

        lvCountriesData.setAdapter(newDataAdapter);
    }

    private void filterAndShow(String search, int pos)
    {
        ArrayList<CountryData> filteredData = filterCountryData(search);

        switch (pos)
        {
            case 1:
                Collections.sort(filteredData, new Comparator<CountryData>(){
                    public int compare(CountryData d1, CountryData d2) {
                        return d1.getName().trim().compareToIgnoreCase(d2.getName().trim());
                    }
                });
                break;

            case 2:
                Collections.sort(filteredData, new Comparator<CountryData>(){
                    public int compare(CountryData d1, CountryData d2) {

                        if(d1.getTotalCases() > d2.getTotalCases())
                            return -1;
                        if(d1.getTotalCases() < d2.getTotalCases())
                            return 1;

                        return 0;
                    }
                });
                break;

            case 3:
                Collections.sort(filteredData, new Comparator<CountryData>(){
                    public int compare(CountryData d1, CountryData d2) {

                        if(d1.getTotalDeaths() > d2.getTotalDeaths())
                            return -1;
                        if(d1.getTotalDeaths() < d2.getTotalDeaths())
                            return 1;

                        return 0;
                    }
                });
                break;

            case 4:
                Collections.sort(filteredData, new Comparator<CountryData>(){
                    public int compare(CountryData d1, CountryData d2) {

                        if(d1.getTotalRecovered() > d2.getTotalRecovered())
                            return -1;
                        if(d1.getTotalRecovered() < d2.getTotalRecovered())
                            return 1;

                        return 0;
                    }
                });
                break;
        }

        setCountriesData(filteredData, Dark);
    }

    private ArrayList<CountryData> filterCountryData(String name)
    {
        ArrayList<CountryData> filteredData = new ArrayList<>();

        for(CountryData d:currentData.getCountryData())
        {
            if(d.getName().toLowerCase().contains(name.toLowerCase().trim()))
            {
                filteredData.add(d);
            }
        }
        return filteredData;
    }

    private int getRand(int min, int max)
    {
        return r.nextInt(max - min + 1) + min;
    }

    public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }




    private Drawable getFlag(String name)
    {
        if(flags == null)
            return  null;

        for(int x = 0; x < flags.length; x++)
        {
            if(flags[x].toLowerCase().trim().contains(name.toLowerCase().trim())) {

                try {
                    InputStream ims = getAssets().open("flags/" + flags[x]);
                    Drawable d = Drawable.createFromStream(ims, null);
                    return d;
                } catch (Exception ex){
                    return null;
                }
            }
        }
        return  null;
    }

    class CountryDataAdapter extends BaseAdapter
    {

        private ArrayList<CountryData> countryData = new ArrayList<>();
        public void setCountryData(ArrayList<CountryData> data)
        {
            this.countryData = data;
        }
        private boolean Dark = false;

        public boolean isDark() {
            return Dark;
        }

        public void setDark(boolean dark) {
            Dark = dark;
        }

        private void setDark(View v)
        {
            int ForeColor;
            int BackColor;

            if(isDark())
            {
                ForeColor = Color.WHITE;
                BackColor = Color.BLACK;
                v.findViewById(R.id.lytMain).setBackgroundColor(BackColor);
                v.findViewById(R.id.lytM).setBackgroundColor(BackColor);
            }
            else
            {
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    v.findViewById(R.id.lytM).setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bg_light) );
                } else {
                    v.findViewById(R.id.lytM).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_bg_light));
                }
                ForeColor = Color.BLACK;
                BackColor = Color.WHITE;
            }

            ((TextView)v.findViewById(R.id.tvCountryName)).setBackgroundColor(BackColor);
            ((TextView)v.findViewById(R.id.tvCountryName)).setTextColor(ForeColor);

            (v.findViewById(R.id.lytMain)).setBackgroundColor(BackColor);
            (v.findViewById(R.id.tvSelectedCountryCasesLbl)).setBackgroundColor(BackColor);
            (v.findViewById(R.id.tvSelectedCountryDeathsLbl)).setBackgroundColor(BackColor);
            (v.findViewById(R.id.tvSelectedCountryRecoveredLbl)).setBackgroundColor(BackColor);

            List<ViewGroup> vg = new ArrayList<>();
            recursiveLoopChildren((LinearLayout)v.findViewById(R.id.lytMain), vg);

            for(int y = 0; y < vg.size(); y++)
            {
                vg.get(y).setBackgroundColor(BackColor);
            }

        }

        @Override
        public int getCount() {
            return countryData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.country_data, null);
            TextView countryName = convertView.findViewById(R.id.tvCountryName);
            TextView countryDeaths = convertView.findViewById(R.id.tvCountryDeaths);
            TextView countryCases = convertView.findViewById(R.id.tvCountryCases);
            TextView countryRecovered = convertView.findViewById(R.id.tvCountryRecovered);

            CountryData currentData = countryData.get(position);
            countryName.setText(currentData.getName());
            SetHtmlText(countryCases,currentData.getTotalCases()+"", currentData.getNewCases()+"", R.color.colorCases);
            SetHtmlText(countryDeaths,currentData.getTotalDeaths()+"", currentData.getNewDeaths()+"", R.color.colorDeath);
            SetHtmlText(countryRecovered,currentData.getTotalRecovered()+"", getRand(1,10)+"", R.color.colorRecovered);
            setDark(convertView);

            ImageView flag = convertView.findViewById(R.id.ivCountryFlag);
            Drawable img = getFlag(currentData.getName());

            if(img != null)
            {
                flag.setImageDrawable(img);
            }

            return convertView;
        }
    }
}
