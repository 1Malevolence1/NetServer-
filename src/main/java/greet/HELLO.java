package greet;

public class HELLO extends Greetable{

    @Override
    public String buildResponse(String userName) {
        return "Hello " + userName;
    }
}
