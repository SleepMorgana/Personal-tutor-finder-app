package com.example.toto;

import com.example.toto.interfaces.DatabaseHelper;
import com.example.toto.sessions.Session;
import com.example.toto.sessions.Status;
import com.example.toto.subjects.Subject;
import com.example.toto.users.Role;
import com.example.toto.users.User;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

public class UserPackageTest {
    //private Context context = ApplicationProvider.getApplicationContext();

    //Testing the user.flatten2 method, assuring that the complex Map nesting is reduced to a single map
    @Test
    public void flattenTest(){
        Map<String, Map<String,Map<String,Object>>> mock = new HashMap<>();
        Map<String,Map<String,Object>> mockSubjects = new HashMap<>();

        Map<String,Object> spanish = new HashMap<>();
        spanish.put("id","subject-id1");
        spanish.put("Name","spanish");
        mockSubjects.put("subject-id1",spanish);

        Map<String,Object> french = new HashMap<>();
        french.put("id","subject-id2");
        french.put("Name","french");
        mockSubjects.put("subject-id2",french);

        mock.put("Subjects",mockSubjects);

        Map<String, Subject> expected = new HashMap<>();
        expected.put("subject-id1",new Subject("subject-id1","spanish"));
        expected.put("subject-id2",new Subject("subject-id2","french"));

        User user = new User("foo","foo@gmail.com", Role.STUDENT, "id-1", Status.ACCEPTED);

        Map<String, Subject> actual = user.flatten2(mock.get("Subjects"));
        for(Map.Entry<String,Subject> entry : expected.entrySet()) {
            assertTrue(actual.containsKey(entry.getKey()));
        }

        //test with null arg
        mock.remove("Subjects");
        mock.put("Subjects",null);
        expected = new HashMap<>();
        actual = user.flatten2(mock.get("Subjects"));
        assertEquals(String.valueOf(expected),String.valueOf(actual));
    }

    //Testing the adding session to user instance method, assuring that only one instance of a session
    //is added to the list. Also checking that the sessions and SessionIds are in sync
    @Test
    public void addSessionToUser(){
        User user = new User("foo","foo@gmail.com", Role.STUDENT, "id-1", Status.ACCEPTED);
        //String subject,String sender,String target, Status status, String id
        Session session = new Session("Latin 101", "id-1", "id-2", Status.PENDING, "session-1");
        user.addSession(session);
        user.addSession(session);

        assertEquals(user.getSessionIds().get(0).toString(),session.getId());
        assertEquals(user.getSessions().get(0),session);
        assertTrue(user.getSessions().size() == 1);
        assertTrue(user.getSessionIds().size() == 1);
    }
    
    /*
        Testing getNUpcomingSessionDates would require to setup the systems to fixed date,
        due to the method using the current day as reference
    */
//    @Test
//    public void getNUpcomingSessionDatesTest(){
//        User user = new User("foo","foo@gmail.com", Role.STUDENT, "id-1", Status.ACCEPTED);
//        Session testSession = new Session("Foobar", "id-2", "id-1", Status.ACCEPTED, "session-8");
//        String[] testCases = {
//                "1558090464000",
//                "1558176864000",
//                "1558004064000"
//        };
//
//        List<Date> expectedCases = new ArrayList<>();
//
//        testSession.setDates(Arrays.<Object>asList(testCases));
//        user.addSession(testSession);
//
//        user.getNUpcomingSessionDates(2);
//
//
//    }
}
