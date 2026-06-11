/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.quickchatt;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sasav
 */
public class MessageIT {

 
/**
 * Unit tests for Message class — Parts 2 & 3
 * Uses the exact test data from the assignment.
 */


    // ---------------------------------------------------------------
    // Test data (from assignment images)
    // ---------------------------------------------------------------
    // Msg 1: +27834557896 | "Did you get the cake?"      | Sent
    // Msg 2: +27838884567 | "Where are you? You are late! I have asked you to be on time." | Stored
    // Msg 3: +27834484567 | "Yohoooo, I am at your gate." | Disregard
    // Msg 4: 0838884567   | "It is dinner time !"         | Sent
    // Msg 5: +27838884567 | "Ok, I am leaving without you." | Stored

    @Before
    public void setUp() {
        // Reset static state before every test
        Message.resetAll();
    }

    @After
    public void tearDown() {
        Message.resetAll();
    }

    // Helper: load all 5 test messages
    private void loadAllTestMessages() {
        Message m1 = new Message(1, "+27834557896", "Did you get the cake?");
        m1.SentMessage(1);   // Sent

        Message m2 = new Message(2, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        m2.SentMessage(3);   // Stored

        Message m3 = new Message(3, "+27834484567", "Yohoooo, I am at your gate.");
        m3.SentMessage(2);   // Disregard

        Message m4 = new Message(4, "0838884567", "It is dinner time !");
        m4.SentMessage(1);   // Sent

        Message m5 = new Message(5, "+27838884567", "Ok, I am leaving without you.");
        m5.SentMessage(3);   // Stored
    }

    // ---------------------------------------------------------------
    // PART 2 TESTS
    // ---------------------------------------------------------------

    @Test
    public void testCheckMessageLength_Success() {
        Message m = new Message(1, "+27834557896", "Did you get the cake?");
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    public void testCheckMessageLength_Failure() {
        String longMsg = "a".repeat(260);
        Message m = new Message(1, "+27834557896", longMsg);
        String result = m.checkMessageLength();
        assertTrue(result.startsWith("Message exceeds 250 characters by"));
    }

    @Test
    public void testCheckRecipientCell_Success() {
        Message m = new Message(1, "+27834557896", "Did you get the cake?");
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCell_Failure() {
        // Message 4 has no international code
        Message m = new Message(4, "0838884567", "It is dinner time !");
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            m.checkRecipientCell()
        );
    }

    @Test
    public void testCheckMessageID() {
        Message m = new Message(1, "+27834557896", "Did you get the cake?");
        assertTrue(m.checkMessageID());
        System.out.println("Message ID generated: " + m.getMessageID());
    }

    @Test
    public void testCreateMessageHash_Format() {
        Message m = new Message(1, "+27834557896", "Did you get the cake?");
        String hash = m.createMessageHash();
        assertEquals(hash.toUpperCase(), hash);
        assertTrue(hash.endsWith("DIDCAKE"));
    }

    @Test
    public void testSentMessage_Send() {
        Message m = new Message(1, "+27834557896", "Did you get the cake?");
        assertEquals("Message successfully sent.", m.SentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard() {
        Message m = new Message(3, "+27834484567", "Yohoooo, I am at your gate.");
        assertEquals("Press 0 to delete the message.", m.SentMessage(2));
    }

    @Test
    public void testSentMessage_Store() {
        Message m = new Message(2, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        assertEquals("Message successfully stored.", m.SentMessage(3));
    }

    // ---------------------------------------------------------------
    // PART 3 TESTS
    // ---------------------------------------------------------------

    /** Sent Messages array — should contain messages 1 and 4 */
    @Test
    public void testSentMessagesArrayPopulated() {
        loadAllTestMessages();
        assertTrue("Array should contain msg1",
                Message.getSentMessages().contains("Did you get the cake?"));
        assertTrue("Array should contain msg4",
                Message.getSentMessages().contains("It is dinner time !"));
        assertEquals(2, Message.getSentMessages().size());
    }

    /** Display longest message — should be message 2 */
    @Test
    public void testLongestStoredMessage() {
        loadAllTestMessages();
        String result = Message.longestStoredMessage();
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
    }

    /** Search by recipient +27838884567 — should return msgs 2 and 5 */
    @Test
    public void testSearchByRecipient() {
        loadAllTestMessages();
        String result = Message.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    /** Search by message ID for message 4 (0838884567 recipient) */
    @Test
    public void testSearchByMessageID() {
        loadAllTestMessages();
        // Find the generated ID for message 4
        String id4 = Message.getSentObjects().get(1).getMessageID(); // msg4 is 2nd sent
        String result = Message.searchByMessageID(id4);
        assertTrue(result.contains("It is dinner time !"));
    }

    /** Delete stored message using hash of message 2 */
    @Test
    public void testDeleteByHash() {
        loadAllTestMessages();
        // Message 2 is stored — get its hash
        String hash2 = Message.getStoredObjects().get(0).getMessageHash();
        String result = Message.deleteByHash(hash2);
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("successfully deleted"));
        // Stored list should now have 1 item (only msg5)
        assertEquals(1, Message.getStoredObjects().size());
    }

    /** Display report should include hash, recipient, and message */
    @Test
    public void testDisplayReport() {
        loadAllTestMessages();
        String report = Message.displayReport();
        assertTrue(report.contains("SENT MESSAGES"));
        assertTrue(report.contains("STORED MESSAGES"));
        assertTrue(report.contains("DISREGARDED MESSAGES"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("+27838884567"));
    }

    @Test
    public void testReturnTotalMessages() {
        loadAllTestMessages();
        // Messages 1 and 4 were sent
        assertEquals(2, Message.returnTotalMessages());
    }
}

