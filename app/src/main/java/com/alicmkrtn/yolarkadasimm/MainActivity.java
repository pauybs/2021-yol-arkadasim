package com.alicmkrtn.yolarkadasimm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {


    TextView txtuyeol,txtsifremiunuttum;
    EditText edteposta,edtsifre,edtforget;
    Button btngiris,btnzero;
    String eposta,changeeposta;
    String sifretut,forgeteposta;
    DatabaseReference dbreference;
    RelativeLayout layout;
    LinearLayout genel;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtuyeol = findViewById(R.id.txtuyeol);
        edteposta=findViewById(R.id.edtkullanici);
        edtsifre = findViewById(R.id.edtsifre);
        btngiris = findViewById(R.id.btngiris);
        layout = findViewById(R.id.la);
        edtforget = findViewById(R.id.edtunutulansifre);
        btnzero = findViewById(R.id.btnsifregonder);
        txtsifremiunuttum = findViewById(R.id.txtunuttum);
        genel = findViewById(R.id.genel);
        SecureRandom random = new SecureRandom();
        String name = String.valueOf(random.nextInt());
        final FirebaseApp secondApp = FirebaseApp.initializeApp(getApplicationContext(), options, name);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
        {
            Intent gonder = new Intent(getApplicationContext(), anasayfa.class);
            startActivity(gonder);
            finish();
        }
        txtsifremiunuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vieww) {
                genel.setVisibility(View.INVISIBLE);
                Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                ViewGroup hiddenPanel = findViewById(R.id.la);
                hiddenPanel.startAnimation(bottomUp);
                hiddenPanel.setVisibility(View.VISIBLE);
            }
        });
        btnzero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtforget.getText().toString().trim().length() < 5)
                {
                    edtforget.setError("boş bırakmayınız");
                }
                else {
                    forgeteposta = edtforget.getText().toString();
                    sendMessage();
                }
            }
        });
        txtuyeol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gonder = new Intent(getApplicationContext(), uyeol.class);
                startActivity(gonder); }});
        btngiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Giriş yapılıyor...");
                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.show();
                int kontrol = 0;
                if (edtsifre.getText().toString().trim().length() < 8) {
                    edtsifre.setError("Şifre en az 8 hanelidir");
                    edtsifre.requestFocus();
                    kontrol++;
                    pDialog.dismiss();
                }
                if (edteposta.getText().toString().trim().length() < 1) {
                    edteposta.setError("Boş Bırakmayınız");
                    edteposta.requestFocus();
                    kontrol++;
                    pDialog.dismiss();
                }
                if (kontrol == 0) {
                    eposta = edteposta.getText().toString();
                    changeeposta = eposta;
                    changeeposta = encodeUserEmail(changeeposta);
                    sifretut = edtsifre.getText().toString();
                    mAuth.signInWithEmailAndPassword(eposta,sifretut).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Giriş bilgilerinizi doğrulayınız.", Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                            } else {
                                String token = FirebaseInstanceId.getInstance().getToken();
                                dbreference = FirebaseDatabase.getInstance(secondApp).getReference("uyeler").child(changeeposta);
                                dbreference.child("token").setValue(token);
                                SharedPreferences sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("id",changeeposta);
                                editor.commit();

                                pDialog.dismiss();
                                Intent gonder = new Intent(getApplicationContext(), anasayfa.class);
                                startActivity(gonder);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
    public void sendMessage() {
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("E-mail gönderiliyor");
        dialog.setMessage("Lütfen bekleyiniz...");
        dialog.show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(forgeteposta)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sıfırlama bağlantınız gönderildi. Spam kutusunu kontrol etmeyi unutmayınız", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            edtforget.setError("Birşeyler ters gitti");
                            dialog.dismiss();
                        }
                    }
                });
        edtforget.setText("");
        Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
        ViewGroup hiddenPanel = findViewById(R.id.la);
        hiddenPanel.startAnimation(bottomUp);
        hiddenPanel.setVisibility(View.INVISIBLE);
        genel.setVisibility(View.VISIBLE);
    }
    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
    @Override
    public void onRestart() {
        super.onRestart();
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    @Override
    public void onBackPressed() {
        if (layout.getVisibility() == View.VISIBLE)
        {
            Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            ViewGroup hiddenPanel = findViewById(R.id.la);
            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.INVISIBLE);
            genel.setVisibility(View.VISIBLE);
        }
    }

}