package com.emirhalici.myenglishdictionary.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
//import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emirhalici.myenglishdictionary.utils.DatabaseHelper;
import com.emirhalici.myenglishdictionary.R;
import com.emirhalici.myenglishdictionary.models.WordModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;



public class AddWordAdapter extends RecyclerView.Adapter<AddWordAdapter.eViewHolder>{

    public static ArrayList<WordModel> mWordList;
    public static Context context;

    public static class eViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_type;
        public TextView tv_word;
        public TextView tv_definition;

        public eViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_word = (TextView) itemView.findViewById(R.id.tv_word);
            tv_definition = (TextView) itemView.findViewById(R.id.tv_definition);
            itemView.setOnClickListener(v -> {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                String alertTitle = context.getResources().getString(R.string.AddWordAlertDialogTitle);
                dialogBuilder.setTitle(alertTitle);

                String word = mWordList.get(getAdapterPosition()).getWord();
                String alertMessage = context.getResources().getString(R.string.AddWordAlertDialogMessage, word);
                dialogBuilder.setMessage(alertMessage);

                dialogBuilder.setPositiveButton(context.getResources().getString(R.string.Yes), (dialog, which) -> {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    databaseHelper.addOne(mWordList.get(getAdapterPosition()));
                    Toast.makeText(context, context.getResources().getString(R.string.AddWordSuccess), Toast.LENGTH_SHORT).show();
                });

                dialogBuilder.setNegativeButton(context.getResources().getString(R.string.No), (dialog, which) -> {

                });
                dialogBuilder.show();
            });
        }
    }

    public AddWordAdapter(ArrayList<WordModel> exampleList, Context context) {
        mWordList = exampleList;
        this.context = context;
    }

    @NonNull
    @Override
    public eViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_item_card, parent, false);
        eViewHolder evh = new eViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull eViewHolder holder, int position) {
        WordModel currentItem = mWordList.get(position);
        holder.tv_definition.setText(currentItem.getDefinition());
        holder.tv_type.setText(currentItem.getType());
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

}

