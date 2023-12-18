import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ServerMT extends Thread {
    private static int secretValue;
    private int numClients = 0;
    private String winnerIP;
    private ServerSocket serverSocket;
    private ArrayList<Repartiteur> clientList = new ArrayList<>();
    private Repartiteur secretKeeper;
    private boolean running = true;

    public void run() {
        try {
            serverSocket = new ServerSocket(1234);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    informSecretKeeperChange();
                }
            }, 0, 30000); // Change secret keeper every 60 seconds

            while (running) {
                Socket clientSocket = serverSocket.accept();
                numClients++;
                Repartiteur repartiteur = new Repartiteur(clientSocket, numClients, this);
                clientList.add(repartiteur);

                if (secretKeeper == null) {
                    secretKeeper = repartiteur;
                    secretKeeper.setSecretKeeper(true);
                    secretKeeper.notifySecretKeeperChange(); // Notify initial secret keeper
                }
                repartiteur.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void informSecretKeeperChange() {
        if (secretKeeper != null) {
            secretKeeper.setSecretKeeper(false); // Notify the current secret keeper
        }

        if (!clientList.isEmpty()) {
            Random rand = new Random();
            secretKeeper = clientList.get(rand.nextInt(clientList.size()));
            secretKeeper.setSecretKeeper(true); // Set the new secret keeper
            secretKeeper.notifySecretKeeperChange(); // Notify the new secret keeper
            updateSecretValue();
            System.out.println("New secret keeper: Client " + secretKeeper.getNumClient());
        }
    }

    private void updateSecretValue() {
        Random rand = new Random();
        secretValue = 100 * rand.nextInt(9);
        System.out.println("New secret value: " + secretValue);
    }

    public synchronized void endGame(String winnerIP) throws IOException {
        this.running = false;
        this.winnerIP = winnerIP;
        diffuseWinner();
    }

    private void diffuseWinner() throws IOException {
        for (Repartiteur client : clientList) {
            client.whoWon(winnerIP);
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getSecretValue() {
        return secretValue;
    }

    public static void main(String[] args) {
        new ServerMT().start();
    }
}
