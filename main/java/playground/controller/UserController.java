package playground.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import playground.aop.MyLog;
import playground.constants.Playground;
import playground.layout.UserTO;
import playground.logic.ErrorException;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;


@RestController
public class UserController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/playground/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public UserTO registerNewUser(@RequestBody NewUserForm newUserForm) {
		/* function 1
		 * INPUT: NewUserForm
		 * OUTPUT: UserTO
		 */
		return new UserTO(userService.addUser(new UserEntity(newUserForm.getUsername(), newUserForm.getEmail(), newUserForm.getAvatar(), newUserForm.getRole(), Playground.PLAYGROUND_NAME)));
	}

	@RequestMapping(method=RequestMethod.GET,path="/playground/users/confirm/{playground}/{email}/{code}",produces=MediaType.APPLICATION_JSON_VALUE)
	public UserTO verifyUser(@PathVariable("playground") String playground, @PathVariable("email") String email, @PathVariable("code") String code)
	{
		/* function 2
		 * INPUT: NONE
		 * OUTPUT: UserTO
		 */
		return new UserTO(this.userService.verifyUser(email, playground, code));
	}

	@RequestMapping(method = RequestMethod.GET, path = "/playground/users/login/{playground}/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO login(@PathVariable("playground") String userPlayground, @PathVariable("email") String email) {
		/*
		 * function 3
		 * INPUT: NONE OUTPUT: UserTO
		 */
		return new UserTO(this.userService.login(userPlayground, email));
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/playground/users/{playground}/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@RequestBody UserTO user, @PathVariable("email") String email, @PathVariable("playground") String playground) {
		/*
		 * function 4 INPUT: UserTO OUTPUT: NONE
		 */
		this.userService.updateUser(playground,email, user.toEntity());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/playground/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO[] getAllUsers(Pageable pageable) {
		return getElementTOArray(userService.getUsers(pageable));
	}

	public UserTO[] getElementTOArray(UserEntity[] lst){
		ArrayList<UserTO> result = new ArrayList<>();
		for (UserEntity e : lst) {
			result.add(new UserTO(e));
		}
		return result.toArray(new UserTO[lst.length]);
	}
	
	@MyLog
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorException handleException(Exception e) {
		String message = e.getMessage();
		if (message == null)
			message = "There is no relevant message";
		return new ErrorException(message);
	}
}
