class Darts {
    double x;
    double y;
    Darts(double x, double y) {
        this.x = x;
        this.y = y;
    }

    int score() {
        double dist = Math.sqrt(x * x + y * y);
        if (dist <= 1.0) return 10;
        if (dist <= 5.0) return 5;
        if (dist <= 10.0) return 1;
        return 0;
    }

}
