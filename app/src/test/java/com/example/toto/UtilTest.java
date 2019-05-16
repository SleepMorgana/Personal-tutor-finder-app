package com.example.toto;

import android.util.Pair;

import com.example.toto.users.student.MainActivityStudent;
import com.example.toto.utils.Util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static org.junit.Assert.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

//@RunWith(RobolectricTestRunner.class)
public class UtilTest {

    /**
     * Transform a list of dates on a list of 2-tuple(Date in day/month/year, Time in HH:MM:SS)
     * param dates_list list of dates
     * @return Corresponding list of 2-tuple(Date in day/month/year, Time in HH:MM:SS)
     * Testing the method would require mocking of the android.util.Pair class due to its implementation
     * it JUnit is unable to crate Pair instances
     */
//    @Test
//    public void transformListOfDatesTest() throws ParseException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        //Test cases
//        //List<Date> testCases = new ArrayList<>()
//        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        Date[] testCases = {
//                format.parse("03-09-2019 10:05:00"),
//                format.parse("12-05-2019 10:05:00"),
//                format.parse("01-02-1994 10:05:00")
//        };
//        List<Pair<String,String>> expectedCases = new ArrayList<>();
//        expectedCases.add(Pair.create("03-09-2019","10:05:00"));
//        expectedCases.add(Pair.create("03-09-2019","10:05:00"));
//        expectedCases.add(Pair.create("01-02-1994","10:05:00"));
//
//        Class targetClass = null;
//        try {
//            targetClass = Class.forName("com.example.toto.utils.Util");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            fail();
//        }
//
//        Method method = targetClass.getDeclaredMethod("transformListOfDates", new Class[]{List.class});
//
//        method.setAccessible(true);
//
//        Object actual = method.invoke(null, new Object[]{Arrays.asList(testCases)});
//
//        if (actual == null){
//            fail("Unexpected error, null return value to transformListOfDates");
//            return;
//        }
//
//        List<Pair<String,String>> actualResults = (List<Pair<String,String>>) actual;
//
//        for (int i =0; i< expectedCases.size(); i++) {
//            //test pair, first and second element
////            AssertTrue(actualResults.get(i)==null)
////                throw new AssertionError("Unexpected null value at pos: "+i);
//
//
//            assertEquals(expectedCases.get(i).first, actualResults.get(i).first);
//                //throw new AssertionError("Unexpected error invalid tuple element.");
//            assertEquals((expectedCases.get(i).second), actualResults.get(i).second);
//                //throw new AssertionError("Unexpected error invalid tuple element.");
//        }
//    }

    //testing the getPositionFromDataTest method and assuring that it returns the correct index of
    //the an element in the entryset according to the order
    @Test
    public void getPositionFromDataTest(){
        List<String> testCases = new ArrayList<>();
        testCases.add("Ancient History");
        testCases.add("Computing");
        testCases.add("Math");
        testCases.add("Latin");

        int[] expectedResults = {0,1,2,3};
        for (int i=0; i< expectedResults.length; i++){
            int actual = Util.getPositionFromData(""+testCases.get(i).charAt(0),testCases);
            assertEquals(actual,(expectedResults[i]));
        }
    }

    //Testing getCustomAlphabetSetTest, assuring that it returns the expected characters
    @Test
    public void getCustomAlphabetSetTest(){
        Set<String> testCases = new HashSet<String>();
        testCases.add("Ancient History");
        testCases.add("Computing");
        testCases.add("Math");
        testCases.add("Latin");

        String[] expectedResults = {"A","C","L","M"};

        String[] actualResults = Util.getCustomAlphabetSet(testCases);
        for (int i=0; i< expectedResults.length; i++){
            assertEquals(actualResults[i],(expectedResults[i]));
        }
    }

    //Testing getCustomAlphabetListTest, assuring that it returns the expected characters
    @Test
    public void getCustomAlphabetListTest(){
        List<String> testCases = new ArrayList<>();
        testCases.add("Ancient History");
        testCases.add("Computing");
        testCases.add("Math");
        testCases.add("Latin");

        String[] expectedResults = {"A","C","L","M"};

        String[] actualResults = Util.getCustomAlphabetList(testCases);
        for (int i=0; i< expectedResults.length; i++){
            assertEquals(actualResults[i],(expectedResults[i]));
        }
    }
}