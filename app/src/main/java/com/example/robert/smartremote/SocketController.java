package com.example.robert.smartremote;

import android.os.AsyncTask;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Robert on 10/22/2017.
 */

public class SocketController extends AsyncTask<String, Void, Void> {
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Void doInBackground(String... params) {

        //TODO params command:
        //first param: command
        //sencond param: data
        String addr = params[0];
        InetSocketAddress address = new InetSocketAddress(addr, 11000);

        Socket socket = null;
        DataOutputStream dos = null;
        try {
            //TODO according the command type ,change the right action
            socket = new Socket();
            socket.connect(address, 5000);

            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("Hello world<EOF>");
            dos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(dos);
            close(socket);
        }

        return null;
    }

    private void close(Closeable c){
        if(c == null) return;
        try{
            c.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
