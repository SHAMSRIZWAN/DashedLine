package com.library.dashlineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class RoundedDashView extends View {


    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintFirstCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintAfterFirstCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();


    private int primaryCircleMargin;
    private int dashEffectMargin;
    private int secondoryGap;
    private int primaryCircleColor;
    private int secondoryCirclecolor;
    private int gapCounts = 0;
    private int primaryCircleRadius;
    private int secondoryCircleRadius;

    private boolean dashEffectHeightMatchParent;
    private int StateprimaryCircleMargin;

    public RoundedDashView(Context context) {
        super(context);
        init();
        initAttrs(context, null, 0);

    }


    public RoundedDashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttrs(context, attrs, 0);

    }

    public RoundedDashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(context, attrs, defStyleAttr);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundedDashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initAttrs(context, attrs, defStyleAttr);

    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setColor(getResources().getColor(R.color.dash_gray));
        paint.setPathEffect(new DashPathEffect(new float[]{10, 12}, 20));

    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {

        if (attrs != null) {

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedDashView, 0, 0);

            dashEffectMargin = (int) a.getDimension(R.styleable.RoundedDashView_dasheffect_margin,0);
            StateprimaryCircleMargin=(int) a.getDimension(R.styleable.RoundedDashView_primary_circle_topMargin,0);
            primaryCircleMargin = (int) a.getDimension(R.styleable.RoundedDashView_primary_circle_topMargin,0);

            secondoryGap = (int) a.getDimension(R.styleable.RoundedDashView_secondory_circle_topMargin,
                    ScreenUtils.convertDpToPixel(0,context));



            primaryCircleRadius = (int) a.getDimension(R.styleable.RoundedDashView_primary_circle_radius,
                    ScreenUtils.convertDpToPixel(5,context));

            secondoryCircleRadius = (int) a.getDimension(R.styleable.RoundedDashView_secondory_circle_radius,
                    ScreenUtils.convertDpToPixel(5,context));

            dashEffectHeightMatchParent=a.getBoolean(R.styleable.RoundedDashView_dasheffect_height_matchParent,false);

            int colorPrimary = getContext().getResources().getColor(R.color.purple);
            int colorSec = getContext().getResources().getColor(R.color.gray);

            primaryCircleColor = a.getColor(R.styleable.RoundedDashView_primary_cicle_color, colorPrimary);
            secondoryCirclecolor = a.getColor(R.styleable.RoundedDashView_secondory_color, colorSec);


            paintFirstCircle.setStyle(Paint.Style.FILL);
            paintFirstCircle.setColor(primaryCircleColor);

            paintAfterFirstCircle.setStyle(Paint.Style.FILL);
            paintAfterFirstCircle.setColor(secondoryCirclecolor);

            a.recycle();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.reset();
        int end = dashEffectMargin;
        for(int i = 0; i< gapCounts; i++){
            end += secondoryGap;
        }
        path.moveTo(getWidth() / 2f, dashEffectMargin);

        if(dashEffectHeightMatchParent){
            path.quadTo(getWidth() / 2f, getHeight() / 2f, getWidth()/ 2f, getHeight());
            path.lineTo(getWidth()/ 2f, getHeight());

        }else {
            if(gapCounts==0){
                path.lineTo(getWidth()/ 2f, primaryCircleMargin);

            }else {
                path.lineTo(getWidth()/ 2f, end);

            }

        }

        canvas.drawPath(path, paint);
        canvas.drawCircle(getWidth() / 2f, primaryCircleMargin, primaryCircleRadius, paintFirstCircle);

        for (int i = 0; i < gapCounts; i++) {

            int  totalY = primaryCircleMargin += secondoryGap;
            canvas.drawCircle(getWidth() / 2f, totalY, secondoryCircleRadius, paintAfterFirstCircle);
            primaryCircleMargin = totalY;
        }
        primaryCircleMargin=StateprimaryCircleMargin;

    }

    public void setGapCounts(int gapCounts) {
        this.gapCounts = gapCounts;
        invalidate();
    }

    public void setPrimaryCircleColor(int primaryCircleColor) {
        paintFirstCircle.setStyle(Paint.Style.FILL);
        paintFirstCircle.setColor(primaryCircleColor);
        invalidate();
    }
}