package riteshtechnology.squashpal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MatchCreateActivity extends AppCompatActivity {


    //@InjectView(R.id.palName) EditText palName;
    EditText palName,matchLocation,matchTime;
    Button searchPlayer, sendInvite;
    List<ParseUser> allUsers = new ArrayList<>();
    String ToUserName; //For sending push notification
    String ToDisplayName;
    String FromDisplayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        palName = (EditText)findViewById(R.id.palName);
        matchLocation = (EditText)findViewById(R.id.ETmatchLoc);
        matchTime = (EditText)findViewById(R.id.ETmatchTime);
        sendInvite = (Button)findViewById(R.id.btn_sendInvite);




        Log.d("Testing name",palName.toString());
        searchPlayer = (Button)findViewById(R.id.btn_searchPlayer);


        searchPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayMatchingUsers();
            }
        });

        sendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject Match = new ParseObject("Match");
                ParseUser current = ParseUser.getCurrentUser();
                System.out.println(current.getUsername());


                getNameFromCurrentUserName(current);
                //Toast.makeText(MatchCreateActivity.this, current.getUsername(), Toast.LENGTH_SHORT).show();

                System.out.println("Ending Parse Query");
                System.out.println("Check this" + FromDisplayName);
                //System.out.println(FromDisplayName);

                //Save the details in the Match table
                Match.put("FromPlayer", current.getUsername());
                Match.put("ToPlayer", ToUserName);
                Match.put("Location", matchLocation.getText().toString());
                Match.put("Time", matchTime.getText().toString());
                Match.put("Status", "pending");

                //
                Match.put("FromPlayerToDisp", FromDisplayName);   //So that names are available in the Match table
                Match.put("ToPlayerToDisp", ToDisplayName);
                Match.saveInBackground();
                Toast.makeText(MatchCreateActivity.this, "Clicking End", Toast.LENGTH_SHORT).show();





                /*ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("username", ParseUser.getCurrentUser());
                installation.saveInBackground();*/

                //Logic to seperate the username
                String strName = palName.getText().toString();
                /*int start = strName.indexOf("(")+1;
                int end = strName.indexOf(")");
                String userNameToQuery = strName.substring(start, end); //This username will be used to query the parse table*/
                Toast.makeText(MatchCreateActivity.this, strName, Toast.LENGTH_SHORT).show();

                ParsePush parsePush = new ParsePush();
                ParseQuery pQuery = ParseInstallation.getQuery(); // <-- Installation query
                pQuery.whereEqualTo("username", ToUserName);
                parsePush.sendMessageInBackground("Squash Invite", pQuery);





            }
        });


        //String palNameIgnoreCase = palName.getText().toString().toLowerCase();

        //Looking up all users
        /*ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    //Success
                    Log.d("UserList", "Found");
                    //String name = users.get(0).get("name").toString();
                    //Toast.makeText(MatchCreateActivity.this, name, Toast.LENGTH_SHORT).show();
                    allUsers.addAll(users);
                    initializeAllUsersList();
                    Toast.makeText(MatchCreateActivity.this, allUsers.get(1).get("name").toString(), Toast.LENGTH_SHORT).show();

                } else {
                    //There was a problem Alert User

                }
            }
        });*/




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getNameFromCurrentUserName(ParseUser current) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Userdetails");
        query.whereEqualTo("username", current.getUsername());
        try {
            List<ParseObject> entries = query.find();
            if (entries.size() > 0) {
                String name = entries.get(0).get("name").toString();
                String un = entries.get(0).get("username").toString();
                FromDisplayName = name;
                System.out.println(name);
                System.out.println(un);
                System.out.println(FromDisplayName);


            } else
                System.out.print("Something wrong");
        }
        catch(ParseException e){
            System.out.println(e);
        }
    }

    private void displayMatchingUsers() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Userdetails");
        String testing = palName.getText().toString();
        query.whereEqualTo("name", testing);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        initiateDialog(objects);
                    } else
                        Toast.makeText(MatchCreateActivity.this, "Size is zero", Toast.LENGTH_SHORT).show();
                } else {
                    // Something went wrong.
                }
            }
        });
    }

    /*private void initializeAllUsersList() {
        if(allUsers!=null){
            if(allUsers.size()>0){
                Toast.makeText(MatchCreateActivity.this, allUsers.get(0).get("name").toString(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MatchCreateActivity.this, "Size is 0", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(MatchCreateActivity.this, "Its null", Toast.LENGTH_SHORT).show();
        }
    }
*/

    public void initiateDialog(List<ParseObject> objects){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MatchCreateActivity.this);
        builderSingle.setIcon(R.drawable.logo);
        builderSingle.setTitle("Select One Name:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MatchCreateActivity.this,
                android.R.layout.select_dialog_singlechoice);
        for(ParseObject obj: objects){
            /*if(obj.get("username").toString().equals(ParseUser.getCurrentUser().toString())){
                System.out.print("The same user as current");
            }*/

                //System.out.print("Reached here");
                arrayAdapter.add(/*obj.get("name").toString()+" (" +*/obj.get("username").toString()/*+" )"*/);


        }
        /*arrayAdapter.add("Hardik");
        arrayAdapter.add("Archit");
        arrayAdapter.add("Jignesh");
        arrayAdapter.add("Umang");
        arrayAdapter.add("Gatti");
*/
        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToUserName = arrayAdapter.getItem(which);
                        //getNameFromUserNameFromParse(userName);
                        if (ToUserName.equals(ParseUser.getCurrentUser().toString())) {
                            Log.d("Current User Check","Same as current user");
                        }
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Userdetails");
                        query.whereEqualTo("username", ToUserName);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    if (objects.size() > 0) {
                                        String name = objects.get(0).get("name").toString();

                                        palName.setText(name);
                                        ToDisplayName = name;
                                        System.out.println(ToDisplayName);

                                    } else
                                        Toast.makeText(MatchCreateActivity.this, "Size is zero", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Something went wrong.
                                }
                            }
                        });
                        //COnverting the name(username) format
                        /*int start = strName.indexOf("(")+1;
                        int end = strName.indexOf(")");
                        String userNameToQuery = strName.substring(start,end); //This username will be used to query the parse table*/
                        //palName.setText(strName);
                        //getParseUserUsingUserName(userNameToQuery);
                        //Toast.makeText(MatchCreateActivity.this, strName, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MatchCreateActivity.this, userID, Toast.LENGTH_SHORT).show();

                        /*
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                MatchCreateActivity.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();*/
                    }
                });
        builderSingle.show();

    }
}
