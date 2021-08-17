package com.sarlmoclen.smockdemo;

import com.sarlmoclen.smock.SMock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Test
    public void testAdd(){
        Utils utils = Mockito.mock(Utils.class);
        utils.add(1,2);
        utils.add(1,2);
        Mockito.verify(utils, new Times(2)).add(1,2);
    }

    @Test
    public void testAdd_mockito(){
        Utils utils = Mockito.mock(Utils.class);
        utils.add(1,3);
        Mockito.when(1).thenReturn(6);
        Assert.assertEquals(6,utils.add(1,3));
    }

    @Test
    public void testAdd_smock(){
        Utils utils = SMock.mock(Utils.class);
        utils.add(1,2);
        utils.add(1,3);
        SMock.when(1).thenReturn(6);
        Assert.assertEquals(6,utils.add(1,3));
    }

}