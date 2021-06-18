package com.emirhalici.myenglishdictionary.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emirhalici.myenglishdictionary.utils.DatabaseHelper;
import com.emirhalici.myenglishdictionary.fragments.QuizEndFragment;
import com.emirhalici.myenglishdictionary.R;
import com.emirhalici.myenglishdictionary.models.WordModel;
import com.emirhalici.myenglishdictionary.fragments.AddMFragment;
import com.emirhalici.myenglishdictionary.fragments.DisplayWordFragment;
import com.emirhalici.myenglishdictionary.fragments.HomeFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;


public class  AllWordsAdapter extends RecyclerView.Adapter<AllWordsAdapter.eViewHolder> implements Filterable {

    boolean CardType;
    public static ArrayList<WordModel> mWordList;
    public static Context context;

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class eViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_type;
        public TextView tv_word;
        public TextView tv_definition;
        public TextView tv_example;


        public eViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_word = (TextView) itemView.findViewById(R.id.tv_word);
            tv_definition = (TextView) itemView.findViewById(R.id.tv_definition);
            tv_example = (TextView) itemView.findViewById(R.id.tv_example);

            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            Fragment myFragment = manager.findFragmentById(R.id.fragment_container);
            if (myFragment instanceof QuizEndFragment) {
                itemView.setClickable(false);
                itemView.setLongClickable(false);
            } else {
                itemView.setLongClickable(true);

                itemView.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        MaterialAlertDialogBuilder dialogBuilderMain = new MaterialAlertDialogBuilder(context);
                        String alertTitle = context.getResources().getString(R.string.AddWordAlertDialogTitleMain);
                        dialogBuilderMain.setTitle(alertTitle);
                        String alertMessage = context.getString(R.string.AddWordAlertDialogMessageMain, mWordList.get(getAdapterPosition()).getWord());
                        dialogBuilderMain.setMessage(alertMessage);
                        dialogBuilderMain.setPositiveButton(context.getString(R.string.AddWordAlertDialogEdit), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // on clicked edit
                                AppCompatActivity activity = (AppCompatActivity) context;
                                int wordid = mWordList.get(getAdapterPosition()).getId();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, AddMFragment.newInstance(wordid)).addToBackStack(null).commit();

                            }
                        });
                        dialogBuilderMain.setNegativeButton(context.getString(R.string.AddWordAlertDialogDelete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // on clicked delete
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
                                String alertTitleS = context.getResources().getString(R.string.AddWordAlertDialogTitle);
                                dialogBuilder.setTitle(alertTitleS);
                                String alertMessageS = context.getResources().getString(R.string.AddWordAlertDialogDeleteMessage, mWordList.get(getAdapterPosition()).getWord());
                                dialogBuilder.setMessage(alertMessageS);
                                dialogBuilder.setPositiveButton(context.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                                        boolean response = databaseHelper.deleteOne(mWordList.get(getAdapterPosition()));
                                        String toastString = (response ? context.getString(R.string.AddWordAlertDialogDeleteFail) : context.getString(R.string.AddWordAlertDialogDeleteSuccess));
                                        Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show();
                                        AppCompatActivity activity = (AppCompatActivity) context;
                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();

                                    }});
                                dialogBuilder.setNegativeButton(context.getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // code for the click no
                                    }});
                                dialogBuilder.show();


                            }
                        });
                        dialogBuilderMain.setNeutralButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // on clicked cancel
                            }
                        });
                        dialogBuilderMain.show();
                        return true;
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) context;
                        WordModel wordModel = mWordList.get(getAdapterPosition());
                        String word = wordModel.getWord();
                        String definition = wordModel.getDefinition();
                        String type = wordModel.getType();
                        String example = wordModel.getExample();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, DisplayWordFragment.newInstance(wordModel.getId(),word, type, example, definition)).addToBackStack(null).commit();
                    }
                });

            }



        }
    }

    public AllWordsAdapter(ArrayList<WordModel> exampleList, Context context) {
        mWordList = exampleList;
        this.context = context;
    }

    @NonNull
    @Override
    public eViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //public String card = sharedPreferences.getString("sortPreference", "date_asc");
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        Fragment myFragment = manager.findFragmentById(R.id.fragment_container);
        View v;
        if (!(myFragment instanceof QuizEndFragment)) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            CardType = sharedPreferences.getBoolean("CardType", true);
            if (CardType) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_card, parent, false);
            }
            else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.compact_card, parent, false);
            }
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_card, parent, false);
        }

        eViewHolder evh = new eViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull eViewHolder holder, int position) {
        WordModel currentItem = mWordList.get(position);
        holder.tv_definition.setText(currentItem.getDefinition());
        holder.tv_type.setText(currentItem.getType());
        holder.tv_word.setText(currentItem.getWord());
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        Fragment myFragment = manager.findFragmentById(R.id.fragment_container);
        if (myFragment instanceof QuizEndFragment || CardType) {
            holder.tv_example.setText(currentItem.getExample());
        }

    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }




}

