package riteshtechnology.squashpal;

import android.app.Application;
import android.widget.Toast;

import com.parse.Parse;

/**
 * Created by Home on 2/21/2016.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Sm2wwAv72dlUrhmSwSJwRsIJ5G8B0GfRTtvQ9uf2", "rJVwfkHcshHIwKk5x2GQSj07Odl6ctKtsBBaqBXx");
        Toast.makeText(ParseApplication.this, "Parse is Initialized", Toast.LENGTH_SHORT).show();
    }
}