import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(25255);

        System.out.println("Server started");

        while (true){
            Socket client = serverSocket.accept(); // принимает запрос
            handleRequest(client);

        }
    }

    private static void handleRequest(Socket client) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream())); // Входной поток
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream())); // выходной поток

        StringBuilder sb = new StringBuilder("Hello,");
        String userName = br.readLine();
        System.out.println("Server got string: " + userName);

        sb.append(userName);
        bw.write(sb.toString());

        bw.newLine();
        bw.flush(); // отправить данные

        br.close();
        bw.close();

        client.close();
    }

}