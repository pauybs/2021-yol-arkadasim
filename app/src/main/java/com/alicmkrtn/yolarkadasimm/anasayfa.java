package com.alicmkrtn.yolarkadasimm;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class anasayfa extends AppCompatActivity {
    Fragment fragment = null;
    BottomNavigationView navView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_sonilanlar:
                    if(navView.getMenu().findItem(R.id.nav_sonilanlar).isChecked())
                    {
                        fragment=null;
                        break;
                    }
                    fragment = new fragment_sonilanlar();
                    break;
                case R.id.nav_ilanara:
                    if(navView.getMenu().findItem(R.id.nav_ilanara).isChecked())
                    {
                        fragment=null;
                        break;
                    }
                    fragment = new fragment_ilanara();
                    break;
                case R.id.nav_ilanver:
                    if(navView.getMenu().findItem(R.id.nav_ilanver).isChecked())
                    {
                        fragment=null;
                        break;
                    }
                    fragment = new fragment_ilanver();
                    break;
                case R.id.nav_mesajlar:
                    if(navView.getMenu().findItem(R.id.nav_mesajlar).isChecked())
                    {
                        fragment=null;
                        break;
                    }
                    fragment = new fragment_mesajlar();
                    break;
                case R.id.nav_profilim:
                    if(navView.getMenu().findItem(R.id.nav_profilim).isChecked())
                    {
                        fragment=null;
                        break;
                    }
                    fragment = new fragment_profilim();
                    break;
            }
            return loadFragment(fragment);
        }
    };
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        loadFragment(new fragment_sonilanlar());
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}