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


class SimpleServer extends Thread{
    private Socket socket;

    public SimpleServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            handleRequest(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private  void handleRequest(Socket client) throws IOException {
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream())); // Входной поток
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream())); // выходной поток


            String request = br.readLine();
            String[] lines = request.split("\\s+");

            String commands = lines[0];
            String userNames = lines[1];

            System.out.println("Server got string 1: " + commands);
            System.out.println("Server got string 2: " + userNames);


            String response = buildRespons(commands, userNames);

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

    private  String buildRespons(String commands, String userNames) {
        switch (commands){
            case "Hello" : return commands + " " + userNames;
            case "Good morning" : return commands + " " + userNames;
            case "Good night" : return commands + " " + userNames;
            case "CRAZY" : return commands + " " + userNames;
            default: return "Такой команды нет";
        }

    }
}
