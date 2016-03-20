package pt.ulisboa.tecnico.bubbledocs.dto;

public class UserDto {

	private String username;
	private String name;
	private int id;
	private String token;
	private float sessionTime;
	private String email;


	public UserDto(String username, String name, int id,
			String token, float sessionTime, String email) {
		this.username = username;
		this.name = name;
		this.id = id;
		this.token = token;
		this.sessionTime = sessionTime;
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public String getName() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

	public String getToken() {
		return this.token;
	}

	public float getSessionTime() {
		return this.sessionTime;
	}

	public String getEmail() {
		return this.email;
	}

}
