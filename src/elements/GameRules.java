package elements;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class GameRules {
	
	private String rulesStr = "Our vision is to become a part of the official school program.\r\n" + 
			"We aim to improve the memory of students on facts about the world in all topics, and to become the best tool in the market which could help teachers create more interactive lessons for their students. \r\n" + 
			"For students it will be an alternative way to improving their knowledge in the school material and possibly quiz themselves before tests. \r\n" + 
			"";
	
	@RequestMapping("/view_rules")
	public String viewRules() {
		return this.rulesStr;
	}
}