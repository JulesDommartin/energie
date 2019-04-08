import data.*;

import java.util.*;

// todo
// Prendre enc oompte le temps et les recharges

public class Main {

    public static void main(String[] args) {
        List<Client> clients = Assets.getClients();
        Vehicule vehicule = Assets.getVehicule();
        Depot depot = Assets.getDepot();


        Float totalCommand = 0.0f;
        for (Client c : clients) {
            totalCommand = totalCommand + c.getDemande() ;
        }

        // Tri dans l'ordre croissant des demandes
        clients.sort(Comparator.comparingInt(Client::getDemande));
        List<Client> voisinage1 = new ArrayList(clients);
        clients.sort(Comparator.comparingDouble(Client::getLatitude));
        List<Client> voisinage2 = new ArrayList(clients);
        clients.sort(Comparator.comparingDouble(Client::getLongitude));
        List<Client> voisinage3 = new ArrayList(clients);

//        Solution s1 = new Solution(capaciteHeuristique(voisinage1, vehicule, depot), clients, vehicule);
//        Solution s2 = new Solution(capaciteHeuristique(voisinage2, vehicule, depot), clients);
//        Solution s3 = new Solution(capaciteHeuristique(voisinage3, vehicule, depot), clients);

        int nbIteration = 3;

        Solution s1 = getSolutionFromVoisinage(voisinage1, vehicule, depot, clients, nbIteration);
        s1.export();

        // List<Client> voisinage1voisin1 = trouverVoisin(voisinage1);
        // Solution s1v1 = new Solution(capaciteHeuristique(voisinage1voisin1, vehicule, depot), clients, vehicule);

        // if(s1.evaluate() < s1v1.evaluate()){
        //     Solution s1v2 = new Solution(capaciteHeuristique(trouverVoisin(voisinage1), vehicule, depot), clients, vehicule);
        //     if(s1.evaluate() < s1v2.evaluate()) {
        //         s1.export();
        //     } else {
        //         s1v2.export();
        //     }
        // } else {
        //     Solution s1v2 = new Solution(capaciteHeuristique(trouverVoisin(voisinage1voisin1), vehicule, depot), clients, vehicule);
        //     if(s1v1.evaluate() < s1v2.evaluate()) {
        //         s1v1.export();
        //     } else {
        //         s1v2.export();
        //     }
        // }

    }

    private static Solution getSolutionFromVoisinage(List<Client> voisinage, Vehicule vehicule, Depot depot, List<Client> clients, int nbIteration) {
        System.out.println(nbIteration);
        Solution s = new Solution(capaciteHeuristique(voisinage, vehicule, depot), clients, vehicule);
        if (nbIteration > 0) {
            List<Client> voisinageSuivant = trouverVoisin(voisinage);
            Solution s1 = new Solution(capaciteHeuristique(voisinageSuivant, vehicule, depot), clients, vehicule);
            nbIteration--;
            if (s.evaluate() > s1.evaluate()) {
                return getSolutionFromVoisinage(voisinageSuivant, vehicule, depot, clients, nbIteration);
            } else {
                return getSolutionFromVoisinage(voisinage, vehicule, depot, clients, nbIteration);
            }
        } else {
            return s;
        }
    } 

    private static Map<Integer, List<Point>> capaciteHeuristique(List<Client> clients, Vehicule vehicule, Depot depot) {
        Map<Integer, List<Point>>  tournees = new HashMap<>();

        int distanceRestante = vehicule.getMax_dist();
        int capaciteRestante = vehicule.getCapacity();
        int tempsRestant = 12 * 60 * 60;

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
                boolean hasTimeToGoToNextClient = tempsRestant > lastPoint.getTimeTo(c);
                boolean hasTimeToReturnToWarehouseAfterShipment = (tempsRestant - lastPoint.getTimeTo((c)) > depot.getTimeTo(c));
                if(canShipToNextClient &&
                        canReturnToWarehouseAfterShipment &&
                        hasTimeToGoToNextClient &&
                        hasTimeToReturnToWarehouseAfterShipment) {
                    pointsTournee.add(c);
                    if (i == clients.size() - 1) {
                        // Si tous les clients sont livrees, alors on peut retourner au depot
                        pointsTournee.add(depot);
                    }
                    capaciteRestante -= c.getDemande();
                    distanceRestante -= lastPoint.getDistanceTo(c);
                    tempsRestant -= (lastPoint.getTimeTo(c) + (5 * 60) + (10 * c.getDemande()));
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
        return tournees;
    }

    private static List<Client> trouverVoisin(List<Client> clients) {
        ArrayList result = new ArrayList(clients);

        int index1 = (int)Math.floor(Math.random() * clients.size());
        int index2 = (int)Math.floor(Math.random() * clients.size());
        while (index1 == index2){
            index2 = (int)Math.floor(Math.random() * clients.size());
        }

        Collections.swap(result, index1, index2);
        return result;
    }
}

