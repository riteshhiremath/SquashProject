package riteshtechnology.squashpal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SelectedMatchActivity extends AppCompatActivity {

    TextView SMA_ToPlayer,SMA_FromPlayer,SMA_Location,SMA_Time;
    Button SMA_Accept;
    String selected_Object_id;
    String selected_match_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SMA_Accept = (Button)findViewById(R.id.B_SMA_Accept);
        SMA_ToPlayer = (TextView)findViewById(R.id.TV_SMA_ToPlayer);
        SMA_FromPlayer = (TextView)findViewById(R.id.TV_SMA_FromPlayer);
        SMA_Location = (TextView)findViewById(R.id.TV_SMA_Location);
        SMA_Time = (TextView)findViewById(R.id.TV_SMA_Time);

        selected_Object_id = getIntent().getStringExtra("ObjectId");
        //Toast.makeText(SelectedMatchActivity.this, selected_Object_id, Toast.LENGTH_SHORT).show();
        selected_match_status = getIntent().getStringExtra("Status");

        //If the match is accepted: Button gone if yes
        if(selected_match_status.equals("accepted")){
            SMA_Accept.setVisibility(View.GONE);
        }


        extractParseWithObjectId(selected_Object_id);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //Takes the object ID and extracts the Parse Match Object
    private void extractParseWithObjectId(String selected_Object_id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
        query.getInBackground(selected_Object_id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    System.out.println("Object has been found!!");
                    System.out.println(object.get("FromPlayer"));
                    SMA_FromPlayer.setText(object.get("FromPlayerToDisp").toString());
                    SMA_ToPlayer.setText(object.get("ToPlayerToDisp").toString());
                    SMA_Time.setText(object.get("Time").toString());
                    SMA_Location.setText(object.get("Location").toString());

                } else {
                    System.out.println("Object has not been found!!");
                }
            }
        });
    }

    public void setMatchStatus(View v){
        Log.d("Accept", "Clicked");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
            query.getInBackground(selected_Object_id, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        String currentStatus;
                        currentStatus = object.get("Status").toString();

                        //Consider switch case

                        if(currentStatus.equals("pending")){
                            object.put("Status", "accepted");
                            object.saveInBackground();
                            Toast.makeText(SelectedMatchActivity.this, "The match is accepted", Toast.LENGTH_SHORT).show();
                        }
                        else if(currentStatus.equals("accepted")){
                            Toast.makeText(SelectedMatchActivity.this, "This has already been accepted", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SelectedMatchActivity.this, "Sorry, this has been rejected!!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        System.out.println("Object has not been found!!");
                    }
                }
            });

    }


}
