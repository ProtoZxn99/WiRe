package patrick.pramedia.wire.entitas;

/**
 * Created by PRA on 5/11/2019.
 */

public class RemoveDevice {

    private String group_id;
    private String device_id;
    private String device_name;

    public RemoveDevice() {
    }

    public RemoveDevice(String group_id, String device_id, String device_name) {
        this.group_id = group_id;
        this.device_id = device_id;
        this.device_name = device_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }
}
