package com.avatar.newarkluxury;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Created by chx on 2016/12/22.
 */

public abstract class BaseFragment extends Fragment {
    private Logger mLogger = LoggerFactory.getLogger(BaseFragment.class);
    private Dialog mDialog, mWarningDialog;
    private EditText mEditText;

    protected static final String STATUS_PASSWORD = "newarkadmin";
    private Typeface mChineseTypeface, mEnglishTypeface;
    protected ImageView mO2DensityImage, mCO2DensityImage, mPowerRestImage,
            mEmergencyImage, mSewageImage;

    protected TextView mTemperatureText, mHumidityText, mO2DensityText, mCO2DensityText,
            mWaterRestText, mPowerRestText, mO2DensityUnitText, mCO2DensityUnitText;

    protected ImageView mSlideView;

//    protected ImageView mLifeSmogWarningImage, mDeviceSmogWarningImage;
//    protected TextView mLifeSmogWarningText, mDeviceSmogWarningText;

    private BroadcastReceiver mReceiver;
    protected WaveView mWaveView;
    protected WaveHelper mWaveHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AssetManager mgr = getActivity().getAssets();
        mChineseTypeface = Typeface.createFromAsset(mgr, "fonts/chinese.ttf");
        mEnglishTypeface = Typeface.createFromAsset(mgr, "fonts/GUN4FC.TTF");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mReceiver == null) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (Communicator.ACTION_STATE.equalsIgnoreCase(action)) {
                        MachineState state = (MachineState) intent.getSerializableExtra(Communicator.FLAG_GET_STATE);
                        onUpdateMachineState(state);
                    } else if (MainActivity.ACTION_SLIDE_START.equalsIgnoreCase(action)) {
                        mSlideView.setSelected(true);
                    } else if (MainActivity.ACTION_SLIDE_END.equalsIgnoreCase(action)) {
                        mSlideView.setSelected(false);
                    }
                }
            };
        }
        IntentFilter filter = new IntentFilter(Communicator.ACTION_STATE);
        filter.addAction(MainActivity.ACTION_SLIDE_START);
        filter.addAction(MainActivity.ACTION_SLIDE_END);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWaveHelper != null)
            mWaveHelper.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWaveHelper != null)
            mWaveHelper.cancel();
    }

    public abstract void onUpdateMachineState(MachineState state);

    public void setTextToChinese() {
//        updateLanguage(Locale.SIMPLIFIED_CHINESE);
        setTextSub(getView(), mChineseTypeface);
    }

    public void setTextToEnglish() {
//        updateLanguage(Locale.ENGLISH);
        setTextSub(getView(), mEnglishTypeface);
    }

    private void setTextSub(View view, Typeface typeface) {
        if (view instanceof TextView) {
            String s = (String) view.getTag();
            if (!(s != null && s.equalsIgnoreCase("N"))) {
                view.requestLayout();
                ((TextView) view).setTypeface(typeface);
                if (view instanceof AppTextView) {
                    ((AppTextView) view).reLoadLanguage();
                }
//                ((TextView) view).setTextLocale(locale);
//                view.invalidate();
            }
        } else if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setTextSub(((ViewGroup) view).getChildAt(i), typeface);
            }
        }
    }

    public void setTextFont(TextView view) {
        Configuration config = getResources().getConfiguration();
        String language = config.locale.getLanguage();
        if (language.endsWith("zh")) {
            view.setTypeface(mChineseTypeface);
        } else {
            view.setTypeface(mEnglishTypeface);
        }
    }

    public void initText() {
        setShadow(getView());
//        setTextToChinese();
        Configuration config = getResources().getConfiguration();
        String language = config.locale.getLanguage();
        if (language.endsWith("zh")) {
            setTextToChinese();
        } else {
            setTextToEnglish();
        }
    }

    private void setShadow(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setLetterSpacing(0.1f);
            String s = (String) view.getTag();
            ((TextView) view).setTypeface(mEnglishTypeface);
            if (s == null || !s.equalsIgnoreCase("S")) {
                ((TextView) view).setShadowLayer(4F, 0F,0F, 0xFF63D7FF);
            }
        } else if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setShadow(((ViewGroup) view).getChildAt(i));
            }
        }
    }

    public void changeLanguage() {
        Configuration config = getResources().getConfiguration();
        String language = config.locale.getLanguage();
        if (language.endsWith("zh")) {
            config.setLocale(Locale.ENGLISH);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            ((MainActivity) getActivity()).updateLanguage(false);
        } else {
            config.setLocale(Locale.SIMPLIFIED_CHINESE);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            ((MainActivity) getActivity()).updateLanguage(true);
        }
    }

    public void startStatusActivity() {
        mDialog = new Dialog(getActivity(), R.style.CustomDialogTheme);
        View root = LayoutInflater.from(getActivity()).inflate(
                R.layout.status_dialog, null);
        mDialog.setContentView(root);
//            mDialog.setContentView(R.layout.status_dialog);
        mEditText = (EditText)mDialog.findViewById(R.id.dialog_edit);
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 650;
        lp.height = 272;
        dialogWindow.setAttributes(lp);
        mDialog.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText().toString().equalsIgnoreCase(STATUS_PASSWORD)) {
                    mDialog.dismiss();
                    startActivity(new Intent(getActivity(), MaintainActivity.class));
                } else {
                    Toast.makeText(getActivity(), getResources()
                            .getString(R.string.password_error), Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }
        });
        mDialog.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        Configuration config = getResources().getConfiguration();
        String language = config.locale.getLanguage();
        if (language.endsWith("zh")) {
            setTextSub(root, mChineseTypeface);
        } else {
            setTextSub(root, mEnglishTypeface);
        }
        mEditText.setTypeface(mEnglishTypeface);
        mDialog.show();
//        startActivity(new Intent(getActivity(), MaintainActivity.class));
    }

    public void showDialog(@StringRes int s, View.OnClickListener listener) {
        if (mWarningDialog == null) {
            mWarningDialog = new Dialog(getActivity(), R.style.CustomDialogTheme);
            View root = LayoutInflater.from(getActivity()).inflate(
                    R.layout.warning_dialog, null);
            mWarningDialog.setContentView(root);
            Window dialogWindow = mWarningDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = 650;
            lp.height = 200;
            dialogWindow.setAttributes(lp);
            mWarningDialog.findViewById(R.id.warning_dialog_cancel)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mWarningDialog.dismiss();
                        }
                    });
        }
        ((TextView) mWarningDialog.findViewById(R.id.warning_dialog_text)).setText(s);
        mWarningDialog.findViewById(R.id.warning_dialog_ok).setOnClickListener(listener);
        mWarningDialog.show();
    }

    public void dismissWarningDialog() {
        if (mWarningDialog != null && mWarningDialog.isShowing()) {
            mWarningDialog.dismiss();
        }
    }

//    protected void updateLanguage(Locale locale) {
//        try {
//            Object objIActMag, objActMagNative;
//            Class clzIActMag = Class.forName("android.app.IActivityManager");
//            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
//            Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
//            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
//            Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
//            Configuration config = (Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
//            config.locale = locale;
//            Class[] clzParams = { Configuration.class };
//            Method mtdIActMag$updateConfiguration =
//                    clzIActMag.getDeclaredMethod("updateConfiguration", clzParams);
//            mtdIActMag$updateConfiguration.invoke(objIActMag, config);
//        } catch (Exception e) {
//            mLogger.error("update language error!", e);
//        }
//    }

    public void startEmergencyAnimation() {
        Drawable drawable = mEmergencyImage.getDrawable();
        if (drawable instanceof StateListDrawable) {
            Drawable anim = ((DrawableContainer.DrawableContainerState)drawable
                    .getConstantState()).getChild(1);
            if (anim != null && anim instanceof AnimationDrawable) {
                ((AnimationDrawable) anim).start();
            }
        }
        Drawable drawable2 = mSewageImage.getDrawable();
        if (drawable2 instanceof StateListDrawable) {
            Drawable anim = ((DrawableContainer.DrawableContainerState)drawable2
                    .getConstantState()).getChild(1);
            if (anim != null && anim instanceof AnimationDrawable) {
                ((AnimationDrawable) anim).start();
            }
        }
    }

//    public void startSmogAnimation() {
//        Drawable drawable1 = mLifeSmogWarningImage.getDrawable();
//        if (drawable1 instanceof StateListDrawable) {
//            Drawable anim = ((DrawableContainer.DrawableContainerState)drawable1
//                    .getConstantState()).getChild(0);
//            if (anim != null && anim instanceof AnimationDrawable) {
//                ((AnimationDrawable) anim).start();
//            }
//        }
//        Drawable drawable2 = mDeviceSmogWarningImage.getDrawable();
//        if (drawable2 instanceof StateListDrawable) {
//            Drawable anim = ((DrawableContainer.DrawableContainerState)drawable2
//                    .getConstantState()).getChild(0);
//            if (anim != null && anim instanceof AnimationDrawable) {
//                ((AnimationDrawable) anim).start();
//            }
//        }
//    }

    public void setTemperature(float f) {
        mTemperatureText.setText(FormatUtils.float2String(f + 0.5f));
    }

    public void setHumidity(float f) {
        mHumidityText.setText(FormatUtils.float2String(f + 0.5f));
    }

    public void setWaterRest(float f) {
        mWaterRestText.setText(FormatUtils.float2String(f + 0.5f));
        mWaveView.setWaterLevelRatio(f / 100);
        if (f > 20) {
            mWaveView.setWaveColor(Color.parseColor("#990DE9D9"),
                    Color.parseColor("#B20DE9E0"), Color.parseColor("#1998FCFC"));
        } else {
            mWaveView.setWaveColor(Color.parseColor("#A5D42533"),
                    Color.parseColor("#C2D80324"), Color.parseColor("#19FFD9C4"));
        }
    }

    public void setPowerRest(float f) {
        mPowerRestText.setText(FormatUtils.float2String(f + 0.5f));
        switch ((int) f/10) {
            case 0:
                mPowerRestImage.setImageResource(R.drawable.battery_1);
                break;
            case 1:
                mPowerRestImage.setImageResource(R.drawable.battery_2);
                break;
            case 2:
                mPowerRestImage.setImageResource(R.drawable.battery_3);
                break;
            case 3:
                mPowerRestImage.setImageResource(R.drawable.battery_4);
                break;
            case 4:
                mPowerRestImage.setImageResource(R.drawable.battery_5);
                break;
            case 5:
                mPowerRestImage.setImageResource(R.drawable.battery_6);
                break;
            case 6:
                mPowerRestImage.setImageResource(R.drawable.battery_7);
                break;
            case 7:
                mPowerRestImage.setImageResource(R.drawable.battery_8);
                break;
            case 8:
                mPowerRestImage.setImageResource(R.drawable.battery_9);
                break;
            case 9:
            case 10:
                mPowerRestImage.setImageResource(R.drawable.battery_10);
                break;
        }
    }

    public void setO2Density(float f) {
        mO2DensityText.setText(FormatUtils.float2String(f + 0.5f));
        if (f > 23) {
            mO2DensityText.setTextColor(getResources().getColor(R.color.text_green));
            mO2DensityUnitText.setTextColor(getResources().getColor(R.color.text_green));
            mO2DensityImage.setImageResource(R.drawable.o2_high);
        } else if (f > 19) {
            mO2DensityText.setTextColor(getResources().getColor(R.color.text_green));
            mO2DensityUnitText.setTextColor(getResources().getColor(R.color.text_green));
            mO2DensityImage.setImageResource(R.drawable.o2_green);
        } else if (f > 18) {
            mO2DensityText.setTextColor(getResources().getColor(R.color.text_blue));
            mO2DensityUnitText.setTextColor(getResources().getColor(R.color.text_blue));
            mO2DensityImage.setImageResource(R.drawable.o2_blue);
        } else if (f > 17) {
            mO2DensityText.setTextColor(getResources().getColor(R.color.text_orange));
            mO2DensityUnitText.setTextColor(getResources().getColor(R.color.text_orange));
            mO2DensityImage.setImageResource(R.drawable.o2_orange);
        } else {
            mO2DensityText.setTextColor(getResources().getColor(R.color.text_red));
            mO2DensityUnitText.setTextColor(getResources().getColor(R.color.text_red));
            mO2DensityImage.setImageResource(R.drawable.o2_red);
        }
    }

    public void setCO2Density(float f) {
        mCO2DensityText.setText(FormatUtils.float2String(f + 0.5f));
        if (f < 2000) {
            mCO2DensityText.setTextColor(getResources().getColor(R.color.text_green));
            mCO2DensityUnitText.setTextColor(getResources().getColor(R.color.text_green));
            mCO2DensityImage.setImageResource(R.drawable.co2_green);
        } else if (f < 4000) {
            mCO2DensityText.setTextColor(getResources().getColor(R.color.text_blue));
            mCO2DensityUnitText.setTextColor(getResources().getColor(R.color.text_blue));
            mCO2DensityImage.setImageResource(R.drawable.co2_blue);
        } else if (f < 8000) {
            mCO2DensityText.setTextColor(getResources().getColor(R.color.text_orange));
            mCO2DensityUnitText.setTextColor(getResources().getColor(R.color.text_orange));
            mCO2DensityImage.setImageResource(R.drawable.co2_orange);
        } else {
            mCO2DensityText.setTextColor(getResources().getColor(R.color.text_red));
            mCO2DensityUnitText.setTextColor(getResources().getColor(R.color.text_red));
            mCO2DensityImage.setImageResource(R.drawable.co2_red);
        }
    }

//    public void setSmogWarningState(boolean lifeSmogWarning, boolean deviceSmogWarning) {
//        if (lifeSmogWarning) {
//            mLifeSmogWarningImage.setSelected(true);
//            mLifeSmogWarningText.setText(R.string.life_smoke_warning_text);
//        } else {
//            mLifeSmogWarningImage.setSelected(false);
//            mLifeSmogWarningText.setText(R.string.life_smoke_no_warning);
//        }
//        if (deviceSmogWarning) {
//            mDeviceSmogWarningImage.setSelected(true);
//            mDeviceSmogWarningText.setText(R.string.device_smoke_warning_text);
//        } else {
//            mDeviceSmogWarningImage.setSelected(false);
//            mDeviceSmogWarningText.setText(R.string.device_smoke_no_warning);
//        }
//    }
}
