package com.example.mai2.main_programme.activities.show_note.recyclers;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mai2.R;
import com.example.mai2.main_programme.algorithm.math.Buffer;

import java.util.Locale;

public class RecyclerResultAdapter
        extends RecyclerView.Adapter<RecyclerResultAdapter.ResultHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private final Buffer buffer;
    private final String[] criteriaNames;
    private final String[] candidatesNames;

    public RecyclerResultAdapter(Context context,
                                 Buffer buffer,
                                 String[] criteriaNames,
                                 String[] candidatesNames){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.buffer = buffer;
        this.criteriaNames = criteriaNames;
        this.candidatesNames = candidatesNames;
    }

    private View createOneViewElement(@NonNull ViewGroup parent){
        ViewGroup element =
                (ViewGroup) inflater.inflate(R.layout.recycler_container_layout, parent, false);
        LinearLayout container = element.findViewById(R.id.child_view_container);
        GradientDrawable back = (GradientDrawable) container.getBackground().mutate();
        back.setColor(context.getColor(R.color.design_gray));
        LinearLayout candidatesContainer = element.findViewById(R.id.candidates_container);
        for (String candidatesName : candidatesNames) {
            TableRow valueRow =
                    (TableRow) inflater.inflate(R.layout.row_result_layout, null);

            TextView candidateHeader = valueRow.findViewById(R.id.candidate_name_text);
            candidateHeader.setText(candidatesName);
            candidatesContainer.addView(valueRow);
        }

        return element;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = createOneViewElement(parent);

        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        double[] valueArray;
        try{
            valueArray = buffer.eachRelativeWeights.get(position);
            holder.criteriaName.setText(criteriaNames[position]);
        } catch (IndexOutOfBoundsException e){
            valueArray = buffer.finalRelativeWeights;
            holder.criteriaName.setText(R.string.final_rating_header);
        }

        for (int i = 0; i < holder.candidateContainer.getChildCount(); ++i){
            TableRow valueRow = (TableRow) holder.candidateContainer.getChildAt(i);
            TextView valueText = valueRow.findViewById(R.id.value_text);
            valueText.setText(String.format(Locale.CANADA, "%.5f", valueArray[i]));
        }
    }

    @Override
    public int getItemCount() {
        return criteriaNames.length + 1;
    }

    public static class ResultHolder extends RecyclerView.ViewHolder {
        public final LinearLayout candidateContainer;
        public final TextView criteriaName;
        public ResultHolder(@NonNull View itemView) {
            super(itemView);
            candidateContainer = itemView.findViewById(R.id.candidates_container);
            criteriaName = itemView.findViewById(R.id.criteria_header);
        }
    }
}
