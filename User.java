public class User extends Person implements Runnable{
    private Dispatcher dispatcher;

    public User(int ID, Dispatcher dispatcher) {
        super(ID);
        personApp = app.values()[(int) (Math.random() * app.values().length)];
        this.dispatcher = dispatcher;
        System.out.println("- USER " + ID + ": " + personApp + ", " + personService);
    }

    @Override
    public void run() {
        dispatcher.requestRider(personService, personApp, ID);
    }
}