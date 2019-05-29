package patrick.pramedia.wire.entitas;

/**
 * Created by PRA on 5/11/2019.
 */

public class Timer {

    private String id;
    private String name;
    private boolean state;

    public Timer() {
    }

    public Timer(String id, String name, boolean state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
