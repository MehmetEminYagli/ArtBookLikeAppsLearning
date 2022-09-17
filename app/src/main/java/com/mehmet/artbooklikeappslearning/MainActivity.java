package com.mehmet.artbooklikeappslearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mehmet.artbooklikeappslearning.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ////binding methodunu kullandım
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        // hangi menüyü bağlamak istiyoruz onu yazıyoruz , ikinciside hangi menü ile bağlanacağını soruyor bu genelde create ettiğimiz zaman Menu menu yazan yazıyı kullanırız
        menuInflater.inflate(R.menu.art_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_art){
            Intent intent = new Intent(this,detailAct.class); //detailAct kısmına yönlendiriyorum
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}