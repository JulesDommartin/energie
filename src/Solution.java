import data.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class Solution {
    private Map<Integer, List<Point>> tournee;

    public List<Client> getClients() {
        return clients;
    }

    private List<Client> clients;
    private Vehicule vehicule;

    Solution(Map<Integer, List<Point>> t, List<Client> c, Vehicule v) {
        tournee = t;
        clients = c;
        vehicule = v;
    }

    Float evaluate() {
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
        Integer visiteManquantes = clients.size() - visitedClient.size();
        Long visiteMltiples = visitedClient.size() - visitedClient.stream().distinct().count();
        int contrainteDistance = 0;
        float distanceTotale = totalDistance;
        float dureeTotale = totalDuree;
        int nombreVehicule = 1;
        int contrainteQuantite = 0;
        int contrainteDuree = 0;
        return distanceTotale + dureeTotale / 600 + (nombreVehicule - 1) * 500 + contrainteDistance * 50000 + contrainteQuantite * 10000 + contrainteDuree * 1000 + 100000*(visiteManquantes + visiteMltiples);
    }

    void export() {
        System.out.println("Objectif : " + this.evaluate());
        String jsonString = "{\"tournees\": [";
        String depotString = "";
        for (Map.Entry<Integer, List<Point>> entry : tournee.entrySet()) {
            System.out.println("Tournee " + (entry.getKey() + 1) + " :");
            float totalDemandes = 0.0f;
            float totalDistance = 0.0f;
            float totalDuree    = 0.0f;
            jsonString += "{\"clients\": [";
            for(Point p : entry.getValue()) {
                if (p instanceof Depot) {
                    System.out.println("Depot");
                    depotString = "\"depot\": {\"latitude\":" + ((Depot) p).getLatitude() + ", \"longitude\": " + ((Depot) p).getLongitude() + "}";
                } else if (p instanceof Recharge) {
                    System.out.println("Recharge");
                } else {
                    Client c = (Client)p;
                    int numClient = entry.getValue().indexOf(p);
                    Point lastPoint = entry.getValue().get(numClient - 1);
                    totalDemandes += c.getDemande();
                    totalDistance += lastPoint.getDistanceTo(c);
                    totalDuree += lastPoint.getTimeTo(c) + (5 * 60) + (10 * c.getDemande());
                    System.out.println("Client " + numClient + " : [Demande = " +c.getDemande() + ", Distance = " + lastPoint.getDistanceTo(c) + ", Point = {" + c.getLatitude() + ":" + c.getLongitude() + "}]");
                    jsonString += "{\"latitude\":" + c.getLatitude() + ", \"longitude\": " + c.getLongitude() + "}";
                    if (numClient < entry.getValue().size() - 2) {
                        jsonString += ",";
                    }
                }
            }
            jsonString += "]}";
            if (entry.getKey() < tournee.size() - 1) {
                jsonString += ",";
            }
            System.out.println("Total Demande : " + totalDemandes);
            System.out.println("Total Distance : " + totalDistance);
            System.out.println("Total Temps : " + totalDuree);
            System.out.println("=====================================");
        }
        jsonString += "]," + depotString + "}";
        System.out.println(jsonString);

        try {
            Date date = new Date();
            BufferedWriter writer = new BufferedWriter(new FileWriter(date.getTime() + "_result.json"));
            writer.write(jsonString);

            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
