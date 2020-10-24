package com.example.represent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.ShowRepsAddress.input";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessageRepsAddress(View view) {
        Intent intent = new Intent(this, ShowRepsAddress.class);
        EditText address_1_eT = (EditText) findViewById(R.id.editText2);
        String address_1 = address_1_eT.getText().toString().replace(' ', '+');
        address_1 = address_1 + '+';
        EditText city_eT = (EditText) findViewById(R.id.editText4);
        String city = city_eT.getText().toString().replace(' ', '+') + '+';
        EditText state_eT = (EditText) findViewById(R.id.editText5);
        String state = state_eT.getText().toString() + '+';
        EditText zipcode_address_eT = (EditText) findViewById(R.id.editText6);
        String zipcode = zipcode_address_eT.getText().toString();
        String full_address = address_1 + city + state + zipcode;
        intent.putExtra(EXTRA_MESSAGE, full_address);
        startActivity(intent);
    }

}