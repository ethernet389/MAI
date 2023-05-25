package com.example.mai2.main_programme.activities.check_mai_activity.recyclers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.activities.create_mai_activity.CreateMAIActivity;
import com.example.mai2.main_programme.activities.result_activity.ResultActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;
import com.example.mai2.main_programme.db.tables.mai_note.MAINote;

import java.util.List;

public class MAINoteRecyclerAdapter
        extends RecyclerView.Adapter<MAINoteRecyclerAdapter.ViewHolder>{
    private final List<String> names, configNames;
    private final Context context;
    private final LayoutInflater inflater;

    public MAINoteRecyclerAdapter(Context context,
                                  List<String> names,
                                  List<String> configNames){
        this.names = names;
        this.configNames = configNames;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View element =
                inflater.inflate(R.layout.one_choose_element, parent, false);
        return new ViewHolder(element);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = names.get(position);
        String configName = configNames.get(position);

        holder.intentButton.setText(String.format("%s (%s)", name, configName));
        holder.intentButton.setOnClickListener(listener -> {
            Intent intent = new Intent(context,
                    ResultActivity.class);
            intent.putExtra(Constants.NOTE_KEY, name);
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(listener -> {
            new Thread(){
                @Override
                public void run() {
                    AppDatabase db = AppDatabase.getAppDatabase(context);
                    db.getMAINoteDao().deleteMAINoteByName(name);
                }
            }.start();
            names.remove(name);
            configNames.remove(configName);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final Button intentButton;
        public final ImageView deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            intentButton = itemView.findViewById(R.id.logic_button);
            deleteButton = itemView.findViewById(R.id.delete_one_element);
        }
    }
}
