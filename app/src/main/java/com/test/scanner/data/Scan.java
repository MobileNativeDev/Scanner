package com.test.scanner.data;

import java.util.Objects;

public class Scan implements Comparable<Scan> {

    private final String value;
    private final int probability;

    public Scan(String value, int probability) {
        this.value = value;
        this.probability = probability;
    }

    public String getValue() {
        return value;
    }

    public int getProbability() {
        return probability;
    }

    @Override
    public int compareTo(Scan scan) {
        return scan.getProbability() - probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scan scan = (Scan) o;
        return probability == scan.probability && Objects.equals(value, scan.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, probability);
    }

}
