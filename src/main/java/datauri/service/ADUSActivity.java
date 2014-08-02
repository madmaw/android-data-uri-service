package datauri.service;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;

/**
 * Created by chris on 2/08/14.
 */
public class ADUSActivity extends Activity {

    private ServerSocket serverSocket;
    private Thread serverThread;
    private int port;

    public ADUSActivity() {
        super();
    }

    private void launchBrowser(Intent receivedIntent) {
        String data = receivedIntent.getDataString();
        if( data != null ) {
            if( data.startsWith("data:") ) {
                data = data.substring(5);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://localhost:"+port+"/"+ data));
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        port = getResources().getInteger(R.integer.port);
        try {
            Log.w(getClass().getSimpleName(), "binding to port "+port+"...");
            serverSocket = new ServerSocket(port);
            Log.w(getClass().getSimpleName(), "...bound");


            // start up web-server thread
            serverThread = new Thread() {
                @Override
                public void run() {
                    boolean done = false;
                    launchBrowser(getIntent());
                    while ( !done && serverSocket != null ) {
                        try {
                            Log.w(ADUSActivity.class.getSimpleName(), "listening...");
                            Socket connectionSocket = serverSocket.accept();
                            Log.w(ADUSActivity.class.getSimpleName(), "...connected!");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                            String line = reader.readLine();
                            Log.w(ADUSActivity.class.getSimpleName(), "read "+line);
                            String[] parts = line.split(" ");
                            String method = parts[0];
                            String path = parts[1];
                            if( path.startsWith("/") ) {
                                path = path.substring(1);
                            }
                            Log.w(ADUSActivity.class.getSimpleName(), "method "+method);
                            Log.w(ADUSActivity.class.getSimpleName(), "path "+path);

                            int contentIndex = path.indexOf(',');
                            byte[] content;
                            String contentType;
                            String charset = null;
                            if( contentIndex >= 0 ) {
                                String metadata = path.substring(0, contentIndex);
                                String[] metadataParts = metadata.split(";");
                                contentType = metadataParts[0];
                                String encoding = null;
                                int index = 1;
                                while( metadataParts.length > index ) {
                                    String next = metadataParts[index];
                                    if( next.contains("charset=") ) {
                                        charset = next.substring(8);
                                    } else {
                                        encoding = next;
                                    }
                                    index++;
                                }
                                String contentString = path.substring(contentIndex+1);
                                if( "base64".equalsIgnoreCase(encoding) ) {
                                    content = Base64.decode(contentString, Base64.NO_WRAP);
                                } else {
                                    if( charset == null ) {
                                        charset = "US-ASCII";
                                    }
                                    content = URLDecoder.decode(contentString, charset).getBytes(charset);
                                }
                            } else {
                                charset = "US-ASCII";
                                contentType = "text/plain";
                                content = path.getBytes(charset);
                            }
                            if( charset != null ) {
                                contentType += ";charset="+charset;
                            }

                            Log.w(ADUSActivity.class.getSimpleName(), new String(content));


                            OutputStream outs = connectionSocket.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outs));
                            writer.write("HTTP/1.0 200 OK");
                            writer.newLine();
                            writer.write("Connection: close");
                            writer.newLine();
                            writer.write("Server: " + ADUSActivity.class.getSimpleName() + " " + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
                            writer.newLine();
                            writer.write("Content-Type: "+contentType);
                            writer.newLine();
                            // allow any bullshit through
                            writer.write("X-XSS-Protection: 0");
                            writer.newLine();
                            writer.newLine();
                            writer.flush();
                            if( method.equals("GET") ) {
                                done = true;
                                outs.write(content);
                                outs.flush();
                                connectionSocket.shutdownOutput();
                                connectionSocket.close();
                            }
                        } catch( Exception ex ) {
                            Log.e(ADUSActivity.class.getSimpleName(), "could not accept socket", ex);
                        }

                    }

                    finish();
                }
            };
            Log.w(ADUSActivity.class.getSimpleName(), "starting thread");
            serverThread.start();
            Log.w(ADUSActivity.class.getSimpleName(), "started thread");
        } catch( Exception ex ) {
            Log.e(ADUSActivity.class.getSimpleName(), "start up exited", ex);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(getClass().getSimpleName(), "destroying");
        if( serverSocket != null ) {
            try {
                serverSocket.close();
                serverSocket = null;
                serverThread.interrupt();
            } catch( Exception ex ) {
                Log.w(getClass().getSimpleName(), "unable to close socket", ex);
            }
        }
    }
}
