package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

/**
 * Activity to search for user input in list of thing to do. List items is clickable and start the activity
 * for search nearby locations of the selected type.
 */
public class SelectWhatToDoActivity extends AppCompatActivity {

    private static String TAG = SelectWhatToDoActivity.class.toString();

    // Components
    FloatingActionButton backButton;
    ListView searchResultList;
    EditText searchInput;

    // Search result components
    List<SearchResult> searchResults;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_what_to_do);

        populateList();
        initComponents();
    }



    @Override
    protected void onResume() {
        populateList();
        initComponents();
        super.onResume();
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

        initTextFieldInputListener();
    }

    private void initTextFieldInputListener() {
        // TODO. Check if this is software related. Not sure if software "keyboards" fire this event.
        searchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String searchWord = searchInput.getText().toString();
                Log.d(TAG, "Keyevent fired. Searching for text: " + searchWord);
                search(searchWord);
                return true;
            }
        });
    }

    /*
    Method to call for one an character is entered in search field.
     */
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
        adapter.notifyDataSetChanged();
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
