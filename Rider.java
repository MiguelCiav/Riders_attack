public class Rider extends Person {
    private status riderStatus;
    private int travelTime;
    private int arrivalTime;
    private RiderTimer riderTimer;

    public Rider(int ID) {
        super(ID);
        riderStatus = status.AVAILABLE;
        travelTime = (int) (Math.random() * 50 + 1);
        arrivalTime = (int) (Math.random() * 30 + 1);
        riderTimer = new RiderTimer();
        System.out.println("RIDER " + ID + " CREADO " + "(" + personApp + ", " + personService + ", " + arrivalTime +  ", " + travelTime + ")");
    }

    public void arrive() {
        riderStatus = status.BUSY;
        riderTimer.countdown(arrivalTime);
    }

    public boolean arrivalFinished() {
        if(riderTimer.isFinished()) {
            riderTimer.reset();
            return true;
        }
        return false;
    }

    public void travel() {
        riderTimer.countdown(travelTime);
    }

    public boolean travelFinished() {
        if(riderTimer.isFinished()) {
            riderStatus = status.AVAILABLE;
            riderTimer.reset();
            return true;
        }
        return false;
    }

    public void cancelTravel() {
        riderTimer.reset();
        this.riderStatus = status.AVAILABLE;
    }

    public boolean isAvailable(service riderService, app riderApp) {
        return riderStatus == status.AVAILABLE && personService == riderService && personApp == riderApp;
    }

    public boolean isAvailable(service riderService) {
        return riderStatus == status.AVAILABLE && personService == riderService;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
}
