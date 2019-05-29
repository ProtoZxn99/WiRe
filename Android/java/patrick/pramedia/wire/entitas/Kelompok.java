package patrick.pramedia.wire.entitas;

/**
 * Created by munil on 4/13/2019.
 */

public class Kelompok {

    private String id_kelompok;
    private String nama_kelompok;
    private boolean status_group;

    public String getId_kelompok() {
        return id_kelompok;
    }

    public void setId_kelompok(String id_kelompok) {
        this.id_kelompok = id_kelompok;
    }

    public String getNama_kelompok() {
        return nama_kelompok;
    }

    public void setNama_kelompok(String nama_kelompok) {
        this.nama_kelompok = nama_kelompok;
    }

    public boolean getStatus_group() {
        return status_group;
    }

    public void setStatus_group(boolean status_group) {
        this.status_group = status_group;
    }
}
