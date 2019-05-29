package patrick.pramedia.wire.entitas;

public class EntitasDevice {

    private String id;
    private String nama;
    private boolean status_alat;

    public EntitasDevice() {
    }

    public EntitasDevice(String id, String nama, boolean status_alat) {
        this.id = id;
        this.nama = nama;
        this.status_alat = status_alat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public boolean getStatus_alat() {
        return status_alat;
    }

    public void setStatus_alat(boolean status_alat) {
        this.status_alat = status_alat;
    }
}
