package data;

public class Vehicule {
    public Integer getMax_dist() {
        return max_dist;
    }

    public void setMax_dist(Integer max_dist) {
        this.max_dist = max_dist;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCharge_fast() {
        return charge_fast;
    }

    public void setCharge_fast(Integer charge_fast) {
        this.charge_fast = charge_fast;
    }

    public Integer getCharge_midium() {
        return charge_midium;
    }

    public void setCharge_midium(Integer charge_midium) {
        this.charge_midium = charge_midium;
    }

    public Integer getCharge_slow() {
        return charge_slow;
    }

    public void setCharge_slow(Integer charge_slow) {
        this.charge_slow = charge_slow;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public static Vehicule getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(Vehicule ourInstance) {
        Vehicule.ourInstance = ourInstance;
    }

    private Integer max_dist = null;
    private Integer capacity = null;
    private Integer charge_fast = null;
    private Integer charge_midium = null;
    private Integer charge_slow = null;
    private String start_time = null;
    private String end_time = null;
    private static Vehicule ourInstance = new Vehicule();

    public static Vehicule getInstance() {
        return ourInstance;
    }

    private Vehicule() {
    }
}
