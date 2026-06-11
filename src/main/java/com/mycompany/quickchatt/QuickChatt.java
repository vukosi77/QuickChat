/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.quickchatt;

import java.util.Scanner;

/**
 * 
 * @author sasav
 */
public class QuickChatt {
     public static void main(String[] args) {
        System.out.println("\nWelcome to QuickChat.");
    }

    public static void startmessaging(Scanner input, Registration_and_login user) {

        System.out.println("Hello, " + user.getFirstName() + " " + user.getLastName() + "!");

        System.out.print("\nHow many messages would you like to send? ");
        int numMessages = 0;
        try {
            numMessages = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Defaulting to 0.");
        }

        int     messageCounter = 0;
        boolean running        = true;

        while (running) {
            System.out.println("\n========================================");
            System.out.println("         QUICKCHAT MENU");
            System.out.println("========================================");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Stored Messages");
            System.out.println("4) Quit");
            System.out.print("Choose an option: ");

            String choice = input.nextLine().trim();

            switch (choice) {

                // ── OPTION 1: SEND MESSAGES ──────────────────────────
                case "1":
                    if (messageCounter >= numMessages) {
                        System.out.println("You have already sent all " + numMessages + " message(s).");
                        break;
                    }
                    while (messageCounter < numMessages) {
                        System.out.println("\n--- Message " + (messageCounter + 1) + " of " + numMessages + " ---");

                        System.out.print("Enter recipient cell number (with international code e.g. +27...): ");
                        String recipientCell = input.nextLine().trim();

                        String messageText  = "";
                        boolean validMsg    = false;
                        while (!validMsg) {
                            System.out.print("Enter message (max 250 characters): ");
                            messageText = input.nextLine();
                            if (messageText.length() > 250) {
                                int over = messageText.length() - 250;
                                System.out.println("Please enter a message of less than 250 characters.");
                                System.out.println("(Exceeds limit by " + over + " characters.)");
                            } else {
                                validMsg = true;
                            }
                        }

                        messageCounter++;
                        Message msg = new Message(messageCounter, recipientCell, messageText);

                        System.out.println(msg.checkRecipientCell());
                        System.out.println(msg.checkMessageLength());

                        if (msg.checkMessageID()) {
                            System.out.println("Message ID generated: " + msg.getMessageID());
                        } else {
                            System.out.println("Warning: Message ID issue detected.");
                        }
                        System.out.println("Message Hash: " + msg.getMessageHash());

                        System.out.println("\nWhat would you like to do with this message?");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choose: ");

                        int sendChoice = 2;
                        try {
                            sendChoice = Integer.parseInt(input.nextLine().trim());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid choice. Message disregarded.");
                        }

                        String sendResult = msg.SentMessage(sendChoice);
                        System.out.println(sendResult);

                        if (sendChoice == 2) {
                            System.out.print("Press 0 to confirm deletion, or any key to keep: ");
                            String del = input.nextLine().trim();
                            if (del.equals("0")) {
                                System.out.println("Message deleted.");
                                messageCounter--;
                            }
                        }

                        if (sendChoice == 1 || sendChoice == 3) {
                            System.out.println("\n--- Message Details ---");
                            System.out.println("Message ID   : " + msg.getMessageID());
                            System.out.println("Message Hash : " + msg.getMessageHash());
                            System.out.println("Recipient    : " + msg.getRecipientCell());
                            System.out.println("Message      : " + msg.getMessageText());
                        }
                    }
                    System.out.println("\nTotal Messages sent: " + Message.returnTotalMessages());
                    break;

                // ── OPTION 2: RECENTLY SENT  ────────────
                case "2":
                    
               System.out.println("\n--- RECENTLY SENT MESSAGES ---");
               System.out.println(Message.printMessages());
               break;

                // ── OPTION 3: STORED MESSAGES MENU (Part 3) ──────────
                case "3":
                    storedMessagesMenu(input, user);
                    break;

                // ── OPTION 4: QUIT ────────────────────────────────────
                case "4":
                    running = false;
                    System.out.println("Thank you"  + user.getFirstName() + user.getLastName() + " for using QuickChat. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }
        }
    }

    // ================================================================
    // PART 3 — Stored Messages Sub-Menu
    // ================================================================
    private static void storedMessagesMenu(Scanner input, Registration_and_login user) {
        boolean back = false;
        while (!back) {
            System.out.println("\n========================================");
            System.out.println("      STORED MESSAGES MENU");
            System.out.println("========================================");
            System.out.println("a) Display sender & recipient of stored messages");
            System.out.println("b) Display longest stored message");
            System.out.println("c) Search by Message ID");
            System.out.println("d) Search messages by recipient");
            System.out.println("e) Delete a message using message hash");
            System.out.println("f) Display full report");
            System.out.println("0) Back to main menu");
            System.out.print("Choose: ");

            String sub = input.nextLine().trim().toLowerCase();

            switch (sub) {
                case "a":
                    System.out.println(Message.displayStoredSenderRecipient(
                            user.getFirstName() + " " + user.getLastName()));
                    break;
                case "b":
                    System.out.println(Message.longestStoredMessage());
                    break;
                case "c":
                    System.out.print("Enter Message ID to search: ");
                    String searchID = input.nextLine().trim();
                    System.out.println(Message.searchByMessageID(searchID));
                    break;
                case "d":
                    System.out.print("Enter recipient cell number to search: ");
                    String recipient = input.nextLine().trim();
                    System.out.println(Message.searchByRecipient(recipient));
                    break;
                case "e":
                    System.out.print("Enter message hash to delete: ");
                    String hash = input.nextLine().trim();
                    System.out.println(Message.deleteByHash(hash));
                    break;
                case "f":
                    System.out.println(Message.displayReport());
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}



   