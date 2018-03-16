package com.jj.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public class RpcClient<T> {
    public static <T> T getProxy(final Class<T> interfaceClass, final InetSocketAddress address) {
        return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = null;
                        ObjectInputStream in = null;
                        ObjectOutputStream out = null;

                        try {
                            socket = new Socket();
                            socket.connect(address);
                            out = new ObjectOutputStream(socket.getOutputStream());
                            out.writeUTF(interfaceClass.getName());
                            out.writeUTF(method.getName());
                            out.writeObject(method.getParameterTypes());
                            out.writeObject(args);

                            in = new ObjectInputStream(socket.getInputStream());
                            return in.readObject();
                        } finally {
                            if (socket != null) socket.close();
                            if (in != null) in.close();
                            if (out != null) out.close();
                        }
                    }
                });
    }
}
