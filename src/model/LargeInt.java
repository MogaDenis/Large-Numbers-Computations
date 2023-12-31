package model;

import java.util.ArrayList;
import java.util.List;

public class LargeInt extends LargeNumber implements Comparable<LargeInt> {
    public LargeInt(Integer number) {
        if (number < 0) {
            this.negative = true;
            number = number * (-1);
        }

        while (number != 0) {
            Integer lastDigit = number % 10;

            this.digits.add(lastDigit);

            number /= 10;
        }
    }

    public LargeInt(String number) {
        if (number.charAt(0) == '-') {
            this.negative = true;
        }

        for (int index = number.length() - 1; index >= 0; index--) {
            char digit = number.charAt(index);

            this.digits.add(Character.getNumericValue(digit));
        }

        this.removeLeadingZeroes();
    }

    public LargeInt(LargeNumber other) {
        this.negative = other.negative;

        this.digits = new ArrayList<>(other.digits);
    }

    public LargeInt(List<Integer> digits) {
        this.digits = new ArrayList<>(digits);
    }

    @Override
    public LargeNumber addTo(LargeNumber other) {
        if (other.negative)
            return this.subtractFrom(other.absoluteValue());

        LargeInt otherLargeInt = new LargeInt(other);

        Integer sign = (this.negative) ? -1 : 1;

        this.negative = this.negative && ((LargeInt) this.absoluteValue()).compareTo(otherLargeInt) > 0;

        int remainder = 0;

        int firstIndex = 0, secondIndex = 0;

        while (firstIndex < this.digits.size() && secondIndex < otherLargeInt.digits.size()) {
            int result = Math.abs(sign * this.digits.get(firstIndex) + otherLargeInt.digits.get(secondIndex) + remainder);
            remainder = result / 10;
            result %= 10;

            this.digits.set(firstIndex, result);

            firstIndex++;
            secondIndex++;
        }

        while (firstIndex < this.digits.size()) {
            int result = Math.abs(sign * this.digits.get(firstIndex) + remainder);
            remainder = result / 10;
            result %= 10;

            this.digits.set(firstIndex, result);

            firstIndex++;
        }

        while (secondIndex < otherLargeInt.digits.size()) {
            int result = Math.abs(otherLargeInt.digits.get(secondIndex) + remainder);
            remainder = result / 10;
            result %= 10;

            this.digits.add(result);

            secondIndex++;
        }

        this.removeLeadingZeroes();

        return this;
    }

    @Override
    public LargeNumber subtractFrom(LargeNumber other) {
        if (other.negative)
            return this.addTo(other.absoluteValue());

        LargeInt otherLargeInt = new LargeInt(other);

        Integer sign = (this.negative) ? -1 : 1;

        this.negative = this.compareTo(otherLargeInt) < 0;

        int borrow = 0;

        int firstIndex = 0, secondIndex = 0;

        while (firstIndex < this.digits.size() && secondIndex < otherLargeInt.digits.size()) {
            int result = sign * this.digits.get(firstIndex) - otherLargeInt.digits.get(secondIndex) - borrow;

            if (result < 0) {
                borrow = 1;
                result = 10 + result;
            }
            else
                borrow = 0;

            this.digits.set(firstIndex, result);

            firstIndex++;
            secondIndex++;
        }

        while (firstIndex < this.digits.size()) {
            int result = sign * this.digits.get(firstIndex) - borrow;

            if (result < 0) {
                borrow = 1;
                result = 10 + result;
            }
            else
                borrow = 0;
            this.digits.set(firstIndex, result);

            firstIndex++;
        }

        while (secondIndex < otherLargeInt.digits.size()) {
            int result = otherLargeInt.digits.get(secondIndex) - borrow;

            if (result < 0) {
                borrow = 1;
                result = 10 + result;
            }
            else
                borrow = 0;

            this.digits.add(result);

            secondIndex++;
        }

        this.removeLeadingZeroes();

        return this;
    }

    @Override
    protected LargeNumber multiplyWithDigit(Integer digit) {
        LargeInt copy = new LargeInt(this);

        int remainder = 0;

        for (int index = 0; index < copy.digits.size(); index++) {
            int result = copy.digits.get(index) * digit + remainder;

            remainder = result / 10;
            result = result % 10;

            copy.digits.set(index, result);
        }

        if (remainder != 0)
            copy.digits.add(remainder);

        return copy;
    }

    @Override
    public LargeNumber multiplyWith(LargeNumber other) {
        this.negative = this.negative != other.negative;

        LargeInt otherLargeInt = new LargeInt(other);

        LargeInt result = new LargeInt(0);

        for (int index = 0; index < otherLargeInt.digits.size(); index++) {
            LargeInt multiplicationWithDigit = (LargeInt) this.multiplyWithDigit(otherLargeInt.digits.get(index));

            for (int count = 0; count < index; count++)
                multiplicationWithDigit.digits.addFirst(0);

            result.addTo(multiplicationWithDigit);
        }

        this.digits = result.digits;

        return this;
    }

    @Override
    public LargeNumber divideBy(LargeNumber other) {
        this.negative = this.negative != other.negative;

        int quotient = 0;

        LargeInt otherLargeInt = new LargeInt(other);

        while (((LargeInt) this.absoluteValue()).compareTo((LargeInt) otherLargeInt.absoluteValue()) >= 0) {
            this.digits = this.absoluteValue().subtractFrom(otherLargeInt.absoluteValue()).digits;

            quotient++;
        }

        LargeInt result = new LargeInt(quotient);
        this.digits = result.digits;

        return this;
    }

    @Override
    public LargeNumber absoluteValue() {
        return new LargeInt(this.digits);
    }

    @Override
    public String toString() {
        boolean isZero = true;

        StringBuilder stringRepresentation = new StringBuilder();

        for (Integer digit : this.digits) {
            if (digit != 0)
                isZero = false;

            stringRepresentation.insert(0, digit);
        }

        if (isZero)
            return "0";

        if (this.negative) {
            stringRepresentation.insert(0, '-');
        }

        return stringRepresentation.toString();
    }

    @Override
    public int compareTo(LargeInt other) {
        if (this.negative && !other.negative)
            return -1;

        if (!this.negative && other.negative)
            return 1;

        if (this.negative) {
            if (this.digits.size() > other.digits.size())
                return -1;

            if (this.digits.size() < other.digits.size())
                return 1;

            for (int index = this.digits.size() - 1; index >= 0; index--) {
                if (this.digits.get(index) > other.digits.get(index))
                    return -1;

                if (this.digits.get(index) < other.digits.get(index))
                    return 1;
            }

            return 0;
        }

        if (this.digits.size() < other.digits.size())
            return -1;

        if (this.digits.size() > other.digits.size())
            return 1;

        for (int index = this.digits.size() - 1; index >= 0; index--) {
            if (this.digits.get(index) < other.digits.get(index))
                return -1;

            if (this.digits.get(index) > other.digits.get(index))
                return 1;
        }

        return 0;
    }
}