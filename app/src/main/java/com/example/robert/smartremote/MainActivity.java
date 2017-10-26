package com.example.robert.smartremote;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText txtIpAddress;
    Button btnConnnect;
    Button btnScrollUp;
    Button btnScrollDown;
    TextView txtStatus;

    private Socket _socket;
    private PrintWriter _printWriter;
    private boolean _isConnected = false;
    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this._context = this;  //save the context to make Toast

        txtIpAddress = (EditText) findViewById(R.id.txt_ip_addr);
        btnConnnect = (Button) findViewById(R.id.btnConnect);
        btnScrollDown = (Button) findViewById(R.id.btnScrollDown);
        btnScrollUp = (Button) findViewById(R.id.btnScrollUp);
        txtStatus = (TextView) findViewById(R.id.txt_status);

        txtIpAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String ipAddr = txtIpAddress.getText().toString();
                    if (!validate(ipAddr)) {
                        Toast.makeText(getApplicationContext(), "Please input correct server ip address!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        btnConnnect.setOnClickListener(this);
        btnScrollDown.setOnClickListener(this);
        btnScrollUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String ipAddr = txtIpAddress.getText().toString();
        if (!validate(ipAddr)) {
            Toast.makeText(getApplicationContext(), "Please input correct server ip address!", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] command = null;
        switch (view.getId()) {
            case R.id.btnConnect:
                if (!_isConnected) {
                    ConnectServerTask connectServerTask = new ConnectServerTask();
                    connectServerTask.execute(ipAddr);
                } else {
                    close(_socket);
                    close(_printWriter);
                    btnConnnect.setText("Connect");
                    _isConnected = false;
                }
                break;
            case R.id.btnScrollUp:
                if (_isConnected && _printWriter != null) {
                    new SendCommandTask().execute(CmdType.SCROLL_UP);
                    //use PrinterWriter
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                _printWriter.writeUTF(CmdType.SCROLL_UP);
                                _printWriter.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();*/


                }
                break;
            case R.id.btnScrollDown:
                if (_isConnected && _printWriter != null) {
                    new SendCommandTask().execute(CmdType.SCROLL_DOWN);
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                _printWriter.writeUTF(CmdType.SCROLL_DOWN);
                                _printWriter.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();*/
                }
                break;
            default:
                return;
        }

        /*new SocketController(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                if (output == SocketController.Result.OK) {
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
        }).execute(command);*/
    }

    private void setBtnState(boolean flag) {
        //btnConnnect.setEnabled(flag);
        btnScrollUp.setEnabled(!flag);
        btnScrollDown.setEnabled(!flag);
    }

    public static void close(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    public class ConnectServerTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            boolean result = false;
            String ipAddress = params[0];
            try {
                InetSocketAddress socketAddress = new InetSocketAddress(ipAddress, 11000);
                _socket = new Socket();

                _socket.connect(socketAddress, 3000);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            _isConnected = result;
            Toast.makeText(_context, _isConnected ? "Server connected." : "Can not connect to server",
                    Toast.LENGTH_LONG).show();
            try {
                if (_isConnected) {
                    btnConnnect.setText("DisConnect");
                    _printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            _socket.getOutputStream(), "utf-8")));
                } else {
                    btnConnnect.setText("Connect");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class SendCommandTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String cmd = strings[0];
            if (_isConnected && _socket != null && _printWriter != null) {
                    _printWriter.println(cmd);
                    _printWriter.flush();
            }

            return null;
        }
    }
}
