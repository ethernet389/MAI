package com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.recyclers;

import static com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.ChooseMAIConfigActivity.NAME_OF_CONFIG_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.CreateMAIActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;

import java.util.List;

public class ChooseTemplateRecyclerAdapter
        extends RecyclerView.Adapter<ChooseTemplateRecyclerAdapter.ViewHolder>{
    private final List<String> MAIConfigsName;
    private final Context context;
    private final LayoutInflater inflater;

    public ChooseTemplateRecyclerAdapter(Context context, List<String> MAIConfigsName){
        this.MAIConfigsName = MAIConfigsName;
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
        String MAIConfigName = MAIConfigsName.get(position);

        holder.intentButton.setText(MAIConfigName);
        holder.intentButton.setOnClickListener(listener -> {
            Intent intent = new Intent(context,
                    CreateMAIActivity.class);
            intent.putExtra(NAME_OF_CONFIG_KEY, MAIConfigName);
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(listener -> {
            new Thread(){
                @Override
                public void run() {
                    AppDatabase db = AppDatabase.getAppDatabase(context);
                    db.getMAIConfigDao().deleteMAIConfigByName(MAIConfigName);
                }
            }.start();
            MAIConfigsName.remove(MAIConfigName);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return MAIConfigsName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final View parentView;
        public final Button intentButton;
        public final ImageView deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentView = itemView;
            intentButton = itemView.findViewById(R.id.logic_button);
            deleteButton = itemView.findViewById(R.id.delete_one_element);
        }
    }
}
