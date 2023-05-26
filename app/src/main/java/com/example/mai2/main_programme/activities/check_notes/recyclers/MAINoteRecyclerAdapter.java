package com.example.mai2.main_programme.activities.check_notes.recyclers;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mai2.R;
import com.example.mai2.main_programme.Constants;
import com.example.mai2.main_programme.activities.show_note.ResultActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;

import org.javatuples.Pair;

import java.util.ArrayList;

public class MAINoteRecyclerAdapter
        extends RecyclerView.Adapter<MAINoteRecyclerAdapter.ViewHolder>{
    private final ArrayList<Pair<String, String>> cursorList;
    private final Context context;
    private final LayoutInflater inflater;

    public MAINoteRecyclerAdapter(Context context, ArrayList<Pair<String, String>> cursorList){
        this.cursorList = cursorList;
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
        Pair<String, String> pair = cursorList.get(position);
        Log.d("position", "" + position);
        String name = pair.getValue0();
        Log.d("value0", name);
        String configName = pair.getValue1();

        holder.intentButton.setText(String.format("%s (%s)", name, configName));
    }

    @Override
    public int getItemCount() {
        return cursorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final Button intentButton;
        public final ImageView deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            intentButton = itemView.findViewById(R.id.logic_button);
            deleteButton = itemView.findViewById(R.id.delete_one_element);

            intentButton.setOnClickListener(listener -> {
                int position = getAbsoluteAdapterPosition();
                if (position == NO_POSITION) return;
                String name = cursorList.get(position).getValue0();
                Intent intent = new Intent(context,
                        ResultActivity.class);
                intent.putExtra(Constants.NOTE_KEY, name);
                context.startActivity(intent);
            });

            deleteButton.setOnClickListener(listener -> {
                int position = getAbsoluteAdapterPosition();
                if (position == NO_POSITION) return;
                String name = cursorList.get(position).getValue0();
                new Thread(){
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getAppDatabase(context);
                        db.getMAINoteDao().deleteMAINoteByName(name);
                    }
                }.start();
                cursorList.remove(position);
                notifyItemRemoved(position);
            });
        }
    }
}
