package hospital;

import java.util.Map;

public class
NextEventsOnClientType {
    Map<ClientType, NextEvents> nextElementsOnClientType;

    public NextEventsOnClientType(Map<ClientType, NextEvents> nextElementsOnClientType) {
        this.nextElementsOnClientType = nextElementsOnClientType;
    }

    public Event getNextElement(ClientType clientType) {
        NextEvents nextElement =  nextElementsOnClientType.get(clientType);
        if(nextElement != null) {
            return nextElement.getNextElement();
        }
        else return null;
    }
}
