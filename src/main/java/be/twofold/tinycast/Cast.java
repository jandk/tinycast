package be.twofold.tinycast;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public final class Cast extends AbstractList<CastNode> {
    private static final long INITIAL_HASH = 0x5A4C524E454C4156L;

    private final AtomicLong hasher;
    private final List<CastNode> rootNodes;

    Cast(List<CastNode> rootNodes) {
        this(0, rootNodes);
    }

    private Cast(long initialHash, List<CastNode> rootNodes) {
        this.hasher = new AtomicLong(initialHash);
        this.rootNodes = Objects.requireNonNull(rootNodes);
    }

    public static Cast create() {
        return new Cast(INITIAL_HASH, new ArrayList<>());
    }

    public static Cast create(long initialHash) {
        return new Cast(initialHash, new ArrayList<>());
    }

    public static Cast read(InputStream in) throws CastException {
        return CastReader.read(in);
    }

    @Override
    public int size() {
        return rootNodes.size();
    }

    @Override
    public CastNode get(int index) {
        return rootNodes.get(index);
    }

    public CastNodes.Root createRoot() {
        CastNodes.Root root = new CastNodes.Root(hasher);
        rootNodes.add(root);
        return root;
    }

    public void write(OutputStream out) throws IOException {
        CastWriter.write(this, out);
    }
}
