package com.example.expensemanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetActivity extends AppCompatActivity {
    private EditText edt1;
    private Button resetaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
       reset();
    }
    private void reset()
    {
        edt1=findViewById(R.id.email_reset);
        resetaccount=findViewById(R.id.user_reset);
        resetaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Toast.makeText(getApplicationContext(),"reset Successfull",Toast.LENGTH_LONG).show();
            }
        });
    }
}
