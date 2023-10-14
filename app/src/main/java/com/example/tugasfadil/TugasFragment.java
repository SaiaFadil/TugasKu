package com.example.tugasfadil;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

public class TugasFragment extends Fragment {

    private RecyclerView recyclerView;
    private myAdapter adapter;
    private DataHelper dbcenter;
    private Cursor cursor;

    public TugasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tugas, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbcenter = new DataHelper(getActivity());
        RefreshList();
        adapter = new myAdapter(getActivity(), cursor);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tugas ORDER BY no DESC", null);

        // Refresh adapter dengan cursor baru
        if (adapter != null) {
            adapter.swapCursor(cursor);
        }
    }
}
