package com.shawntime.rpc.provider.service.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.shawntime.rpc.core.protocol.ParameterEntity;
import com.shawntime.rpc.core.protocol.RequestProtocol;
import com.shawntime.rpc.core.protocol.ResponseProtocol;
import com.shawntime.rpc.core.serialize.Formatter;
import com.shawntime.rpc.core.serialize.Parse;
import com.shawntime.rpc.core.util.ApplicationContextUtil;
import com.shawntime.rpc.core.util.ClassUtil;
import com.shawntime.rpc.core.util.JsonHelper;
import com.shawntime.rpc.provider.ServiceConfigManager;
import com.shawntime.rpc.provider.service.AbstractRpcProvider;
import org.springframework.stereotype.Component;

/**
 * Created by shma on 2018/2/12.
 */
public class SocketRpcProvider extends AbstractRpcProvider {

    private static final ExecutorService exec = Executors.newCachedThreadPool();

    @Override
    public void start() {
        int port = ServiceConfigManager.getPort();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            logger.warn("服务的创建成功,监听" + port + "中...");
            while (true) {
                final Socket socket = serverSocket.accept();
                logger.warn("监听到客户端连接,Socket创建成功...");
                exec.submit(new HandleThread(socket, formatter, parse));
            }
        } catch (IOException e) {
            logger.error("socket连接异常，服务退出....", e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class HandleThread implements Runnable {

        private Socket socket;

        private Formatter formatter;

        private Parse parse;

        public HandleThread(Socket socket, Formatter formatter, Parse parse) {
            this.socket = socket;
            this.formatter = formatter;
            this.parse = parse;
        }

        public void run() {
            BufferedReader br = null;
            PrintWriter pw = null;
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream());
                StringBuffer sb = new StringBuffer();
                String requestBody;
                while ((requestBody = br.readLine()) != null) {
                    sb.append(requestBody);
                }
                socket.shutdownInput();//关闭输入流
                RequestProtocol request = parse.requestParse(sb.toString());
                Class<?> clazz = ClassUtil.forNameWithPrimitive(request.getClazz());
                Object object = getRealObject(clazz);
                Method method = getMethod(request, clazz);
                Object[] newArgs = getArgs(request);
                Object data;
                ResponseProtocol response = new ResponseProtocol();
                if (request.getReturnClazz().equalsIgnoreCase("void")) {
                    method.invoke(object, newArgs);
                    response.setClazz(Void.class.getSimpleName());
                    response.setValue(null);
                } else {
                    data = method.invoke(object, newArgs);
                    response.setClazz(data.getClass().getName());
                    response.setValue(data);
                }
                String responseBody = formatter.responseBodyFormatter(response);
                pw.write(responseBody);
                pw.flush();
            } catch (Exception e) {
                logger.error("服务端处理异常:[msg:{}]", e.getMessage(), e);
                ResponseProtocol response = new ResponseProtocol();
                response.setValue("error");
                response.setClazz(String.class.getName());
                String responseBody = formatter.responseBodyFormatter(response);
                pw.write(responseBody);
                pw.flush();
            } finally {
                if (pw != null) {
                    pw.close();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Method getMethod(RequestProtocol request, Class methodClazz)
            throws NoSuchMethodException, ClassNotFoundException {
        ParameterEntity[] args = request.getArgs();
        Class[] clazz = null;
        if (args != null) {
            clazz = new Class[args.length];
            for (int i=0; i<args.length; ++i) {
                clazz[i] = ClassUtil.forNameWithPrimitive(args[i].getClazz());
            }
        }
        return methodClazz.getMethod(request.getMethodName(), clazz);
    }

    private Object[] getArgs(RequestProtocol request) throws ClassNotFoundException {
        ParameterEntity[] parameterEntitys = request.getArgs();
        Object[] args = null;
        if (parameterEntitys != null && parameterEntitys.length > 0) {
            args = new Object[parameterEntitys.length];
            for (int i=0; i<parameterEntitys.length; ++i) {
                Object value = parameterEntitys[i].getValue();
                args[i] = JsonHelper.deSerialize(value.toString(),
                        ClassUtil.forNameWithPrimitive(parameterEntitys[i].getClazz()));
            }
        }
        return args;
    }

    private Object getRealObject(Class clazz) {
        return ApplicationContextUtil.getBean(clazz);
    }
}
