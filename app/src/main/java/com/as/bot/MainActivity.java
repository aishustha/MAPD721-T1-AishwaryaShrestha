package com.as.bot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChatService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button
        Button generate_message = (Button) findViewById(R.id.generate_button);
        Button stop_service = findViewById(R.id.stop_button);

        generate_message.setOnClickListener(this);
        stop_service.setOnClickListener(this);

        registerServiceStateChangeReceiver();
    }

    //Generate Message
    private void generateMessage(){
        Log.v(TAG, "Message");
        Bundle data = new Bundle();
        data.putInt(ChatService.CMD, ChatService.CHAT_GENERATE_MESSAGE);
        Intent intent = new Intent(this, ChatService.class);
        intent.putExtras(data);
        startService(intent);
    }

    //Stop Service
    private void stopService(){
        Bundle data = new Bundle();
        data.putInt(ChatService.CMD, ChatService.CHAT_STOP_SERVICE);
        Intent intent = new Intent(this, ChatService.class);
        intent.putExtras(data);
        startService(intent);
    }

    //receiving ID
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.generate_button:
                generateMessage();
                break;
            case R.id.stop_button:
                stopService();
                break;
        }
    }

    //Broadcast Receiver - receives message from the Chat Bot Service and update UI with the message
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            TextView result = (TextView) findViewById(R.id.displayResult);
            if (Constants.BROADCAST_GENERATE_MESSAGE.equals(action)) {
                result.findViewById(R.id.displayResult);
                result.setText("Hello Aishwarya \n" +
                        "How are you? \n" +
                        "Good Bye Aishwarya ");

            } else if (Constants.BROADCAST_STOP_SERVICE.equals(action)) {
                //where XX two last digits of our student number
                result.setText("Chat Bot Stopped: 62");
            }
        }
    };

    //Registration
    private void registerServiceStateChangeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_GENERATE_MESSAGE);
        intentFilter.addAction(Constants.BROADCAST_STOP_SERVICE);
        registerReceiver(broadcastReceiver, intentFilter);
    }
}
