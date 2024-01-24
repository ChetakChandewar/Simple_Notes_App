package com.example.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> notesAdapter;
    private final List<String> notesList = new ArrayList<>();
    private int noteCounter = 1;

    public static final int CREATE_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        notesAdapter = new ArrayAdapter<String>(this, R.layout.note_item_layout, R.id.noteTitleTextView, notesList) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.noteTitleTextView);

                // Split the note into title and description
                String[] parts = getItem(position).split("\n", 2);
                String title = parts[0];
                textView.setText(title);

                return view;
            }
        };

        listView.setAdapter(notesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedNote = notesList.get(position);

                // Launch NoteDetailActivity to display full details
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                // Split the note into title and description
                String[] parts = selectedNote.split("\n", 2);
                String title = parts[0];
                String description = (parts.length > 1) ? parts[1] : "";
                intent.putExtra("SELECTED_TITLE", title);
                intent.putExtra("SELECTED_DESCRIPTION", description);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedNote = notesList.get(position);
                showOptionsDialog(selectedNote);
                return true;
            }
        });
    }

    private void showOptionsDialog(final String note) {
        String[] options = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                editNote(note);
                                break;
                            case 1:
                                deleteNote(note);
                                break;
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void editNote(String note) {
        // Launch the EditNoteActivity for editing the note
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra("ORIGINAL_NOTE_TEXT", note);
        intent.putExtra("NOTE_POSITION", notesList.indexOf(note));
        startActivityForResult(intent, EDIT_NOTE_REQUEST);
    }

    // Change in the deleteNote method
    private void deleteNote(String note) {
        int position = notesList.indexOf(note);
        if (position >= 0 && position < notesList.size()) {
            notesList.remove(note);
            updateNoteNumbers(position + 1); // Pass the position to start updating numbers from
            notesAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Deleted: " + note, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show();
        }
    }

    // Updated method to correctly update note numbers
    private void updateNoteNumbers(int startNumber) {
        for (int i = startNumber; i <= notesList.size(); i++) {
            String note = notesList.get(i - 1);
            String[] parts = note.split("\\.", 2);  // Split using dot to get the existing number
            String updatedNote = i + ". " + parts[1] + "\n";  // Use the existing title and description
            notesList.set(i - 1, updatedNote);
        }
    }

    public void createNewNote(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivityForResult(intent, CREATE_NOTE_REQUEST);
    }

    // Updated onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra("NOTE_TITLE");
            String description = data.getStringExtra("NOTE_DESCRIPTION");

            // Find the maximum note number currently in the list
            int maxNoteNumber = 0;
            for (String note : notesList) {
                String[] parts = note.split("\\.", 2);
                int noteNumber = Integer.parseInt(parts[0]);
                if (noteNumber > maxNoteNumber) {
                    maxNoteNumber = noteNumber;
                }
            }

            // Increment the noteCounter based on the maximum note number
            noteCounter = maxNoteNumber + 1;

            String note = noteCounter + ". " + title + "\n"; // You can modify the format as needed
            if (!description.isEmpty()) {
                note += description;
            }

            notesList.add(note);
            notesAdapter.notifyDataSetChanged();

            // Increment the noteCounter for the next note
            noteCounter++;
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            String editedTitle = data.getStringExtra("EDITED_TITLE");
            String editedDescription = data.getStringExtra("EDITED_DESCRIPTION");
            int position = data.getIntExtra("NOTE_POSITION", -1);

            if (position != -1 && position < notesList.size()) {
                // Get the existing note
                String existingNote = notesList.get(position);

                // Split the existing note into title and description
                String[] parts = existingNote.split("\n", 2);
                String existingTitle = parts[0];
                String existingDescription = (parts.length > 1) ? parts[1] : "";

                // Check if the title is different after editing
                if (!existingTitle.equals(editedTitle)) {
                    // Update the title and description
                    String editedNote = (position + 1) + ". " + editedTitle + "\n";
                    if (!editedDescription.isEmpty()) {
                        editedNote += editedDescription;
                    }
                    notesList.set(position, editedNote);
                    notesAdapter.notifyDataSetChanged();
                } else {
                    // Only the description is changed, so update it
                    notesList.set(position, existingTitle + "\n" + editedDescription);
                    notesAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
