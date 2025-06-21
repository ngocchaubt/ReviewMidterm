package com.btnc.midtermproject;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.btnc.midtermproject.connectors.AccountConnector;
import com.btnc.midtermproject.connectors.SQLiteConnector;
import com.btnc.midtermproject.models.Account;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginActivity extends AppCompatActivity {
    EditText edt_username, edt_password;
    CheckBox chkSaveLoginInfor;
    String DATABASE_NAME = "MidtermDatabase.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        chkSaveLoginInfor = findViewById(R.id.chkSaveLoginInfor);

        // Tự động điền lại thông tin nếu đã lưu
        SharedPreferences preferences = getSharedPreferences("LOGIN_INFORMATION", MODE_PRIVATE);
        boolean isSaved = preferences.getBoolean("SAVED", false);
        if (isSaved) {
            String usr = preferences.getString("USERNAME", "");
            String pwd = preferences.getString("PASSWORD", "");
            edt_username.setText(usr);
            edt_password.setText(pwd);
            chkSaveLoginInfor.setChecked(true);
        }

        // Null check for EditText fields
        if (edt_username == null || edt_password == null) {
            Toast.makeText(this, "UI error: EditText not found", Toast.LENGTH_LONG).show();
            return;
        }

        processCopy();

        // Gán sự kiện cho nút đăng nhập (giả sử id là btn_login)
        findViewById(R.id.btn_login).setOnClickListener(this::doLogin);
    }

    public void doLogin(View view) {
        String username = edt_username.getText().toString().trim();
        String password = edt_password.getText().toString().trim();

        // Check for empty username or password
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        AccountConnector ac = new AccountConnector();
        SQLiteConnector connector = new SQLiteConnector(this);
        SQLiteDatabase db = null;
        Account acc = null;

        try {
            db = connector.openDatabase();
            acc = ac.login(db, username, password);
        } finally {
            // Close database after use
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        if (acc != null) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, CategoryManagementActivity.class);
            intent.putExtra("USERNAME", acc.getUsername());
            startActivity(intent);
            finish(); // Đóng LoginActivity để không quay lại khi nhấn back
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDatabaseFromAssets();
                Toast.makeText(this, "Copied database from assets", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error copying DB: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    private void copyDatabaseFromAssets() {
        try {
            InputStream input = getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()) f.mkdirs(); // Use mkdirs() for full path

            OutputStream output = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLoginInformation() {
        SharedPreferences preferences = getSharedPreferences("LOGIN_INFORMATION", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String usr = edt_username.getText().toString();
        String pwd = edt_password.getText().toString();
        boolean isSave = chkSaveLoginInfor.isChecked();
        editor.putString("USERNAME", usr);
        editor.putString("PASSWORD", pwd);
        editor.putBoolean("SAVED", isSave);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLoginInformation();
    }
}