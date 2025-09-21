package be.twofold.tinycast;

import java.io.*;
import java.util.*;

public final class CastWriter {
    private final BinaryWriter writer;

    CastWriter(BinaryWriter writer) {
        this.writer = Objects.requireNonNull(writer);
    }

    public static void write(Cast cast, OutputStream out) {
    }
}
