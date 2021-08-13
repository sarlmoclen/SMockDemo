package com.sarlmoclen.smock;

import android.os.Environment;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class SMockCore {

    public void mock(){
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .name("ckm")
                .make();
        try {
            dynamicType.saveIn(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
