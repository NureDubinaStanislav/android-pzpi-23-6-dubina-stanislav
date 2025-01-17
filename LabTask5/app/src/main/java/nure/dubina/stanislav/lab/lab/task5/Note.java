package nure.dubina.stanislav.lab.lab.task5;

import android.net.Uri;
import java.io.Serializable;

public class Note implements Serializable {
    private int id;
    private String title;
    private String description;
    private int importance;
    private String dateTime;
    private Uri imageUri;

    public Note(int id, String title, String description, int importance, String dateTime, Uri imageUri) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.importance = importance;
        this.dateTime = dateTime;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImportance() {
        return importance;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}