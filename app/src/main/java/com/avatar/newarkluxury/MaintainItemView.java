package com.avatar.newarkluxury;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by chx on 2016/12/29.
 */

public class MaintainItemView extends LinearLayout {

    private Context mContext;
    private TextView mNameText, mValueText;
    private ImageView mValueImage;
    private int mCurrentStyle;
    public static final int SHORT_STATE = 0;
    public static final int SHORT_WARNING = 1;
    public static final int LONG_STATE = 2;
    public static final int LONG_WARNING = 3;
    public static final int SHORT_VALUE = 4;
    public static final int LONG_VALUE = 5;
    public static final int HEIGHT_SHORT_STATE = 6;
    public static final int HEIGHT_SHORT_WARNING = 7;

    public MaintainItemView(Context context) {
        super(context);
    }

    public MaintainItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaintainItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MaintainItemView(Context context, int style, int color) {
        this(context);
        mContext = context;
        initView(style, color);
    }

    public static MaintainItemView getItemView(Context context, int style, int color,
                                               @StringRes int name, boolean value) {
        MaintainItemView view = new MaintainItemView(context, style, color);
        view.setAllText(name, value);
        return view;
    }

    public static MaintainItemView getItemView2(Context context, int style, int color,
                                                @StringRes int name, String value) {
        MaintainItemView view = new MaintainItemView(context, style, color);
        view.setAllText2(name, value);
        return view;
    }

    public void initView(int style, int color) {
        setOrientation(LinearLayout.HORIZONTAL);
        mNameText = new TextView(mContext);
        mNameText.setGravity(Gravity.CENTER);
        mNameText.setTextColor(color);
        mNameText.setTextSize(12);

        switch (style) {
            case SHORT_STATE:
                addShortStateView();
                break;
            case SHORT_WARNING:
                addShortWarningView();
                break;
            case LONG_STATE:
                addLongStateView();
                break;
            case LONG_WARNING:
                addLongWarningView();
                break;
            case SHORT_VALUE:
                addShortValueView(color);
                break;
            case LONG_VALUE:
                addLongValueView(color);
                break;
            case HEIGHT_SHORT_STATE:
                addHeightStateView();
                break;
            case HEIGHT_SHORT_WARNING:
                addHeightWarningView();
                break;
            default:
        }
    }

    public void addShortStateView() {
        LinearLayout.LayoutParams params1 = new LayoutParams(230, 33);
        LinearLayout.LayoutParams params2 = new LayoutParams(22, 22);
        params2.gravity = Gravity.CENTER_VERTICAL;
        params2.setMarginStart(53);
        mValueImage = new ImageView(mContext);
        mValueImage.setImageResource(R.drawable.maintain_state_icon);
        addTextView(params1, params2);
    }

    public void addShortWarningView() {
        LinearLayout.LayoutParams params1 = new LayoutParams(230, 33);
        LinearLayout.LayoutParams params2 = new LayoutParams(22, 22);
        params2.gravity = Gravity.CENTER_VERTICAL;
        params2.setMarginStart(53);
        mValueImage = new ImageView(mContext);
        mValueImage.setImageResource(R.drawable.maintain_warning_icon);
        Drawable drawable = mValueImage.getDrawable();
        if (drawable instanceof StateListDrawable) {
            Drawable anim = ((DrawableContainer.DrawableContainerState)drawable
                    .getConstantState()).getChild(0);
            if (anim != null && anim instanceof AnimationDrawable) {
                ((AnimationDrawable) anim).start();
            }
        }
        addTextView(params1, params2);
    }

    public void addLongStateView() {
        LinearLayout.LayoutParams params1 = new LayoutParams(252, 33);
        LinearLayout.LayoutParams params2 = new LayoutParams(22, 22);
        params2.gravity = Gravity.CENTER_VERTICAL;
        params2.setMarginStart(42);
        mValueImage = new ImageView(mContext);
        mValueImage.setImageResource(R.drawable.maintain_state_icon);
        addTextView(params1, params2);
    }

    public void addLongWarningView() {
        LinearLayout.LayoutParams params1 = new LayoutParams(252, 33);
        LinearLayout.LayoutParams params2 = new LayoutParams(22, 22);
        params2.gravity = Gravity.CENTER_VERTICAL;
        params2.setMarginStart(42);
        mValueImage = new ImageView(mContext);
        mValueImage.setImageResource(R.drawable.maintain_warning_icon);
        Drawable drawable = mValueImage.getDrawable();
        if (drawable instanceof StateListDrawable) {
            Drawable anim = ((DrawableContainer.DrawableContainerState)drawable
                    .getConstantState()).getChild(0);
            if (anim != null && anim instanceof AnimationDrawable) {
                ((AnimationDrawable) anim).start();
            }
        }
        addTextView(params1, params2);
    }

    public void addShortValueView(int color) {
        LinearLayout.LayoutParams params1 = new LayoutParams(230, 33);
        LinearLayout.LayoutParams params2 = new LayoutParams(128, 33);
        mValueText = new TextView(mContext);
        mValueText.setGravity(Gravity.CENTER);
        mValueText.setTextColor(color);
        addTextView(params1, params2);
    }

    public void addLongValueView(int color) {
        LinearLayout.LayoutParams params1 = new LayoutParams(252, 33);
        LinearLayout.LayoutParams params2 = new LayoutParams(106, 33);
        mValueText = new TextView(mContext);
        mValueText.setGravity(Gravity.CENTER);
        mValueText.setTextColor(color);
        addTextView(params1, params2);
    }

    public void addHeightStateView() {
        LinearLayout.LayoutParams params1 = new LayoutParams(230, 62);
        LinearLayout.LayoutParams params2 = new LayoutParams(22, 22);
        params2.gravity = Gravity.CENTER_VERTICAL;
        params2.setMarginStart(53);
        mValueImage = new ImageView(mContext);
        mValueImage.setImageResource(R.drawable.maintain_state_icon);
        addTextView(params1, params2);
    }

    public void addHeightWarningView() {
        LinearLayout.LayoutParams params1 = new LayoutParams(230, 62);
        LinearLayout.LayoutParams params2 = new LayoutParams(22, 22);
        params2.gravity = Gravity.CENTER_VERTICAL;
        params2.setMarginStart(53);
        mValueImage = new ImageView(mContext);
        mValueImage.setImageResource(R.drawable.maintain_warning_icon);
        Drawable drawable = mValueImage.getDrawable();
        if (drawable instanceof StateListDrawable) {
            Drawable anim = ((DrawableContainer.DrawableContainerState)drawable
                    .getConstantState()).getChild(0);
            if (anim != null && anim instanceof AnimationDrawable) {
                ((AnimationDrawable) anim).start();
            }
        }
        addTextView(params1, params2);
    }

    public void addTextView(LayoutParams params1, LayoutParams params2) {
        addView(mNameText, params1);
        if (mValueImage != null) {
            addView(mValueImage, params2);
        } else if (mValueText != null) {
            addView(mValueText, params2);
        }
    }

    public void setAllText(int name, boolean value) {
        mNameText.setText(name);
        if (value) {
            mValueImage.setSelected(true);
        }
    }

    public void setAllText2(int name, String value) {
        mNameText.setText(name);
        mValueText.setText(value);
    }

}
