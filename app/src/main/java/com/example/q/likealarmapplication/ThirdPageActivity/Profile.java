package com.example.q.likealarmapplication.ThirdPageActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.q.likealarmapplication.MainActivity;
import com.example.q.likealarmapplication.R;

public class Profile extends AppCompatActivity {
    ArrayAdapter<CharSequence> adspin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setPrompt("시/도 를 선택하세요.");

        adspin = ArrayAdapter.createFromResource(this, R.array.selected,    android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>  parent, View view, int position, long id) {
                Toast.makeText(Profile.this,
                        adspin.getItem(position) + "을 선택 했습니다.", 1).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?>  parent) {
            }
        });


    }
}
