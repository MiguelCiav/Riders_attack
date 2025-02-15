public class RiderTimer{
    private int time;
    private Counter counter;
    private boolean on;

    public RiderTimer() {
        this.time = 0;
        this.on = false;
        counter = new Counter();
        counter.start();
    }

    public void countdown(int seconds) {
        on = true;
        time = seconds;
    }

    public boolean isFinished() {
        return time <= 0;
    }

    public void reset() {
        on = false;
    }

    private class Counter extends Thread {
        public void run () {
            while (true) {
                if(!on) {
                    Thread.yield();
                    continue;
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(time < 0){
                    reset();
                }
                time--;
            }
        }
    }
}
