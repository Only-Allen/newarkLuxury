package com.avatar.newarkluxury;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;
import com.serotonin.util.queue.ByteQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by chx on 2016/12/9.
 */

public class Communicator {
    private static final String TAG = "Communicator";

    private static Communicator mInstance;
    private static Context mContext;

    //发送获取到的机器状态的广播，界面接受广播同步状态
    public static final String ACTION_STATE = "com.avatar.newark.get.state.finish";
    public static final String FLAG_GET_STATE = "com.avatar.newark.get.machine.state";

    private static final Object syncLock = new Object();

    private static final int CONTROL_INTERVAL = 100;//按钮脉冲信号的间隔时间
    private static final int HANDLE_INTERVAL = 500;//点击按钮之后的延迟同步状态
    private static final int QUERY_INTERVAL = 500;//后台一直同步状态的间隔时间

    private MachineState mCurrentState, mNextState;

    private int startAddress = Constant.STATE_1347;//最低地址
    private int endAddress = Constant.STATE_1381;//最高地址

    private int startAddress2 = Constant.WARNING_1413;
    private int endAddress2 = Constant.STATE_1490;

    private int unsignedShortMax = 0xffff;

    private ScheduledExecutorService mControlExecutor;
    private ExecutorService mQueryExecutor;
    private ExecutorService mSendRequestExecutor;
    private final int HANDLER_QUERY = 0;
    private final int HANDLER_CALLBACK = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_QUERY:
                    queryAllMessages();
                    break;
                case HANDLER_CALLBACK:
//                    mCallback.onComplete((MachineState) msg.obj);
//                    if (msg.obj != null) {
                        mCurrentState = (MachineState) msg.obj;
//                    }
//                    if (mCurrentState == null) {
//                        return;
//                    }
                    Intent intent = new Intent(ACTION_STATE);
                    intent.putExtra(FLAG_GET_STATE, mCurrentState);
                    mContext.sendBroadcast(intent);
                    break;
                default:
            }
        }
    };
//    private ReadCallback mCallback;
    private ModbusMaster mTcpMaster;
    private Logger mLogger;
    private static final String IP = "192.168.1.9";
    private static final int PORT = 502;
    private static final int SLAVE_ID = 247;
    private short mEmergency;

    private int[] mTwoShortsAddress = new int[] {
            Constant.STATE_1347,
            Constant.STATE_1349,
            Constant.STATE_1351,
            Constant.STATE_1353,
            Constant.STATE_1355,
            Constant.STATE_1357,
            Constant.STATE_1359,
            Constant.STATE_1361
    };

    private int[] mTwoShortsAddress2 = new int[] {
    };

    private int[] mSingleShortAddress = new int[] {
            Constant.STATE_1363,
            Constant.STATE_1364,
            Constant.STATE_1365,
            Constant.STATE_1366,
            Constant.STATE_1367,
            Constant.STATE_1368,
            Constant.STATE_1369,
            Constant.STATE_1370,
            Constant.STATE_1371,
            Constant.STATE_1372,
            Constant.STATE_1373,
            Constant.STATE_1374,
            Constant.STATE_1375,
            Constant.STATE_1376,
            Constant.STATE_1380,
            Constant.STATE_1381,
            Constant.WARNING_1325,
            Constant.WARNING_1326,
            Constant.WARNING_1329,
            Constant.WARNING_1330,
            Constant.WARNING_1331,
            Constant.WARNING_1332,
            Constant.WARNING_1335,
            Constant.WARNING_1336,
            Constant.WARNING_1378,
            Constant.WARNING_1379
    };

    private int mSingleShortAddress2[] = {
            Constant.STATE_1415,
            Constant.STATE_1416,
            Constant.STATE_1417,
            Constant.STATE_1418,
            Constant.STATE_1452,
            Constant.STATE_1473,
            Constant.STATE_1490,
            Constant.WARNING_1413,
            Constant.WARNING_1489
    };

    private int mWordAddress2[] = {
            Constant.WARNING_1453,
            Constant.WARNING_1455,
            Constant.WARNING_1457,
            Constant.WARNING_1459,
            Constant.WARNING_1461,
            Constant.WARNING_1463,
            Constant.WARNING_1465
    };

    private Communicator() {
        mControlExecutor  = Executors.newScheduledThreadPool(1);
        mQueryExecutor = Executors.newCachedThreadPool();
        mSendRequestExecutor = Executors.newSingleThreadExecutor();
        mLogger = LoggerFactory.getLogger(Communicator.class);
        ModbusFactory modbusFactory = new ModbusFactory();

        IpParameters params = new IpParameters();
        params.setHost(IP);
        if (502 != PORT) {
            params.setPort(PORT);
        }

        mTcpMaster = modbusFactory.createTcpMaster(params, true);
        mSendRequestExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    mTcpMaster.init();
                    mTcpMaster.setTimeout(100);
                } catch (ModbusInitException e)  {
                    mLogger.error("modbus init error", e);
                }
            }
        });
    }

    public static Communicator getInstance() {
        if (mInstance == null) {
            synchronized (syncLock) {
                if (mInstance == null) {
                    mInstance = new Communicator();
                }
            }
        }
        return mInstance;
    }

//    public void setCallback(ReadCallback callback) {
//        mCallback = callback;
//        queryAllMessages();
//    }

    public void startCommunicate(Context context) {
        setContext(context);
        queryAllMessages();
    }

    public void reStartCommunicate() {
        mHandler.removeMessages(HANDLER_QUERY);
        mHandler.sendEmptyMessageDelayed(HANDLER_QUERY, HANDLE_INTERVAL);
    }

    public void setContext(Context context) {
        mContext = context;
    }

/**************************************base method start******************************************/
    private byte[] query(int start, int readLength) {
        try {
            //ReadHoldingRegistersRequest的功能码是03
            ModbusRequest request = new ReadHoldingRegistersRequest(SLAVE_ID, start, readLength);
            ModbusResponse response = mTcpMaster.send(request);

            ByteQueue byteQueue = new ByteQueue(readLength * 2 + 3);
            response.write(byteQueue);

            return byteQueue.peek(3, byteQueue.size() - 3);
        } catch (Exception e) {
            mLogger.error("queryAllWarnings error", e);
            return null;
        }
    }

    private void control(final int start, final short[] values) {
        try {
            //WriteRegistersRequest的功能码是10
            WriteRegistersRequest request = new WriteRegistersRequest(SLAVE_ID, start, values);
            WriteRegistersResponse response = (WriteRegistersResponse) mTcpMaster.send(request);

            if (response.isException())
                Log.i(TAG, "Exception response: message=" + response.getExceptionMessage());
            else
                Log.i(TAG, "write success");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Runnable getControlRunnable(final int start, final short[] data) {
        return new Runnable() {
            @Override
            public void run() {
                control(start, data);
            }
        };
    }

    public Callable<byte[]> getQueryCallable(final int start, final int length) {
        return new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return query(start, length);
            }
        };
    }

    /**
     * 发送查询请求
     * @param c 查询动作
     * @return 查询的数据
     */
    public byte[] sendQueryRequest(Callable<byte[]> c) {
        Future<byte[]> f = mQueryExecutor.submit(c);
        byte[] bytes;
        try {
            bytes = f.get();
        } catch (Exception e) {
            e.printStackTrace();
            bytes = null;
        }
        return bytes;
    }

    /**
     * 发送控制请求
     * @param r 控制动作
     * @param r0 清零动作
     */
    public void sendControlRequest(Runnable r, Runnable r0) {
        mControlExecutor.submit(r);
        mControlExecutor.schedule(r0, CONTROL_INTERVAL, TimeUnit.MILLISECONDS);
    }
/**************************************base method end********************************************/

/**************************************control start**********************************************/

    /**
     * 开启和关闭紧急模式
     * @param open open if true,or close
     */
    public void sendEmergencyRequest(boolean open) {
        short value;
        if (open) {
            value = Constant.VALUE_12_BIT;
            value |= mEmergency;
        } else {
            value = (short) (unsignedShortMax ^ Constant.VALUE_12_BIT);
            value &= mEmergency;
        }
        mControlExecutor.submit(getControlRunnable(Constant.CONTROL_1379, new short[] {value}));
    }

    /**
     * 主抽风机开关
     */
    public void switchWindGears1() {
        sendControlRequest(getControlRunnable(
                Constant.CONTROL_1379, new short[] {(short) (Constant.VALUE_13_BIT | mEmergency)}),
                getControlRunnable(Constant.CONTROL_1379, new short[] {(short) (mEmergency &
                        (short) (unsignedShortMax ^ Constant.VALUE_13_BIT))}));
    }

    /**
     * 次抽风机开关
     */
    public void switchWindGears2() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1489, new short[] {Constant.VALUE_11_BIT}),
                getControlRunnable(Constant.CONTROL_1489, new short[] {0}));
    }

    /**
     * 开启和关闭排污
     */
    public void handleSewage() {
        sendControlRequest(getControlRunnable(
                Constant.CONTROL_1379, new short[] {(short) (Constant.VALUE_14_BIT | mEmergency)}),
                getControlRunnable(Constant.CONTROL_1379, new short[] {(short) (mEmergency &
                        (short) (unsignedShortMax ^ Constant.VALUE_14_BIT))}));
    }

    /**
     * 补水和停止补水
     */
    public void addWaterOrStop() {
        sendControlRequest(getControlRunnable(
                Constant.CONTROL_1379, new short[] {(short) (Constant.VALUE_15_BIT | mEmergency)}),
                getControlRunnable(Constant.CONTROL_1379, new short[] {(short) (mEmergency &
                        (short) (unsignedShortMax ^ Constant.VALUE_15_BIT))}));
    }

    /**
     * 开关灭菌灯
     */
    public void switchGermLight() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1380, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1380, new short[] {0}));
    }

    /**
     * 开关主照明灯
     */
    public void switchPrimaryLight() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1337, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1337, new short[] {0}));
    }

    /**
     * 开关氛围灯1
     */
    public void switchMoodLight1() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1337, new short[] {Constant.VALUE_8_BIT}),
                getControlRunnable(Constant.CONTROL_1337, new short[] {0}));
    }

    /**
     * 开关氛围灯2
     */
    public void switchMoodLight2() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1338, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1338, new short[] {0}));
    }

    /**
     * 门压紧
     */
    public void compressDoor() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1489, new short[] {Constant.VALUE_12_BIT}),
                getControlRunnable(Constant.CONTROL_1489, new short[] {0}));
    }

    /**
     * 门反锁
     */
    public void lockDoor() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1489, new short[] {Constant.VALUE_13_BIT}),
                getControlRunnable(Constant.CONTROL_1489, new short[] {0}));
    }

    /**
     * 门解压
     */
    public void decompressDoor() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1489, new short[] {Constant.VALUE_14_BIT}),
                getControlRunnable(Constant.CONTROL_1489, new short[] {0}));
    }

    /**
     * 逃生口全关
     */
    public void closeEscape() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1340, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1340, new short[] {0}));
    }

    /**
     * 开启天窗
     */
    public void openScuttle() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1340, new short[] {Constant.VALUE_8_BIT}),
                getControlRunnable(Constant.CONTROL_1340, new short[] {0}));
    }

    /**
     * 开启逃生口
     */
    public void openEscape() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1341, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1341, new short[] {0}));
    }

    /**
     * 开启下逃逸口
     */
    public void openUnderEscape() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1483, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1483, new short[] {0}));
    }

    /**
     * 关闭下逃逸口
     */
    public void closeUnderEscape() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1483, new short[] {Constant.VALUE_8_BIT}),
                getControlRunnable(Constant.CONTROL_1483, new short[] {0}));
    }

    /**
     * 开启内门
     */
    public void openDoor2() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1484, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1484, new short[] {0}));
    }

    /**
     * 关闭内门
     */
    public void closeDoor2() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1484, new short[] {Constant.VALUE_8_BIT}),
                getControlRunnable(Constant.CONTROL_1484, new short[] {0}));
    }

    /**
     * 开窗
     * @param index  which window_close to open
     */
    public void openWindowAt(int index) {
        switch(index) {
            case 1:
                sendControlRequest(
                        getControlRunnable(Constant.CONTROL_1342, new short[] {Constant.VALUE_8_BIT}),
                        getControlRunnable(Constant.CONTROL_1342, new short[] {0}));
                break;
            case 2:
                sendControlRequest(
                        getControlRunnable(Constant.CONTROL_1343, new short[] {Constant.VALUE_8_BIT}),
                        getControlRunnable(Constant.CONTROL_1343, new short[] {0}));
                break;
            case 3:
                sendControlRequest(
                        getControlRunnable(Constant.CONTROL_1344, new short[] {Constant.VALUE_8_BIT}),
                        getControlRunnable(Constant.CONTROL_1344, new short[] {0}));
                break;
            case 4:
                sendControlRequest(
                        getControlRunnable(Constant.CONTROL_1345, new short[] {Constant.VALUE_8_BIT}),
                        getControlRunnable(Constant.CONTROL_1345, new short[] {0}));
                break;
            default:
                mLogger.error("the index of window is error when open!!!!");
        }
    }

    /**
     * 关窗
     * @param index  which window_close to close
     */
    public void closeWindowAt(int index) {
        switch(index) {
            case 1:
                sendControlRequest(
                        getControlRunnable(Constant.CONTROL_1342, new short[] {Constant.VALUE_0_BIT}),
                        getControlRunnable(Constant.CONTROL_1342, new short[] {0}));
                break;
            case 2:
                sendControlRequest(
                        getControlRunnable(Constant.CONTROL_1343, new short[] {Constant.VALUE_0_BIT}),
                        getControlRunnable(Constant.CONTROL_1343, new short[] {0}));
                break;
            case 3:
                sendControlRequest(
                        getControlRunnable(Constant.CONTROL_1344, new short[] {Constant.VALUE_0_BIT}),
                        getControlRunnable(Constant.CONTROL_1344, new short[] {0}));
                break;
            case 4:
                sendControlRequest(
                        getControlRunnable(Constant.CONTROL_1345, new short[] {Constant.VALUE_0_BIT}),
                        getControlRunnable(Constant.CONTROL_1345, new short[] {0}));
                break;
            default:
                mLogger.error("the index of window is error when close!!!!");
        }
    }

    /**
     * 开关卫生间灯
     */
    public void switchToiletLight() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1413, new short[] {Constant.VALUE_4_BIT}),
                getControlRunnable(Constant.CONTROL_1413, new short[] {0}));
    }

    /**
     * 开关外部射灯
     */
    public void switchOutLight() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1489, new short[] {Constant.VALUE_15_BIT}),
                getControlRunnable(Constant.CONTROL_1489, new short[] {0}));
    }

    /**
     * 开关榻榻米灯
     */
    public void switchTatamiLight() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1490, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1490, new short[] {0}));
    }

    /**
     * 开关中控系统
     */
    public void switchControlSystem() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1001, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1001, new short[] {0}));
    }

    /**
     * 1#窗驱动器报警复位
     */
    public void resetWindow1Warning() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1467, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1467, new short[] {0}));
    }

    /**
     * 2#窗驱动器报警复位
     */
    public void resetWindow2Warning() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1467, new short[] {Constant.VALUE_8_BIT}),
                getControlRunnable(Constant.CONTROL_1467, new short[] {0}));
    }

    /**
     * 3#窗驱动器报警复位
     */
    public void resetWindow3Warning() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1468, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1468, new short[] {0}));
    }

    /**
     * 4#窗驱动器报警复位
     */
    public void resetWindow4Warning() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1468, new short[] {Constant.VALUE_8_BIT}),
                getControlRunnable(Constant.CONTROL_1468, new short[] {0}));
    }

    /**
     * 逃逸口驱动器报警复位
     */
    public void resetEscapeWarning() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1469, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1469, new short[] {0}));
    }

    /**
     * 内门驱动器报警复位
     */
    public void resetDoor2Warning() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1469, new short[] {Constant.VALUE_8_BIT}),
                getControlRunnable(Constant.CONTROL_1469, new short[] {0}));
    }

    /**
     * 下逃逸口驱动器报警复位
     */
    public void resetUnderEscapeWarning() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1470, new short[] {Constant.VALUE_0_BIT}),
                getControlRunnable(Constant.CONTROL_1470, new short[] {0}));
    }

//    /**
//     * 烟雾报警复位
//     */
//    public void resetSmogAlarm() {
//        sendControlRequest(
//                getControlRunnable(Constant.CONTROL_534, new short[] {Constant.VALUE_0_BIT}),
//                getControlRunnable(Constant.CONTROL_534, new short[] {0}));
//    }

    /**
     * 开关警示
     */
    public void switchAlarm() {
        sendControlRequest(
                getControlRunnable(Constant.CONTROL_1473, new short[] {Constant.VALUE_1_BIT}),
                getControlRunnable(Constant.CONTROL_1473, new short[] {0}));
    }
/**************************************control end************************************************/

/**************************************query start************************************************/
    /**
     * 查询所有状态和警示数据
     */
    public void queryAllMessages() {
        mSendRequestExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mNextState = new MachineState();
//                queryAllStates();
//                queryAllWarnings();
                queryTotalMessage();
                Message msg = Message.obtain();
                msg.what = HANDLER_CALLBACK;
                msg.obj = mNextState;
                mHandler.sendMessage(msg);
            }
        });
        mHandler.sendEmptyMessageDelayed(HANDLER_QUERY, QUERY_INTERVAL);
    }

    /**
     * 查询所有float数据
     */
    public void queryAllStates() {
        List<Future<byte[]>> futures = new ArrayList<>();
        for (int i : mTwoShortsAddress) {
            futures.add(mQueryExecutor.submit(getQueryCallable(i, 2)));
        }
        for (int i = 0; i < futures.size(); i++) {
            try {
                byte[] bytes = futures.get(i).get();
                fillState(mTwoShortsAddress[i], bytes);
            } catch (Exception e) {
                mLogger.error("queryAllWarnings error", e);
            }
        }
    }

    /**
     * 查询所有short数据
     */
    public void queryAllWarnings() {
        List<Future<byte[]>> futures = new ArrayList<>();
        for (int i : mSingleShortAddress) {
            futures.add(mQueryExecutor.submit(getQueryCallable(i, 1)));
        }
        for (int i = 0; i < futures.size(); i++) {
            try {
                byte[] bytes = futures.get(i).get();
                if (mSingleShortAddress[i] == Constant.WARNING_1379) {
                    mEmergency = (short) (bytes[0] << 8 | bytes[1]);
                }
                fillState(mSingleShortAddress[i], bytes);
            } catch (Exception e) {
                mLogger.error("queryAllWarnings error", e);
            }
        }
    }

    /**
     * 一次请求返回所有数据
     */

    private void queryTotalMessage() {
        try {
            byte[] bytes = sendQueryRequest(getQueryCallable(startAddress,
                    endAddress - startAddress + 1));
            if (bytes == null) {
                mNextState = null;
                return;
            }
            for (int i : mTwoShortsAddress) {
                byte[] newBytes = new byte[4];
                System.arraycopy(bytes, (i - startAddress) * 2, newBytes, 0, 4);
                fillState(i, newBytes);
            }
            for (int i : mSingleShortAddress) {
                byte[] newBytes = new byte[2];
                System.arraycopy(bytes, (i - startAddress) * 2, newBytes, 0, 2);
                if (i == Constant.WARNING_1379) {
                    mEmergency = (short) (newBytes[0] << 8 | newBytes[1]);
                }
                fillState(i, newBytes);
            }
            byte[] bytes2 = sendQueryRequest(getQueryCallable(startAddress2,
                    endAddress2 - startAddress2 + 1));
            if (bytes2 == null) {
                mNextState = null;
                return;
            }
            for (int i : mSingleShortAddress2) {
                byte[] newBytes = new byte[2];
                System.arraycopy(bytes2, (i - startAddress2) * 2, newBytes, 0, 2);
                fillState(i, newBytes);
            }
            for (int i : mWordAddress2) {
                byte[] newBytes = new byte[4];
                System.arraycopy(bytes2, (i - startAddress2) * 2, newBytes, 0, 4);
                fillWordData(i, newBytes);
            }
            for (int i : mTwoShortsAddress2) {
                byte[] newBytes = new byte[4];
                System.arraycopy(bytes2, (i - startAddress2) * 2, newBytes, 0, 4);
                fillState(i, newBytes);
            }
            //STATE_1003单独请求
            byte[] bytes3 = sendQueryRequest(getQueryCallable(Constant.STATE_1003, 1));
            if (bytes3 == null) {
                mNextState = null;
                return;
            }
            fillState(Constant.STATE_1003, bytes3);

        } catch (Exception e) {
            mLogger.error("queryTotalMessage error:\n", e);
            mNextState = null;
        }
    }

    private void fillState(int start, byte[] data) {
        int[] ints = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            ints[i] = data[i] & 0xff;
        }
        if (data.length == 2) {
            int value = ints[0] * 0x100 | ints[1];
            if (start == Constant.WARNING_1379) {
                mEmergency = (short) (value);
            }
            mNextState.fillData(start, value);
        } else if (data.length == 4) {
            int i = 0x00000000;
            i |= ints[1];
            i |= ints[0] << 8;
            i |= ints[3] << 16;
            i |= ints[2] << 24;
            mNextState.setRealData(start, Float.intBitsToFloat(i));
        }
    }

    public void fillWordData(int start, byte[] data) {
        int[] ints = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            ints[i] = data[i] & 0xff;
        }


        long value = 0x00000000;
        value |= ints[1];
        value |= ints[0] << 8;
        value |= ints[3] << 16;
        value |= ints[2] << 24;
        mNextState.setWordData(start, (int) value);
    }

/**************************************query end**************************************************/

}
