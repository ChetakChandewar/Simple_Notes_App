package com.example.notes;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {

    private EditText editTitleEditText;
    private EditText editDescriptionEditText;
    private int notePosition;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editTitleEditText = findViewById(R.id.editTitleEditText);
        editDescriptionEditText = findViewById(R.id.editDescriptionEditText);

        Intent intent = getIntent();
        if (intent != null) {
            String originalNoteText = intent.getStringExtra("ORIGINAL_NOTE_TEXT");
            notePosition = intent.getIntExtra("NOTE_POSITION", -1);
            if (originalNoteText != null) {
                // Split the note into title and description
                String[] parts = originalNoteText.split("\n", 2);
                String title = parts[0];
                String description = (parts.length > 1) ? parts[1] : "";

                editTitleEditText.setText(title);
                editDescriptionEditText.setText(description);
            }
        }
    }

    public void saveChanges(View view) {
        String editedTitle = editTitleEditText.getText().toString();
        String editedDescription = editDescriptionEditText.getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("EDITED_TITLE", editedTitle);
        resultIntent.putExtra("EDITED_DESCRIPTION", editedDescription);
        resultIntent.putExtra("NOTE_POSITION", notePosition);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

