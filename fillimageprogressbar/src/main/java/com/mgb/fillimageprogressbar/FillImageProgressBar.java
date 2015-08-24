package com.mgb.fillimageprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by mgradob on 8/9/15.
 */
public class FillImageProgressBar extends View {

    //region Global Variables
    private static final int TOP_DOWN = 0;
    private static final int DOWN_TOP = 1;
    private static final int LEFT_RIGHT = 2;
    private static final int RIGHT_LEFT = 3;

    private Paint mProgressPaint;

    private Context mContext;

    private static final String LOG_TAG = "FILL IMAGE PROGRESS BAR";

    private int progress;
    private int progressMax;
    float scale = 0;
    int progressEndY = 0, progressEndX = 0;
    int width;
    int height;

    private int filledSectionColor;
    private int unfilledSectionColor;
    private int fillOrientation;
    private int imageSource;

    private Bitmap mBitmap;

    private IndeterminateProgress mIndeterminateProgress;
    //endregion

    //region Getters & Setters
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setProgressMax(int progressMax) {
        this.progressMax = progressMax;
    }

    public void setFilledSectionColor(int filledSectionColor) {
        this.filledSectionColor = filledSectionColor;
    }

    public void setUnfilledSectionColor(int unfilledSectionColor) {
        this.unfilledSectionColor = unfilledSectionColor;
    }

    public void setFillOrientation(int fillOrientation) {
        this.fillOrientation = fillOrientation;
    }

    public void setImageSource(int imageSource) {
        this.imageSource = imageSource;
    }
    //endregion

    public FillImageProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setAttributes(attrs);
    }

    private void setAttributes(AttributeSet attrs) {
        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.FillImageProgressBar, 0, 0);

        try {
            setFilledSectionColor(typedArray.getColor(R.styleable.FillImageProgressBar_filledSectionColor, Color.BLUE));
            setUnfilledSectionColor(typedArray.getColor(R.styleable.FillImageProgressBar_unfilledSectionColor, 0x00000000));
            setFillOrientation(typedArray.getInt(R.styleable.FillImageProgressBar_fillOrientation, TOP_DOWN));
            setImageSource(typedArray.getResourceId(R.styleable.FillImageProgressBar_src, 0));
            this.setBackgroundColor(Color.BLACK);

            mBitmap = BitmapFactory.decodeResource(getResources(), imageSource);
        } catch (Exception ex) {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int bitmapWidthSize = MeasureSpec.getSize(mBitmap.getWidth());
        int bitmapHeightSize = MeasureSpec.getSize(mBitmap.getHeight());

        switch (widthMode) {
            case MeasureSpec.EXACTLY:   // Must be this size.
                width = widthSize;

                break;
            case MeasureSpec.AT_MOST:   // Can't be bigger than...
                width = Math.min(widthSize, bitmapWidthSize);

                break;
            case MeasureSpec.UNSPECIFIED:   // Whatever...
            default:
                width = bitmapWidthSize;

                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;

                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, bitmapHeightSize);

                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                height = bitmapHeightSize;

                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (fillOrientation) {
            case DOWN_TOP:
                Log.d(LOG_TAG, "Vertical filling down_top");
                scale = (height / progressMax);
                progressEndY = (int) (scale * progress);

                // Draw the part of the line that's filled.
                mProgressPaint.setStrokeWidth(width);
                mProgressPaint.setColor(filledSectionColor);
                canvas.drawLine(getWidth() / 2, getHeight(), getWidth() / 2, (getHeight() - progressEndY), mProgressPaint);

                // Draw the part of the line that's filled.
                mProgressPaint.setStrokeWidth(width);
                mProgressPaint.setColor(unfilledSectionColor);
                canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, (getHeight() - progressEndY), mProgressPaint);

                break;
            case TOP_DOWN:
                Log.d(LOG_TAG, "Vertical filling top_down");
                scale = (height / progressMax);
                progressEndY = (int) (scale * progress);

                // Draw the part of the line that's filled.
                mProgressPaint.setStrokeWidth(width);
                mProgressPaint.setColor(filledSectionColor);
                canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, progressEndY, mProgressPaint);

                // Draw the part of the line that's filled.
                mProgressPaint.setStrokeWidth(width);
                mProgressPaint.setColor(unfilledSectionColor);
                canvas.drawLine(getWidth() / 2, progressEndY, getWidth() / 2, getHeight(), mProgressPaint);

                break;
            case LEFT_RIGHT:
                Log.d(LOG_TAG, "Horizontal filling left_right");
                scale = (width / progressMax);
                progressEndX = (int) (scale * progress);

                // Draw the part of the line that's filled.
                mProgressPaint.setStrokeWidth(height);
                mProgressPaint.setColor(filledSectionColor);
                canvas.drawLine(0, getHeight() / 2, progressEndX, getHeight() / 2, mProgressPaint);

                // Draw the part of the line that's filled.
                mProgressPaint.setStrokeWidth(height);
                mProgressPaint.setColor(unfilledSectionColor);
                canvas.drawLine(progressEndX, getHeight() / 2, getWidth(), getHeight() / 2, mProgressPaint);

                break;
            case RIGHT_LEFT:
                Log.d(LOG_TAG, "Horizontal filling right_left");
                scale = (width / progressMax);
                progressEndX = (int) (scale * progress);

                // Draw the part of the line that's filled.
                mProgressPaint.setStrokeWidth(height);
                mProgressPaint.setColor(filledSectionColor);
                canvas.drawLine(getWidth(), getHeight() / 2, (getWidth() - progressEndX), getHeight() / 2, mProgressPaint);

                // Draw the part of the line that's filled.
                mProgressPaint.setStrokeWidth(height);
                mProgressPaint.setColor(unfilledSectionColor);
                canvas.drawLine((getWidth() - progressEndX), getHeight() / 2, 0, getHeight() / 2, mProgressPaint);

                break;
            default:
                break;
        }

        canvas.drawBitmap(mBitmap, (canvas.getWidth() - mBitmap.getWidth()) / 2, (canvas.getHeight() - mBitmap.getHeight()) / 2, null);
    }

    public void resetToFixed(int imageResource) {
        progress = 0;
        mBitmap = BitmapFactory.decodeResource(getResources(), imageResource);
        invalidate();
    }

    public void resetToProgress(int imageResource) {
        progress = 0;
        mBitmap = BitmapFactory.decodeResource(getResources(), imageResource);
        invalidate();
    }

    public void setIndeterminate(boolean indeterminate) {
        if (indeterminate) {
            if (mIndeterminateProgress == null) {
                mIndeterminateProgress = new IndeterminateProgress();
                mIndeterminateProgress.execute();
            }
        } else {
            if (mIndeterminateProgress != null) {
                mIndeterminateProgress.cancel(true);
                mIndeterminateProgress = null;
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        // save our added state - progress
        bundle.putInt("progress", progress);

        // save super state
        bundle.putParcelable("superState", super.onSaveInstanceState());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            // restore our added state - progress
            setProgress(bundle.getInt("progress"));

            // restore super state
            state = bundle.getParcelable("superState");
        }

        super.onRestoreInstanceState(state);
    }

    private class IndeterminateProgress extends AsyncTask<Void, Integer, Void> {
        int loopCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setProgressMax(10);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            setProgress(values[0]);
            invalidate();
        }

        @Override
        protected Void doInBackground(Void... params) {
            int progress = 0;

            try {
                while (true) {
                    while (progress <= progressMax) {
                        publishProgress(progress++);
                        Thread.sleep(100);
                    }

                    progress = 0;
                }
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);

            setProgress(0);
            invalidate();
        }
    }
}
