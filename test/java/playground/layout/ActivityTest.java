package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.PostConstruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import playground.Constants;
import playground.dal.ElementDao;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ActivityTest {
	private RestTemplate restTemplate;

	private ElementService elementService;
	private UserService userService;
	private ActivityService activityService;


	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@LocalServerPort
	private int port;
	private String url;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port;
		System.err.println(this.url);
	}

	@Before
	public void setup() {
		userService.cleanUserService();
		elementService.cleanElementService();
		activityService.cleanActivityService();
	}

	@After
	public void teardown() {
		userService.cleanUserService();
		elementService.cleanElementService();
		activityService.cleanActivityService();
	}
	// ******************************************************************************************//
	// url #11 /playground/activities/{userPlayground}/{email} started

	// 11.1 Scenario: Sending Echo activity
	@Test
	public void SendEchoActivity() { // TODO: Eden Sharoni: "isEqualToIgnoringGivenFields" --> is it ok to add
										// playerPlayground and playerEmail??
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ActivityEntity ent = new ActivityEntity();
		ent.setType(Constants.DEFAULT_ACTIVITY_TYPE);
		ActivityTO act = new ActivityTO(ent);
		ActivityTO ob = this.restTemplate.postForObject(this.url + Constants.Function_11, act, ActivityTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS);
		System.err.println(act.toString());
		System.err.println(ob.toString());
		assertThat(act).isEqualToIgnoringGivenFields(ob, "id", "playerPlayground", "playerEmail");
	}

	// 11.2 Scenario: Sending Message activity
	@Test
	public void SendMessageActivityToExistingBoard() {
		ElementEntity messageBoard = new ElementEntity("msgboard", Constants.EMAIL_FOR_TESTS, Constants.LOCATION_X1,
				Constants.LOCATION_Y1);
		messageBoard.setType(Constants.ELEMENT_MESSAGEBOARD_TYPE);
		elementService.addElementNoLogin(messageBoard);
		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, Constants.MANAGER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ActivityEntity ent = new ActivityEntity();
		ent.setType(Constants.MESSAGE_ACTIVITY);
		ent.setElementId(messageBoard.getSuperkey());
		ActivityTO act = new ActivityTO(ent);
		ActivityTO message = this.restTemplate.postForObject(this.url + Constants.Function_11, act, ActivityTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS);
		assertThat(act).isEqualToIgnoringGivenFields(message, "id");
	}

	// 11.3 Scenario: Sending Message activity to non existing message board
	@Test(expected = RuntimeException.class)
	public void SendMessageActivityToNonExistingBoard() {
		ElementEntity messageBoard = new ElementEntity("msgboard", Constants.EMAIL_FOR_TESTS, Constants.LOCATION_X1,
				Constants.LOCATION_Y1);
		messageBoard.setType(Constants.ELEMENT_MESSAGEBOARD_TYPE);

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, Constants.MANAGER_ROLE, Constants.PLAYGROUND_NAME);
		user.verifyUser();
		userService.addUser(user);
		ActivityEntity ent = new ActivityEntity();
		ent.setType(Constants.MESSAGE_ACTIVITY);
		ent.setElementId(messageBoard.getSuperkey());
		ActivityTO act = new ActivityTO(ent);
		ActivityTO message = this.restTemplate.postForObject(this.url + Constants.Function_11, act, ActivityTO.class,
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS);

	}



	@Test
	public void AddingQuestionToPlaygroundAsManager() {

	}

	@Test(expected = RuntimeException.class)
	public void AddingQuestionToMessageBoardAsPlayer() {

	}

	@Test
	public void AddingToPlaygroundAQuestionWithAMissingAttribute() {

	}

	@Test
	public void ReadExistingQuestionActivity() {

	}

	@Test
	public void ReadingAQuestionThatNotExistInDatabase() {

	}

	@Test
	public void AnsweringAQuestionWithCorrectAnswer() {

	}

	@Test
	public void AnsweringAQuestionWithIncorrectAnswer() {

	}

	@Test
	public void GettingScoreFromDatabase() {// TODO: Eden Sharoni: Why Constants.CREATOR_PLAYGROUND_FOR_TESTS and not
											// Constants.EMAIL_FOR_TESTS?

		UserEntity user = new UserEntity(Constants.DEFAULT_USERNAME, Constants.EMAIL_FOR_TESTS,
				Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE, Constants.PLAYGROUND_NAME);

		user.verifyUser();
		userService.addUser(user);
		ActivityEntity ent = new ActivityEntity();
		ent.setType(Constants.MESSAGE_ACTIVITY);
		ent.setElementId(Constants.CREATOR_PLAYGROUND_FOR_TESTS);
		ActivityTO act = new ActivityTO(ent);
		System.err.println(ent);
		System.err.println(act);
		ActivityTO rules = this.restTemplate.postForObject(this.url + Constants.Function_11, act, ActivityTO.class,
				Constants.PLAYGROUND_NAME, Constants.CREATOR_PLAYGROUND_FOR_TESTS);
		System.err.print("1");
		System.err.print("act.getType(): " + act.getType());
		System.err.print("rules.getType(): " + rules.getType());
		assertThat(act.getType().equals(rules.getType()));
	}

	// url #11 /playground/activities/{userPlayground}/{email} finished
	// ******************************************************************************************//

}
