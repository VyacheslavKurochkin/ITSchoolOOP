package ru.kurochkin.tree;

import java.util.*;
import java.util.function.Consumer;

public class BinarySearchTree<E> {
    private Node<E> root;
    private int nodesCount;
    private final Comparator<E> comparator;

    public BinarySearchTree() {
        comparator = null;
    }

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public BinarySearchTree(E data) {
        comparator = null;

        add(data);
    }

    @SuppressWarnings("unchecked")
    public BinarySearchTree(E... array) {
        comparator = null;

        for (E data : array) {
            add(data);
        }
    }

    private Node<E> createNode(E data) {
        nodesCount++;

        return new Node<>(data);
    }

    public int size() {
        return nodesCount;
    }

    private int compare(E data1, E data2) {
        if (comparator != null) {
            return comparator.compare(data1, data2);
        }

        if (data1 == null && data2 == null) {
            return 0;
        }

        if (data1 == null) {
            return -1;
        }

        if (data2 == null) {
            return 1;
        }

        //noinspection unchecked
        return ((Comparable<E>) data1).compareTo(data2);
    }

    public void add(E data) {
        if (root == null) {
            root = createNode(data);

            return;
        }

        Node<E> currentNode = root;

        while (currentNode != null) {
            if (compare(data, currentNode.getData()) < 0) {
                if (currentNode.getLeft() == null) {
                    currentNode.setLeft(createNode(data));

                    return;
                }

                currentNode = currentNode.getLeft();
            } else {
                if (currentNode.getRight() == null) {
                    currentNode.setRight(createNode(data));

                    return;
                }

                currentNode = currentNode.getRight();
            }
        }
    }

    public boolean contains(E data) {
        Node<E> currentNode = root;

        while (currentNode != null) {
            int comparisonResult = compare(data, currentNode.getData());

            if (comparisonResult == 0) {
                return true;
            }

            if (comparisonResult < 0) {
                if (currentNode.getLeft() == null) {
                    return false;
                }

                currentNode = currentNode.getLeft();
            } else {
                if (currentNode.getRight() == null) {
                    return false;
                }

                currentNode = currentNode.getRight();
            }
        }

        return false;
    }

    public boolean remove(E data) {
        Node<E> removedNode = root;
        Node<E> parentNode = null;

        while (removedNode != null) {
            int dataEqualityResult = compare(data, removedNode.getData());

            if (dataEqualityResult == 0) {
                break;
            }

            parentNode = removedNode;

            if (dataEqualityResult < 0) {
                removedNode = removedNode.getLeft();
            } else {
                removedNode = removedNode.getRight();
            }
        }

        if (removedNode == null) {
            return false;
        }

        Node<E> childNode;

        if (removedNode.getLeft() == null || removedNode.getRight() == null) {
            childNode = removedNode.getLeft() != null ? removedNode.getLeft() : removedNode.getRight();
        } else {
            Node<E> minLeftNode = removedNode.getRight();
            Node<E> minLeftNodeParent = null;

            while (minLeftNode.getLeft() != null) {
                minLeftNodeParent = minLeftNode;

                minLeftNode = minLeftNode.getLeft();
            }

            if (minLeftNodeParent != null) {
                minLeftNodeParent.setLeft(minLeftNode.getRight());
                minLeftNode.setRight(removedNode.getRight());
            }

            minLeftNode.setLeft(removedNode.getLeft());

            childNode = minLeftNode;
        }

        removedNode.setLeft(null);
        removedNode.setRight(null);

        if (parentNode == null) {
            root = childNode;
        } else {
            if (parentNode.getRight() == removedNode) {
                parentNode.setRight(childNode);
            } else {
                parentNode.setLeft(childNode);
            }
        }

        nodesCount--;

        return true;
    }

    public void visitInBreadth(Consumer<E> consumer) {
        if (root == null) {
            return;
        }

        Queue<Node<E>> queue = new LinkedList<>();

        queue.add(root);

        while (!queue.isEmpty()) {
            Node<E> currentNode = queue.poll();

            consumer.accept(currentNode.getData());

            if (currentNode.getLeft() != null) {
                queue.offer(currentNode.getLeft());
            }

            if (currentNode.getRight() != null) {
                queue.offer(currentNode.getRight());
            }
        }
    }

    public void visitInDepth(Consumer<E> consumer) {
        if (root == null) {
            return;
        }

        Deque<Node<E>> stack = new LinkedList<>();

        stack.push(root);

        while (!stack.isEmpty()) {
            Node<E> currentNode = stack.pop();

            consumer.accept(currentNode.getData());

            if (currentNode.getRight() != null) {
                stack.push(currentNode.getRight());
            }

            if (currentNode.getLeft() != null) {
                stack.push(currentNode.getLeft());
            }
        }
    }

    public void visitInDepthRecursive(Consumer<E> consumer) {
        visitInDepthRecursive(root, consumer);
    }

    private void visitInDepthRecursive(Node<E> node, Consumer<E> consumer) {
        if (node == null) {
            return;
        }

        consumer.accept(node.getData());

        visitInDepthRecursive(node.getLeft(), consumer);
        visitInDepthRecursive(node.getRight(), consumer);
    }
}
