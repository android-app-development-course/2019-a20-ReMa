package com.iwktd.rema;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class LruCacheTest extends TestCase {

    @Test
    public void testInsertNewRecord() {
        LruCache p = new LruCache();
        p.insertNewRecord(0);
        p.insertNewRecord(1);

        ArrayList<Integer> q = new ArrayList<Integer>();
        q.add(0);
        q.add(1);

        Assert.assertEquals(q, p.getAllRecord());
    }

    @Test
    public void testInsertNewRecordRepeat() {
        LruCache p = new LruCache();
        p.insertNewRecord(0);
        p.insertNewRecord(1);
        p.insertNewRecord(1); // repeat

        ArrayList<Integer> q = new ArrayList<Integer>();
        q.add(0);
        q.add(1);

        Assert.assertEquals(q, p.getAllRecord());
    }

    @Test
    public void testInsertNewRecordFullAndRepeat() {
        LruCache p = new LruCache();
        p.insertNewRecord(0);  //popped
        p.insertNewRecord(1);
        p.insertNewRecord(2);
        p.insertNewRecord(3);  // reserved.
        p.insertNewRecord(4);
        p.insertNewRecord(5);
        p.insertNewRecord(6);
        p.insertNewRecord(7);
        p.insertNewRecord(8);
        p.insertNewRecord(9);
        p.insertNewRecord(10); // should be popped.
        p.insertNewRecord(11);
        p.insertNewRecord(12);
        p.insertNewRecord(13);
        p.insertNewRecord(14);
        p.insertNewRecord(15);
        p.insertNewRecord(16);
        p.insertNewRecord(17);
        p.insertNewRecord(18);
        p.insertNewRecord(10);  // full and repeat

        ArrayList<Integer> q = new ArrayList<Integer>();
        q.add(3);
        q.add(4);
        q.add(5);
        q.add(6);
        q.add(7);
        q.add(8);
        q.add(9);
        //q.add(10);
        q.add(11);
        q.add(12);
        q.add(13);
        q.add(14);
        q.add(15);
        q.add(16);
        q.add(17);
        q.add(18);
        q.add(10);

        Assert.assertEquals(q, p.getAllRecord());
    }

}