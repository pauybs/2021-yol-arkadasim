package com.alicmkrtn.yolarkadasimm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class fragment_profilim extends Fragment {
    String vad,vkalkis,vvaris,vfiyat,vicerik,vurl,vtarih,vid;
    ImageView imgprofil;
    EditText edtadsoyad,edteposta,edtsifre,edtbiyografi;
    Button btnguncelle,btncikisyap;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String id;
    Boolean kontroll;
    Button btnilanlarim,btnprofilbilgilerim;
    DatabaseReference dbreference;
    String dwnld,silinecekurl,kontrol;
    RecyclerView listView;
    RelativeLayout layout_message,layout_profil;
    List<Dessert> productList;
    String profurlsi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profilim, container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        id = sharedPref.getString("id","null");
        SharedPreferences sharedPreff =this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editorr = sharedPreff.edit();
        //Toast.makeText(getContext(),id,Toast.LENGTH_LONG).show();
        imgprofil = view.findViewById(R.id.imgprofilresmi);
        edtadsoyad = view.findViewById(R.id.edtadsoyad);
        edteposta = view.findViewById(R.id.edteposta);
        edtsifre = view.findViewById(R.id.edtsifre);
        edtbiyografi = view.findViewById(R.id.edtbiyografi);
        btnguncelle = view.findViewById(R.id.btnguncelle);
        btncikisyap = view.findViewById(R.id.btncikisyap);
        btnilanlarim = view.findViewById(R.id.btnilanlarim);
        btnprofilbilgilerim = view.findViewById(R.id.btnprofilbilgilerim);
        listView = view.findViewById(R.id.listview);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        layout_message = view.findViewById(R.id.layout_message);
        layout_profil = view.findViewById(R.id.layout_profil);
        mAuth = FirebaseAuth.getInstance();

        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        resmiyukle();
        verilerigetir();
        ilanlarimigetir();
        layout_profil.setVisibility(View.INVISIBLE);
        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_up);
        ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
        hiddenPanel.startAnimation(bottomUp);
        hiddenPanel.setVisibility(View.VISIBLE);
        btnilanlarim.setBackgroundColor(getResources().getColor(R.color.btnsecilen));
        btnprofilbilgilerim.setBackgroundColor(getResources().getColor(R.color.btniptal));

        btnilanlarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vieww) {
                ilanlarimigetir();
                layout_profil.setVisibility(View.INVISIBLE);
                btnilanlarim.setBackgroundColor(getResources().getColor(R.color.btnsecilen));
                btnprofilbilgilerim.setBackgroundColor(getResources().getColor(R.color.btniptal));
                Animation bottomUp = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_up);
                ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
                hiddenPanel.startAnimation(bottomUp);
                hiddenPanel.setVisibility(View.VISIBLE);
            }
        });

        btnprofilbilgilerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vieww) {
                layout_message.setVisibility(View.INVISIBLE);
                btnprofilbilgilerim.setBackgroundColor(getResources().getColor(R.color.btnsecilen));
                btnilanlarim.setBackgroundColor(getResources().getColor(R.color.btniptal));
                Animation bottomUp = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_up);
                ViewGroup hiddenPanel = view.findViewById(R.id.layout_profil);
                hiddenPanel.startAnimation(bottomUp);
                hiddenPanel.setVisibility(View.VISIBLE);
            }
        });


        btncikisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        //set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        //set title
                        .setTitle("Uyarı")
                        //set message
                        .setMessage("Çıkış Yapmak İstiyor Musunuz ?")
                        //set positive button
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.getInstance().signOut();
                                Intent gonder = new Intent(getContext(), MainActivity.class);
                                startActivity(gonder);
                                getActivity().finish();
                            }
                        })
                        //set negative button
                        .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imgprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        listView.addOnItemTouchListener(new CustomRVItemTouchListener(getContext(), listView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View vieww, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Uyarı");
                builder.setMessage("Bu ilanı silmek istediğinizden emin misiniz?");
                builder.setNegativeButton("Hayır", null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        String key = productList.get(position).getid();

                        ilanlarimigetir();
                    }
                });
                builder.show();
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnguncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = 0;
                if (edtadsoyad.getText().toString().trim().length()<1)
                {
                    edtadsoyad.setError("Boş bırakmayınız");
                    a++;
                }
                if (edtsifre.getText().toString().trim().length()<8)
                {
                    edtsifre.setError("Şifre En az 8 haneli olmalıdır");
                    a++;
                }
                if (a==0)
                {
                    DatabaseReference myRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                                builder.setTitle("Başarılı");
                                builder.setMessage("Bilgileriniz değiştirildi");
                                builder.setNegativeButton("Tamam", null);
                                builder.show();
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            //Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    };
                    myRef.addListenerForSingleValueEvent(eventListener);
                }

            }
        });
        return view;
    }
    public void ilanlarimigetir()
    {
        productList.clear();
        kontroll = false;

        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);

        DatabaseReference myRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(id).child("ilanlarim");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {

                    yaz(vad,vkalkis,vvaris,vfiyat,vicerik,vurl,vtarih,vid);
                    kontroll = true;
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        if (kontroll == false)
        {
            productList.clear();
            DesertAdapter flavorAdapter = new DesertAdapter(getActivity(), productList);
            listView.setAdapter(flavorAdapter);
        }
    }
    public void yaz(String adi, String kalkisyeri, String varisyeri, String fiyati, String icerik, String url, String tarih, String id) {
        productList.add(new Dessert(adi, kalkisyeri, varisyeri, fiyati, icerik, url, tarih, id,"",""));
        DesertAdapter flavorAdapter = new DesertAdapter(getActivity(), productList);
        listView.setAdapter(flavorAdapter);
    }
    public void verilerigetir()
    {
        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);


        DatabaseReference myRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (kontrol.trim().length() <10 || imgprofil.getDrawable() == null)
                {
                    imgprofil.setImageResource(R.drawable.addphoto);
                }

                strepostaa = decodeUserEmail(strepostaa);
                edtadsoyad.setText(strada);
                edteposta.setText(strepostaa);
                edtsifre.setText(strsifree);
                edtbiyografi.setText(strbiyografi);
                //Toast.makeText(getApplicationContext(),strada,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imgprofil);
            uploadImage();
        }
    }
    private void uploadImage() {
        if(mImageUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("yükleniyor...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            SecureRandom random = new SecureRandom();
                            String name = String.valueOf(random.nextInt());
                            final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);
                            //dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(id).child("ilanlarim");
                            progressDialog.dismiss();
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uri.isComplete());
                            Uri url = uri.getResult();
                            dwnld = url.toString();
                            dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
                            Toast.makeText(getContext(), "Yüklendi", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Hata "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void resmiyukle()
    {
        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);
        DatabaseReference scoreRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (profurlsi.trim().length()>10)
                {
                    Picasso.get().load(profurlsi).into(imgprofil);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        scoreRef.addListenerForSingleValueEvent(eventListener);
    }
}
