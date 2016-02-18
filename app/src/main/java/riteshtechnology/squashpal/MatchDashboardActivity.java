package riteshtechnology.squashpal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import riteshtechnology.squashpal.Adapters.MRAAdapter;

public class MatchDashboardActivity extends AppCompatActivity{

    ListView matchList;
    List<String> toPlayer = new ArrayList<>();
    List<String> fromPlayer = new ArrayList<>();
    List<String> matchStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_recieve);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Query from Parse
        matchInfoFromParse();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent takeToMCA = new Intent(MatchDashboardActivity.this,MatchCreateActivity.class);
                startActivity(takeToMCA);
            }
        });
    }

    //OR Query
    //Either sender or reciever is the player
    private void matchInfoFromParse(){

        ParseQuery<ParseObject> queryTo = ParseQuery.getQuery("Match");
        queryTo.whereEqualTo("ToPlayer", ParseUser.getCurrentUser().getUsername());

        ParseQuery<ParseObject> queryFrom = ParseQuery.getQuery("Match");
        queryFrom.whereEqualTo("FromPlayer", ParseUser.getCurrentUser().getUsername());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(queryTo);
        queries.add(queryFrom);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {
                    if (results != null) {
                        Log.d("Null check", "Not null");
                        if (results.size() > 0) {
                            Toast.makeText(MatchDashboardActivity.this, "Inside the query", Toast.LENGTH_SHORT).show();
                            System.out.println(results.size());

                            Toast.makeText(MatchDashboardActivity.this, "There is data", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MatchDashboardActivity.this, results.get(0).get("ToPlayer").toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(MatchDashboardActivity.this, results.get(0).getCreatedAt().toString(), Toast.LENGTH_SHORT).show();
                            for (ParseObject match : results) {
                                toPlayer.add(match.get("ToPlayer").toString());
                                fromPlayer.add(match.get("FromPlayer").toString());
                                matchStatus.add(match.get("Status").toString());
                            }

                            //Initialize List View
                            //Set adapter to ListView
                            //**Use Recycler View
                            displayInListView(results);
                        } else {
                            Toast.makeText(MatchDashboardActivity.this, "No data", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d("Null check", "null");

                    }
                }


            }//Done ends
        });


    }


    //Displaying the queried result from Parse
    //Handle onItemClick
    //Send the selected object to next activity
    private void displayInListView(final List<ParseObject> objects) {
        MRAAdapter adapter = new MRAAdapter(MatchDashboardActivity.this, toPlayer, fromPlayer,matchStatus);
        matchList = (ListView) findViewById(R.id.listView_matches);
        matchList.setAdapter(adapter);

        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = toPlayer.get(position);
                Toast.makeText(MatchDashboardActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
                String selected_Object_id = objects.get(position).getObjectId();
                System.out.println(selected_Object_id);
                Intent takeToSMA = new Intent(MatchDashboardActivity.this, SelectedMatchActivity.class);
                takeToSMA.putExtra("ObjectId", selected_Object_id);
                takeToSMA.putExtra("Status", matchStatus.get(position));
                startActivity(takeToSMA);
            }
        });
    }

    //Optional - Not in use
    //Use this to query from either To or From Player
    private void matchInfoFromParseUsingToPlayer() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
        query.whereEqualTo("ToPlayer", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        Toast.makeText(MatchDashboardActivity.this, "There is data", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MatchDashboardActivity.this, objects.get(0).get("ToPlayer").toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MatchDashboardActivity.this, objects.get(0).getCreatedAt().toString(), Toast.LENGTH_SHORT).show();
                        for (ParseObject match : objects) {
                            toPlayer.add(match.get("ToPlayer").toString());
                            fromPlayer.add(match.get("FromPlayer").toString());
                            matchStatus.add(match.get("Status").toString());
                        }

                        //Initialize List View
                        //Set adapter to ListView
                        //**Use Recycler View
                        displayInListView(objects);

                    } else {
                        Toast.makeText(MatchDashboardActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Something went wrong.
                }
            }
        });
    }









}
