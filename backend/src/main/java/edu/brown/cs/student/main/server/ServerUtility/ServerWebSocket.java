package edu.brown.cs.student.main.server.ServerUtility;

import edu.brown.cs.student.main.server.Exceptions.ShuttleDataException;
import edu.brown.cs.student.main.server.TransLocUtility.TransLocAPISource;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerWebSocket extends WebSocketServer {

    private static TransLocAPISource transLoc;
    private final Set<WebSocket> clients;
    private boolean fetchData = false;
    private final ScheduledExecutorService scheduler;
    private int messageCount = 0;

    public ServerWebSocket(int port) {
        super(new InetSocketAddress(port));
        this.clients = new HashSet<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        transLoc = new TransLocAPISource();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        clients.add(webSocket);
        System.out.println("WebSocket opened: " + webSocket.getRemoteSocketAddress());
        if (!fetchData) {
            System.out.println("Starting sending vehicle data.");
            startSendingLoop();
        }
        fetchData = true;
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        clients.remove(webSocket);
        System.out.println("WebSocket closed: " + webSocket.getRemoteSocketAddress());
        if (clients.isEmpty()) {
            fetchData = false;
            System.out.println("Stopped sending vehicle data.");
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        System.out.println("Received message from " + webSocket.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.err.println("WebSocket error: " + e.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started.");
    }

    private void startSendingLoop() {
        scheduler.scheduleAtFixedRate(this::sendMessagesToAllClients, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void sendMessagesToAllClients() {
        if (fetchData) {
            try {
                String message = transLoc.mapToJson(transLoc.parseVehicleData(transLoc.getVehicleData()));
                sendMessageToAllClients(message);
                messageCount++;
            } catch (ShuttleDataException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToAllClients(String message) {
        for (WebSocket client : clients) {
            client.send(message);
        }
    }
}
