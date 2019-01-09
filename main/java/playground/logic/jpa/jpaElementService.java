package playground.logic.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import playground.aop.PlayerLogin;
import playground.Constants;
import playground.aop.LoginRequired;
import playground.aop.ManagerLogin;
import playground.aop.MyLog;
import playground.dal.ElementDao;
import playground.logic.ElementDataException;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
import playground.logic.UserEntity;
import playground.logic.UserService;

/**
 * 
 * 
 * 
 * Element types and their attributes:
 * 
 * ELEMENT_DEFAULT_TYPE - NONE
 * 
 * ELEMENT_QUESTION_TYPE - ELEMENT_QUESTION_KEY ELEMENT_ANSWER_KEY
 * ELEMENT_POINT_KEY
 * 
 * ELEMENT_MESSAGEBOARD_TYPE - MESSAGEBOARD_MESSAGE_COUNT int values in strings
 * indicated by above constant, "1" for message num 1, "2" for second and etc
 *
 */

@Service
public class jpaElementService implements ElementService {

	// this is the database we need are saving in
	private ElementDao elementsDB;
	private UserService userService;
	private IdGeneratorElementDao IdGeneratorElement;

	@Autowired
	public jpaElementService(ElementDao elementsDB, IdGeneratorElementDao idGeneratorElement) {
		this.elementsDB = elementsDB;
		this.IdGeneratorElement = idGeneratorElement;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserService getUserService() {
		return userService;
	}

	@Override
	@MyLog
	public void cleanElementService() {
		elementsDB.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	@LoginRequired
	public ElementEntity[] getAllElementsInRadius(String userPlayground, String email, double x, double y,
			double distance, Pageable pageable) {
		if (distance < 0)
			throw new RuntimeException("Negative distance (" + distance + ")");

		return lstToArray(elementsDB.findAllByXBetweenAndYBetween(x - distance, x + distance, y - distance,
				y + distance, pageable));

	}

	public ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, Pageable pageable) {
		return lst.stream().skip(pageable.getPageSize() * pageable.getPageNumber()).limit(pageable.getPageSize())
				.collect(Collectors.toList()).toArray(new ElementEntity[pageable.getPageSize()]);
	}

	private boolean roleIsCorrectExpirationDateCheck(UserEntity user, Date date) {
		// TODO check how to improve
		Date now = new Date();
		if (user.getRole().equals(Constants.PLAYER_ROLE) && now.compareTo(date) > 0)
			return true;
		else if (user.getRole().equals(Constants.MANAGER_ROLE))
			return true;
		else
			return false;
	}

	@Override
	@MyLog
	public double distanceBetween(double x1, double y1, double x2, double y2) {
		double xin = x1 - x2;
		double yin = y1 - y2;
		return Math.sqrt(xin * xin + yin * yin);
	}

	@Override
	@Transient
	@MyLog
	public boolean isElementInDatabase(ElementEntity element) {
		return this.elementsDB.existsById(element.getSuperkey());
	}

	@Override
	@Transactional(readOnly = false)
	@ManagerLogin
	public void addElements(String userPlayground, String email, ElementEntity[] elements) {
		for (int i = 0; i < elements.length; i++)
			addElement(userPlayground, email, elements[i]);
	}

	@Override
	@Transactional(readOnly = false)
	@MyLog
	public void addElementsNoLogin(ElementEntity[] elements) {
		for (int i = 0; i < elements.length; i++)
			addElementNoLogin(elements[i]);
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	@LoginRequired
	public ElementEntity[] getElementsWithValueInAttribute(String userPlayground, String email, String attributeName,
			String value, Pageable pageable) {
		ArrayList<ElementEntity> elements = getElements();
		ArrayList<ElementEntity> tempElementsList = new ArrayList<>();
		for (ElementEntity e : elements) {
			if (e.getAttributes().containsKey(attributeName) && e.getAttributes().get(attributeName).equals(value))
				if (roleIsCorrectExpirationDateCheck(userService.getUser(userPlayground, email), e.getExpirationDate()))
					tempElementsList.add(e);
		}
		if (tempElementsList.isEmpty())
			return new ElementEntity[0];
		else
			return getElementsBySizeAndPage(tempElementsList, pageable);

	}

	@Override
	@Transient
	@MyLog
	public boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground,
			String creatorEmail) {
		if (element.getCreatorPlayground().equals(creatorPlayground) && element.getCreatorEmail().equals(creatorEmail))
			return true;
		else
			return false;
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email,
			Pageable pageable) {

		return lstToArray(elementsDB.findAllByCreatorPlaygroundAndCreatorEmail(creatorPlayground, email, pageable));
	}

	@Override
	@Transactional(readOnly = true)
	@LoginRequired
	public ElementEntity getElement(String userPlayground, String email, String id, String creatorPlayground) {
		return getElement(userPlayground, email, createKey(id, creatorPlayground));
	}

	@Override
	public String createKey(String id, String creatorPlayground) {
		return id.concat(" " + creatorPlayground);
	}

	@Override
	@Transactional(readOnly = true)
	@LoginRequired
	public ElementEntity getElement(String userPlayground, String email, String superkey) {
		return getElementNoLogin(superkey);
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public ElementEntity getElementNoLogin(String superkey) {
		ElementEntity el = elementsDB.findById(superkey).orElse(null);
		if (el != null) {
			return el;
		} else
			throw new ElementDataException("Could not find element " + superkey);

	}

	@Override
	@Transactional(readOnly = false)
	@ManagerLogin
	public ElementEntity addElement(String userPlayground, String email, ElementEntity element) {
		if (element.getCreatorEmail() == null)
			element.setCreatorEmail(email);

		element.setPlayground(Constants.PLAYGROUND_NAME);

		return addElementNoLogin(element);
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<ElementEntity> getElements() {
		ArrayList<ElementEntity> lst = new ArrayList<ElementEntity>();
		for (ElementEntity e : elementsDB.findAll())
			lst.add(e);
		System.err.println("lst: " + lst);
		return lst;
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<ElementEntity> getElements(Pageable pageable) {
		ArrayList<ElementEntity> lst = new ArrayList<ElementEntity>();
		for (ElementEntity e : elementsDB.findAll(pageable))
			lst.add(e);
		return lst;
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	public ElementEntity[] lstToArray(ArrayList<ElementEntity> lst) {
		return lst.toArray(new ElementEntity[lst.size()]);
	}

	@Override
	@Transactional
	@MyLog
	@ManagerLogin
	public void updateElementsInDatabase(String userPlayground, String email, ArrayList<ElementEntity> elements) {
		try {
			for (ElementEntity el : elements)
				updateElementInDatabaseFromExternalElement(userPlayground, email, el);
		} catch (ElementDataException e) {
			throw new ElementDataException("Elements in collection have incorrect fields.");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public ElementEntity[] getAllElements() {
		ArrayList<ElementEntity> arr = getElements();
		return arr.toArray(new ElementEntity[arr.size()]);
	}

	@Override
	@MyLog
	public ElementEntity addElementNoLogin(ElementEntity element) {
		if (elementsDB.existsById(element.getSuperkey()))
			throw new ElementDataException("element data already exist in database");
		else {
			IdGeneratorElement tmp = IdGeneratorElement.save(new IdGeneratorElement());
			Long id = tmp.getId();
			IdGeneratorElement.delete(tmp);
			element.setId(id + "");
			boolean valid = validateElement(element);
			if (valid)
				return elementsDB.save(element);
			else
				throw new ElementDataException("Element is invalid \n" + element + "\n");
		}
	}

	private boolean validateElement(ElementEntity element) {
		switch (element.getType()) {
		case Constants.ELEMENT_DEFAULT_TYPE:
			return true;
		case Constants.ELEMENT_MESSAGEBOARD_TYPE:
			// if messageboard was created outside the playground
			if (!element.getAttributes().containsKey(Constants.MESSAGEBOARD_MESSAGE_COUNT))
				element.getAttributes().put(Constants.MESSAGEBOARD_MESSAGE_COUNT, 0);
			return true;
		case Constants.ELEMENT_QUESTION_TYPE:
			if (element.getAttributes().containsKey(Constants.ELEMENT_QUESTION_KEY)
					&& element.getAttributes().containsKey(Constants.ELEMENT_ANSWER_KEY)
					&& element.getAttributes().containsKey(Constants.ELEMENT_POINT_KEY)) {

				return true;
			}
			break;
		}
		return false;
	}

	@Override
	@MyLog
	@ManagerLogin
	public void replaceElementWith(String userPlayground, String email, ElementEntity entity, String id,
			String creatorplayground) {
		if (!entity.getId().equals(id) || !entity.getCreatorPlayground().equals(creatorplayground))
			throw new ElementDataException("Cannot change users Id or creatorplayground");
		else {
			ElementEntity tempElement = this.getElement(userPlayground, email, createKey(id, creatorplayground));
			System.err.println("tempElement: " + tempElement);
			if (tempElement != null) {
				// Deletes old and replaces with new
				entity.setCreationDate(tempElement.getCreationDate());
				elementsDB.deleteById(tempElement.getSuperkey());
				elementsDB.save(entity);
			} else
				throw new ElementDataException("element data for update is incorrect");
		}
	}

	@Override
	@Transactional(readOnly = false)
	@MyLog
	@ManagerLogin
	public void updateElementInDatabaseFromExternalElement(String userPlayground, String email, ElementEntity element) {
		updateElementInDatabaseFromExternalElementNoLogin(element);
	}

	@Override
	public void updateElementInDatabaseFromExternalElementNoLogin(ElementEntity element) {
		ElementEntity tempElement = this.getElementNoLogin(element.getSuperkey());
		if (tempElement != null) {
			// Deletes old and replaces with new
			elementsDB.deleteById(tempElement.getSuperkey());
			elementsDB.save(element);
		} else
			throw new ElementDataException("element data for update is incorrect");
		
	}

}
