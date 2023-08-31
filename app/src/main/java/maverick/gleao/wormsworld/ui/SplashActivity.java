package maverick.gleao.wormsworld.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


/**
 * Created by Maverick on 13-May-16.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.splash_scream);

       Thread myThread = new Thread(){
           @Override
           public void run() {
               try {
                   sleep(3000);
                   Intent iv = new Intent(getApplicationContext(),MainMenuActivity.class);
                   startActivity(iv);
                   finish();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       };
        myThread.start();
    }
}
