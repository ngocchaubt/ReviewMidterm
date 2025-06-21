package com.btnc.midtermproject.connectors;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SQLiteConnector {
    private static final String DATABASE_NAME = "MidtermDatabase.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    private SQLiteDatabase database = null;
    private Activity context;

    public SQLiteConnector(Activity context) {
        this.context = context;
    }

    public SQLiteDatabase openDatabase() {
        copyDatabaseFromAssets(); // Đảm bảo database được copy
        String dbPath = context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
        Log.d("SQLiteConnector", "Opening DB at path: " + dbPath);

        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            Log.e("SQLiteConnector", "Database file not found!");
        } else {
            Log.i("SQLiteConnector", "Database exists. Size: " + dbFile.length() + " bytes");
        }

        // Mở database đúng cách
        database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        return database;
    }

    private void copyDatabaseFromAssets() {
        try {
            String outFileName = context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
            File dbFile = new File(outFileName);

            if (!dbFile.exists()) {
                File dbDir = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                if (!dbDir.exists()) dbDir.mkdirs();

                InputStream input = context.getAssets().open(DATABASE_NAME);
                OutputStream output = new FileOutputStream(outFileName);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
                output.close();
                input.close();

                Log.i("SQLiteConnector", "Database copied from assets.");
            }
        } catch (Exception e) {
            Log.e("SQLiteConnector", "Error copying database", e);
        }
    }
}
