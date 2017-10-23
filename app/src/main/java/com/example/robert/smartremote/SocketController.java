package com.example.robert.smartremote;

import android.os.AsyncTask;
import android.util.Log;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Robert on 10/22/2017.
 */

public class SocketController extends AsyncTask<String, Void, String> {

    private AsyncResponse _response;

    public SocketController(AsyncResponse response){
        this._response = response;
    }

    @Override
    protected String doInBackground(String... params) {

        if(params.length < 2){
            throw new IllegalArgumentException("Illegal commands");
        }

        String command = params[0];
        String param = params[1];

        try {
            initSocket(param);
            sendCommand(command);
        }catch (IOException ex){
            ex.printStackTrace();
            return Result.FAIL;
        }

        return Result.OK;
    }

    @Override
    protected void onPostExecute(String result){
        _response.processFinish(result);
    }

    private void sendCommand(String cmd) {
        if(_socket != null && _socket.isConnected()){

            DataOutputStream dos = null;
            try{
                //todo write bytes
                dos = new DataOutputStream(_socket.getOutputStream());
                dos.writeUTF(addEOF(cmd));
                dos.flush();
            } catch (IOException e){
                e.printStackTrace();
                Log.d("sendCommand", e.getMessage());
            } finally {
                close(dos);
            }
        }


    }

    private void initSocket(String ipAddr) throws IOException {
        initSocket(ipAddr, 11000);
    }

    private static Socket _socket;

    private void initSocket (String addr, int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(addr, port);

        _socket = null;
        DataOutputStream dos = null;
        try {
            _socket = new Socket();
            _socket.connect(address, 3000);

            /*dos = new DataOutputStream(_socket.getOutputStream());
            dos.writeUTF("Hello world<EOF>");
            dos.flush();*/

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            //close(dos);
            //close(_socket);
        }

    }

    private String addEOF(String str){
        if(str == null) return str;
        else return str + "<EOF>";
    }

    private void close(Closeable c){
        if(c == null) return;
        try{
            c.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public final class Result{
        public final static String OK = "OK";
        public final static String FAIL = "FAIL";
    }
}


