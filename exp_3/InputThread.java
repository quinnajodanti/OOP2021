import main.java.com.oocourse.OutRequest;
import main.java.com.oocourse.StuRequest;

public class InputThread extends Thread {
    private final WaitQueue waitQueue;

    public InputThread(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        OutRequest outRequest = new OutRequest(System.in);
        while (true) {
            StuRequest stuRequest = outRequest.getNextRequest();
            // 请将ObjectA替换成合适的对象以加锁  (9)
            synchronized (ObjectA) {
                if (stuRequest == null) {
                    ObjectA.close();
                    ObjectA.notifyAll();
                    return;
                } else {
                    Request request = new Request(stuRequest.getLeaveTime(), 
                                                  stuRequest.getBackTime(), 
                                                  stuRequest.getDestination());
                    ObjectA.addRequest(request);
                    ObjectA.notifyAll();
                }
            }
        }
    }
}

