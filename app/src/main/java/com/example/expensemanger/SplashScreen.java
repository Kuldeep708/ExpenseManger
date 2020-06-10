package com.example.expensemanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.expensemanger.MainActivity;
import com.example.expensemanger.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(2000);

                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(mainIntent);

                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        finish();
    }

}

