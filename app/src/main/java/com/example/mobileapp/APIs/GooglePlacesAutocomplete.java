package com.example.mobileapp.APIs;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.mobileapp.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GooglePlacesAutocomplete extends ArrayAdapter<AutocompletePrediction> implements Filterable {
    private List<AutocompletePrediction> resultList;
    private PlacesClient placesClient;
    private final Object lock = new Object();

    public GooglePlacesAutocomplete(Context context, int resource) {
        super(context, resource);
        Places.initialize(context, context.getString(R.string.google_maps_key));
        placesClient = Places.createClient(context);
        resultList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public AutocompletePrediction getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.autocomplete_place_item, parent, false);
        }

        AutocompletePrediction item = getItem(position);
        TextView textView = convertView.findViewById(R.id.autocomplete_text);

        if (item != null) {
            textView.setText(item.getPrimaryText(null).toString());
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    synchronized (lock) {
                        getAutocomplete(constraint);
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            return filterResults;
                        }
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private void getAutocomplete(CharSequence constraint) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(constraint.toString())
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            synchronized (lock) {
                resultList.clear();
                resultList.addAll(response.getAutocompletePredictions());
                lock.notify();
            }
        }).addOnFailureListener((exception) -> {
            synchronized (lock) {
                lock.notify();
            }
        });
    }
}