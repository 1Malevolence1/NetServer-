import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(25255);

        System.out.println("Server started");

        while (true){
            Socket client = serverSocket.accept(); // принимает запрос
            new SimpleServer(client).start();

        }
    }



}


class SimpleServer extends Thread {
    private Socket socket;

    public SimpleServer(Socket socket) {
        this.socket = socket;
        run();
    }

    @Override
    public void run() {
        try {
            handleRequest(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleRequest(Socket client) throws IOException {
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream())); // Входной поток
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream())); // выходной поток

            String request = br.readLine();

            String[] lines = request.split("\\s+");


            Integer IDcommand = Integer.parseInt(lines[0]);
            String userCommand = lines[1];
            String userName = lines[2];
            String number = lines[3];

            System.out.println("Server got string 1: " + IDcommand);
            System.out.println("Server got string 2: " + userCommand);
            System.out.println("Server got string 3: " + userName);
            System.out.println("Server got string 4: " + number);
            System.out.println();


            String response = buildResponse(IDcommand, userName);

            bw.write(response);
            bw.newLine();
            bw.flush(); // отправить данные

            br.close();
            bw.close();

            client.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildResponse(Integer idCommand, String userNames) {
        StringBuilder textServer = new StringBuilder();
        if (idCommand <= Commands.values().length) {
            Commands commands = Commands.values()[idCommand];
            return textServer.append(commands.toString()).append(" ").append(userNames).toString();
        } else return textServer.append("Такой команды нет").toString();
    }
}
