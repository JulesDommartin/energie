package data;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Clients extends ArrayList<Client> {
    private static Clients ourInstance;

    static {
        try {
            ourInstance = new Clients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Clients getInstance() {
        return ourInstance;
    }

    private Clients() throws IOException {
        FileReader demandes = new FileReader("./assets/instance_0/demandes.txt");
        FileReader coordonnees = new FileReader("./assets/instance_0/coords.txt");
        BufferedReader demandeBufRead = new BufferedReader(demandes);
        BufferedReader coordBufRead = new BufferedReader(coordonnees);
        String myLine = null;
        List<Integer> commandes = new ArrayList<>();
        List<Pair<Double,Double>> coords = new ArrayList<>();

        while ( (myLine = demandeBufRead.readLine()) != null)
        {
            commandes.add(Integer.parseInt(myLine));
        }

        Client c = new Client();
        add(c);
    }
}