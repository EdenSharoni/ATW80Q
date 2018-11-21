package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import playground.Constants;
import playground.database.Database;
import playground.elements.ElementTO;
import playground.logic.ConfirmException;
import playground.logic.LoginException;
import playground.logic.UserTO;

@RestController
public class EdenSharoniController {
	@Autowired
	Database db;

	@RequestMapping(method = RequestMethod.GET, path = "/playground/users/login/{playground}/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserTO login(@PathVariable("playground") String playground, @PathVariable("email") String email) {
		/*
		 * function 3INPUT: NONE OUTPUT: UserTO
		 */
		UserTO u = this.db.getUser(email);
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

	@RequestMapping(method = RequestMethod.GET, path = "/playground/elements/{userPlayground}/{email}/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] returnAllElementsByEmailAndCreatorPlayground(@PathVariable("email") String email,
			@PathVariable("userPlayground") String userPlayground) {
		// returns all element with the same playground and email as in url
		/*
		 * function 8 INPUT: NONE OUTPUT: ElementTO[]
		 */

		return this.db.getAllElementsByEmailAndCreatorPlayground(userPlayground, email);

	}
}
