package Graph;

import java.util.LinkedList;

public class Graph {
    /**
     * A class for creating nodes in a graph.
     */
    public class GraphNode {
        private final int row;
        private final int col;
        private final GraphNode[][] parentGraph; // The graph containing this node
        private double dStart = Double.POSITIVE_INFINITY; // current best known distance from the start for this node.
        private boolean isVisit = false;
        private LinkedList<GraphNode> neighbours = new LinkedList<>();

        public GraphNode(int row, int col, GraphNode[][] parentGraph) {
            this.parentGraph = parentGraph;
            this.row = row;
            this.col = col;
        }

        /**
         * Enables access to the neighbours property of a node.
         */
        public void findNeighbours() {
            LinkedList<GraphNode> neighbours = new LinkedList<>();

            if (!this.isVisit) {
                int row = this.row;
                int col = this.col;

                // top and bottom
                if (row != this.parentGraph.length - 1) {
                    neighbours.add(this.parentGraph[row + 1][col]);
                }
                if (row != 0) {
                    neighbours.add(this.parentGraph[row - 1][col]);
                }

                // left
                if (col != 0) {
                    neighbours.add(this.parentGraph[row][col - 1]);
                    if (col % 2 != 0 && row != 0) {
                        neighbours.add(this.parentGraph[row - 1][col - 1]);
                    } else if (col % 2 == 0 && row != this.parentGraph.length - 1) {
                        neighbours.add(this.parentGraph[row + 1][col - 1]);
                    }
                }

                // right
                if (col != this.parentGraph[row].length - 1) {
                    neighbours.add(this.parentGraph[row][col + 1]);
                    if (col % 2 != 0 && row != 0) {
                        neighbours.add(this.parentGraph[row - 1][col + 1]);
                    } else if (col % 2 == 0 && row != this.parentGraph.length - 1) {
                        neighbours.add(this.parentGraph[row + 1][col + 1]);
                    }
                }

                // remove any neighbours that are actually walls
                LinkedList<GraphNode> notWallNeighbours = new LinkedList<>();
                neighbours.forEach((node) -> {
                    if (!node.isVisit) {
                        notWallNeighbours.add(node);
                    }
                });
                neighbours = notWallNeighbours;
            }

            this.neighbours = neighbours;
        }

        public GraphNode up(){
            if (row != 0)
                return this.parentGraph[row - 1][col];
            return null;
        }

        public GraphNode down(){
            if (row != this.parentGraph.length - 1)
                return this.parentGraph[row + 1][col];
            return null;
        }

        public GraphNode upLeft(){
            if (col != 0) {
                if(col % 2 == 0)
                    return this.parentGraph[row][col - 1];
                else if (row != 0)
                    return this.parentGraph[row - 1][col - 1];
            }
            return null;
        }

        public GraphNode downLeft(){
            if (col != 0) {
                if (col % 2 == 0 && row != this.parentGraph.length - 1)
                    return this.parentGraph[row + 1][col - 1];
                else if (col % 2 != 0)
                    return this.parentGraph[row][col - 1];
            }
            return null;
        }

        public GraphNode upRight(){
            if (col != this.parentGraph[row].length - 1) {
                if(col % 2 == 0)
                    return this.parentGraph[row][col + 1];
                else if (row != 0)
                    return this.parentGraph[row - 1][col + 1];
            }
            return null;
        }

        public GraphNode downRight(){
            if (col != this.parentGraph[row].length - 1) {
                if (col % 2 == 0 && row != this.parentGraph.length - 1)
                    return this.parentGraph[row + 1][col + 1];
                else if (col % 2 != 0)
                    return this.parentGraph[row][col + 1];
            }
            return null;
        }

        public GraphNode getAtDirection(String direction){
            switch (direction) {
                case"up" -> { return this.up(); }
                case"upright" -> { return this.upRight(); }
                case"upleft" -> { return this.upLeft(); }
                case"down" -> { return this.down(); }
                case"downleft" -> { return this.downLeft(); }
                case"downright" -> { return this.downRight(); }
                default -> { return null; }
            }
        }

        public LinkedList<GraphNode> getNeighbours() {
            return neighbours;
        }

        public double getDStart() {
            return dStart;
        }

        public void setDStart(double dStart) {
            this.dStart = dStart;
        }

        public boolean isVisit() {
            return isVisit;
        }

        public void setVisit(boolean visit) {
            isVisit = visit;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        @Override
        public String toString(){
            return "(" + row + ", " + col + ")";
        }
    }

    private static Graph instance;
    private final GraphNode[][] graph;
    private GraphNode start, end;
    private LinkedList<GraphNode> executeList = new LinkedList<>();

    public static Graph instance(int row, int col){
        instance = new Graph(row, col);
        return instance;
    }

    public static Graph instance() { return instance; }

    public Graph(int numRows, int numCols) {
        this.graph = new GraphNode[numRows][numCols];

        // Initialize the graph
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.graph[i][j] = new GraphNode(i, j, graph);
            }
        }

        // Assign neighbours to all nodes
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                this.graph[i][j].findNeighbours();
            }
        }
    }

    public GraphNode[][] getGraph() {
        return this.graph;
    }

    public int findShortestDistance(int sRow, int sCol, int eRow, int eCol){
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                this.graph[i][j].setDStart(Double.POSITIVE_INFINITY);
                this.graph[i][j].setVisit(false);
            }
        }
        start = graph[sRow][sCol];
        start.setDStart(0.0);
        end = graph[eRow][eCol];
        if (start == end)
            return (int) end.getDStart();

        try {
            findNeighboursDistance(start);
            while (!executeList.isEmpty()){
                findNeighboursDistance(executeList.removeFirst());
            }
        } catch (Exception e){

        }
        return (int) end.getDStart();
    }

    private void findNeighboursDistance(GraphNode node) throws Exception{
        if(!node.isVisit()){
            node.setVisit(true);
            for (GraphNode neighbour : node.getNeighbours()){
                if(neighbour.getDStart() == Double.POSITIVE_INFINITY){
                    neighbour.setDStart(node.getDStart() + 1);
                    if(neighbour == end)
                        throw new Exception("found end node");

                    if(!neighbour.isVisit())
                        executeList.addLast(neighbour);
                }
            }
//            System.out.println(executeList);
        }
    }

    public static void main(String[] args){
        Graph.instance(6, 8);
//        System.out.println("result " + Graph.instance().findShortestDistance(1,1, 5, 4));
//        System.out.println(Graph.instance().getGraph()[1][1].neighbours);
        int row = 0, col = 0;
        System.out.println("up :" + Graph.instance().getGraph()[row][col].up());
        System.out.println("down :" + Graph.instance().getGraph()[row][col].down());
        System.out.println("upLeft :" + Graph.instance().getGraph()[row][col].upLeft());
        System.out.println("downLeft :" + Graph.instance().getGraph()[row][col].downLeft());
        System.out.println("upRight :" + Graph.instance().getGraph()[row][col].upRight());
        System.out.println("downRight :" + Graph.instance().getGraph()[row][col].downRight());
        System.out.println();

        System.out.println("up :" + Graph.instance().getGraph()[row][col].getAtDirection("up"));
        System.out.println("down :" + Graph.instance().getGraph()[row][col].getAtDirection("down"));
        System.out.println("upLeft :" + Graph.instance().getGraph()[row][col].getAtDirection("upleft"));
        System.out.println("downLeft :" + Graph.instance().getGraph()[row][col].getAtDirection("downleft"));
        System.out.println("upRight :" + Graph.instance().getGraph()[row][col].getAtDirection("upright"));
        System.out.println("downRight :" + Graph.instance().getGraph()[row][col].getAtDirection("downright"));
        System.out.println("empty :" + Graph.instance().getGraph()[row][col].getAtDirection(""));

    }
}