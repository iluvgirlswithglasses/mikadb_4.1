package konasoft.mikadb.model.homepage;

public class LineChartCounterModel {
    private double previousSize;

    public LineChartCounterModel() {
        reset();
    }

    public void reset() {
        previousSize = 0.0;
    }

    public double devide(double a, double b) {
        if (a == 0 || b == 0) {
            previousSize = 0.0;
        } else {
            previousSize = a / b;
        }
        return previousSize;
    }

    public double getPreviousSize() {
        return previousSize;
    }
}
