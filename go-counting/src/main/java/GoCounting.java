import java.awt.Point;
import java.util.*;
import java.util.stream.*;

public class GoCounting {
    class Territory {
        public Player owner;
        public Set<Point> territory;
        public Territory(Player owner, Set<Point> territory) {
            this.owner = owner;
            this.territory = territory;
        }
        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Territory) && (
                (obj == this) || this.territory.equals(((Territory)obj).territory)
            );
        }
    }

    private char[][] board;
    public GoCounting(String board) {
        this.board = Arrays.stream(board.split("\n"))
            .map(line -> line.toCharArray())
            .toArray(size -> new char[size][]);
    }
    private boolean isValidPoint(Point p) {
        return p.y >= 0 && p.y < this.board.length &&
            p.x >= 0 && p.x < this.board[p.y].length;
    }
    private List<Point> neighbors(Point p) {
        return Arrays.asList(
            new Point(p.x - 1, p.y),
            new Point(p.x + 1, p.y),
            new Point(p.x, p.y - 1),
            new Point(p.x, p.y + 1)
        );
    }
    private Territory getTerritoryAndOwner(Point p) {
        if (!isValidPoint(p)) {
            throw new IllegalArgumentException("Invalid coordinate");
        }
        Map<Character, Player> tokens = new HashMap<>();
        tokens.put(' ', Player.NONE);
        tokens.put('B', Player.BLACK);
        tokens.put('W', Player.WHITE);
        Player token = tokens.get(this.board[p.y][p.x]);
        if (token != Player.NONE) {
            return new Territory(Player.NONE, new HashSet<>());
        }
        Set<Point> visited = new HashSet<>();
        Stack<Point> toVisit = new Stack<Point>();
        toVisit.add(p);
        Player owner = null;
        Set<Point> territory = new HashSet<>();
        while (!toVisit.isEmpty()) {
            p = toVisit.pop();
            if (visited.contains(p)) {
                continue;
            }
            visited.add(p);
            if (!isValidPoint(p)) {
                continue;
            }
            token = tokens.get(this.board[p.y][p.x]);
            if (token == Player.NONE) {
                territory.add(p);
                toVisit.addAll(neighbors(p));
            } else {
                if (owner == null) {
                    owner = token;
                } else if (owner != token) {
                    owner = Player.NONE;
                }
            }
        }
        return new Territory(owner == null ? Player.NONE : owner, territory);
    }
    private Player getTerritoryOwner(Point p) {
        return this.getTerritoryAndOwner(p).owner;
    }
    public Player getTerritoryOwner(int x, int y) {
        return this.getTerritoryOwner(new Point(x, y));
    }
    public Set<Point> getTerritory(Point p) {
        return this.getTerritoryAndOwner(p).territory;
    }
    public Set<Point> getTerritory(int x, int y) {
        return this.getTerritory(new Point(x, y));
    }
    public Map<String, Set<Point>> getTerritories() {
        Map<String, Set<Point>> territories = new HashMap<>();
        territories.put("NONE", new HashSet<>());
        territories.put("BLACK", new HashSet<>());
        territories.put("WHITE", new HashSet<>());
        IntStream.range(0, this.board.length)
            .forEach(i -> IntStream.range(0, this.board[i].length)
                .mapToObj(j -> new Point(j, i))
                .filter(p -> !getTerritory(p).isEmpty())
                .forEach(p -> 
                    territories.get(getTerritoryOwner(p).toString()).add(p)
                )
            );
        return territories;
    }
}