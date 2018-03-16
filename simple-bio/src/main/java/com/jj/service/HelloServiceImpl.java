package com.jj.service;

/**
 * Created by jomalone_jia on 2018/3/15.
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHi(String name) {
        return "hi, " + name;
    }
}
