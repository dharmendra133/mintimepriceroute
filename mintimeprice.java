import java.util.*;
import java.lang.*;

class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

public class Main {
    private static List<Pair<Pair<Float, Float>, Pair<Character, Integer>>>[] adj;
    private static Map<String, Integer> statemappingnumber;
    private static Map<Integer, String> numbermappingstate;
    private static int countOfState;

    private static void addInGraph(int source, int destination, float price, float time, char mode) {
        adj[source].add(new Pair<>(new Pair<>(price, time), new Pair<>(mode, destination)));
    }

    private static void makeGraph(String source, String destination, float price, float time, char mode) {
        int sourceNum, destinationNum;
        if (statemappingnumber.containsKey(source)) {
            sourceNum = statemappingnumber.get(source);
        } else {
            statemappingnumber.put(source, countOfState);
            sourceNum = countOfState;
            countOfState++;
        }

        if (statemappingnumber.containsKey(destination)) {
            destinationNum = statemappingnumber.get(destination);
        } else {
            statemappingnumber.put(destination, countOfState);
            destinationNum = countOfState;
            countOfState++;
        }

        addInGraph(sourceNum, destinationNum, price, time, mode);
    }

    private static String miniPrice(int source, int destination) {
        PriorityQueue<Pair<Integer, Pair<Integer, String>>> pq = new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));
        pq.add(new Pair<>(0, new Pair<>(source, "")));

        List<Integer> vis = new ArrayList<>(countOfState);
        for (int i = 0; i < countOfState; i++)
            vis.add(0);

        for (Map.Entry<String, Integer> entry : statemappingnumber.entrySet())
            numbermappingstate.put(entry.getValue(), entry.getKey());

        while (!pq.isEmpty()) {
            Pair<Integer, Pair<Integer, String>> top = pq.poll();
            int price = top.getKey();
            int node = top.getValue().getKey();
            String path = top.getValue().getValue();
            vis.set(node, 1);

            if (node == destination) {
                StringBuilder ans = new StringBuilder();
                System.out.println(path);
                int tempNum = 0;
                String tempMode = "";
                String tempSource = numbermappingstate.get(source);
                String tempDestination;

                for (int i = 1; i < path.length(); i++) {
                    char currentChar = path.charAt(i);
                    if (currentChar == ' ') {
                        if (!tempMode.isEmpty()) {
                            ans.append("Take ").append(tempMode).append(" From ").append(tempSource).append(" to ");
                            tempDestination = numbermappingstate.get(tempNum);
                            ans.append(tempDestination).append(" then ");

                            tempNum = 0;
                            tempMode = "";
                            tempSource = tempDestination;
                        }
                    } else if (currentChar == '_') {
                        continue;
                    } else if (currentChar >= 'A' && currentChar <= 'Z') {
                        if (currentChar == 'B') {
                            tempMode += "Bus";
                        } else if (currentChar == 'F') {
                            tempMode += "Flight";
                        } else {
                            tempMode += "Train";
                        }
                    } else {
                        tempNum = tempNum * 10 + (currentChar - '0');
                    }
                }

                ans.append(" it will take ").append(price).append(" price that is minimum price");
                return ans.toString();
            }

            for (Pair<Pair<Float, Float>, Pair<Character, Integer>> it : adj[node]) {
                if (vis.get(it.getValue().getValue()) == 0) {
                    StringBuilder temp = new StringBuilder(" ").append(it.getValue().getValue()).append("_")
                            .append(it.getValue().getKey()).append(" ");
                    pq.add(new Pair<>( (int) (price +  it.getKey().getKey() ),
                            new Pair<>(it.getValue().getValue(), path + temp.toString())));
                }
            }
        }
        return "no route";
    }





    private static String minimumTimeRoute(int source, int destination) {
        PriorityQueue<Pair<Integer, Pair<Integer, String>>> pq =
                new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));
        pq.add(new Pair<>(0, new Pair<>(source, "")));

        List<Integer> vis = new ArrayList<>(countOfState);
        for (int i = 0; i < countOfState; i++)
            vis.add(0);

        for (Map.Entry<String, Integer> entry : statemappingnumber.entrySet())
            numbermappingstate.put(entry.getValue(), entry.getKey());

        while (!pq.isEmpty()) {
            Pair<Integer, Pair<Integer, String>> top = pq.poll();
            int time = top.getKey();
            int node = top.getValue().getKey();
            String path = top.getValue().getValue();
            vis.set(node, 1);

            if (node == destination) {
                StringBuilder ans = new StringBuilder();
                int tempNum = 0;
                String tempMode = "";
                String tempSource = numbermappingstate.get(source);
                String tempDestination;

                for (int i = 1; i < path.length(); i++) {
                    char currentChar = path.charAt(i);
                    if (currentChar == ' ') {
                        ans.append("Take ").append(tempMode).append(" From ").append(tempSource).append(" to ");
                        tempDestination = numbermappingstate.get(tempNum);
                        ans.append(tempDestination).append(" then ");

                        tempNum = 0;
                        tempMode = "";
                        tempSource = tempDestination;
                    } else if (currentChar == '_') {
                        continue;
                    } else if (currentChar >= 'A' && currentChar <= 'Z') {
                        if (currentChar == 'B') {
                            tempMode += "Bus";
                        } else if (currentChar == 'F') {
                            tempMode += "Flight";
                        } else {
                            tempMode += "Train";
                        }
                    } else {
                        tempNum = tempNum * 10 + (currentChar - '0');
                    }
                }

                ans.append(" it will take ").append(time).append(" hours that is minimum time");
                return ans.toString();
            }

            for (Pair<Pair<Float, Float>, Pair<Character, Integer>> it : adj[node]) {
                if (vis.get(it.getValue().getValue()) == 0) {
                    StringBuilder temp = new StringBuilder(" ").append(it.getValue().getValue()).append("_")
                            .append(it.getValue().getKey()).append(" ");
                    pq.add(new Pair<>( (int)  (time + (it.getKey().getValue())), new Pair<>(it.getValue().getValue(), path + temp.toString())));
                }
            }
        }
        return "no route";
    }




    public static void main(String[] args) {
        adj = new ArrayList[1000000];
        for (int i = 0; i < adj.length; i++)
            adj[i] = new ArrayList<>();

        statemappingnumber = new HashMap<>();
        numbermappingstate = new HashMap<>();
        countOfState = 0;

        makeGraph("Delhi", "Chandigarh", 5000.0f, 2.0f, 'F');
        makeGraph("Delhi", "Chandigarh", 2000.0f, 4.0f, 'T');
        makeGraph("Delhi", "Chandigarh", 300.0f, 6.0f, 'B');

        makeGraph("Delhi", "Patiala", 1800.0f, 6.0f, 'T');
        makeGraph("Delhi", "Patiala", 800.0f, 8.0f, 'B');

        makeGraph("Delhi", "Pune", 6000.0f, 3.0f, 'F');
        makeGraph("Delhi", "Pune", 3500.0f, 14.0f, 'T');
        makeGraph("Delhi", "Pune", 2000.0f, 20.0f, 'B');

        makeGraph("Chandigarh", "Delhi", 4500.0f, 2.0f, 'F');
        makeGraph("Chandigarh", "Delhi", 1000.0f, 6.0f, 'B');
        makeGraph("Chandigarh", "Delhi", 2000.0f, 4.0f, 'T');

        makeGraph("Chandigarh", "Patiala", 400.0f, 1.0f, 'T');
        makeGraph("Chandigarh", "Patiala", 200.0f, 2.0f, 'B');

        makeGraph("Chandigarh", "Pune", 8000.0f, 4.0f, 'F');
        makeGraph("Chandigarh", "Pune", 4000.0f, 18.0f, 'T');
        makeGraph("Chandigarh", "Pune", 3500.0f, 24.0f, 'B');

        makeGraph("Patiala", "Chandigarh", 400.0f, 1.0f, 'T');
        makeGraph("Patiala", "Chandigarh", 200.0f, 2.0f, 'B');
        makeGraph("Patiala", "katra", 2000.0f, 2.0f, 'F');

        makeGraph("Patiala", "Delhi", 1800.0f, 6.0f, 'T');
        makeGraph("Patiala", "Delhi", 800.0f, 8.0f, 'B');
        makeGraph("Patiala", "Pune", 3800.0f, 20.0f, 'T');

        makeGraph("Pune", "Delhi", 6500.0f, 3.0f, 'F');
        makeGraph("Pune", "Delhi", 3500.0f, 14.0f, 'T');
        makeGraph("Pune", "Delhi", 2500.0f, 20.0f, 'B');

        makeGraph("Pune", "Chandigarh", 8200.0f, 4.0f, 'F');
        makeGraph("Pune", "Chandigarh", 3800.0f, 18.0f, 'T');
        makeGraph("Pune", "Chandigarh", 3200.0f, 24.0f, 'B');

        makeGraph("Pune", "Patiala", 3800.0f, 20.0f, 'T');

        System.out.println(miniPrice(statemappingnumber.get("Patiala"), statemappingnumber.get("Pune")));
        System.out.println("*********");
        System.out.println(minimumTimeRoute(statemappingnumber.get("Patiala"), statemappingnumber.get("Pune")));
        System.out.println("*********");


    }
}
