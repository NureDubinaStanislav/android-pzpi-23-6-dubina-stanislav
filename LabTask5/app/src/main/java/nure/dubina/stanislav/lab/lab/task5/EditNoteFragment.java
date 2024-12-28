package nure.dubina.stanislav.lab.lab.task5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditNoteFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText titleText, descriptionText;
    private Spinner importanceSpinner;
    private Button updateButton, deleteButton, selectImageButton;
    private ImageView noteImageView;
    private Uri imageUri;
    private Note note;
    private int noteId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        initializeComponents(view);
        loadNoteData();
        return view;
    }

    private void initializeComponents(View view) {
        titleText = view.findViewById(R.id.titleNote);
        descriptionText = view.findViewById(R.id.descriptionNote);
        importanceSpinner = view.findViewById(R.id.importance);
        updateButton = view.findViewById(R.id.updateButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        selectImageButton = view.findViewById(R.id.selectButton);
        noteImageView = view.findViewById(R.id.noteImage);

        selectImageButton.setOnClickListener(v -> openGallery());
        updateButton.setOnClickListener(v -> updateNote());
        deleteButton.setOnClickListener(v -> deleteNote());
    }

    private void updateNote() {
        NoteDBHelper dbHelper = new NoteDBHelper(getContext());

        String title = titleText.getText().toString().trim();
        String description = descriptionText.getText().toString().trim();
        int importance = importanceSpinner.getSelectedItemPosition();
        String dateTime = getCurrentDateTime();
        String imageUriString = (imageUri != null) ? imageUri.toString() : "";

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Заповніть усі поля", Toast.LENGTH_SHORT).show();
        } else {
            Note note = new Note(noteId, title, description, importance, dateTime, Uri.parse(imageUriString));
            int rowsUpdated = dbHelper.updateNote(note);

            if (rowsUpdated > 0) {
                Toast.makeText(getContext(), "Нотатка оновлена", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Помилка оновлення", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadNoteData() {
        Bundle args = getArguments();
        if (args != null) {
            noteId = args.getInt("note_id");
            String title = args.getString("note_title");
            String description = args.getString("note_description");
            int importance = args.getInt("note_importance");
            String dateTime = args.getString("note_date_time");
            String imageUriString = args.getString("note_image_uri");

            imageUri = imageUriString != null ? Uri.parse(imageUriString) : null;
            note = new Note(noteId, title, description, importance, dateTime, imageUri);

            titleText.setText(note.getTitle());
            descriptionText.setText(note.getDescription());
            importanceSpinner.setSelection(note.getImportance());
            noteImageView.setImageURI(note.getImageUri());
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private Uri saveImageToInternalStorage(Uri sourceUri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(sourceUri);

            File directory = new File(getContext().getFilesDir(), "images");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
            File file = new File(directory, fileName);

            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Помилка збереження зображення", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            Uri savedImageUri = saveImageToInternalStorage(selectedImageUri);
            if (savedImageUri != null) {
                imageUri = savedImageUri;
                noteImageView.setImageURI(imageUri);
            }
        }
    }

    private void deleteNote() {
        new NoteDBHelper(requireContext()).deleteNote(note.getId());
        Toast.makeText(getContext(), "Нотатка видалена", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
