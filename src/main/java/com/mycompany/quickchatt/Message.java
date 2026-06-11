/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quickchatt;

import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author sasav
 */
public class Message {
    
    private String messageID;
    private int    messageNumber;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String sendStatus;   // "Sent", "Stored", "Disregarded"

    // ---------------------------------------------------------------
    // Part 3 — five static arrays (no hard-coding)
    // ---------------------------------------------------------------
    private static ArrayList<String>  sentMessages        = new ArrayList<>();
    private static ArrayList<String>  disregardedMessages = new ArrayList<>();
    private static ArrayList<String>  storedMessages      = new ArrayList<>();
    private static ArrayList<String>  messageHashes       = new ArrayList<>();
    private static ArrayList<String>  messageIDs          = new ArrayList<>();

    // Full Message objects for report / search / delete features
    private static ArrayList<Message> sentObjects         = new ArrayList<>();
    private static ArrayList<Message> storedObjects       = new ArrayList<>();
    private static ArrayList<Message> disregardedObjects  = new ArrayList<>();

    private static int totalMessagesSent = 0;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------
    public Message(int messageNumber, String recipientCell, String messageText) {
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageText   = messageText;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        long id = (long)(rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        String regex = "^\\+\\d{1,3}\\d{1,10}$";
        if (recipientCell != null && recipientCell.matches(regex)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText == null) return "Please enter a message of less than 250 characters.";
        if (messageText.length() <= 250) return "Message ready to send.";
        int over = messageText.length() - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    public String createMessageHash() {
        String idPrefix  = messageID.substring(0, 2);
        String[] words   = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        messageHash = (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    // SentMessage — populates arrays based on user choice
    public String SentMessage(int choice) {
        messageIDs.add(messageID);
        messageHashes.add(messageHash);
        switch (choice) {
            case 1:
                sendStatus = "Sent";
                sentMessages.add(messageText);
                sentObjects.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                sendStatus = "Disregarded";
                disregardedMessages.add(messageText);
                disregardedObjects.add(this);
                return "Press 0 to delete the message.";
            case 3:
                sendStatus = "Stored";
                storedMessages.add(messageText);
                storedObjects.add(this);
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    public static String printMessages() {
        if (sentObjects.isEmpty()) return "No messages have been sent yet.";
        StringBuilder sb = new StringBuilder();
        sb.append("\n===== SENT MESSAGES =====\n");
        for (Message m : sentObjects) {
            sb.append("Message ID   : ").append(m.messageID).append("\n");
            sb.append("Message Hash : ").append(m.messageHash).append("\n");
            sb.append("Recipient    : ").append(m.recipientCell).append("\n");
            sb.append("Message      : ").append(m.messageText).append("\n");
            sb.append("-------------------------\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // ==============================================================
    // PART 3 — Stored Messages Menu Methods
    // ==============================================================

    /** 2a. Display sender and recipient of all stored messages */
    public static String displayStoredSenderRecipient(String senderName) {
        if (storedObjects.isEmpty()) return "No stored messages.";
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Stored Messages: Sender & Recipient ---\n");
        for (Message m : storedObjects) {
            sb.append("Sender    : ").append(senderName).append("\n");
            sb.append("Recipient : ").append(m.recipientCell).append("\n");
            sb.append("---\n");
        }
        return sb.toString();
    }

    /** 2b. Display the longest stored message */
    public static String longestStoredMessage() {
        if (storedObjects.isEmpty()) return "No stored messages.";
        Message longest = storedObjects.get(0);
        for (Message m : storedObjects) {
            if (m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }
        return "Longest stored message: \"" + longest.messageText + "\"";
    }

    /** 2c. Search by message ID — return recipient and message */
    public static String searchByMessageID(String searchID) {
        for (Message m : sentObjects) {
            if (m.messageID.equals(searchID)) {
                return "Recipient : " + m.recipientCell + "\nMessage   : " + m.messageText;
            }
        }
        for (Message m : storedObjects) {
            if (m.messageID.equals(searchID)) {
                return "Recipient : " + m.recipientCell + "\nMessage   : " + m.messageText;
            }
        }
        return "No message found with ID: " + searchID;
    }

    /** 2d. Search all messages (sent + stored) for a particular recipient */
    public static String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentObjects) {
            if (m.recipientCell.equalsIgnoreCase(recipient)) {
                sb.append("\"").append(m.messageText).append("\"\n");
            }
        }
        for (Message m : storedObjects) {
            if (m.recipientCell.equalsIgnoreCase(recipient)) {
                sb.append("\"").append(m.messageText).append("\"\n");
            }
        }
        if (sb.length() == 0) return "No messages found for recipient: " + recipient;
        return "Messages for " + recipient + ":\n" + sb.toString();
    }

    /** 2e. Delete a stored message using its message hash */
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedObjects.size(); i++) {
            Message m = storedObjects.get(i);
            if (m.messageHash.equalsIgnoreCase(hash)) {
                String text = m.messageText;
                storedObjects.remove(i);
                storedMessages.remove(i);
                messageHashes.remove(hash.toUpperCase());
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "No stored message found with hash: " + hash;
    }

    /** 2f. Full report of all messages */
    public static String displayReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== MESSAGE REPORT ==========\n");
        sb.append("\n-- SENT MESSAGES (").append(sentObjects.size()).append(") --\n");
        for (Message m : sentObjects) {
            sb.append("Hash      : ").append(m.messageHash).append("\n");
            sb.append("Recipient : ").append(m.recipientCell).append("\n");
            sb.append("Message   : ").append(m.messageText).append("\n\n");
        }
        sb.append("-- STORED MESSAGES (").append(storedObjects.size()).append(") --\n");
        for (Message m : storedObjects) {
            sb.append("Hash      : ").append(m.messageHash).append("\n");
            sb.append("Recipient : ").append(m.recipientCell).append("\n");
            sb.append("Message   : ").append(m.messageText).append("\n\n");
        }
        sb.append("-- DISREGARDED MESSAGES (").append(disregardedObjects.size()).append(") --\n");
        for (Message m : disregardedObjects) {
            sb.append("Hash      : ").append(m.messageHash).append("\n");
            sb.append("Recipient : ").append(m.recipientCell).append("\n");
            sb.append("Message   : ").append(m.messageText).append("\n\n");
        }
        sb.append("=====================================\n");
        sb.append("Total messages sent: ").append(totalMessagesSent).append("\n");
        return sb.toString();
    }

    // ---------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------
    public String getMessageID()    { return messageID; }// gives meesage
    public String getMessageHash()  { return messageHash; }
    public String getRecipientCell(){ return recipientCell; }
    public String getMessageText()  { return messageText; }
    public int    getMessageNumber(){ return messageNumber; }// gives message number

    // Static array getters (for unit tests)
    public static ArrayList<String>  getSentMessages()        { return sentMessages; }
    public static ArrayList<String>  getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<String>  getStoredMessages()      { return storedMessages; }
    public static ArrayList<String>  getMessageHashes()       { return messageHashes; }
    public static ArrayList<String>  getMessageIDs()          { return messageIDs; }
    public static ArrayList<Message> getSentObjects()         { return sentObjects; }
    public static ArrayList<Message> getStoredObjects()       { return storedObjects; }

    /** Reset static state between unit tests */
    public static void resetAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        sentObjects.clear();
        storedObjects.clear();
        disregardedObjects.clear();
        totalMessagesSent = 0;
    }
}

    

    
    
    