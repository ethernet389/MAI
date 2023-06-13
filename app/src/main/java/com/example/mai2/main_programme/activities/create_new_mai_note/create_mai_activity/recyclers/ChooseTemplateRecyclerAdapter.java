package com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.recyclers;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;
import static com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.ChooseMAIConfigActivity.NAME_OF_CONFIG_KEY;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_new_mai_note.create_mai_activity.CreateMAIActivity;
import com.example.mai2.main_programme.db.database.AppDatabase;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;
import java.util.logging.LogRecord;

public class ChooseTemplateRecyclerAdapter
        extends RecyclerView.Adapter<ChooseTemplateRecyclerAdapter.ViewHolder> {
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
    }

    @Override
    public int getItemCount() {
        return MAIConfigsName.size();
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
                String name = MAIConfigsName.get(position);
                Intent intent = new Intent(context,
                        CreateMAIActivity.class);
                intent.putExtra(NAME_OF_CONFIG_KEY, name);
                context.startActivity(intent);
            });

            intentButton.setOnLongClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position == NO_POSITION) return false;
                Dialog dialog = new Dialog(context);
                String name = MAIConfigsName.get(position);
                dialog.setTitle(name);
                dialog.setContentView(R.layout.dialog_template_layout);
                TextView text = dialog.findViewById(R.id.template_feats);
                new Thread(){
                    @Override
                    public void run() {
                        String[] criteria = AppDatabase.getAppDatabase(context)
                                .getMAIConfigDao()
                                .getCriteriaByName(name);
                        criteria = new Gson().fromJson(criteria[0], String[].class);
                        Message msg = new Message();
                        msg.obj = criteria;
                        handler.sendMessage(msg);
                    }
                    @SuppressLint("HandlerLeak")
                    final Handler handler = new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            String[] criteria = (String[]) msg.obj;
                            text.append(context.getString(R.string.template_content_dialog_header));
                            for (int i = 0; i < criteria.length; ++i) {
                                String s = String.format(Locale.CANADA, "%d. %s\n", i + 1, criteria[i]);
                                text.append(s);
                            }
                        }
                    };
                }.start();
                dialog.show();
                return false;
            });

            deleteButton.setOnClickListener(listener -> {
                int position = getAbsoluteAdapterPosition();
                if (position == NO_POSITION) return;
                String name = MAIConfigsName.get(position);
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getAppDatabase(context);
                    db.getMAIConfigDao().deleteMAIConfigByName(name);
                }).start();
                MAIConfigsName.remove(position);
                notifyItemRemoved(position);
            });
        }
    }
}
