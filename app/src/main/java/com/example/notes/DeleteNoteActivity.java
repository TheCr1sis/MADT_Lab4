package com.example.notes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {

    private ListView deleteNoteListView;
    private ArrayList<String> deleteNotesList;
    private ArrayAdapter<String> deleteNotesAdapter;
    private NoteDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        deleteNoteListView = findViewById(R.id.deleteNoteListView);
        deleteNotesList = new ArrayList<>();
        deleteNotesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, deleteNotesList);
        deleteNoteListView.setAdapter(deleteNotesAdapter);
        deleteNoteListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        dbHelper = new NoteDatabaseHelper(this);

        loadNotesFromDatabase();

        Button btnDeleteNote = findViewById(R.id.btnDeleteNote);
        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedNotes();
                Intent intent = new Intent(DeleteNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadNotesFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {NoteDatabaseHelper.COLUMN_TITLE};
        Cursor cursor = db.query(
                NoteDatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        deleteNotesList.clear();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_TITLE));
            deleteNotesList.add(title);
        }
        cursor.close();
        db.close();

        deleteNotesAdapter.notifyDataSetChanged();
    }

    private void deleteSelectedNotes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int itemCount = deleteNotesList.size();

        for (int i = itemCount - 1; i >= 0; i--) {
            if (deleteNoteListView.isItemChecked(i)) {
                String selectedNote = deleteNotesList.get(i);

                String selection = NoteDatabaseHelper.COLUMN_TITLE + " LIKE ?";
                String[] selectionArgs = {selectedNote};

                db.delete(NoteDatabaseHelper.TABLE_NAME, selection, selectionArgs);
                deleteNotesList.remove(i);
            }
        }

        db.close();
        loadNotesFromDatabase();
        Toast.makeText(this, "Selected notes deleted", Toast.LENGTH_SHORT).show();
    }
}
