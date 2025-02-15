public class Dispatcher {
    private Rider[] riders;
    private int MAX_RIDERS;
    private int usersAttended = 0;

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

    private synchronized int getMinRider(Person.service service, Person.app app) {
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
        if(riderIndex == -1) {
            return -1;
        }
        riders[riderIndex].arrive();
        return riderIndex;
    }

    private synchronized int getNewRider(Person.service service, Person.app app, int actualRider, int ID) {
        int minArrival = riders[actualRider].getArrivalTime();
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
        if(riderIndex == -1) {
            return actualRider;
        }
        riders[actualRider].cancelTravel();
        System.out.println("EL USER " + ID + ", CANCELÓ SU VIAJE CON EL RIDER " + actualRider + ", PREFIRIÓ AL RIDER " + riderIndex);
        riders[riderIndex].arrive();
        System.out.println("EL RIDER " + riderIndex + " ACEPTÓ EL VIAJE DEL USER " + ID + ", YA VA EN CAMINO");
        return riderIndex;
    }

    private int getFirstRider(Person.service service, Person.app app, int ID) {
        int actualRider = -1;
        actualRider = getMinRider(service, app);
        while (actualRider == -1) {
            actualRider = getMinRider(service, app);
        }
        riders[actualRider].arrive();
        System.out.println("EL RIDER " + actualRider + " ACEPTÓ EL VIAJE DEL USER " + ID + ", YA VA EN CAMINO");
        return actualRider;
    }

    private int waitForArrive(Person.service service, Person.app app, int actualRider, int ID) {
        while (!riders[actualRider].arrivalFinished()) {
            Thread.yield();
            actualRider = getNewRider(service, app, actualRider, ID);
        }
        riders[actualRider].travel();
        return actualRider;
    }

    public void requestRider(Person.service service, Person.app app, int ID) {
        System.out.println("USER " + ID + " SOLICITÓ UN VIAJE CON " + app + " EN " + service);
        int actualRider = getFirstRider(service, app, ID);
        int newRider = waitForArrive(service, app, actualRider, ID);
        while(!riders[newRider].travelFinished()){
            Thread.yield();
        }
        System.out.println("USER " + ID + " LLEGÓ A SU DESTINO GRACIAS AL RIDER " + newRider);
        usersAttended++;
        System.out.println("USUARIOS ATENDIDOS: " + usersAttended);
    }
}
