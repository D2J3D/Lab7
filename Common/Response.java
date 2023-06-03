package Common;

import Common.core.SpaceMarine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Response implements Serializable {
    private final List<SpaceMarine> spaceMarines;
    private final String message;

    public Response(String message){
        this.message = message;
        this.spaceMarines = null;
    }
    public Response(String message, List<SpaceMarine> spaceMarines){
        this.message = message;
        this.spaceMarines = spaceMarines;
    }

    public String getMessage() {
        return message;
    }

    public List<SpaceMarine> getSpaceMarines() {
        return spaceMarines;
    }
}
