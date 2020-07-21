package com.hemangi.acremote;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hemangi.acremote.helper.Constants;
import com.hemangi.acremote.database.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvName,tvTemp,tvHum;
    private Button btnApply;

    private SharedPreferenceManager sp;

    private ConsumerIrManager mCIR;
    private SparseArray<String> irData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = new SharedPreferenceManager(this);

        irData = new SparseArray<String>();

        tvName = findViewById(R.id.tvName);
        tvTemp = findViewById(R.id.tvTemp);
        tvHum = findViewById(R.id.tvHum);
        btnApply = findViewById(R.id.btnApply);

        //hemangi setting data
        sp.connectDB();
        tvName.setText(sp.getString(Constants.USERNAME));
        tvTemp.setText(sp.getString(Constants.TEMPERATURE));
        tvHum.setText(sp.getString(Constants.HUMIDITY));
        sp.closeDB();

        irInit();

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    //hemangi split the data
                    int num = Integer.parseInt(tvTemp.getText().toString().trim());
                    String number = String.valueOf(num);
                    int[] tempArr = new int[2];
                    for(int i = 0; i < number.length(); i++) {
                        int j = Character.digit(number.charAt(i), 10);
                        Log.e("hemangi","digit: "+j);
                        tempArr[i] = j;
                    }

                    setIrHexData(tempArr);

                    irSend(v);
                }
            }
        });
    }

    private void setIrHexData(int[] tempArr) {

        //hemangi first digit of temperature
        switch (tempArr[0]){
            case 0:
                irData.put(R.id.btnApply,hex2dec(Constants.ZERO));
                break;
            case 1:
                irData.put(R.id.btnApply,hex2dec(Constants.ONE));
                break;
            case 2:
                irData.put(R.id.btnApply,hex2dec(Constants.TWO));
                break;
            case 3:
                irData.put(R.id.btnApply,hex2dec(Constants.THREE));
                break;
            case 4:
                irData.put(R.id.btnApply,hex2dec(Constants.FOUR));
                break;
            case 5:
                irData.put(R.id.btnApply,hex2dec(Constants.FIVE));
                break;
            case 6:
                irData.put(R.id.btnApply,hex2dec(Constants.SIX));
                break;
            case 7:
                irData.put(R.id.btnApply,hex2dec(Constants.SEVEN));
                break;
            case 8:
                irData.put(R.id.btnApply,hex2dec(Constants.EIGHT));
                break;
            case 9:
                irData.put(R.id.btnApply,hex2dec(Constants.NINE));
                break;
        }

        //hemangi second digit of temperature
        switch (tempArr[1]){
            case 0:
                irData.put(R.id.btnApply,hex2dec(Constants.ZERO));
                break;
            case 1:
                irData.put(R.id.btnApply,hex2dec(Constants.ONE));
                break;
            case 2:
                irData.put(R.id.btnApply,hex2dec(Constants.TWO));
                break;
            case 3:
                irData.put(R.id.btnApply,hex2dec(Constants.THREE));
                break;
            case 4:
                irData.put(R.id.btnApply,hex2dec(Constants.FOUR));
                break;
            case 5:
                irData.put(R.id.btnApply,hex2dec(Constants.FIVE));
                break;
            case 6:
                irData.put(R.id.btnApply,hex2dec(Constants.SIX));
                break;
            case 7:
                irData.put(R.id.btnApply,hex2dec(Constants.SEVEN));
                break;
            case 8:
                irData.put(R.id.btnApply,hex2dec(Constants.EIGHT));
                break;
            case 9:
                irData.put(R.id.btnApply,hex2dec(Constants.NINE));
                break;
        }

        //hemangi pressing enter
        irData.put(R.id.btnApply,hex2dec(Constants.ENTER));

    }

    public void irInit() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // Get a reference to the ConsumerIrManager
            mCIR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void irSend(View view) {

        if(mCIR.hasIrEmitter() == true){
            String data = irData.get(view.getId());
            if (data != null) {
                String values[] = data.split(",");
                int[] pattern = new int[values.length-1];

                for (int i=0; i<pattern.length; i++){
                    pattern[i] = Integer.parseInt(values[i+1]);
                }
                Log.e("hemangi", mCIR.getCarrierFrequencies()+ "@" +        Integer.parseInt(values[0]) + "@" + pattern);

                mCIR.transmit(Integer.parseInt(values[0]), pattern);

                Toast.makeText(MainActivity.this,"Profile Applied",Toast.LENGTH_SHORT).show();
            }
        }else{
            Log.e("hemangi", "not supported options");
            Toast.makeText(MainActivity.this , "This device is not Support     infrared." , Toast.LENGTH_LONG).show();
        }
    }

    protected String hex2dec(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData
                .split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)*26));
        }

        frequency = (int) (1000000 / (frequency * 0.241246));
        list.add(0, Integer.toString(frequency));

        irData = "";
        for (String s : list) {
            irData += s + ",";
        }
        return irData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
