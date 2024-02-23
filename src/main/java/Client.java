
import config.Config;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws IOException {
        sendRequest();


    }

    private static void sendRequest() throws IOException {

        for (int count = 0; count < 16; count++) {
            SimpleClient simpleClient = new SimpleClient(count);
            simpleClient.start();
        }

    }
}

class SimpleClient extends Thread{

    private final String[] COMMANDS = {"HELLO" ,"HEY", "Good night", "CRAZY"};

    private int cmdNumber;

    public SimpleClient(int cmdNumber) {
        this.cmdNumber = cmdNumber;
    }

    @Override
    public void run(){

        try {

            Socket client = new Socket(Config.IP, 25255);

            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream())); // Входной поток
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream())); // выходной поток

            StringBuilder stringBuilder = new StringBuilder();

            String command = COMMANDS[cmdNumber % COMMANDS.length];
            String name = "Danil";

            stringBuilder.append(command).append(" ").append(name);

            bw.write(stringBuilder.toString());
            bw.newLine();
            bw.flush(); // отправить данные

            String  answer = br.readLine();
            System.out.println("Client got string: " + answer);

            br.close();
            bw.close();


        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}