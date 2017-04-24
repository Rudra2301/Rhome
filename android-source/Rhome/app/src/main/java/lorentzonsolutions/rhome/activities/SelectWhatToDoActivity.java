package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.shared.GoogleLocationTypes;

public class SelectWhatToDoActivity extends AppCompatActivity {

    // Components
    FloatingActionButton backButton;
    ListView searchResultList;
    EditText searchInput;

    // Searchresultlist
    List<SearchResult> searchResults;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_what_to_do);

        populateList();
        initComponents();
    }

    private void populateList() {
        for (Map.Entry e: GoogleLocationTypes.mapOfWhatToDo.entrySet()) {
            searchResults.add(new SearchResult((GoogleLocationTypes) e.getKey(), (String) e.getValue()));
        }
    }

    private void initComponents() {
        backButton = (FloatingActionButton) findViewById(R.id.what_to_do_floating_back_button);
        searchResultList = (ListView) findViewById(R.id.what_to_do_search_result_list);
        searchInput = (EditText) findViewById(R.id.what_to_do_search_field);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new SearchResultListAdapter(this, searchResults);
        searchResultList.setAdapter(adapter);
    }

    private void search(String word) {
        searchResults.clear();
        if(word.equals("")) populateList();
        else {
            for (Map.Entry e : GoogleLocationTypes.mapOfWhatToDo.entrySet()) {
                if (word.toLowerCase().contains(((String) e.getValue()).toLowerCase())) {
                    searchResults.add(
                            new SearchResult((GoogleLocationTypes) e.getKey(), (String) e.getValue()));
                }
            }
        }
        // TODO. Fix adapter
        // adapter.notifyDataSetChanged();

    }

    class SearchResult {

        SearchResult(GoogleLocationTypes type, String value) {
            this.type = type;
            this.value = value;
        }
        GoogleLocationTypes type;
        String value;
    }

    // Adapter for list
    class SearchResultListAdapter extends ArrayAdapter<SearchResult> {

        public SearchResultListAdapter(Context context, List<SearchResult> placesList) {
            super(context, 0, placesList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Get the data item for this position
            SearchResult listItem = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_search_result_what_to_do, parent, false);

            // Components in list item
            LinearLayout itemLayout = (LinearLayout) convertView.findViewById(R.id.what_to_do_search_result_item);
            TextView itemName = (TextView) convertView.findViewById(R.id.what_to_do_search_result_name);

            itemName.setText(listItem.value);

            return convertView;
        }

    }
}
