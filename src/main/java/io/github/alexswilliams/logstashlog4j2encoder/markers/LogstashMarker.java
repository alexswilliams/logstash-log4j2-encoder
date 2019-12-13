package io.github.alexswilliams.logstashlog4j2encoder.markers;


import org.apache.logging.log4j.Marker;
import org.jetbrains.annotations.Nullable;

public class LogstashMarker implements Marker {

    private static final Marker[] EMPTY_MARKER_ARRAY = new Marker[0];

    @Override
    public @Nullable Marker addParents(final Marker... markers) {
        return null;
    }

    @Override
    public @Nullable String getName() {
        return null;
    }

    @Override
    public Marker[] getParents() {
        return EMPTY_MARKER_ARRAY;
    }

    @Override
    public boolean hasParents() {
        return false;
    }

    @Override
    public boolean isInstanceOf(final Marker m) {
        return false;
    }

    @Override
    public boolean isInstanceOf(final String name) {
        return false;
    }

    @Override
    public boolean remove(final Marker marker) {
        return false;
    }

    @Override
    public @Nullable Marker setParents(final Marker... markers) {
        return null;
    }
}
