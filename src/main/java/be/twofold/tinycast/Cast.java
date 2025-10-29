package be.twofold.tinycast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a Cast file, which is a container format for models, animations, materials, and game worlds.
 * <p>
 * A Cast file consists of a collection of root nodes that form a scene. Each Cast instance maintains
 * a list of {@link CastNode} objects and manages unique node hashing for linking nodes together.
 * <p>
 * The Cast format uses little-endian byte order and begins with a header containing magic number,
 * version, root node count, and flags. This class provides methods for creating, reading, and writing
 * Cast files, as well as managing root nodes within the scene.
 *
 * @see CastNode
 * @see CastNodes.Root
 */
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

    /**
     * Creates a new empty Cast instance with the default initial hash.
     * <p>
     * The returned Cast will have no root nodes and be ready for new nodes to be added.
     *
     * @return a new empty Cast instance
     */
    public static Cast create() {
        return new Cast(INITIAL_HASH, new ArrayList<>());
    }

    /**
     * Creates a new empty Cast instance with the specified initial hash.
     * <p>
     * This allows customization of the hash sequence used for generating node identifiers.
     *
     * @param initialHash the initial hash value to use for node hash generation
     * @return a new empty Cast instance with the specified initial hash
     */
    public static Cast create(long initialHash) {
        return new Cast(initialHash, new ArrayList<>());
    }

    /**
     * Reads a Cast file from the specified input stream.
     * <p>
     * This method deserializes a Cast file format, including the header, root nodes,
     * and all child nodes with their properties. The stream should contain a valid
     * Cast file with the magic number 0x74736163 ('cast') and version 0x1.
     *
     * @param in the input stream to read from
     * @return a Cast instance containing the deserialized data
     * @throws CastException if an error occurs while reading or parsing the Cast file
     */
    public static Cast read(InputStream in) throws CastException {
        return CastReader.read(in);
    }

    /**
     * Returns the number of root nodes in this Cast file.
     * <p>
     * Root nodes represent scenes and are the top-level containers in a Cast file.
     *
     * @return the number of root nodes
     */
    @Override
    public int size() {
        return rootNodes.size();
    }

    /**
     * Returns the root node at the specified index.
     *
     * @param index the index of the root node to return
     * @return the root node at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public CastNode get(int index) {
        return rootNodes.get(index);
    }

    /**
     * Creates and adds a new root node to this Cast file.
     * <p>
     * Root nodes represent scenes in a Cast file and can contain child nodes such as
     * models, animations, instances, and metadata. Each root node is assigned a unique
     * hash value for linking with other nodes.
     * <p>
     * All children of a root node must have unique hashes.
     *
     * @return the newly created root node
     */
    public CastNodes.Root createRoot() {
        CastNodes.Root root = new CastNodes.Root(hasher);
        rootNodes.add(root);
        return root;
    }

    /**
     * Writes this Cast file to the specified output stream.
     * <p>
     * This method serializes the Cast file format, including the header with magic number,
     * version, root node count, and all nodes with their properties in the proper order.
     * Nodes are written in a stack layout (FILO order) following the Cast specification.
     *
     * @param out the output stream to write to
     * @throws CastException if an error occurs while writing the Cast file
     */
    public void write(OutputStream out) throws CastException {
        CastWriter.write(this, out);
    }
}
