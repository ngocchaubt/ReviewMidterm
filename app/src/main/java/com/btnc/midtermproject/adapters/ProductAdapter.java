package com.btnc.midtermproject.adapters;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.btnc.midtermproject.R;
import com.btnc.midtermproject.models.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter  extends ArrayAdapter<Product> {
    private final Activity context;
    private final int resource;
    private final List<Product> products;

    public ProductAdapter(Activity context, int resource, List<Product> products) {
        super(context, resource, products);
        this.context = context;
        this.resource = resource;
        this.products = products;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(resource, null);

        // Ánh xạ
        ImageView imgProduct = item.findViewById(R.id.imgProduct);
        TextView txtName = item.findViewById(R.id.txtName);
        TextView txtPrice = item.findViewById(R.id.txtPrice);

        Product product = products.get(position);
        txtName.setText(product.getName());
        txtPrice.setText(String.format("%,.0f ₫", product.getPrice() * 1000));


        // Sử dụng Glide để load ảnh
        Glide.with(context)
                .load(product.getImageLink())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(imgProduct);

        return item;
    }

}
