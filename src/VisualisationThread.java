import javax.swing.*;

public class VisualisationThread extends Thread {
    public boolean isRunning = true;
    private volatile boolean updateGraph = true;
    private volatile boolean updateInfo = true;
    private final TSP tsp;
    private VisualizationFrame visualizationFrame;
    public VisualisationThread(TSP tsp){
        this.tsp = tsp;
    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(()->{
                visualizationFrame = new VisualizationFrame(tsp);
        });
        while (isRunning){
            if(updateGraph){
                SwingUtilities.invokeLater(this::updateGraph);
                updateGraph = false;
            }
            if(updateInfo){
                SwingUtilities.invokeLater(this::updateInfo);
                updateInfo = false;
            }
        }
    }


    public void setUpdateGraph(boolean updateGraph) {
        this.updateGraph = updateGraph;
    }
    public void setUpdateInfo(boolean updateInfo){
        this.updateInfo = updateInfo;
    }
    public void updateGraph(){
        visualizationFrame.drawGraph.updateUI();
    }
    public void updateInfo(){
        visualizationFrame.drawInfo.updateUI();
    }

    public void stopThread(){
        isRunning = false;
    }

}
