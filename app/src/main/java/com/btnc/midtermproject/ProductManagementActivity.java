// ProductManagementActivity.java
package com.btnc.midtermproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.btnc.midtermproject.adapters.ProductAdapter;
import com.btnc.midtermproject.connectors.ProductConnector;
import com.btnc.midtermproject.connectors.SQLiteConnector;
import com.btnc.midtermproject.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProductManagementActivity extends AppCompatActivity {
    ListView lvProduct;
    ProductAdapter adapter;
    ProductConnector connector;
    ArrayList<Product> products;
    int categoryId;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
        addViews();
        addEvents();
    }

    private void addViews() {
        lvProduct = findViewById(R.id.lvProduct);
        btnAdd = findViewById(R.id.btnAddProduct);
        connector = new ProductConnector();

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        if (categoryId == -1) {
            Toast.makeText(this, "Không có danh mục được chọn!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadProducts();
    }

    private void loadProducts() {
        SQLiteConnector sqLiteConnector = new SQLiteConnector(this);
        SQLiteDatabase db = sqLiteConnector.openDatabase();
        products = connector.getProductsByCategory(db, categoryId);

        adapter = new ProductAdapter(this, R.layout.item_product, products);
        lvProduct.setAdapter(adapter);
    }

    private void addEvents() {
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Product p = adapter.getItem(i);
                Intent intent = new Intent(ProductManagementActivity.this, ProductDetailActivity.class);
                intent.putExtra("PRODUCT", p);
                intent.putExtra("IS_ADD_MODE", false);
                intent.putExtra("CATEGORY_ID", categoryId);
                startActivityForResult(intent, 400);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductManagementActivity.this, ProductDetailActivity.class);
                intent.putExtra("IS_ADD_MODE", true);
                intent.putExtra("CATEGORY_ID", categoryId);
                startActivityForResult(intent, 300);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SQLiteConnector sqLiteConnector = new SQLiteConnector(this);
        SQLiteDatabase db = sqLiteConnector.openDatabase();

        if (requestCode == 300 && resultCode == 500) {
            Product p = (Product) data.getSerializableExtra("NEW_PRODUCT");
            long flag = connector.addProduct(p, db);
            if (flag > 0) {
                reloadProductList(db);
            }
        } else if (requestCode == 400 && resultCode == 500) {
            Product p = (Product) data.getSerializableExtra("NEW_PRODUCT");
            long flag = connector.updateProduct(p, db);
            if (flag > 0) {
                reloadProductList(db);
            }
        } else if (requestCode == 400 && resultCode == 600) {
            String id = data.getStringExtra("PRODUCT_ID_REMOVE");
            int flag = connector.deleteProduct(Integer.parseInt(id), db);
            if (flag > 0) {
                reloadProductList(db);
            }
        }
    }

    private void reloadProductList(SQLiteDatabase db) {
        products.clear();
        products.addAll(connector.getProductsByCategory(db, categoryId));
        adapter.notifyDataSetChanged();
    }

    public void moveBack(View view) {
        finish();
    }
}
