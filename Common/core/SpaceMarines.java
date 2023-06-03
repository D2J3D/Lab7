package Common.core;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SpaceMarines implements Serializable {
    private List<SpaceMarine> synchronizedMarines;
    private int idOfCollection;
    private final ZonedDateTime creationDate = ZonedDateTime.now();

    public SpaceMarines(List<SpaceMarine> synchronizedMarines){
        this.synchronizedMarines = synchronizedMarines;
    }
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }
    public List<SpaceMarine> getSynchronizedMarines() {
        return synchronizedMarines;
    }

    public int getIdOfCollection() {
        return idOfCollection;
    }

    public void setIdOfCollection(int idOfCollection) {
        this.idOfCollection = idOfCollection;
    }
}
