import java.io.*;
import java.net.Socket;

public class Repartiteur extends Thread {
    private final Socket socket;
    private final int numClient;
    private final ServerMT server;
    private boolean noWin = true;
    private BufferedReader br;
    private PrintWriter pw;
    private boolean isSecretKeeper = false;

    public Repartiteur(Socket socket, int numClient, ServerMT server) {
        this.socket = socket;
        this.numClient = numClient;
        this.server = server;

        try {
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println("Bienvenue, vous etes le client numero: " + numClient);
            System.out.println("Client numero : " + numClient + " connecte");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumClient() {
        return numClient;
    }

    public void setSecretKeeper(boolean isSecretKeeper) {
        this.isSecretKeeper = isSecretKeeper;
    }

    public boolean isSecretKeeper() {
        return isSecretKeeper;
    }

    public void run() {
        try {
            while (noWin) {
                try {
                    int val = Integer.parseInt(br.readLine());
                    pw.println("");

                    if (isSecretKeeper) {
                        pw.println("Secret value: " + ServerMT.getSecretValue());
                    } else {
                        if (numClient%2== 0) {
                            val = val /2;
                        } else {
                            val = (val * 3) /2 ;
                        }
                        if (val > ServerMT.getSecretValue()) {
                            pw.println(val + " > secret value");
                        } else if (val < ServerMT.getSecretValue()) {
                            pw.println(val + " < secret value");
                        } else {
                            pw.println("Felicitations, vous etes le gagnant ! La valeur secrete est : " + ServerMT.getSecretValue());
                            String ipWin = socket.getRemoteSocketAddress().toString();
                            System.out.println("Le client gagnant est : " + numClient + " son adresse IP est : " + ipWin);
                            server.endGame(ipWin);
                            noWin = false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } finally {
            closeResources();
        }
    }

    public void whoWon(String winnerIPAddress) throws IOException {
        try {
            if (pw != null) {
                pw.println("L'addresse IP du gagnant est : " + winnerIPAddress + " la valeur secrete etait : " + ServerMT.getSecretValue());
            }
        } finally {
            closeResources();
        }
    }

    public synchronized void notifySecretKeeperChange() {
        if (pw != null) {
            pw.println("Yvous etes maintenant le secret keeper");
        }
    }

    private void closeResources() {
        try {
            if (pw != null) {
                pw.close();
            }
            if (br != null) {
                br.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
