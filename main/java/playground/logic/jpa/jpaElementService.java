package playground.logic.jpa;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import playground.dal.ElementDao;
import playground.logic.ElementEntity;
import playground.logic.ElementService;
//elia:
//to switch the service we need firstly to go to DummyElementService and remove the @Service there
//@Service
public class jpaElementService implements ElementService {
	
	//this is the database we need are saving in
	private  ElementDao elementsDB;
	
	@Autowired
	public jpaElementService(ElementDao elementsDB) {
		this.elementsDB=elementsDB;
		
	}
	
	

	@Override
	public void cleanElementService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional(readOnly=true)
	public ElementEntity[] getAllElementsInRadius(ElementEntity element, double x, double y, double distance, int page,
			int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isElementInDatabase(ElementEntity element) {
		
		return this.elementsDB.existsById(element.getId());
	}

	@Override
	public void addElements(ElementEntity[] elements, String userPlayground) {
		for(int i=0;i<elements.length;i++) {
			elementsDB.save(elements[i]);
		}
		
		
	}

	@Override
	public void updateElementInDatabaseFromExternalElement(ElementEntity element, String userPlayground,
			String playground, String id) {
		//todo
		 elementsDB.deleteById(id);
		 elementsDB.save(element);
		
	}

	@Override
	public ElementEntity[] getElementsWithValueInAttribute(String creatorPlayground, String creatorEmail,
			String attributeName, String value, int page, int size) {
		ArrayList<ElementEntity> arr=(ArrayList<ElementEntity>) elementsDB.findAll();
		ArrayList<ElementEntity> arrReturned=new ArrayList<ElementEntity>();
		for(ElementEntity el:arr) {
			if(el.getCreatorEmail().equals(creatorEmail)&&el.getCreatorPlayground().equals(creatorPlayground)) {
				//todo
				//understand what is exactly value and attributename
				arrReturned.add(el);
			}
		}
		return (ElementEntity[]) arrReturned.toArray() ;
		
	}

	@Override
	public boolean checkEmailAndPlaygroundInElement(ElementEntity element, String creatorPlayground,
			String creatorEmail) {
		Optional<ElementEntity> el=elementsDB.findById(element.getId());
		if(el.isPresent()) {
			ElementEntity elementI=el.get();
			if(elementI.getCreatorEmail().equals(creatorEmail)&&elementI.getCreatorPlayground().equals(creatorPlayground)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public ElementEntity[] getElementsByCreatorPlaygroundAndEmail(String creatorPlayground, String email, int page,
			int size) {
		ArrayList<ElementEntity> arr=(ArrayList<ElementEntity>) elementsDB.findAll();
		ArrayList<ElementEntity> arrReturned=new ArrayList<ElementEntity>();
		for(ElementEntity el:arr) {
			if(el.getCreatorEmail().equals(email)&&el.getCreatorPlayground().equals(creatorPlayground)) {
				arrReturned.add(el);
			}
		}
		return (ElementEntity[]) arrReturned.toArray() ;
	}

	@Override
	public ElementEntity getElement(String id, String playground) {
		Optional<ElementEntity> el=elementsDB.findById(id);
		if(el.isPresent()) {
			ElementEntity elementI=el.get();
			if(elementI.getCreatorPlayground().equals(playground)) {
				return elementI;
			}
		}
		
		return null;
	}

	@Override
	public void addElement(ElementEntity element) {
		elementsDB.save(element);
		
	}

	@Override
	public ArrayList<ElementEntity> getElements() {
		
		return (ArrayList<ElementEntity>) elementsDB.findAll();
	}

	@Override
	public ElementEntity[] getElementsBySizeAndPage(ArrayList<ElementEntity> lst, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateElementsInDatabase(ArrayList<ElementEntity> elements, String playground) {
		for(ElementEntity el:elements) {
			if(elementsDB.existsById(el.getId())) {
				elementsDB.deleteById(el.getId());
				elementsDB.save(el);
			}
		}
		
	}

	@Override
	public ElementEntity[] getAllElements() {
		
		return (ElementEntity[])this.getElements().toArray();
	}

}