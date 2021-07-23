package example;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -8460356990632230194L;
	private String code;


	public CustomException(String code) {
		super(code);
		this.code = code;
	}

}
