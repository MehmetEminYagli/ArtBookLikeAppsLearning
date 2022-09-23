package com.mehmet.artbooklikeappslearning;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mehmet.artbooklikeappslearning.databinding.RecycleRowBinding;

import java.util.ArrayList;

//extends RecycleView.Adapter ile bunun bir adaptor oldunu belirtiyoruz
public class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ArtHolder> {

    //getItemCount() en kolayı kaç tane oluşturulucak bunun için array listeki veriyi alıcaz
    ArrayList<Art> artArrayList; //ile import ettim veriyi

    public ArtAdapter(ArrayList<Art> artArrayList){
        this.artArrayList = artArrayList;
        //böylelikle artadapter oluşturulduğunda benden bir array list isticek mainactivity içinde bende main activity içerisindeki array listimi vericem bu kodlar bu anlama geliyor
    }

    @NonNull
    @Override
    public ArtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //burada bir artholder oluşturmamız ve layout ile birbirine bağlamamız gerekiyor. biz burada layout'u değil binding'i kullanarak bağlama yapıcaz

        //                                                                                 //context direk this diyemiyoruz o yüzden parent'i kullanıyorduz,sonrasında hangi parenti bağlıyacağımızı söylüyoruz
        RecycleRowBinding recycleRowBinding = RecycleRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ArtHolder(recycleRowBinding);
        //ile birbirilerini bağladık
    }

    @Override
    public void onBindViewHolder(@NonNull ArtHolder holder, int position) {
        //burada da holder bize oluşturlan artholder'ı holderi kullanarak kullanıcıya göstericez
        holder.binding.recycleViewTextView.setText(artArrayList.get(position).name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),detailAct.class);
                holder.itemView.getContext().startActivity(intent);

                intent.putExtra("artId",artArrayList.get(holder.getAdapterPosition()).id);
                intent.putExtra("info","old");
                // buradan da seçilen verinin id'sini aldık detailact kısmında kontrol edicez o id'ye uyan verileri getir diye


                //bunları yazdıktan sonra oluşturulan recyclerView adaptörünü mainactivityde çağırmamız gerekiyor

            }
        });


    }

    @Override
    public int getItemCount() {
        return artArrayList.size();//arraylistin içinde kaç eleman varsa onu söyledik
    }

    //burada recycleView XML dosyasını bağlıcak onun için bir RecycleView xml dosyası oluşturucağız


    public class  ArtHolder extends  RecyclerView.ViewHolder{
                        //xml dosyasının ismi ne olursa onu buraya binding olarak ekliyoruz
        private RecycleRowBinding binding; //bunu iki binding 'i  eşitlemek için kullanıcam

        public ArtHolder(RecycleRowBinding binding) {
            super(binding.getRoot());// diyerek görünümü alıyoruz

            this.binding = binding;


        }
    }


}
