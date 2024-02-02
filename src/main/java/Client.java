
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

    private final Commands[] COMMANDS = Commands.values();

    private int cmdNumber;

    public SimpleClient(int cmdNumber) {
        this.cmdNumber = cmdNumber;

    }

    @Override
    public void run(){

        try {

            Socket client = new Socket("192.168.1.68", 25255);

            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream())); // Входной поток
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream())); // выходной поток

            StringBuilder stringBuilder = new StringBuilder();

            String name = "Danil";
            int idCommand = cmdNumber % COMMANDS.length;
            Commands command = COMMANDS[idCommand];

            stringBuilder.append(idCommand).append(" ")
                    .append(command.toString()).append(" ")
                    .append(name).append(" ").append(cmdNumber);


            bw.write(stringBuilder.toString());
            bw.newLine();
            bw.flush();


        //    stringBuilder.append(command).append(" ").append(name);


            String  answer = br.readLine();
            System.out.println("Client got string: " + answer);

            br.close();
            bw.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}