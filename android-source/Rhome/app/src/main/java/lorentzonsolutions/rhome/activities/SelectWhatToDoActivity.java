package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.googleWebApi.GoogleLocationTypes;
import lorentzonsolutions.rhome.interfaces.RhomeActivity;
import lorentzonsolutions.rhome.utils.Resources;

/**
 * Activity to search for user input in list of thing to do. List items is clickable and start the activity
 * for search nearby locations of the selected type.
 *
 * @author Johan Lorentzon
 */
public class SelectWhatToDoActivity extends AppCompatActivity implements RhomeActivity {

    // TODO. Make the input field responsive. The result should appear live without having to click the search button.

    private static String TAG = SelectWhatToDoActivity.class.toString();

    FloatingActionButton backButton;
    ListView searchResultList;
    TextInputEditText searchInput;
    Button searchButton;
    Button showListOfTypesButton;

    List<SearchResult> searchResults = new ArrayList<>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_what_to_do);

        populateList();
        assignViews();
        initEvents();
        hideSoftKeyboard();
    }

    @Override
    protected void onResume() {
        populateList();
        assignViews();
        initEvents();
        hideSoftKeyboard();
        super.onResume();
    }

    @Override
    public void initEvents() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new SearchResultListAdapter(this, searchResults);
        searchResultList.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchWord = searchInput.getText().toString();
                Log.d(TAG, "Search fired. Searching for text: " + searchWord);
                search(searchWord);
            }
        });

        searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult itemClicked = (SearchResult) parent.getItemAtPosition(position);

                Log.d(TAG, "Type selected: " + itemClicked.type.getAsGoogleType() + "\n Value string: " + itemClicked.value);

                Intent intent = new Intent(Resources.getInstance().getContext(), ListNearbyPlacesActivity.class);
                intent.putExtra("selected_type", itemClicked.type.getAsGoogleType());
                startActivity(intent);

            }
        });

        showListOfTypesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resources.getInstance().getContext(), ListLocationTypeSelectionActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void assignViews() {
        backButton = (FloatingActionButton) findViewById(R.id.what_to_do_floating_back_button);
        searchResultList = (ListView) findViewById(R.id.what_to_do_search_result_list);
        searchInput = (TextInputEditText) findViewById(R.id.what_to_do_search_field);
        searchButton = (Button) findViewById(R.id.what_to_do_search_button);
        showListOfTypesButton = (Button) findViewById(R.id.what_to_do_list_types_button);

        searchInput.clearFocus();

    }

    private void populateList() {
        searchResults.clear();
        for (Map.Entry e: GoogleLocationTypes.mapOfWhatToDo.entrySet()) {
            searchResults.add(new SearchResult((GoogleLocationTypes) e.getKey(), (String) e.getValue()));
        }
    }

    /**
     * Method matches the word with any value in the map of things to do. Updates the list and notifies the adapter.
     *
     * @param word
     */
    private void search(String word) {
        searchResults.clear();
        if(word.equals("")) populateList();
        else {
            for (Map.Entry e : GoogleLocationTypes.mapOfWhatToDo.entrySet()) {
                if ((((String) e.getValue()).toLowerCase()).contains(word.toLowerCase())) {
                    searchResults.add(
                            new SearchResult((GoogleLocationTypes) e.getKey(), (String) e.getValue()));
                }
            }
        }
        hideSoftKeyboard();
        if(searchResults.size() == 0) makeSnackBar("No results...");
        else makeSnackBar("Found " + searchResults.size() + " matches.");
        adapter.notifyDataSetChanged();
    }

    /**
     * Shows a snack bar to the user with given message.
     *
     * @param message
     */
    private void makeSnackBar(String message) {
        Snackbar mySnackbar = Snackbar.make(getWindow().getDecorView(),
                message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

    /**
     * Method for hiding the input keyboard on the users screen.
     *
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    class SearchResult {

        SearchResult(GoogleLocationTypes type, String value) {
            this.type = type;
            this.value = value;
        }
        GoogleLocationTypes type;
        String value;
    }

    /**
     * Inner class for list adapter.
     */
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
