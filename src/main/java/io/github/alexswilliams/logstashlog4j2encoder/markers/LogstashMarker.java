package io.github.alexswilliams.logstashlog4j2encoder.markers;


import org.apache.logging.log4j.Marker;

public class LogstashMarker implements Marker {
    @Override
    public Marker addParents(Marker... markers) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Marker[] getParents() {
        return new Marker[0];
    }

    @Override
    public boolean hasParents() {
        return false;
    }

    @Override
    public boolean isInstanceOf(Marker m) {
        return false;
    }

    @Override
    public boolean isInstanceOf(String name) {
        return false;
    }

    @Override
    public boolean remove(Marker marker) {
        return false;
    }

    @Override
    public Marker setParents(Marker... markers) {
        return null;
    }
}
