package com.alicmkrtn.yolarkadasimm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Locale;

public class fragment_ilanara extends Fragment {


    ProgressDialog pDialog;
    String kalkis,varis;
    List<String> cityy = new ArrayList<>();
    ArrayList<ilandesert> aaaa = new ArrayList<ilandesert>();
    LinearLayout linearkalkis,linearvaris;
    EditText edtkalkis,edtvaris;
    ListView listview,listView1;
    DatabaseReference dbreference;
    int denemeee;
    String id,aliciid,aliciisim;
    String vericiad,vericimesaj,vericitarih;
    ImageView imgprofilresmi,imggonder;
    EditText edtmesaj;
    TextView txtadsoyad;
    String qad,qkalkis,qvaris,qfiyat,qprofurl,qaciklama,qtarih,qcinsiyetdegeri;
    TextView txtkalkis,txtvaris;
    ArrayList<com.alicmkrtn.yolarkadasimm.messages> messages = new ArrayList<com.alicmkrtn.yolarkadasimm.messages>();
    List<Dessert> productList;
    View view;
    RecyclerView listView2;
    ListView listViewchat;
    LinearLayout linearkücük;
    RelativeLayout linearmesaj;
    List<String> sehirler = new ArrayList<>();
    String[] sehirler1 ={"Adana","Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
            "Aydın", "Balıkesir","Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
            "Çankırı", "Çorum","Denizli","Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum ", "Eskişehir",
            "Gaziantep", "Giresun","Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir",
            "Kars", "Kastamonu", "Kayseri","Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya ", "Malatya",
            "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya",
            "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon  ", "Tunceli", "Şanlıurfa", "Uşak",
            "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt ", "Karaman", "Kırıkkale", "Batman", "Şırnak",
            "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük ", "Kilis", "Osmaniye ", "Düzce"};

    String kendicinsiyetim;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.ilanaradeneme, container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        id = sharedPref.getString("id","null");
        SharedPreferences sharedPreff = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        vericiad = sharedPreff.getString("ad","a");
        listView2 = view.findViewById(R.id.listview2);
        listView2.setHasFixedSize(true);
        listView2.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();

        listview = view.findViewById(R.id.denemelistview);
        listView1 = view.findViewById(R.id.denemelistview2);
        edtkalkis = view.findViewById(R.id.edtkalkis);
        edtvaris = view.findViewById(R.id.edtvaris);
        linearkalkis = view.findViewById(R.id.linearkalkis);
        linearvaris = view.findViewById(R.id.linearvaris);


        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);


        for (int i =0; i<sehirler1.length;i++)
        {
            sehirler.add(sehirler1[i]);
            yazz(sehirler.get(i));
        }
        linearkücük = view.findViewById(R.id.linearkücük);
        linearmesaj = view.findViewById(R.id.layout_message);
        txtkalkis = view.findViewById(R.id.kckkalkis);
        txtvaris = view.findViewById(R.id.kckvaris);
        edtmesaj = view.findViewById(R.id.edtmesaj);
        imgprofilresmi = view.findViewById(R.id.imgprofilresmi);
        imggonder = view.findViewById(R.id.imggonder);
        txtadsoyad = view.findViewById(R.id.txtadsoyad);
        listViewchat = view.findViewById(R.id.listviewchat);
        edtkalkis.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aaaa.clear();
                cityy.clear();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    cityy = filterCity(sehirler,charSequence.toString());
                    if (cityy.isEmpty())
                    {
                        aaaa.clear();
                        ilandesertadapter flavorAdapter = new ilandesertadapter(getActivity(), aaaa);
                        listview.setAdapter(flavorAdapter);
                    }
                    else{
                        for (int ia =0; ia<cityy.size();ia++)
                        {
                            yazz(cityy.get(ia));
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (edtkalkis.getText().toString().trim().length()<1)
                {
                    sehirler.clear();
                    aaaa.clear();
                    cityy.clear();
                    for (int i =0; i<sehirler1.length;i++)
                    {
                        sehirler.add(sehirler1[i]);
                        yazz(sehirler.get(i));
                    }
                }
            }
        });
        edtvaris.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                aaaa.clear();
                cityy.clear();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    cityy = filterCity(sehirler,charSequence.toString());
                    if (cityy.isEmpty())
                    {
                        aaaa.clear();
                        ilandesertadapter flavorAdapter = new ilandesertadapter(getActivity(), aaaa);
                        listView1.setAdapter(flavorAdapter);
                    }
                    else{
                        for (int ia =0; ia<cityy.size();ia++)
                        {
                            yazz(cityy.get(ia));
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (edtvaris.getText().toString().trim().length()<1)
                {
                    sehirler.clear();
                    aaaa.clear();
                    cityy.clear();
                    for (int i =0; i<sehirler1.length;i++)
                    {
                        sehirler.add(sehirler1[i]);
                        yazz(sehirler.get(i));
                    }
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View vieww, int i, long l) {
                kalkis = aaaa.get(i).getad();
                sehirler.clear();
                aaaa.clear();
                cityy.clear();
                for (int iq =0; iq<sehirler1.length;iq++)
                {
                    sehirler.add(sehirler1[iq]);
                    yazz(sehirler.get(iq));
                }
                linearkalkis.setVisibility(View.INVISIBLE);
                Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.righttoleft);
                ViewGroup hiddenPanel = view.findViewById(R.id.linearvaris);
                hiddenPanel.startAnimation(bottomUp);
                hiddenPanel.setVisibility(View.VISIBLE);
            }
        });
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View vieww, int i, long l) {
                varis = aaaa.get(i).getad();
                linearvaris.setVisibility(View.INVISIBLE);

                Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.righttoleft);
                ViewGroup hiddenPanel = view.findViewById(R.id.linearkücük);
                hiddenPanel.startAnimation(bottomUp);
                hiddenPanel.setVisibility(View.VISIBLE);

                productList.clear();
                DesertAdapter flavorAdapter = new DesertAdapter(getActivity(), productList);
                listView2.setAdapter(flavorAdapter);
                denemeee = 0;
                txtvaris.setText(varis);
                txtkalkis.setText(kalkis);
                cocugucek();
            }
        });
        if (linearmesaj.getVisibility() == View.VISIBLE)
        {
            Animation bottomUp = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_down);
            ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.INVISIBLE);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (linearvaris.getVisibility() == View.VISIBLE)
                {
                    linearvaris.setVisibility(View.INVISIBLE);
                    linearkücük.setVisibility(View.INVISIBLE);
                    linearkalkis.setVisibility(View.VISIBLE);
                    linearmesaj.setVisibility(View.INVISIBLE);
                }
                if (linearkücük.getVisibility() == View.VISIBLE)
                {
                    linearvaris.setVisibility(View.VISIBLE);
                    linearkücük.setVisibility(View.INVISIBLE);
                    linearkalkis.setVisibility(View.INVISIBLE);
                    linearmesaj.setVisibility(View.INVISIBLE);
                }
                if (linearmesaj.getVisibility() == View.VISIBLE)
                {
                    linearkücük.setVisibility(View.VISIBLE);
                    Animation bottomUp = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_down);
                    ViewGroup hiddenPanel = view.findViewById(R.id.layout_message);
                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.INVISIBLE);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        listView2.addOnItemTouchListener(new CustomRVItemTouchListener(getContext(), listView2, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View vieww, int position) {

                aliciisim = productList.get(position).getad();
                aliciid = productList.get(position).getid();
                Picasso.get().load(productList.get(position).geturl()).into(imgprofilresmi);
                txtadsoyad.setText(aliciisim);
                //linearbüyük.setVisibility(View.INVISIBLE);
                linearkücük.setVisibility(View.INVISIBLE);
                Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
                ViewGroup hiddenPanel = (ViewGroup) view.findViewById(R.id.layout_message);
                hiddenPanel.startAnimation(bottomUp);
                hiddenPanel.setVisibility(View.VISIBLE);
            }
            @Override
            public void onLongClick(View view, int position) {
                //Toast.makeText(getContext(), "LongClicked at " + position, Toast.LENGTH_SHORT).show();
            }
        }));
        imggonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    linearkücük.setVisibility(View.VISIBLE);
                    linearmesaj.setVisibility(View.INVISIBLE);
                    vericitarih = new SimpleDateFormat("dd'/'MM'/'y HH:mm", Locale.getDefault()).format(new Date());

                    dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
                    String keyal = dbreference.push().getKey();

                    mesajyaz(vericiad, vericimesaj, vericitarih);
                    edtmesaj.setText("");
                    Toast.makeText(getContext(),"Mesajınız Gönderildi",Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
    public List<String>  filterCity(List<String> cities,String searchValue){
        aaaa.clear();
        cityy.clear();
        for(String city:sehirler){
            if(city.toLowerCase().contains(searchValue.toLowerCase())){
                cityy.add(city);
            }
        }
        return cityy;
    }
    public void yazz(String isimmm)
    {
        aaaa.add(new ilandesert(isimmm));
        ilandesertadapter flavorAdapter = new ilandesertadapter(getActivity(), aaaa);
        listview.setAdapter(flavorAdapter);
        listView1.setAdapter(flavorAdapter);
    }

    public void cocugucek()
    {
        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);
        DatabaseReference scoreRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        konumagorecek(ds.getKey(), qad, qprofurl,cinsiyet);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        scoreRef.addListenerForSingleValueEvent(eventListener);
    }
    public void konumagorecek(final String keyial, final String adial, final String profual,final String cinsiyet) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("İlanlar Aranıyor...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        productList.clear();
        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);
        final DatabaseReference scoreRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(keyial).child("ilanlarim");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    else
                    {
                        scoreRef.child(String.valueOf(dataSnapshot.getChildren())).removeValue();
                    }
                    if (qkalkis.equalsIgnoreCase(kalkis)) {
                        if (qvaris.equalsIgnoreCase(varis)) {
                            if (ds.child("aciklama").getValue(String.class)!=null||ds.child("fiyat").getValue(String.class)!=null||ds.child("tarih").getValue(String.class)!=null)
                            {

                                productList.add(new Dessert(adial, qkalkis, qvaris, qfiyat, qaciklama, profual, qtarih, keyial,cinsiyet,qcinsiyetdegeri));
                            }
                        }
                    }
                }
                if (productList.isEmpty())
                {
                    Toast.makeText(getActivity(),"İlan Bulunamadı",Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
                yaz();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                pDialog.dismiss();
            }
        };
        scoreRef.addListenerForSingleValueEvent(eventListener);
    }
    public void yaz()
    {
        DesertAdapter flavorAdapter = new DesertAdapter(getActivity(), productList);
            listView2.setAdapter(flavorAdapter);
    }
    public void mesajyaz(String kisi, String mesaj, String tarih)
    {
        messages.add(new messages(mesaj,kisi,tarih));
        messagesAdapter messagesAdapter = new messagesAdapter(getActivity(),messages,id);
        listViewchat.setAdapter(messagesAdapter);
    }
}
