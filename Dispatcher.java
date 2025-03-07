public class Dispatcher {
    private Rider[] riders;
    private int MAX_RIDERS;
    private int usersAttended = 0;
    private int availableRiders;
    private int goal;
    private boolean justCars = true;
    private boolean justMotorcicles = true;

    public Dispatcher(int bipbip, int ridery, int yummy, int goal) {
        this.MAX_RIDERS = bipbip + ridery + yummy;
        availableRiders = MAX_RIDERS;
        this.goal = goal;
        riders = new Rider[MAX_RIDERS];
        initRiders(bipbip, ridery, yummy);
    }

    private void verifyVehicles(Rider rider) {
        justCars = justCars && rider.getService() == Person.service.CAR;
        justMotorcicles = justMotorcicles && rider.getService() == Person.service.MOTORCYCLE;
    }

    public void initRiders(int bipbip, int ridery, int yummy) {
        int i = 0;
        for (i = 0; i < bipbip; i++) {
            riders[i] = new Rider(i,Person.app.BIPBIP);
            verifyVehicles(riders[i]);
        }
        ridery += bipbip;
        for (; i < ridery; i++) {
            riders[i] = new Rider(i,Person.app.RIDERY);
            verifyVehicles(riders[i]);
        }
        yummy += ridery;
        for (; i < yummy; i++) {
            riders[i] = new Rider(i,Person.app.YUMMY);
            verifyVehicles(riders[i]);
        }
    }

    public void requestRider(Person.service service, Person.app app, int ID) {
        if (service == Person.service.CAR && justMotorcicles) {
            service = Person.service.MOTORCYCLE;
            System.out.println("- USER " + ID + ": CAR NO DISPONIBLE, SOLICITA VIAJE EN " + service + " (" + app + ")");
        } else if (service == Person.service.MOTORCYCLE && justCars) {
            service = Person.service.CAR;
            System.out.println("- USER " + ID + ": MOTORCYCLE NO DISPONIBLE, SOLICITA VIAJE EN " + service + " (" + app + ")");
        } else {
            System.out.println("- USER " + ID + ": SOLICITA VIAJE EN " + service + " (" + app + ")");
        }
        int actualRider = requestFirstRider(service, app, ID);
        int newRider = waitForArrive(service, app, actualRider, ID);
        while(!riders[newRider].travelFinished()){
            try {
                Thread.sleep(riders[newRider].getTravelTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("- USER " + ID + ": LLEGO A SU DESTINO GRACIAS AL RIDER " + newRider);
        usersAttended++;
        if(usersAttended == goal){
            endRiders();
        }
    }

    private int requestFirstRider(Person.service service, Person.app app, int ID) {
        int actualRider = -1;
        actualRider = getMinRider(service, app);
        while (actualRider == -1) {
            Thread.yield();
            if(availableRiders > 0) {
                actualRider = getMinRider(service, app);
            }
        }
        System.out.println("- RIDER " + actualRider + ": ACEPTO VIAJE DEL USER " + ID);
        availableRiders++;
        return actualRider;
    }

    private synchronized int getMinRider(Person.service service, Person.app app) {
        int minArrival = Integer.MAX_VALUE;
        int riderIndex = -1;
        for (int i = 0; i < MAX_RIDERS; i++) {
            if (riders[i].getArrivalTime() > minArrival) {
                continue;
            }
            if (riders[i].getArrivalTime() < minArrival && riders[i].isAvailable(service)) {
                minArrival = riders[i].getArrivalTime();
                riderIndex = i;
                continue;
            }
            if (riderIndex != -1 && riders[riderIndex].getApp() != app && riders[i].isAvailable(service, app)) {
                minArrival = riders[i].getArrivalTime();
                riderIndex = i;
            }
        }
        if(riderIndex == -1) {
            return -1;
        }
        riders[riderIndex].arrive();
        availableRiders--;
        return riderIndex;
    }

    private int waitForArrive(Person.service service, Person.app app, int actualRider, int ID) {
        while (!riders[actualRider].arrivalFinished()) {
            Thread.yield();
            actualRider = getNewRider(service, app, actualRider, ID);
        }
        System.out.println("- RIDER " + actualRider + ": YA LLEGO A LA UBICACION DEL USER " + ID);
        riders[actualRider].travel();
        return actualRider;
    }

    private synchronized int getNewRider(Person.service service, Person.app app, int actualRider, int ID) {
        int minArrival = riders[actualRider].getArrivalTime();
        int riderIndex = -1;
        for (int i = 0; i < MAX_RIDERS; i++) {
            if (riders[i].getArrivalTime() > minArrival) {
                continue;
            }
            if (riders[i].getArrivalTime() < minArrival && riders[i].isAvailable(service)) {
                minArrival = riders[i].getArrivalTime();
                riderIndex = i;
                continue;
            }
            if (riders[actualRider].getApp() != app && riders[i].isAvailable(service, app)) {
                minArrival = riders[i].getArrivalTime();
                riderIndex = i;
            }
        }
        if(riderIndex == -1) {
            return actualRider;
        }
        riders[actualRider].cancelTravel();
        System.out.println("- USER " + ID + ": CANCELO SU VIAJE CON EL RIDER " + actualRider);
        riders[riderIndex].arrive();
        System.out.println("- RIDER " + riderIndex + ": ES EL NUEVO RIDER DE " + ID);
        return riderIndex;
    }

    private void endRiders() {
        for (int i = 0; i < MAX_RIDERS; i++){
            riders[i].finishWork();
        }
    }
}
