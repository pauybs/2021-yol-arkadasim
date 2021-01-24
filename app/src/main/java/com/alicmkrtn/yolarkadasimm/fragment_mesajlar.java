package com.alicmkrtn.yolarkadasimm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class fragment_mesajlar extends Fragment {



    private ProgressDialog pDialog;
    DatabaseReference dbreference;
    String igonderici, itarih, imesaj;
    String vericimesaj, vericitarih;
    ListView listView, listViewChat;
    EditText edtmesaj;
    ImageView imggonder;
    String id, ikinciid;
    String ad, fotograf;
    RelativeLayout layoutmesaj, layoutgenel;
    ImageView imgprofilresmi;
    String kontrol;
    TextView txtad;
    String vericiad;
    String TOKEN;
    int deneme=0;
    ArrayList<Custom_messajes> desserts = new ArrayList<Custom_messajes>();
    ArrayList<com.alicmkrtn.yolarkadasimm.messages> messages = new ArrayList<com.alicmkrtn.yolarkadasimm.messages>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mesajlar, container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        id = sharedPref.getString("id", "null");
        SharedPreferences sharedPreff = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        vericiad = sharedPreff.getString("ad", "a");

        listView = view.findViewById(R.id.listview);
        imgprofilresmi = view.findViewById(R.id.imgprofilresmi);
        layoutgenel = view.findViewById(R.id.a);
        layoutmesaj = view.findViewById(R.id.layout_message);
        txtad = view.findViewById(R.id.txtadsoyad);
        listViewChat = view.findViewById(R.id.listviewchat);
        edtmesaj = view.findViewById(R.id.edtmesaj);
        imggonder = view.findViewById(R.id.imggonder);
        konumagorecek();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Mesajlar Yükleniyor...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View vieww, int i, long l) {
                if (kontrol.trim().length() < 10) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Uyarı");
                    builder.setMessage("Profil resmi eklemeden işleminize devam edemezsiniz.");
                    builder.setNegativeButton("Tamam", null);
                    builder.show();
                } else {
                    fotograf = desserts.get(i).getkalkis();
                    Picasso.get().load(fotograf).into(imgprofilresmi);
                    /*Picasso.with(getContext())
                            .load(fotograf)
                            .into(imgprofilresmi);*/
                    ad = desserts.get(i).getad();
                    txtad.setText(ad);
                    ikinciid = desserts.get(i).getid();
                    //Toast.makeText(getContext(),ikinciid,Toast.LENGTH_LONG).show();
                    layoutgenel.setVisibility(View.INVISIBLE);
                    Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
                    ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.VISIBLE);



                    dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(id).child("mesajlarim").child(ikinciid);
                    dbreference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            messages.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                mesajyaz(igonderici, imesaj, itarih);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
        imggonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vericitarih = new SimpleDateFormat("dd'/'MM'/'y HH:mm", Locale.getDefault()).format(new Date());
                //FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(id).child("mesajlarim").child(ikinciid);
                dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
                vericimesaj = edtmesaj.getText().toString();
                if (vericimesaj.trim().length() < 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Uyarı!");
                    builder.setMessage("Boş mesaj atamazsınız !");
                    builder.setNegativeButton("Tamam", null);
                    builder.show();
                } else {
                    String keyal = dbreference.push().getKey();

                    edtmesaj.setText("");
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (layoutgenel.getVisibility() == View.INVISIBLE) {
                    layoutgenel.setVisibility(View.VISIBLE);
                }
                if (layoutmesaj.getVisibility() == View.VISIBLE) {
                    layoutmesaj.setVisibility(View.INVISIBLE);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }
    public void konumagorecek() {
        deneme=0;

        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);
        //FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(id).child("mesajlarim").child(ikinciid);

        dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Toast.makeText(getContext(),kontrol,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference scoreRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(id).child("mesajlarim");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    resimcek(ds.getKey());
                }
                pDialog.dismiss();
                //Toast.makeText(getContext(),desserts.+"",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        scoreRef.addListenerForSingleValueEvent(eventListener);
    }
    public void resimcek(final String idd) {

        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);
        //FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(id).child("mesajlarim").child(ikinciid);

        DatabaseReference scoreRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(idd);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                yaz(ad,fotograf,idd);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        scoreRef.addListenerForSingleValueEvent(eventListener);
    }
    public void yaz(String adi, String kalkisyeri, String id) {
        desserts.add(new Custom_messajes(adi,kalkisyeri, id));
        Custom_messajes_adapter flavorAdapter = new Custom_messajes_adapter(getActivity(),desserts);
        listView.setAdapter(flavorAdapter);
    }
    public void mesajyaz(String kisi, String mesaj, String tarih)
    {
        messages.add(new messages(mesaj,kisi,tarih));
        messagesAdapter messagesAdapter = new messagesAdapter(getActivity(),messages,id);
        listViewChat.setAdapter(messagesAdapter);
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}
