import data.*;

import java.util.*;

// todo
// generer la liste des voisins a partir d'une solution -> Voir trouverVoisin2
// trouver differentes manieres de generer des voisins : swap de 2 clients, essayer de rajouter un client avant d'aller au depot, essayer de rajouter un client avant d' aller a la recharge
// Utiliser la liste de client normale et la liste randomisée avec shuffledClient

public class Main {

    public static void main(String[] args) {
        List<Client> clients = Assets.getClients();
        Vehicule vehicule = Assets.getVehicule();
        Depot depot = Assets.getDepot();

        Float totalCommand = 0.0f;
        for (Client c : clients) {
            totalCommand = totalCommand + c.getDemande();
        }

        // Tri dans l'ordre croissant des demandes
        clients.sort(Comparator.comparingInt(Client::getDemande));
        // Liste Random
        List<Client> shuffledClient = Assets.getClients();
        Collections.shuffle(shuffledClient);

        List<Client> voisinage1 = new ArrayList(clients);

        int nbIteration = 3;

        Solution s1 = getSolutionFromVoisinage(voisinage1, vehicule, depot, clients, nbIteration);
        // s1.export();
        Solution s2 = getSolutionFromVoisinage2(voisinage1, vehicule, depot, clients, nbIteration);
        s2.export();

    }

    private static Solution getSolutionFromVoisinage(List<Client> voisinage, Vehicule vehicule, Depot depot, List<Client> clients, int nbIteration) {
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
    private static Solution getSolutionFromVoisinage2(List<Client> voisinage, Vehicule vehicule, Depot depot, List<Client> clients, int nbIteration) {
        Solution s = new Solution(capaciteHeuristique(voisinage, vehicule, depot), clients, vehicule);
        if (nbIteration > 0) {
            nbIteration--;
            List<List<Client>> voisinagesSuivants = trouverVoisin2(voisinage);
            List<Solution> solutions = new ArrayList();
            for(List<Client> c : voisinagesSuivants) {
                solutions.add(new Solution(capaciteHeuristique(c, vehicule, depot),c, vehicule));
            }
            Solution bestSolution = s;
            for(Solution solution : solutions) {
                if(bestSolution.evaluate() > solution.evaluate()) bestSolution = solution;
            }
            if(bestSolution == s) return s;
            else return getSolutionFromVoisinage2(bestSolution.getClients(),vehicule, depot, clients, nbIteration);
        } else {
            return s;
        }
    }

    private static Map<Integer, List<Point>> capaciteHeuristique(List<Client> clients, Vehicule vehicule, Depot depot) {
        Map<Integer, List<Point>> tournees = new HashMap<>();

        int distanceRestante = vehicule.getMax_dist();
        int capaciteRestante = vehicule.getCapacity();
        int tempsRestant = 12 * 60 * 60;

        int numeroTournee = 0;
        int i = 0;
        while (i < clients.size()) {
            Client c = clients.get(i);

            List<Point> pointsTournee = tournees.get(numeroTournee);
            if (pointsTournee == null) {
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
                boolean canReturnToWarehouseAfterShipment = (distanceRestante - lastPoint.getDistanceTo(c)) > depot.getDistanceTo(c);
                boolean hasTimeToReturnToWarehouseAfterShipment = (tempsRestant - lastPoint.getTimeTo((c)) > depot.getTimeTo(c));
                if (canReturnToWarehouseAfterShipment && hasTimeToReturnToWarehouseAfterShipment) {
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
                    // Si l'autonomie n'est pas suffisante pour faire les deux trajet, on recharge
                    if (!canReturnToWarehouseAfterShipment && hasTimeToReturnToWarehouseAfterShipment) {
                        pointsTournee.add(new Recharge());
                        distanceRestante = vehicule.getMax_dist();
                        tempsRestant -= vehicule.getCharge_fast();
                        numeroTournee++;
                    } else {
                        // Sinon le temps restant n'est pas suffisant, on rentre au depot
                        pointsTournee.add(new Depot());
                        numeroTournee++;
                    }
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

        int index1 = (int) Math.floor(Math.random() * clients.size());
        int index2 = (int) Math.floor(Math.random() * clients.size());
        while (index1 == index2) {
            index2 = (int) Math.floor(Math.random() * clients.size());
        }

        Collections.swap(result, index1, index2);
        return result;
    }

    private static List<List<Client>> trouverVoisin2(List<Client> clients) {
        List<List<Client>> result = new ArrayList();
        int index1, index2;

        for (int i = 0; i < clients.size() - 1; i++) {
            index1 = i;
            if (i == clients.size() -1) {
                index2 = 0;
            } else {
                index2 = i +1;
            }
            List<Client> voisin = new ArrayList<>(clients);
            Collections.swap(voisin, index1, index2);
            result.add(voisin);
        }
        return result;
    }

}

