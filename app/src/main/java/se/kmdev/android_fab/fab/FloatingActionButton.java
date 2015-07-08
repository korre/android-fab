package se.kmdev.android_fab.fab;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Floating action button that can be added in XML or by java code.
 * Recommended size is 56dp for a regular button or 40dp for mini.
 * For more info about the concept:
 * http://www.google.com/design/spec/components/buttons-floating-action-button.html
 *
 * Created by Kristoffer, http://kmdev.se.
 */
public class FloatingActionButton extends View {

    private final String TAG = FloatingActionButton.class.getName();

    private final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private final int DEFAULT_SHADOW_COLOR = Color.DKGRAY;
    private final int DEFAULT_SIZE = 56;
    private final int DEFAULT_CENTER_SIZE = 20;
    private final int ANIMATION_DURATION = 300;

    private float mScreenHeight;
    private float mButtonStartY;

    private ObjectAnimator animator;

    private List<OnFloatingActionButtonPressedListener> listeners;
    private Paint mPaint;
    private Bitmap mCenterIcon;
    private float mSize;

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        listeners = new ArrayList<>();
        mPaint = new Paint();
        mSize = pxFromDp(this.getContext(), DEFAULT_SIZE);

        post(new Runnable() {
            @Override
            public void run() {
                calculateScreenHeight();
                calculateButtonStartY();

                setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            for (OnFloatingActionButtonPressedListener listener : listeners) {
                                listener.onPress();
                            }
                        }
                        return true;
                    }
                });
            }
        });
    }

    public void addOnFloatingActionButtonPressedListener(OnFloatingActionButtonPressedListener
                                                                 onFloatingActionButtonPressedListener) {
        listeners.add(onFloatingActionButtonPressedListener);
    }

    public void attachToListView(AbsListView listView) {
        listView.setOnTouchListener(new OnTouchListener() {
            float initialY, currentY, finalY, initialButtonY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = MotionEventCompat.getActionMasked(motionEvent);

                switch (action) {
                    case (MotionEvent.ACTION_MOVE):
                        if (initialY == 0) {
                            stopCurrentAnimation();
                            initialY = motionEvent.getRawY();
                            initialButtonY = getY();
                        } else {
                            currentY = motionEvent.getRawY();
                            if (initialY < currentY) { // scrolling up (finger down)
                                setY(Math.max(mButtonStartY, initialButtonY - (currentY - initialY)));
                            } else {
                                setY(Math.min(mScreenHeight, initialButtonY + (initialY - currentY)));
                            }
                        }
                        break;
                    case (MotionEvent.ACTION_UP):
                        finalY = motionEvent.getRawY();
                        if (initialY < finalY) {
                            animateButton(getY(), mButtonStartY);
                        } else {
                            animateButton(getY(), mScreenHeight);
                        }
                        initialY = 0;
                        break;
                }

                return false; // Has to be false so it doesn't freeze the ListView.
            }
        });
    }

    public void setCenterIconResource(int resourceID) {
        setCenterIconBitmap(BitmapFactory.decodeResource(getResources(), resourceID));
    }

    public void setCenterIconBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            mCenterIcon = scaleBitmapToDefaultSize(bitmap);
        } else {
            Log.w(TAG, "Image doesn't seem to exist!");
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        drawBaseCircle(canvas);
        drawCenterIcon(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mCenterIcon != null) {
            mCenterIcon.recycle();
        }

        super.onDetachedFromWindow();
    }

    private void drawBaseCircle(Canvas canvas) {
        mPaint.setColor(DEFAULT_BACKGROUND_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setShadowLayer(5.0f, 3.0f, 3.0f, DEFAULT_SHADOW_COLOR);
        canvas.drawCircle(mSize / 2, mSize / 2, mSize / 2, mPaint);
    }

    private void drawCenterIcon(Canvas canvas) {
        if (mCenterIcon != null) {
            final float left = mSize/2f - (pxFromDp(getContext(), DEFAULT_CENTER_SIZE)/2f);
            final float top = left;
            canvas.drawBitmap(mCenterIcon, left, top, null);
        } else {
            Log.w(TAG, "No icon is set to button.");
        }
    }

    private float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private Bitmap scaleBitmapToDefaultSize(Bitmap bitmap) {
        final int centerSize = (int) pxFromDp(getContext(), DEFAULT_CENTER_SIZE);
        return Bitmap.createScaledBitmap(bitmap, centerSize, centerSize, false);
    }

    private void calculateScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        mScreenHeight = point.y;
    }

    private void calculateButtonStartY() {
        mButtonStartY = getY();
    }

    private void stopCurrentAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    private void animateButton(float start, float end) {
        stopCurrentAnimation();
        animator = ObjectAnimator.ofFloat(this, View.Y, start, end);
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
    }


    /**
     * Listener used for catching button press.
     */
    public interface OnFloatingActionButtonPressedListener {
        void onPress();
    }
}
