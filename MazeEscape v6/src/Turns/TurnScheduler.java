package Turns;

import Agent.*;
import Maze.*;

public class TurnScheduler {
    private MazeManager maze;
    private Queue<Agent> agentQueue;

    public TurnScheduler(MazeManager maze, Queue<Agent> agentQueue) {
        this.maze = maze;
        this.agentQueue = agentQueue;
    }

    public Queue<Agent> getTurnOrder() {
        return agentQueue;
    }

    public Agent getNextAgent() {
        if (agentQueue.isEmpty()) {
            return null;
        }
        // Rotate through agents until a valid one is found
        for (int i = 0; i < agentQueue.size(); i++) {
            Agent agent = agentQueue.dequeue();
            if (!agent.hasReachedGoal() && !agent.isStuck()) {
                agentQueue.enqueue(agent); // Re-enqueue only active agents
                return agent;
            }
            // Skip finished agents (don't re-enqueue)
        }
        return null; // No active agents
    }

    public void addAgent(Agent a) {
        agentQueue.enqueue(a);
    }

    public void removeAgent(Agent a) {
        Queue<Agent> tempQueue = new Queue<>();
        while (!agentQueue.isEmpty()) {
            Agent current = agentQueue.dequeue();
            if (current != a) {
                tempQueue.enqueue(current);
            }
        }
        while (!tempQueue.isEmpty()) {
            agentQueue.enqueue(tempQueue.dequeue());
        }
    }

    public boolean isQueueEmpty() {
        return agentQueue.isEmpty();
    }
}