package greet;

public class HEY extends Greetable{
    @Override
    public String buildResponse(String userName) {
        return "HEY " + userName;
    }
}
