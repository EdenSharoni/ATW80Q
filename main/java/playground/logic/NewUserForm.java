package playground.logic;

import playground.constants.User;

public class NewUserForm {

	private String email;
	private String username;
	private String avatar;
	private String role;

	public NewUserForm() {
		this.email = "default";
		this.username = "random";
		this.avatar = "ava";
		this.role = "no";
	}

	public NewUserForm(String email, String username, String avatar, String role) {
		super();
		if (username != null && role != null) {
			this.email = email;
			this.username = username;
			this.avatar = avatar;
			this.role = role;
		} else
			throw new RegisterNewUserException(User.INCORRECT_REGISTER_INPUT_ERROR);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "NewUserForm [email=" + email + ", username=" + username + ", avatar=" + avatar + ", role=" + role + "]";
	}

}
