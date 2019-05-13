import data.*;

import java.util.*;

// todo
// generer la liste des voisins a partir d'une solution -> Voir trouverVoisin2
// trouver differentes manieres de generer des voisins : swap de 2 clients, essayer de rajouter un client avant d'aller au depot, essayer de rajouter un client avant d' aller a la recharge
// Utiliser la liste de client normale et la liste randomisée avec shuffledClient

public class Main {

    public enum TrouverVoisinType {
        METHODE_1,
        METHODE_2,
        METHODE_3
    };

    public static void main(String[] args) {
        // Cette variable est utilisée comme référentiel de base.
        List<Client> clients = Assets.getClients();
        Vehicule vehicule = Assets.getVehicule();
        Depot depot = Assets.getDepot();

        // Tri dans l'ordre croissant des demandes
        clients.sort(Comparator.comparingInt(Client::getDemande));

        // Liste 1 (Déterministe)
        List<Client> voisinage1 = new ArrayList(clients);
        
        // Liste 2 Random (non déterministe)
        List<Client> shuffledClient2 = Assets.getClients();
        Collections.shuffle(shuffledClient2);

        // Liste 3 Random (non déterministe)
        List<Client> shuffledClient3 = Assets.getClients();
        Collections.shuffle(shuffledClient3);


        int nbIteration = 3;

        Solution s1 = getSolution(voisinage1, vehicule, depot, clients, nbIteration, TrouverVoisinType.METHODE_1);
        s1.export();
        Solution s2 = getSolution(shuffledClient2, vehicule, depot, clients, nbIteration, TrouverVoisinType.METHODE_2);
        s2.export();
        Solution s3 = getSolution(shuffledClient3, vehicule, depot, clients, nbIteration, TrouverVoisinType.METHODE_3);
        s3.export();

    }

    private static Solution getSolution(List<Client> voisinage, Vehicule vehicule, Depot depot, List<Client> clients, int nbIteration, TrouverVoisinType typeVoisin) {
        // On détermine la valeur de la première solution qu'on va chercher à optimiser avec son voisinage.
        Solution s = new Solution(capaciteHeuristique(voisinage, vehicule, depot), clients, vehicule);
        
        // Notre méthode est récursive, on cherche donc à savoir s'il reste des itérations à effectuer
        if (nbIteration > 0) {
            List<List<Client>> voisinagesSuivants;
            switch (typeVoisin) {
                case METHODE_1:
                    voisinagesSuivants = trouverVoisin1(voisinage);
                    break;
                case METHODE_2:
                    voisinagesSuivants = trouverVoisin2(voisinage);
                    break;
                case METHODE_3:
                    voisinagesSuivants = trouverVoisin3(voisinage);
                    break;
                default:
                    voisinagesSuivants = trouverVoisin1(voisinage);
                    break;
            }
            List<Solution> solutions = new ArrayList();
            for(List<Client> c : voisinagesSuivants) {
                solutions.add(new Solution(capaciteHeuristique(c, vehicule, depot),c, vehicule));
            }
            Solution bestSolution = s;
            for(Solution solution : solutions) {
                if(bestSolution.evaluate() > solution.evaluate()) bestSolution = solution;
            }
            if(bestSolution == s) return s;
            else return getSolution(bestSolution.getClients(),vehicule, depot, clients, nbIteration, typeVoisin);
        // Sinon, on retourne la solution trouvée
        } else {
            return s;
        }
    }

    // private static Solution getSolutionFromVoisinage(List<Client> voisinage, Vehicule vehicule, Depot depot, List<Client> clients, int nbIteration) {
    //     // On détermine la valeur de la première solution qu'on va chercher à optimiser avec son voisinage.
    //     Solution s = new Solution(capaciteHeuristique(voisinage, vehicule, depot), clients, vehicule);
        
    //     // Notre méthode est récursive, on cherche donc à savoir s'il reste des itérations à effectuer
    //     if (nbIteration > 0) {
    //         List<Client> voisinageSuivant = trouverVoisin1(voisinage);
    //         Solution s1 = new Solution(capaciteHeuristique(voisinageSuivant, vehicule, depot), clients, vehicule);
    //         nbIteration--;
    //         if (s.evaluate() > s1.evaluate()) {
    //             return getSolutionFromVoisinage(voisinageSuivant, vehicule, depot, clients, nbIteration);
    //         } else {
    //             return getSolutionFromVoisinage(voisinage, vehicule, depot, clients, nbIteration);
    //         }
    //     // Sinon, on retourne la solution trouvée
    //     } else {
    //         return s;
    //     }
    // }
    // private static Solution getSolutionFromVoisinage2(List<Client> voisinage, Vehicule vehicule, Depot depot, List<Client> clients, int nbIteration) {
    //     // On détermine la valeur de la première solution qu'on va chercher à optimiser avec son voisinage.
    //     Solution s = new Solution(capaciteHeuristique(voisinage, vehicule, depot), clients, vehicule);
    //     if (nbIteration > 0) {
    //         nbIteration--;
    //         List<List<Client>> voisinagesSuivants = trouverVoisin2(voisinage);
    //         List<Solution> solutions = new ArrayList();
    //         for(List<Client> c : voisinagesSuivants) {
    //             solutions.add(new Solution(capaciteHeuristique(c, vehicule, depot),c, vehicule));
    //         }
    //         Solution bestSolution = s;
    //         for(Solution solution : solutions) {
    //             if(bestSolution.evaluate() > solution.evaluate()) bestSolution = solution;
    //         }
    //         if(bestSolution == s) return s;
    //         else return getSolutionFromVoisinage2(bestSolution.getClients(),vehicule, depot, clients, nbIteration);
    //     } else {
    //         return s;
    //     }
    // }

    // private static Solution getSolutionFromVoisinage3(List<Client> voisinage, Vehicule vehicule, Depot depot, List<Client> clients, int nbIteration) {
    //     // On détermine la valeur de la première solution qu'on va chercher à optimiser avec son voisinage.
    //     Solution s = new Solution(capaciteHeuristique(voisinage, vehicule, depot), clients, vehicule);
    //     if (nbIteration > 0) {
    //         nbIteration--;
    //         List<List<Client>> voisinagesSuivants = trouverVoisin3(voisinage);
    //         List<Solution> solutions = new ArrayList();
    //         for(List<Client> c : voisinagesSuivants) {
    //             solutions.add(new Solution(capaciteHeuristique(c, vehicule, depot),c, vehicule));
    //         }
    //         Solution bestSolution = s;
    //         for(Solution solution : solutions) {
    //             if(bestSolution.evaluate() > solution.evaluate()) bestSolution = solution;
    //         }
    //         if(bestSolution == s) return s;
    //         else return getSolutionFromVoisinage3(bestSolution.getClients(),vehicule, depot, clients, nbIteration);
    //     } else {
    //         return s;
    //     }
    // }

    private static Map<Integer, List<Point>> capaciteHeuristique(List<Client> clients, Vehicule vehicule, Depot depot) {
        Map<Integer, List<Point>> tournees = new HashMap<>();

        // On initialise les variables distance restante et capacité restante avec les valeurs maximales du véhicule
        int distanceRestante = vehicule.getMax_dist();
        int capaciteRestante = vehicule.getCapacity();

        // On initialise le temps restants à 12h en secondes.
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

    // Première méthode de recherche de voisinage
    private static List<List<Client>> trouverVoisin1(List<Client> clients) {
        // On cherche à créer une liste de liste de clients
        List<List<Client>> result = new ArrayList();

        for (int i = 0; i < Math.round(clients.size() / 2); i++) {
            for (int j = i + 1; j < clients.size(); j++) {
                // Pour chaque liste créée, on inverse 2 clients et on l'ajoute à la liste de liste.
                List<Client> voisin = new ArrayList<>(clients);
                Collections.swap(voisin, i, j);
                result.add(voisin);
            }
        }

        return result;
    }

    // Deuxième méthode de recherche de voisinage
    private static List<List<Client>> trouverVoisin2(List<Client> clients) {
        // On cherche à créer une liste de liste de clients
        List<List<Client>> result = new ArrayList();

        // Pour chaque liste créée on a besoin de 2 indexs pour échanger les positions.
        int index1, index2;

        // On parcourt la liste des clients pour construire nos listes.
        for (int i = 0; i < clients.size() - 1; i++) {
            index1 = i;
            // On vérifie que les indexs sont disponibles
            if (i == clients.size() -1) {
                index2 = 0;
            } else {
                index2 = i + 1;
            }

            // Pour chaque liste créée, on inverse 2 clients consécutifs et on l'ajoute à la liste de liste.
            List<Client> voisin = new ArrayList<>(clients);
            Collections.swap(voisin, index1, index2);
            result.add(voisin);
        }
        return result;
    }

    // Troisième méthode de recherche de voisinage
    private static List<List<Client>> trouverVoisin3(List<Client> clients) {
        // On cherche à créer une liste de liste de clients
        List<List<Client>> result = new ArrayList();

        // Pour chaque liste créée on a besoin de 3 indexs pour échanger leur position.
        int index1, index2, index3;

        // On parcourt la liste des clients pour construire nos listes.
        for (int i = 0; i < clients.size() - 1; i++) {
            index1 = i;
            // On vérifie que les indexs sont disponibles
            if (i == clients.size() - 2) {
                index2 = 0;
                index3 = 1;
            } else {
                index2 = i + 1;
                index3 = i + 2;
            }
            
            // Pour chaque liste créée, on inverse 3 clients consécutifs et on l'ajoute à la liste de liste.
            List<Client> voisin = new ArrayList<>(clients);
            Collections.swap(voisin, index1, index2);
            Collections.swap(voisin, index1, index3);
            result.add(voisin);
        }
        return result;
    }

}

