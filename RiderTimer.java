public class RiderTimer{
    private int time;
    private Counter counter;
    private boolean kill;

    public RiderTimer() {
        time = 0;
        kill = false;
        counter = new Counter();
        counter.start();
    }

    public void countdown(int seconds) {
        time = seconds;
        if(counter.getState() == Thread.State.TIMED_WAITING) {
            counter.interrupt();
        }
    }

    public boolean isFinished() {
        return time <= 0;
    }

    public void reset() {
        time = 0;
    }

    public void kill() {
        kill = true;
        if(counter.getState() == Thread.State.TIMED_WAITING) {
            counter.interrupt();
        }
    }

    private class Counter extends Thread {
        public void run () {
            while (true) {
                if(kill) {
                    break;
                }
                if(time <= 0){
                    try {
                        sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e) {
                        continue;
                    }
                }
                try {
                    sleep(1000);
                    time--;
                } catch (InterruptedException e) {
                    continue;
                }
            }
        }
    }
}
