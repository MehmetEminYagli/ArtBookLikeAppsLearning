package com.mehmet.artbooklikeappslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mehmet.artbooklikeappslearning.databinding.ActivityDetailBinding;

public class detailAct extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }

    public void Save(View view) {

    }

    public  void  SelectImage(View view){

    }
}