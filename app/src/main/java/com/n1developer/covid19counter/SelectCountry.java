package com.n1developer.covid19counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SelectCountry extends AppCompatActivity {

    String[] flags;
    ListView lvCountries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestAgain();
        try {
            flags = getAssets().list("flags");
            try {
                Bundle extras = getIntent().getExtras();

                if((extras != null && !extras.containsKey("yes")))
                {
                    String cname = TemporaryData.readFromFile(getApplicationContext(),"country.txt");
                    if(!cname.trim().equals(""))
                    {
                        for(int x = 0; x < flags.length; x++)
                        {
                            if(flags[x].toLowerCase().trim().contains(cname.toLowerCase().trim()))
                            {
                                lauchMain(cname);
                                finish();
                                return;
                            }
                        }
                    }
                }else
                {
                    is  = true;
                }


            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        setContentView(R.layout.activity_select_country);
        lvCountries= findViewById(R.id.lvCountries);
        EditText edSearch = findViewById(R.id.edSearch);
        setFlags("");

        lvCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String countryName = currentFlags[position].replaceFirst("[.][^.]+$", "").toLowerCase().trim();;

                try {
                    TemporaryData.writeToFile(getApplicationContext(),"country.txt",countryName);
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                lauchMain(countryName);
                finish();
            }
        });
        edSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                setFlags(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

    }boolean doubleBackToExitPressedOnce = false;

    boolean is = false;
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
    private void lauchMain(String c)
    {
        Intent intent = new Intent(SelectCountry.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String message = c;
        intent.putExtra("name", message);
        Log.d("Print","hello");
        startActivity(intent);
    }
    private String[] currentFlags;
    private void setFlags(String name)
    {
        List<String> flag = new ArrayList<>();

        for(int x = 0; x < flags.length; x++)
        {
            if(flags[x].replaceFirst("[.][^.]+$", "").toLowerCase().trim().contains(name.toLowerCase().trim()))
                flag.add(flags[x]);
        }
        currentFlags = flag.toArray(new String[0]);
        CountrySelectAdapter ad = new CountrySelectAdapter();
        ad.setFlags(currentFlags);
        lvCountries.setAdapter(ad);
    }
    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    class CountrySelectAdapter extends BaseAdapter
    {
        String[] flags;

        public void setFlags(String[] flags)
        {
            this.flags = flags;
        }

        @Override
        public int getCount() {
            return flags.length;
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

            convertView = getLayoutInflater().inflate(R.layout.country,null);
            ImageView v = convertView.findViewById(R.id.ivCountryFlag);
            TextView name = convertView.findViewById(R.id.tvCountryName);

            name.setText(capitalize(flags[position].replaceFirst("[.][^.]+$", "")));
            try {
                InputStream ims = getAssets().open("flags/"+flags[position]);
                Drawable d = Drawable.createFromStream(ims, null);
                v.setImageDrawable(d);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                System.exit(0);
            }

            return convertView;
        }
    }

    String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
    private void requestAgain()
    {
        for(String p:permissions)
        {
            if(!checkPermission(p))
            {
                requestPermission(p);
            }
        }
    }

    private boolean checkPermission(String permisionName) {
        int result = ContextCompat.checkSelfPermission(SelectCountry.this, permisionName);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private final int PERMISSION_REQUEST_CODE = 1;
    private void requestPermission(String name) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(SelectCountry.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Write, Read External Storage and Internet permissions allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(SelectCountry.this, new String[]{name}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive.");
                } else {
                    System.exit(0);
                }
                break;
        }
    }
}
