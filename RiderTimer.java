public class RiderTimer extends Thread{
    private int time;
    private boolean finished;

    public RiderTimer() {
        this.time = 0;
        this.finished = false;
    }

    public void run() {
        while (time > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time--;
        }
        finished = true;
    }

    public void countdown(int seconds) {
        time = seconds;
        finished = false;
        this.start();
    }

    public boolean isFinished() {
        return finished;
    }

    public void reset() {
        time = 0;
        finished = false;
    }
}
