package com.joefs.jsontestapp.UI;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joefs.jsontestapp.Model.Person;
import com.joefs.jsontestapp.R;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class jsonOutput extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 40;
    private String outputString;
    private TextView tvJSON;
    private Button bJSON;
    private String jsonObject;
    private Context context;
    private final JSONArray jsonDataArray = new JSONArray();

    // Unique request code.
    private static final int WRITE_REQUEST_CODE = 43;
    private static int RESULT_CODE;
    private Intent intentData;
    private String filePath;
    List<Person> persons = new ArrayList<Person>();


    public jsonOutput() {
        this.context = jsonOutput.this;
//        jsonDataArray = new JSONArray();
//        jsonDataArray.put(jsonObject);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_output);
        this.jsonObject = getIntent().getStringExtra("Person_JSON");
        Person person1 = new Person("Joe", "mwe23@sdf.com", "121 232 2323", "Test 1");
        Person person2 = new Person("Matt", "mwe23@sdf.com", "232 2323", "Test 2");
        Person person3 = new Person("Lawn", "mwe23@sdf.com", "2323", "Test 3");
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        this.tvJSON = findViewById(R.id.tvJSONOutput2);
        this.bJSON = findViewById(R.id.bSaveJSON);
        tvJSON.setMovementMethod(new ScrollingMovementMethod());
        this.context = jsonOutput.this;
        jsonDataArray.put(jsonObject);
        outputGenerator();
        bJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJSON();
            }
        });
    }


    public void outputGenerator() {
        tvJSON.setText(jsonObject);
    }

    public void saveJSON() {
        //Initiates process to create a file in chosen directory and store that data file as json.
        createFile("application/json", "test.txt");
    }

    public void writeToFile(String pickedFile, JSONArray jsonDataArray) throws IOException {
        FileWriter fWriter;
        Log.d("TAG22", pickedFile);
        File file = new File(pickedFile);
        Log.d("TAG22", file.getPath());
        fWriter = new FileWriter(file, true);
        fWriter.write(jsonDataArray.toString());
        fWriter.flush();
        fWriter.close();
        Toast.makeText(getApplicationContext(), "File written successfully.", Toast.LENGTH_LONG).show();
        Log.i("TAG43", "Uri: " + pickedFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == WRITE_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    Log.i("TAG", "Uri: " + uri.toString());

                    //Write file:
                    //try {
                    // writeToFile(uri.getPath(), jsonDataArray);
                    // writeFileOnInternalStorage(context, uri.getPath(), jsonDataArray.toString());

                    // try {
                    Log.i("TAG-BeforeFileWrite", "File path: " + getPathFromUri(context, uri));
                    // writeToFile(getPathFromUri(context, uri), jsonDataArray);
                    filePath = getPathFromUri(context, uri);
                    settleWriteAuth(getPathFromUri(context, uri), persons);
                    //writeFileAsByte(getPathFromUri(context, uri), jsonObject);
                    Log.i("TAG-AfterFileWrite", "File path: " + getPathFromUri(context, uri));
                    //   } catch (IOException e) {
                    //       e.printStackTrace();
                    //   }

                    Toast.makeText(getApplicationContext(), "File saved successfully.", Toast.LENGTH_LONG).show();

//                    } catch (IOException e) {
//                        e.printStackTrace();
//
//                    }
                }
            }
        }
    }


    //START - Getting the absolute file path from Uri

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    //END - Getting the absolute file path from Uri

    private void createFile(String mimeType, String fileName) {
        //Initiate File Chooser
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }


    //Secure File Permissions before write
    public void settleWriteAuth(String pathFromUri, List<Person> Person) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Log.i("TAG20", "Permissions DENIED. Show explanation and try again.");
                //writeFileAsByte(pathFromUri, jsonObject);
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                Log.i("TAG21", "Permissions sorted");

            }
        } else {
            // Permission has already been granted
            Log.i("TAG22", "Permissions sorted");
            //writeFileAsByte(pathFromUri, jsonObject);
            try {
                writeJson(persons, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Write path
    public void writeFileOnInternalStorage(String sFileName, String sBody) {
        File file = new File(sFileName);
        if (!file.exists()) {
            file.mkdir();
        }

        try {
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Log.i("TAG43", "Uri: " + sFileName + "json.txt");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public void writeFileAsByte(String filePath, String sBody) {

        Log.i("TAG11", "filepath: " + filePath);
        File file = new File(filePath);
        try {
            file.createNewFile();
            Log.i("TAG12", "File created");
        } catch (IOException e) {
            Log.i("TAG13", "File Failed to create");
            e.printStackTrace();
        }
        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(file);

            // Writes bytes from the specified byte array to this file output stream
            fos.write(sBody.getBytes());
            Log.i("TAG13", "File written");
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while writing file " + ioe);
        } finally {
            // close the streams using close method
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }

        }


    }

    public void writeJson(List<Person> persons, String filePath) throws IOException {
        JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter(filePath)));
        //writer.setIndent("    ");

        //writer.name("Test").value(212);
        writePersonObject(writer, persons);

        writer.flush();
        writer.close();

    }

    public void writePersonObject(JsonWriter writer, List<Person> persons) throws IOException {
        writer.beginArray();
        for (Person person : persons){
            Log.i("TAG50", person.toString());
           writer.beginObject();
            writePerson(writer, person);
          writer.endObject();
        }
        writer.endArray();

    }

    public void writePerson(JsonWriter writer, Person person) throws IOException {

        Log.i("TAG60", person.toString());
        writer.name("Name").value(person.getName());
        writer.name("Email").value(person.getEmail());
        writer.name("Phone").value(person.getPhone());
        writer.name("Message").value(person.getMessage());

        Log.i("TAG61", "Object written");
    }

}