package Model;

public class Address {
    private int Addressid;
    private int userid;
    private String doorno;
    private String street;
    private String city;
    private String state;
    private String Country;

    public Address(int addressid, int userid, String doorno, String street, String city, String state, String country) {
        Addressid = addressid;
        this.userid = userid;
        this.doorno = doorno;
        this.street = street;
        this.city = city;
        this.state = state;
        Country = country;
    }

    public Address() {

    }

    public Address(int userid, String doorno, String street, String city, String state, String country) {
        this.userid = userid;
        this.doorno = doorno;
        this.street = street;
        this.city = city;
        this.state = state;
        Country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "Addressid=" + Addressid +
                ", userid=" + userid +
                ", doorno='" + doorno + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", Country='" + Country + '\'' +
                '}';
    }

    public int getAddressid() {
        return Addressid;
    }

    public void setAddressid(int addressid) {
        Addressid = addressid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getDoorno() {
        return doorno;
    }

    public void setDoorno(String doorno) {
        this.doorno = doorno;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }
}
