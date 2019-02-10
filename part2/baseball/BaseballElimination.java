/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {

    private int numTeam;
    private HashMap<String, Integer> teamToId;
    private HashMap<Integer, String> idToTeam;
    private int[] wins;
    private int[] loses;
    private int[] remains;
    private int[][] games;
    private HashMap<String, Bag<String>> elimCertificates;


    public BaseballElimination(String filename) {
        In in = new In(filename);
        this.teamToId = new HashMap<>();
        this.idToTeam = new HashMap<>();
        this.elimCertificates = new HashMap<>();
        this.numTeam = in.readInt();
        this.wins = new int[numTeam];
        this.loses = new int[numTeam];
        this.remains = new int[numTeam];
        this.games = new int[numTeam][numTeam];

        for (int i = 0; i < numTeam; i++) {
            String team = in.readString();
            this.wins[i] = in.readInt();
            this.loses[i] = in.readInt();
            this.remains[i] = in.readInt();
            this.teamToId.put(team, i);
            this.idToTeam.put(i, team);
            this.elimCertificates.put(team, null);

            for (int j = 0; j < numTeam; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return numTeam;
    }

    public Iterable<String> teams() {
        return this.teamToId.keySet();
    }

    public int wins(String team) {
        if (!this.teamToId.containsKey(team)) throw new IllegalArgumentException();
        int teamId = teamToId.get(team);
        return wins[teamId];
    }

    public int losses(String team) {
        if (!this.teamToId.containsKey(team)) throw new IllegalArgumentException();
        int teamId = teamToId.get(team);
        return loses[teamId];
    }

    public int remaining(String team) {
        if (!this.teamToId.containsKey(team)) throw new IllegalArgumentException();
        int teamId = teamToId.get(team);
        return remains[teamId];
    }

    public int against(String team1, String team2) {
        if (!this.teamToId.containsKey(team1) || !this.teamToId.containsKey(team2)) {
            throw new IllegalArgumentException();
        }
        int team1Id = teamToId.get(team1);
        int team2Id = teamToId.get(team2);
        return games[team1Id][team2Id];
    }

    public boolean isEliminated(String team) {
        if (!this.teamToId.containsKey(team)) throw new IllegalArgumentException();
        if (!elimCertificates.containsKey(team)) return false;
        if (elimCertificates.get(team) != null) return true;

        if (isTriviallyEliminated(team)) return true;
        FlowNetwork fn = buildFlowNetwork(team);

        int source = fn.V() - 2;
        int target = fn.V() - 1;
        FordFulkerson ff = new FordFulkerson(fn, source, target);
        // System.out.println(fn.toString());
        for (FlowEdge fe : fn.adj(source)) {
            if (fe.flow() < fe.capacity()) {
                addToCertificate(team, ff);
                return true;
            }
        }
        elimCertificates.remove(team);
        return false;
    }

    private boolean isTriviallyEliminated(String team) {
        int maxPotentialWins = wins(team) + remaining(team);
        Bag<String> certificate = new Bag<>();
        for (String teamI : teamToId.keySet()) {
            if (wins(teamI) > maxPotentialWins) {
                certificate.add(teamI);
                elimCertificates.put(team, certificate);
                return true;
            }
        }
        return false;
    }

    private FlowNetwork buildFlowNetwork(String team) {
        int numPairs = (numTeam - 1) * (numTeam - 2) / 2; // exclude candidate to eliminiate
        int numVerticies = (numTeam) + numPairs + 2; // includes source and target
        FlowNetwork fn = new FlowNetwork(numVerticies);
        int candidateId = teamToId.get(team);
        int source = numVerticies - 2;
        int target = numVerticies - 1;
        int maxWins = wins(team) + remaining(team);

        int offset = 0;
        for (int row = 0; row < numTeam; row++) {
            for (int col = row + 1; col < numTeam; col++) {
                if ((col == candidateId) || (row == candidateId)) continue;

                int gameVertex = numTeam + (offset++);
                // System.out.println("game node: " + gameVertex);
                fn.addEdge(new FlowEdge(source, gameVertex, against(idToTeam.get(row),
                                                                    idToTeam.get(col))));
                // add flow edge from game node to team node
                fn.addEdge(new FlowEdge(gameVertex, row, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(gameVertex, col, Double.POSITIVE_INFINITY));
            }
        }

        // add flow edge from team node to target
        for (int i = 0; i < numTeam; i++) {
            if (idToTeam.get(i).equals(team)) continue;
            fn.addEdge(new FlowEdge(i, target, Math.max(0, maxWins - wins(idToTeam.get(i)))));
        }
        return fn;
    }

    private void addToCertificate(String team, FordFulkerson ff) {
        final int numPairs = (numTeam - 1) * (numTeam - 2) / 2; // need this calculate team node
        Bag<String> certificate = new Bag<>();
        for (String curTeam : teams()) {
            if (!curTeam.equals(team)) {
                int teamId = teamToId.get(curTeam);
                if (ff.inCut(teamId)) {
                    certificate.add(curTeam);
                }
            }
        }
        this.elimCertificates.put(team, certificate);
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!this.teamToId.containsKey(team)) throw new IllegalArgumentException();
        if (!this.elimCertificates.containsKey(team))
            return null; // removed from eliminated team
        if (this.elimCertificates.get(team) == null) { // not checked
            isEliminated(team);
            return certificateOfElimination(team);
        }
        else {
            return this.elimCertificates.get(team);
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
