package com.btnc.midtermproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.btnc.midtermproject.connectors.CategoryConnector;
import com.btnc.midtermproject.connectors.SQLiteConnector;
import com.btnc.midtermproject.models.Category;

import java.util.ArrayList;

public class CategoryManagementActivity extends AppCompatActivity {

    private ListView lvCategory;
    private SQLiteConnector sqLiteConnector;
    private CategoryConnector categoryConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        lvCategory = findViewById(R.id.lvCategory);

        sqLiteConnector = new SQLiteConnector(this);
        categoryConnector = new CategoryConnector();

        // Mở database và lấy danh sách Category
        ArrayList<Category> categoryList = categoryConnector.getAllCategories(sqLiteConnector.openDatabase());

        // Chuyển danh sách Category thành tên
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }

        // Dùng ArrayAdapter với layout mặc định
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                categoryNames
        ) {
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.view.View view = super.getView(position, convertView, parent);
                android.widget.TextView textView = view.findViewById(android.R.id.text1);

                String nameWithIndex = (position + 1) + ". " + getItem(position);
                textView.setText(nameWithIndex);

                // Gán font poppins_semibold
                android.graphics.Typeface typeface = androidx.core.content.res.ResourcesCompat.getFont(getContext(), R.font.poppins_semibold);
                textView.setTypeface(typeface);

                // (Tùy chọn) chỉnh thêm size, màu cho đẹp
                textView.setTextSize(18);
                textView.setTextColor(android.graphics.Color.BLACK);

                return view;
            }
        };


        lvCategory.setAdapter(adapter);
        lvCategory.setOnItemClickListener((parent, view, position, id) -> {
            Category selected = categoryList.get(position);
            Intent intent = new Intent(CategoryManagementActivity.this, ProductManagementActivity.class);
            intent.putExtra("CATEGORY_ID", selected.getId());
            startActivity(intent);
        });

    }
}
