public class Dispatcher {
    private Rider[] riders;
    private int MAX_RIDERS;

    public Dispatcher(int MAX_RIDERS) {
        this.MAX_RIDERS = MAX_RIDERS;
        riders = new Rider[MAX_RIDERS];
        initRiders();
    }

    public void initRiders() {
        for (int i = 0; i < MAX_RIDERS; i++) {
            riders[i] = new Rider(i);
        }
    }

    private int getMinRider(Person.service service, Person.app app) {
        int minArrival = Integer.MAX_VALUE;
        int riderIndex = -1;
        for (int i = 0; i < MAX_RIDERS; i++) {
            if (riders[i].getArrivalTime() > minArrival) {
                continue;
            }
            if (riders[i].isAvailable(service, app)) {
                minArrival = riders[i].getArrivalTime();
                riderIndex = i;
                continue;
            }
            if (riders[i].isAvailable(service)) {
                minArrival = riders[i].getArrivalTime();
                riderIndex = i;
            }
        }
        return riderIndex;
    }

    private int getFirstRider(Person.service service, Person.app app) {
        int actualRider = -1;
        actualRider = getMinRider(service, app);
        while (actualRider == -1) {
            Thread.yield();
            actualRider = getMinRider(service, app);
        }
        riders[actualRider].arrive();
        return actualRider;
    }

    private int waitForArrive(Person.service service, Person.app app, int actualRider) {
        int newRider;
        while (!riders[actualRider].arrivalFinished()) {
            newRider = getMinRider(service, app);
            if(newRider == -1) {
                Thread.yield();
                continue;
            }
            if(riders[newRider].getArrivalTime() < riders[actualRider].getArrivalTime()) {
                riders[actualRider].cancelTravel();
                riders[newRider].arrive();
                actualRider = newRider;
            }
            Thread.yield();
        }
        riders[actualRider].travel();
        return actualRider;
    }

    public synchronized void requestRider(Person.service service, Person.app app) {
        int actualRider = getFirstRider(service, app);
        actualRider = waitForArrive(service, app, actualRider);
        while(!riders[actualRider].travelFinished()){
            Thread.yield();
        }
    }
}
