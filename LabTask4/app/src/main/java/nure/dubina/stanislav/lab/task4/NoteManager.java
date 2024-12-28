package nure.dubina.stanislav.lab.task4;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class NoteManager {

    private static final List<Note> notesList = new ArrayList<>();

    public static int addNote(String title, String description, int importance, String dateTime, String imageUri) {
        int id = notesList.size() + 1;
        Uri uri = Uri.parse(imageUri);
        Note note = new Note(id, title, description, importance, dateTime, uri);
        notesList.add(note);
        return id;
    }

    public static List<Note> getAllNotes() {
        return new ArrayList<>(notesList);
    }

    public static int updateNote(int id, String title, String description, int importance, String dateTime, String imageUri) {
        for (int i = 0; i < notesList.size(); i++) {
            Note note = notesList.get(i);
            if (note.getId() == id) {
                Uri uri = Uri.parse(imageUri);
                notesList.set(i, new Note(id, title, description, importance, dateTime, uri));
                return 1;
            }
        }
        return 0;
    }

    public static void deleteNote(int id) {
        notesList.removeIf(note -> note.getId() == id);
    }
}