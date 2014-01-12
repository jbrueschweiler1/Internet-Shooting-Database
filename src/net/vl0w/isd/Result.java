package net.vl0w.isd;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Result {

	private Date date;
	private Weapon weapon;
	private String description;
	private Map<Segment, List<Shot>> shotMap;
	private boolean decimalsAllowed;

	Result(Weapon weapon) {
		this.weapon = weapon;
		this.shotMap = new HashMap<Segment, List<Shot>>();
		this.decimalsAllowed = true;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public boolean decimalsAllowed() {
		return decimalsAllowed;
	}

	public String getDescription() {
		return description;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public double getResult() {
		return getResult(Position.UNKNOWN);
	}

	public double getResult(Position position) {
		float result = 0;
		for (List<Shot> shotsInSegment : shotMap.values()) {
			for (Shot shot : shotsInSegment) {
				result += shot.getValue();
			}
		}
		return result;
	}

	public Set<Segment> getSegments() {
		return shotMap.keySet();
	}

	public void setShot(int shotNumber, double value) throws DataException {
		if (getSegments().size() > 1) {
			throw new DataException("Specify the segment your shot belongs to");
		}

		Segment segment = getSegments().iterator().next();
		setShot(segment, shotNumber, value);
	}

	public void setShot(Segment segment, int shotNumber, double value)
			throws DataException {
		Shot newShotValue = new Shot(value);

		if (newShotValue.hasDecimals() && !decimalsAllowed) {
			throw new DataException(
					"This result does not allow decimal results");
		}

		getShot(segment, shotNumber).setValue(newShotValue);
	}

	public Shot getShot(Segment segment, int shotNumber) throws DataException {
		if (shotNumber < 0 || shotNumber > segment.getShotCount()) {
			throw new DataException("Illegal shot number " + shotNumber
					+ " in segment " + segment);
		}

		List<Shot> shots = getShots(segment);
		return shots.get(shotNumber - 1);
	}

	public List<Shot> getShots(Segment segment) throws DataException {
		if (!shotMap.containsKey(segment)) {
			throw new DataException("Unknown segment");
		}

		return shotMap.get(segment);
	}

	void addSegment(Segment segment) throws DataException {
		// Init a shot list with shots with value 0
		List<Shot> shotList = new ArrayList<>();
		for (int i = 0; i < segment.getShotCount(); i++) {
			shotList.add(new Shot(0));
		}

		shotMap.put(segment, shotList);
	}

	void setDecimalsAllowed(boolean value) {
		this.decimalsAllowed = value;
	}

	void setDescription(String description) {
		this.description = description;
	}
}
