package com.hemangi.acremote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hemangi.acremote.helper.Constants;
import com.hemangi.acremote.database.SharedPreferenceManager;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName,etTemp,etHum;
    private Button btnSave;

    private SharedPreferenceManager sp;
    private ConsumerIrManager mCIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sp = new SharedPreferenceManager(this);

        etName = findViewById(R.id.etName);
        etTemp = findViewById(R.id.etTemp);
        etHum = findViewById(R.id.etHum);
        btnSave = findViewById(R.id.btnSave);

        //hemangi setting data
        sp.connectDB();
        etName.setText(sp.getString(Constants.USERNAME));
        etTemp.setText(sp.getString(Constants.TEMPERATURE));
        etHum.setText(sp.getString(Constants.HUMIDITY));
        sp.closeDB();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            // Get a reference to the ConsumerIrManager
            mCIR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);

            if(!mCIR.hasIrEmitter()){
                noIRHardware();
            }
        }else {
            noIRHardware();
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().trim().equals("")){
                    Toast.makeText(ProfileActivity.this,"Enter name",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etTemp.getText().toString().trim().equals("")){
                    Toast.makeText(ProfileActivity.this,"Enter temperature",Toast.LENGTH_SHORT).show();
                    return;
                }

                int tempVal = Integer.parseInt(etTemp.getText().toString().trim());

                if ( tempVal < 10 ){
                    Toast.makeText(ProfileActivity.this,"Temperature must be between 10 - 40",Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( tempVal > 40 ){
                    Toast.makeText(ProfileActivity.this,"Temperature must be between 10 - 40",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etHum.getText().toString().trim().equals("")){
                    Toast.makeText(ProfileActivity.this,"Enter humidity",Toast.LENGTH_SHORT).show();
                    return;
                }

                int humVal = Integer.parseInt(etHum.getText().toString().trim());
                if ( humVal < 20 ){
                    Toast.makeText(ProfileActivity.this,"Humidity must be between 20 - 60",Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( humVal > 60 ){
                    Toast.makeText(ProfileActivity.this,"Humidity must be between 20 - 60",Toast.LENGTH_SHORT).show();
                    return;
                }

                sp.connectDB();
                sp.setBoolean(Constants.IS_LOGGED_IN,true);
                sp.setString(Constants.USERNAME,etName.getText().toString().trim());
                sp.setString(Constants.TEMPERATURE,etTemp.getText().toString().trim());
                sp.setString(Constants.HUMIDITY,etHum.getText().toString().trim());
                sp.closeDB();

                Toast.makeText(ProfileActivity.this,"Data saved",Toast.LENGTH_SHORT).show();

                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void noIRHardware() {
        /*Toast.makeText(ProfileActivity.this,"This device does not support IR Hardware",Toast.LENGTH_SHORT).show();
        finish();
        return;*/
    }
}
