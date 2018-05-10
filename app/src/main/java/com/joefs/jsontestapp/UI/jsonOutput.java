package com.joefs.jsontestapp.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private Context context;

    public jsonOutput() {
        this.context = jsonOutput.this;
    }

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
            createFile(jsonObject);
    }

    public void createFile(String jsonObject){

        final JSONArray jsonDataArray = new JSONArray();
        jsonDataArray.put(jsonObject);

        //TODO - CHECK EMULATOR
         final File documentsDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!documentsDir.exists()) {
            documentsDir.mkdirs();
        }


        //Alert Message to save
        String dialogTitle = "Confirm file write?";
        String dialogMessage = "Are you sure you'd want to write the file in path: " +
                documentsDir + "json_data.json" +
                " with data: " +
                jsonDataArray.toString() +
                " ?";

        new AlertDialog.Builder(context)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            writeToFile(documentsDir, jsonDataArray);
                            Toast.makeText(context, "JSON File successfully written to disk", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(context, "Failed to write to disk", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }}).show();


    }

    public void writeToFile(File documentsDir, JSONArray jsonDataArray) throws IOException {
        FileWriter fWriter;
        File file = new File(documentsDir, "json_data.json");
        Log.d("TAG", file.getPath());
        fWriter = new FileWriter(file, true);
        fWriter.write(jsonDataArray.toString());
        fWriter.flush();
        fWriter.close();
    }

}

