package simulation.network.entity;

import simulation.statistics.ConsensusStatistics;
import simulation.util.rng.RandomNumberGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Validator<T> extends TimedNode<T> {

    private final int id;
    private final Map<Integer, Validator<T>> allNodes;
    private final ConsensusStatistics statistics;


    public Validator(String name, int id, NodeTimerNotifier<T> timerNotifier,
            RandomNumberGenerator serviceTimeGenerator, Collection<Object> states) {
        super(name, timerNotifier, serviceTimeGenerator);
        this.allNodes = new HashMap<>();
        this.id = id;
        this.statistics = new ConsensusStatistics(states);
    }

    public void setAllNodes(List<? extends Validator<T>> allNodes) {
        for (Validator<T> node : allNodes) {
            this.allNodes.put(node.getId(), node);
        }
    }

    public Collection<Validator<T>> getAllNodes() {
        return allNodes.values();
    }

    public Validator<T> getNode(int id) {
        return allNodes.get(id);
    }

    protected void broadcastMessageToAll(T message) {
        broadcastMessage(message, getAllNodes());
    }

    public int getId() {
        return id;
    }

    public ConsensusStatistics getConsensusStatistics() {
        return statistics;
    }

    public abstract int getConsensusCount();
    public abstract Object getState();

    @Override
    public void registerTimeElapsed(double time) {
        statistics.addTime(getState(), time);
        statistics.setConsensusCount(getConsensusCount());
    }
}