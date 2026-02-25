class PeopleRecord {
    String givenName;
    String familyName;
    String companyName;
    String address;
    String city;
    String county;
    String state;
    String zip;
    String phone1;
    String phone2;
    String email;
    String web;
    String birthday;

    PeopleRecord left;
    PeopleRecord right;

    public PeopleRecord(String dataLine) {
        String[] parts = dataLine.split(";", -1);
        if (parts.length >= 13) {
            this.givenName = parts[0];
            this.familyName = parts[1];
            this.companyName = parts[2];
            this.address = parts[3];
            this.city = parts[4];
            this.county = parts[5];
            this.state = parts[6];
            this.zip = parts[7];
            this.phone1 = parts[8];
            this.phone2 = parts[9];
            this.email = parts[10];
            this.web = parts[11];
            this.birthday = parts[12];
        }
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getEmail() {
        return email;
    }

    public String getWeb() {
        return web;
    }

    public String getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return givenName + " " + familyName + " (" + email + ")";
    }
}
