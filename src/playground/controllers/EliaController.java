package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.exceptions.ConfirmException;
import playground.exceptions.LoginException;
import playground.layout.ActivityTO;
import playground.layout.ElementTO;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RestController
public class EliaController {
	
	@Autowired
	private ElementService elementService;
	@Autowired
	private UserService userService;

	@RequestMapping(
			method=RequestMethod.GET,
			path="elia_check",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public String viewMessages() {
		return "Elia";
	}

	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground }/{email}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
    public String setUser (@RequestBody ElementTO element)  {
		db.addElement(element);
		return " ";
	}
	
	

	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getElementsAtLocation(@RequestBody ElementTO element,@PathVariable("email") String email,@PathVariable("userPlayground") String userPlayground,@PathVariable("distance") double distance, @PathVariable("x") int x, @PathVariable("y") int y)throws ConfirmException{
		login(userPlayground, email);
		
		if(distance<0)
			throw new RuntimeException("Negative distance (" + distance + ")");
		
		ElementTO[] elementsInRange= elementService.getAllElementsTOInRadius(element,x,y,distance);
		return elementsInRange;
	}
	
	//*****************************************
		//do not copy this - originally from EdenSharoni
		//*****************************************
		@RequestMapping(method = RequestMethod.GET, path = "/login2", produces = MediaType.APPLICATION_JSON_VALUE)
		public UserEntity login(@PathVariable("playground") String playground, @PathVariable("email") String email) {
			/*
			 * function 3INPUT: NONE OUTPUT: UserTO
			 */
			UserEntity u = this.userService.getUser(email);
			if (u != null) {
				if (u.getPlayground().equals(playground)) {
					if (u.isVerified()) {
						return u;
					} else {
						throw new LoginException("User is not verified.");
					}
				} else {
					throw new ConfirmException("User does not belong to the specified playground.");
				}

			} else {
				throw new LoginException("Email is not registered.");
			}
		}
}
