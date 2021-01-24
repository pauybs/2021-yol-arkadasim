package com.alicmkrtn.yolarkadasimm;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class fragment_ilanver extends Fragment{



    CheckBox aynisincler;

    ListView listview,listView1;
    EditText edtkalkis,edtvaris;
    List<String> cityy = new ArrayList<>();
    LinearLayout linearkalkis,linearvaris,linearson;
    ImageView btntarihsec;
    TextView txtkalkis,txtvaris;

    ArrayList<ilandesert> aaaa = new ArrayList<ilandesert>();
    String a1,a2,a3,a4,a5;
    String kontrol;
    DatabaseReference dbreference;
    String kalkis,varis;
    EditText edtaciklama,edtfiyat;
    String id;
    TextView txttarihgoster;
    private ProgressDialog pDialog;
    Button btnilanver;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ilanver, container, false);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        id = sharedPref.getString("id","null");
        //Toast.makeText(getContext(),id,Toast.LENGTH_LONG).show();
        listview = view.findViewById(R.id.denemelistview);
        listView1 = view.findViewById(R.id.denemelistview2);
        edtkalkis = view.findViewById(R.id.edtkalkis);
        edtvaris = view.findViewById(R.id.edtvaris);
        linearkalkis = view.findViewById(R.id.linearkalkis);
        linearvaris = view.findViewById(R.id.linearvaris);
        linearson = view.findViewById(R.id.linearson);
        btntarihsec = view.findViewById(R.id.btntarihsec);
        txtkalkis = view.findViewById(R.id.txtkalkis);
        txtvaris = view.findViewById(R.id.txtvaris);

        txttarihgoster = view.findViewById(R.id.txttarihgoster);
        btnilanver = view.findViewById(R.id.btnilanver);
        edtaciklama = view.findViewById(R.id.edtaciklama);
        edtfiyat = view.findViewById(R.id.edtfiyat);

        aynisincler = view.findViewById(R.id.cinsiyetsecimi);

        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getContext(), options, name);

        for (int i =0; i<sehirler1.length;i++)
        {
            sehirler.add(sehirler1[i]);
            yaz(sehirler.get(i));
        }

        btntarihsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txttarihgoster.setText("");
                Calendar mcurrentTimeee = Calendar.getInstance();
                final int yearr = mcurrentTimeee.get(Calendar.YEAR);//Güncel Yılı alıyoruz
                final int monthh = mcurrentTimeee.get(Calendar.MONTH);//Güncel Ayı alıyoruz
                final int dayy = mcurrentTimeee.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz
                DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String ay = String.valueOf((monthOfYear+1));
                        String gun = String.valueOf(dayOfMonth);
                        if (String.valueOf(monthOfYear).length() == 1)
                        {
                            ay = ("0"+ay);
                        }
                        if (gun.length() == 1)
                        {
                            gun = ("0"+gun);
                        }
                        txttarihgoster.setText( gun + "/" + ay+ "/"+year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    }
                },yearr,monthh,dayy);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Tarih Seçiniz");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);
                datePicker.show();
            }
        });
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
                            yaz(cityy.get(ia));
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
                        yaz(sehirler.get(i));
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
                            yaz(cityy.get(ia));
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
                        yaz(sehirler.get(i));
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
                    yaz(sehirler.get(iq));
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
                txtkalkis.setText(kalkis);
                txtvaris.setText(varis);
                linearkalkis.setVisibility(View.INVISIBLE);
                linearvaris.setVisibility(View.INVISIBLE);
                Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.righttoleft);
                ViewGroup hiddenPanel = view.findViewById(R.id.linearson);
                hiddenPanel.startAnimation(bottomUp);
                hiddenPanel.setVisibility(View.VISIBLE);
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (linearvaris.getVisibility()==View.VISIBLE)
                {
                    edtkalkis.setText("");
                    sehirler.clear();
                    aaaa.clear();
                    cityy.clear();
                    for (int iq =0; iq<sehirler1.length;iq++)
                    {
                        sehirler.add(sehirler1[iq]);
                        yaz(sehirler.get(iq));
                    }
                    linearvaris.setVisibility(View.INVISIBLE);
                    Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.lefttoright);
                    ViewGroup hiddenPanel = view.findViewById(R.id.linearkalkis);
                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.VISIBLE);
                }
                if (linearson.getVisibility() == View.VISIBLE)
                {
                    edtvaris.setText("");
                    sehirler.clear();
                    aaaa.clear();
                    cityy.clear();
                    for (int iq =0; iq<sehirler1.length;iq++)
                    {
                        sehirler.add(sehirler1[iq]);
                        yaz(sehirler.get(iq));
                    }
                    linearson.setVisibility(View.INVISIBLE);
                    Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.lefttoright);
                    ViewGroup hiddenPanel = view.findViewById(R.id.linearvaris);
                    hiddenPanel.startAnimation(bottomUp);
                    hiddenPanel.setVisibility(View.VISIBLE);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        btnilanver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tut = 0;
                if (edtaciklama.getText().toString().trim().length() < 1 || edtaciklama.getText().toString() == null) {
                    edtaciklama.setError("Açıklamayı Boş Bırakmayınız");
                    edtaciklama.requestFocus();
                    tut++;
                }
                if (edtfiyat.getText().toString().trim().length() < 1 || edtfiyat.getText().toString() == null) {
                    edtfiyat.setError("Fiyatı Boş Bırakmayınız");
                    edtfiyat.requestFocus();
                    tut++;
                }
                if (txttarihgoster.getText().toString().trim().length() >14 || txttarihgoster.getText().toString() == null)
                {
                    txttarihgoster.setError("Tarih Belirleyiniz");
                    Toast.makeText(getContext(),"Tarih Belirleyiniz",Toast.LENGTH_LONG).show();
                    tut++;
                }
                if (tut == 0) {
                    final String cinsiyetsecimi;
                    pDialog = new ProgressDialog(getContext());
                    pDialog.setMessage("İlan Veriliyor...");
                    pDialog.setCancelable(false);
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.show();

                    if (aynisincler.isChecked())
                    {
                        cinsiyetsecimi = "evet";
                    }
                    else{
                        cinsiyetsecimi = "hayır";
                    }

                    final DatabaseReference myRef = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            kontrol = dataSnapshot.child(id).child("indirmeurlsi").getValue(String.class);
                            if (kontrol.trim().length() < 10) {
                                pDialog.dismiss();
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Uyarı");
                                builder.setMessage("Profil resmi eklemeden işleminize devam edemezsiniz.");
                                builder.setNegativeButton("Tamam", null);
                                builder.show();
                            } else {
                                a1 =txttarihgoster.getText().toString();
                                a2 =kalkis;
                                a3 =varis;
                                a4 =edtfiyat.getText().toString();
                                a5 =edtaciklama.getText().toString();
                                dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler");
                                String key = dbreference.push().getKey();
                                if(a1 == null || a2 == null || a3 == null || a4 == null || a5 == null)
                                {
                                    pDialog.dismiss();
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Uyarı");
                                    builder.setMessage("İnternet baglantınızı kontrol ediniz.");
                                    builder.setNegativeButton("Tamam", null);
                                    builder.show();
                                }
                                else{

                                    txttarihgoster.setText("");
                                    edtaciklama.setText("");
                                    edtfiyat.setText("");
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Başarılı");
                                    builder.setMessage("İlanınız Oluşturuldu");
                                    builder.setNegativeButton("Tamam", null);
                                    builder.show();
                                }
                            }
                            pDialog.dismiss();
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    };
                    myRef.addListenerForSingleValueEvent(eventListener);
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
    public void yaz(String isimmm)
    {
        aaaa.add(new ilandesert(isimmm));
        ilandesertadapter flavorAdapter = new ilandesertadapter(getActivity(), aaaa);
        listview.setAdapter(flavorAdapter);
        listView1.setAdapter(flavorAdapter);
    }
}
