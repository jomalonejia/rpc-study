package com.jj.server;

import java.io.IOException;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public interface Server {
    void stop();
    void start() throws IOException;
    void registry(Class interfaceClass,Class impl);
}
