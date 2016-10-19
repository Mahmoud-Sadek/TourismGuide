package com.example.sadokey.tourism_guide.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sadokey.tourism_guide.Classes.Suggestion;
import com.example.sadokey.tourism_guide.R;

import java.util.List;

/**
 * Created by Ahmed AboMazin on 9/14/2016.
 */

public class SuggestionAdapter extends BaseAdapter {
    List<Suggestion> mylist;
    Context context;
    Activity activity;

    public SuggestionAdapter(Activity activity, Context context, List<Suggestion> mylist) {
        this.context = context;
        this.mylist = mylist;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View myView = activity.getLayoutInflater().inflate(R.layout.problem_approve, parent, false);

        ImageView img_person = (ImageView) myView.findViewById(R.id.imageView_Suggested_pesron);
        TextView txt_person_name = (TextView) myView.findViewById(R.id.txt_suggested_pesron_name);
        TextView txt_details = (TextView) myView.findViewById(R.id.edittext_Suggestion_subject);

        Suggestion item = mylist.get(position);
        //img_person.setImageURI(item.getImage());

        txt_person_name.setText(item.getName());
        txt_details.setText(item.getSubject());

        return myView;
    }

}
