package maverick.gleao.wormsworld.ui;

import android.content.Context;
import android.media.MediaPlayer;

//Credit: Professor Miguel Pimenta Monteiro Sudoku's example
public class Music {
   private static MediaPlayer mp = null;

   /** Stop old song and start new one */
   
   public static void play(Context context, int resource) {
       stop(context);
       mp = MediaPlayer.create(context, resource);
       mp.setLooping(true);
       mp.start();
   }
   

   /** Stop the music */
   public static void stop(Context context) { 
      if (mp != null)
      {
          mp.stop();
          mp.release();
          mp = null;
      }
   }
}
