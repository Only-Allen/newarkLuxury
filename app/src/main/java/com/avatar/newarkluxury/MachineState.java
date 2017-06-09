package com.avatar.newarkluxury;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by chx on 2016/12/16.
 */

public class MachineState implements Serializable {
    public enum State {
        CLOSED, MIDDLE, OPENED, CLOSING, OPENING, INIT
    }

    public enum DoorState {
        COMPRESSING, DECOMPRESSING, OPENED, CLOSED
    }

    //state
    private boolean mIsDoorCompressed;
    private boolean mIsDoorDecompressed;
    private boolean mIsDoorLocked;
    private float mTemperature;
    private float mHumidity;
    private float mO2Density;
    private float mCO2Density;
    private float mWaterInPressure;
    private float mWaterRest;
    private float mPowerRest;
    private float mUpsCurrent;

    private DoorState mDoorState;
    private boolean mIsDoorOpened;
    private boolean mIsDoor2Opened;
    private boolean mIsDoor2Closed;
    private boolean mIsUnderEscapeOpened;
    private boolean mIsUnderEscapeClosed;

    private State mEscapeState;
    private State mScuttleState;
    private State mWindowState1;
    private State mWindowState2;
    private State mWindowState3;
    private State mWindowState4;

    private boolean mSystemRunning;
    private boolean mFanRunning;
    private boolean mIsFan1Running;
    private boolean mIsFan2Running;
    private boolean mWaterIn;
    private boolean mWaterTank;
    private boolean mSeWageOut;
    private boolean mBoosterPump;
    private boolean mSewagePump;
//    private boolean mGasOutValve;
//    private boolean mGasToiletOutValve;
//    private boolean mGasInFan;
//    private boolean mGasToiletOutFan;
    private boolean mPrimaryLight;
    private boolean mMoodLight1;
    private boolean mMoodLight2;
//    private boolean mGasInValve1;
//    private boolean mGasInValve2;
    private boolean mGermLight;
    private boolean mToiletLight;
    private boolean mTatamiLight;
    private boolean mOutLight;

    private boolean mWaterPressLow;
    private boolean mWaterTankLow;
    private boolean mEleState;
    private boolean mUpsState;

    //warning
    private boolean mO2DensityLow1;
    private boolean mO2DensityLow2;
    private boolean mO2DensityLow3;
    private boolean mUpsOverLimit;
//    private boolean mEmergencyAirLoop;
    private boolean mSewageOn;
    private boolean mWaterTankOverTime;
    private boolean mDisinfectOverTime;

    private boolean mInEmergency;
    private boolean mInKeyEmergency;

    private boolean mWaterRestSensorError;
    private boolean mWaterPressSensorError;
    private boolean mEscapeSensorError;
    private boolean mTemperatureSensorError;
    private boolean mHumiditySensorError;
    private boolean mO2SensorError;
    private boolean mCO2SensorError;
    private boolean mUPSSensorError;
//    private boolean mPowerRestError;
//    private boolean mWaterInValveError;
    private boolean mWaterTankValveError;
    private boolean mSewageOutError;
    private boolean mPumpBoosterError;
    private boolean mPumpSewageError;
    private boolean mGasInValve1Error;
    private boolean mGasInValve2Error;
    private boolean mGasOutValveError;//tatami
    private boolean mHotWaterInValveError;
    private boolean mGasOutLeftValveError;
    private boolean mGasOutRightValveError;
//    private boolean mGasToiletOutValveError;
//    private boolean mGasInFanError;
//    private boolean mAirControllerError;

    private boolean mDoorEmergencyOpened;
    private boolean mDoorEmergencyUnimpacted;
    private boolean mDoorManualEn;
    private boolean mDoorOpenedWhenImpacting;
//    private boolean mDoorPinAutoFailed;
//    private boolean mDoorWedgeAutoFailed;
//    private boolean mDoorImpactFailed;

    private boolean mEscapeEmergencyOpened;
    private boolean mEscapeManualEn;
    private boolean mEscapeAntiClamp;
    private boolean mEscapeSensorLost;
    private boolean mEscapeCloseSensorError;
    private boolean mEscapeOpenSensorError;
    private boolean mEscapeMechanismError;
    private boolean mEscapePositionLost;
    private boolean mEscapeOverTorque;

    private boolean mWindow1EmergencyOpened;
    private boolean mWindow1PositionLost;
    private boolean mWindow1ManualEn;
//    private boolean mWindow1CloseSensorError;
//    private boolean mWindow1OpenSensorError;
    private boolean mWindow1OverTorque;

    private boolean mWindow2EmergencyOpened;
    private boolean mWindow2PositionLost;
    private boolean mWindow2ManualEn;
//    private boolean mWindow2CloseSensorError;
//    private boolean mWindow2OpenSensorError;
    private boolean mWindow2OverTorque;

    private boolean mWindow3EmergencyOpened;
    private boolean mWindow3PositionLost;
    private boolean mWindow3ManualEn;
//    private boolean mWindow3CloseSensorError;
//    private boolean mWindow3OpenSensorError;
    private boolean mWindow3OverTorque;

    private boolean mWindow4EmergencyOpened;
    private boolean mWindow4PositionLost;
    private boolean mWindow4ManualEn;
//    private boolean mWindow4CloseSensorError;
//    private boolean mWindow4OpenSensorError;
    private boolean mWindow4OverTorque;

    private boolean mLifeSmokeWarning;
//    private boolean mDeviceSmokeWarning;
    private boolean mWaterPressLowWhenAdd;
    private boolean mWaterTankLowWhenAdd;

    private boolean mCO2DensityHigh1;
    private boolean mCO2DensityHigh2;
    private boolean mCO2DensityHigh3;

    private boolean mIsAddingWater; //水箱正在补水
    private boolean mAddingWaterHand; //手动补水

    private boolean mIsDisinfectPrepare; //消毒准备
    private boolean mNoDisinfectInEmergency; //紧急模式不允许消毒
    private boolean mNoAddWaterInEmergency; //紧急模式不允许补水
    private boolean mSewageNeedInEmergency; //紧急排污在紧急模式下使用
    private boolean mIsAlarmDisabled; //屏蔽警示
    private boolean mIsDisinfectDone; //消毒完成

    private int mWindow1ErrorCode;
    private int mWindow2ErrorCode;
    private int mWindow3ErrorCode;
    private int mWindow4ErrorCode;
    private int mEscapeErrorCode;
    private int mDoor2ErrorCode;
    private int mUnderEscapeErrorCode;

    public void fillData(int start,int value) {
        switch (start) {
            case Constant.STATE_1003:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mTatamiLight = true;
                }
                if ((value & Constant.VALUE_1_BIT) != 0) {
                    mOutLight = true;
                }
                break;
            case Constant.STATE_1363:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState1 = State.CLOSED;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState2 = State.CLOSED;
                }
                break;
            case Constant.STATE_1364:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState3 = State.CLOSED;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState4 = State.CLOSED;
                }
                break;
            case Constant.STATE_1365:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState1 = State.MIDDLE;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState2 = State.MIDDLE;
                }
                break;
            case Constant.STATE_1366:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState3 = State.MIDDLE;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState4 = State.MIDDLE;
                }
                break;
            case Constant.STATE_1367:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState1 = State.OPENED;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState2 = State.OPENED;
                }
                break;
            case Constant.STATE_1368:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState3 = State.OPENED;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState4 = State.OPENED;
                }
                break;
            case Constant.STATE_1369:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState1 = State.CLOSING;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState2 = State.CLOSING;
                }
                break;
            case Constant.STATE_1370:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState3 = State.CLOSING;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState4 = State.CLOSING;
                }
                break;
            case Constant.STATE_1371:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState1 = State.OPENING;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState2 = State.OPENING;
                }
                break;
            case Constant.STATE_1372:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState3 = State.OPENING;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState4 = State.OPENING;
                }
                break;
            case Constant.STATE_1373:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState1 = State.INIT;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState2 = State.INIT;
                }
                break;
            case Constant.STATE_1374:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindowState3 = State.INIT;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindowState4 = State.INIT;
                }
                break;
            case Constant.STATE_1375:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mPrimaryLight = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mMoodLight1 = true;
                }
                break;
            case Constant.STATE_1376:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mMoodLight2 = true;
                }
                break;
            case Constant.STATE_1380:
                if ((value & Constant.VALUE_2_BIT) != 0) {
                    mIsAddingWater = true;
                }
                if ((value & Constant.VALUE_3_BIT) != 0) {
                    mIsDoorCompressed = true;
                }
                if ((value & Constant.VALUE_6_BIT) != 0) {
                    mIsDoorOpened = true;
                } else {
                    mIsDoorOpened = false;
                }
                if ((value & Constant.VALUE_7_BIT) != 0) {
                    mIsDoorLocked = true;
                } else {
                    mIsDoorLocked = false;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mScuttleState = State.CLOSED;
                }
                if ((value & Constant.VALUE_9_BIT) != 0) {
                    mScuttleState = State.MIDDLE;
                }
                if ((value & Constant.VALUE_10_BIT) != 0) {
                    mScuttleState = State.OPENED;
                }
                if ((value & Constant.VALUE_11_BIT) != 0) {
                    mEscapeState = State.MIDDLE;
                }
                if ((value & Constant.VALUE_12_BIT) != 0) {
                    mEscapeState = State.OPENED;
                }
                if ((value & Constant.VALUE_13_BIT) != 0) {
                    mScuttleState = State.CLOSING;
                }
                if ((value & Constant.VALUE_14_BIT) != 0) {
                    mScuttleState = State.OPENING;
                }
                if ((value & Constant.VALUE_15_BIT) != 0) {
                    mEscapeState = State.CLOSING;
                }
                break;
            case Constant.STATE_1381:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mEscapeState = State.OPENING;
                }
                if ((value & Constant.VALUE_1_BIT) != 0) {
                    mEscapeState = State.INIT;
                }
                if ((value & Constant.VALUE_2_BIT) != 0) {
                    mSystemRunning = true;
                }
                if ((value & Constant.VALUE_3_BIT) != 0) {
                    mFanRunning = true;
                }
                if ((value & Constant.VALUE_4_BIT) != 0) {
                    mIsFan1Running = true;
                }
                if ((value & Constant.VALUE_5_BIT) != 0) {
                    mIsFan2Running = true;
                }
                if ((value & Constant.VALUE_6_BIT) != 0) {
                    mWaterIn = true;
                }
                if ((value & Constant.VALUE_7_BIT) != 0) {
                    mWaterTank = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mSeWageOut = true;
                }
                if ((value & Constant.VALUE_9_BIT) != 0) {
                    mBoosterPump = true;
                }
                if ((value & Constant.VALUE_10_BIT) != 0) {
                    mSewagePump = true;
                }
                break;
            case Constant.STATE_1415:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mGermLight = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mToiletLight = true;
                }
                break;
            case Constant.STATE_1416:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mIsDisinfectPrepare = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mNoDisinfectInEmergency = true;
                }
                break;
            case Constant.STATE_1417:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mO2DensityLow1 = true;
                }
                if ((value & Constant.VALUE_1_BIT) != 0) {
                    mO2DensityLow2 = true;
                }
                if ((value & Constant.VALUE_2_BIT) != 0) {
                    mO2DensityLow3 = true;
                }
                if ((value & Constant.VALUE_3_BIT) != 0) {
                    mWaterPressLowWhenAdd = true;
                }
                if ((value & Constant.VALUE_4_BIT) != 0) {
                    mWaterTankLowWhenAdd = true;
                }
                if ((value & Constant.VALUE_5_BIT) != 0) {
                    mWaterPressLow = true;
                }
                if ((value & Constant.VALUE_6_BIT) != 0) {
                    mWaterTankLow = true;
                }
                if ((value & Constant.VALUE_7_BIT) != 0) {
                    mCO2DensityHigh1 = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mCO2DensityHigh2 = true;
                }
                if ((value & Constant.VALUE_9_BIT) != 0) {
                    mCO2DensityHigh3 = true;
                }
                if ((value & Constant.VALUE_11_BIT) != 0) {
                    mWaterTankValveError = true;
                }
                if ((value & Constant.VALUE_13_BIT) != 0) {
                    mSewageOutError = true;
                }
                break;
            case Constant.STATE_1418:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mGasInValve1Error = true;
                }
                if ((value & Constant.VALUE_1_BIT) != 0) {
                    mGasInValve2Error = true;
                }
                if ((value & Constant.VALUE_4_BIT) != 0) {
                    mGasOutValveError = true;
                }
                if ((value & Constant.VALUE_7_BIT) != 0) {
                    mEleState = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mUpsState = true;
                }
                break;
            case Constant.STATE_1452:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mAddingWaterHand = true;
                }
                break;
            case Constant.STATE_1473:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mNoAddWaterInEmergency = true;
                }
                if ((value & Constant.VALUE_2_BIT) != 0) {
                    mSewageNeedInEmergency = true;
                }
                if ((value & Constant.VALUE_3_BIT) != 0) {
                    mIsDisinfectDone = true;
                }
                if ((value & Constant.VALUE_4_BIT) != 0) {
                    mIsDoorDecompressed = true;
                }
                if ((value & Constant.VALUE_5_BIT) != 0) {
                    mIsAlarmDisabled = true;
                }
                break;
            case Constant.STATE_1490:
                if ((value & Constant.VALUE_1_BIT) != 0) {
                    mIsDoor2Opened = true;
                }
                if ((value & Constant.VALUE_2_BIT) != 0) {
                    mIsDoor2Closed = true;
                }
                if ((value & Constant.VALUE_3_BIT) != 0) {
                    mIsUnderEscapeOpened = true;
                }
                if ((value & Constant.VALUE_4_BIT) != 0) {
                    mIsUnderEscapeClosed = true;
                }
                break;
            case Constant.WARNING_1325:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindow1EmergencyOpened = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindow2EmergencyOpened = true;
                }
                break;
            case Constant.WARNING_1326:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindow3EmergencyOpened = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindow4EmergencyOpened = true;
                }
                break;
            case Constant.WARNING_1329:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindow1PositionLost = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindow2PositionLost = true;
                }
                break;
            case Constant.WARNING_1330:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindow3PositionLost = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindow4PositionLost = true;
                }
                break;
            case Constant.WARNING_1331:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindow1ManualEn = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindow2ManualEn = true;
                }
                break;
            case Constant.WARNING_1332:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindow3ManualEn = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindow4ManualEn = true;
                }
                break;
            case Constant.WARNING_1335:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindow1OverTorque = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindow2OverTorque = true;
                }
                break;
            case Constant.WARNING_1336:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mWindow3OverTorque = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWindow4OverTorque = true;
                }
                break;
            case Constant.WARNING_1378:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mUpsOverLimit = true;
                }
                if ((value & Constant.VALUE_2_BIT) != 0) {
                    mSewageOn = true;
                }
                if ((value & Constant.VALUE_3_BIT) != 0) {
                    mWaterTankOverTime = true;
                }
                if ((value & Constant.VALUE_4_BIT) != 0) {
                    mDisinfectOverTime = true;
                }
                if ((value & Constant.VALUE_5_BIT) != 0) {
                    mInEmergency = true;
                }
                if ((value & Constant.VALUE_6_BIT) != 0) {
                    mInKeyEmergency = true;
                }
                if ((value & Constant.VALUE_7_BIT) != 0) {
                    mWaterRestSensorError = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mWaterPressSensorError = true;
                }
                if ((value & Constant.VALUE_9_BIT) != 0) {
                    mEscapeSensorError = true;
                }
                if ((value & Constant.VALUE_10_BIT) != 0) {
                    mTemperatureSensorError = true;
                }
                if ((value & Constant.VALUE_11_BIT) != 0) {
                    mHumiditySensorError = true;
                }
                if ((value & Constant.VALUE_12_BIT) != 0) {
                    mO2SensorError = true;
                }
                if ((value & Constant.VALUE_13_BIT) != 0) {
                    mCO2SensorError = true;
                }
                if ((value & Constant.VALUE_14_BIT) != 0) {
                    mUPSSensorError = true;
                }
                if ((value & Constant.VALUE_15_BIT) != 0) {
                    mDoorEmergencyOpened = true;
                }
                break;
            case Constant.WARNING_1379:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mDoorEmergencyUnimpacted = true;
                }
                if ((value & Constant.VALUE_1_BIT) != 0) {
                    mDoorManualEn = true;
                }
                if ((value & Constant.VALUE_2_BIT) != 0) {
                    mDoorOpenedWhenImpacting = true;
                }
                if ((value & Constant.VALUE_3_BIT) != 0) {
                    mEscapeEmergencyOpened = true;
                }
                if ((value & Constant.VALUE_4_BIT) != 0) {
                    mEscapeManualEn = true;
                }
                if ((value & Constant.VALUE_5_BIT) != 0) {
                    mEscapeAntiClamp = true;
                }
                if ((value & Constant.VALUE_6_BIT) != 0) {
                    mEscapeSensorLost = true;
                }
                if ((value & Constant.VALUE_7_BIT) != 0) {
                    mEscapeCloseSensorError = true;
                }
                if ((value & Constant.VALUE_8_BIT) != 0) {
                    mEscapeOpenSensorError = true;
                }
                if ((value & Constant.VALUE_9_BIT) != 0) {
                    mEscapeMechanismError = true;
                }
                if ((value & Constant.VALUE_10_BIT) != 0) {
                    mEscapePositionLost = true;
                }
                if ((value & Constant.VALUE_11_BIT) != 0) {
                    mEscapeOverTorque = true;
                }
                break;
            case Constant.WARNING_1413:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mLifeSmokeWarning = true;
                }
                break;
            case Constant.WARNING_1489:
                if ((value & Constant.VALUE_0_BIT) != 0) {
                    mHotWaterInValveError = true;
                }
                if ((value & Constant.VALUE_1_BIT) != 0) {
                    mGasOutLeftValveError = true;
                }
                if ((value & Constant.VALUE_2_BIT) != 0) {
                    mGasOutRightValveError = true;
                }
                break;
            default:
        }
    }

    public void setRealData(int start, float value) {
        switch (start) {
            case Constant.STATE_1347:
                mTemperature = value;
                break;
            case Constant.STATE_1349:
                mHumidity = value;
                break;
            case Constant.STATE_1351:
                mO2Density = value;
                break;
            case Constant.STATE_1353:
                mCO2Density = value;
                break;
            case Constant.STATE_1355:
                mWaterInPressure = value;
                break;
            case Constant.STATE_1357:
                mWaterRest = value;
                break;
            case Constant.STATE_1359:
                mPowerRest = value;
                break;
            case Constant.STATE_1361:
                mUpsCurrent = value;
                break;
        }
    }

    public void setWordData(int start, int value) {
        switch (start) {
            case Constant.WARNING_1453:
                mWindow1ErrorCode = value;
                break;
            case Constant.WARNING_1455:
                mWindow2ErrorCode = value;
                break;
            case Constant.WARNING_1457:
                mWindow3ErrorCode = value;
                break;
            case Constant.WARNING_1459:
                mWindow4ErrorCode = value;
                break;
            case Constant.WARNING_1461:
                mEscapeErrorCode = value;
                break;
            case Constant.WARNING_1463:
                mDoor2ErrorCode = value;
                break;
            case Constant.WARNING_1465:
                mUnderEscapeErrorCode = value;
                break;
        }
    }

    public State getWindowStateAt(int index) {
        switch (index) {
            case 1:
                return mWindowState1;
            case 2:
                return mWindowState2;
            case 3:
                return mWindowState3;
            case 4:
                return mWindowState4;
            default:
                return null;
        }
    }

    public float getTemperature() {
        return mTemperature;
    }

    public float getHumidity() {
        return mHumidity;
    }

    public float getO2Density() {
        return mO2Density;
    }

    public float getCO2Density() {
        return mCO2Density;
    }

    public float getWaterRest() {
        return mWaterRest;
    }

    public float getPowerRest() {
        return mPowerRest;
    }

    public float getWaterInPressure() {
        return mWaterInPressure;
    }

    public float getUpsCurrent() {
        return mUpsCurrent;
    }

    public int[] getErrorCodes() {
        return new int[] {
                mWindow1ErrorCode,
                mWindow2ErrorCode,
                mWindow3ErrorCode,
                mWindow4ErrorCode,
                mEscapeErrorCode,
                mDoor2ErrorCode,
                mUnderEscapeErrorCode
        };
    }

    public boolean[] getPanState() {
        return new boolean[] {mIsFan1Running, mIsFan2Running};
    }

    public boolean isInEmergency() {
        return mInEmergency;
    }

    public boolean isInKeyEmergency() {
        return mInKeyEmergency;
    }

    public boolean isSewageOn() {
        return mSewageOn;
    }

    public boolean isAlarmDisabled() {
        return mIsAlarmDisabled;
    }

    public State getEscapeState() {
        return mEscapeState;
    }

    public boolean[] getUnderEscapeState() {
        return new boolean[] {mIsUnderEscapeOpened, mIsUnderEscapeClosed};
    }

    public State getScuttleState() {
        return mScuttleState;
    }

    public DoorState getDoorState() {
        return mDoorState;
    }

    public boolean[] getDoor2State() {
        return new boolean[] {mIsDoor2Opened, mIsDoor2Closed};
    }

    public boolean isDoorCompressed() {
        return mIsDoorCompressed;
    }

    public boolean isDoorDecompressed() {
        return mIsDoorDecompressed;
    }

    public boolean isDoorOpened() {
        return mIsDoorOpened;
    }

    public boolean isDoorLocked() {
        return mIsDoorLocked;
    }

    public boolean isPrimaryLightOn() {
        return mPrimaryLight;
    }

    public boolean isMoodLight1On() {
        return mMoodLight1;
    }
    public boolean isMoodLight2On() {
        return mMoodLight2;
    }

    public boolean isToiletLightOn() {
        return mToiletLight;
    }

    public boolean isOutLightOn() {
        return mOutLight;
    }

    public boolean isTatamiLightOn() {
        return mTatamiLight;
    }

    public boolean isTemperatureSensorError() {
        return mTemperatureSensorError;
    }

    public boolean isHumiditySensorError() {
        return mHumiditySensorError;
    }

    public String getO2Warning() {
        if (mO2SensorError) {
            return "O2传感器故障";
        } else if (mO2DensityLow3) {
            return "O2浓度低3级";
        } else if (mO2DensityLow2) {
            return "O2浓度低2级";
        } else if (mO2DensityLow1) {
            return "O2浓度低1级";
        } else {
            return "ok";
        }
    }

    public String getCO2Warning() {
        if (mCO2SensorError) {
            return "CO2传感器故障";
        } else if (mCO2DensityHigh3) {
            return "CO2浓度低3级";
        } else if (mCO2DensityHigh2) {
            return "CO2浓度低2级";
        } else if (mCO2DensityHigh1) {
            return "CO2浓度低1级";
        } else {
            return "ok";
        }
    }

    public String getWaterPressureWarning() {
        if (mWaterPressSensorError) {
            return "市政水压传感器故障";
        } else if (mWaterPressLow) {
            return "市政水压力低";
        } else if (mWaterPressLowWhenAdd) {
            return "补水时，市政水压力低";
        } else {
            return "ok";
        }
    }

    public String getWaterRestWarning() {
        if (mWaterRestSensorError) {
            return "水箱液位传感器故障";
        } else if (mWaterTankLow) {
            return "水箱液位低";
        } else if (mWaterTankLowWhenAdd) {
            return "补水时，水箱液位低";
        } else {
            return "ok";
        }
    }

//    public String getPowerRestWarning() {
//        if (mPowerRestError) {
//            return "剩余电量监测故障";
//        } else {
//            return "ok";
//        }
//    }

    public String getUPSWarning() {
        if (mUPSSensorError) {
            return "UPS插座用电监测故障";
        } else if (mUpsOverLimit) {
            return "UPS插座超出允许负载";
        } else {
            return "ok";
        }
    }

    public String getDoorWarning() {
        if (mDoorEmergencyOpened) {
            return "紧急状况下门未关闭";
        } else if (mDoorEmergencyUnimpacted) {
            return "紧急状况下门未密封";
        } else if (mDoorManualEn) {
            return "电动时手柄处于插入状态";
        } else if (mDoorOpenedWhenImpacting) {
            return "密封时门处于开启状态";
        } else {
            return "ok";
        }
    }

    public String getEscapeWarning() {
        if (mEscapeEmergencyOpened) {
            return "紧急状况未关闭";
        } else if (mEscapeManualEn) {
            return "电动时手柄处于插入状态";
        } else if (mEscapeAntiClamp) {
            return "防夹警告";
        } else if (mEscapeSensorLost) {
            return "拉线传感器丢失";
        } else if (mEscapeCloseSensorError) {
            return "逃生口全关传感器故障";
        } else if (mEscapeOpenSensorError) {
            return "逃生口全开传感器故障";
        } else if (mEscapeMechanismError) {
            return "疑似机构故障";
        } else if (mEscapePositionLost) {
            return "位置丢失";
        } else if (mEscapeOverTorque) {
            return "驱动故障";
        } else {
            return "ok";
        }
    }

    public String getWindow1Warning() {
        if (mWindow1EmergencyOpened) {
            return "紧急状况未关闭";
        } else if (mWindow1PositionLost) {
            return "位置丢失";
        } else if (mWindow1OverTorque) {
            return "驱动故障";
        } else {
            return "ok";
        }
    }

    public String getWindow2Warning() {
        if (mWindow2EmergencyOpened) {
            return "紧急状况未关闭";
        } else if (mWindow2PositionLost) {
            return "位置丢失";
        } else if (mWindow2OverTorque) {
            return "驱动故障";
        } else {
            return "ok";
        }
    }

    public String getWindow3Warning() {
        if (mWindow3EmergencyOpened) {
            return "紧急状况未关闭";
        } else if (mWindow3PositionLost) {
            return "位置丢失";
        } else if (mWindow3OverTorque) {
            return "驱动故障";
        } else {
            return "ok";
        }
    }

    public String getWindow4Warning() {
        if (mWindow4EmergencyOpened) {
            return "紧急状况未关闭";
        } else if (mWindow4PositionLost) {
            return "位置丢失";
        } else if (mWindow4OverTorque) {
            return "驱动故障";
        } else {
            return "ok";
        }
    }

    public boolean isControlSystemRunning() {
        return mSystemRunning;
    }

    public boolean isWaterInOn() {
        return mWaterIn;
    }

//    public boolean isWaterInError() {
//        return mWaterInValveError;
//    }

    public boolean isWaterTankInOn() {
        return mWaterTank;
    }

    public boolean isWaterTankInError() {
        return mWaterTankValveError;
    }

    public boolean isSewageOutOn() {
        return mSeWageOut;
    }

    public boolean isSewageOutError() {
        return mSewageOutError;
    }

    public boolean isBoosterPumpOn() {
        return mBoosterPump;
    }

    public boolean isBoosterPumpError() {
        return mPumpBoosterError;
    }

    public boolean isSewagePumpOn() {
        return mSewagePump;
    }

//    public boolean isSewagePumpError() {
//        return mPumpSewageError;
//    }
//
//    public boolean isGasOutValveOn() {
//        return mGasOutValve;
//    }
//
//    public boolean isGasOutValveError() {
//        return mGasOutValveError;
//    }
//
//    public boolean isGasToiletOutValveOn() {
//        return mGasToiletOutValve;
//    }
//
//    public boolean isGasToiletOutValveError() {
//        return mGasToiletOutValveError;
//    }
//
//    public boolean isGasInFanOn() {
//        return mGasInFan;
//    }
//
//    public boolean isGasInFanError() {
//        return mGasInFanError;
//    }
//
//    public boolean isGasToiletOutFanOn() {
//        return mGasToiletOutFan;
//    }
//
//    public boolean isGasToiletOutFanError() {
//        return mGasToiletOutFanError;
//    }
//
//    public boolean isGasInValve1On() {
//        return mGasInValve1;
//    }
//
//    public boolean isGasInValve1Error() {
//        return mGasInValve1Error;
//    }
//
//    public boolean isGasInValve2On() {
//        return mGasInValve2;
//    }
//
//    public boolean isGasInValve2Error() {
//        return mGasInValve2Error;
//    }

    public boolean isGermLightOn() {
        return mGermLight;
    }

    public boolean isAddingWater() {
        return mAddingWaterHand;
    }

    public boolean isEleStateOn() {
        return mEleState;
    }

    public boolean isUpsStateOn() {
        return mUpsState;
    }


    public String getEscapeStateString(State state, State scuttleState) {
        if (state == State.CLOSED) {
            return "关闭";
        } else if (state == State.MIDDLE) {
            return "逃生口中间位置";
        } else if (state == State.OPENED) {
            return "逃生口开启";
        } else if (state == State.CLOSING) {
            return "逃生口正在关闭";
        } else if (state == State.OPENING) {
            return "逃生口正在开启";
        } else if (state == State.INIT) {
            return "逃生口正在初始化位置";
        } else {
            if (scuttleState == State.CLOSED) {
                return "关闭";
            } else if (scuttleState == State.MIDDLE) {
                return "天窗处于中间位置";
            } else if (scuttleState == State.OPENING) {
                return "天窗正在开启";
            } else if (scuttleState == State.CLOSING) {
                return "天窗正在关闭";
            } else if (scuttleState == State.OPENED) {
                return "天窗开启";
            }
            return "error";
        }
    }


    public String getStateString(State state) {
        if (state == State.CLOSED) {
            return "关闭";
        } else if (state == State.MIDDLE) {
            return "中间位置";
        } else if (state == State.OPENED) {
            return "开启";
        } else if (state == State.CLOSING) {
            return "正在关闭";
        } else if (state == State.OPENING) {
            return "正在开启";
        } else if (state == State.INIT) {
            return "正在初始化位置";
        }
        return "error";

    }

    public String getDoorStateString() {
        if (mIsDoorOpened) {
            return "开启";
        } else {
            return "关闭";
        }
    }

    public ArrayList<Integer> getWarningStringIds() {
        ArrayList<Integer> warningStringIds = new ArrayList<>();
//        warningStringIds.add(getUPSWarning());
        if (mUpsOverLimit) {
            warningStringIds.add(R.string.ups_load_over_limit);
        }
//        if (mEmergencyAirLoop) {
//            warningStringIds.add(R.string.air_loop_when_emergency);
//        }
        if (mWaterTankOverTime) {
            warningStringIds.add(R.string.water_tank_over_time);
        }
        if (mDisinfectOverTime) {
            warningStringIds.add(R.string.disinfect_over_time);
        }
        if (mInKeyEmergency) {
            warningStringIds.add(R.string.in_key_emergency);
        }
//        //device----------------------------------------------------
        if (mWaterRestSensorError) {
            warningStringIds.add(R.string.water_rest_sensor_error);
        }
        if (mWaterPressSensorError) {
            warningStringIds.add(R.string.water_press_sensor_error);
        }
        if (mEscapeSensorError) {
            warningStringIds.add(R.string.escape_line_sensor_error);
        }
        if (mTemperatureSensorError) {
            warningStringIds.add(R.string.temperature_sensor_error);
        }
        if (mHumiditySensorError) {
            warningStringIds.add(R.string.humidity_sensor_error);
        }
        if (mO2SensorError) {
            warningStringIds.add(R.string.o2_sensor_error);
        }
        if (mCO2SensorError) {
            warningStringIds.add(R.string.co2_sensor_error);
        }
        if (mUPSSensorError) {
            warningStringIds.add(R.string.ups_check_error);
        }
//        if (mPowerRestError) {
//            warningStringIds.add(R.string.power_rest_check_error);
//        }
//        if (mWaterInValveError) {
//            warningStringIds.add(R.string.water_in_valve_error);
//        }
        if (mWaterTankValveError) {
            warningStringIds.add(R.string.water_tank_valve_error);
        }
        if (mSewageOutError) {
            warningStringIds.add(R.string.water_out_valve_error);
        }
        if (mPumpBoosterError) {
            warningStringIds.add(R.string.pump_booster_error);
        }
        if (mPumpSewageError) {
            warningStringIds.add(R.string.pump_sewage_error);
        }
        if (mGasInValve1Error) {
            warningStringIds.add(R.string.gas_in_valve_1_error);
        }
        if (mGasInValve2Error) {
            warningStringIds.add(R.string.gas_in_valve_2_error);
        }
        if (mGasOutValveError) {
            warningStringIds.add(R.string.gas_out_valve_error);
        }
        if (mHotWaterInValveError) {
            warningStringIds.add(R.string.hot_water_in_valve_error);
        }
        if (mGasOutLeftValveError) {
            warningStringIds.add(R.string.gas_out_left_valve_error);
        }
        if (mGasOutRightValveError) {
            warningStringIds.add(R.string.gas_out_right_valve_error);
        }
//        if (mGasToiletOutValveError) {
//            warningStringIds.add(R.string.gas_toilet_out_valve_error);
//        }
//        if (mGasInFanError) {
//            warningStringIds.add(R.string.gas_in_fan_error);
//        }
//        if (mGasOutFanError) {
//            warningStringIds.add(R.string.gas_out_fan_error);
//        }
//        if (mGasToiletOutFanError) {
//            warningStringIds.add(R.string.gas_toilet_out_fan_error);
//        }
//        if (mAirControllerError) {
//            warningStringIds.add(R.string.air_controller_error);
//        }
//        //door----------------------------------------------------
        if (mDoorEmergencyOpened) {
            warningStringIds.add(R.string.door_emergency_open);
        }
        if (mDoorEmergencyUnimpacted) {
            warningStringIds.add(R.string.door_emergency_unimapceted);
        }
        if (mDoorManualEn) {
            warningStringIds.add(R.string.door_manual_in);
        }
        if (mDoorOpenedWhenImpacting) {
            warningStringIds.add(R.string.door_opened_when_impacted);
        }
//        if (mDoorPinAutoFailed) {
//            warningStringIds.add(R.string.door_pin_failed);
//        }
//        if (mDoorWedgeAutoFailed) {
//            warningStringIds.add(R.string.door_wedge_failed);
//        }
//        if (mDoorImpactFailed) {
//            warningStringIds.add(R.string.door_detect_impacted_failed);
//        }
//        //escape----------------------------------------------------
        if (mEscapeEmergencyOpened) {
            warningStringIds.add(R.string.escape_emergency_open);
        }
        if (mEscapeManualEn) {
            warningStringIds.add(R.string.escape_manual_in);
        }
        if (mEscapeAntiClamp) {
            warningStringIds.add(R.string.escape_anti_clamp);
        }
        if (mEscapeSensorLost) {
            warningStringIds.add(R.string.escape_sensor_lost);
        }
        if (mEscapeCloseSensorError) {
            warningStringIds.add(R.string.escape_close_sensor_error);
        }
        if (mEscapeOpenSensorError) {
            warningStringIds.add(R.string.escape_open_sensor_error);
        }
        if (mEscapeMechanismError) {
            warningStringIds.add(R.string.escape_mechanism_error);
        }
        if (mEscapePositionLost) {
            warningStringIds.add(R.string.escape_position_lost);
        }
        if (mEscapeOverTorque) {
            warningStringIds.add(R.string.escape_over_torque);
        }
//        //window----------------------------------------------------
        if (mWindow1EmergencyOpened) {
            warningStringIds.add(R.string.window_1_emergency_open);
        }
        if (mWindow1PositionLost) {
            warningStringIds.add(R.string.window_1_position_lost);
        }
//        if (mWindow1CloseSensorError) {
//            warningStringIds.add(R.string.window_1_close_sensor_error);
//        }
//        if (mWindow1OpenSensorError) {
//            warningStringIds.add(R.string.window_1_open_sensor_error);
//        }
        if (mWindow1ManualEn) {
            warningStringIds.add(R.string.window_1_manual_mode_on);
        }
        if (mWindow1OverTorque) {
            warningStringIds.add(R.string.window_1_over_torque);
        }

        if (mWindow2EmergencyOpened) {
            warningStringIds.add(R.string.window_2_emergency_open);
        }
        if (mWindow2PositionLost) {
            warningStringIds.add(R.string.window_2_position_lost);
        }
//        if (mWindow2CloseSensorError) {
//            warningStringIds.add(R.string.window_2_close_sensor_error);
//        }
//        if (mWindow2OpenSensorError) {
//            warningStringIds.add(R.string.window_2_open_sensor_error);
//        }
        if (mWindow2ManualEn) {
            warningStringIds.add(R.string.window_2_manual_mode_on);
        }
        if (mWindow2OverTorque) {
            warningStringIds.add(R.string.window_2_over_torque);
        }

        if (mWindow3EmergencyOpened) {
            warningStringIds.add(R.string.window_3_emergency_open);
        }
        if (mWindow3PositionLost) {
            warningStringIds.add(R.string.window_3_position_lost);
        }
//        if (mWindow3CloseSensorError) {
//            warningStringIds.add(R.string.window_3_close_sensor_error);
//        }
//        if (mWindow3OpenSensorError) {
//            warningStringIds.add(R.string.window_3_open_sensor_error);
//        }
        if (mWindow3ManualEn) {
            warningStringIds.add(R.string.window_3_manual_mode_on);
        }
        if (mWindow3OverTorque) {
            warningStringIds.add(R.string.window_3_over_torque);
        }

        if (mWindow4EmergencyOpened) {
            warningStringIds.add(R.string.window_4_emergency_open);
        }
        if (mWindow4PositionLost) {
            warningStringIds.add(R.string.window_4_position_lost);
        }
//        if (mWindow4CloseSensorError) {
//            warningStringIds.add(R.string.window_4_close_sensor_error);
//        }
//        if (mWindow4OpenSensorError) {
//            warningStringIds.add(R.string.window_4_open_sensor_error);
//        }
        if (mWindow4ManualEn) {
            warningStringIds.add(R.string.window_4_manual_mode_on);
        }
        if (mWindow4OverTorque) {
            warningStringIds.add(R.string.window_4_over_torque);
        }
//        //other
        if (mLifeSmokeWarning) {
            warningStringIds.add(R.string.life_smoke_warning);
        }
//        if (mDeviceSmokeWarning) {
//            warningStringIds.add(R.string.device_smoke_warning);
//        }
//        if (mWaterPressLowWhenAdd) {
//            warningStringIds.add(R.string.water_press_low_when_add);
//        }
//        if (mWaterTankLowWhenAdd) {
//            warningStringIds.add(R.string.water_tank_low_when_add);
//        }
        if (mWaterPressLow) {
            warningStringIds.add(R.string.water_press_low);
        }
        if (mWaterTankLow) {
            warningStringIds.add(R.string.water_tank_low);
        }
        if (mIsDisinfectPrepare) {
            warningStringIds.add(R.string.disinfect_prepare);
        }
        if (mNoDisinfectInEmergency) {
            warningStringIds.add(R.string.no_disinfect_in_emergency);
        }
        if (mO2DensityLow1) {
            warningStringIds.add(R.string.o2_density_low_1);
        }
        if (mO2DensityLow2) {
            warningStringIds.add(R.string.o2_density_low_2);
        }
        if (mO2DensityLow3) {
            warningStringIds.add(R.string.o2_density_low_3);
        }
        if (mCO2DensityHigh1) {
            warningStringIds.add(R.string.co2_density_high_1);
        }
        if (mCO2DensityHigh2) {
            warningStringIds.add(R.string.co2_density_high_2);
        }
        if (mCO2DensityHigh3) {
            warningStringIds.add(R.string.co2_density_high_3);
        }
        if (mNoAddWaterInEmergency) {
            warningStringIds.add(R.string.no_add_water_in_emergency);
        }
        if (mSewageNeedInEmergency) {
            warningStringIds.add(R.string.sewage_need_in_emergency);
        }
        if (mIsAlarmDisabled) {
            warningStringIds.add(R.string.disabled_alarm);
        }
        return warningStringIds;
    }

    public ArrayList<Integer> getPromptStringIds() {
        ArrayList<Integer> promptStringIds = new ArrayList<>();
        //door-------------------------------------------------------------
        if (mDoorState == DoorState.COMPRESSING) {
            promptStringIds.add(R.string.door_compressing);
        } else if (mDoorState == DoorState.DECOMPRESSING) {
            promptStringIds.add(R.string.door_decompressing);
        }
        //escape-------------------------------------------------------------
        if (mEscapeState == State.OPENING) {
            promptStringIds.add(R.string.escape_opening);
        } else if (mEscapeState == State.CLOSING) {
            promptStringIds.add(R.string.escape_closing);
        } else if (mEscapeState == State.INIT) {
            promptStringIds.add(R.string.escape_init);
        }
        if (mScuttleState == State.OPENING) {
            promptStringIds.add(R.string.sunfoor_opening);
        } else if (mScuttleState == State.CLOSING) {
            promptStringIds.add(R.string.sunfoor_closing);
        }
        //window-------------------------------------------------------------
        if (mWindowState1 == State.OPENING) {
            promptStringIds.add(R.string.window_1_opening);
        } else if (mWindowState1 == State.CLOSING) {
            promptStringIds.add(R.string.window_1_closing);
        } else if (mWindowState1 == State.INIT) {
            promptStringIds.add(R.string.window_1_init);
        }

        if (mWindowState2 == State.OPENING) {
            promptStringIds.add(R.string.window_2_opening);
        } else if (mWindowState2 == State.CLOSING) {
            promptStringIds.add(R.string.window_2_closing);
        } else if (mWindowState2 == State.INIT) {
            promptStringIds.add(R.string.window_2_init);
        }

        if (mWindowState3 == State.OPENING) {
            promptStringIds.add(R.string.window_3_opening);
        } else if (mWindowState3 == State.CLOSING) {
            promptStringIds.add(R.string.window_3_closing);
        } else if (mWindowState3 == State.INIT) {
            promptStringIds.add(R.string.window_3_init);
        }

        if (mWindowState4 == State.OPENING) {
            promptStringIds.add(R.string.window_4_opening);
        } else if (mWindowState4 == State.CLOSING) {
            promptStringIds.add(R.string.window_4_closing);
        } else if (mWindowState4 == State.INIT) {
            promptStringIds.add(R.string.window_4_init);
        }
        if (mIsAddingWater) {
            promptStringIds.add(R.string.add_water_in_tank);
        }
        if (mGermLight) {
            promptStringIds.add(R.string.disinfect_light_on);
        }
        if (mSewageOn) {
            promptStringIds.add(R.string.in_sewage_mode);
        }
        if (mIsDisinfectDone) {
            promptStringIds.add(R.string.disinfect_done);
        }
        return promptStringIds;
    }

    public boolean isLifeSmogWarning() {
        return mLifeSmokeWarning;
    }

//    public boolean isDeviceSmogWarning() {
//        return mDeviceSmokeWarning;
//    }

//    public boolean[] getEnvironmentStates() {
//        return new boolean[] {
//                mLifeTemperatureHigh,
//                mDeviceTemperatureHigh,
//                mMainAirConditionRunning,
//                mBackupAirConditionRunning
//        };
//    }

    public boolean[] getEnvironmentWarnings() {
        return new boolean[] {
                mO2DensityLow1,
                mO2DensityLow2,
                mO2DensityLow3,
                mCO2DensityHigh1,
                mCO2DensityHigh2,
                mCO2DensityHigh3,
                mUpsOverLimit,
//                mEmergencyAirLoop,
                mDisinfectOverTime,
                mLifeSmokeWarning
//                mDeviceSmokeWarning
        };
    }

    public boolean[] getDoorStatus() {
        return new boolean[] {
                mIsDoorCompressed,
                mIsDoorDecompressed,
                mDoorState == DoorState.COMPRESSING,
                mDoorState == DoorState.DECOMPRESSING,
                mIsDoorOpened,
                mIsDoorLocked
        };
    }

    public boolean[] getDoorWarnings() {
        return new boolean[] {
                mDoorEmergencyOpened,
                mDoorEmergencyUnimpacted,
                mDoorManualEn,
                mDoorOpenedWhenImpacting
//                mDoorPinAutoFailed,
//                mDoorWedgeAutoFailed,
//                mDoorImpactFailed
        };
    }

    public boolean[] getWindow1Status() {
        return new boolean[] {
                mWindowState1 == State.CLOSED,
                mWindowState1 == State.MIDDLE,
                mWindowState1 == State.OPENED,
                mWindowState1 == State.OPENING,
                mWindowState1 == State.CLOSING,
                mWindowState1 == State.INIT
        };
    }

    public boolean[] getWindow2Status() {
        return new boolean[] {
                mWindowState2 == State.CLOSED,
                mWindowState2 == State.MIDDLE,
                mWindowState2 == State.OPENED,
                mWindowState2 == State.OPENING,
                mWindowState2 == State.CLOSING,
                mWindowState2 == State.INIT
        };
    }

    public boolean[] getWindow3Status() {
        return new boolean[] {
                mWindowState3 == State.CLOSED,
                mWindowState3 == State.MIDDLE,
                mWindowState3 == State.OPENED,
                mWindowState3 == State.OPENING,
                mWindowState3 == State.CLOSING,
                mWindowState3 == State.INIT
        };
    }

    public boolean[] getWindow4Status() {
        return new boolean[] {
                mWindowState4 == State.CLOSED,
                mWindowState4 == State.MIDDLE,
                mWindowState4 == State.OPENED,
                mWindowState4 == State.OPENING,
                mWindowState4 == State.CLOSING,
                mWindowState4 == State.INIT
        };
    }

    public boolean[] getWindow1Warnings() {
        return new boolean[] {
                mWindow1EmergencyOpened,
                mWindow1PositionLost,
                mWindow1ManualEn,
                mWindow1OverTorque
        };
    }

    public boolean[] getWindow2Warnings() {
        return new boolean[] {
                mWindow2EmergencyOpened,
                mWindow2PositionLost,
                mWindow2ManualEn,
                mWindow2OverTorque
        };
    }

    public boolean[] getWindow3Warnings() {
        return new boolean[] {
                mWindow3EmergencyOpened,
                mWindow3PositionLost,
                mWindow3ManualEn,
                mWindow3OverTorque
        };
    }

    public boolean[] getWindow4Warnings() {
        return new boolean[] {
                mWindow4EmergencyOpened,
                mWindow4PositionLost,
                mWindow4ManualEn,
                mWindow4OverTorque
        };
    }

    public boolean[] getEscapeStatus() {
        return new boolean[] {
                mScuttleState == State.CLOSED,
                mScuttleState == State.MIDDLE,
                mScuttleState == State.OPENED,
                mEscapeState == State.MIDDLE,
                mEscapeState == State.OPENED,
                mScuttleState == State.OPENING,
                mScuttleState == State.CLOSING,
                mEscapeState == State.OPENING,
                mEscapeState == State.CLOSING,
                mEscapeState == State.INIT
        };
    }

    public boolean[] getEscapeWarnings() {
        return new boolean[] {
                mEscapeEmergencyOpened,
                mEscapeManualEn,
                mEscapeAntiClamp,
                mEscapeSensorLost,
                mEscapeCloseSensorError,
                mEscapeOpenSensorError,
                mEscapeMechanismError,
                mEscapePositionLost,
                mEscapeOverTorque
        };
    }

    public boolean[] getControlSystemStatus() {
        return new boolean[] {
                mEleState,
                mUpsState,
                mSystemRunning,
                mInEmergency,
                mInKeyEmergency,
                mFanRunning,
                mIsFan1Running,
                mIsFan2Running,
                mWaterIn,
                mWaterTank,
                mSeWageOut,
                mBoosterPump,
                mSewagePump,
//                mPrimaryLight,
//                mMoodLight1,
//                mMoodLight2,
//                mToiletLight,
//                mTatamiLight,
//                mOutLight,
                mGermLight,
                mIsDisinfectPrepare,
                mNoDisinfectInEmergency,
                mIsDisinfectDone
        };
    }

    public boolean[] getDeviceErrors() {
        return new boolean[] {
                mWaterRestSensorError,
                mWaterPressSensorError,
                mEscapeSensorError,
                mTemperatureSensorError,
                mHumiditySensorError,
                mO2SensorError,
                mCO2SensorError,
                mUPSSensorError,
                mWaterTankValveError,
                mSewageOutError,
                mPumpBoosterError,
                mPumpSewageError,
                mGasInValve1Error,
                mGasInValve2Error,
                mGasOutValveError,
                mHotWaterInValveError,
                mGasOutLeftValveError,
                mGasOutRightValveError
        };
    }

    public boolean isWindow1Error() {
        return mWindow1EmergencyOpened || mWindow1PositionLost ||
                mWindow1ManualEn || mWindow1OverTorque;
    }

    public boolean isWindow2Error() {
        return mWindow2EmergencyOpened || mWindow2PositionLost ||
                mWindow2ManualEn || mWindow2OverTorque;
    }

    public boolean isWindow3Error() {
        return mWindow3EmergencyOpened || mWindow3PositionLost ||
                mWindow3ManualEn || mWindow3OverTorque;
    }

    public boolean isWindow4Error() {
        return mWindow4EmergencyOpened || mWindow4PositionLost ||
                mWindow4ManualEn || mWindow4OverTorque;
    }

    public boolean isDoorError() {
        return mDoorEmergencyOpened || mDoorEmergencyUnimpacted ||
                mDoorManualEn || mDoorOpenedWhenImpacting;
    }

    public boolean isDoor2Error() {
        return false;
    }

    public boolean isEscapeError() {
        return mEscapeEmergencyOpened || mEscapeManualEn || mEscapeAntiClamp || mEscapeSensorLost ||
                mEscapeCloseSensorError || mEscapeOpenSensorError || mEscapeMechanismError ||
                mEscapePositionLost || mEscapeOverTorque;
    }

    public boolean isUnderEscapeError() {
        return false;
    }
}
