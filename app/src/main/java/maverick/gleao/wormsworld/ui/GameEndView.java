package maverick.gleao.wormsworld.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class GameEndView extends View {
    Bitmap bitmap;

    public GameEndView(Context context, Bitmap bitmap) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.bitmap = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paintBackground = new Paint();

        canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),
                new Rect(0,0,getWidth(),getHeight()),paintBackground);
    }
}
