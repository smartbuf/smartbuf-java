package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.common.IDPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TypePool for output side.
 *
 * @author sulin
 * @since 2019-04-30 18:06:44
 */
public class OutputTypePool {

    private IDPool pool = new IDPool(1000);

    private Node root = new Node();

    public int getTypeID(List<ContextField> fields) {
        if (fields == null || fields.size() == 0) {
            throw new IllegalArgumentException("fields can't be empty");
        }
        Node node = root;
        for (ContextField field : fields) {
            node = node.getSubNode(field.getName().getId());
            node = node.getSubNode(field.getType().getId());
        }
        if (node.type == null) {
            node.type = new ContextType(pool.acquire(), fields);
        }
        return node.type.getId();
    }

    public static class Node {

        private ContextType type;
        private Map<Integer, Node> subNodeMap = new HashMap<>();

        private Node getSubNode(int id) {
            Node node = subNodeMap.get(id);
            if (node == null) {
                node = new Node();
                subNodeMap.put(id, node);
            }
            return node;
        }

    }

}
