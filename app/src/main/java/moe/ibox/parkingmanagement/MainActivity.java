package moe.ibox.parkingmanagement;

import android.annotation.SuppressLint;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import moe.ibox.parkingmanagement.entity.Plate;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private RFID rfid;
    private PlatesDao platesDao;

    private Button button_entry;
    private TextView textView_plate, textView_approach, textView_disapproach, textView_during, textView_price;

    @SuppressWarnings("BusyWait")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_entry = findViewById(R.id.button_entry);
        textView_plate = findViewById(R.id.textView_plate);
        textView_approach = findViewById(R.id.textView_approach);
        textView_disapproach = findViewById(R.id.textView_disapproach);
        textView_during = findViewById(R.id.textView_during);
        textView_price = findViewById(R.id.textView_price);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.CHINESE);
                }
            }
        });

//        rfid = new RFID(DataBusFactory.newSocketDataBus("172.16.16.15", 6005));
        rfid = new RFID(DataBusFactory.newSerialDataBus(1, 115200));

        platesDao = PlatesDao.getInstance(getApplicationContext());

        new Thread(() -> {
            while (true) {
                try {
                    assert rfid != null;
                    rfid.readSingleEpc(new SingleEpcListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onVal(String s) {
                            if (rfid != null) {
                                if (platesDao.isApproached(s)) {
                                    try {
                                        Plate plate = platesDao.getPlateInfoByRfid(s);
                                        runOnUiThread(() -> {
                                            Date nowDate = new Date();
                                            textView_plate.setText(plate.getPlate());
                                            textView_approach.setText(PlatesDao.simpleDateFormat.format(plate.getApproachTime()));
                                            textView_disapproach.setText(PlatesDao.simpleDateFormat.format(nowDate));
                                            long duringMinutes = (nowDate.getTime() - plate.getApproachTime().getTime()) / 1000 / 60 + 1;
                                            textView_during.setText(duringMinutes + " 分钟");
                                            textView_price.setText(duringMinutes * 10 + " 元");

                                            button_entry.setOnClickListener(v -> {
                                                platesDao.approach(s, false);
                                                tts.speak("一路平安", TextToSpeech.QUEUE_FLUSH, null, null);
                                            });
                                        });
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    if (platesDao.approach(s, true) != 0) {
                                        try {
                                            tts.speak("欢迎入场，" + platesDao.getPlateInfoByRfid(s).getPlate(), TextToSpeech.QUEUE_FLUSH, null, null);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                Log.i("RFID", s);
                            }
                        }
                    });
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}