package com.joefs.jsontestapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.joefs.jsontestapp.Model.Person;
import com.joefs.jsontestapp.R;
import com.joefs.jsontestapp.Utillity.PersonInfoUtil;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText message;
    private Button testJSON;
    private Button uploadJSON;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        phone = findViewById(R.id.etPhone);
        message = findViewById(R.id.etMessage);


        testJSON = findViewById(R.id.Test_JSON);
        testJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveForm();
                Intent intent = new Intent(MainActivity.this, jsonOutput.class);
                intent.putExtra("Person_JSON", PersonInfoUtil.toJSON(person));
                startActivity(intent);
            }
        });

        uploadJSON = findViewById(R.id.upload_json);
        uploadJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, uploadJson.class);
                startActivity(intent);
            }
        });
    }

    public void retrieveForm(){
        String Name = name.getText().toString();
        String Email = email.getText().toString();
        String Phone = phone.getText().toString();
        String Message = message.getText().toString();
        Log.i(TAG, "Received following information: " + "Name: " + Name + " Email: " + Email + " Phone: " + Phone + " Message: " + Message);
        person = new Person(Name, Email, Phone, Message);
    }

}
