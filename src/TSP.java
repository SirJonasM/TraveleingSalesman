public interface TSP {
    int BEGINWITHALLPOINT = -1;
    int BEGINWITHATENTH = -2;
    boolean evolution();
    void start();
    int getCurrentPoints();
    int getMaxPoints();

    int getCurrentCounter();
    int getGeneration();
    void addPoint();
}
