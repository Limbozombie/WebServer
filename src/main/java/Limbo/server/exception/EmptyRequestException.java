package Limbo.server.exception;

public class EmptyRequestException extends Exception {
    
    private static final long serialVersionUID = - 967560111457967547L;
    
    public EmptyRequestException(String message) {
        super(message);
    }
    
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
