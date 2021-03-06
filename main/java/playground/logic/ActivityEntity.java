package playground.logic;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.databind.ObjectMapper;
import playground.constants.Activity;
import playground.constants.Playground;

@Entity
@Table(name = "ACTIVITY")
public class ActivityEntity {

	// Primary key - playground+id
	private static final long serialVersionUID = 514354009958930154L;
	private String playground;
	private String id = " ";
	private String elementPlayground;
	private String elementId;
	private String type;
	private String playerPlayground;
	private String playerEmail;
	private String superkey;
	private Map<String, Object> attribute;

	public ActivityEntity() {
		attribute = new HashMap<String, Object>();
		this.type = Activity.DEFAULT_ACTIVITY_TYPE;
		this.playground = Playground.PLAYGROUND_NAME;
	}

	public ActivityEntity(String jsonString) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ActivityEntity acEntity = objectMapper.readValue(jsonString, ActivityEntity.class);
			this.id = acEntity.id;
			this.playground = acEntity.playground;
			this.elementPlayground = acEntity.elementPlayground;
			this.elementId = acEntity.elementId;
			this.type = acEntity.type;
			this.playerPlayground = acEntity.playerPlayground;
			this.playerEmail = acEntity.playerEmail;
			this.attribute = acEntity.attribute;
		} catch (Exception e) {
			throw new ActivityDataException(e.getMessage());
		}

	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getElementPlayground() {
		return elementPlayground;
	}

	public void setElementPlayground(String elementPlayground) {
		this.elementPlayground = elementPlayground;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlayerPlayground() {
		return playerPlayground;
	}

	public void setPlayerPlayground(String playerPlayground) {
		this.playerPlayground = playerPlayground;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	@Transient
	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	@Lob
	// large object - can take as much space as it needs in the computer
	public String getJsonAttributes() {
		try {
			return new ObjectMapper().writeValueAsString(this.attribute);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setJsonAttributes(String jsonAttributes) {
		try {
			this.attribute = new ObjectMapper().readValue(jsonAttributes, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Id
	public String getSuperkey() {
		return id.concat(" " + playground);
	}

	public void setSuperkey(String Superkey) {
		// empty
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((superkey == null) ? 0 : superkey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityEntity other = (ActivityEntity) obj;
		if (this.getSuperkey() == null) {
			if (other.getSuperkey() != null)
				return false;
		} else if (!this.getSuperkey().equals(other.getSuperkey()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityEntity [playground=" + playground + ", id=" + id + ", elementPlayground=" + elementPlayground
				+ ", elementId=" + elementId + ", type=" + type + ", playerPlayground=" + playerPlayground
				+ ", playerEmail=" + playerEmail + ", superkey=" + superkey + ", attribute=" + attribute + "]";
	}

}
