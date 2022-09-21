package com.mehmet.artbooklikeappslearning;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mehmet.artbooklikeappslearning.databinding.ActivityDetailBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class detailAct extends AppCompatActivity {

    private ActivityDetailBinding binding;
    ActivityResultLauncher<Intent> galeriyegit;
    ActivityResultLauncher<String> izinsonucu;
    //bu resultlauncher'ları kullanabilmek için önce uygulama açıldığığında çalıştırmamız lazım yani oncreate yazmamız gerekiyor
    //kafa karıştırmamak için ayrı bir method oluşturup oncreate içine yazılacakları method içine yazıp o methodu onCreate altında çağırmak hem daha okunaklı hem daha az hata yapmamıza olanak sağlar ... ben makale yazmaya gidiom bb

    //bitmap için seçilen resmin bitmap'ini genel yaptım ki her yerde kullanabileyim
    Bitmap SecilenResim;

    //database'i her yerde kullanabilmek için yazıyorum
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        RegisterLauncher();


    }

    public void Save(View view) {

        String name = binding.ArtName.getText().toString();
        String ArtistName = binding.artistName.getText().toString();
        String year = binding.artYear.getText().toString();

        Bitmap smallImage = makesmallImage(SecilenResim,300);

        //resmi SQL 'e kayıt edilebilecek sisteme çevirme
        ByteArrayOutputStream SqlresimKayit = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50, SqlresimKayit);
        byte[] byteDizi = SqlresimKayit.toByteArray();
        //sql 'e kayıt edilebilecek şekilde 0 ve 1 lere dönüştürdük ve byteDizi içerisine kayıt ettik


        //veriyi save butonuna basılıca kayıt etmek istiyorum

        database = this.openOrCreateDatabase("arts",MODE_PRIVATE,null);

        try {

            database.execSQL("CREATE TABLE IF NOT EXISTS arts (id INTEGER , artname VARCHAR , paintername VARCHAR , year Varchar, image BLOB)");

            //verileri kayıt etme kısmı
            String sqliteString = "INSERT INTO arts(id,artname,paintername,year,image) VALUES (?,?,?,?)";
            SQLiteStatement verikaydet = database.compileStatement(sqliteString);
            //verileri tiplerine göre bağlıyorum
            //sql de indexler 0 dan değil 1 den başlıyor
            verikaydet.bindString(1,name);
            verikaydet.bindString(2,ArtistName);
            verikaydet.bindString(3,year);
            verikaydet.bindBlob(4,byteDizi);

            verikaydet.execute();

            //veri kaydetme işi bitti

        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(detailAct.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //bütün sayfaları kapat
        startActivity(intent); // sonra mainactivity sayfasını aç
        //burada yaptığım şey veri kaydettikten sonra bizi ana sayfaya yönlendiriyor ana sayfada ise kaydettiği veriyi görücek
        //diğer sayfaları kapatıyorum neden çünkü uygulama hızlı çalışsın diye.



    }

    public Bitmap makesmallImage(Bitmap resim , int MaximumSize){
        int Width = resim.getWidth();
        int Height = resim.getHeight();
        //genislik / yükseklik > 1 ise yatay
        //genislik / yükseklik < 1 ise dikey

        float bitmapOran = (float) (Width/Height);

        if (bitmapOran > 1) { //yatay
            Width = MaximumSize;
            Height = (int) (Width / bitmapOran);
        }else {
            //dikey
            Height = MaximumSize;
            Width = (Height * MaximumSize);
        }

        //bu methodun bize bir değer dönrümesi gerekiyor
        return Bitmap.createScaledBitmap(resim ,100,100,true);
    }

    public  void  SelectImage(View view){
        //izin verildimi verilmedimi onu kontrol ediyoruz                                              packagemanager ile izin verildimi verilmedimi onu kontrol ediyoruz
        //                                                                                             şuam izin verilmediyse ne yapıcaz onu yazıyoruz
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //ActivityCompat.shouldShowRequestPermissionRationale ==> bu komut da izin verilmediyse bunu kullanıcıya anlatmamız gerekebilir
            // bu komut sayesinde android kendi kontrol ediyor
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                                                                            //length_ındefinite ==>kullanıcı bir tuşa basana kadar durmasını ayarlıyor
                Snackbar.make(view,"Need permisson for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //izin istiyoruz
                    //burada da izin isticektik ya onu yazdık biz şimdi sadece methodu çağırmamız yeterli
                        //parantez içinde bizden string veri istiyor  istediği string ne diyorsanız eğer oda hangi izni istiyorsa onu yazıyoruz
                        izinsonucu.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();

            }else{
                //izin istiyoruz
                izinsonucu.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }else{
            //galeriye nasıl gidicez
            Intent intentToGallery =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galeriyegit.launch(intentToGallery); //galeriye gittik galeride yapılan işlemleri çağırdık

        }
    }

    private void RegisterLauncher(){
        //burada  hem galeriye gidiyoruz hemde galeriden seçilen resmi activity içine alıyoruz
                                                      //StartActivityForResult ==> bir sonuç için activity'i başlat
        galeriyegit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //burada da şimdi kullanıcı resmi seçti ama okey demedi yada seçmedi çıkış yaptı yada galeriye girdi ama telefonun sarjı bitti bu gibi durumlarda ne yapıcaz onu yazmamız gerekiyor .
                //ActivityResult komutu bu işler için işimizi kolaylaştırıyor
                    //bu sonuçları yakalamak için kodlar vardır GetResultCode() bu işe yarar.
                if(result.getResultCode() == RESULT_OK){
                    //bir şey seçildiyse
                    Intent intentSonucu = result.getData();//intent olan datayı aldık
                    if(intentSonucu != null){
                        //eğer intent olan datanın içi boş değilse şunları yap
                        Uri Resim = intentSonucu.getData(); //intent sonucu olan datayı URİ şeklinde datasını aldık
                        /* bu komut işimizi görür ama biz dataları veritabanına koyucağımız için pekte işimize yaramaz
                        // binding.imageView2.setImageURI(Resim); //sonra Uri şeklindeki datayı çekip istediğimiz yere koyduk*/
                        //o yüzden şöyle yapmak daha mantıklı bana göre
                        try
                        {
                            if (Build.VERSION.SDK_INT >= 28)
                            {
                                ImageDecoder.Source kaynak = ImageDecoder.createSource(detailAct.this.getContentResolver(),Resim);
                                //şimdi kaynak diye resmin decoder'ını aldık bu decoder'ı bitmap'e aktarmamız gerekiyor
                                SecilenResim = ImageDecoder.decodeBitmap(kaynak);
                                //kaynağı bitmap'e çevirdim şimdi bitmap olan resmi istediğim yere gönderebiliriyim
                                binding.imageView2.setImageBitmap(SecilenResim);
                                //böylelikye resmi bitmap'e çevirmiş oldum bu bitmap'i veritabanına atıp sonra oradan çekebilirim işimi kolaylaştırdım yani
                                //şimdi bir sorunumuz var bu yazdığım kodlar API 28 üstü telefonlarda çalışıyor 28 altı telefonlarda çalışmıyor bunun için ne yapmam gerekiyor?
                                // if ile işimizi görücez tabi
                            }
                            else
                            {//API 28 altı telefonlar için ise şu kodları yazıyoruz
                                SecilenResim = MediaStore.Images.Media.getBitmap(detailAct.this.getContentResolver(),Resim);
                                binding.imageView2.setImageBitmap(SecilenResim);
                                //NEDEN BÖYLE BİRŞEY YAPIYORUZ DİYORSAN EĞER CEVABI ŞUDUR ANDROİD STUDİO DEVELOPER'LARI  --> getBitmap() <-- KOMUTUNU KALDIRMAYA ÇALIŞIYORLAR O YÜZDEN BÖYLE
                                //YANİ ŞİMDİLİK BÖYLE DEVAM EDİLİYOR İLERİDE TEDAVÜLDEN KALKARSA EĞER KULANMAYIZ
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else {
                        //intentsonucu boş değilse ne olacak buraya
                    }
                }else {
                    //aktivite sonucu bir şey seçilmediyse ne olucak buraya yazmamız gerekiyor
                }
            }
        });




        //izin isteme kısmı burası verilirse gidicek verilmezse mesaj çıkıcak
            izinsonucu = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result == true){
                        //permission granted
                        Intent intenttoGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galeriyegit.launch(intenttoGallery); //galeriye gittik galeride yapılan işlemleri çağırdık
                    }else {
                        //permission Denied
                        Toast.makeText(detailAct.this, "İzin versen ölürmüsün", Toast.LENGTH_LONG).show();

                    }
                }
            });
    }


}