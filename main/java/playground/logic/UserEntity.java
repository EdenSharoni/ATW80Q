package playground.logic;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import playground.Constants;

//KEY IS EMAIL+PLAYGROUND
@Entity
@Table(name = "USER")
public class UserEntity {
	

	private String email;
	private String avatar;
	private String username;
	private String playground;
	private String role = Constants.UNDEFINED_ROLE;
	private String verificationCode = Constants.DEFAULT_VERIFICATION_CODE;
	private String superkey;
	private String id;
	private long points = 0;

	public UserEntity() {
	}
	
	
	public UserEntity(String username, String email, String avatar, String role, String playground) {
		super();
		setUsername(username);
		setEmail(email);
		setAvatar(avatar);
		setRole(role);
		setPlayground(playground);
		setPoints(0);
		setSuperkey();
		this.verificationCode = generateCode();
	}


	public UserEntity(NewUserForm user) {
		this(user.getUsername(),user.getEmail(),user.getAvatar(),user.getRole(),Constants.PLAYGROUND_NAME);
		
	}

	@Transient
	private static String generateCode() {
		return Constants.DEFAULT_VERIFICATION_CODE;
	}

	@Id
	public String getSuperkey() {
		return superkey;
	}

	public void setSuperkey(String superkey) {
		this.superkey = superkey;
	}
	
	public void setSuperkey() {
		superkey = createKey(email, playground);
	}
	@Transient
	public static String createKey(String email, String playground) {
		return email.concat(" " + playground);
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		role = role.toLowerCase();
		if (role.equals(Constants.MODERATOR_ROLE.toLowerCase())) {
			this.role = Constants.MODERATOR_ROLE;
		} else if (role.equals(Constants.PLAYER_ROLE.toLowerCase())) {
			this.role = Constants.PLAYER_ROLE;
		} else {
			this.role = Constants.UNDEFINED_ROLE;
			throw new RuntimeException("Undefined role");
		}
	}

	
	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	
	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	@Transient
	public void verifyUser() {
		verificationCode = null;
	}
	
	@Transient
	public boolean isVerified() {
		if (this.verificationCode == null)
			return true;
		else
			return false;
	}

	@Override
	@Transient
	public String toString() {
		return "UserEntity [superkey="+superkey+",id="+id+" email=" + email + ", avatar=" + avatar + ", username=" + username + ", playground="
				+ playground + ", role=" + role + ", verificationCode=" + verificationCode + ", points=" + points + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (playground == null) {
			if (other.playground != null)
				return false;
		} else if (!playground.equals(other.playground))
			return false;
		if (points != other.points)
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (superkey == null) {
			if (other.superkey != null)
				return false;
		} else if (!superkey.equals(other.superkey))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (verificationCode == null) {
			if (other.verificationCode != null)
				return false;
		} else if (!verificationCode.equals(other.verificationCode))
			return false;
		return true;
	}

}
