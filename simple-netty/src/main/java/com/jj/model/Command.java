package com.jj.model;

import java.io.Serializable;

/**
 * Created by jomalone_jia on 2018/3/16.
 */
public class Command implements Serializable {

    private static final long serialVersionUID = 7590999461767050471L;

    private String actionName;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
