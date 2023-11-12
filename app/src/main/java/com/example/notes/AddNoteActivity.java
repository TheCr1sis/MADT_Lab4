package com.example.notes;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextNoteTitle;
    private EditText editTextNoteContent;
    private Button btnSaveNote;
    private NoteDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        editTextNoteContent = findViewById(R.id.editTextNoteContent);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        dbHelper = new NoteDatabaseHelper(this);

        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = editTextNoteTitle.getText().toString().trim();
                String noteContent = editTextNoteContent.getText().toString().trim();

                if (!noteTitle.isEmpty() && !noteContent.isEmpty()) {
                    saveNoteToDatabase(noteTitle, noteContent);
                    Toast.makeText(AddNoteActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddNoteActivity.this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveNoteToDatabase(String title, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHelper.COLUMN_TITLE, title);
        values.put(NoteDatabaseHelper.COLUMN_CONTENT, content);
        db.insert(NoteDatabaseHelper.TABLE_NAME, null, values);
        db.close();
    }
}
