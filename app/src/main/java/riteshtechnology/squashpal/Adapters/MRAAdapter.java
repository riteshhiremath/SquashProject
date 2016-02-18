package riteshtechnology.squashpal.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import riteshtechnology.squashpal.R;

/**
 * Created by Home on 2/12/2016.
 */
public class MRAAdapter extends ArrayAdapter<String> {

    private final Activity context;
    //private String[] toPlayer;
    //private String[] fromPlayer;

    private List<String> toPlayer;
    private List<String> fromPlayer;
    private List<String> matchStatus;

    /*public MRAAdapter(Activity context, String[] toPlayer, String[] fromPlayer) {
        super(context, R.layout.mda_match_list_each_row, toPlayer);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.toPlayer=toPlayer;
        this.fromPlayer=fromPlayer;
    }
*/
    public MRAAdapter(Activity context, List<String> toPlayer, List<String> fromPlayer,List<String> matchStatus) {
        super(context, R.layout.mda_match_list_each_row, toPlayer);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.toPlayer = toPlayer;
        this.fromPlayer = fromPlayer;
        this.matchStatus = matchStatus;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mda_match_list_each_row, null, true);

        TextView player1 = (TextView) rowView.findViewById(R.id.toPlayer_name);
        TextView player2 = (TextView) rowView.findViewById(R.id.fromPlayer_name);
        TextView textStatus = (TextView)rowView.findViewById(R.id.matchStatus);


        player1.setText(toPlayer.get(position));
        player2.setText(fromPlayer.get(position));
        textStatus.setText(matchStatus.get(position));
        return rowView;

    };

}
