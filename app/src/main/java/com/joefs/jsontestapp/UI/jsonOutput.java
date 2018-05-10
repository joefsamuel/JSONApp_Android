package com.joefs.jsontestapp.UI;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joefs.jsontestapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class jsonOutput extends AppCompatActivity {

    private String outputString;
    private TextView tvJSON;
    private Button bJSON;
    private String jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_output);
        this.jsonObject = getIntent().getStringExtra("Person_JSON");
        this.tvJSON = (TextView) findViewById(R.id.tvJSON);
        this.bJSON = (Button) findViewById(R.id.bSaveJSON);
        outputGenerator();
        bJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJSON();
            }
        });
    }


    public void outputGenerator(){
        tvJSON.setText(jsonObject);
    }

    public void saveJSON(){
        Toast.makeText(this, "Not yet implemented", Toast.LENGTH_LONG).show();
        try {
            createFile(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to write to disk. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    public void createFile(String jsonObject) throws IOException {

        JSONArray jsonDataArray = new JSONArray();
        jsonDataArray.put(jsonObject);
//TODO - CHECK EMULATOR
         File documentsDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!documentsDir.exists()) {
            documentsDir.mkdirs();
        }



//        Toast.makeText(this,fullPath, Toast.LENGTH_LONG).show();

      //  OutputStream fOut = null;
        //FileWriter file = new FileWriter(fullPath + "JSON_Data.json", false);
//        FileWriter file = new FileWriter("JSON_Data.json", false);
//        file.write(jsonDataArray.toString());
//        if(file.exists())
//            file.delete();
//        file.createNewFile();
//        fOut = new FileOutputStream(file);
//        fOut.write(jsonDataArray.toString().getBytes());
        // 100 means no compression, the lower you go, the stronger the compression
        //image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//        fOut.flush();
//        fOut.close();
//
//        file.flush();
//        file.close();



        FileWriter fWriter;
        File file = new File(documentsDir, "json_data.json");
        Log.d("TAG", file.getPath());
        fWriter = new FileWriter(file, true);
        Log.d("TAG1", "Just about to write: " + jsonObject);
        fWriter.write("Test");
        Log.d("TAG2", "Yay!");
        fWriter.flush();
        fWriter.close();

//        try {
//
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write("Hello");
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }


        Toast.makeText(this, "JSON File successfully written to disk with: " + jsonDataArray.toString().getBytes(), Toast.LENGTH_SHORT).show();
    }
}