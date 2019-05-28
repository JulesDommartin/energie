import data.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class ApplicationTest {


    List<Client> clients;
    Vehicule vehicule;
    Depot depot;
    Application app;

    // Chargement des assets
    @org.junit.Before
    public void setUp() throws Exception {
        clients = Assets.getClients();
        vehicule = Assets.getVehicule();
        depot = Assets.getDepot();
        app = new Application();
    }

    // Teste dans chaque tournée si les clients ne sont pas présents 2 fois
    @org.junit.Test
    public void noDuplication() {
        int nbIteration = 3;
        clients.sort(Comparator.comparingInt(Client::getDemande));

        List<Client> voisinage = new ArrayList(clients);
        Solution s1 = app.getSolution(voisinage, vehicule, depot, clients, nbIteration, Application.TrouverVoisinType.METHODE_1);

        for (Integer i = 0; i < s1.getTournees().size(); i++) {
            long countTourneeDistinct = s1.getTournees().get(i).stream().filter(point -> point instanceof Client).distinct().count();
            long countTournee = s1.getTournees().get(i).stream().filter(point -> point instanceof Client).count();
            assertEquals(countTourneeDistinct, countTournee);
        }


    }

    // Teste si tous les clients sont desservis
    @org.junit.Test
    public void allClientsAreDelivered() {
        int nbIteration = 3;
        clients.sort(Comparator.comparingInt(Client::getDemande));

        List<Client> voisinage = new ArrayList(clients);
        Solution s1 = app.getSolution(voisinage, vehicule, depot, clients, nbIteration, Application.TrouverVoisinType.METHODE_1);

        int nbClientDelivered = 0;

        for (Integer i = 0; i < s1.getTournees().size(); i++) {
            nbClientDelivered += s1.getTournees().get(i).stream().filter(point -> point instanceof Client).distinct().count();
        }

        assertEquals(nbClientDelivered, clients.size());
    }

    @org.junit.Test
    public void nonDeterministSolution() {
        // On crée 2 listes aléatoires de clients
        List<Client> shuffledClient1 = Assets.getClients();
        Collections.shuffle(shuffledClient1);

        List<Client> shuffledClient2 = Assets.getClients();
        Collections.shuffle(shuffledClient2);

        Solution s1 = app.getSolution(shuffledClient1, vehicule, depot, clients, 3, Application.TrouverVoisinType.METHODE_1);
        Solution s2 = app.getSolution(shuffledClient2, vehicule, depot, clients, 3, Application.TrouverVoisinType.METHODE_1);

        // Si les solutions sont différentes elles doivent donner 2 évaluations différentes.
        assertNotEquals(s1.evaluate(), s2.evaluate());
    }

    // Teste si la fonction objectif renvoie bien la valeur attendue avec la fonction déterministe
    @org.junit.Test
    public void checkObjectiveFunction() {
        clients.sort(Comparator.comparingInt(Client::getDemande));

        Solution s1 = app.getSolution(clients, vehicule, depot, clients, 3, Application.TrouverVoisinType.METHODE_1);

        assertEquals(s1.evaluate(), new Float(12.608334));
    }

}