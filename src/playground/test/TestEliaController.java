package playground.test;

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
import playground.*;
import playground.database.Database;
import playground.elements.ElementTO;
import playground.logic.MessageService;
import playground.logic.UserEntity;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestEliaController {
	//
	private RestTemplate restTemplate;
	
	@Autowired
	private Database db;
	
	@LocalServerPort
	private int port;
	private String url;

	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/messages";
		System.err.println(this.url);
	}
	
	@Before
	public void setup() {
		
	}

	@After
	public void teardown() {
		this.db.cleanDatabase();
	}

	private void clean() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void testServerIsBootingCorrectly() throws Exception {
		
	}
	
	@Test
	public void testIfWeGETNoElementsFromDatabaseWithNegativeRadius() {
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(name,playground,creatorPlayground,email);
		double distance=-1;
		assertThat(db.getAllElementsInRadius(element, distance));
	}
	
	@Test
	public void testIfWeGETNoElementsFromDatabaseWithRadius_0_() {
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(name,playground,creatorPlayground,email);
		double distance=0;
		assertThat(db.getAllElementsInRadius(element, distance));
	}
	
	@Test
	public void testPOSTNewElement() {
		
		String playground="playground",creatorPlayground="creator",name="nameOfElement:(english hei 7)",email="email@email.com";
		ElementTO element=new ElementTO(name,playground,creatorPlayground,email);
		db.addElement(element);
		assertThat(db.getElements().contains(element));
	}
	

}
