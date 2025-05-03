package Agent;

import Maze.MazeManager;
import java.util.ArrayList;
import java.util.Scanner;

public class Agent {
    private int id;
    private int currentX, currentY;
    private StringStack moveHistory;
    private int totalMoves;
    private int backtracks;
    private int trapsTriggered;
    private int powerUpsUsed;
    private int maxStackDepth;
    private boolean hasPowerUp;
    private boolean hasReachedGoal;
    private boolean isStuck;
    private MazeManager maze;

    //constructor
    public Agent(int id, int startX, int startY, MazeManager maze) {
        this.id = id;
        this.currentX = startX;
        this.currentY = startY;
        this.moveHistory = new StringStack();
        this.hasReachedGoal = false;
        this.totalMoves = 0;
        this.backtracks = 0;
        this.trapsTriggered = 0;
        this.powerUpsUsed = 0;
        this.maxStackDepth = 0;
        this.hasPowerUp = false;
        this.isStuck = false;
        this.maze = maze; // Set the maze reference
        recordMove(startX, startY);
    }
    public int getId() { return id; }
    public int getCurrentX() { return currentX; }
    public int getCurrentY() { return currentY; }
    public boolean hasReachedGoal() { return hasReachedGoal; }
    public int getTotalMoves() { return totalMoves; }
    public int getBacktracks() { return backtracks; }
    public int getTrapsTriggered() { return trapsTriggered; }
    public void setTrapsTriggered(int trapsTriggered){this.trapsTriggered=trapsTriggered;}
    public void incrementTrapsTriggered() { trapsTriggered++; }
    public int getPowerUpsUsed() { return powerUpsUsed; }
    public int getMaxStackDepth() { return maxStackDepth; }
    public boolean hasPowerUp() { return hasPowerUp; }
    public void setHasPowerUp(boolean hasPowerUp) { this.hasPowerUp = hasPowerUp; }
    public void setHasReachedGoal(boolean hasReachedGoal) { this.hasReachedGoal = hasReachedGoal; }
    public boolean isFinished() { return hasReachedGoal || isStuck; }
    public void setX(int x) { this.currentX = x; }
    public void setY(int y) { this.currentY = y; }
    public boolean isStuck() { return isStuck; }
    public MazeManager getMazeManager() {
        return maze;
    }
    public int getMoveHistorySize() {
        return moveHistory.size();
    }

    //applying valid dir
    public void move(String direction) {
        int newX = currentX;
        int newY = currentY;

        switch (direction.toUpperCase()) {
            case "UP": newX--; break;
            case "DOWN": newX++; break;
            case "LEFT": newY--; break;
            case "RIGHT": newY++; break;
            default:
                System.out.println("Invalid direction");
                return;
        }

        if (isMoveValid(newX, newY)) {
            maze.moveAgent(this, direction);
            recordMove(newX, newY);
            totalMoves++;
        } else {
            System.out.println("Move to (" + newX + "," + newY + ") is invalid");
        }
    }
    //cheking the validty of the move
    private boolean isMoveValid(int x, int y) {
        return maze.getTile(x, y) != null && maze.getTile(x, y).isTraversable();
    }

    public void backtrack(int steps) {
        System.out.println("Agent " + id + " backtracking " + steps + " step(s) from (" + currentX + "," + currentY + ")");
        if (moveHistory.size() <= steps) {
            steps = moveHistory.size() - 1; // Don’t clear all history
        }
        if (steps <= 0) {
            System.out.println("No moves to backtrack");
            return;
        }

        for (int i = 0; i < steps; i++) {
            if (!moveHistory.isEmpty()) {
                moveHistory.pop();
            }
        }

        String targetPosition = moveHistory.peek();
        if (targetPosition != null) {
            String[] position = targetPosition.split(",");
            int newX = Integer.parseInt(position[0]);
            int newY = Integer.parseInt(position[1]);
            if (isMoveValid(newX, newY)) {
                int oldX = currentX;
                int oldY = currentY;
                currentX = newX;
                currentY = newY;
                maze.updateAgentLocation(this, oldX, oldY);
                backtracks++;
                System.out.println("Agent " + id + " backtracked to (" + newX + "," + newY + ")");
            } else {
                System.out.println("Target position (" + newX + "," + newY + ") is invalid");
                isStuck = true;
            }
        }
    }

    // Default backtrack for manual action (1 step)
    public void backtrack() {
        backtrack(1);
    }

    public void applyPowerUp() {
        if (hasPowerUp) {
            System.out.println("Power-up applied by Agent " + id);
            powerUpsUsed++;
            hasPowerUp = false;
            int[] newPos = maze.findRandomValidPosition();
            if (newPos != null) {
                int oldX = currentX;
                int oldY = currentY;
                currentX = newPos[0];
                currentY = newPos[1];
                if (isMoveValid(currentX, currentY)) {
                    maze.updateAgentLocation(this, oldX, oldY);
                    recordMove(currentX, currentY);
                    System.out.println("Agent " + id + " teleported to (" + currentX + "," + currentY + ")");
                }
            }
        }
    }

    private void recordMove(int x, int y) {
        moveHistory.push(x + "," + y);
        maxStackDepth = Math.max(maxStackDepth, moveHistory.size());
    }

    public String getLastFiveMoves() {
        StringBuilder sb = new StringBuilder();
        StringStack temp = new StringStack();
        int count = 0;
        while (!moveHistory.isEmpty() && count < 5) {
            String move = moveHistory.pop();
            temp.push(move);
            sb.append(move).append(" ");
            count++;
        }
        while (!temp.isEmpty()) {
            moveHistory.push(temp.pop());
        }
        return sb.toString().trim();
    }

    public String getMoveHistoryAsString() {
        return getLastFiveMoves();
    }



    public String takeAction(MazeManager maze) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Agent " + id + " → move (UP/DOWN/LEFT/RIGHT), BACKTRACK, or USEPOWERUP: ");
        String cmd = "";
        try {
            cmd = scanner.nextLine().trim().toUpperCase();
        } catch (Exception e) {
            return "Skipped turn (input error)";
        }
        if (cmd.isEmpty()) {
            return "Skipped turn (empty input)";
        }
        if ("BACKTRACK".equals(cmd)) {
            backtrack(1);
            return "Backtracked 1 step";
        } else if ("USEPOWERUP".equals(cmd)) {
            if (hasPowerUp) {
                applyPowerUp();
                return "Used power-up and teleported to (" + currentX + "," + currentY + ")";
            } else {
                applyPowerUp(); // Still call to log "No power-up available"
                return "No power-up available";
            }
        } else if (maze.isValidMove(currentX, currentY, cmd)) {
            move(cmd);
            return "Moved " + cmd + " to (" + currentX + "," + currentY + ")";
        } else {
            return "Invalid move or command";
        }
    }
}