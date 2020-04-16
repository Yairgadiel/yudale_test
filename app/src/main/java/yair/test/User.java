package yair.test;

public class User {

    // region Members

    private String _emailAddress;
    private String _password;
    private String _name;

    // endregion

    // region C'tor

    public User() {}

    public User(final String emailAddress, final String passowrd) {
        _emailAddress = emailAddress;
        _password = passowrd;
    }

    public User(final String name, final String emailAddress, final String passowrd) {
        _name = name;
        _emailAddress = emailAddress;
        _password = passowrd;
    }

    // endregion

    // region Properties

    public String getEmailAddress() {
        return _emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this._emailAddress = emailAddress;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }


    // endregion
}
