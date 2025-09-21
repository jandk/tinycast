package be.twofold.tinycast;

import java.io.*;
import java.util.*;

final class CastReader {
    private static final Map<CastNodeID, Set<String>> ARRAY_TYPES = Map.of(
        CastNodeID.MESH, Set.of("vp", "vn", "vt", "c%d", "u%d", "wb", "wv", "f"),
        CastNodeID.HAIR, Set.of("se", "pt"),
        CastNodeID.BLEND_SHAPE, Set.of("vi", "vp", "ts"),
        CastNodeID.CURVE, Set.of("kb", "kv"),
        CastNodeID.NOTIFICATION_TRACK, Set.of("kb")
    );

    private final BinaryReader reader;

    CastReader(BinaryReader reader) {
        this.reader = Objects.requireNonNull(reader);
    }

    static Cast read(InputStream in) throws CastException {
        try (BinaryReader reader = new BinaryReader(new BufferedInputStream(in))) {
            return new CastReader(reader).read();
        } catch (IOException e) {
            throw new CastException("Error reading cast file", e);
        }
    }

    private Cast read() throws IOException {
        int magic = reader.readInt();
        if (magic != 0x74736163) {
            throw new CastException("Invalid magic number: " + magic);
        }

        int version = reader.readInt();
        if (version != 1) {
            throw new CastException("Invalid version: " + version);
        }

        int rootNodeCount = reader.readInt();
        int flags = reader.readInt();
        if (flags != 0) {
            throw new CastException("Invalid flags: " + flags);
        }

        List<CastNode> rootNodes = new ArrayList<>(rootNodeCount);
        for (int i = 0; i < rootNodeCount; i++) {
            rootNodes.add(readNode());
        }
        return new Cast(rootNodes);
    }

    private CastNode readNode() {
        return null;
    }
}
