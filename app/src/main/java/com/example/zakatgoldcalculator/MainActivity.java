package com.example.zakatgoldcalculator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView tvOutput;
    EditText etWeight, etPrice;
    Spinner spType;
    Button btnCalculate, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        tvOutput = findViewById(R.id.tvOutput);
        etWeight = findViewById(R.id.etWeight);
        etPrice = findViewById(R.id.etPrice);
        spType = findViewById(R.id.spType);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        // Setup spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gold_type,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateZakat();
            }

        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etWeight.setText("");
                etPrice.setText("");
                spType.setSelection(0);
                tvOutput.setText("Your zakat result will show here. Please fill in the form first :)");
                etPrice.setText("");
            }
        });


        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void calculateZakat() {

        // Validate input
        if (etWeight.getText().toString().isEmpty() || etPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight = Double.parseDouble(etWeight.getText().toString());
        double price = Double.parseDouble(etPrice.getText().toString());
        String type = spType.getSelectedItem().toString();

        double uruf = type.equalsIgnoreCase("Keep") ? 85 : 200;

        // Calculations
        double totalValue = weight * price;

        double zakatWeight = weight - uruf;
        if (zakatWeight < 0) zakatWeight = 0;

        double zakatPayable = zakatWeight * price;
        double totalZakat = zakatPayable * 0.025;

        // Output
        String result =
                "Gold Weight: " + weight + " g\n" +
                        "Gold Price: RM " + price + "\n" +
                        "Type: " + type + "\n" +
                        "Uruf (X): " + uruf + " g\n\n" +

                        "Total Gold Value: RM " + String.format("%.2f", totalValue) + "\n" +
                        "Zakat Payable Weight: " + String.format("%.2f", zakatWeight) + " g\n" +
                        "Zakat Payable Value: RM " + String.format("%.2f", zakatPayable) + "\n" +
                        "Total Zakat (2.5%): RM " + String.format("%.2f", totalZakat);

        tvOutput.setText(result);

    }



    // MENU HANDLING
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        // ABOUT PAGE
        if (selected == R.id.menuAbout) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        // SHARE FUNCTION
        else if (selected == R.id.menuShare) {
            String link = "https://yourappwebsite.com";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, link);

            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

}
