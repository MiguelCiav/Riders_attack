public abstract class Person {
    public static enum status {
        AVAILABLE, BUSY
    }
    public static enum service {
        MOTORCYCLE, CAR
    }
    public static enum app {
        BIPBIP, RIDERY, YUMMY
    }

    protected service personService;
    protected app personApp;
    protected int ID;

    public Person (int ID) {
        this.ID = ID;
        personService = service.values()[(int) (Math.random() * service.values().length)];
    }

    public service getService() {
        return personService;
    }

    public app getApp() {
        return personApp;
    }

    public int getID() {
        return ID;
    }
}
