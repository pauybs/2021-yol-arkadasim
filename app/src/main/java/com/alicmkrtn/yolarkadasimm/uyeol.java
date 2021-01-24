package com.alicmkrtn.yolarkadasimm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

public class uyeol extends AppCompatActivity {



    String dwnld;
    ImageView imgprofil;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    FirebaseStorage storage;
    StorageReference storageReference;


    DatabaseReference dbreference;
    CheckBox kadin,erkek;
    EditText edtadsoyad,edteposta,edtsifre,edtsifreonay;
    Button btnkayitol,btngirisyap;
    String valid_email;
    String changeeposta,eposta,adsoyad,sifre;
    String cinsiyetitut;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uyeol);
        btngirisyap = findViewById(R.id.btngiris);
        btnkayitol = findViewById(R.id.btnkayitol);
        edtsifreonay = findViewById(R.id.edtsifretekrar);
        edtsifre = findViewById(R.id.edtsifre);
        edteposta = findViewById(R.id.edteposta);
        edtadsoyad = findViewById(R.id.edtadsoyad);
        kadin = findViewById(R.id.checkkadin);
        erkek = findViewById(R.id.checkerkek);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imgprofil = findViewById(R.id.imgprofilresmi);
        imgprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getApplicationContext(), options, name);

        mAuth = FirebaseAuth.getInstance();
        kadin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                erkek.setChecked(false);
                if (kadin.isChecked()==false) {
                    kadin.setChecked(true); } }});
        erkek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                kadin.setChecked(false);
                if (erkek.isChecked()==false)
                {
                    erkek.setChecked(true); } }});
        btngirisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gonder = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(gonder);
                finish();}});
        btnkayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new ProgressDialog(uyeol.this);
                pDialog.setMessage("Hesabınız oluşturuluyor...");
                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.show();
                int tut=0;
                if (edtsifre.getText().toString().trim().length() <8 || edtsifre.getText().toString() == null)
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(uyeol.this);
                    builder.setTitle("Uyarı");
                    builder.setMessage("Şifre en az 8 haneli olmalıdır.");
                    builder.setNegativeButton("Tamam", null);
                    builder.show();
                    tut++;
                    pDialog.dismiss();
                }
                if (edtsifre.getText().toString().equals(edtsifreonay.getText().toString()))
                { }
                else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(uyeol.this);
                    builder.setTitle("Uyarı");
                    builder.setMessage("Şifreler uyuşmuyor.");
                    builder.setNegativeButton("Tamam", null);
                    builder.show();
                    tut++;
                    pDialog.dismiss();
                }
                if (edtadsoyad.getText().toString().trim().length() <2 || edtadsoyad.getText().toString() == null)
                {
                    edtadsoyad.setError("Geçersiz İsim Soyisim");
                    edtadsoyad.requestFocus();
                    tut++;
                    pDialog.dismiss();
                }
                if(mImageUri == null)
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(uyeol.this);
                    builder.setTitle("Uyarı");
                    builder.setMessage("Fotograf eklemeden işleminize devam edemezsiniz.");
                    builder.setNegativeButton("Tamam", null);
                    builder.show();
                    tut++;
                    pDialog.dismiss();
                }
                if (tut==0){
                    eposta = edteposta.getText().toString();
                    changeeposta = eposta;
                    adsoyad = edtadsoyad.getText().toString();
                    sifre = edtsifre.getText().toString();
                    changeeposta = encodeUserEmail(changeeposta);
                    if (kadin.isChecked())
                    {
                        cinsiyetitut = "Kadın";
                    }
                    if (erkek.isChecked())
                    {
                        cinsiyetitut = "Erkek";
                    }
                    mAuth.createUserWithEmailAndPassword(eposta, sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "E-posta adresinizi doğru girdiğinizden emin olunuz veya daha önce üyelik açmış olabilirsiniz", Toast.LENGTH_LONG).show();
                                pDialog.dismiss();
                            } else {
                                dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");


                                SharedPreferences sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("id",changeeposta);
                                editor.commit();

                                SharedPreferences sharedPrefff = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorrf = sharedPrefff.edit();
                                editorrf.putString("ad",adsoyad);
                                editorrf.commit();

                                edtadsoyad.setText("");
                                edteposta.setText("");
                                edtsifre.setText("");
                                edtsifreonay.setText("");
                                final AlertDialog.Builder builder = new AlertDialog.Builder(uyeol.this);
                                builder.setTitle("Başarılı");
                                builder.setMessage("Hesabınız oluşturuldu. Başarıyla giriş yapabilirsiniz");
                                builder.setNegativeButton("Tamam", null);
                                builder.show();
                                pDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imgprofil);
            //Picasso.with(getActivity()).load(mImageUri).into(imgprofil);
            uploadImage();
        }
    }
    private void uploadImage() {
        if(mImageUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(uyeol.this);
            progressDialog.setTitle("yükleniyor...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            //ref.putFile(mImageUri)
            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uri.isComplete());
                    Uri url = uri.getResult();
                    dwnld = url.toString();
                    imgprofil.setImageURI(mImageUri);
                    Toast.makeText(getApplicationContext(), "Yüklendi", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Hata "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Yükleniyor "+(int)progress+"%");
                        }
                    });
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}