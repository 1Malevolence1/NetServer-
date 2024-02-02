import greet.Greetable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Server {

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        ServerSocket serverSocket = new ServerSocket(25255);
        Map<String, Greetable> handlers = loadHandler();
        System.out.println("Server started");

        while (true) {
            Socket client = serverSocket.accept(); // принимает запрос
            new SimpleServer(client, handlers).start();

        }
    }

    private static Map<String, Greetable> loadHandler() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<String, Greetable> result = new HashMap<>();
        // .class - загрузчик классов
        // getClassLoader - Каждый класс знает, каким загрузчиком он был загружен, и можно получить эту информацию, вызвав у него этот метод
        try (InputStream is = Server.class.getClassLoader().getResourceAsStream("server.properties")) {

            Properties properties = new Properties();
            properties.load(is); // Загружает свойства из файла, представленного объектом InputStream

            for (Object command : properties.keySet()) {
                String className = properties.getProperty(command.toString());

                Class<Greetable> cl = (Class<Greetable>) Class.forName(className);   // Получаем класс
                Greetable handler = cl.getConstructor().newInstance();
                result.put(command.toString(), handler);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}






class SimpleServer extends Thread{
    private Socket socket;
    private  Map<String, Greetable> handlers;

    public SimpleServer(Socket socket, Map<String, Greetable> handlers) {
        this.socket = socket;
        this.handlers = handlers;
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

    private String buildRespons(String commands, String userNames) {
       Greetable handler = handlers.get(commands);
       if(handler != null){
           return handler.buildResponse(userNames);
       }

       return "Такой команнды нет";

    }
}
