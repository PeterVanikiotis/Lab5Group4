package com.example.firebase;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPrice;
    Button buttonAddProduct;
    ListView listViewProducts;
    List<Product> products;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference databaseProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        listViewProducts = findViewById(R.id.listViewProducts);
        buttonAddProduct = findViewById(R.id.addButton);

        products = new ArrayList<>();
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("products");

        clickOnAddProduct();
        onItemLongClick();

    }


    private void clickOnAddProduct() {
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
    }

    private void onItemLongClick() {

        listViewProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = products.get(i);
                showUpdateDeleteDialog(product.getId(), product.getProductName());
                return true;
            }
        });
    }

    private void showUpdateDeleteDialog(final String productId, String productName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.dialog_editTextName);
        final EditText editTextPrice = dialogView.findViewById(R.id.dialog_editTextPrice);
        final Button buttonUpdate = dialogView.findViewById(R.id.buttonUpdateProduct);
        final Button buttonDelete = dialogView.findViewById(R.id.buttonDeleteProduct);

        dialogBuilder.setTitle(productName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                double price = Double.parseDouble(editTextPrice.getText().toString());
                if (!TextUtils.isEmpty(name)) {
                    updateProduct(productId, name, price);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(productId);
                b.dismiss();
            }
        });
    }

    private void updateProduct(String id, String name, double price) {

        Toast.makeText(getApplicationContext(), "NOT IMPLEMENTED YET", Toast.LENGTH_LONG).show();
    }

    private void deleteProduct(String id) {

        Toast.makeText(getApplicationContext(), "NOT IMPLEMENTED YET", Toast.LENGTH_LONG).show();
    }

    private void addProduct() {

        String id = myRef.push().getKey();
        Product p = new Product(editTextName.getText().toString(), Integer.parseInt(editTextPrice.getText().toString()));
        assert id != null;
        myRef.child(id).setValue(p);
        editTextName.setText("");
        editTextPrice.setText("");
    }
    protected void onStart() {
        super.onStart();
        databaseProducts.addValueEventListener(new ValueEventListener() {
           public void onDataChange(DataSnapshot dataSnapshot) {
               products.clear();
               for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                   Product product = postSnapshot.getValue(Product.class);
                   products.add(product);
               }
               ProductList productsAdapter = new ProductList(MainActivity.this, products);
               listViewProducts.setAdapter(productsAdapter);
           }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}