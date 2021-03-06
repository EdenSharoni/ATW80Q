package playground.logic.stubs;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import playground.constants.Activity;
import playground.constants.Element;
import playground.dal.ActivityDao;
import playground.logic.ActivityEntity;
import playground.logic.ActivityService;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.jpa.IdGeneratorActivityDao;

//@Service
public class DummyActivityService implements ActivityService {

	private ArrayList<ActivityEntity> activityDB;
	private ArrayList<ElementEntity> questionDB;
	private ElementService elementService;

	@Autowired
	public DummyActivityService(ActivityDao activity, IdGeneratorActivityDao IdGeneratorActivity) {
		this.activityDB = new ArrayList<ActivityEntity>();
		this.questionDB = new ArrayList<ElementEntity>();
	}

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;

	}

	public String[] readMessagesFromMessageboard() {
		ArrayList<String> messages = new ArrayList<>();
		for (ElementEntity elEn : elementService.getAllElements()) {
			if (elEn.getType().equals(Activity.ACTIVITY_MESSAGE_KEY))
				messages.add(elEn.getAttributes().toString());
		}
		return messages.toArray(new String[messages.size()]);
	}

	public ActivityEntity[] getAll(ArrayList<ActivityEntity> lst, int size, int page) {
		return lst.stream().skip(size * page).limit(size).collect(Collectors.toList())
				.toArray(new ActivityEntity[lst.size()]);
	}

	@Override
	public ActivityEntity getActivity(String superkey) {
		for (ActivityEntity e : activityDB) {
			if (e.getSuperkey().equals(superkey)) {
				return e;
			}
		}
		return null;
	}

	@Override
	public ArrayList<ActivityEntity> getAllMessagesActivitiesInMessageBoard(String Superkey, Pageable pageable) {
		return null;
	}

	@Override
	public ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e) {
		activityDB.add(e);
		return e;
	}

	@Override
	public Object getQuestion(ActivityEntity activity) {
		String id = activity.getElementId();
		for (ElementEntity e : questionDB)
			if (e.getSuperkey().equals(id)) {
				return e;
			}
		return null;
	}

	public Object setQuestion(ActivityEntity activity) {
		String id = activity.getElementId();
		for (ElementEntity e : questionDB)
			if (e.getSuperkey().equals(id)) {
				e.getAttributes().put(Element.ELEMENT_ANSWER_KEY,
						activity.getAttribute().get(Element.ELEMENT_ANSWER_KEY));
				return activity.getAttribute().get(Element.ELEMENT_ANSWER_KEY);
			}
		return null;
	}

	@Override
	public boolean answerQuestion(ActivityEntity activity) {
		return false;
	}

	@Override
	public void cleanActivityService() {
		activityDB.clear();

	}

	@Override
	public Object executeActivity(String userPlayground, String email, ActivityEntity activity, Pageable pageable) {
		return null;
	}

	@Override
	public Object addMessage(ActivityEntity activity) {
		return null;
	}

	@Override
	public String getGameRules(ActivityEntity activity) {
		return null;
	}

	@Override
	public ActivityEntity createActivityEntity(String json) {
		return null;
	}

}
