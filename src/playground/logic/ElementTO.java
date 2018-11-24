package playground.logic;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ElementTO implements Serializable{

	private static final long serialVersionUID = 1L;
	protected String name;
	protected String id;
	protected String playground;
	protected Date creationDate;
	protected Date expirationDate;
	protected String type;
	protected String creatorPlayground;
	protected String creatorEmail;
	protected Location location;
	protected Map<String, Object>attributes = Collections.synchronizedMap(new HashMap<>()); 
	
	public boolean attributeExists(String attributeName, String value) {
		switch(attributeName)
		{
		case "name": return name.equals(value);
		case "id": return id.equals(value);
		case "playground": return playground.equals(value);
		case "type": return type.equals(value);
		case "creatorPlayground": return creatorPlayground.equals(value);
		case "creatorEmail": return creatorEmail.equals(value);
		
		case "creationDate": return (new Date(value)).equals(creationDate);
		case "expirationDate": return (new Date(value)).equals(expirationDate);
		case "location": return (new Location(value)).equals(location);
		}
		return false;
	}
	
	public ElementTO(ElementEntity e)
	{
		this.setPlayground(e.getPlayground());
		this.setAttributes(e.getAttributes());
		this.setCreationDate(e.getCreationDate());
		this.setCreatorEmail(e.getCreatorEmail());
		this.setCreatorPlayground(e.getCreatorPlayground());
		this.setExpirationDate(e.getExpirationDate());
		this.setType(e.getType());
		this.setId(e.getId());
		this.setLocation(e.getLocation());
		this.setName(e.getName());
	}
	
	public ElementTO(String id, String playground, String creatorPlayground, String creatorEmail) {
		// this constructor is used for /playground/elements/{userPlayground}/{email}/{playground}/{id} 
		//which won't pass expirationDate, name, type and location
		// TODO add expirationDate, location implementation
		super();
//		this.name = name;
		this.id = id;
		this.playground = playground;
		this.creationDate = new Date();
//		this.expirationDate = expirationDate;
//		this.type = type;
		this.creatorPlayground = creatorPlayground;
		this.creatorEmail = creatorEmail;
//		this.attributes ;
//		this.location = location;
		
	}
	
	public ElementTO() {
		// TODO Auto-generated constructor stub
	}
	


	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlayground() {
		return playground;
	}
	public void setPlayground(String playground) {
		this.playground = playground;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getExirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date exirationDate) {
		this.expirationDate = exirationDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreatorPlayground() {
		return creatorPlayground;
	}
	public void setCreatorPlayground(String creatorPlayground) {
		this.creatorPlayground = creatorPlayground;
	}
	public String getCreatorEmail() {
		return creatorEmail;
	}
	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public String toString() {
		return "ElementEntity [name=" + name + ", id=" + id + ", playground=" + playground + ", creationDate="
				+ creationDate + ", exirationDate=" + expirationDate + ", type=" + type + ", creatorPlayground="
				+ creatorPlayground + ", creatorEmail=" + creatorEmail + ", location=" + location + "]";
	}
}