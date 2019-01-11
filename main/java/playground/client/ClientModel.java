package playground.client;

import org.springframework.web.client.RestTemplate;

import playground.constants.Playground;
import playground.constants.User;
import playground.layout.UserTO;

public class ClientModel {

	private String current_userPlayground;
	private String current_email;
	private RestTemplate restTemplate;
	private String host;
	private int port;

	
	public ClientModel(String host,int port) {
		this.restTemplate = new RestTemplate();
		this.host = host;
		this.port = port;
	}
	
	
	
	public void SignIn(String userPlayground, String email) {
		if(!(userPlayground.equals("") || email.equals("")))
		{
			try {
			UserTO user = this.restTemplate.getForObject(this.getURL() + Playground.Function_3, UserTO.class, userPlayground, email);
			current_email = user.getEmail();
			current_userPlayground = user.getPlayground();
			}catch(Exception e) {
				
			}
		}
	}
	
	
	
	public String getURL()
	{
		return "http://" + host + ":" + port;
	}


	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	

	
}
