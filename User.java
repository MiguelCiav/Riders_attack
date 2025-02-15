public class User extends Person implements Runnable{
    private Dispatcher dispatcher;

    public User(int ID, Dispatcher dispatcher) {
        super(ID);
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        dispatcher.requestRider(personService, personApp);
    }
}