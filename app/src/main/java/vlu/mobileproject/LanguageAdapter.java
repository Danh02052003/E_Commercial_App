package vlu.mobileproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class LanguageAdapter extends ArrayAdapter<LanguageItem> {

    public LanguageAdapter(Context context, ArrayList<LanguageItem> languageList) {
        super(context, 0, languageList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LanguageItem languageItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_listview_language, parent, false);
        }

        // Lookup view for data population
        TextView languageName = convertView.findViewById(R.id.languageName);
        ImageView flagImage = convertView.findViewById(R.id.flagImage);

        // Populate the data into the template view using the data object
        languageName.setText(languageItem.name());
        flagImage.setImageResource(languageItem.flagResource());

        // Return the completed view to render on screen
        return convertView;
    }
}
