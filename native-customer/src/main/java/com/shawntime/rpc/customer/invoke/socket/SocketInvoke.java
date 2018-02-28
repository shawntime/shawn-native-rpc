package com.shawntime.rpc.customer.invoke.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.shawntime.rpc.core.zookeeper.ZookeeperServerConfig;
import com.shawntime.rpc.customer.invoke.Invoke;

/**
 * Created by shma on 2018/2/12.
 */
public class SocketInvoke implements Invoke {

    @Override
    public String invoke(String requestBody, ZookeeperServerConfig serverConfig) throws IOException {
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            socket = new Socket(serverConfig.getIp(), serverConfig.getPort());
            pw = new PrintWriter(socket.getOutputStream());
            pw.write(requestBody);
            pw.flush();
            socket.shutdownOutput();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String data;
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }
            return sb.toString();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (br != null) {
                br.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }
}
