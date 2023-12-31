package model;

import java.util.ArrayList;
import java.util.List;

public abstract class LargeNumber {
    protected List<Integer> digits = new ArrayList<>();
    protected Boolean negative = false;

    public abstract LargeNumber addTo(LargeNumber other);
    public abstract LargeNumber subtractFrom(LargeNumber other);
    public abstract LargeNumber multiplyWith(LargeNumber other);
    protected abstract LargeNumber multiplyWithDigit(Integer digit);
    public abstract LargeNumber divideBy(LargeNumber other);
    public abstract LargeNumber absoluteValue();

    protected void removeLeadingZeroes() {
        while (!this.digits.isEmpty() && this.digits.getLast() == 0) {
            this.digits.removeLast();
        }
    }
}
