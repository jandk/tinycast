package be.twofold.tinycast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
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
public final class Cast {
    private final AtomicLong hasher;
    private final List<CastNode> rootNodes;

    Cast(AtomicLong hasher, List<CastNode> rootNodes) {
        this.hasher = Objects.requireNonNull(hasher);
        this.rootNodes = Objects.requireNonNull(rootNodes);
    }

    private Cast(long initialHash, List<CastNode> rootNodes) {
        this(new AtomicLong(initialHash), rootNodes);
    }

    /**
     * Creates a new empty Cast instance with the default initial hash.
     * <p>
     * The returned Cast will have no root nodes and be ready for new nodes to be added.
     *
     * @return a new empty Cast instance
     */
    public static Cast create() {
        return create(ThreadLocalRandom.current().nextLong());
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
     * Retrieves the list of root nodes in this Cast instance.
     * Root nodes represent the highest-level entries in the hierarchy and
     * can contain child nodes such as models, animations, and metadata.
     *
     * @return an unmodifiable list of root nodes in this Cast instance
     */
    public List<CastNode> getRootNodes() {
        return Collections.unmodifiableList(rootNodes);
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

    /**
     * Finds a node in this Cast by its hash value.
     *
     * @param hash the hash to search for
     * @return the node with the given hash, or empty if not found
     */
    public Optional<CastNode> findNodeByHash(long hash) {
        for (CastNode root : rootNodes) {
            Optional<CastNode> result = findNodeByHash(root, hash);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a node of a specific type in this Cast by its hash value.
     *
     * @param hash the hash to search for
     * @param type the expected type of the node
     * @return the node with the given hash cast to the expected type, or empty if not found or wrong type
     */
    public <T extends CastNode> Optional<T> findNodeByHash(long hash, Class<T> type) {
        return findNodeByHash(hash)
            .filter(type::isInstance)
            .map(type::cast);
    }

    private Optional<CastNode> findNodeByHash(CastNode node, long hash) {
        if (node.getHash() == hash) {
            return Optional.of(node);
        }
        for (CastNode child : node.children) {
            Optional<CastNode> result = findNodeByHash(child, hash);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof Cast
            && rootNodes.equals(((Cast) o).rootNodes);
    }

    @Override
    public int hashCode() {
        return rootNodes.hashCode();
    }

    @Override
    public String toString() {
        return "Cast(" + rootNodes.size() + " root nodes)";
    }
}
