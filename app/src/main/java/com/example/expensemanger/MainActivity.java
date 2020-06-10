package com.example.expensemanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText edt1;
    private EditText edt2;
    private Button login;
    private Button java;
    private TextView forgot;
    private TextView newsign;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       mAuth =FirebaseAuth.getInstance();
       mDialog=new ProgressDialog(this);


        register();
    }

    private void register() {
        edt1 = findViewById(R.id.email_login);
        edt2 = findViewById(R.id.pass_login);
        login = findViewById(R.id.btnlogin_login);
        forgot = findViewById(R.id.forgot_login);
        newsign = findViewById(R.id.account_login);
        java= findViewById(R.id.btnlearnjava);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt1.getText().toString().trim();
                String pass = edt2.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    edt1.setText("Enter Your Email");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    edt2.setText("enter pasword");
                    return;
                }
                mDialog.setMessage("Processing");
                mDialog.show();
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful())
                     {
                         startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                         Toast.makeText(getApplicationContext(),"login Successfull ",Toast.LENGTH_SHORT).show();
                         mDialog.dismiss();
                     }
                     else
                         Toast.makeText(getApplicationContext(),"login Failed",Toast.LENGTH_SHORT).show();
                         mDialog.dismiss();
                    }
                });
               // startActivity(new Intent(getApplicationContext(),HomeActivity.class));

            }
        });
        newsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetActivity.class));
            }
        });
        java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LearnJva.class));
            }
        });

    }


}

