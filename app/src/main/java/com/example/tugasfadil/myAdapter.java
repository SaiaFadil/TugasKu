package com.example.tugasfadil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    private Context context;
    private Cursor cursor;
    private DataHelper dbHelper;
    private ProgressDialog progressDialog;

    public myAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        dbHelper = new DataHelper(context);

        // Inisialisasi ProgressDialog di konstruktor
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Sabar");
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.drawable.logosplash);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow("no")); // Ambil ID data

        String matkul = cursor.getString(cursor.getColumnIndexOrThrow("matkul"));
        String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"));
        String deadline = cursor.getString(cursor.getColumnIndexOrThrow("tgl"));

        holder.matkulTextView.setText(matkul);
        holder.deskripsiTextView.setText(deskripsi);
        holder.deadlineTextView.setText(deadline);

        // Tambahkan fungsi penghapusan saat tombol "Hapus" ditekan
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tampilkan konfirmasi dialog sebelum menghapus data
                showDeleteConfirmationDialog(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView matkulTextView;
        public TextView deskripsiTextView;
        public TextView deadlineTextView;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            matkulTextView = itemView.findViewById(R.id.matkultugas);
            deskripsiTextView = itemView.findViewById(R.id.deskripsitugas);
            deadlineTextView = itemView.findViewById(R.id.tgltugas);
            btnDelete = itemView.findViewById(R.id.button_selesai);
        }
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    private void showDeleteConfirmationDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Jika Tugas Selesai Maka Data Akan Dihapus\nApakah Anda Yakin Menghapus Data??");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Tampilkan ProgressDialog sebelum penghapusan
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Tutup ProgressDialog
                        progressDialog.dismiss();
                        hapusData(id);
                    }
                }, 2000);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void hapusData(int id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete("tugas", "no=?", new String[]{String.valueOf(id)});
        database.close();

        // Perbarui tampilan setelah penghapusan
        swapCursor(getAllDataFromDatabase());

        // Tampilkan notifikasi setelah berhasil menghapus
        showSuccessDialog();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Data berhasil dihapus")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Tidak perlu melakukan apa-apa setelah mengklik "OK"
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private Cursor getAllDataFromDatabase() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return database.rawQuery("SELECT * FROM tugas", null);
    }

}
