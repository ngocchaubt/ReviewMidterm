package com.btnc.midtermproject.connectors;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.btnc.midtermproject.models.Category;

import java.util.ArrayList;

public class CategoryConnector {
    public ArrayList<Category> getAllCategories(SQLiteDatabase database) {
        ArrayList<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Category";
        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);

            Category category = new Category(id, name);
            categories.add(category);
        }

        cursor.close();
        return categories;
    }
}
