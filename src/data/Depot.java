package data;

import java.util.HashMap;
import java.util.Map;

public class Depot implements Point {
    public Double getLatitude() {
        return latitude;
    }

    void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    void setTimeTo(Client c, Integer time) {
        times.putIfAbsent(c, time);
    }

    public Integer getTimeTo(Client c) {
        return times.get(c);
    }
    public Double getDistanceTo(Client c) {
        return distances.get(c);
    }

    void setDistanceTo(Client c, Double distance) {
        distances.putIfAbsent(c, distance);
    }

    public Map<Client, Integer> getTimes() {
        return times;
    }

    public Map<Client, Double> getDistances() {
        return distances;
    }

    private Map<Client, Integer> times = new HashMap<>();
    private Map<Client, Double> distances = new HashMap<>();
    private Double latitude;
    private Double longitude;

    public Depot() {
    }

    Depot(Depot d) {
        times = new HashMap<>(d.times);
        distances = new HashMap<>(d.distances);
        latitude = d.latitude;
        longitude = d.longitude;
    }

}
