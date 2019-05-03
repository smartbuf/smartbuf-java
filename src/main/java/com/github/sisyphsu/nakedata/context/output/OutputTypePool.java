package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.IDPool;
import com.github.sisyphsu.nakedata.context.ContextField;
import com.github.sisyphsu.nakedata.context.ContextStruct;
import com.github.sisyphsu.nakedata.context.ContextType;

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

    /**
     * IDPool, which used for id allocation.
     */
    private IDPool pool = new IDPool(1000);
    /**
     * Root node of type-tree.
     */
    private TreeNode root = new TreeNode();

    /**
     * 类型表, KEY为[struct-id > types]
     */
    private Map<int[], ContextType> typeMap = new HashMap<>();

    public int getType(ContextStruct struct, int[] types) {
        return 0;
    }

    /**
     * Get the unique type-id for the specified fields.
     *
     * @param fields Fields of one customized type.
     * @return type-id
     */
    public int getTypeID(List<ContextField> fields) {
        if (fields == null || fields.size() == 0) {
            throw new IllegalArgumentException("fields can't be empty");
        }
        TreeNode node = root;
        for (ContextField field : fields) {
            node = node.getSubNode(field.getName().getId());
            node = node.getSubNode(field.getType().getId());
        }
        if (node.type == null) {
            // TODO Create new context-type, may need to release some unactive.
//            node.type = new ContextType(pool.acquire(), fields);
        }
        return node.type.getId();
    }

    /**
     * Represent one node of type-tree.
     */
    public static class TreeNode {

        /**
         * Current cached type, could be null.
         */
        private ContextType type;
        /**
         * Next level node's Map, Key may be field name's ID or field type's ID.
         */
        private Map<Integer, TreeNode> subNodeMap = new HashMap<>();

        /**
         * Get the specified sub node.
         *
         * @param id Field name's ID or field type's ID.
         * @return The result node in Type-Tree
         */
        public TreeNode getSubNode(int id) {
            TreeNode node = subNodeMap.get(id);
            if (node == null) {
                node = new TreeNode();
                subNodeMap.put(id, node);
            }
            return node;
        }

    }

}
