package Model;

public class Outsourced extends Part {
    // Fields
    private String companyName;

    // Constructors
    public Outsourced(int id, int stock, int min, int max, String name, double price, String companyName) {
        super(id, stock, min, max, name, price);
        this.companyName = companyName;
    }


    // Getters & Setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}