package com.btnc.midtermproject.connectors;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.btnc.midtermproject.models.Account;

public class AccountConnector {

    public Account login(SQLiteDatabase db, String username, String password) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Account WHERE Username = ? AND Password = ?",
                new String[]{username, password}
        );

        if (cursor.moveToFirst()) {
            Account acc = new Account();
            acc.setUsername(cursor.getString(0)); // cột 0: Username
            acc.setPassword(cursor.getString(1)); // cột 1: Password
            cursor.close();
            return acc;
        }

        cursor.close();
        return null;
    }
}
