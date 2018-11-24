package playground.logic;

import java.io.Serializable;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import playground.elements.ElementTO;
import playground.exceptions.ElementDataException;

@Service
public class ElementService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static ArrayList<ElementEntity> elements = new ArrayList<ElementEntity>();
//	private static ArrayList<UserEntity> users = Database.getUsers();
	
	public ArrayList<ElementEntity> getElements() {
		return elements;
	}

	public static void setElements(ArrayList<ElementEntity> elementsToSet) {
		elements = elementsToSet;
	}
	
	public void addElement(ElementEntity element) {
		getElements().add(element);
	}
	
	public ElementEntity getElement(String id, String playground) {
		for(ElementEntity e: elements)
		{
			if(e.getId().equals(id) && e.getPlayground().equals(playground))
				return e;
		}
		return null;
	}
	
	public ArrayList<ElementTO> getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email) {
		ArrayList<ElementTO> result = new ArrayList<>();
		for (ElementEntity element : elements) {
			if(checkIfElementsMadeBySpecificUserInSpecificPlayground(element, creatorPlayground, email))
				result.add(new ElementTO(element));
		}
		return result;
	}
	
	public Boolean checkIfElementsMadeBySpecificUserInSpecificPlayground(ElementEntity element, 
			String creatorPlayground, String creatorEmail) {
		if(element.getCreatorPlayground() == creatorPlayground && element.getCreatorEmail() == creatorEmail)
			return true;
		else
			return false;
	}
	
	public ElementTO[] getElementsWithValueInAttribute(String creatorPlayground, 
			String creatorEmail, String attributeName, String value) {
		// TODO Auto-generated method stub
		ArrayList<ElementTO> tempElementsList = new ArrayList<>();
		for (ElementEntity element: elements)
		{
			if (checkIfElementsMadeBySpecificUserInSpecificPlayground(element, element.getCreatorPlayground(), 
					element.getCreatorPlayground()) && element.getAttributes().containsKey(attributeName)
					&& element.getAttributes().get(attributeName).equals(value))
					tempElementsList.add(new ElementTO(element));			
		}
		return tempElementsList.toArray(
				new ElementTO[tempElementsList.size()]);
	}
	
	public void updateElementInDatabaseFromExternalElement(ElementEntity element, String id, String playground) {
		
		System.out.println("Perform update");
		System.out.println("Not updated element" + this.getElement(id, playground));
		System.out.println("Updated element" + element);
		ElementEntity tempElement = this.getElement(id, playground);
				if(tempElement != null) {
			System.out.println("Elemnt by id and string"+this.getElement(id, playground).toString());
			
			System.out.println("temp element" + tempElement.toString());
			System.out.println("element" + element.toString());
			elements.remove(tempElement);
			elements.add(element);
		}
		else
			throw new ElementDataException("element data for update is incorrect");
	}
	
	public ElementTO[] getAllElementsTOInRadius(ElementTO element,double distance)
	{
		
		//find in a circle
		ArrayList<ElementTO> array=new ArrayList<>();
		for(ElementEntity el:elements) {
			double xin=el.getLocation().getX()-element.getLocation().getX();
			double yin=el.getLocation().getY()-element.getLocation().getY();
			
			if(Math.sqrt(xin*xin+yin*yin)<=distance) {
				array.add(new ElementTO(el));
			}
		}
		//element.getLocation();
//		double x=0;
//		double y=0;
//		//boundaries
//		double north=y+distance;
//		double south=y-distance;
//		double east=x-distance;
//		double west=x+distance;
		//find in double x and double y 
//		for(ElementTO el:this.elements) {
//			if(el.getLocation().getX()>=east&&el.getLocation().getX()<=west&&
//					el.getLocation().getY()>=south&&el.getLocation().getY()<=north) {
//				array.add(el);
//			}
//			
//		}
		return (ElementTO[]) array.toArray();
	}
	
	public void cleanElementService() {
		elements.clear();
	}
	
	//TODO change
//	public boolean checkElementIsCorrect(ElementEntity element) {
//		UserEntity userToCheckWith = null;
//		for (UserEntity u : users) {
//			if(u.getEmail() == element.getCreatorEmail()) {
//				userToCheckWith = u;
//				break;
//			}
//		}
//		if (userToCheckWith != null && 
//				userToCheckWith.getPlayground().equals(element.getCreatorPlayground())) 
//			return true;
//		else
//			return false;
//	}

}