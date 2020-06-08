package Model;

public class InHouse extends Part {
    // Fields
    private int machineid;

    // Constructors
    public InHouse(int id, int stock, int min, int max, String name, double price, int machineid) {
        super(id, stock, min, max, name, price);
        this.machineid = machineid;
    }

    // Getters & Setters
    public int getMachineid() {
        return machineid;
    }

    public void setMachineid(int machineid) {
        this.machineid = machineid;
    }

    // Methods
}