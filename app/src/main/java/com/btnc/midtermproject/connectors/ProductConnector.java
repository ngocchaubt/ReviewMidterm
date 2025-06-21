package com.btnc.midtermproject.connectors;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.btnc.midtermproject.models.Product;

import java.util.ArrayList;

public class ProductConnector {
    private ArrayList<Product> productList;

    public ProductConnector() {
        productList = new ArrayList<>();
    }

    // Lấy danh sách sản phẩm theo category
    public ArrayList<Product> getProductsByCategory(SQLiteDatabase database, int categoryId) {
        productList.clear();
        String query = "SELECT * FROM Product WHERE CategoryID = ?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(categoryId)});

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("ProductID"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("UnitPrice"));
            String imageLink = cursor.getString(cursor.getColumnIndexOrThrow("ImageLink"));
            int cateId = cursor.getInt(cursor.getColumnIndexOrThrow("CategoryID"));

            Product product = new Product(id, name, price, imageLink, cateId);
            productList.add(product);
        }

        cursor.close();
        return productList;
    }

    // Thêm sản phẩm mới
    public long addProduct(Product p, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("ProductName", p.getName());
        values.put("UnitPrice", p.getPrice());
        values.put("ImageLink", p.getImageLink());
        values.put("CategoryID", p.getCategoryId());
        return database.insert("Product", null, values);
    }

    // Cập nhật sản phẩm
    public long updateProduct(Product p, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("ProductName", p.getName());
        values.put("UnitPrice", p.getPrice());
        values.put("ImageLink", p.getImageLink());
        values.put("CategoryID", p.getCategoryId());
        return database.update("Product", values, "ProductID=?", new String[]{String.valueOf(p.getId())});
    }

    // Xoá sản phẩm theo ID
    public int deleteProduct(int id, SQLiteDatabase database) {
        return database.delete("Product", "ProductID=?", new String[]{String.valueOf(id)});
    }
}
