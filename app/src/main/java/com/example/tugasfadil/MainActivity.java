package com.example.tugasfadil;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navbarbttm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navbarbttm = findViewById(R.id.bottomNav);
        navbarbttm.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.Beranda) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.Tugas) {
                    selectedFragment = new TugasFragment();
                } else if (item.getItemId() == R.id.About) {
                    selectedFragment = new ProfilFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, selectedFragment)
                            .commit();
                }
                return true;
            }
        });

        // Buat ColorStateList untuk warna ikon saat item aktif dan tidak aktif
        ColorStateList iconTintList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        getResources().getColor(R.color.nav_item_color_selected),
                        getResources().getColor(R.color.white)
                }
        );

        // Tetapkan ColorStateList ke itemIconTint untuk BottomNavigationView
        navbarbttm.setItemIconTintList(iconTintList);

        // Buat ColorStateList untuk warna teks label saat item aktif dan tidak aktif
        ColorStateList textTintList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        getResources().getColor(R.color.nav_item_color_selected),
                        getResources().getColor(R.color.white)
                }
        );

        // Tetapkan ColorStateList ke itemTextColor untuk BottomNavigationView
        navbarbttm.setItemTextColor(textTintList);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new HomeFragment())
                .commit();
    }
    public void onBackPressed() {
    }
}
