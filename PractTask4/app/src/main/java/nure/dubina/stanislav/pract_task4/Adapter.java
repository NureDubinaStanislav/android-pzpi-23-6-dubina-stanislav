package nure.dubina.stanislav.pract_task4;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView ageTextView;

        public ViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.name);
            ageTextView = v.findViewById(R.id.age);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);

        holder.nameTextView.setText(user.getName());
        holder.ageTextView.setText(Integer.toString(user.getAge()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}