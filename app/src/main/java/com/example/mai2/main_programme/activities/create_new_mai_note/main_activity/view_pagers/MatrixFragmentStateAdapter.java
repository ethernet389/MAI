package com.example.mai2.main_programme.activities.create_new_mai_note.main_activity.view_pagers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mai2.R;
import com.example.mai2.main_programme.activities.create_new_mai_note.main_activity.fragments.MatrixFragment;

public class MatrixFragmentStateAdapter extends FragmentStateAdapter {
    private final String[] criteria;
    private final String[] candidates;
    private final Context context;

    public MatrixFragmentStateAdapter(@NonNull FragmentManager fragmentManager,
                                      @NonNull Lifecycle lifecycle,
                                      Context context,
                                      String[] criteria,
                                      String[] candidates) {
        super(fragmentManager, lifecycle);
        this.context = context;
        this.criteria = criteria;
        this.candidates = candidates;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            String title = context.getString(R.string.criteria_relative);
            return new MatrixFragment(criteria, title);
        }
        String title = context.getString(R.string.command_text_template) + criteria[position - 1];
        return new MatrixFragment(candidates, title);
    }

    @Override
    public int getItemCount() {
        return criteria.length + 1;
    }
}
