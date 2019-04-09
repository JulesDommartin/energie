package data;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Assets {
    private static String instance = "instance_0";

    private static ArrayList<Client> clients;

    private static Depot depot;
    private static Vehicule vehicule;

    static {
        try {
            new Assets();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Vehicule getVehicule() {
        return new Vehicule(vehicule);
    }

    public static ArrayList<Client> getClients() {
        return new ArrayList<>(clients);
    }

    public static Depot getDepot() {
        return new Depot(depot);
    }

    private Assets() throws Exception {
        // Loading files and creating buffers
        FileReader demandesAssets = new FileReader("./out/production/energie/assets/" + instance + "/demandes.txt");
        FileReader tempsAssets = new FileReader("./out/production/energie/assets/" + instance + "/times.txt");
        FileReader distanceAssets = new FileReader("./out/production/energie/assets/" + instance + "/distances.txt");
        FileReader coordonneesAssets = new FileReader("./out/production/energie/assets/" + instance + "/coords.txt");
        FileReader vehiculeAssets = new FileReader("./out/production/energie/assets/" + instance + "/vehicle.ini");
        BufferedReader demandeBufRead = new BufferedReader(demandesAssets);
        BufferedReader coordBufRead = new BufferedReader(coordonneesAssets);
        BufferedReader tempsBufRead = new BufferedReader(tempsAssets);
        BufferedReader distancesBufRead = new BufferedReader(distanceAssets);
        BufferedReader vehicleBufRead = new BufferedReader(vehiculeAssets);

        // Intermediate variables
        List<Integer> demandes = new ArrayList<>();
        List<Pair<Double, Double>> coordonees = new ArrayList<>();
        List<Integer[]> temps = new ArrayList<>();
        List<Double[]> distances = new ArrayList<>();
        Pair<Double, Double> coordoneesDepot = null;
        Integer[] tempsDepot = null;
        Double[] distancesDepot = null;


        readDemands(demandeBufRead, demandes);
        coordoneesDepot = readCoordonnees(coordBufRead, coordonees);
        tempsDepot = readTimes(tempsBufRead, temps);
        distancesDepot = readDistances(distancesBufRead, distances);
        vehicule = readVehicule(vehicleBufRead);

        if (coordonees.size() != demandes.size()) throw new Exception("Assets non valide.");
        if (coordonees.size() != temps.size()) throw new Exception("Assets non valide.");
        if (coordoneesDepot == null) throw new Exception("Assets non valide.");
        if (tempsDepot == null) throw new Exception("Assets non valide.");
        if (distancesDepot == null) throw new Exception("Assets non valide.");
        if (vehicule == null) throw new Exception("Assets non valide.");

        createDepot(coordoneesDepot);
        createClients(demandes, coordonees);
        createDepotTimeAndDistances(tempsDepot, distancesDepot);
        createClientsTimes(temps);
        createClientsDistances(distances);
    }

    private void createDepotTimeAndDistances(Integer[] times, Double[] distances) {
        for (int i = 0; i < times.length - 1; i++)
            depot.setTimeTo(clients.get(i), times[i]);
        for (int i = 0; i < distances.length - 1; i++)
            depot.setDistanceTo(clients.get(i), distances[i]);
    }

    private Vehicule readVehicule(BufferedReader vehicleBufRead) throws IOException {
        String myLine;
        Vehicule result = null;
        Integer max_dist = null;
        Integer capacity = null;
        Integer charge_fast = null;
        Integer charge_midium = null;
        Integer charge_slow = null;
        String start_time = null;
        String end_time = null;
        while ((myLine = vehicleBufRead.readLine()) != null) {
            String[] strings = myLine.split(" = ");
            if (strings.length == 2) {
                switch (strings[0]) {
                    case "max_dist":
                        max_dist = Integer.valueOf(strings[1]);
                        break;
                    case "capacity":
                        capacity = Integer.valueOf(strings[1]);
                        break;
                    case "charge_fast":
                        charge_fast = Integer.valueOf(strings[1]);
                        break;
                    case "charge_medium":
                        charge_midium = Integer.valueOf(strings[1]);
                        break;
                    case "charge_slow":
                        charge_slow = Integer.valueOf(strings[1]);
                        break;
                    case "start_time":
                        start_time = strings[1];
                        break;
                    case "end_time":
                        end_time = strings[1];
                        break;
                }
            }
        }
        if (max_dist != null && capacity != null && charge_fast != null && charge_midium != null && charge_slow != null && start_time != null && end_time != null) {
            result = Vehicule.getInstance();
            result.setMax_dist(max_dist);
            result.setCapacity(capacity);
            result.setCharge_fast(charge_fast);
            result.setCharge_medium(charge_midium);
            result.setCharge_slow(charge_slow);
            result.setStart_time(start_time);
            result.setEnd_time(end_time);
        }
        return result;
    }

    private void createClientsDistances(List<Double[]> distances) {
        for (int i = 0; i < clients.size(); i++) {
            Client currentClient = clients.get(i);
            Double[] dists = distances.get(i);
            for (int j = 0; j < dists.length - 1; j++) {
                Client client = clients.get(j);
                currentClient.setDistanceTo(client, dists[j]);
            }
        }
    }

    private void createClientsTimes(List<Integer[]> temps) {
        for (int i = 0; i < clients.size(); i++) {
            Client currentClient = clients.get(i);
            Integer[] times = temps.get(i);
            for (int j = 0; j < times.length - 1; j++) {
                Client client = clients.get(j);
                currentClient.setTimeTo(client, times[j]);
            }
        }
    }

    private Integer[] readTimes(BufferedReader tempsBufRead, List<Integer[]> temps) throws IOException {
        String myLine;
        Integer[] result = null;
        while ((myLine = tempsBufRead.readLine()) != null) {
            String[] strings = myLine.split(" ");
            temps.add(StringArrToIntegerArr(strings));
        }
        int lastIndex = temps.size() -1;
        result = temps.get(lastIndex);
        temps.remove(lastIndex);
        return result;
    }

    private Double[] readDistances(BufferedReader distanceBufRead, List<Double[]> distances) throws IOException {
        String myLine;
        Double[] result = null;
        while ((myLine = distanceBufRead.readLine()) != null) {
            String[] strings = myLine.split(" ");
            distances.add(StringArrToDoubleArr(strings));
        }
        int lastIndex = distances.size() -1;
        result = distances.get(lastIndex);
        distances.remove(lastIndex);
        return result;
    }

    private Pair<Double, Double> readCoordonnees(BufferedReader coordBufRead, List<Pair<Double, Double>> coordonees) throws IOException {
        Pair<Double, Double> result = null;
        String myLine;
        while ((myLine = coordBufRead.readLine()) != null) {
            String[] strings = myLine.split(",");
            if (strings.length == 2) {
                coordonees.add(new Pair<>(Double.parseDouble(strings[0]), Double.parseDouble(strings[1])));
            }
        }
        int lastIndex = coordonees.size() -1;
        result = coordonees.get(lastIndex);
        coordonees.remove(lastIndex);
        return result;
    }

    private void readDemands(BufferedReader demandeBufRead, List<Integer> demandes) throws IOException {
        String myLine;
        while ((myLine = demandeBufRead.readLine()) != null) {
            demandes.add(Integer.parseInt(myLine));
        }
    }

    private void createClients(List<Integer> demandes, List<Pair<Double, Double>> coordonees) {
        clients = new ArrayList<>();
        for (int i = 0; i < coordonees.size(); i++) {
            Pair<Double, Double> c = coordonees.get(i);
            Integer demande = demandes.get(i);
            Client client = new Client();
            client.setLatitude(c.getKey());
            client.setLongitude(c.getValue());
            client.setDemmande(demande);
            clients.add(client);
        }
    }

    private void createDepot(Pair<Double, Double> coordonees) {
        depot = new Depot();
        depot.setLatitude(coordonees.getKey());
        depot.setLongitude(coordonees.getValue());
    }

    private static Integer[] StringArrToIntegerArr(String[] s) {
        Integer[] result = new Integer[s.length];
        for (int i = 0; i < s.length; i++) {
            result[i] = Integer.valueOf(s[i]);
        }
        return result;
    }

    private static Double[] StringArrToDoubleArr(String[] s) {
        Double[] result = new Double[s.length];
        for (int i = 0; i < s.length; i++) {
            result[i] = Double.valueOf(s[i]);
        }
        return result;
    }
}