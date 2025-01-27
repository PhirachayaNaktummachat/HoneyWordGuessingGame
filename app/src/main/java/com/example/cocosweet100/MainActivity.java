package com.example.cocosweet100;

import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.os.Handler;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView wordTextView, scoreTextView, timerTextView;
    private Button restartButton, backToCategoryButton;
    private ArrayList<String> wordList;
    private int currentIndex = 0;
    private int score = 0;
    private boolean isTiltedForward = false;
    private boolean isTiltedBackward = false;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 1 minute in milliseconds
    private static final String PREFS_NAME = "GamePrefs";
    private static final String TIME_LEFT_KEY = "timeLeftInMillis";
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private long startTime;
    private boolean isGameRunning = false; // ตัวแปรสถานะเกม
    private Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing UI components
        wordTextView = findViewById(R.id.wordTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        restartButton = findViewById(R.id.restartButton);
        backToCategoryButton = findViewById(R.id.backToCategoryButton);

        // Set up button click listeners
        restartButton.setOnClickListener(v -> restartGame());

        backToCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
            startActivity(intent);
            finish();
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> showBackConfirmationDialog());

        // Initializing sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Retrieve category from the Intent
        String category = getIntent().getStringExtra("category");

        // Initialize word list based on category
        if (category != null) {
            initializeWordList(category);
        } else {
            wordList = new ArrayList<>(); // ป้องกัน NullPointerException
        }

        // Start the game
        startGame();
    }


    private void initializeWordList(String category) {
        wordList = new ArrayList<>();
        String language = Locale.getDefault().getLanguage();


        if (category != null) {
            switch (category) {
                case "Fruits":
                    if("en".equals(language)) {
                        wordList.add("Apple");
                        wordList.add("Banana");
                        wordList.add("Orange");
                        wordList.add("Durian");
                        wordList.add("Grape");
                        wordList.add("Strawberry");
                        wordList.add("Dragon Fruit");
                        wordList.add("Santol");
                        wordList.add("Jackfruit");
                        wordList.add("Rose Apple");
                        wordList.add("Cherry");
                        wordList.add("Guava");
                        wordList.add("Coconut");
                        wordList.add("Mango");
                    }else {
                        wordList.add("แอปเปิ้ล");
                        wordList.add("กล้วย");
                        wordList.add("ส้ม");
                        wordList.add("ทุเรียน");
                        wordList.add("องุ่น");
                        wordList.add("สตรอเบอร์รี่");
                        wordList.add("แก้วมังกร");
                        wordList.add("กระท้อน");
                        wordList.add("ขนุน");
                        wordList.add("ชมพู่");
                        wordList.add("เชอรี่");
                        wordList.add("ฝรั่ง");
                        wordList.add("มะพร้าว");
                        wordList.add("มะม่วง");
                    }
                    break;


                case "Animals":
                    if("en".equals(language)) {
                        wordList.add("Lion");
                        wordList.add("Elephant");
                        wordList.add("Tiger");
                        wordList.add("Sea Lion");
                        wordList.add("Hippopotamus");
                        wordList.add("Cat");
                        wordList.add("Monkey");
                        wordList.add("Cow");
                        wordList.add("Buffalo");
                        wordList.add("Dog");
                        wordList.add("Deer");
                        wordList.add("Mouse");
                        wordList.add("Bear");
                        wordList.add("Sloth");
                        wordList.add("Camel");
                        wordList.add("Owl");
                    }else {
                        wordList.add("สิงโต");
                        wordList.add("ช้าง");
                        wordList.add("เสือ");
                        wordList.add("สิงโตทะเล");
                        wordList.add("ฮิปโป");
                        wordList.add("แมว");
                        wordList.add("ลิง");
                        wordList.add("วัว");
                        wordList.add("ควาย");
                        wordList.add("สุนัข");
                        wordList.add("กวาง");
                        wordList.add("หนู");
                        wordList.add("หมี");
                        wordList.add("สล็อท");
                        wordList.add("อูฐ");
                        wordList.add("นกฮูก");
                    }
                    break;



                case "Food":
                    if("en".equals(language)) {
                        wordList.add("Gravy Noodle With Marinated Pork");
                        wordList.add("Stir-Fried Minced Pork ");
                        wordList.add("Rice With Pork Leg Stew");
                        wordList.add("Rice Steamed With Chicken Soup");
                        wordList.add("Spicy Tom Yum Pork Noodle Soup");
                        wordList.add("Spaghetti Carbonara");
                        wordList.add("Pizza");
                        wordList.add("Spicy Tom Yum Shrimp");
                        wordList.add("Fried Pork");
                        wordList.add("Papaya Salad");
                        wordList.add("Thai Curry Noodles With Chicken");
                        wordList.add("Spicy Minced Pork Salad ");
                        wordList.add("Fried Crab In Yellow Curry");
                        wordList.add("Fried Fish");
                        wordList.add("Fried Rice");
                    }else {
                        wordList.add("ราดหน้าหมู");
                        wordList.add("กะเพราไก่ไข่ดาว");
                        wordList.add("ข้าวขาหมู");
                        wordList.add("ข้าวมันไก่");
                        wordList.add("ก๋วยเตี๋ยวเส้นเล็กต้มยำ");
                        wordList.add("สปาเก็ตตีคาโบนารา");
                        wordList.add("พิซซ่า");
                        wordList.add("ต้มยำกุ้งน้ำข้น");
                        wordList.add("หมูทอด");
                        wordList.add("ส้มตำ");
                        wordList.add("ข้าวซอยไก่");
                        wordList.add("ลาบหมู");
                        wordList.add("ปูผัดผงกระหรี่");
                        wordList.add("ปลาทอด");
                        wordList.add("ข้าวผัด");
                    }
                    break;




                case "Drinks":
                    if("en".equals(language)) {
                        wordList.add("Americano");
                        wordList.add("Latte");
                        wordList.add("Thai Tea");
                        wordList.add("Green Tea");
                        wordList.add("Thai Black Coffee");
                        wordList.add("Cocoa");
                        wordList.add("Orange Juice");
                        wordList.add("Chrysanthemum Tea");
                        wordList.add("Ginger Tea");
                        wordList.add("Fruit Juice");
                        wordList.add("Italian Soda");
                        wordList.add("Soft Drink");
                        wordList.add("Iced Black Tea");
                        wordList.add("Boba Tea");
                        wordList.add("Cheese Tea");
                    }else {
                        wordList.add("อเมริกาโน");
                        wordList.add("ลาเต้");
                        wordList.add("ชาไทย");
                        wordList.add("ชาเขียว");
                        wordList.add("โอเลี้ยง");
                        wordList.add("โกโก้");
                        wordList.add("น้ำส้ม");
                        wordList.add("เก๊กฮวย");
                        wordList.add("น้ำขิง");
                        wordList.add("น้ำผลไม้");
                        wordList.add("อิตาเลียนโซดา");
                        wordList.add("น้ำอัดลม");
                        wordList.add("ชาดำเย็น");
                        wordList.add("ชานมไข่มุก");
                        wordList.add("ชาชีส");
                    }
                    break;




                case "Hit Song":
                    if("en".equals(language)) {
                        wordList.add("Day One");
                        wordList.add("Golden-Tongued Bird (Sali Ka Lin Tong)");
                        wordList.add("Moonlit Floor");
                        wordList.add("Like a Wedding (Muean Wiwa)");
                        wordList.add("Hidden, Not Seeking (Son Mai Ha)");
                        wordList.add("Sky Loves Dad (Fa Rak Pho)");
                        wordList.add("Brother Zone");
                        wordList.add("Espresso");
                        wordList.add("Hot2Hot");
                        wordList.add("Tear Drops (Yot Nam Ta)");
                        wordList.add("APT");
                        wordList.add("Aen Ra Naeng (Rafter Bird Pose)");
                        wordList.add("Because They Love (Khon Man Rak)");
                        wordList.add("Still Deleted? (Lop Yang)");
                        wordList.add("Fades Away (Jang Hai)");

                    }else {
                        wordList.add("Day One");
                        wordList.add("สาลิกาลิ้นทอง");
                        wordList.add("Moonlit Floor");
                        wordList.add("เหมือนวิวาห์");
                        wordList.add("ซ่อนไม่หา");
                        wordList.add("ฟ้ารักพ่อ");
                        wordList.add("แค่นัองชาย");
                        wordList.add("Espresso");
                        wordList.add("Hot2Hot");
                        wordList.add("หยดน้ำตา");
                        wordList.add("Apt");
                        wordList.add("แอ่นระเเนง");
                        wordList.add("คนมันรัก");
                        wordList.add("ลบยัง");
                        wordList.add("จางหาย");

                    }
                    break;


                case "Folk Song":
                    if("en".equals(language)) {
                        wordList.add("Suay khayee Jai ");
                        wordList.add("Yai Laem ");
                        wordList.add("Thephee Baan Phai ");
                        wordList.add("Sang Dai Sang Laew ");
                        wordList.add("Kham Paeng ");
                        wordList.add("Bak Khon Shua ");
                        wordList.add("Ror Pen Khon That Pai ");
                        wordList.add("Ngai Ngong  ");
                        wordList.add("Yark Pen Khon Rak Mai Yark Pen Choo ");
                        wordList.add("Mai Mee Khaw Mae Tang Tae Rearm Ton ");

                    }else {
                        wordList.add("สวยขยี้ใจ");
                        wordList.add("ยายเเล่ม");
                        wordList.add("เทพีบ้านไพร");
                        wordList.add("ซังได้ซังแล้ว");
                        wordList.add("คำแพง");
                        wordList.add("บักคนชั่ว");
                        wordList.add("รอเป็นคนถัดไป");
                        wordList.add("ไหง่ง่อง");
                        wordList.add("อยากเป็นคนรักไม่อยากเป็นชู้");
                        wordList.add("ไม่มีข้อแม้ตั้งแต่เริ่มต้น");
                    }
                    break;




                case "Sweets":
                    if("en".equals(language)) {
                        wordList.add("Chocolate Dubai");
                        wordList.add("Soft Cookies");
                        wordList.add("Durian Cheesecake");
                        wordList.add("Croffle");
                        wordList.add("Banana Cake");
                        wordList.add("Khanom Khai (Egg Snack)");
                        wordList.add("Ice Cream");
                        wordList.add("Cheese Tart");
                        wordList.add("Sticky Rice with Mango");
                        wordList.add("Foy Thong (Golden Threads)");
                        wordList.add("Thong Yot (Egg Yolk Drops)");
                        wordList.add("Med Kanun (Jackfruit Seeds)");
                        wordList.add("Bua Loi (Glutinous Rice Balls in Coconut Milk)");
                        wordList.add("Kluay Buat Chee (Banana in Coconut Milk)");
                        wordList.add("Khanom Chan (Layered Cake)");

                    }else {
                        wordList.add("ช็อคโกแลตดูไบ");
                        wordList.add("ซอฟคุกกี้");
                        wordList.add("ชีสเค้กทุเรียน");
                        wordList.add("ครอฟเฟิล");
                        wordList.add("เค้กกล้วยหอม");
                        wordList.add("ขนมไข่");
                        wordList.add("ไอศกรีม");
                        wordList.add("ชีสทาร์ต");
                        wordList.add("ข้าวเหนียวมะม่วง");
                        wordList.add("ฝอยทอง");
                        wordList.add("ทองหยอด");
                        wordList.add("เม็ดขนุน");
                        wordList.add("บัวลอย");
                        wordList.add("กล้วยบวชชี");
                        wordList.add("ขนมชั้น");
                    }
                    break;




                case "Celebrity":
                    if("en".equals(language)) {
                        wordList.add("Yaya");
                        wordList.add("Lisa (Blackpink)");
                        wordList.add("Nadech )");
                        wordList.add("Num Kanchai ");
                        wordList.add("Pimrypie ");
                        wordList.add("V (BTS)");
                        wordList.add("Jungkook (BTS)");
                        wordList.add("BamBam (Got7)");
                        wordList.add("Pita Limcharoenrat ");
                        wordList.add("Prayuth Chan-o-cha ");
                        wordList.add("Ten (WayV)");
                        wordList.add("Mind (4EVE)");
                        wordList.add("Bow Kanyarat ");
                        wordList.add("Palmmy )");
                        wordList.add("Settha Thavisin");

                    }else {
                        wordList.add("ญาญ่า");
                        wordList.add("ลิซ่า Blackpink");
                        wordList.add("ณเดช");
                        wordList.add("หนุ่มกรรชัย");
                        wordList.add("พิมรี่พาย");
                        wordList.add("V bts");
                        wordList.add("จองกุก BTS");
                        wordList.add("แบมแบม Got7");
                        wordList.add("พิธา ลิ้มเจริญรัตน์");
                        wordList.add("ประยุทธ์ จันโอชา");
                        wordList.add("เต็น wayv");
                        wordList.add("มายด์ 4eve");
                        wordList.add("โบว์ กัญญารัตน์");
                        wordList.add("ปามมี่");
                        wordList.add("เศรษฐา ทวีสิน");
                    }
                    break;



                case "Place":
                    if("en".equals(language)) {
                        wordList.add("Chiang Khan");
                        wordList.add("Khao Kho");
                        wordList.add("Khao Yai");
                        wordList.add("Phu Chi Fa");
                        wordList.add("Koh Chang");
                        wordList.add("Hua Hin");
                        wordList.add("Chao Lao");
                        wordList.add("khao Si Thep");
                        wordList.add("Art Museum");
                        wordList.add("Bang Saen");

                    }else {
                        wordList.add("เชียงคาน");
                        wordList.add("เขาค้อ");
                        wordList.add("เขาใหญ่");
                        wordList.add("ภูชี้ฟ้า");
                        wordList.add("เกาะช้าง");
                        wordList.add("หัวหิน");
                        wordList.add("เจ้าหลาว");
                        wordList.add("สีเทพ");
                        wordList.add("หอศิลป์");
                        wordList.add("บางแสน");
                    }
                    break;



                default:
                    break;
            }
        }
    }


    private void startGame() {
        currentIndex = 0;
        score = 0;
        timeLeftInMillis = 60000; // Reset timer to 1 minute
        isTiltedForward = false; // Reset sensor states
        isTiltedBackward = false;

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        // ซ่อน UI อื่น ๆ ระหว่างการนับถอยหลัง
        wordTextView.setVisibility(View.VISIBLE); // แสดงเฉพาะ TextView สำหรับเลขนับถอยหลัง
        scoreTextView.setVisibility(View.GONE);
        timerTextView.setVisibility(View.GONE);
        restartButton.setVisibility(View.GONE);
        backToCategoryButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);

        if (!wordList.isEmpty()) {
            new CountDownTimer(4000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    wordTextView.setText(String.valueOf(millisUntilFinished / 1000)); // แสดงตัวเลขนับถอยหลัง
                }

                @Override
                public void onFinish() {
                    wordTextView.setVisibility(View.VISIBLE);
                    scoreTextView.setVisibility(View.VISIBLE);
                    timerTextView.setVisibility(View.VISIBLE);
                    showNewWord(); // แสดงคำศัพท์คำแรก
                    startTimer();  // เริ่มจับเวลา
                    scoreTextView.setText("Score: 0");
                    timerTextView.setText("Time Left: 60s");
                }
            }.start();
        } else {
            wordTextView.setText("No words available in this category.");
            endGame();
        }
    }



    //    private void restartGame() {
//        // Reinitialize the word list based on the current category
//        String category = getIntent().getStringExtra("category");
//        initializeWordList(category);
//
//        // รีลงทะเบียนเซนเซอร์
//        if (accelerometer != null) {
//            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//
//        // รีเซ็ตสถานะเกมและเริ่มใหม่
//        startGame();
//    }
    private void restartGame() {
        stopTimer(); // หยุด Timer ปัจจุบัน
        resetTimer(); // รีเซ็ตเวลาเป็น 60 วินาที
        score = 0; // รีเซ็ตคะแนน
        currentIndex = 0; // รีเซ็ตคำศัพท์
        isGameRunning = false;
        startTimer(); // เริ่ม Timer ใหม่
        updateUI(); // อัปเดต UI
    }


    private void showNewWord() {
        if (currentIndex < wordList.size()) {
            wordTextView.setText(wordList.get(currentIndex));
        } else {
            endGame();
        }
    }


    //        private void startTimer() {
//        startTime = System.currentTimeMillis();
//        isGameRunning = true;
//
//        timerRunnable = new Runnable() {
//            @Override
//            public void run() {
//                if (!isGameRunning) return; // ตรวจสอบสถานะเกม
//
//                long millis = 60000 - (System.currentTimeMillis() - startTime);
//                if (millis > 0) {
//
//                    timerTextView.setText("Time Left: " + millis / 1000 + "s");
//                    timerHandler.postDelayed(this, 100); // เรียกซ้ำทุก 100ms
//                } else {
//                    endGame();
//                }
//            }
//        };
//
//        timerHandler.post(timerRunnable); // เริ่ม Timer
//    }
    private void startTimer() {
        if (isGameRunning) return; // หลีกเลี่ยงการเริ่มซ้ำ

        startTime = System.currentTimeMillis(); // บันทึกเวลาเริ่มต้นใหม่
        isGameRunning = true;

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isGameRunning) return; // ตรวจสอบสถานะเกม

                // คำนวณเวลาที่เหลือโดยใช้เวลาที่เหลือก่อนหน้าและเวลาที่ผ่านไป
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = timeLeftInMillis - elapsedTime;

                if (remainingTime > 0) {
                    timerTextView.setText("Time Left: " + remainingTime / 1000 + "s");
                    timerHandler.postDelayed(this, 100); // อัปเดตทุก 100 มิลลิวินาที
                } else {
                    timeLeftInMillis = 0; // ตั้งเวลาให้แน่ใจว่าเหลือ 0
                    endGame(); // จบเกมเมื่อหมดเวลา
                }
            }
        };

        timerHandler.post(timerRunnable); // เริ่ม Timer
    }



    private void stopTimer() {
        if (!isGameRunning) return; // หลีกเลี่ยงการหยุดซ้ำ
        isGameRunning = false;
        long elapsedTime = System.currentTimeMillis() - startTime;
        timeLeftInMillis -= elapsedTime; // ลดเวลาที่ผ่านไปจากเวลาที่เหลือ
        timerHandler.removeCallbacks(timerRunnable); // หยุด Timer
    }

    private void updateTimerUI() {
        int secondsLeft = (int) (timeLeftInMillis / 1000);
        timerTextView.setText("Time Left: " + secondsLeft + "s");
    }

    private void resetTimer() {
        timeLeftInMillis = 60000; // รีเซ็ตเวลาเป็น 60 วินาที
        updateTimerUI(); // อัปเดต UI ให้ตรงกับค่าเริ่มต้น
    }

    private void endGame() {
        stopTimer(); // หยุดเวลาเมื่อเกมจบ
        sensorManager.unregisterListener(this); // ปิดการใช้งาน Sensor
        wordTextView.setText("Game Over!");
        scoreTextView.setText("Final Score: " + score);
        timerTextView.setText("Time's up!");
        restartButton.setVisibility(View.VISIBLE); // แสดงปุ่ม Restart
        backToCategoryButton.setVisibility(View.VISIBLE); // Show back to categories button
        backButton.setVisibility(View.GONE);
    }

    private void updateUI() {
        // Update UI components based on the restored state
        if (currentIndex < wordList.size()) {
            wordTextView.setText(wordList.get(currentIndex));
        } else {
            wordTextView.setText("Game Over!");
        }
        scoreTextView.setText("Score: " + score);
        timerTextView.setText("Time Left: " + (timeLeftInMillis / 1000) + "s");
        restartButton.setVisibility(View.GONE); // Hide restart button
        backToCategoryButton.setVisibility(View.GONE); // Hide back button
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentIndex", currentIndex);
        outState.putInt("score", score);
        outState.putLong("timeLeftInMillis", timeLeftInMillis);
        outState.putStringArrayList("wordList", wordList);

        // Cancel timer to avoid memory leaks
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentIndex = savedInstanceState.getInt("currentIndex", 0);
        score = savedInstanceState.getInt("score", 0);
        timeLeftInMillis = savedInstanceState.getLong("timeLeftInMillis", 60000);
        wordList = savedInstanceState.getStringArrayList("wordList");

        // Resume the game state
        updateUI();
        startTimer(); // Resume timer
    }

    //
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        // บันทึกเวลาที่เหลือใน SharedPreferences
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putLong(TIME_LEFT_KEY, timeLeftInMillis);
//        editor.apply();
//
//        // ยกเลิกการทำงานของ Listener และ Timer
//        sensorManager.unregisterListener(this);
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//        stopTimer(); // หยุดตัวจับเวลา
//    }
    @Override
    protected void onPause() {
        super.onPause();
        if (isGameRunning) {
            stopTimer(); // หยุด Timer เมื่อออกจากแอป
        }
    }


    //    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // ดึงเวลาที่เหลือจาก SharedPreferences
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        timeLeftInMillis = prefs.getLong(TIME_LEFT_KEY, 60000); // ใช้ 60 วินาทีเป็นค่าเริ่มต้น
//
//        if (accelerometer != null) {
//            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//
//        if (currentIndex < wordList.size()) {
//            startTimer(); // เริ่มการนับเวลาใหม่ตามเวลาที่เหลือ
//        }
//    }
    @Override
    protected void onResume() {
        super.onResume();
        if (timeLeftInMillis > 0 && !isGameRunning) {
            startTimer(); // เริ่ม Timer ต่อเมื่อมีเวลาเหลือ
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float z = event.values[2];

        if (currentIndex >= wordList.size()) {
            return; // Prevent further actions if the game is over
        }

        // Tilt downward (face down): Correct answer
        if (z < -7 && !isTiltedForward) {
            isTiltedForward = true;
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            currentIndex++;
            showNewWord();
        }

        // Tilt upward (face up): Skip word
        else if (z > 7 && !isTiltedBackward) {
            isTiltedBackward = true;
            Toast.makeText(this, "Skipped!", Toast.LENGTH_SHORT).show();
            currentIndex++;
            showNewWord();
        }

        // Reset tilt status
        if (z > -2 && z < 2) {
            isTiltedForward = false;
            isTiltedBackward = false;
        }

        // Update score
        scoreTextView.setText("Score: " + score);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No action needed
    }

    private void showBackConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirm Exit")
                .setMessage("Are you sure you want to exit the game? Your progress will not be saved.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // หยุด Timer และกลับไปยังหน้าหมวดหมู่
                    stopTimer();
                    sensorManager.unregisterListener(this); // หยุดเซนเซอร์
                    Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
                    startActivity(intent);
                    finish(); // ปิด Activity ปัจจุบัน
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss(); // ปิด Dialog หากผู้ใช้ยกเลิก
                })
                .show();
    }

}