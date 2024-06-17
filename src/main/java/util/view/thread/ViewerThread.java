package util.view.thread;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;

public class ViewerThread extends Thread {

    private final Graph displayGraph;
    private Viewer viewer;

    public ViewerThread(Graph displayGraph) {
        this.displayGraph = displayGraph;
        this.start();
    }

    @Override
    public void run() {
        this.viewer = displayGraph.display();
        this.viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

        try { this.closeViewer(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private synchronized void closeViewer() throws InterruptedException {
        this.wait();
        viewer.close();
    }

    public synchronized void notifyViewer() {
        this.notifyAll();
    }
}
