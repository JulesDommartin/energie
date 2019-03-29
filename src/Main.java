import data.*;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<Client> clients = Assets.getClients();
        Vehicule vehicule = Assets.getVehicule();
        Depot depot = Assets.getDepot();
        Map<Integer, List<Point>> tourneesCapacite = new HashMap<>();


        Float totalCommand = 0.0f;
        for (Client c : clients) {
            totalCommand = totalCommand + c.getDemande() ;
        }

        // Tri dans l'ordre croissant des demandes
        clients.sort(Comparator.comparingInt(Client::getDemande));

        capaciteHeuristique(clients, vehicule, depot, tourneesCapacite);


        // Affichage du rapport des tournees
        System.out.println("Nombre de tournee maximales ; " + Math.ceil(totalCommand / vehicule.getCapacity()));
        for (Map.Entry<Integer, List<Point>> entry : tourneesCapacite.entrySet()) {
            System.out.println("Tournee " + (entry.getKey() + 1) + " :");
            float totalDemandes = 0.0f;
            float totalDistance = 0.0f;
            for(Point p : entry.getValue()) {
                if (p instanceof Depot) {
                    System.out.println("Depot");
                } else {
                    Client c = (Client)p;
                    int numClient = entry.getValue().indexOf(p);
                    Point lastPoint = entry.getValue().get(numClient - 1);
                    totalDemandes += c.getDemande();
                    totalDistance += lastPoint.getDistanceTo(c);
                    System.out.println("Client " + numClient + " : [Demande = " +c.getDemande() + ", Distance = " + lastPoint.getDistanceTo(c) + ", Point = {" + c.getLatitude() + ":" + c.getLongitude() + "}]");
                }
            }
            System.out.println("Total Demande : " + totalDemandes);
            System.out.println("Total Distance : " + totalDistance);
            System.out.println("=====================================");
        }

    }

    private static void capaciteHeuristique(List<Client> clients, Vehicule vehicule, Depot depot, Map<Integer, List<Point>> tournees) {
        int distanceRestante = vehicule.getMax_dist();
        int capaciteRestante = vehicule.getCapacity();

        int numeroTournee = 0;
        int i = 0;
        while (i < clients.size()) {
            Client c = clients.get(i);

            List<Point> pointsTournee = tournees.get(numeroTournee);
            if(pointsTournee == null) {
                // Creation d'une tournee
                distanceRestante = vehicule.getMax_dist();
                capaciteRestante = vehicule.getCapacity();
                tournees.put(numeroTournee, new LinkedList());
                pointsTournee = tournees.get(numeroTournee);
                pointsTournee.add(depot);
            }

            // Verification de la capacitee restante
            if (capaciteRestante > c.getDemande()) {
                // Recuperation du lieu actuel du vehicule
                Point lastPoint = pointsTournee.get(pointsTournee.size() - 1);

                // Si l'autonomie restante est suffisante pour livrer le prochain client et retourner au depot
                boolean canShipToNextClient = distanceRestante > lastPoint.getDistanceTo(c);
                boolean canReturnToWarehouseAfterShipment = (distanceRestante - lastPoint.getDistanceTo(c)) > depot.getDistanceTo(c);
                if(canShipToNextClient && canReturnToWarehouseAfterShipment) {
                    pointsTournee.add(c);
                    if (i == clients.size() - 1) {
                        // Si tous les clients sont livrees, alors on peut retourner au depot
                        pointsTournee.add(depot);
                    }
                    capaciteRestante -= c.getDemande();
                    distanceRestante -= lastPoint.getDistanceTo(c);
                    i++;
                } else {
                    // Si l'autonomie n'est pas suffisante pour faire les deux trajet, on retourne au depot pour recharger
                    pointsTournee.add(depot);
                    numeroTournee++;
                }
            } else {
                // Si la capacitee n'est pas suffisante, on fini la tournee et on retourne au depot
                pointsTournee.add(depot);
                numeroTournee++;
            }
        }
    }
}


