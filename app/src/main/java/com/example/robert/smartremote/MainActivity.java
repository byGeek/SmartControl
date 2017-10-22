package com.example.robert.smartremote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    EditText txtIpAddress;
    SocketController socketController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtIpAddress = (EditText) findViewById(R.id.txt_ip_addr);
        //socketController = new SocketController();
    }

    public void onConnect(View view) {
        String ipAddr = txtIpAddress.getText().toString();
        if (TextUtils.isEmpty(ipAddr)) {
            Toast.makeText(getApplicationContext(), "Please input server ip address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (socketController == null) {
            socketController = new SocketController();
        }
        String[] command = new String[]{CmdType.CONNECT, ipAddr};
        socketController.execute(command);
    }

    public void onScrollUp(View view) {
        if (socketController != null){
            socketController.execute(CmdType.SCROLL_UP);
        }
    }

    public void onScrollDown(View view) {
        if(socketController != null){
            socketController.execute(CmdType.SCROLL_DOWN);
        }
    }
}
