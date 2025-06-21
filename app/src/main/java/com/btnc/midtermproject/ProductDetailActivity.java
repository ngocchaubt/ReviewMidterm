// ProductDetailActivity.java
package com.btnc.midtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.btnc.midtermproject.models.Product;
import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {
    EditText edtProductId, edtProductName, edtProductPrice;
    Button btnSave, btnUpdate, btnDelete;
    ImageView imgProduct;
    boolean isAddMode = true;
    int categoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        addViews();
        addEvents();
    }

    private void addViews() {
        edtProductId = findViewById(R.id.edtProductId);
        edtProductName = findViewById(R.id.edtProductName);
        edtProductPrice = findViewById(R.id.edtProductPrice);
        btnSave = findViewById(R.id.btnSave);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        imgProduct = findViewById(R.id.imgProduct);

        Intent intent = getIntent();
        isAddMode = intent.getBooleanExtra("IS_ADD_MODE", true);
        categoryId = intent.getIntExtra("CATEGORY_ID", -1);

        if (!isAddMode) {
            Product p = (Product) intent.getSerializableExtra("PRODUCT");
            if (p != null) {
                edtProductId.setText(String.valueOf(p.getId()));
                edtProductName.setText(p.getName());
                edtProductPrice.setText(String.valueOf(p.getPrice()));

                if (p.getImageLink() != null && !p.getImageLink().isEmpty()) {
                    Glide.with(this)
                            .load(p.getImageLink())
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(imgProduct);
                }

                btnUpdate.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }
        } else {
            btnSave.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.GONE); // <-- bổ sung dòng này
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void addEvents() {
        btnSave.setOnClickListener(v -> processSaveProduct());
        btnUpdate.setOnClickListener(v -> processUpdateProduct());
        btnDelete.setOnClickListener(v -> processDeleteProduct());
    }

    private void processSaveProduct() {
        Product p = getProductFromFields();
        p.setCategoryId(categoryId);
        Intent intent = getIntent();
        intent.putExtra("NEW_PRODUCT", p);
        setResult(500, intent);
        finish();
    }

    private void processUpdateProduct() {
        Product p = getProductFromFields();
        p.setCategoryId(categoryId);
        Intent intent = getIntent();
        intent.putExtra("NEW_PRODUCT", p);
        setResult(500, intent);
        finish();
    }

    private void processDeleteProduct() {
        String id = edtProductId.getText().toString();
        Intent intent = getIntent();
        intent.putExtra("PRODUCT_ID_REMOVE", id);
        setResult(600, intent);
        finish();
    }

    private Product getProductFromFields() {
        Product p = new Product();
        String id = edtProductId.getText().toString();
        if (!id.isEmpty()) {
            p.setId(Integer.parseInt(id));
        }
        p.setName(edtProductName.getText().toString());
        p.setPrice(Double.parseDouble(edtProductPrice.getText().toString()));

        if (!isAddMode) {
            Intent intent = getIntent();
            Product old = (Product) intent.getSerializableExtra("PRODUCT");
            if (old != null) {
                p.setImageLink(old.getImageLink());
            }
        } else {
            // mặc định ảnh khi thêm mới
            p.setImageLink("https://via.placeholder.com/200");  // hoặc thay bằng tên file
        }

        return p;
    }

    public void moveBack(View view) {
        finish();
    }
}
