package itmo.MainService.exception;

public class HouseExistsException extends Exception {
    public HouseExistsException (String message){
        super(message);
    }
}
