package com.avatar.newarkluxury;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chx on 2016/12/28.
 */

public class MaintainActivity extends AppCompatActivity implements View.OnClickListener{
    private BroadcastReceiver mReceiver;
    private LinearLayout mEnvironmentStatus, mEnvironmentWarnings, mDoorStatus1, mDoorStatus2,
            mDoorWarnings, mWindowStatus, mWindowWarnings, mEscapeStatus, mEscapeWarnings,
            mControlSystem1, mControlSystem2, mDeviceErrors;
    private ImageView mDisableAlarmImage;
    private TextView mDisableAlarmText, mControlSystemText;
    private int mWhiteColor, mRedColor, mGreenColor;
    private Communicator mCommunicator;
    private boolean mIsControlSystemRunning;
    private Dialog mWarningDialog;
    private int[] mEnvironmentStatusStrings = new int[] {
            R.string.life_cabin_temperature,
            R.string.life_cabin_humidity,
            R.string.o2_density,
            R.string.co2_density,
            R.string.water_in_pressure,
            R.string.water_rest_percent,
            R.string.ups_power_rest,
            R.string.ups_electricity,
            R.string.life_cabin_smog_warning,
//            R.string.device_cabin_smog_warning,
//            R.string.life_cabin_temperature_high,
//            R.string.device_cabin_temperature_high,
            R.string.main_air_condition_running,
            R.string.backup_air_condition_running
    };
    private int[] mEnvironmentWarningStrings = new int[] {
            R.string.o2_density_low_1_item,
            R.string.o2_density_low_2_item,
            R.string.o2_density_low_3_item,
            R.string.co2_density_high_1_item,
            R.string.co2_density_high_2_item,
            R.string.co2_density_high_3_item,
            R.string.ups_load_over_limit,
//            R.string.air_loop_when_emergency,
            R.string.disinfect_over_time_item,
            R.string.life_smoke_warning
//            R.string.device_smoke_warning
    };
    private int[] mDoorStatusStrings = new int[] {
            R.string.door_compressed,
            R.string.door_decompressed,
            R.string.compressing,
            R.string.decompressing,
            R.string.door_open_status,
            R.string.door_lock_status
    };
    private int[] mDoorWarningStrings = new int[] {
            R.string.door_emergency_open,
            R.string.door_emergency_unimapceted,
            R.string.door_manual_in,
            R.string.door_opened_when_impacted
//            R.string.door_pin_failed,
//            R.string.door_wedge_failed,
//            R.string.door_detect_impacted_failed
    };
    private int[] mWindow1StatusStrings = new int[] {
            R.string.window_1_close_status,
            R.string.window_1_middle_position,
            R.string.window_1_open_completely,
            R.string.window_1_opening,
            R.string.window_1_closing,
            R.string.window_1_init
    };
    private int[] mWindow2StatusStrings = new int[] {
            R.string.window_2_close_status,
            R.string.window_2_middle_position,
            R.string.window_2_open_completely,
            R.string.window_2_opening,
            R.string.window_2_closing,
            R.string.window_2_init
    };
    private int[] mWindow3StatusStrings = new int[] {
            R.string.window_3_close_status,
            R.string.window_3_middle_position,
            R.string.window_3_open_completely,
            R.string.window_3_opening,
            R.string.window_3_closing,
            R.string.window_3_init
    };
    private int[] mWindow4StatusStrings = new int[] {
            R.string.window_4_close_status,
            R.string.window_4_middle_position,
            R.string.window_4_open_completely,
            R.string.window_4_opening,
            R.string.window_4_closing,
            R.string.window_4_init
    };
    private int[] mWindow1WarningStrings = new int[] {
            R.string.window_1_emergency_open,
            R.string.window_1_position_lost,
//            R.string.window_1_close_sensor_error,
//            R.string.window_1_open_sensor_error,
            R.string.window_1_manual_mode_on,
            R.string.window_1_over_torque,
            R.string.window_1_error_code
    };
    private int[] mWindow2WarningStrings = new int[] {
            R.string.window_2_emergency_open,
            R.string.window_2_position_lost,
//            R.string.window_2_close_sensor_error,
//            R.string.window_2_open_sensor_error,
            R.string.window_2_manual_mode_on,
            R.string.window_2_over_torque,
            R.string.window_2_error_code
    };
    private int[] mWindow3WarningStrings = new int[] {
            R.string.window_3_emergency_open,
            R.string.window_3_position_lost,
//            R.string.window_3_close_sensor_error,
//            R.string.window_3_open_sensor_error,
            R.string.window_3_manual_mode_on,
            R.string.window_3_over_torque,
            R.string.window_3_error_code
    };
    private int[] mWindow4WarningStrings = new int[] {
            R.string.window_4_emergency_open,
            R.string.window_4_position_lost,
//            R.string.window_4_close_sensor_error,
//            R.string.window_4_open_sensor_error,
            R.string.window_4_manual_mode_on,
            R.string.window_4_over_torque,
            R.string.window_4_error_code
    };
    private int[] mEscapeStatusStrings = new int[] {
            R.string.scuttle_close_status,
            R.string.scuttle_middle_position,
            R.string.scuttle_open_status,
            R.string.escape_middle_position,
            R.string.escape_open_completely,
            R.string.sunfoor_opening,
            R.string.sunfoor_closing,
            R.string.escape_opening,
            R.string.escape_closing,
            R.string.escape_init
    };
    private int[] mEscapeWarningStrings = new int[] {
            R.string.escape_emergency_open,
            R.string.escape_manual_in,
            R.string.escape_anti_clamp,
            R.string.escape_sensor_lost,
            R.string.escape_close_sensor_error,
            R.string.escape_open_sensor_error,
            R.string.escape_mechanism_error,
            R.string.escape_position_lost,
            R.string.escape_over_torque,
            R.string.escape_error_code
    };
    private int[] mControlStatusStrings = new int[] {
            R.string.ele_status,
            R.string.ups_status,
            R.string.control_system_status,
            R.string.in_emergency,
            R.string.in_key_emergency,
            R.string.ventilate_status,//
            R.string.ventilate_level_1,//
            R.string.ventilate_level_2,//
            R.string.water_in,
            R.string.water_in_tank,
            R.string.sewage_out,
            R.string.booster_pump,
            R.string.sewage_pump,
//            R.string.gas_in_valve_1,
//            R.string.gas_in_valve_2,
//            R.string.gas_out_valve,
//            R.string.gas_out_fan,
//            R.string.gas_in_fan_1,
//            R.string.gas_in_fan_2,
//            R.string.primary_light_status,
//            R.string.mood_light_status,
            R.string.germ_light_status,
            R.string.disinfect_prepare,
            R.string.no_disinfect_in_emergency,
            R.string.disinfect_done
    };
    private int[] mDeviceErrorStrings = new int[] {
            R.string.water_rest_sensor_error,
            R.string.water_press_sensor_error,
            R.string.escape_line_sensor_error,
            R.string.temperature_sensor_error,
            R.string.humidity_sensor_error,
            R.string.o2_sensor_error,
            R.string.co2_sensor_error,
            R.string.ups_check_error,
            R.string.water_tank_valve_error,
            R.string.water_out_valve_error,
            R.string.pump_booster_error,
            R.string.pump_sewage_error,
            R.string.gas_in_valve_1_error,
            R.string.gas_in_valve_2_error,
            R.string.gas_out_valve_error,
            R.string.hot_water_in_valve_error,
            R.string.gas_out_left_valve_error,
            R.string.gas_out_right_valve_error
    };
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_maintain);
        initColor();
        initView();
        mCommunicator = Communicator.getInstance();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MachineState state = (MachineState) intent.getSerializableExtra(Communicator.FLAG_GET_STATE);
                onUpdateMachineState(state);
            }
        };
        IntentFilter filter = new IntentFilter(Communicator.ACTION_STATE);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void initView() {
        findViewById(R.id.maintain_window_1_reset).setOnClickListener(this);
        findViewById(R.id.maintain_window_2_reset).setOnClickListener(this);
        findViewById(R.id.maintain_window_3_reset).setOnClickListener(this);
        findViewById(R.id.maintain_window_4_reset).setOnClickListener(this);
        findViewById(R.id.maintain_escape_reset).setOnClickListener(this);
        findViewById(R.id.maintain_under_escape_reset).setOnClickListener(this);
        findViewById(R.id.maintain_door_2_reset).setOnClickListener(this);
        findViewById(R.id.maintain_door_2_time_reset).setOnClickListener(this);
//        findViewById(R.id.maintain_smog_reset).setOnClickListener(this);
        findViewById(R.id.maintain_control_system).setOnClickListener(this);
        findViewById(R.id.maintain_back).setOnClickListener(this);
        mDisableAlarmImage = (ImageView) findViewById(R.id.maintain_disable_alarm);
        mDisableAlarmImage.setOnClickListener(this);
        mDisableAlarmText = (TextView) findViewById(R.id.maintain_disable_alarm_text);
        mControlSystemText = (TextView) findViewById(R.id.maintain_control_system_text);

        mEnvironmentStatus = (LinearLayout) findViewById(R.id.activity_maintain_environment_status);
        mEnvironmentWarnings = (LinearLayout) findViewById(R.id.activity_maintain_environment_warning);
        mDoorStatus1 = (LinearLayout) findViewById(R.id.activity_maintain_door_status_1);
        mDoorStatus2 = (LinearLayout) findViewById(R.id.activity_maintain_door_status_2);
        mDoorWarnings = (LinearLayout) findViewById(R.id.activity_maintain_door_warning);
        mWindowStatus = (LinearLayout) findViewById(R.id.activity_maintain_window_status);
        mWindowWarnings = (LinearLayout) findViewById(R.id.activity_maintain_window_warning);
        mEscapeStatus = (LinearLayout) findViewById(R.id.activity_maintain_escape_status);
        mEscapeWarnings = (LinearLayout) findViewById(R.id.activity_maintain_escape_warning);
        mControlSystem1 = (LinearLayout) findViewById(R.id.activity_maintain_control_system_1);
        mControlSystem2 = (LinearLayout) findViewById(R.id.activity_maintain_control_system_2);
        mDeviceErrors = (LinearLayout) findViewById(R.id.activity_maintain_device_warning);
        mDeviceErrors.post(new Runnable() {
            @Override
            public void run() {
                onUpdateMachineState(new MachineState());
            }
        });
    }

    public void initColor() {
        mWhiteColor = getResources().getColor(R.color.textColor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maintain_window_1_reset:
                mCommunicator.resetWindow1Warning();
                break;
            case R.id.maintain_window_2_reset:
                mCommunicator.resetWindow2Warning();
                break;
            case R.id.maintain_window_3_reset:
                mCommunicator.resetWindow3Warning();
                break;
            case R.id.maintain_window_4_reset:
                mCommunicator.resetWindow4Warning();
                break;
            case R.id.maintain_escape_reset:
                mCommunicator.resetEscapeWarning();
                break;
            case R.id.maintain_under_escape_reset:
                mCommunicator.resetUnderEscapeWarning();
                break;
            case R.id.maintain_door_2_reset:
                mCommunicator.resetDoor2Warning();
                break;
            case R.id.maintain_door_2_time_reset:
                mCommunicator.resetPutterTimeOut();
                break;
            case R.id.maintain_disable_alarm:
                mCommunicator.switchAlarm();
                break;
//            case R.id.maintain_smog_reset:
//                mCommunicator.resetSmogAlarm();
//                break;
            case R.id.maintain_control_system:
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
            case R.id.maintain_back:
                finish();
                break;
        }
    }

    public void setAlarmState(boolean disable) {
        if (disable) {
            mDisableAlarmImage.setSelected(true);
            mDisableAlarmText.setText(R.string.enable_alarm);
        } else {
            mDisableAlarmImage.setSelected(false);
            mDisableAlarmText.setText(R.string.disable_alarm);
        }
    }

    public void setControlSystemText(boolean open) {
        mIsControlSystemRunning = open;
        if (open) {
            mControlSystemText.setText(R.string.close_control_system);
        } else {
            mControlSystemText.setText(R.string.open_control_system);
        }

    }

    public void onUpdateMachineState(MachineState state) {
        if (state == null) {
            return;
        }
        fillEnvironmentData(state);
        fillDoorData(state);
        fillWindow1Data(state);
        fillEscapeData(state);
        fillControlSystemData(state);
        fillDeviceErrorsData(state);
        setControlSystemText(state.isControlSystemRunning());
        setAlarmState(state.isAlarmDisabled());
    }
    /************************************环境状态******************************/
    public void fillEnvironmentData(MachineState state) {
        mEnvironmentStatus.removeAllViews();
        mEnvironmentWarnings.removeAllViews();
        float temperature = state.getTemperature();
        float humidity = state.getHumidity();
        float o2Density = state.getO2Density();
        float co2Density = state.getCO2Density();
        float waterInPressure = state.getWaterInPressure();
        float waterRest = state.getWaterRest();
        float upsPower = state.getPowerRest();
        float upsCurrent = state.getUpsCurrent();
        ArrayList<String> list = new ArrayList<>();
        list.add(FormatUtils.float2String(temperature + 0.5f) + "℃");
        list.add(FormatUtils.float2String(humidity) + "%RH");
        list.add(FormatUtils.float2String(o2Density) + "%");
        list.add(FormatUtils.float2String(co2Density) + "ppm");
        list.add(FormatUtils.float2String(waterInPressure) + "Kpa");
        list.add(FormatUtils.float2String(waterRest) + "%");
        list.add(FormatUtils.float2String(upsPower) + "%");
        list.add(FormatUtils.float2StringWith1(upsCurrent) + "A");
        for (int i = 0; i < 8; i++) {
            mEnvironmentStatus.addView(MaintainItemView.getItemView2(this,
                    MaintainItemView.SHORT_VALUE, mWhiteColor,
                    mEnvironmentStatusStrings[i], list.get(i)));
        }
        mEnvironmentStatus.addView(MaintainItemView.getItemView(this,
                MaintainItemView.SHORT_WARNING, mWhiteColor,
                mEnvironmentStatusStrings[8], state.isLifeSmogWarning()));
//        boolean[] status = state.getEnvironmentStates();
//        for (int i = 0; i < 2; i++) {
//            mEnvironmentStatus.addView(MaintainItemView.getItemView(this,
//                    MaintainItemView.SHORT_WARNING, mWhiteColor,
//                    mEnvironmentStatusStrings[i + 10], status[i]));
//        }
//        for (int i = 2; i < status.length; i++) {
//            mEnvironmentStatus.addView(MaintainItemView.getItemView(this,
//                    MaintainItemView.SHORT_STATE, mWhiteColor,
//                    mEnvironmentStatusStrings[i + 10], status[i]));
//        }
        boolean[] warnings = state.getEnvironmentWarnings();
        for (int i = 0; i < warnings.length; i++) {
            mEnvironmentWarnings.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_WARNING, mWhiteColor,
                    mEnvironmentWarningStrings[i], warnings[i]));
        }

    }

    public void fillDoorData(MachineState state) {
        mDoorStatus1.removeAllViews();
        mDoorStatus2.removeAllViews();
        mDoorWarnings.removeAllViews();
        boolean[] status = state.getDoorStatus();
        boolean[] warnings = state.getDoorWarnings();
        for (int i = 0; i < 2; i++) {
            mDoorStatus1.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_STATE, mWhiteColor,
                    mDoorStatusStrings[i], status[i]));
        }
        for (int i = 2; i < status.length; i++) {
            mDoorStatus2.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_STATE, mWhiteColor,
                    mDoorStatusStrings[i], status[i]));
        }
        for (int i = 0; i < warnings.length; i++) {
            mDoorWarnings.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_WARNING, mWhiteColor,
                    mDoorWarningStrings[i], warnings[i]));
        }
    }

    public void fillWindow1Data(MachineState state) {
        mWindowStatus.removeAllViews();
        mWindowWarnings.removeAllViews();
        boolean[] status = state.getWindow1Status();
        boolean[] warnings = state.getWindow1Warnings();
        for (int i = 0; i < status.length; i++) {
            mWindowStatus.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_STATE, mWhiteColor,
                    mWindow1StatusStrings[i], status[i]));
        }
        for (int i = 0; i < warnings.length; i++) {
            mWindowWarnings.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_WARNING, mWhiteColor,
                    mWindow1WarningStrings[i], warnings[i]));
        }
//        mWindowWarnings.addView(MaintainItemView.getItemView2(this,
//                MaintainItemView.SHORT_VALUE, mWhiteColor,
//                mWindowWarningStrings[6], Integer.toString(state.getWindowErrorCodes())));
    }

    public void fillWindow2Data(MachineState state) {
        mWindowStatus.removeAllViews();
        mWindowWarnings.removeAllViews();
        boolean[] status = state.getWindow2Status();
        boolean[] warnings = state.getWindow2Warnings();
        for (int i = 0; i < status.length; i++) {
            mWindowStatus.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_STATE, mWhiteColor,
                    mWindow2StatusStrings[i], status[i]));
        }
        for (int i = 0; i < warnings.length; i++) {
            mWindowWarnings.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_WARNING, mWhiteColor,
                    mWindow2WarningStrings[i], warnings[i]));
        }
//        mWindowWarnings.addView(MaintainItemView.getItemView2(this,
//                MaintainItemView.SHORT_VALUE, mWhiteColor,
//                mWindowWarningStrings[6], Integer.toString(state.getWindowErrorCodes())));
    }

    public void fillWindow3Data(MachineState state) {
        mWindowStatus.removeAllViews();
        mWindowWarnings.removeAllViews();
        boolean[] status = state.getWindow3Status();
        boolean[] warnings = state.getWindow3Warnings();
        for (int i = 0; i < status.length; i++) {
            mWindowStatus.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_STATE, mWhiteColor,
                    mWindow3StatusStrings[i], status[i]));
        }
        for (int i = 0; i < warnings.length; i++) {
            mWindowWarnings.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_WARNING, mWhiteColor,
                    mWindow3WarningStrings[i], warnings[i]));
        }
//        mWindowWarnings.addView(MaintainItemView.getItemView2(this,
//                MaintainItemView.SHORT_VALUE, mWhiteColor,
//                mWindowWarningStrings[6], Integer.toString(state.getWindowErrorCodes())));
    }

    public void fillWindow4Data(MachineState state) {
        mWindowStatus.removeAllViews();
        mWindowWarnings.removeAllViews();
        boolean[] status = state.getWindow4Status();
        boolean[] warnings = state.getWindow4Warnings();
        for (int i = 0; i < status.length; i++) {
            mWindowStatus.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_STATE, mWhiteColor,
                    mWindow4StatusStrings[i], status[i]));
        }
        for (int i = 0; i < warnings.length; i++) {
            mWindowWarnings.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.SHORT_WARNING, mWhiteColor,
                    mWindow4WarningStrings[i], warnings[i]));
        }
//        mWindowWarnings.addView(MaintainItemView.getItemView2(this,
//                MaintainItemView.SHORT_VALUE, mWhiteColor,
//                mWindowWarningStrings[6], Integer.toString(state.getWindowErrorCodes())));
    }

    public void fillEscapeData(MachineState state) {
        mEscapeStatus.removeAllViews();
        mEscapeWarnings.removeAllViews();
        boolean[] status = state.getEscapeStatus();
        boolean[] warnings = state.getEscapeWarnings();
        for (int i = 0; i < status.length; i++) {
            mEscapeStatus.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.LONG_STATE, mWhiteColor,
                    mEscapeStatusStrings[i], status[i]));
        }
        for (int i = 0; i < warnings.length; i++) {
            mEscapeWarnings.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.LONG_WARNING, mWhiteColor,
                    mEscapeWarningStrings[i], warnings[i]));
        }
//        mEscapeWarnings.addView(MaintainItemView.getItemView2(this,
//                MaintainItemView.LONG_VALUE, mWhiteColor,
//                mEscapeWarningStrings[9], Integer.toString(state.getWindowErrorCodes())));
    }

    public void fillControlSystemData(MachineState state) {
        mControlSystem1.removeAllViews();
        mControlSystem2.removeAllViews();
        boolean[] status = state.getControlSystemStatus();
        for (int i = 0; i < 7; i++) {
            mControlSystem1.addView(MaintainItemView.getItemView(this,
                    MaintainItemView.LONG_STATE, mWhiteColor,
                    mControlStatusStrings[i], status[i]));
        }
        for (int i = 7; i < status.length; i++) {
            if (i == 17) {
                mControlSystem2.addView(MaintainItemView.getItemView(this,
                        MaintainItemView.HEIGHT_SHORT_STATE, mWhiteColor,
                        mControlStatusStrings[i], status[i]));
            } else {
                mControlSystem2.addView(MaintainItemView.getItemView(this,
                        MaintainItemView.SHORT_STATE, mWhiteColor,
                        mControlStatusStrings[i], status[i]));
            }
        }
    }

    public void fillDeviceErrorsData(MachineState state) {
        mDeviceErrors.removeAllViews();
        boolean[] warnings = state.getDeviceErrors();
        for (int i = 0; i < warnings.length; i++) {
            if (i == 7) {
                mDeviceErrors.addView(MaintainItemView.getItemView(this,
                        MaintainItemView.HEIGHT_SHORT_WARNING, mWhiteColor,
                        mDeviceErrorStrings[i], warnings[i]));
            } else {
                mDeviceErrors.addView(MaintainItemView.getItemView(this,
                        MaintainItemView.SHORT_WARNING, mWhiteColor,
                        mDeviceErrorStrings[i], warnings[i]));
            }
        }
    }

    public void showDialog(@StringRes int s, View.OnClickListener listener) {
        if (mWarningDialog == null) {
            mWarningDialog = new Dialog(this, R.style.CustomDialogTheme);
            View root = LayoutInflater.from(this).inflate(
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
}
