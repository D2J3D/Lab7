package Common.core;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Double x; //Значение поля должно быть больше -250, Поле не может быть null
    private Double y; //Поле не может быть null

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Coordinates(){}

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }
}
