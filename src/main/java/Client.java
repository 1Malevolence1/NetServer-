
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        sendRequest();
    }

    private static void sendRequest() throws IOException {
        Socket client= new Socket("192.168.1.68", 25255);

        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream())); // Входной поток
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream())); // выходной поток

        String name = "Danil";


        bw.write(name);
        bw.newLine();
        bw.flush(); // отправить данные

        String  answer = br.readLine();
        System.out.println("Client got string: " + answer);

        br.close();
        bw.close();
    }
}
