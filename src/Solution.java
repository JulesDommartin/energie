import data.Client;
import data.Depot;
import data.Point;
import data.Vehicule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Solution {
    private Float distanceTotale;
    private Float dureeTotale;
    private Integer nombreVehicule;
    private Integer contrainteDistance;
    private Integer contrainteQuantite;
    private Integer contrainteDuree;
    private Integer visiteManquantes;
    private Long visiteMltiples;
    Map<Integer, List<Point>> tournee;
    List<Client> clients;
    Vehicule vehicule;

    Solution(Map<Integer, List<Point>> t, List<Client> c, Vehicule v) {
        tournee = t;
        clients = c;
        vehicule = v;
    }

    public Float evaluate() {
        List<Client> visitedClient = new ArrayList<>();
        float totalDistance = 0.0f;
        float totalDuree = 0.0f;
        for (Map.Entry<Integer, List<Point>> entry : tournee.entrySet()) {
            for(Point p : entry.getValue()) {
                if (p instanceof Client) {
                    Client c = (Client)p;
                    visitedClient.add(c);
                    int numClient = entry.getValue().indexOf(p);
                    Point lastPoint = entry.getValue().get(numClient - 1);
                    totalDistance += lastPoint.getDistanceTo(c);
                    totalDuree += lastPoint.getTimeTo(c) + (5 * 60) + (10 * c.getDemande());
                }
            }
        }
        visiteManquantes = clients.size() - visitedClient.size();
        visiteMltiples = visitedClient.size() - visitedClient.stream().distinct().count();
        contrainteDistance = 0;
        distanceTotale = totalDistance;
        dureeTotale = totalDuree;
        nombreVehicule = 1;
        contrainteQuantite = 0;
        contrainteDuree = 0;
        return distanceTotale + dureeTotale / 600 + (nombreVehicule - 1) * 500 + contrainteDistance * 50000 + contrainteQuantite * 10000 + contrainteDuree * 1000 + 100000*(visiteManquantes + visiteMltiples);
    }

    public void export() {
        System.out.println("Objectif : " + this.evaluate());
        for (Map.Entry<Integer, List<Point>> entry : tournee.entrySet()) {
            System.out.println("Tournee " + (entry.getKey() + 1) + " :");
            float totalDemandes = 0.0f;
            float totalDistance = 0.0f;
            float totalDuree    = 0.0f;
            for(Point p : entry.getValue()) {
                if (p instanceof Depot) {
                    System.out.println("Depot");
                } else {
                    Client c = (Client)p;
                    int numClient = entry.getValue().indexOf(p);
                    Point lastPoint = entry.getValue().get(numClient - 1);
                    totalDemandes += c.getDemande();
                    totalDistance += lastPoint.getDistanceTo(c);
                    totalDuree += lastPoint.getTimeTo(c) + (5 * 60) + (10 * c.getDemande());
                    System.out.println("Client " + numClient + " : [Demande = " +c.getDemande() + ", Distance = " + lastPoint.getDistanceTo(c) + ", Point = {" + c.getLatitude() + ":" + c.getLongitude() + "}]");
                }
            }
            System.out.println("Total Demande : " + totalDemandes);
            System.out.println("Total Distance : " + totalDistance);
            System.out.println("Total Temps : " + totalDuree);
            System.out.println("=====================================");
        }

    }
}
