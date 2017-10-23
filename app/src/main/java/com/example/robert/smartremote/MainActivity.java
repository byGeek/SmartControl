package com.example.robert.smartremote;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    EditText txtIpAddress;
    Button btnConnnect;
    Button btnScrollUp;
    Button btnScrollDown;
    TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtIpAddress = (EditText) findViewById(R.id.txt_ip_addr);
        btnConnnect = (Button)findViewById(R.id.btnConnect);
        btnScrollDown = (Button)findViewById(R.id.btnScrollDown);
        btnScrollUp = (Button)findViewById(R.id.btnScrollUp);
        txtStatus = (TextView)findViewById(R.id.txt_status);

        //setBtnState(true);
    }

    public void onConnect(View view) {
        String ipAddr = txtIpAddress.getText().toString();
        if (!validate(ipAddr)) {
            Toast.makeText(getApplicationContext(), "Please input correct server ip address!", Toast.LENGTH_SHORT).show();
            return;
        }

        Button btn = (Button)view;
        String[] command = null;
        switch (btn.getId()){
            case R.id.btnConnect:
                command = new String[]{CmdType.CONNECT, ipAddr};
                break;
            case R.id.btnScrollUp:
                command = new String[]{CmdType.SCROLL_UP, ipAddr};
                break;
            case R.id.btnScrollDown:
                command = new String[]{CmdType.SCROLL_DOWN, ipAddr};
                break;
        }
        new SocketController(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                if(output == SocketController.Result.OK){
                    txtStatus.setText("Command OK");
                    txtStatus.setTextColor(Color.BLACK);
                    //setBtnState(false);
                    //btnConnnect.setText("DisConnect");
                } else {
                    txtStatus.setText("Command FAIL.");
                    txtStatus.setTextColor(Color.RED);
                    //setBtnState(true);
                    //btnConnnect.setText("Connect");
                }
            }
        }).execute(command);
    }

    private void setBtnState(boolean flag){
        //btnConnnect.setEnabled(flag);
        btnScrollUp.setEnabled(!flag);
        btnScrollDown.setEnabled(!flag);
    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }
}
