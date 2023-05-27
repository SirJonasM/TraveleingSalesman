public class VisualisationThread extends Thread{
    private final TSP tsp;
    private VisualizationFrame visualizationFrame;
    public VisualisationThread(TSP tsp){
        this.tsp = tsp;
    }

    @Override
    public void run() {
        visualizationFrame = new VisualizationFrame(tsp);
    }

    public void updateGraph(){
        visualizationFrame.drawGraph.updateUI();
    }
    public void updateInfo(){
        visualizationFrame.drawInfo.updateUI();
    }

}
