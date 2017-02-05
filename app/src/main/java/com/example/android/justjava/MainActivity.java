package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.R.attr.value;
import static com.example.android.justjava.R.id.quantity;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    boolean creamTopping = false;
    boolean chocolateTopping = false;
    String customerName;

    int quantity = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        displayQuantity(quantity);
        int price = calculatePrice();
        String message = createOrderSummary(price);
        Intent msg = new Intent(Intent.ACTION_SENDTO);
        msg.setData(Uri.parse("mailto:")); // only email apps should handle this
        msg.putExtra(Intent.EXTRA_SUBJECT, "Java coffee for "+customerName);
        msg.putExtra(Intent.EXTRA_TEXT, message);
        if (msg.resolveActivity(getPackageManager()) != null) {
            startActivity(msg);
        }
        else {
            Toast.makeText(getApplicationContext(),"Could not find application to send email...",Toast.LENGTH_SHORT).show();
        }
        //displayMessage(message);
    }

    public void increment(View view){
        if(quantity>=100){
            Toast upperLimit = Toast.makeText(getApplicationContext(),"Sorry! At most 100 cups per person...",Toast.LENGTH_SHORT);
            upperLimit.show();
        }
        else{
            quantity++;
            displayQuantity(quantity);
        }
    }

    public void decrement(View view){
        if(quantity<=0){
            Toast lowerLimit = Toast.makeText(getApplicationContext(),"Sorry! Cannot order less than one cup...",Toast.LENGTH_SHORT);
            lowerLimit.show();
        }
        else{
            quantity--;
            displayQuantity(quantity);
        }
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int value) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + value);
    }

//    private void displayMessage(String message) {
//        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
//        orderSummaryTextView.setText(message);
//
//    }

    private int calculatePrice() {
        CheckBox cream = (CheckBox)findViewById(R.id.whipped_cream);
        CheckBox choco = (CheckBox)findViewById(R.id.chocolate);
        int perCupCost = 5;
        if(cream.isChecked()){
            creamTopping = true;
            perCupCost = perCupCost+1;
        }

        if(choco.isChecked()){
            chocolateTopping = true;
            perCupCost = perCupCost+2;
        }
        return quantity * perCupCost;
    }

    private String createOrderSummary(int price){
        EditText name = (EditText)findViewById(R.id.editText);
        customerName = name.getText().toString();

        String value = "Name: "+customerName;
        value+="\nAdd whipped cream? "+creamTopping;
        value+="\nAdd chocolate? "+chocolateTopping;
        value+="\nQuantity: "+quantity;
        value+="\nTotal: $"+price;
        value+="\nThank you!";

        return value;
    }
}
