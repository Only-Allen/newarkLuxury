package com.avatar.newarkluxury;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chx on 2016/11/21.
 */

public class ControllerFragment extends BaseFragment implements View.OnClickListener {
    private static Logger mLogger = LoggerFactory.getLogger(ControllerFragment.class);
    private Communicator mCommunicator;
    private Handler mHandler = new Handler();

    //emergency
    private ImageView mBackgroundImage, mBorderImage;
    private TextView mEmergencyText;

    //escape
    private ImageView mScuttleImage, mEscapeImage, mEscapeCloseImage;

    //door
    private ImageView mCompressImage, mDecompressImage, mDoorLockImage;
    private ImageView mDoor2Open, mDoor2Close, mDoor2Circle, mDoor2Icon;

    //window
    private TextView mWindow1Text, mWindow2Text, mWindow3Text, mWindow4Text;
    private ImageView mWindow1OpenImage, mWindow2OpenImage, mWindow3OpenImage, mWindow4OpenImage;
    private ImageView mWindow1CloseImage, mWindow2CloseImage, mWindow3CloseImage, mWindow4CloseImage;
//    private ImageView mWindow1Icon;

    //light
    private ImageView mPrimaryIcon, mToiletIcon, mMoodIcon1,
            mMoodIcon2, mOutIcon, mTatamiIcon, /*mPanCircle, mPanIcon, */mLightCircle, mLightIcon;

    //air recycle
//    private ImageView mPanClose, mPanLevel1, mPanLevel2;

    //disinfection and add water
    private ImageView mDisinfectImage, mDisinfectImageLeft, mDisinfectImageRight;
    private ImageView mAddWaterImage, mAddWaterImageLeft, mAddWaterImageRight;

    private TextView mOpenUnderEscape, mCloseUnderEscape;
    private ImageView mPan1Open, mPan1Close, mPan2Open, mPan2Close;

    //control system
    private ImageView mControlSystemImage;
    private AppTextView mControlSystemText;

    //model image
    private ImageView mModelImage, mWindow1ModelImage, mWindow2ModelImage, mWindow3ModelImage,
            mWindow4ModelImage, mDoorModelImage, mDoor2ModelImage, mScuttleModelImage, mUnderEscapeModelImage;

    private List<Integer> mPromptStringIds, mWarningStringIds;
    private static final int PROMPT_NUMBER = 5;//提示的最大条数
    private static final int WARNING_NUMBER = 5;//警告的最大条数

    private LinearLayout mPromptContainer, mWarningContainer;

    private TextView mNetUnavailableText;
    private int mIndexOfString;
    private int mNumberOfNull;
    private static final int DELAY_CONNECTING_NET_TEXT_CHANGE = 1000;
    private static final int NUMBER_OF_NULL_AS_DISCONNECT = 10;

    private boolean mShouldIgnoreState;
    private boolean mIsControlSystemRunning;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommunicator = Communicator.getInstance();
        mPromptStringIds = new ArrayList<>();
        mWarningStringIds = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_controller, container, false);
        initView(v);
        v.post(new Runnable() {
            @Override
            public void run() {
                initText();
                mCommunicator.startCommunicate(getActivity());
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void initView(View v) {
        mWaterRestText = (TextView) v.findViewById(R.id.controller_water_rest_text);
        mPowerRestText = (TextView) v.findViewById(R.id.controller_power_rest_text);
        mTemperatureText = (TextView) v.findViewById(R.id.controller_temperature_text);
        mHumidityText = (TextView) v.findViewById(R.id.controller_humidity_text);
        mO2DensityText = (TextView) v.findViewById(R.id.controller_o2_density_text);
        mO2DensityUnitText = (TextView) v.findViewById(R.id.controller_o2_density_unit_text);
        mCO2DensityText = (TextView) v.findViewById(R.id.controller_co2_density_text);
        mCO2DensityUnitText = (TextView) v.findViewById(R.id.controller_co2_density_unit_text);
        mPowerRestImage = (ImageView) v.findViewById(R.id.controller_power_rest_image);
        mO2DensityImage = (ImageView) v.findViewById(R.id.controller_o2_density_image);
        mCO2DensityImage = (ImageView) v.findViewById(R.id.controller_co2_density_image);
        mScuttleImage = (ImageView) v.findViewById(R.id.controller_scuttle);
        mEscapeImage = (ImageView) v.findViewById(R.id.controller_escape);
        mEscapeCloseImage = (ImageView) v.findViewById(R.id.controller_escape_close);
        mOpenUnderEscape = (TextView) v.findViewById(R.id.controller_open_under_escape);
        mCloseUnderEscape = (TextView) v.findViewById(R.id.controller_close_under_escape);
        mCompressImage = (ImageView) v.findViewById(R.id.controller_compress);
        mDoorLockImage = (ImageView) v.findViewById(R.id.controller_door_lock);
        mDecompressImage = (ImageView) v.findViewById(R.id.controller_decompress);
        mDoor2Open = (ImageView) v.findViewById(R.id.controller_door_2_open);
        mDoor2Close = (ImageView) v.findViewById(R.id.controller_door_2_close);

        mEmergencyImage = (ImageView) v.findViewById(R.id.controller_emergency);
        mSewageImage = (ImageView) v.findViewById(R.id.controller_sewage);
        mOutWarningImage = (ImageView) v.findViewById(R.id.controller_out_warning);
        mVoiceWarningImage = (ImageView) v.findViewById(R.id.controller_voice_warning);
        mDisinfectImage = (ImageView) v.findViewById(R.id.controller_disinfect);
        mDisinfectImageLeft = (ImageView) v.findViewById(R.id.controller_disinfect_left);
        mDisinfectImageRight = (ImageView) v.findViewById(R.id.controller_disinfect_right);
        mAddWaterImage = (ImageView) v.findViewById(R.id.controller_add_water);
        mAddWaterImageLeft = (ImageView) v.findViewById(R.id.controller_add_water_left);
        mAddWaterImageRight = (ImageView) v.findViewById(R.id.controller_add_water_right);
//        mPanClose = (ImageView) v.findViewById(R.id.controller_pan_close);
//        mPanLevel1 = (ImageView) v.findViewById(R.id.controller_pan_level_1);
//        mPanLevel2 = (ImageView) v.findViewById(R.id.controller_pan_level_2);
        mPan1Open = (ImageView) v.findViewById(R.id.controller_pan_1_open);
        mPan1Close = (ImageView) v.findViewById(R.id.controller_pan_1_close);
        mPan2Open = (ImageView) v.findViewById(R.id.controller_pan_2_open);
        mPan2Close = (ImageView) v.findViewById(R.id.controller_pan_2_close);
        mWindow1OpenImage = (ImageView) v.findViewById(R.id.controller_open_window_1);
        mWindow1CloseImage = (ImageView) v.findViewById(R.id.controller_close_window_1);
        mWindow2OpenImage = (ImageView) v.findViewById(R.id.controller_open_window_2);
        mWindow2CloseImage = (ImageView) v.findViewById(R.id.controller_close_window_2);
        mWindow3OpenImage = (ImageView) v.findViewById(R.id.controller_open_window_3);
        mWindow3CloseImage = (ImageView) v.findViewById(R.id.controller_close_window_3);
        mWindow4OpenImage = (ImageView) v.findViewById(R.id.controller_open_window_4);
        mWindow4CloseImage = (ImageView) v.findViewById(R.id.controller_close_window_4);

        mControlSystemImage = (ImageView) v.findViewById(R.id.controller_control_system);
        mControlSystemText = (AppTextView) v.findViewById(R.id.controller_control_system_text);

        startEmergencyAnimation();

        v.findViewById(R.id.controller_language).setOnClickListener(this);
        mEmergencyImage.setOnClickListener(this);
        mSewageImage.setOnClickListener(this);
        mOutWarningImage.setOnClickListener(this);
        mVoiceWarningImage.setOnClickListener(this);
        mScuttleImage.setOnClickListener(this);
        mEscapeImage.setOnClickListener(this);
        mEscapeCloseImage.setOnClickListener(this);
        mOpenUnderEscape.setOnClickListener(this);
        mCloseUnderEscape.setOnClickListener(this);
        mDecompressImage.setOnClickListener(this);
        mDoorLockImage.setOnClickListener(this);
        mCompressImage.setOnClickListener(this);
        mWindow1OpenImage.setOnClickListener(this);
        mWindow1CloseImage.setOnClickListener(this);
        mWindow2OpenImage.setOnClickListener(this);
        mWindow2CloseImage.setOnClickListener(this);
        mWindow3OpenImage.setOnClickListener(this);
        mWindow3CloseImage.setOnClickListener(this);
        mWindow4OpenImage.setOnClickListener(this);
        mWindow4CloseImage.setOnClickListener(this);
        v.findViewById(R.id.controller_primary_light).setOnClickListener(this);
        v.findViewById(R.id.controller_toilet_light).setOnClickListener(this);
        v.findViewById(R.id.controller_mood_light_1).setOnClickListener(this);
        v.findViewById(R.id.controller_mood_light_2).setOnClickListener(this);
        v.findViewById(R.id.controller_out_light).setOnClickListener(this);
        v.findViewById(R.id.controller_tatami_light).setOnClickListener(this);
        v.findViewById(R.id.controller_setting).setOnClickListener(this);
//        mPanClose.setOnClickListener(this);
//        mPanLevel1.setOnClickListener(this);
//        mPanLevel2.setOnClickListener(this);
        mDisinfectImage.setOnClickListener(this);
        mAddWaterImage.setOnClickListener(this);
        mControlSystemImage.setOnClickListener(this);
        mPan1Open.setOnClickListener(this);
        mPan1Close.setOnClickListener(this);
        mPan2Open.setOnClickListener(this);
        mPan2Close.setOnClickListener(this);
        mDoor2Open.setOnClickListener(this);
        mDoor2Close.setOnClickListener(this);

        mWaveView = (WaveView) v.findViewById(R.id.controller_wave_view);
        mWaveHelper = new WaveHelper(mWaveView);

        mSlideView = (ImageView) v.findViewById(R.id.controller_slide_view);
        mSlideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setPageAt(1);
            }
        });

        mModelImage = (ImageView) v.findViewById(R.id.controller_model);
        mWindow1ModelImage = (ImageView) v.findViewById(R.id.control_window_1);
        mWindow2ModelImage = (ImageView) v.findViewById(R.id.control_window_2);
        mWindow3ModelImage = (ImageView) v.findViewById(R.id.control_window_3);
        mWindow4ModelImage = (ImageView) v.findViewById(R.id.control_window_4);
        mScuttleModelImage = (ImageView) v.findViewById(R.id.control_scuttle);
        mUnderEscapeModelImage = (ImageView) v.findViewById(R.id.control_under_escape);
        mDoorModelImage = (ImageView) v.findViewById(R.id.control_door);
        mDoor2ModelImage = (ImageView) v.findViewById(R.id.control_door_2);
        mBackgroundImage = (ImageView) v.findViewById(R.id.controller_emergency_background);
        mBorderImage = (ImageView) v.findViewById(R.id.controller_emergency_border);
        mEmergencyText = (TextView) v.findViewById(R.id.controller_emergency_text);

//        mDoorCircle = (ImageView) v.findViewById(R.id.controller_door_circle);
//        mDoorIcon = (ImageView) v.findViewById(R.id.controller_door_icon);
        mDoor2Circle = (ImageView) v.findViewById(R.id.controller_door_2_circle);
        mDoor2Icon = (ImageView) v.findViewById(R.id.controller_door_2_icon);
        mLightCircle = (ImageView) v.findViewById(R.id.controller_light_circle);
        mLightIcon = (ImageView) v.findViewById(R.id.controller_light_icon);
        mPrimaryIcon = (ImageView) v.findViewById(R.id.controller_primary_icon);
        mToiletIcon = (ImageView) v.findViewById(R.id.controller_toilet_icon);
        mMoodIcon1 = (ImageView) v.findViewById(R.id.controller_mood_icon_1);
        mMoodIcon2 = (ImageView) v.findViewById(R.id.controller_mood_icon_2);
        mOutIcon = (ImageView) v.findViewById(R.id.controller_out_icon);
        mTatamiIcon = (ImageView) v.findViewById(R.id.controller_tatami_icon);
//        mWindow1Icon = (ImageView) v.findViewById(R.id.controller_window_1_icon);

        mWindow1Text = (TextView) v.findViewById(R.id.controller_window_1_text);
        mWindow2Text = (TextView) v.findViewById(R.id.controller_window_2_text);
        mWindow3Text = (TextView) v.findViewById(R.id.controller_window_3_text);
        mWindow4Text = (TextView) v.findViewById(R.id.controller_window_4_text);

        mWarningContainer = (LinearLayout) v.findViewById(R.id.controller_warning_container);
        mPromptContainer = (LinearLayout) v.findViewById(R.id.controller_prompt_container);

//        mLifeSmogWarningImage = (ImageView) v.findViewById(R.id.controller_life_area_alarm_image);
//        mDeviceSmogWarningImage = (ImageView) v.findViewById(R.id.controller_device_area_alarm_image);
//        mLifeSmogWarningText = (TextView) v.findViewById(R.id.controller_life_area_alarm_text);
//        mDeviceSmogWarningText = (TextView) v.findViewById(R.id.controller_device_area_alarm_text);
//        startSmogAnimation();
//        mPromptContainer.post(new Runnable() {
//            @Override
//            public void run() {
//                onUpdateMachineState(new MachineState());
//            }
//        });
        //just to see UI, will be deleted soon
//        v.post(new Runnable() {
//            @Override
//            public void run() {
//                setWaterRest(70);
//                setMode(1);
//                setLightState(true, true, true, false, true, false);
//            }
//        });
    }

    @Override
    public void onClick(final View v) {
        v.setEnabled(false);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setEnabled(true);
            }
        }, 100);
        mShouldIgnoreState = true;
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                mShouldIgnoreState = false;
            }
        }, 300);
        mCommunicator.reStartCommunicate();
        switch (v.getId()) {
            case R.id.controller_language:
                changeLanguage();
                break;
            case R.id.controller_emergency:
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
            case R.id.controller_sewage:
                if (mSewageImage.isSelected()) {
                    mSewageImage.setSelected(false);
                } else {
                    mSewageImage.setSelected(true);
                }
                mCommunicator.handleSewage();
                break;
            case R.id.controller_scuttle:
                mCommunicator.openScuttle();
                break;
            case R.id.controller_escape:
                mCommunicator.openEscape();
                break;
            case R.id.controller_escape_close:
                mCommunicator.closeEscape();
                break;
            case R.id.controller_decompress:
                mCommunicator.decompressDoor();
                break;
            case R.id.controller_door_lock:
                mCommunicator.lockDoor();
                break;
            case R.id.controller_compress:
                showDialog(R.string.if_compress_door, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCommunicator.compressDoor();
                        dismissWarningDialog();
                    }
                });
                break;
            case R.id.controller_open_window_1:
                mCommunicator.openWindowAt(1);
                break;
            case R.id.controller_close_window_1:
                mCommunicator.closeWindowAt(1);
                break;
            case R.id.controller_open_window_2:
                mCommunicator.openWindowAt(2);
                break;
            case R.id.controller_close_window_2:
                mCommunicator.closeWindowAt(2);
                break;
            case R.id.controller_open_window_3:
                mCommunicator.openWindowAt(3);
                break;
            case R.id.controller_close_window_3:
                mCommunicator.closeWindowAt(3);
                break;
            case R.id.controller_open_window_4:
                mCommunicator.openWindowAt(4);
                break;
            case R.id.controller_close_window_4:
                mCommunicator.closeWindowAt(4);
                break;
            case R.id.controller_primary_light:
                mCommunicator.switchPrimaryLight();
                if (mPrimaryIcon.isSelected()) {
                    mPrimaryIcon.setSelected(false);
                } else {
                    mPrimaryIcon.setSelected(true);
                }
                break;
            case R.id.controller_toilet_light:
                mCommunicator.switchToiletLight();
                if (mToiletIcon.isSelected()) {
                    mToiletIcon.setSelected(false);
                } else {
                    mToiletIcon.setSelected(true);
                }
                break;
            case R.id.controller_mood_light_1:
                mCommunicator.switchMoodLight1();
                if (mMoodIcon1.isSelected()) {
                    mMoodIcon1.setSelected(false);
                } else {
                    mMoodIcon1.setSelected(true);
                }
                break;
            case R.id.controller_mood_light_2:
                mCommunicator.switchMoodLight2();
                if (mMoodIcon2.isSelected()) {
                    mMoodIcon2.setSelected(false);
                } else {
                    mMoodIcon2.setSelected(true);
                }
                break;
            case R.id.controller_out_light:
                mCommunicator.switchOutLight();
                if (mOutIcon.isSelected()) {
                    mOutIcon.setSelected(false);
                } else {
                    mOutIcon.setSelected(true);
                }
                break;
            case R.id.controller_tatami_light:
                mCommunicator.switchTatamiLight();
                if (mTatamiIcon.isSelected()) {
                    mTatamiIcon.setSelected(false);
                } else {
                    mTatamiIcon.setSelected(true);
                }
                break;
//            case R.id.controller_pan_close:
//                mPanClose.setSelected(true);
//                if (mPanLevel1.isSelected()) {
//                    mPanLevel1.setSelected(false);
//                    mCommunicator.changeWindGears();
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mCommunicator.changeWindGears();
//                        }
//                    }, 100);
//                } else if (mPanLevel2.isSelected()) {
//                    mPanLevel2.setSelected(false);
//                    mCommunicator.changeWindGears();
//                }
//                setPanState(0);
//                break;
//            case R.id.controller_pan_level_1:
//                mPanLevel1.setSelected(true);
//                if (mPanClose.isSelected()) {
//                    mPanClose.setSelected(false);
//                    mCommunicator.changeWindGears();
//                } else if (mPanLevel2.isSelected()) {
//                    mPanLevel2.setSelected(false);
//                    mCommunicator.changeWindGears();
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mCommunicator.changeWindGears();
//                        }
//                    }, 100);
//                }
//                setPanState(1);
//                break;
//            case R.id.controller_pan_level_2:
//                mPanLevel2.setSelected(true);
//                if (mPanClose.isSelected()) {
//                    mPanClose.setSelected(false);
//                    mCommunicator.changeWindGears();
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mCommunicator.changeWindGears();
//                        }
//                    }, 100);
//                } else if (mPanLevel1.isSelected()) {
//                    mPanLevel1.setSelected(false);
//                    mCommunicator.changeWindGears();
//                }
//                setPanState(2);
//                break;
            case R.id.controller_disinfect:
                mCommunicator.switchGermLight();
                if (mDisinfectImageLeft.isSelected()) {
                    setDisinfectState(false);
                } else {
                    setDisinfectState(true);
                }
                break;
            case R.id.controller_add_water:
                mCommunicator.addWaterOrStop();
                if (mAddWaterImageLeft.isSelected()) {
                    setAddWaterState(false);
                } else {
                    setAddWaterState(true);
                }
                break;
            case R.id.controller_setting:
                startStatusActivity();
                break;
            case R.id.controller_control_system:
                if (mIsControlSystemRunning) {
                    showDialog(R.string.if_close_control_system, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCommunicator.switchControlSystem();
                            dismissWarningDialog();
                        }
                    });
                } else {
                    showDialog(R.string.if_open_control_system, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCommunicator.switchControlSystem();
                            dismissWarningDialog();
                        }
                    });
                }
                break;
            case R.id.controller_open_under_escape:
                mCommunicator.openUnderEscape();
                break;
            case R.id.controller_close_under_escape:
                mCommunicator.closeUnderEscape();
                break;
            case R.id.controller_door_2_open:
                mCommunicator.openDoor2();
                break;
            case R.id.controller_door_2_close:
                mCommunicator.closeDoor2();
                break;
            case R.id.controller_pan_1_open:
            case R.id.controller_pan_1_close:
                mCommunicator.switchWindGears1();
                break;
            case R.id.controller_pan_2_open:
            case R.id.controller_pan_2_close:
                mCommunicator.switchWindGears2();
                break;
            case R.id.controller_out_warning:
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
            case R.id.controller_voice_warning:
                if (mVoiceWarningImage.isSelected())  {
                    mCommunicator.switchVoiceWarning();
                    mVoiceWarningImage.setSelected(false);
                } else {
                    showDialog(R.string.if_open_voice_warning, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCommunicator.switchVoiceWarning();
                            mVoiceWarningImage.setSelected(true);
                            dismissWarningDialog();
                        }
                    });
                }
                break;
            default:
        }
        mCommunicator.reStartCommunicate();
    }

/**************************************    UI    *************************************************/

    @Override
    public void onUpdateMachineState(MachineState state) {
        if (state == null) {
            if (mNumberOfNull >= NUMBER_OF_NULL_AS_DISCONNECT) {
                showNetUnavailable();
            }
            mNumberOfNull++;
            return;
        }
        mNumberOfNull = 0;
        if (mShouldIgnoreState) {
            return;
        }
        setPanState(state.getPanState());
        setTemperature(state.getTemperature());
        setHumidity(state.getHumidity());
        setPowerRest(state.getPowerRest());
        setWaterRest(state.getWaterRest());
        setO2Density(state.getO2Density());
        setCO2Density(state.getCO2Density());
//        setSmogWarningState(state.isLifeSmogWarning(), state.isDeviceSmogWarning());
        setLightState(state.isPrimaryLightOn(), state.isMoodLight1On(), state.isMoodLight2On(),
                state.isToiletLightOn(), state.isOutLightOn(), state.isTatamiLightOn());
        setWindowsState(state.getWindowStateAt(1), state.getWindowStateAt(2),
                state.getWindowStateAt(3), state.getWindowStateAt(4));
        setEscapeState(state.getEscapeState(), state.getScuttleState());
        setUnderEscapeState(state.getUnderEscapeState());
        setDoorState(state.isDoorCompressed(), state.isDoorDecompressed(),
                state.isDoorLocked(), state.isDoorOpened());
        setDoor2State(state.getDoor2State());
        setControlSystem(state.isControlSystemRunning());
        setDisinfectState(state.isGermLightOn());
        setAddWaterState(state.isAddingWater());
        setErrorImage(state);
        setEmergencyMode(state.isInEmergency(), state.isSewageOn());
        setOutWarningState(state.isLightWarningOn(), state.isVoiceWarningOn());
        fillPromptText(state);
        fillWarningText(state);
        showPromptAndWarning();
    }

    public void setPanState(boolean[] states) {
        if (states[0]) {
            mPan1Open.setEnabled(false);
            mPan1Close.setEnabled(true);
        } else {
            mPan1Open.setEnabled(true);
            mPan1Close.setEnabled(false);
        }
        if (states[1]) {
            mPan2Open.setEnabled(false);
            mPan2Close.setEnabled(true);
        } else {
            mPan2Open.setEnabled(true);
            mPan2Close.setEnabled(false);
        }
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
                mBorderImage.setVisibility(View.INVISIBLE);
                mModelImage.setImageResource(R.drawable.model_big);
//                mEmergencyImage.setSelected(false);
                break;
            case 1: //emergency
                mEmergencyText.setVisibility(View.VISIBLE);
                mEmergencyText.setTextColor(getResources().getColor(R.color.text_red));
                mEmergencyText.setText(R.string.in_emergency_mode);
                mEmergencyText.setShadowLayer(4F, 0F,0F, getResources().getColor(R.color.text_red));
                mBackgroundImage.setVisibility(View.VISIBLE);
                mBackgroundImage.setImageResource(R.drawable.controller_emergency_background);
                mBorderImage.setVisibility(View.VISIBLE);
                mModelImage.setImageResource(R.drawable.controller_emergency_model);
//                mEmergencyImage.setSelected(true);
                break;
            case 2: //sewage
                mEmergencyText.setVisibility(View.VISIBLE);
                mEmergencyText.setTextColor(getResources().getColor(R.color.text_orange));
                mEmergencyText.setText(R.string.in_sewage_mode);
                mEmergencyText.setShadowLayer(4F, 0F,0F, getResources().getColor(R.color.text_orange));
                mBackgroundImage.setVisibility(View.VISIBLE);
                mBackgroundImage.setImageResource(R.drawable.controller_sewage_background);
                mBorderImage.setVisibility(View.INVISIBLE);
                mModelImage.setImageResource(R.drawable.controller_sewage_model);
                break;
            default:
                mLogger.error("get error mode...");
        }
    }

    public void setLightState(boolean primaryOn, boolean toiletOn, boolean mood1On,
                              boolean mood2On, boolean outOn, boolean tatamiOn) {
        if (primaryOn) {
            mPrimaryIcon.setSelected(true);
        } else {
            mPrimaryIcon.setSelected(false);
        }
        if (toiletOn) {
            mToiletIcon.setSelected(true);
        } else {
            mToiletIcon.setSelected(false);
        }
        if (mood1On) {
            mMoodIcon1.setSelected(true);
        } else {
            mMoodIcon1.setSelected(false);
        }
        if (mood2On) {
            mMoodIcon2.setSelected(true);
        } else {
            mMoodIcon2.setSelected(false);
        }
        if (outOn) {
            mOutIcon.setSelected(true);
        } else {
            mOutIcon.setSelected(false);
        }
        if (tatamiOn) {
            mTatamiIcon.setSelected(true);
        } else {
            mTatamiIcon.setSelected(false);
        }
        if (primaryOn || toiletOn || mood1On || mood2On || outOn || tatamiOn) {
            mLightCircle.setSelected(true);
            mLightIcon.setSelected(true);
        } else {
            mLightCircle.setSelected(false);
            mLightIcon.setSelected(false);
        }
    }

    public void setWindowsState(MachineState.State window1State, MachineState.State window2State,
                                MachineState.State window3State, MachineState.State window4State) {
        int lightColor = getResources().getColor(R.color.kinds);
        int darkColor = getResources().getColor(R.color.window_open);
        mWindow1OpenImage.setSelected(false);
        mWindow1CloseImage.setSelected(false);
//        mWindow1Icon.setSelected(true);
        mWindow2OpenImage.setSelected(false);
        mWindow2CloseImage.setSelected(false);
        mWindow3OpenImage.setSelected(false);
        mWindow3CloseImage.setSelected(false);
        mWindow4OpenImage.setSelected(false);
        mWindow4CloseImage.setSelected(false);
        mWindow1ModelImage.setImageResource(R.drawable.control_window_1_opened);
        mWindow2ModelImage.setImageResource(R.drawable.control_window_2_opened);
        mWindow3ModelImage.setImageResource(R.drawable.control_window_3_opened);
        mWindow4ModelImage.setImageResource(R.drawable.control_window_4_opened);
        if (window1State == MachineState.State.INIT) {
            mWindow1Text.setTextColor(lightColor);
            mWindow1Text.setShadowLayer(0F, 0F,0F, lightColor);
            mWindow1OpenImage.setSelected(true);
            mWindow1CloseImage.setSelected(true);
        } else if (window1State == MachineState.State.CLOSED) {
            mWindow1Text.setTextColor(lightColor);
            mWindow1Text.setShadowLayer(0F, 0F,0F, lightColor);
//            mWindow1Icon.setSelected(false);
            mWindow1CloseImage.setSelected(true);
            mWindow1ModelImage.setImageBitmap(null);
        } else if (window1State == MachineState.State.OPENED) {
            mWindow1Text.setTextColor(darkColor);
            mWindow1Text.setShadowLayer(20F, 0F,0F, darkColor);
            mWindow1OpenImage.setSelected(true);
        }

        if (window2State == MachineState.State.INIT) {
            mWindow2Text.setTextColor(lightColor);
            mWindow2Text.setShadowLayer(0F, 0F,0F, lightColor);
            mWindow2OpenImage.setSelected(true);
            mWindow2CloseImage.setSelected(true);
        } else if (window2State == MachineState.State.CLOSED) {
            mWindow2Text.setTextColor(lightColor);
            mWindow2Text.setShadowLayer(0F, 0F,0F, lightColor);
            mWindow2CloseImage.setSelected(true);
            mWindow2ModelImage.setImageBitmap(null);
        } else if (window2State == MachineState.State.OPENED) {
            mWindow2Text.setTextColor(darkColor);
            mWindow2Text.setShadowLayer(20F, 0F,0F, darkColor);
            mWindow2OpenImage.setSelected(true);
        } else {
            mWindow2Text.setTextColor(darkColor);
            mWindow2Text.setShadowLayer(20F, 0F,0F, darkColor);
        }

        if (window3State == MachineState.State.INIT) {
            mWindow3Text.setTextColor(lightColor);
            mWindow3Text.setShadowLayer(0F, 0F,0F, lightColor);
            mWindow3OpenImage.setSelected(true);
            mWindow3CloseImage.setSelected(true);
        } else if (window3State == MachineState.State.CLOSED) {
            mWindow3Text.setTextColor(lightColor);
            mWindow3Text.setShadowLayer(0F, 0F,0F, lightColor);
            mWindow3CloseImage.setSelected(true);
            mWindow3ModelImage.setImageBitmap(null);
        } else if (window3State == MachineState.State.OPENED) {
            mWindow3Text.setTextColor(darkColor);
            mWindow3Text.setShadowLayer(20F, 0F,0F, darkColor);
            mWindow3OpenImage.setSelected(true);
        } else {
            mWindow3Text.setTextColor(darkColor);
            mWindow3Text.setShadowLayer(20F, 0F,0F, darkColor);
        }

        if (window4State == MachineState.State.INIT) {
            mWindow4Text.setTextColor(lightColor);
            mWindow4Text.setShadowLayer(0F, 0F,0F, lightColor);
            mWindow4OpenImage.setSelected(true);
            mWindow4CloseImage.setSelected(true);
        } else if (window4State == MachineState.State.CLOSED) {
            mWindow4Text.setTextColor(lightColor);
            mWindow4Text.setShadowLayer(0F, 0F,0F, lightColor);
            mWindow4CloseImage.setSelected(true);
            mWindow4ModelImage.setImageBitmap(null);
        } else if (window4State == MachineState.State.OPENED) {
            mWindow4Text.setTextColor(darkColor);
            mWindow4Text.setShadowLayer(20F, 0F,0F, darkColor);
            mWindow4OpenImage.setSelected(true);
        } else {
            mWindow4Text.setTextColor(darkColor);
            mWindow4Text.setShadowLayer(20F, 0F,0F, darkColor);
        }
    }

    public void setDoorState(boolean isDoorCompressed, boolean isDoorDecompressed,
                             boolean isDoorLocked, boolean isDoorOpened) {
        mCompressImage.setSelected(false);
        mDecompressImage.setSelected(false);
        mDoorLockImage.setSelected(false);
        if (isDoorCompressed) {
            mCompressImage.setSelected(true);
        }
        if (isDoorDecompressed){
            mDecompressImage.setSelected(true);
        }
        if (isDoorLocked) {
            mDoorLockImage.setSelected(true);
        }
        if (isDoorOpened) {
            mDoorModelImage.setImageResource(R.drawable.control_door_opened);
        } else {
            mDoorModelImage.setImageBitmap(null);
        }
    }

    public void setDoor2State(boolean[] state) {
        mDoor2ModelImage.setImageResource(R.drawable.control_door_2_opened);
        if (state[0]) {
            mDoor2Circle.setSelected(true);
            mDoor2Icon.setSelected(true);
        } else if (state[1]) {
            mDoor2ModelImage.setImageBitmap(null);
            mDoor2Circle.setSelected(false);
            mDoor2Icon.setSelected(false);
        } else {
            mDoor2Circle.setSelected(true);
            mDoor2Icon.setSelected(true);
        }
    }

    public void setEscapeState(MachineState.State escapeState, MachineState.State scuttleState) {
        mScuttleImage.setSelected(false);
        mEscapeImage.setSelected(false);
        mEscapeCloseImage.setSelected(false);
        mScuttleModelImage.setImageResource(R.drawable.control_scuttle_opened);
        if (escapeState == MachineState.State.OPENED) {
            mEscapeImage.setSelected(true);
        } else {
            if (scuttleState == MachineState.State.OPENED) {
                mScuttleImage.setSelected(true);
            }
        }
        if (scuttleState == MachineState.State.CLOSED) {
            mEscapeCloseImage.setSelected(true);
            mScuttleModelImage.setImageBitmap(null);
        }
    }

    public void setUnderEscapeState(boolean[] state) {
        mUnderEscapeModelImage.setImageResource(R.drawable.control_under_scuttle_opened);
        if (state[0]) {
            mOpenUnderEscape.setSelected(true);
            mCloseUnderEscape.setSelected(false);
        } else if (state[1]) {
            mUnderEscapeModelImage.setImageBitmap(null);
            mOpenUnderEscape.setSelected(false);
            mCloseUnderEscape.setSelected(true);
        } else {
            mOpenUnderEscape.setSelected(false);
            mCloseUnderEscape.setSelected(false);
        }
    }

    public void setControlSystem(boolean run) {
        mIsControlSystemRunning = run;
        if (run) {
            mControlSystemText.setTextById(R.string.close_control_system);
        } else {
            mControlSystemText.setTextById(R.string.open_control_system);
        }
    }

    public void setDisinfectState(boolean state) {
        if (state) {
            mDisinfectImageLeft.setSelected(true);
            mDisinfectImageRight.setSelected(true);
        } else {
            mDisinfectImageLeft.setSelected(false);
            mDisinfectImageRight.setSelected(false);
        }
    }

    public void setAddWaterState(boolean state) {
        if (state) {
            mAddWaterImageLeft.setSelected(true);
            mAddWaterImageRight.setSelected(true);
        } else {
            mAddWaterImageLeft.setSelected(false);
            mAddWaterImageRight.setSelected(false);
        }
    }
    
    public void setErrorImage(MachineState state) {
        if (state.isWindow1Error()) {
            mWindow1ModelImage.setImageResource(R.drawable.control_window_1_error);
        }
        if (state.isWindow2Error()) {
            mWindow2ModelImage.setImageResource(R.drawable.control_window_2_error);
        }
        if (state.isWindow3Error()) {
            mWindow3ModelImage.setImageResource(R.drawable.control_window_3_error);
        }
        if (state.isWindow4Error()) {
            mWindow4ModelImage.setImageResource(R.drawable.control_window_4_error);
        }
        if (state.isDoorError()) {
            mDoorModelImage.setImageResource(R.drawable.control_door_error);
        }
        if (state.isDoor2Error()) {
            mDoor2ModelImage.setImageResource(R.drawable.control_door_2_error);
        }
        if (state.isEscapeError()) {
            mScuttleModelImage.setImageResource(R.drawable.control_scuttle_error);
        }
        if (state.isUnderEscapeError()) {
            mUnderEscapeModelImage.setImageResource(R.drawable.control_scuttle_error);
        }
    }

    public TextView getPromptOrWarningTextView(int stringId, boolean warning) {
        AppTextView textView = new AppTextView(getActivity());
        textView.setTextById(stringId);
        textView.setTextSize(14);
        if (warning) {
            textView.setTextColor(getResources().getColor(R.color.text_red));
            textView.setBackgroundResource(R.drawable.warning_text_border);
        } else {
            textView.setTextColor(getResources().getColor(R.color.text_blue));
            textView.setBackgroundResource(R.drawable.prompt_text_border);
        }
        setTextFont(textView);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return textView;
    }

    public void fillPromptText(MachineState state) {
        ArrayList<Integer> promptStringIds = state.getPromptStringIds();
        for (int i = 0; i < mPromptStringIds.size(); i++) {
            Integer prompt = mPromptStringIds.get(i);
            if (!promptStringIds.contains(prompt)) {
                mPromptStringIds.remove(prompt);
                i--;
            }
        }
        for (int i : promptStringIds) {
            if (!mPromptStringIds.contains(i)) {
                mPromptStringIds.add(0, i);
            }
        }

    }

    public void fillWarningText(MachineState state) {
        ArrayList<Integer> warningStringIds = state.getWarningStringIds();
        for (int i = 0; i < mWarningStringIds.size(); i++) {
            Integer warning = mWarningStringIds.get(i);
            if (!warningStringIds.contains(warning)) {
                mWarningStringIds.remove(warning);
                i--;
            }
        }
        for (int i : warningStringIds) {
            if (!mWarningStringIds.contains(i)) {
                mWarningStringIds.add(0, i);
            }
        }
    }

    public void showPromptAndWarning() {
        int promptNum = mPromptStringIds.size() > PROMPT_NUMBER ?
                PROMPT_NUMBER : mPromptStringIds.size();
        if (promptNum == 0) {
            mPromptContainer.setVisibility(View.GONE);
        } else {
            mPromptContainer.removeAllViews();
            for (int i = 0; i < promptNum; i++) {
                mPromptContainer.addView(getPromptOrWarningTextView(mPromptStringIds.get(i), false));
            }
            mPromptContainer.setVisibility(View.VISIBLE);
        }


        int warningNum = mWarningStringIds.size() > WARNING_NUMBER ?
                WARNING_NUMBER : mWarningStringIds.size();
        if (warningNum == 0) {
            mWarningContainer.setVisibility(View.GONE);
        } else {
            mWarningContainer.removeAllViews();
            for (int i = 0; i < warningNum; i++) {
                mWarningContainer.addView(getPromptOrWarningTextView(mWarningStringIds.get(i), true));
            }
            mWarningContainer.setVisibility(View.VISIBLE);
        }
    }

    private void showNetUnavailable() {
        mWarningContainer.setVisibility(View.VISIBLE);
        mWarningContainer.removeAllViews();
        if (mNetUnavailableText == null) {
            mNetUnavailableText = getPromptOrWarningTextView(R.string.connecting_net, true);
            mIndexOfString = 0;
            mNetUnavailableText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mIndexOfString == 0) {
                        mIndexOfString = 1;
                        mNetUnavailableText.setText(R.string.connecting_net1);
                    } else if (mIndexOfString == 1) {
                        mIndexOfString = 2;
                        mNetUnavailableText.setText(R.string.connecting_net2);
                    } else {
                        mIndexOfString = 0;
                        mNetUnavailableText.setText(R.string.connecting_net);
                    }
                    mNetUnavailableText.postDelayed(this, DELAY_CONNECTING_NET_TEXT_CHANGE);
                }
            }, DELAY_CONNECTING_NET_TEXT_CHANGE);
        }
        mWarningContainer.addView(mNetUnavailableText);
    }

}