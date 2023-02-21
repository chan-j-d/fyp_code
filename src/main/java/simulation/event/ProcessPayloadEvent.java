package simulation.event;

import simulation.network.entity.NetworkNode;
import simulation.network.entity.Payload;
import simulation.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static simulation.event.EventUtil.convertPayloadsToQueueEvents;

public class ProcessPayloadEvent<T> extends Event {

    private NetworkNode<T> node;
    private Payload<T> payload;
    private double processingEndTime;

    public ProcessPayloadEvent(double time, NetworkNode<T> node, Payload<T> payload) {
        super(time);
        this.node = node;
        this.payload = payload;
        this.processingEndTime = 0;
    }

    @Override
    public List<Event> simulate() {
        Pair<Double, List<Payload<T>>> durationPayloadsPair = node.processPayload(getTime(), payload);
        List<Payload<T>> processedPayloads = durationPayloadsPair.second();
        processingEndTime = getTime() + durationPayloadsPair.first();
        List<Event> eventList =
                new ArrayList<>(convertPayloadsToQueueEvents(processingEndTime, node, processedPayloads));
        eventList.add(new PollQueueEvent<>(processingEndTime, node));
        return eventList;
    }

    @Override
    public String toString() {
        return String.format("%s-%.3f (ProcessPayload): Processing payload at %s (%s)",
                super.toString(), processingEndTime, node, payload);
    }
}
