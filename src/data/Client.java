package data;

import java.util.HashMap;
import java.util.Map;

public class Client implements Point {
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

    public Integer getDemande() {
        return demande;
    }

    public Integer getTimeTo(Client c) {
        return times.get(c);
    }
    public Double getDistanceTo(Client c) {
        return distances.get(c);
    }

    void setTimeTo(Client c, Integer time) {
        times.putIfAbsent(c, time);
    }
    void setDistanceTo(Client c, Double distance) {
        distances.putIfAbsent(c, distance);
    }

    void setDemmande(Integer demande) {
        this.demande = demande;
    }

    public Map<Client, Double> getDistances() {
        return distances;
    }

    public Map<Client, Integer> getTimes() {
        return times;
    }

    private Double latitude;
    private Double longitude;
    private Integer demande;
    private Map<Client, Double> distances = new HashMap<>();
    private Map<Client, Integer> times = new HashMap<>();

}
