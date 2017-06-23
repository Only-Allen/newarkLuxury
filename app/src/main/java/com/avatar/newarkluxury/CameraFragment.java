package com.avatar.newarkluxury;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xm.ChnInfo;
import com.xm.DevInfo;
import com.xm.MyConfig;
import com.xm.NetSdk;
import com.xm.video.MySurfaceView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Created by Administrator on 2016/11/21.
 */

public class CameraFragment extends BaseFragment implements View.OnClickListener, Handler.Callback{

    private static Logger mLogger = LoggerFactory.getLogger(CameraFragment.class);
    private int mSocketStyle = MyConfig.SocketStyle.TCPSOCKET;
    private WndsHolder mWndsHolder;
    private MyNetSdk mNetSdk;
    private NetSdk.OnDisConnectListener mDisconnectListener;
    protected final int NUMBER_OF_CAMERAS = 9;
    protected final int NUMBER_OF_SURFACE_VIEW = 9;
    private ValueAnimator[] animBigs, animSmalls;

    private	long[] mLoginId = new long[NUMBER_OF_CAMERAS];
    private String[] IP_CAMERAS = new String[] {"192.168.1.10", "192.168.1.11",
            "192.168.1.12", "192.168.1.13", "192.168.1.14", "192.168.1.15",
            "192.168.1.16", "192.168.1.17", "192.168.1.18"};

    private Handler mHandler;
    private static final int HANDLER_PLAY = 0;
    private Communicator mCommunicator;

    private RelativeLayout mSurfaceViewContainer;
    private RelativeLayout.LayoutParams mTmpParams;
    private ViewParent[] mParents;
    private RelativeLayout.LayoutParams[] mParams;

    private ImageView mModelImage, mWindow1ModelImage, mWindow2ModelImage, mWindow3ModelImage,
            mWindow4ModelImage, mDoorModelImage, mDoor2ModelImage, mScuttleModelImage,
            mUnderEscapeModelImage, mBackgroundImage;
    private TextView mEmergencyText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);
        mCommunicator = Communicator.getInstance();
        mWndsHolder = new WndsHolder(NUMBER_OF_SURFACE_VIEW);
        mWndsHolder.vvs[0] = (MySurfaceView) v.findViewById(R.id.mfv1);
        mWndsHolder.vvs[1] = (MySurfaceView) v.findViewById(R.id.mfv2);
        mWndsHolder.vvs[2] = (MySurfaceView) v.findViewById(R.id.mfv3);
        mWndsHolder.vvs[3] = (MySurfaceView) v.findViewById(R.id.mfv4);
        mWndsHolder.vvs[4] = (MySurfaceView) v.findViewById(R.id.mfv5);
        mWndsHolder.vvs[5] = (MySurfaceView) v.findViewById(R.id.mfv6);
        mWndsHolder.vvs[6] = (MySurfaceView) v.findViewById(R.id.mfv7);
        mWndsHolder.vvs[7] = (MySurfaceView) v.findViewById(R.id.mfv8);
        mWndsHolder.vvs[8] = (MySurfaceView) v.findViewById(R.id.mfv9);
        for (int i = 0; i < NUMBER_OF_SURFACE_VIEW; i++) {
            mWndsHolder.vvs[i].init(getActivity(), i);
            mWndsHolder.vvs[i].setOnClickListener(this);
        }
        mSurfaceViewContainer = (RelativeLayout) v.findViewById(R.id.camera_container);
        initView(v);
        init();
        v.post(new Runnable() {
            @Override
            public void run() {
                initText();
                initAnimations();
                setWaterRest(20);//just to see UI, will be deleted soon
//                setMode(2);
            }
        });
        return v;
    }
    
    public void initView(View v) {
        mWaterRestText = (TextView) v.findViewById(R.id.camera_water_rest_text);
        mPowerRestText = (TextView) v.findViewById(R.id.camera_power_rest_text);
        mTemperatureText = (TextView) v.findViewById(R.id.camera_temperature_text);
        mHumidityText = (TextView) v.findViewById(R.id.camera_humidity_text);
        mO2DensityText = (TextView) v.findViewById(R.id.camera_o2_density_text);
        mO2DensityUnitText = (TextView) v.findViewById(R.id.camera_o2_density_unit_text);
        mCO2DensityText = (TextView) v.findViewById(R.id.camera_co2_density_text);
        mCO2DensityUnitText = (TextView) v.findViewById(R.id.camera_co2_density_unit_text);
        mPowerRestImage = (ImageView) v.findViewById(R.id.camera_power_rest_image);
        mO2DensityImage = (ImageView) v.findViewById(R.id.camera_o2_density_image);
        mCO2DensityImage = (ImageView) v.findViewById(R.id.camera_co2_density_image);

        mEmergencyImage = (ImageView) v.findViewById(R.id.camera_emergency);
        mSewageImage = (ImageView) v.findViewById(R.id.camera_sewage);
        mOutWarningImage = (ImageView) v.findViewById(R.id.camera_out_warning);
        mEmergencyImage.setOnClickListener(this);
        mSewageImage.setOnClickListener(this);
        mOutWarningImage.setOnClickListener(this);
        startEmergencyAnimation();

        v.findViewById(R.id.camera_setting).setOnClickListener(this);
        v.findViewById(R.id.camera_language).setOnClickListener(this);

        mWaveView = (WaveView) v.findViewById(R.id.camera_wave_view);
        mWaveHelper = new WaveHelper(mWaveView);

        mSlideView = (ImageView) v.findViewById(R.id.camera_slide_view);
        mSlideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setPageAt(0);
            }
        });

        mBackgroundImage = (ImageView) v.findViewById(R.id.camera_emergency_background);
        mModelImage = (ImageView) v.findViewById(R.id.camera_model);
        mWindow1ModelImage = (ImageView) v.findViewById(R.id.camera_window_1);
        mWindow2ModelImage = (ImageView) v.findViewById(R.id.camera_window_2);
        mWindow3ModelImage = (ImageView) v.findViewById(R.id.camera_window_3);
        mWindow4ModelImage = (ImageView) v.findViewById(R.id.camera_window_4);
        mScuttleModelImage = (ImageView) v.findViewById(R.id.camera_scuttle);
        mUnderEscapeModelImage = (ImageView) v.findViewById(R.id.camera_under_escape);
        mDoorModelImage = (ImageView) v.findViewById(R.id.camera_door);
        mDoor2ModelImage = (ImageView) v.findViewById(R.id.camera_door_2);
        mEmergencyText = (TextView) v.findViewById(R.id.camera_emergency_text);

//        mLifeSmogWarningImage = (ImageView) v.findViewById(R.id.camera_life_area_alarm_image);
//        mDeviceSmogWarningImage = (ImageView) v.findViewById(R.id.camera_device_area_alarm_image);
//        mLifeSmogWarningText = (TextView) v.findViewById(R.id.camera_life_area_alarm_text);
//        mDeviceSmogWarningText = (TextView) v.findViewById(R.id.camera_device_area_alarm_text);
//        startSmogAnimation();
//        mEmergencyText.post(new Runnable() {
//            @Override
//            public void run() {
//                onUpdateMachineState(new MachineState());
//            }
//        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLER_PLAY:
                break;
            default:
        }
        return false;
    }

    public void init() {
        mHandler = new Handler(this);
        NetSdk.DevInit();
        mNetSdk = MyNetSdk.getInstance();
        mDisconnectListener = new NetSdk.OnDisConnectListener() {
            @Override
            public void onDisConnect(int i, long l, byte[] bytes, long l1) {
                for (int ii = 0; ii < NUMBER_OF_CAMERAS; ii++) {
                    if (l == mLoginId[ii]) {
                        mWndsHolder.vvs[ii].onStop();
                        final int num = ii;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mWndsHolder.vvs[num].setBackgroundResource(R.drawable.camera_back);
                            }
                        });
                        new CameraThread(ii).start();
                    }
                }
            }
        };
        mNetSdk.setMyOnDisConnectListener(mDisconnectListener);
        for (int i = 0; i < NUMBER_OF_CAMERAS; i++) {
            new CameraThread(i).start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mfv1:
                onAnimatorStart(0);
                break;
            case R.id.mfv2:
                onAnimatorStart(1);
                break;
            case R.id.mfv3:
                onAnimatorStart(2);
                break;
            case R.id.mfv4:
                onAnimatorStart(3);
                break;
            case R.id.mfv5:
                onAnimatorStart(4);
                break;
            case R.id.mfv6:
                onAnimatorStart(5);
                break;
            case R.id.mfv7:
                onAnimatorStart(6);
                break;
            case R.id.mfv8:
                onAnimatorStart(7);
                break;
            case R.id.mfv9:
                onAnimatorStart(8);
                break;
            case R.id.camera_emergency:
                if (mEmergencyImage.isSelected()) {
                    mCommunicator.sendEmergencyRequest(false);
                    mEmergencyImage.setSelected(false);
                } else {
                    showDialog(R.string.if_open_emergency_mode, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCommunicator.sendEmergencyRequest(true);
                            mEmergencyImage.setSelected(true);
                            dismissWarningDialog();
                        }
                    });
                }
                break;
            case R.id.camera_sewage:
                if (mSewageImage.isSelected()) {
                    mSewageImage.setSelected(false);
                } else {
                    mSewageImage.setSelected(true);
                }
                mCommunicator.handleSewage();
                break;
            case R.id.camera_language:
                changeLanguage();
                break;
            case R.id.camera_setting:
                startStatusActivity();
                break;
            case R.id.camera_out_warning:
                if (mOutWarningImage.isSelected()) {
                    mCommunicator.switchOutWarning();
                    mOutWarningImage.setSelected(false);
                } else {
                    showDialog(R.string.if_open_out_warning, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCommunicator.switchOutWarning();
                            mOutWarningImage.setSelected(true);
                            dismissWarningDialog();
                        }
                    });
                }
                break;
            default:
        }
    }

    private boolean startPing(String ip) {
//        Log.e("Ping", "startPing...");
        boolean success = false;
        Process p = null;

        try {
            p = Runtime.getRuntime().exec("ping -c 1 -i 0.2 -W 1 " + ip);
            int status = p.waitFor();
            if (status == 0) {
                success = true;
            } else {
                success = false;
            }
        } catch (IOException e) {
            success = false;
        } catch (InterruptedException e) {
            success = false;
        } finally {
            p.destroy();
        }

        return success;
    }

    class CameraThread extends Thread {
        private int mNumber;

        public CameraThread(int number) {
            mNumber = number;
        }

        @Override
        public void run() {
            DevInfo devInfo = new DevInfo();
            devInfo.Ip = IP_CAMERAS[mNumber];
            devInfo.TCPPort = 34567;
            devInfo.UserName = "admin".getBytes();
            devInfo.PassWord = "";
            int[] mLoginError = new int[1];
            mLoginId[mNumber] = mNetSdk.onLoginDevNat(0, devInfo, mLoginError, mSocketStyle);
            mLogger.info("the login id is: " + mLoginId[mNumber] + ", error is: " + mLoginError[0]);
            while ((mLoginError[0] <= -10000 && mLoginError[0] >= -99999) || mLoginId[mNumber] == 0) {
                mLogger.info("login failed: id=" + mLoginId[mNumber] + ";error=" + mLoginError[0] + "! retry...");
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    mLogger.error("thread sleep error:\n", e);
                }
                mLoginId[mNumber] = mNetSdk.onLoginDevNat(0, devInfo, mLoginError, mSocketStyle);
            }
            mNetSdk.SetupAlarmChan(mLoginId[mNumber]);
            mNetSdk.SetAlarmMessageCallBack();
            ChnInfo chnInfo = new ChnInfo();
            chnInfo.ChannelNo = 0;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWndsHolder.vvs[mNumber].setBackground(null);
                }
            });
            long playHandle = mNetSdk.onRealPlay(mNumber, mLoginId[mNumber], chnInfo);
            mWndsHolder.vvs[mNumber].onPlay();
            mNetSdk.setDataCallback(playHandle);
            mNetSdk.setReceiveCompleteVData(0, true);
//            mNetSdk.setOnSubDisConnectListener(new NetSdk.OnSubDisConnectListener() {
//                @Override
//                public void onSubDisConnect(long l, int i, long l1) {
//                    mLogger.info("onSubDisConnect");
//                }
//            });
        }
    }

    public void initAnimations() {
        int numberOfAnimations = NUMBER_OF_SURFACE_VIEW;
        mParents = new ViewGroup[numberOfAnimations];
        mParams = new RelativeLayout.LayoutParams[numberOfAnimations];
        float scale = 1920 / mWndsHolder.vvs[0].getWidth();
        mTmpParams = new RelativeLayout.LayoutParams(1, 1);
        animBigs = new ValueAnimator[numberOfAnimations];
        animSmalls = new ValueAnimator[numberOfAnimations];
        for (int i = 0; i < numberOfAnimations; i++) {
            mParents[i] = mWndsHolder.vvs[i].getParent();
            mParams[i] = (RelativeLayout.LayoutParams)mWndsHolder.vvs[i].getLayoutParams();
            final RelativeLayout layout = (RelativeLayout) mParents[i];
            final MySurfaceView view = mWndsHolder.vvs[i];
            final RelativeLayout.LayoutParams params = mParams[i];


//            ObjectAnimator transX = ObjectAnimator.ofFloat(mWndsHolder.vvs[i], "translationX", mWndsHolder.vvs[i].getTranslationX(), 0f);
//            ObjectAnimator transY = ObjectAnimator.ofFloat(mWndsHolder.vvs[i], "translationY", mWndsHolder.vvs[i].getTranslationY(), 0f);
//            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mWndsHolder.vvs[i], "scaleX", 1f, scale);
//            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mWndsHolder.vvs[i], "scaleY", 1f, scale);
            animBigs[i] = ValueAnimator.ofFloat(0f, 1f);
            animBigs[i].setDuration(500);
            animBigs[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float f = (float) animation.getAnimatedValue();
                    if (f == 0) {
                        view.setEnabled(false);
                        mTmpParams.width = params.width;
                        mTmpParams.height = params.height;
                        mTmpParams.setMargins(params.getMarginStart(), params.topMargin,
                                params.getMarginEnd(), params.bottomMargin);
                        ((RelativeLayout)view.getParent()).removeView(view);
                        view.setLayoutParams(mTmpParams);
                        mSurfaceViewContainer.addView(view);
                        mSurfaceViewContainer.setBackgroundColor(Color.parseColor("#A6000000"));
                        mSurfaceViewContainer.setClickable(true);
                        removeOtherSurfaceView(view);
//                        view.setZOrderOnTop(true);
                        view.bringToFront();
                        view.setZOrderMediaOverlay(true);
                    } else {
                        int width = (int) (params.width + (1920 - params.width) * f);
                        int height = (int) (params.height + (1080 - params.height) * f);
                        int start = (int) (params.getMarginStart() * (1 - f));
                        int top = (int) (params.topMargin - (params.topMargin - 60) * (f));
                        mTmpParams.width = width;
                        mTmpParams.height = height;
                        mTmpParams.setMarginStart(start);
                        mTmpParams.topMargin = top;
                        view.setLayoutParams(mTmpParams);
                        if (f == 1) {
                            view.setEnabled(true);
                        }
                    }
                }
            });


//            ObjectAnimator transX = ObjectAnimator.ofFloat(mWndsHolder.vvs[i], "translationX", 0f, mWndsHolder.vvs[i].getTranslationX());
//            ObjectAnimator transY = ObjectAnimator.ofFloat(mWndsHolder.vvs[i], "translationY", 0f, mWndsHolder.vvs[i].getTranslationY());
//            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mWndsHolder.vvs[i], "scaleX", scale, 1f);
//            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mWndsHolder.vvs[i], "scaleY", scale, 1f);
            animSmalls[i] = ValueAnimator.ofFloat(0, 1);
            animSmalls[i].setDuration(500);
            animSmalls[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float f = (float) animation.getAnimatedValue();
                    if (f == 1) {
//                        params.width = mTmpParams.width;
//                        params.height = mTmpParams.height;
//                        params.setMargins(mTmpParams.getMarginStart(), mTmpParams.topMargin,
//                                mTmpParams.getMarginEnd(), mTmpParams.bottomMargin);
//                        mSurfaceViewContainer.removeView(view);
                        ((RelativeLayout)view.getParent()).removeView(view);
                        view.setLayoutParams(params);
                        layout.addView(view);
                        mSurfaceViewContainer.setBackgroundColor(Color.TRANSPARENT);
                        mSurfaceViewContainer.setClickable(false);
//                        view.setZOrderOnTop(false);
                        resumeOtherSurfaceView(view);
                        view.setZOrderMediaOverlay(false);
                        view.setEnabled(true);
                    } else {
                        if (f == 0) {
                            view.setEnabled(false);
                        }
                        int width = (int) (params.width + (1920 - params.width) * (1 -f));
                        int height = (int) (params.height + (1080 - params.height) * (1 -f));
                        int start = (int) (params.getMarginStart() * (f));
                        int top = (int) (int) (params.topMargin - (params.topMargin - 60) * (1 - f));
                        mTmpParams.width = width;
                        mTmpParams.height = height;
                        mTmpParams.setMarginStart(start);
                        mTmpParams.topMargin = top;
                        view.setLayoutParams(mTmpParams);
                    }
                }
            });
        }
    }

    public void onAnimatorStart(int i) {
        if (mWndsHolder.vvs[i].isSelected()) {
            mWndsHolder.vvs[i].setSelected(false);
            animSmalls[i].start();
        } else {
            mWndsHolder.vvs[i].setSelected(true);
            animBigs[i].start();
        }
    }

    public void removeOtherSurfaceView(MySurfaceView view) {
        for (int i = 0; i < NUMBER_OF_SURFACE_VIEW; i++) {
            if (mWndsHolder.vvs[i] != view) {
                ((ViewGroup) mParents[i]).removeView(mWndsHolder.vvs[i]);
            }
        }
    }

    public void resumeOtherSurfaceView(MySurfaceView view) {
        for (int i = 0; i < NUMBER_OF_SURFACE_VIEW; i++) {
            if (mWndsHolder.vvs[i] != view) {
                ((ViewGroup) mParents[i]).addView(mWndsHolder.vvs[i]);
            }
        }
    }
    public static class WndsHolder {
        MySurfaceView[] vvs;
        public WndsHolder(int number) {
            vvs = new MySurfaceView[number];
        }
    }

    @Override
    public void onUpdateMachineState(MachineState state) {
        if (state == null) {
            return;
        }
        setTemperature(state.getTemperature());
        setHumidity(state.getHumidity());
        setPowerRest(state.getPowerRest());
        setWaterRest(state.getWaterRest());
        setO2Density(state.getO2Density());
        setCO2Density(state.getCO2Density());
//        setSmogWarningState(state.isLifeSmogWarning(), state.isDeviceSmogWarning());
        setEmergencyMode(state.isInEmergency(), state.isSewageOn());
        setModelState(state);
    }

    public void setEmergencyMode(boolean emergency, boolean sewage) {
        if (emergency) {
            mEmergencyImage.setSelected(true);
        } else {
            mEmergencyImage.setSelected(false);
        }
        if (sewage) {
            mSewageImage.setSelected(true);
        } else {
            mSewageImage.setSelected(false);
        }
        if (emergency) {
            setMode(1);
        } else if (sewage) {
            setMode(2);
        } else {
            setMode(0);
        }
    }

    public void setMode(int mode) {
        switch (mode) {
            case 0: //normal
                mEmergencyText.setVisibility(View.INVISIBLE);
                mBackgroundImage.setVisibility(View.INVISIBLE);
                mModelImage.setImageResource(R.drawable.model_small);
//                mEmergencyImage.setSelected(false);
                break;
            case 1: //emergency
                mEmergencyText.setVisibility(View.VISIBLE);
                mEmergencyText.setTextColor(getResources().getColor(R.color.text_red));
                mEmergencyText.setText(R.string.in_emergency_mode);
                mEmergencyText.setShadowLayer(4F, 0F,0F, getResources().getColor(R.color.text_red));
                mBackgroundImage.setVisibility(View.VISIBLE);
                mBackgroundImage.setImageResource(R.drawable.camera_emergency_background);
                mModelImage.setImageResource(R.drawable.camera_emergency_model);
//                mEmergencyImage.setSelected(true);
                break;
            case 2: //sewage
                mEmergencyText.setVisibility(View.VISIBLE);
                mEmergencyText.setTextColor(getResources().getColor(R.color.text_orange));
                mEmergencyText.setText(R.string.in_sewage_mode);
                mEmergencyText.setShadowLayer(4F, 0F,0F, getResources().getColor(R.color.text_orange));
                mBackgroundImage.setVisibility(View.VISIBLE);
                mBackgroundImage.setImageResource(R.drawable.camera_sewage_background);
                mModelImage.setImageResource(R.drawable.camera_sewage_model);
                break;
            default:
                mLogger.error("get error mode...");
        }
    }
    
    public void setModelState(MachineState state) {
        mWindow1ModelImage.setImageResource(R.drawable.camera_window_1_opened);
        mWindow2ModelImage.setImageResource(R.drawable.camera_window_2_opened);
        mWindow3ModelImage.setImageResource(R.drawable.camera_window_3_opened);
        mWindow4ModelImage.setImageResource(R.drawable.camera_window_4_opened);
        mDoorModelImage.setImageResource(R.drawable.camera_door_opened);
        mDoor2ModelImage.setImageResource(R.drawable.camera_door_2_opened);
        mScuttleModelImage.setImageResource(R.drawable.camera_scuttle_opened);
        mUnderEscapeModelImage.setImageResource(R.drawable.camera_under_scuttle_opened);
        // if closed
        if (state.getWindowStateAt(1) == MachineState.State.CLOSED) {
            mWindow1ModelImage.setImageBitmap(null);
        }
        if (state.getWindowStateAt(2) == MachineState.State.CLOSED) {
            mWindow2ModelImage.setImageBitmap(null);
        }
        if (state.getWindowStateAt(3) == MachineState.State.CLOSED) {
            mWindow3ModelImage.setImageBitmap(null);
        }
        if (state.getWindowStateAt(4) == MachineState.State.CLOSED) {
            mWindow4ModelImage.setImageBitmap(null);
        }
        if (state.getDoorState() == MachineState.DoorState.CLOSED) {
            mDoorModelImage.setImageBitmap(null);
        }
        if (state.getDoor2State()[1]) {
            mDoor2ModelImage.setImageBitmap(null);
        }
        if (state.getScuttleState() == MachineState.State.CLOSED) {
            mScuttleModelImage.setImageBitmap(null);
        }
        if (state.getUnderEscapeState()[1]) {
            mUnderEscapeModelImage.setImageBitmap(null);
        }
        //if error
        if (state.isWindow1Error()) {
            mWindow1ModelImage.setImageResource(R.drawable.camera_window_1_error);
        }
        if (state.isWindow2Error()) {
            mWindow2ModelImage.setImageResource(R.drawable.camera_window_2_error);
        }
        if (state.isWindow3Error()) {
            mWindow3ModelImage.setImageResource(R.drawable.camera_window_3_error);
        }
        if (state.isWindow4Error()) {
            mWindow4ModelImage.setImageResource(R.drawable.camera_window_4_error);
        }
        if (state.isDoorError()) {
            mDoorModelImage.setImageResource(R.drawable.camera_door_error);
        }
        if (state.isDoor2Error()) {
            mDoor2ModelImage.setImageResource(R.drawable.camera_door_2_error);
        }
        if (state.isEscapeError()) {
            mScuttleModelImage.setImageResource(R.drawable.camera_scuttle_error);
        }
        if (state.isUnderEscapeError()) {
            mUnderEscapeModelImage.setImageResource(R.drawable.camera_scuttle_error);
        }

    }
}
