package playground.logic;

import java.util.ArrayList;

import org.springframework.data.domain.Pageable;

public interface UserService {

	ArrayList<UserEntity> getUsers();
	UserEntity[] getUsers(Pageable pageable);

	UserEntity addUser(UserEntity user);

	UserEntity verifyUser(String email, String playground, String code);

	void cleanUserService();

	void updateUser(UserEntity user);

	UserEntity getUser(String email, String playground);

	UserEntity login(String playground, String email);

	void addUser(NewUserForm user);

	boolean isUserInDatabase(UserEntity user);
	
	
	void updateUser(String playground, String email, UserEntity user);
	

}
