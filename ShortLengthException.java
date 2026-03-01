// custom exception for word lengths that are too short
public class ShortLengthException extends Exception {
    public ShortLengthException(String msg) {
        super(msg);
    }
}
