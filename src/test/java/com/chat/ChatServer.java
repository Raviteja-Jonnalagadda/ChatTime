package com.chat;


import javax.websocket.OnClose; // Import for handling close events
import javax.websocket.OnMessage; // Import for message handling
import javax.websocket.OnOpen; // Import for open connection
import javax.websocket.Session; // WebSocket session
import javax.websocket.server.ServerEndpoint; // Annotation to define endpoint
import java.util.Set; // To store active sessions
import java.util.concurrent.CopyOnWriteArraySet; // Thread-safe set for sessions

@ServerEndpoint("/chat") 
public class ChatServer {

    // Thread-safe set to store all active sessions 
    private static Set<Session> sessions = new CopyOnWriteArraySet<>();

    // Called when a new WebSocket connection is established
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session); // Add new session to the set
        System.out.println("New Connection Opened. Total connections: " + sessions.size()); // Debug: connection count
        broadcastMessage("A new user has joined. Total users: " + sessions.size()); // Notify all users
    }

    // Called when a message is received from a client
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message: " + message); // Debug: message received
        broadcastMessage(message); // Broadcast message to all clients
    }

    // Called when a connection is closed
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session); // Remove session from set
        System.out.println("Connection Closed. Total connections: " + sessions.size()); // Debug: connection count
        broadcastMessage("A user has left. Total users: " + sessions.size()); // Notify remaining users
    }

    // Helper method to broadcast a message to all connected clients
    private void broadcastMessage(String message) {
        for (Session session : sessions) {
            try {
                System.out.println("Sending message to session ID " + session.getId()); // Debug: message sending
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                System.out.println("Error sending message: " + e.getMessage()); // Debug: error
            }
        }
    }
}