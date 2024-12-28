package nure.dubina.stanislav.lab.lab.task5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotesListFragment extends Fragment implements NoteAdapter.OnNoteClickListener {

    private RecyclerView notesRecyclerView;
    private NoteAdapter notesAdapter;
    private final ArrayList<Note> allNotes = new ArrayList<>();
    private final ArrayList<Note> filteredNotes = new ArrayList<>();
    private boolean sortByImportanceAscending = true;
    private boolean sortByTitleAscending = true;
    private float textSize = 14f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        initializeComponents(view);
        setupRecyclerView();
        loadNotes();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterNotes(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterNotes(newText);
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            toggleSortByImportance();
            return true;
        } else if (item.getItemId() == R.id.action_sort_az) {
            toggleSortByTitle();
            return true;
        } else if (item.getItemId() == R.id.action_theme_toggle) {
            toggleTheme();
            return true;
        } else if (item.getItemId() == R.id.action_text_size) {
            toggleTextSize();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme() {
        boolean isNightMode = (getResources().getConfiguration().uiMode &
                android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES;

        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        requireActivity().recreate();
    }

    private void toggleTextSize() {
        textSize = (textSize == 14f) ? 18f : 14f;
        notesAdapter.setTextSize(textSize);
        notesAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Розмір шрифту: " + textSize + "sp", Toast.LENGTH_SHORT).show();
    }

    private void filterNotes(String query) {
        filteredNotes.clear();
        String lowerCaseQuery = query.toLowerCase();
        for (Note note : allNotes) {
            if (note.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                    note.getDescription().toLowerCase().contains(lowerCaseQuery)) {
                filteredNotes.add(note);
            }
        }
        notesAdapter.notifyDataSetChanged();
    }

    private void initializeComponents(View view) {
        notesRecyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton addNoteButton = view.findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(v -> openAddNoteFragment());
    }

    private void setupRecyclerView() {
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notesAdapter = new NoteAdapter(getContext(), filteredNotes, this);
        notesAdapter.setTextSize(textSize);
        notesRecyclerView.setAdapter(notesAdapter);
    }

    private void loadNotes() {
        NoteDBHelper dbHelper = new NoteDBHelper(getContext());

        allNotes.clear();
        allNotes.addAll(dbHelper.getAllNotes());
        filteredNotes.clear();
        filteredNotes.addAll(allNotes);
        notesAdapter.notifyDataSetChanged();
    }

    private void toggleSortByImportance() {
        List<Note> targetList = filteredNotes.isEmpty() ? allNotes : filteredNotes;
        if (sortByImportanceAscending) {
            Collections.sort(targetList, (n1, n2) -> Integer.compare(n1.getImportance(), n2.getImportance()));
            Toast.makeText(getContext(), "Sorted by importance (ascending)", Toast.LENGTH_SHORT).show();
        } else {
            Collections.sort(targetList, (n1, n2) -> Integer.compare(n2.getImportance(), n1.getImportance()));
            Toast.makeText(getContext(), "Sorted by importance (descending)", Toast.LENGTH_SHORT).show();
        }
        sortByImportanceAscending = !sortByImportanceAscending;
        notesAdapter.notifyDataSetChanged();
    }

    private void toggleSortByTitle() {
        List<Note> targetList = filteredNotes.isEmpty() ? allNotes : filteredNotes;
        if (sortByTitleAscending) {
            Collections.sort(targetList, (n1, n2) -> n1.getTitle().compareTo(n2.getTitle()));
            Toast.makeText(getContext(), "Sorted by title (A-Z)", Toast.LENGTH_SHORT).show();
        } else {
            Collections.sort(targetList, (n1, n2) -> n2.getTitle().compareTo(n1.getTitle()));
            Toast.makeText(getContext(), "Sorted by title (Z-A)", Toast.LENGTH_SHORT).show();
        }
        sortByTitleAscending = !sortByTitleAscending;
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(Note note) {
        openEditNoteFragment(note);
    }

    @Override
    public void onEditNoteClick(Note note, int position) {
        openEditNoteFragment(note);
    }

    @Override
    public void onDeleteNoteClick(Note note, int position) {
        NoteDBHelper dbHelper = new NoteDBHelper(getContext());

        dbHelper.deleteNote(note.getId());
        loadNotes();
        Toast.makeText(getContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private void openAddNoteFragment() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new AddNoteFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openEditNoteFragment(Note note) {
        Bundle bundle = new Bundle();
        bundle.putInt("note_id", note.getId());
        bundle.putString("note_title", note.getTitle());
        bundle.putString("note_description", note.getDescription());
        bundle.putInt("note_importance", note.getImportance());
        bundle.putString("note_date_time", note.getDateTime());
        bundle.putString("note_image_uri", note.getImageUri() != null ? note.getImageUri().toString() : null);

        EditNoteFragment fragment = new EditNoteFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
