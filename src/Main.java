import data.Assets;
import data.Client;
import data.Depot;
import data.Vehicule;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Client> clients = Assets.getClients();
        Vehicule vehicule = Assets.getVehicule();
        Depot depot = Assets.getDepot();
    }
}

