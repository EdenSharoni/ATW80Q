package playground.layout;
import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.PostConstruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import playground.Constants;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ConfigurationProperties(prefix = "test")
public class JPATest {
	
	private RestTemplate restTemplate;
	private ElementService elementService;
	private UserService userService;

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
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

	}

	@After
	public void teardown() {
		userService.cleanUserService();
		elementService.cleanElementService();

	}

	
	//1.1 Scenario: Adding non existing element
	@Test
	public void addNonExistingElementToJPA() {
		
		
		String playground="playground",creatorPlayground="creator";
		ElementEntity element = new ElementEntity(Constants.DEFAULT_ELEMENT_NAME, playground,creatorPlayground,1,2);
		
		elementService.addElementNoLogin(element);
		assertThat(elementService.isElementInDatabase(element));
		assertThat(elementService.getElementNoLogin(ElementEntity.createKey(element.getId(), element.getCreatorPlayground()))).isEqualToIgnoringGivenFields(element, "creationDate");
	}
	
	//1.2 Scenario:  Adding an existing element to jpa
	@Test
	public void addExistingElementToJPA() {
		
		String playground="playground",creatorPlayground="creator";
		ElementEntity element = new ElementEntity("name1", playground,creatorPlayground,1,2);
		elementService.addElementNoLogin(element);
		ElementEntity element2 = new ElementEntity("name2", playground,creatorPlayground,1,2);
		elementService.addElementNoLogin(element2);
		assertThat(elementService.isElementInDatabase(element));
		assertThat(elementService.getElementNoLogin(ElementEntity.createKey(element.getId(), element.getCreatorPlayground())))
		.isEqualToIgnoringGivenFields(element, "creationDate");
	}
	
	//2.1 Scenario:  Adding non existing user
	@Test
	public void addNonExistingUserToJPA() {
		
		UserEntity user = new UserEntity("username1", "email@email.com", "avatar1.jpg",Constants.PLAYER_ROLE,"rolnik");
		userService.addUser(user);
		assertThat(userService.isUserInDatabase(user));
		assertThat(userService.getUser(user.getPlayground(), user.getEmail())).isEqualTo(user);
	}
	
	//2.2 Scenario:  Adding an existing user to jpa
	@Test(expected = RuntimeException.class)
	public void addExistingUserToJPA() {
		
		UserEntity user = new UserEntity("username1", "email@email.com", "avatar1.jpg",Constants.PLAYER_ROLE,"rolnik");
		UserEntity user2 = new UserEntity("username2", "email@email.com", "avatar1.jpg",Constants.PLAYER_ROLE,"rolnik");
		
		userService.addUser(user);
		userService.addUser(user2);
		
	}
	
}
