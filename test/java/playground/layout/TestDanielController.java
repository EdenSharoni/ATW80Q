package playground.layout;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import playground.Constants;
import playground.layout.ElementTO;
import playground.layout.UserTO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.Location;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;
import playground.logic.UserService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestDanielController {
	
	private RestTemplate restTemplate;
	
	private ElementService elementService;
	private UserService userService;
	
	@Autowired
	public void setElementService(ElementService elementService){
		this.elementService = elementService;
	}
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@LocalServerPort
	private int port;
	private String url;
	
//	@Autowired
//	private Database database;

	private ObjectMapper jsonMapper;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port;
		System.err.println(this.url);
		this.jsonMapper = new ObjectMapper();
	}
	
	@Before
	public void setup() {
		
	}

	@After
	public void teardown() {
		userService.cleanUserService();
		elementService.cleanElementService();
	}
	
	@Test(expected=RuntimeException.class)
	public void testRegisterNewUserWithWrongEmail() throws JsonProcessingException{
		NewUserForm postUserForm =  new NewUserForm("wrongEmail", Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE);
		jsonMapper.writeValueAsString(new UserTO(new UserEntity(postUserForm)));
	}
	
	@Test(expected=RuntimeException.class)
	public void testUserAlreadyExists() {
		NewUserForm postUserForm =  new NewUserForm(Constants.EMAIL_FOR_TESTS, Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE);
		UserTO userToAdd = new UserTO(new UserEntity(postUserForm));
		userService.addUser(userToAdd.toEntity());
		String actualReturnedValue = this.restTemplate.postForObject(
				this.url+"/playground/users", postUserForm, String.class);
		assertThat(actualReturnedValue).isNull();
	}
	
	@Test
	public void testSuccessfullyRegisterNewUser() throws Exception{
		NewUserForm postUserForm = new NewUserForm(Constants.EMAIL_FOR_TESTS, Constants.DEFAULT_USERNAME, Constants.AVATAR_FOR_TESTS, Constants.PLAYER_ROLE);
		String testValue = jsonMapper.writeValueAsString(new UserTO(new UserEntity(postUserForm)));
		
		String actualReturnedValue = this.restTemplate.postForObject(this.url+"/playground/users", postUserForm, String.class);
		//TODO fix problem with mapping
		assertThat(actualReturnedValue)
		.isNotNull()
		.isEqualTo(testValue);
		
		assertThat(jsonMapper.writeValueAsString(userService.getUser(Constants.EMAIL_FOR_TESTS))).isEqualTo(testValue);	
		//TODO check if its right to use JSON for tests this way
	}
	
	
	@Test(expected = RuntimeException.class)
	public void testWrongElementPassedForUpdate() {
		
		ElementEntity elementEntityForTest = 
				new ElementEntity(Constants.ID_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS,new Location(0,1));
		ElementTO elementForTest = new ElementTO(elementEntityForTest);
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  elementForTest, 
				Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.EMAIL_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.ID_FOR_TESTS);

	}
	
/*	@Test
	public void testSuccessfullyUpdateElement() throws Exception{
	
		ElementEntity updatedElementForTestEntity = 
				new ElementEntity(Constants.ID_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS,new Location(0,1));
		elementService.addElement(updatedElementForTestEntity);
		
		updatedElementForTestEntity.setCreatorPlayground("for test");
		ElementTO updatedElementForTestTO = new ElementTO(updatedElementForTestEntity);
		
		this.restTemplate.put(this.url+"/playground/elements/{userPlayground}/{email}/{playground}/{id}",  updatedElementForTestTO, Constants.CREATOR_PLAYGROUND_FOR_TESTS, 
				Constants.EMAIL_FOR_TESTS, Constants.PLAYGROUND_NAME, Constants.ID_FOR_TESTS);
		
		System.out.println("Arrived");
		ElementEntity actualEntity = elementService.getElement(Constants.ID_FOR_TESTS, Constants.PLAYGROUND_NAME);
		System.out.println("Actual entity:"+actualEntity.toString());
		System.out.println("updatedElementForTestEntity: "+updatedElementForTestEntity.toString());
		
		assertThat(actualEntity).isNotNull();
		System.out.println("Location from actualEntity: "+actualEntity.getLocation().toString());
		System.out.println("Location from actualEntity: "+updatedElementForTestEntity.getLocation().toString());
		assertEquals(actualEntity,updatedElementForTestTO.toEntity());
		//TODO check why fails because of location
		}*/
	
	@Test(expected=RuntimeException.class)
	public void testAttributeNotExist() {
		ResponseEntity<ElementTO[]> responseEntity = restTemplate.getForEntity(this.url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class);
		ElementTO[] elements = responseEntity.getBody();
		assertThat(elements).isNull();
	}
	
	
	//Problem with gettin ElementTO[] Eyal help needed
/*  @Test
	public void testSuccessfullyGetElementsByUserPlaygroundEmailAttributeNameValue(){
		ElementTO[] elementForTest = {new ElementTO(new ElementEntity(Constants.ID_FOR_TESTS, 
				Constants.PLAYGROUND_NAME, Constants.EMAIL_FOR_TESTS, new Location(1,0)))};
		HashMap<String, Object> testMap = new HashMap<>();
		testMap.put("attribute1","attr1Value");
		testMap.put("attribute2","attr2Value");
		testMap.put("attr3","attr3Val");
		
		elementForTest[0].setAttributes(testMap);
		elementService.addElement(elementForTest[0].toEntity());
		System.out.println("Check that element added" + elementService.getElements().toString());
		
		ElementTO[] forNow = this.restTemplate.getForObject(url + 
				"/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", ElementTO[].class, 
				Constants.CREATOR_PLAYGROUND_FOR_TESTS, Constants.EMAIL_FOR_TESTS, "attr3", "attr3Val");
	    
		System.out.println("forNow"+forNow.toString());
		assertThat(forNow).isNotNull();
		assertThat(forNow[0]).isEqualToComparingFieldByField(elementForTest[0]);
	}*/
	
	
}
