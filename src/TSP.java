public interface TSP {
    int BEGINWITHALLPOINTS = -1;
    int BEGINWITHATENTH = -2;
    boolean evolute();
    void start();
    int getCurrentPoints();
    int getMaxPoints();

    int getCurrentCounter();
    int getGeneration();
    void addPoint();

}
