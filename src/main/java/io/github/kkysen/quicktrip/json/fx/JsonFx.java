package io.github.kkysen.quicktrip.json.fx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Node;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class JsonFx {
    
    private final Map<String, Node> ids = new HashMap<>();
    private final Map<Node, String> reverseIds = new HashMap<>();
    
    private final List<Node> nodes = new ArrayList<Node>() {
        
        private static final long serialVersionUID = -1900139564238734712L;
        
        @Override
        public Node remove(final int index) {
            final Node node = super.remove(index);
            final String id = reverseIds.remove(node);
            ids.remove(id);
            return node;
        }
        
    };
    
    public JsonFx() {}
    
    public void add(final String id, final Node node) {
        ids.put(id, node);
        nodes.add(node);
    }
    
    public List<Node> getNodes() {
        return nodes;
    }
    
}
