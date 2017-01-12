package io.github.kkysen.quicktrip.app.javafx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GridRows {
    
    private final GridPane grid;
    private final ObservableList<Node> childList;
    private final List<Node[]> rows = new ArrayList<>();
    private final Map<Integer, Nodes> rowsWithNodes = new HashMap<>();
    private int size = 0;
    
    public void addExistingNodesInGridPane() {
        final Map<Integer, Map<Integer, Node>> nodeLocations = new TreeMap<>();
        for (final Node node : childList) {
            //System.out.println("node: " + node);
            final int i = GridPane.getRowIndex(node);
            final int j = GridPane.getColumnIndex(node);
            if (nodeLocations.containsKey(i)) {
                nodeLocations.get(i).put(j, node);
            } else {
                final Map<Integer, Node> row = new TreeMap<>();
                row.put(j, node);
                nodeLocations.put(i, row);
            }
        }
        childList.clear();
        for (final Entry<Integer, Map<Integer, Node>> row : nodeLocations.entrySet()) {
            add(row.getValue().values().toArray(new Node[0]));
        }
    }
    
    public GridRows(final GridPane grid) {
        this.grid = grid;
        childList = grid.getChildren();
        addExistingNodesInGridPane();
    }
    
    public GridRows() {
        this(new GridPane());
    }
    
    private void addGridRow(final int rowIndex, final Node... children) {
        //System.out.println(rowIndex + ": " + Arrays.toString(children));
        //remove(children);
        //        for (final Node child : children) {
        //            child.setOnMouseClicked(event -> {
        //                System.out.println("\n" + child + "\n");
        //                rows.forEach(nodes -> System.out.println(Arrays.toString(nodes)));
        //            });
        //        }
        //grid.addRow(rowIndex, children);
        if (children == null) {
            grid.add(new Label(), 0, size);
            return;
        }
        for (int i = 0; i < children.length; i++) {
            Node child = children[i];
            if (child == null) {
                child = new Label();
            }
            grid.add(child, i, rowIndex);
        }
    }
    
    public void add(final Node... children) {
        rows.add(children);
        addGridRow(size++, children);
    }
    
    public void emptyRow() {
        add((Node[]) null);
    }
    
    public void addNodes(final Nodes nodes) {
        add(nodes.toNodeArray());
        rowsWithNodes.put(size, nodes);
    }
    
    private void remove(final Node... children) {
        for (final Node child : children) {
            childList.remove(child);
        }
    }
    
    public void addAll(final int index, final Collection<Node[]> nodeColl) {
        final int shift = nodeColl.size();
        final int end = index + shift;
        for (int i = index; i < end; i++) {
            final Node[] children = rows.get(i);
            remove(children);
            addGridRow(i + shift, children);
        }
        rows.addAll(index, nodeColl);
        final Iterator<Node[]> iter = nodeColl.iterator();
        for (int i = index; i < end; i++) {
            addGridRow(i, iter.next());
        }
        size += shift;
    }
    
    public List<Node[]> removeRange(final int from, final int to) {
        final List<Node[]> removed = rows.subList(from, to);
        removed.forEach(this::remove);
        for (int i = from; i < to; i++) {
            rowsWithNodes.remove(i);
        }
        size -= to - from;
        final List<Node[]> removedCopy = new ArrayList<>(removed);
        removed.clear();
        return removedCopy;
    }
    
    public void add(final int rowIndex, final Node... children) {
        if (rowIndex == size) {
            add(children);
        }
        for (int i = size - 1; i >= rowIndex; i--) {
            final Node[] childrenInRow = rows.get(i);
            remove(childrenInRow);
            addGridRow(i + 1, childrenInRow);
        }
        rows.add(rowIndex, children);
        addGridRow(rowIndex, children);
        size++;
    }
    
    public void addNodes(final int rowIndex, final Nodes nodes) {
        add(rowIndex, nodes.toNodeArray());
        rowsWithNodes.put(rowIndex, nodes);
    }
    
    public Node[] remove(final int rowIndex) {
        if (rowIndex == size - 1) {
            final Node[] children = rows.remove(rowIndex);
            remove(children);
            size--;
            return children;
        }
        return removeRange(rowIndex, rowIndex + 1).get(0);
    }
    
    public void clear() {
        childList.clear();
        rows.clear();
        rowsWithNodes.clear();
        size = 0;
    }
    
    public Map<Integer, Nodes> getRowsWithNodes() {
        return new HashMap<>(rowsWithNodes);
    }
    
    @Override
    public String toString() {
        return rows.parallelStream().map(Arrays::toString).collect(Collectors.joining("\n"));
    }
    
}