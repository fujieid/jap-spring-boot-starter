package com.fujieid.jap.spring.boot.starter.operations;

import com.fujieid.jap.core.result.JapResponse;
import com.fujieid.jap.simple.SimpleStrategy;
import com.fujieid.jap.spring.boot.common.util.JapUtil;
import com.fujieid.jap.spring.boot.japsimplespringbootstarter.autoconfigure.SimpleProperties;

public class SimpleOperations extends AbstractJapOperations{
    private SimpleStrategy simpleStrategy;
    private SimpleProperties simpleProperties;

    public SimpleOperations(SimpleStrategy simpleStrategy, SimpleProperties simpleProperties){
        this.simpleStrategy = simpleStrategy;
        this.simpleProperties = simpleProperties;
    }
    public JapResponse authenticate(){
        return super.authenticate(this.simpleStrategy,this.simpleProperties.getSimple());
    }
}
