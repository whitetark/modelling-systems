package hospital;

import java.util.HashMap;
import java.util.Map;

public class
NextElementsOnClientType {
    Map<ClientType, NextElements> nextElementsOnClientType;

    public NextElementsOnClientType(Map<ClientType, NextElements> nextElementsOnClientType) {
        this.nextElementsOnClientType = nextElementsOnClientType;
    }

    public Element getNextElement(ClientType clientType) {
        NextElements nextElement =  nextElementsOnClientType.get(clientType);
        if(nextElement != null) {
            return nextElement.getNextElement();
        }
        else return null;
    }
}
