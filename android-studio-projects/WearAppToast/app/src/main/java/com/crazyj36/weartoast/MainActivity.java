package com.crazyj36.weartoast;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.crazyj36.weartoast.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toast.makeText(MainActivity.this, getString(R.string.toastedText), Toast.LENGTH_SHORT).show();
    }
}