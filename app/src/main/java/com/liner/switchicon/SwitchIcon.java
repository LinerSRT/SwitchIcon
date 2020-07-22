package com.liner.switchicon;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @package com.liner.switchicon
 * @created at 22.07.2020 - 18:49
 * @autor Line'R (serinity320@gmail.com)
 **/
public class SwitchIcon extends AppCompatImageView {
    @FloatRange(from = 0.0, to = 1.0)
    private float fraction = 0f;
    private int dashThickness = 0;
    private int dashLengthXProjection = 0;
    private int dashLengthYProjection = 0;
    private PorterDuffColorFilter colorFilter;
    @FloatRange(from = 0.0, to = 1.0)
    private float disabledStateAlpha;
    private long animationDuration;
    private int dashXStart;
    private int dashYStart;
    @ColorInt
    private int iconTintColor;
    @ColorInt
    private int disabledStateColor;
    private boolean noDash;
    private ArgbEvaluator colorEvaluator;
    private Path clipPath;
    private Point dashStart;
    private Point dashEnd;
    private Paint dashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean isIconEnabled = false;


    private float dashPadding = 10f;

    public SwitchIcon(Context context) {
        super(context);
        init(context, null);
    }

    public SwitchIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        dashPaint.setStyle(Paint.Style.STROKE);
        dashStart = new Point();
        dashEnd = new Point();
        clipPath = new Path();
        colorEvaluator = new ArgbEvaluator();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchIcon, 0, 0);
        iconTintColor = typedArray.getColor(R.styleable.SwitchIcon_si_tint_color, Color.RED);
        animationDuration = typedArray.getInteger(R.styleable.SwitchIcon_si_animation_duration, 300);
        disabledStateAlpha = typedArray.getFloat(R.styleable.SwitchIcon_si_disabled_alpha, 0.5f);
        disabledStateColor = typedArray.getColor(R.styleable.SwitchIcon_si_disabled_color, Color.GRAY);
        isIconEnabled = typedArray.getBoolean(R.styleable.SwitchIcon_si_enabled, true);
        dashPadding = typedArray.getDimension(R.styleable.SwitchIcon_si_dash_padding, 0f);
        noDash = typedArray.getBoolean(R.styleable.SwitchIcon_si_no_dash, false);
        typedArray.recycle();
        if (disabledStateAlpha < 0f || disabledStateAlpha > 1f) {
            throw new IllegalArgumentException("Wrong value for si_disabled_alpha [" + disabledStateAlpha + "]. "
                    + "Must be value from range [0, 1]");
        }
        colorFilter = new PorterDuffColorFilter(iconTintColor, PorterDuff.Mode.SRC_IN);
        setColorFilter(colorFilter);
        dashXStart = getPaddingLeft();
        dashYStart = getPaddingTop();
        dashPaint.setColor(iconTintColor);
        initDashCoordinates();
        setFraction((isIconEnabled) ? 0f : 1f);
    }

    public void setIconEnabled(boolean iconEnabled, boolean animate) {
        this.isIconEnabled = iconEnabled;
        switchState(animate);
    }

    public void switchState(boolean animate) {
        float newFraction = (isIconEnabled) ? 1f : 0f;
        isIconEnabled = !isIconEnabled;
        if (animate) {
            animateToFraction(newFraction);
        } else {
            setFraction(newFraction);
            invalidate();
        }
    }

    public void switchState() {
        switchState(true);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        dashLengthXProjection = width - getPaddingLeft() - getPaddingRight();
        dashLengthYProjection = height - getPaddingTop() - getPaddingBottom();
        dashThickness = (int) ((1f / 12f) * (dashLengthXProjection + dashLengthYProjection) / 2f);
        dashPaint.setStrokeWidth(dashThickness);
        initDashCoordinates();
        updateClipPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!noDash) {
            drawDash(canvas);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutPath(clipPath);
            } else {
                canvas.clipPath(clipPath, Region.Op.XOR);
            }
        }
        super.onDraw(canvas);
    }

    private void animateToFraction(float toFraction) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fraction, toFraction);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setFraction((Float) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(animationDuration);
        valueAnimator.start();
    }

    private void setFraction(float fraction) {
        this.fraction = fraction;
        updateColor(fraction);
        updateAlpha(fraction);
        updateClipPath();
        postInvalidateOnAnimation();
    }

    private void updateClipPath() {
        float delta = (float) (dashThickness / Math.sin(Math.toRadians(45f)));
        clipPath.reset();
        clipPath.moveTo(dashXStart, dashYStart + delta);
        clipPath.lineTo(dashXStart + delta, dashYStart);
        clipPath.lineTo(dashXStart + dashLengthXProjection * fraction, dashYStart + dashLengthYProjection * fraction - delta);
        clipPath.lineTo(dashXStart + dashLengthXProjection * fraction - delta, dashYStart + dashLengthYProjection * fraction);
    }

    private void drawDash(Canvas canvas) {
        float x = fraction * (dashEnd.x - dashStart.x) + dashStart.x;
        float y = fraction * (dashEnd.y - dashStart.y) + dashStart.y;
        canvas.drawLine(dashStart.x+1+dashPadding, dashStart.y-1+dashPadding, x+1-dashPadding, y-1-dashPadding, dashPaint);
    }

    private void updateColor(float fraction) {
        if (iconTintColor != disabledStateColor) {
            int color = (int) colorEvaluator.evaluate(fraction, iconTintColor, disabledStateColor);
            updateImageColor(color);
            dashPaint.setColor(color);
        }
    }

    private void updateAlpha(float fraction) {
        int alpha = (int) ((disabledStateAlpha + (1f - fraction) * (1f - disabledStateAlpha)) * 255);
        updateImageAlpha(alpha);
        dashPaint.setAlpha(alpha);
    }

    private void updateImageColor(@ColorInt int color) {
        colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        setColorFilter(color);
    }

    private void updateImageAlpha(int alpha) {
        setImageAlpha(alpha);
    }

    private void initDashCoordinates() {
        float delta1 = (float) (1.5f * (Math.sin(Math.toRadians(45f))) * dashThickness);
        float delta2 = (float) (0.5f * (Math.sin(Math.toRadians(45f))) * dashThickness);
        dashStart.x = (int) (dashXStart + delta2);
        dashStart.y = (int) (dashYStart + delta1);
        dashEnd.x = (int) (dashXStart + dashLengthXProjection - delta1);
        dashEnd.y = (int) (dashYStart + dashLengthYProjection - delta2);
    }

}
