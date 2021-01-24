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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class fragment_sonilanlar extends Fragment {


    Date strsimdi,strverici;
    private ProgressDialog pDialog;
    DatabaseReference dbreference;
    ListView listViewchat;
    RecyclerView listView;
    String aliciisim,aliciid,vericimesaj,vericitarih;
    int tut;
    ImageView imggonder,imgprofil;
    TextView txtad;
    EditText edtmesaj;
    String vericiad;
    ArrayList<com.alicmkrtn.yolarkadasimm.messages> messages = new ArrayList<com.alicmkrtn.yolarkadasimm.messages>();
    String qad, qkalkis, qvaris, qfiyat, qprofurl, qaciklama, qtarih,id,qcinsiyetdegeri;
    RelativeLayout linearmesaj,lineargenel;
    List<Dessert> productList;

    String kendicinsiyetim,cinsiyetitut,cinsiyetdegeritut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sonilanlar, container, false);
        listView = view.findViewById(R.id.listview);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        lineargenel = view.findViewById(R.id.a);
        linearmesaj = view.findViewById(R.id.layout_message);
        listViewchat = view.findViewById(R.id.listviewchat);
        imggonder = view.findViewById(R.id.imggonder);
        imgprofil = view.findViewById(R.id.imgprofilresmi);
        txtad = view.findViewById(R.id.txtadsoyad);
        edtmesaj = view.findViewById(R.id.edtmesaj);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        id = sharedPref.getString("id","null");
        SharedPreferences sharedPreff = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        vericiad = sharedPreff.getString("ad","a");
        cocugucek();

        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);


        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Son İlanlar Yükleniyor...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        if (linearmesaj.getVisibility() == View.VISIBLE)
        {
            Animation bottomUp = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_down);
            ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.INVISIBLE);
        }

        listView.addOnItemTouchListener(new CustomRVItemTouchListener(getContext(), listView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View vieww, int position) {

                aliciisim = productList.get(position).getad();
                aliciid = productList.get(position).getid();
                cinsiyetitut = productList.get(position).getcinsiyet();
                cinsiyetdegeritut = productList.get(position).getcinsiyetdegeri();
                if (cinsiyetdegeritut.equalsIgnoreCase("Evet"))
                {
                    if (kendicinsiyetim.equalsIgnoreCase(cinsiyetitut))
                    {
                        Picasso.get().load(productList.get(position).geturl()).into(imgprofil);
                    /*Picasso.with(getContext())
                            .load(productList.get(position).geturl())
                            .into(imgprofil);*/
                        txtad.setText(aliciisim);
                        lineargenel.setVisibility(View.INVISIBLE);
                        Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
                        ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
                        hiddenPanel.startAnimation(bottomUp);
                        hiddenPanel.setVisibility(View.VISIBLE);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Uyarı!");
                        builder.setMessage("İlan veren sadece kendi cinsiyetiyle aynı olanların mesaj atmasını istiyor.");
                        builder.setNegativeButton("Tamam", null);
                        builder.show();
                    }
                }
                else{
                    Picasso.get().load(productList.get(position).geturl()).into(imgprofil);
                    /*Picasso.with(getContext())
                            .load(productList.get(position).geturl())
                            .into(imgprofil);*/
                    txtad.setText(aliciisim);
                    lineargenel.setVisibility(View.INVISIBLE);
                    Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
                    ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        imggonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vieww) {
                vericimesaj = edtmesaj.getText().toString();
                if (vericimesaj.trim().length() <1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Uyarı!");
                    builder.setMessage("Boş mesaj atamazsınız !");
                    builder.setNegativeButton("Tamam", null);
                    builder.show();
                }
                else{
                    vericitarih = new SimpleDateFormat("dd'/'MM'/'y HH:mm", Locale.getDefault()).format(new Date());
                    dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
                    String keyal = dbreference.push().getKey();
                    if (id == null || vericimesaj == null || vericitarih == null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Uyarı!");
                        builder.setMessage("İnternet baglantınızı kontrol ediniz!");
                        builder.setNegativeButton("Tamam", null);
                        builder.show();
                    }
                    else{
                        lineargenel.setVisibility(View.VISIBLE);

                        mesajyaz(vericiad, vericimesaj, vericitarih);
                        edtmesaj.setText("");
                        Toast.makeText(getContext(),"Mesajınız Gönderildi",Toast.LENGTH_LONG).show();
                        Animation bottomUp = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_down);
                        ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
                        hiddenPanel.startAnimation(bottomUp);
                        hiddenPanel.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (lineargenel.getVisibility() == View.INVISIBLE)
                {
                    lineargenel.setVisibility(View.VISIBLE);
                }
                if (linearmesaj.getVisibility() == View.VISIBLE)
                {
                    Animation bottomUp = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_down);
                    ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.INVISIBLE);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return view;
    }
    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

    public void mesajyaz(String kisi, String mesaj, String tarih)
    {
        messages.add(new messages(mesaj,kisi,tarih));
        messagesAdapter messagesAdapter = new messagesAdapter(getActivity(),messages,id);
        listViewchat.setAdapter(messagesAdapter);
    }
    public void cocugucek() {
        tut =0;
        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);
        DatabaseReference scoreRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                kendicinsiyetim = dataSnapshot.child(id).child("cinsiyet").getValue(String.class);

                }
                pDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        scoreRef.addListenerForSingleValueEvent(eventListener);
    }
    public void konumagorecek(final String keyial, final String adial, final String profual, final String cinsiyet) {
        vericitarih = new SimpleDateFormat("dd'/'MM'/'y", Locale.getDefault()).format(new Date()).toString();

        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);
        DatabaseReference scoreRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(keyial).child("ilanlarim");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    qtarih = ds.child("tarih").getValue(String.class);
                    SimpleDateFormat simpleFormatter = new SimpleDateFormat("dd'/'MM'/'y");
                    try {
                        strverici = simpleFormatter.parse(qtarih);
                        strsimdi = simpleFormatter.parse(vericitarih);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getContext(),""+strsimdi+strverici,Toast.LENGTH_LONG).show();
                    if(strverici.after(strsimdi)){
                        if (tut == 20)
                        {
                            break;
                        }

                        productList.add(new Dessert(adial, qkalkis, qvaris, qfiyat, qaciklama, profual, qtarih, keyial,cinsiyet,qcinsiyetdegeri));
                        tut++;
                    }
                }
                yaz();
                pDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        scoreRef.addListenerForSingleValueEvent(eventListener);
    }
    public void yaz() {
        DesertAdapter flavorAdapter = new DesertAdapter(getActivity(), productList);
        listView.setAdapter(flavorAdapter);
    }
}
