package http.server;

public enum HTTPCode {
	SUCCESS(200, "OK"), 
	RESOURCE_NOT_FOUND(404, "Not found"), 
	INTERNAL_SERVER_ERROR(500, "Internal server error");
	
	public final int code;
	public final String reasonPhrase;
	
	private HTTPCode(int code, String reasonPhrase)
	{
		this.code = code;
		this.reasonPhrase = reasonPhrase;
	}
}
