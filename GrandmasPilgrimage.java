import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GrandmasPilgrimage {
    private static ArrayList<City> cities;
    public static void main(String args[]) {
        cities = fileReader("data.txt");
        travel();
    }

    public static void travel() {
        Route route = new Route(cities);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            route = evolve(route,i);
        }
    }

    public static Route evolve(Route route, int iteration) {
        Route newRoute = new Route(route.path,route.distance);
        newRoute.mutate();
        if(route.calculateDistance() > newRoute.calculateDistance()) {
            System.out.println("New route found: " + String.format("%.7f", newRoute.distance) + " / Iteration: " + iteration);
            return newRoute;
        } else {
            return route;
        }
    }

    public static ArrayList<City> fileReader(String path) {
        cities = new ArrayList<City>();
        String[] data = new String[0];
        try {
            data = Files.readAllLines(Paths.get(path)).toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cities = new ArrayList<>();
        for (String line : data) {
            if(line.split(" ").length == 3){
                cities.add(new City(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]), line.split(" ")[2]));
            }
        }
        return cities;
    }
}

class City {
    final double latitude;
    final double longitude;
    final String name;

    public City(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
}

class Route {
    public ArrayList<City> path;
    public double distance;

    public Route(ArrayList<City> path) {
        this.path = new ArrayList<>(path);
        this.distance = 0;
    }

    public Route(ArrayList<City> path, double distance) {
        this.path = new ArrayList<>(path);
        this.distance = distance;
    }

    private double euclidianDistance(City a, City b) {
        return Math.sqrt(Math.pow(b.latitude - a.latitude, 2) + Math.pow(b.longitude - a.longitude, 2));
    }

    public double calculateDistance() {
        double distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            distance += euclidianDistance(path.get(i), path.get(i + 1));
        }
        distance += euclidianDistance(path.get(path.size() - 1), path.get(0));
        this.distance = distance;
        return distance;
    }

    public void mutate() {
        int index1 = (int) (Math.random() * path.size());
        int index2 = (int) (Math.random() * path.size());

        while (index1 == index2) {
            index2 = (int) (Math.random() * path.size());
        }

        int start = Math.min(index1, index2);
        int end = Math.max(index1, index2);

        ArrayList<City> copy = new ArrayList<>(path.subList(start, end));

        for (int i = 0; i < copy.size(); i++) {
            path.set(start + i, copy.get(copy.size() - 1 - i));
        }
    }
}