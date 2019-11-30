package com.example.shirin_pc.inputtime;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import android.os.Handler;
public class MainActivity extends AppCompatActivity {


    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private EditText mEditTextInput;
    private TextView mTextViewCountDown,tv;
    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
  private long mTimeLeftInMillis;
    private long mEndTime;

   public static int recivecolor=4;

//json part
     RequestQueue requestQueue;
    static final int INTERNET_REQ = 23;
    static final String REQ_TAG = "VACTIVITY";

 //jsonpart

//send datat to widget class
    public static boolean red =false;
    public static boolean orange=false;
    public static boolean green=false;

    public   Thread t=new Thread();


    //for lock and unlock 2
    private BroadcastReceiver mReceiver;
    //

    public  static boolean flag_start=false;
    public  static boolean flag_active=false;
    //private Button button;
     ArrayList<String> reciveList;
    //private Button Loop_compare;
    public String RunItem;
    public  static String temp="Shirin";
    public static String variable;
    private TextView show_task,lable_task;
    private EditText input_task;
    private Button loop;
    private  boolean startflag=false;

//receive task from desktop
    public String receive_task;
    public boolean receive_status;
    public long recive_time;

    //Test part
    public String getapprun;
    Button btn;
//test button instead of json
    public String tas="email";
    long tim =60000;
    public String input;



    @Override
    protected void onPause() {
        super.onPause();





    }

    @Override
    protected void onResume() {
        super.onResume();






    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//for lock&unlock  1
        Log.v("$$$$$$", "In Method: onDestroy()");

        if (mReceiver != null)
        {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
//for lock&unlock  1


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show_task= (TextView) findViewById(R.id.show_task);
//background color
        getWindow().getDecorView().setBackgroundColor(Color.rgb(242, 250, 224));
//background color



//json
        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();
 //json

    //json get

        String url ="https://consciously.free.beeceptor.com/my/api";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                           receive_task  =response.getString("current_task");
                            receive_status=response.getBoolean("status");
                            recive_time=response.getLong("time");
                            String str = recive_time+"";
                           // Toast.makeText(MainActivity.this,str, Toast.LENGTH_SHORT).show();
                            String s1=String.valueOf(receive_status);
//                            tv= (TextView)findViewById(R.id.textView2);
//                            tv.setText(s1);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(MainActivity.this,response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error getting response", Toast.LENGTH_SHORT).show();

            }
        });
        jsonObjectRequest.setTag(REQ_TAG);
        requestQueue.add(jsonObjectRequest);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (receive_status==true) {
                    show_task.setText(receive_task);
                    mTimeLeftInMillis = recive_time;
                    startTimer();
                }

            }
        }, 5000);

  // if to check start app as new task or continue with modify of desktop version
if (receive_status==true){
    show_task.setText(receive_task);
        mTimeLeftInMillis=recive_time;
    startTimer();



}


      //  test method changecolor

//             Thread t=new Thread(){
//
//
//            @Override
//            public void run(){
//
//                while(!isInterrupted()){
//
//                    try {
//                        Thread.sleep(5000);  //1000ms = 1 sec
//
//                        runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//
//                           ChangeLogoColor();
//
//                            }
//
//
//                        });
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        };
//
//        t.start();


//test





//for lock and unlock3
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // Customized BroadcastReceiver class


        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);

//for lock&unlock  3


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        }
//one time
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity

            startActivity(new Intent(MainActivity.this, Skipt.class));
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG)
                    .show();
        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();




          //

        lable_task=(TextView) findViewById(R.id.label_task);
        input_task= (EditText) findViewById(R.id.input_task);


        //
        mEditTextInput = (EditText) findViewById(R.id.edit_text_input);
        mTextViewCountDown = (TextView) findViewById(R.id.text_view_countdown);
        //


        //
        mButtonSet = (Button) findViewById(R.id.button_set);
        mButtonStartPause = (Button) findViewById(R.id.button_start_pause);
        mButtonReset = (Button) findViewById(R.id.button_reset);


        //test json








        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;

                if (millisInput == 0) {
                    Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });



        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//json start //post json file

                String task=mEditTextInput.getText().toString();

                String input = mEditTextInput.getText().toString();
               long millisInput1 = Long.parseLong(input) * 60000;


                JSONObject json = new JSONObject();
                try {
                    json.put("current_task",task);
                    json.put("time",millisInput1);
                    json.put("status",flag_active);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url ="https://consciously.free.beeceptor.com/my/api";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(MainActivity.this,response.toString(), Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Error getting response".toString(), Toast.LENGTH_SHORT).show();

                }
                });
                jsonObjectRequest.setTag(REQ_TAG);
                requestQueue.add(jsonObjectRequest);
  //json end


                //treat compare to distracted list and run app

   //each 5 second check application is open or no .

     if(startflag=true ) {
         Intent intent = getIntent();
         reciveList = intent.getStringArrayListExtra("KeyList");


         // loop and compare
         Thread t = new Thread() {


             @Override
             public void run() {

                 while (!isInterrupted()) {

                     try {
                         Thread.sleep(5000);  //1000ms = 1 sec

                         runOnUiThread(new Runnable() {

                             @Override
                             public void run() {

                                 boolean flag_status = Compare();
                                 if (flag_status) {
                                     OpenWidget();
                                     //Toast.makeText(MainActivity.this,"This works" ,Toast.LENGTH_LONG).show();
                                    // flag_open_distract_app = false;
                                 }

                             }


                         });

                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }

                 }
             }
         };

         t.start();
     }

               // End of each 5 second check application is open or no .

                //treat compare to distracted list and run app
                if(reciveList!=null) {




//end of loop and compare


                    show_task.setText(input_task.getText());
                    if (mTimerRunning) {

                        pauseTimer();

                    } else {
                        // show_task.setText(input_task.getText());
                        startTimer();
                        ;
                    }
                }else {show_task.setText(input_task.getText());
                    if (mTimerRunning) {

                        pauseTimer();

                    } else {
                        // show_task.setText(input_task.getText());
                        startTimer();

                    }}
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });





        //json






    }



    //json start

    private Response.Listener<String> getPostResponseListener(){
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this,"String Response : "+ response , Toast.LENGTH_SHORT).show();

            }
        };
    }

    private Response.ErrorListener getPostErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error getting response" , Toast.LENGTH_SHORT).show();

            }
        };
    }


    //json end



    //Compare method
    public  boolean Compare(){
        boolean flag_same=false;
        String compare=Info_App_Run();
        //Toast.makeText(MainActivity.this, compare, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < reciveList.size(); i++) {
            if (compare.contains(reciveList.get(i))) {
                flag_same = true;
                break;
            }

        }
        return flag_same ;
    }
//end of Compare method

    //get app run
    public String Info_App_Run(){

        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        String sss=procInfos.get(0).processName;
        //Toast.makeText(MainActivity.this, temp, Toast.LENGTH_SHORT).show();
        if(temp.equals(sss) )
        {
            //Toast.makeText(MainActivity.this, RunItem, Toast.LENGTH_SHORT).show();
            RunItem="sh";
        }
        else
        {
            RunItem= procInfos.get(0).processName;

        }

        temp=sss;
        //Toast.makeText(MainActivity.this, "TEMP IS "+temp+"SSS is "+sss+"Runtime is: "+RunItem , Toast.LENGTH_SHORT).show();

        return RunItem;
    }

    //end get app run
////////////////////////////////////////////////////

   //open widget to show you distracted

    public void OpenWidget(){
        //flag_open_distract_app=true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(MainActivity.this, Widget_App_Distract.class));
            finish();
        } else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(MainActivity.this, Widget_App_Distract.class));
            finish();
        } else {
            askPermission();
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
//

//open widdget to show timw is finish
    }
    public void OpenFinish(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(MainActivity.this,
                   Widget_Finish_Time.class));
            finish();
        } else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(MainActivity.this, Widget_Finish_Time.class));
            finish();
        } else {
            askPermission();
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }

    }







//********************************************
    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);

    }
//Bar Menu **********************************************************************
    // Bar Menu to go other pages
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.distracted_list:
                openDistracted_list();

                return true;
            case R.id.help:
                Toast.makeText(this,"Item tutorial selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.exit:
                this.finishAffinity();


            default: return super.onOptionsItemSelected(item);
        }
    }


    // Go to distaracted page
    public void openDistracted_list() {
        Intent intent=new Intent(this,Distracted_List.class);
        startActivity(intent);
    }
    //End Go to distaracted page


    //End Go to Run_app page
    //End  bar Menu
    /////////////****************************************************

    //Timer
    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {


flag_start=true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(MainActivity.this, FloatingViewService.class));


        } else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(MainActivity.this, FloatingViewService.class));

        } else {
            askPermission();

        }
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                ChangeLogoColor();

                updateCountDownText();
            }
//////////////////////////////////
            @Override
            public void onFinish() {
                //to poen widget finishtime
                OpenFinish();
                //to poen widget finishtime


                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {

flag_start=false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(MainActivity.this, FloatingViewService.class));


        } else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(MainActivity.this, FloatingViewService.class));

        } else {
            askPermission();
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    public void updateCountDownText() {

       //test timer logo


        //

        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
        variable=timeLeftFormatted;

    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
            input_task.setVisibility(View.GONE);
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");
            show_task.setText(null);


            if (mTimeLeftInMillis < 1000) {


                mButtonStartPause.setVisibility(View.INVISIBLE);
                //show_task.setVisibility(View.GONE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
              input_task.setVisibility(View.VISIBLE);

            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
                //show_task.setVisibility(View.GONE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
              //  show_task.setVisibility(View.VISIBLE);

            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //json

        if (requestQueue != null) {
            requestQueue.cancelAll(REQ_TAG);
        }
        //json



        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {

                mTimeLeftInMillis = 0;
                if(mTimeLeftInMillis==0)//{Toast.makeText(this, "Y", Toast.LENGTH_SHORT).show();}
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }
//end of timer
    //for lock&unlock  4
    public class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                Log.v("$$$$$$", "In Method:  ACTION_SCREEN_OFF");
                Toast.makeText(MainActivity.this, "Off", Toast.LENGTH_SHORT).show();
                // onPause() will be called.
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

                Log.v("$$$$$$", "In Method:  ACTION_SCREEN_ON");
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    startService(new Intent(MainActivity.this, WelcomeWidget.class));


                } else if (Settings.canDrawOverlays(MainActivity.this)) {
                    startService(new Intent(MainActivity.this, WelcomeWidget.class));

                } else {
                    askPermission();
                    Toast.makeText(MainActivity.this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();
//onResume() will be called.

//Better check for whether the screen was already locked
// if locked, do not take any resuming action in onResume()

                //Suggest you, not to take any resuming action here.
            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                Log.v("$$$$$$", "In Method:  ACTION_USER_PRESENT");
//Handle resuming events
            }

        }


    }

//for lock&unlock  4


    private void ChangeLogoColor(){

        int i=2;

        if(mTimeLeftInMillis> (mStartTimeInMillis/2)) i= 1;
        if(mTimeLeftInMillis > mStartTimeInMillis/4  && mTimeLeftInMillis< (mStartTimeInMillis/2)) i = 0;
        if(mTimeLeftInMillis< mStartTimeInMillis / 4) i = -1;


        switch (i)
        {
            case 1:

                recivecolor=1;
              green=true;
                break;

            case 0:

                recivecolor=0;
                orange=true;
                break;

            case -1:
                recivecolor=-1;

                red=true;

                break;

        }

    }


}


