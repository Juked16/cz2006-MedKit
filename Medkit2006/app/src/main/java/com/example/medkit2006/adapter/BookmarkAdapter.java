package com.example.medkit2006.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.medkit2006.MainActivity;
import com.example.medkit2006.R;
import com.example.medkit2006.boundary.FacilityDetailUI;
import com.example.medkit2006.boundary.SearchUI;
import com.example.medkit2006.entity.Bookmark;

import java.util.ArrayList;

public class BookmarkAdapter extends ArrayAdapter<Bookmark> {
    public BookmarkAdapter(@NonNull Context context, ArrayList<Bookmark> bookmarks) {
        super(context, 0, bookmarks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tile_bookmark, parent, false);
        TextView name = convertView.findViewById(R.id.bookmarkListFacilityName);
        Bookmark bookmark = getItem(position);
        name.setText(getItem(position).getFacilityName());
        name.setOnClickListener(btn -> {
            Intent intent = new Intent(getContext(), FacilityDetailUI.class);
            intent.putExtra(SearchUI.EXTRA_MESSAGE, bookmark.getFacilityName());
            getContext().startActivity(intent);
        });
        convertView.findViewById(R.id.bookmarkListNotesBtn).setOnClickListener(btn -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Notes");
            EditText notes = new EditText(getContext());
            notes.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1000)});
            notes.setText(getItem(position).getNotes());
            builder.setView(notes);
            builder.setPositiveButton("Update", (dialog, i) -> {
                if (!notes.getText().toString().equals(bookmark.getNotes())) {
                    bookmark.setNotes(notes.getText().toString());
                    MainActivity.bookmarkMgr.updateNotes(bookmark, null, e -> {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed to update notes", Toast.LENGTH_SHORT).show();
                    });
                }
            });
            builder.setNegativeButton("Cancel", (dialog, i) -> dialog.dismiss());
            builder.show();
        });
        convertView.findViewById(R.id.bookmarkListDeleteBtn).setOnClickListener(btn -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Confirm delete?");
            builder.setPositiveButton("Delete", (dialog, i) ->
                    MainActivity.bookmarkMgr.remove(bookmark, () -> remove(bookmark), e -> {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed to remove", Toast.LENGTH_SHORT).show();
                    }));
            builder.setNegativeButton("Cancel", (dialog, i) -> dialog.dismiss());
            builder.show();
        });
        return convertView;
    }
}
