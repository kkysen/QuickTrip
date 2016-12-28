package io.github.kkysen.quicktrip.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridRows {
    
    private final GridPane grid;
    private final ObservableList<Node> childList;
    private final List<Node[]> rows = new ArrayList<>();
    private int size = 0;
    
    public GridRows(final GridPane grid) {
        this.grid = grid;
        childList = grid.getChildren();
    }
    
    public GridRows() {
        this(new GridPane());
    }
    
    private void addGridRow(final int rowIndex, final Node... children) {
        System.out.println(rowIndex + ": " + Arrays.toString(children));
        grid.addRow(rowIndex, children);
    }
    
    public void add(final Node... children) {
        rows.add(children);
        addGridRow(size++, children);
    }
    
    private void remove(final Node... children) {
        for (final Node child : children) {
            childList.remove(child);
        }
    }
    
    public void addAll(final int index, final Collection<Node[]> nodeColl) {
        final int shift = nodeColl.size();
        final int end = index + shift;
        for (int i = end - 1; i >= index; i--) {
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
            addGridRow(i - from, rows.get(i));
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
    
    public Node[] remove(final int rowIndex) {
        if (rowIndex == size - 1) {
            final Node[] children = rows.remove(rowIndex);
            remove(children);
            size--;
            return children;
        }
        return removeRange(rowIndex, rowIndex + 1).get(0);
    }
    
}