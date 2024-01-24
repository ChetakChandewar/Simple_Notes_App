package com.example.notes;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
    }

    public void saveNote(View view) {
        String noteTitle = titleEditText.getText().toString();
        String noteDescription = descriptionEditText.getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("NOTE_TITLE", noteTitle);
        resultIntent.putExtra("NOTE_DESCRIPTION", noteDescription);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
