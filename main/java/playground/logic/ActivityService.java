package playground.logic;

import java.util.ArrayList;
import org.springframework.data.domain.Pageable;

public interface ActivityService {

	public String toString();

	ActivityEntity getActivity(String superkey);

	ArrayList<ActivityEntity> getAllMessagesActivitiesInMessageBoard(String Superkey, Pageable pageable);

	ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e);

	Object executeActivity(String userPlayground, String email, ActivityEntity activity, Pageable pageable);

	Object getQuestion(ActivityEntity activity);

	boolean answerQuestion(ActivityEntity activity);

	void cleanActivityService();

	Object addMessage(ActivityEntity activity);

	String getGameRules(ActivityEntity activity);

	ActivityEntity createActivityEntity(String json);

}
