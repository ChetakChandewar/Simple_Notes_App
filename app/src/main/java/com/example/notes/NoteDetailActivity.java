package com.example.notes;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NoteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        TextView fullNoteTextView = findViewById(R.id.fullNoteTextView);

        Intent intent = getIntent();
        if (intent != null) {
            String selectedTitle = intent.getStringExtra("SELECTED_TITLE");
            String selectedDescription = intent.getStringExtra("SELECTED_DESCRIPTION");
            if (selectedTitle != null) {
                String selectedNote = selectedTitle + "\n";
                if (selectedDescription != null && !selectedDescription.isEmpty()) {
                    selectedNote += selectedDescription;
                }
                fullNoteTextView.setText(selectedNote);
            }
        }
    }
}
