package playground.logic.stubs;

import org.springframework.stereotype.Service;

import playground.logic.ActivityService;
import playground.logic.ElementService;
import playground.logic.UserService;

@Service
public class DummyActivityService implements ActivityService {

	@Override
	public void setElementService(ElementService elementService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserService(UserService userService) {
		// TODO Auto-generated method stub
		
	}

}
