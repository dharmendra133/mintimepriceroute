import java.util.*;

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

class Edge {
    float price, time;
    char mode;
    int destination;

    public Edge(float price, float time, char mode, int destination) {
        this.price = price;
        this.time = time;
        this.mode = mode;
        this.destination = destination;
    }
}

public class finallogic {
    static List<List<Edge>> adj = new ArrayList<>();
    static Map<String, Integer> statemappingnumber = new HashMap<>();
    static Map<Integer, String> numbermappingstate = new HashMap<>();
    static int countOfState;

    static void pathremove(StringBuilder s) {
        int n = s.length() - 1;
        while (n >= 0) {
            char c = s.charAt(n);
            if (c == '*') {
                s.deleteCharAt(n);
                break;
            } else {
                s.deleteCharAt(n);
            }
            n--;
        }
    }

    static void dfs(int s, int d, int[] vis, StringBuilder ans, StringBuilder path) {
        if (s == d) {
            ans.append(path);
            ans.append("?");
            return;
        }

        vis[s] = 1;

        for (Edge it : adj.get(s)) {
            if (vis[it.destination] == 0) {
                path.append('*');
                path.append(numbermappingstate.get(s));
                path.append("_");
                path.append(it.mode);
                path.append("_");
                path.append(numbermappingstate.get(it.destination));
                dfs(it.destination, d, vis, ans, path);
                pathremove(path);
            }
        }
        vis[s] = 0;
    }

    static String allRoute(int source, int destination) {
        int[] vis = new int[countOfState];
        StringBuilder ans = new StringBuilder();
        StringBuilder path = new StringBuilder();
        dfs(source, destination, vis, ans, path);
        return ans.toString();
    }

    static void addInGraph(int source, int destination, float price, float time, char mode) {
        adj.get(source).add(new Edge(price, time, mode, destination));
    }

    static void makeGraph(String source, String destination, float price, float time, char mode) {
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

    static String fastestRoute(int source, int destination) {
        PriorityQueue<Pair<Float, Pair<Integer, StringBuilder>>> pq = new PriorityQueue<>(Comparator.comparingDouble(p -> p.getKey()));
        pq.add(new Pair<>(0.0f, new Pair<>(source, new StringBuilder())));
        int[] vis = new int[countOfState];
        Arrays.fill(vis, 0);

        for (Map.Entry<String, Integer> entry : statemappingnumber.entrySet())
            numbermappingstate.put(entry.getValue(), entry.getKey());

        while (!pq.isEmpty()) {
            float time = pq.peek().getKey();
            int node = pq.peek().getValue().getKey();
            StringBuilder path = pq.peek().getValue().getValue();
            pq.poll();
            vis[node] = 1;

            if (node == destination) {
                StringBuilder ans = new StringBuilder();
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
                            tempDestination = "";
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
                ans.append("it will take ").append(time).append(" hours that's Fastest Route");
                return ans.toString();
            }

            for (Edge it : adj.get(node)) {
                if (vis[it.destination] == 0) {
                    StringBuilder temp = new StringBuilder(" ").append(it.destination).append("_").append(it.mode).append(" ");
                    pq.add(new Pair<>(time + it.time, new Pair<>(it.destination, new StringBuilder(path.toString() + temp.toString()))));
                }
            }
        }
        return "no route";
    }

    static String cheapestRoute(int source, int destination) {
        PriorityQueue<Pair<Float, Pair<Integer, StringBuilder>>> pq = new PriorityQueue<>(Comparator.comparingDouble(p -> p.getKey()));
        pq.add(new Pair<>(0.0f, new Pair<>(source, new StringBuilder())));

        while (!pq.isEmpty()) {
            float price = pq.peek().getKey();
            int node = pq.peek().getValue().getKey();
            StringBuilder path = pq.peek().getValue().getValue();
            pq.poll();
            int[] vis = new int[countOfState];
            Arrays.fill(vis, 0);
            vis[node] = 1;

            if (node == destination) {
                StringBuilder ans = new StringBuilder();
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
                            tempDestination = "";
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
                ans.append(" it will take ").append(price).append("Rs that is minimum price");
                return ans.toString();
            }

            for (Edge it : adj.get(node)) {
                if (vis[it.destination] == 0) {
                    StringBuilder temp = new StringBuilder(" ").append(it.destination).append("_").append(it.mode).append(" ");
                    pq.add(new Pair<>(price + it.price, new Pair<>(it.destination, new StringBuilder(path.toString() + temp.toString()))));
                }
            }
        }
        return "no route";
    }

    public static void main(String[] args) {
        int MAX_STATES = 1000000;
        countOfState = 0;
        for (int i = 0; i < MAX_STATES; i++) {
            adj.add(new ArrayList<>());
        }

        makeGraph("Delhi", "Chandigarh", 2000.0f, 2.0f, 'F');
        makeGraph("Delhi", "Chandigarh", 700.0f, 6.0f, 'B');
        makeGraph("Delhi", "Patiala", 1100.0f, 6.0f, 'T');
        makeGraph("Delhi", "Patiala", 900.0f, 8.0f, 'B');
        makeGraph("Delhi", "Jammu", 7000.0f, 4.2f, 'F');
        makeGraph("Jammu", "Katra", 200.0f, 2.0f, 'B');
        makeGraph("Jammu", "Patiala", 1100.0f, 11.0f, 'T');
        makeGraph("Patiala", "Katra", 800.0f, 6.0f, 'T');
        makeGraph("Patiala", "Katra", 700.0f, 7.0f, 'B');

        makeGraph("Chandigarh", "Delhi", 2000.0f, 2.0f, 'F');
        makeGraph("Chandigarh", "Delhi", 700.0f, 6.0f, 'B');
        makeGraph("Patiala", "Delhi", 1100.0f, 6.0f, 'T');
        makeGraph("Patiala", "Delhi", 900.0f, 8.0f, 'B');
        makeGraph("Jammu", "Delhi", 7000.0f, 4.2f, 'F');
        makeGraph("Katra", "Jammu", 200.0f, 2.0f, 'B');
        makeGraph("Patiala", "Jammu", 1100.0f, 11.0f, 'T');
        makeGraph("Katra", "Patiala", 800.0f, 6.0f, 'T');
        makeGraph("Katra", "Patiala", 700.0f, 7.0f, 'B');

        for (Map.Entry<String, Integer> entry : statemappingnumber.entrySet())
            numbermappingstate.put(entry.getValue(), entry.getKey());

        System.out.println(cheapestRoute(statemappingnumber.get("Delhi"), statemappingnumber.get("Katra")));
        System.out.println("********************");
        System.out.println(fastestRoute(statemappingnumber.get("Delhi"), statemappingnumber.get("Katra")));
    }
}
