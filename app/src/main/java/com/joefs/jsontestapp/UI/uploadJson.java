package com.joefs.jsontestapp.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joefs.jsontestapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class uploadJson extends AppCompatActivity {

    //Open file manager to choose file
    private static final int READ_REQUEST_CODE = 42;
    private Uri uri;
    private TextView tvJSONOutput2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_json);
        this.tvJSONOutput2 = findViewById(R.id.tvJSONOutput2);
        selectFile();

    }



    public void selectFile(){
        performFileSearch();
        Log.i("TAG0", "Success in reading the file");

    }

    //Upon selecting a document
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("TAG1", "Uri: " + uri.toString());
                //showImage(uri);
                this.uri = uri;
                Toast.makeText(this, "File has successfully been selected!", Toast.LENGTH_LONG).show();
            }
            try {
                tvJSONOutput2.setText(readTextFromUri(uri));
            } catch (IOException e) {
                Log.e("TAG1", "Unable to display file: " + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        //intent.setType("application/json");
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    //Read from chosen location
    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
//        fileInputStream.close();
//        parcelFileDescriptor.close();
        return stringBuilder.toString();
    }
}
