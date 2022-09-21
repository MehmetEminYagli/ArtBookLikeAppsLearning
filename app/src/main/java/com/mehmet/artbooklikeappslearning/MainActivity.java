package com.mehmet.artbooklikeappslearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mehmet.artbooklikeappslearning.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //artArraylist oluşturdum okunan verileri bu diziye ekliyeceğim
    ArrayList<Art> ArtarrayList;
    ArtAdapter artAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //binding methodunu kullandım

        ArtarrayList = new ArrayList<>(); //boş diziyi burada çalıştırdım

        //ÖNCELİKLE BU ARTADAPTÖRÜ RECYCLEVİEW İÇERİSİNDE BAĞLAMAMIZ ÜSTÜNE ÜSTLÜK BİRDE LAYOUT MANAGER'I VERMEMİZ GEREKİYOR

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        artAdapter = new ArtAdapter(ArtarrayList);
        binding.recyclerView.setAdapter(artAdapter);
        //komutları ile verileri recylerView id 'li kısımda gösterilecek


        getData();

    }

    private void  getData(){
//verileri database içerisind çekmek
        try {

            SQLiteDatabase database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);

            Cursor cursor = database.rawQuery("SELECT * FROM arts",null);
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("artname");

            while(cursor.moveToNext()){
                // kaç tane veri olduğunu bilmediğimiz için cursor ileriye doğru hareket ettikçe devam et diyoruz

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);

                //art diye class oluşturup constructor ile arraylist yapıyorum ve arrayliste ekliyorum
                Art art = new Art(name, id);
                //burada da boş diziye ekliyorum
                ArtarrayList.add(art);
            }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //recycleView'e veri gelince abi veri geldi göster dememiz gerekiyor .
            artAdapter.notifyDataSetChanged(); //veri seti değişti haberin olsun dememiz gerekiyor BUNU UNUTMA
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            cursor.close();
            //şimdi recycleView ile verileri kullanıcaya göstericez bunun için ilk adım recycleViewadapter classı oluşturuyoruz
        }catch (Exception e){
            e.printStackTrace();
        }
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