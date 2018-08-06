package slon.sky.dev.lab7;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.math.BigInteger;

/**
 * Created by Slon on 09.05.2017.
 */

public class CalculationService extends Service {

    public static final String FACTORIAL = "factorial";
    public static final String FIBONACCI = "fibonacci";
    public static final String EXPONENT = "exponent";

    public static final String TASK_BEGIN = "slon.sky.dev.lab7.TASK_BEGIN";
    public static final String TASK_FINISH = "slon.sky.dev.lab7.TASK_FINISH";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String operation = intent.getStringExtra("type");
        int x = intent.getIntExtra("x", 0);

        new Thread(new Calculation(x, operation)).start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class Calculation implements Runnable {

        private int x;
        private String operation;

        public Calculation(int x, String operation) {
            this.x = x;
            this.operation = operation;
        }

        @Override
        public void run() {

            Intent i = new Intent();
            i.setAction(TASK_BEGIN);
            sendBroadcast(i);

            String result = "";
            switch (operation) {
                case FACTORIAL:
                    result = calcFactorial(x);
                    break;
                case FIBONACCI:
                    result = calcFibonacci(x);
                    break;
                case EXPONENT:
                    result = calcExponent(x);
                    break;
            }

            i = new Intent();
            i.setAction(TASK_FINISH);
            i.putExtra("result", result);
            sendBroadcast(i);

            stopSelf();
        }

        private String calcFactorial(int x) {
            BigInteger res = new BigInteger("1");
            for(int i = 1; i <= x; i++) {
                res = res.multiply(new BigInteger(i+""));
            }
            return res.toString();
        }

        private String calcFibonacci(int x) {
            if(x == 1 || x == 2) {
                return "1";
            }
            BigInteger i0 = new BigInteger("1");
            BigInteger i1 = new BigInteger("1");
            BigInteger res = new BigInteger("0");
            for(int i = 0; i < x; i++) {
                res = i0.add(i1);
                i0 = i1;
                i1 = res;
            }
            return res.toString();
        }

        private String calcExponent(int x) {
            BigInteger res = new BigInteger("1");
            for(int i = 0; i < x; i++) {
                res = res.multiply(new BigInteger(x+""));
            }
            return res.toString();
        }
    }
}
