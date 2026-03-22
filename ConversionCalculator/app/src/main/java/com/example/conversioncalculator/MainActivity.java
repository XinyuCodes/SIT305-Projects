package com.example.conversioncalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    EditText currencyInput1;
    EditText fuelInput1;
    TextView currencyOutput;
    TextView fuelOutput1;
    TextView fuelOutputUnit;
    EditText tempInput1;
    TextView tempOutput1;
//    Spinner fromCurrency;
//    Spinner toCurrency;
    Button convertCurrencyButton;

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }
        );

        /// ///////////////////////////////////////////////////
        /// Currency Conversion Part
        /// ///////////////////////////////////////////////////
        currencyInput1 = findViewById(R.id.currencyInput1);
        currencyOutput = findViewById(R.id.currencyOutput1);
        Button convertButton = findViewById(R.id.convertCurrencyButton);

        Spinner fromCurrencySpinner = (Spinner) findViewById(R.id.currencyInputUnit);
        Spinner toCurrency = (Spinner) findViewById(R.id.currencyOutputUnit);
        ///creating array adapter using string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.currency_array,
                android.R.layout.simple_list_item_1
                );
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        fromCurrencySpinner.setAdapter(adapter);
        toCurrency.setAdapter(adapter);
        /// storing the currency conversion rates
        Map<String, Double> usdRates = Map.of(
                        "AUD", 1.55,
                        "EUR", 0.92,
                        "JPY", 148.5,
                        "GBP", 0.78
        );
        ///  conversion
        convertButton.setOnClickListener( v->
                {   String fromCurrency = fromCurrencySpinner.getSelectedItem().toString();
                    String toCurrencyUnit = toCurrency.getSelectedItem().toString();
                    if (!currencyInput1.getText().toString().isEmpty()) {
                        double fromRate = fromCurrency.equals("USD") ? 1.0 : usdRates.get(fromCurrency);
                        double toRate = toCurrencyUnit.equals("USD") ? 1.0 : usdRates.get(toCurrencyUnit);
                        double currencyInput = Double.parseDouble(currencyInput1.getText().toString());
                        double finalAmount = (double) currencyInput * (1 / fromRate) * toRate;

                        currencyOutput.setText(String.format("%.2f", finalAmount));
                    }
                }
        )
        ;

        /// ///////////////////////////////////////////////////
        /// Fuel Conversion Part
        /// ///////////////////////////////////////////////////

        fuelInput1 = findViewById(R.id.fuelInput1);
        fuelOutput1 = findViewById(R.id.fuelOutput1);
        fuelOutputUnit = findViewById(R.id.fuelOutputUnit);
        Button convertFuelButton = findViewById(R.id.convertFuelButton);

        Spinner fuelInputUnit = (Spinner) findViewById(R.id.fuelInputUnit);
        ///creating array adapter using string array and default spinner layout
        ArrayAdapter<CharSequence> adapterFuel = ArrayAdapter.createFromResource(
                this,
                R.array.fuel_array,
                android.R.layout.simple_list_item_1
        );
        adapterFuel.setDropDownViewResource(android.R.layout.simple_list_item_1);
        fuelInputUnit.setAdapter(adapterFuel);
        /// storing the currency conversion rates
        Map<String, Double> fuelConversionUnit = Map.of(
                "KM_TO_MILES", 0.621,
                "MPG_TO_KML", 0.425,
                "G_TO_L", 3.785
        );

        ///  conversion
        convertFuelButton.setOnClickListener( v->
                {
                    String fuelUnit = fuelInputUnit.getSelectedItem().toString();
                    if (!fuelInput1.getText().toString().isEmpty()) {
                        /// generating shorthand
                        String fromShorthand;
                        String toShorthand;
                        Double finalFuelAmount;
                        String finalUnit;
                        double fuelInput = Double.parseDouble(fuelInput1.getText().toString());
                        switch (fuelUnit) {
                            case "Kilometers":
                                fromShorthand = "KM";
                                break;
                            case "Nautical Miles":
                                fromShorthand = "MILES";
                                break;
                            case "Gallon":
                                fromShorthand = "G";
                                break;
                            case "Liter":
                                fromShorthand = "L";
                                break;
                            case "Miles per Gallon":
                                fromShorthand = "MPG";
                                break;
                            case "Kilometers per Liter":
                                fromShorthand = "KML";
                                break;
                            default:
                                fromShorthand = "UNKNOWN";
                                break;
                        }

                        switch (fuelUnit) {
                            case "Kilometers":
                                toShorthand = "MILES";
                                break;
                            case "Nautical Miles":
                                toShorthand = "KM";
                                break;
                            case "Gallon":
                                toShorthand = "L";
                                break;
                            case "Liter":
                                toShorthand = "G";
                                break;
                            case "Miles per Gallon":
                                toShorthand = "KML";
                                break;
                            case "Kilometers per Liter":
                                toShorthand = "MPG";
                                break;
                            default:
                                toShorthand = "UNKNOWN";
                                break;
                        }

                        switch (fuelUnit) {
                            case "Kilometers":
                                finalUnit = "Miles";
                                break;
                            case "Nautical Miles":
                                finalUnit = "Kilometers";
                                break;
                            case "Gallon":
                                finalUnit = "Liters";
                                break;
                            case "Liter":
                                finalUnit = "Gallon";
                                break;
                            case "Miles per Gallon":
                                finalUnit = "Kilometers per Liter";
                                break;
                            case "Kilometers per Liter":
                                finalUnit = "Miles per Gallon";
                                break;
                            default:
                                finalUnit = "UNKNOWN";
                                break;
                        } //converting unit

                        String reverseKey;
                        String fromKey;

                        reverseKey = toShorthand.toString() + "_TO_" + fromShorthand.toString();
                        fromKey = fromShorthand.toString() + "_TO_" + toShorthand.toString();
                        android.util.Log.d("FUEL", "raw fuelUnit from spinner: '" + fuelUnit + "'");

                        if (fuelConversionUnit.containsKey(fromKey)) {
                            finalFuelAmount = fuelInput * fuelConversionUnit.get(fromKey);
                        } else {
                            finalFuelAmount = fuelInput * (1.0 / fuelConversionUnit.get(reverseKey));
                        }
                        fuelOutput1.setText(String.format("%.2f", finalFuelAmount));
                        fuelOutputUnit.setText(finalUnit.toString());
                    }
                }
        );

        /// ///////////////////////////////////////////////////
        /// temperature Conversion Part
        /// ///////////////////////////////////////////////////
        tempInput1 = findViewById(R.id.tempInput1);
        tempOutput1 = findViewById(R.id.tempOutput1);
        Button convertTemperatureButton = findViewById(R.id.convertTemperatureButton);
        Spinner tempInputUnit = (Spinner) findViewById(R.id.tempInputUnit);
        Spinner tempOutputUnit = (Spinner) findViewById(R.id.tempOutputUnit);
        ///creating array adapter using string array and default spinner layout
        ArrayAdapter<CharSequence> tempAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.temp_array,
                android.R.layout.simple_list_item_1
        );
        tempAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        tempInputUnit.setAdapter(tempAdapter);
        tempOutputUnit.setAdapter(tempAdapter);
        /// creating a pattern to apply formulae to

        ///  conversion
        convertTemperatureButton.setOnClickListener( v->
                {
                    String tempInput = tempInputUnit.getSelectedItem().toString();
                    char tempInputFirstChar = tempInput.charAt(0);

                    String tempOutput = tempOutputUnit.getSelectedItem().toString();
                    char tempOutputFirstChar = tempOutput.charAt(0);

                    String convertPattern = tempInputFirstChar + "_TO_" + tempOutputFirstChar;
                    /// case statement to do the calculation
                    double result;
                    double tempInputDouble = Double.parseDouble(tempInput1.getText().toString());

                    if(tempInput.equals(tempOutput) )
                    {
                        result = tempInputDouble;
                    }
                    else {
                        switch (convertPattern) {
                            case "C_TO_F":
                                result = tempInputDouble * 1.8 + 32;
                                break;
                            case "F_TO_C":
                                result = (tempInputDouble - 32) / 1.8;
                                break;
                            case "C_TO_K":
                                result = tempInputDouble + 273.15;
                                break;
                            case "K_TO_C":
                                result = tempInputDouble - 273.15;
                                break;
                            case "F_TO_K":
                                result = (tempInputDouble - 32) / 1.8 + 273.15;
                                break;
                            case "K_TO_F":
                                result = (tempInputDouble - 273.15) * 1.8 + 32;
                                break;
                            default:
                                result = 9999999;
                                break;
                        }
                    }
                    tempOutput1.setText(String.format("%.2f", result));

                }
        )
        ;

    }
}