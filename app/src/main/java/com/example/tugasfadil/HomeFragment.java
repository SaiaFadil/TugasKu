package com.example.tugasfadil;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.net.ParseException;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private Spinner namamatkulInput;
    private EditText deskripsiInput, tanggalInput;
    private DatePickerDialog picker;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Sabar");
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.drawable.logosplash);

        tanggalInput = view.findViewById(R.id.tanggalinput);
        tanggalInput.setInputType(InputType.TYPE_NULL);

        tanggalInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int tgl = cldr.get(Calendar.DAY_OF_MONTH);
                int bulan = cldr.get(Calendar.MONTH);
                int tahun = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tanggalInput.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, tahun, bulan, tgl);
                picker.show();
            }
        });
// Dalam metode onCreateView
        namamatkulInput = view.findViewById(R.id.namamatkulinput);
        String[] namaMatkulOptions = {"Pilih Mata Kuliahmu","Workshop Mobile Applications",  "Struktur Data", "Workshop Sistem Informasi Berbasis Web", "Workshop Kualitas Perangkat Lunak","Konsep Jaringan Komputer", "Matematika Diskrit", "Interpersonal Skill"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_item, namaMatkulOptions) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == 0) {
                    // Set teks menjadi cetak tebal
                    ((TextView) view).setTypeface(null, Typeface.BOLD);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        namamatkulInput.setAdapter(adapter);

// Set nilai default ke nol
        namamatkulInput.setSelection(0);


        deskripsiInput = view.findViewById(R.id.deskripsiinput);

        MaterialButton tambahButton = view.findViewById(R.id.button_tambah);
        tambahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahTugas();

            }
        });

        return view;
    }


    private void tambahTugas() {
        String matkul = namamatkulInput.getSelectedItem().toString();
        String deskripsi = deskripsiInput.getText().toString();
        String tanggal = tanggalInput.getText().toString();

        if (matkul.equals("Pilih Mata Kuliahmu")) {
            Toast.makeText(getActivity(), "Harap Pilih MataKuliah kamu dengan benar!", Toast.LENGTH_LONG).show();
        } else if (deskripsi.isEmpty()) {
            deskripsiInput.setError("Harap isi deskripsi tugasmu");
        } else if (tanggal.isEmpty()) {
            Toast.makeText(getActivity(), "Harap Pilih Tanggal Deadlinemu", Toast.LENGTH_LONG).show();

        } else if (isValidDate(tanggal)) {
                // Buat objek database helper
                DataHelper db = new DataHelper(getActivity());

                // Buka database untuk menulis
                SQLiteDatabase database = db.getWritableDatabase();

                // Buat objek ContentValues untuk menyimpan data
                ContentValues values = new ContentValues();
                values.put("matkul", matkul);
                values.put("deskripsi", deskripsi);
                values.put("tgl", tanggal);

                // Masukkan data ke database
                long newRowId = database.insert("tugas", null, values);

                // Tutup database
                db.close();

                if (newRowId != -1) {
                    // Data berhasil disimpan
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Tutup ProgressDialog
                            progressDialog.dismiss();

                    showSuccessDialog();
                        }
                    }, 2000);
                } else {
                    Toast.makeText(getActivity(), "GAGAL MENAMBAHKAN DATA", LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Tanggal tidak valid. Gunakan format dd/MM/yyyy", LENGTH_SHORT).show();
            }
        }


    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Data berhasil disimpan")
                .setMessage("Ingin melihat data?")
                .setPositiveButton("Lihat", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Ganti konten FrameLayout dengan TugasFragment
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.navbarbttm.setSelectedItemId(R.id.Tugas);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        namamatkulInput.setSelection(0);
                        deskripsiInput.setText(null);
                        tanggalInput.setText(null);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean isValidDate(String date) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);

            try {
                // Coba parse tanggal
                Date parsedDate = sdf.parse(date);
                // Pastikan tanggal yang di-parse adalah sama dengan tanggal asli yang dimasukkan
                return sdf.format(parsedDate).equals(date);
            } catch (ParseException | java.text.ParseException e) {
                return false;
            }
        }

    }
