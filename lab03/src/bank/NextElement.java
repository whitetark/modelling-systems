package bank;

public class NextElement {
    protected Element element;
    protected double probability;
    protected int priority;

    public NextElement(Element element, double probability, int priority) {
        this.element = element;
        this.probability = probability;
        this.priority = priority;
    }

    public NextElement(Element element, int priority) {
        this.element = element;
        this.priority = priority;
    }

    public NextElement(Element element, double probability) {
        this.element = element;
        this.probability = probability;
    }
}
