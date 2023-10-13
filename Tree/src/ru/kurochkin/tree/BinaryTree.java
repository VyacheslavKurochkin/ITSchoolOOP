package ru.kurochkin.tree;

import java.util.*;
import java.util.function.Consumer;

public class BinaryTree<E> {
    private Node<E> root;
    private int nodesCount;
    private Comparator<E> comparator;

    public BinaryTree() {
    }

    public BinaryTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public BinaryTree(E data) {
        add(data);
    }

    @SuppressWarnings("unchecked")
    public BinaryTree(E... array) {
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
        if (data1 == null && data2 == null) {
            return 0;
        }

        if (data1 == null) {
            return -1;
        }

        if (data2 == null) {
            return 1;
        }

        if (comparator != null) {
            return comparator.compare(data1, data2);
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

    private Node<E> getNode(E data) {
        Node<E> currentNode = root;

        while (currentNode != null) {
            if (compare(data, currentNode.getData()) == 0) {
                return currentNode;
            }

            if (compare(data, currentNode.getData()) < 0) {
                if (currentNode.getLeft() == null) {
                    return null;
                }

                currentNode = currentNode.getLeft();
            } else {
                if (currentNode.getRight() == null) {
                    return null;
                }

                currentNode = currentNode.getRight();
            }
        }

        return null;
    }

    public boolean contains(E data) {
        return getNode(data) != null;
    }

    public boolean remove(E data) {
        Node<E> removedNode = root;
        Node<E> parentNode = null;

        while (removedNode != null) {
            if (compare(removedNode.getData(), data) == 0) {
                break;
            }

            parentNode = removedNode;

            if (compare(data, removedNode.getData()) < 0) {
                removedNode = removedNode.getLeft();
            } else {
                removedNode = removedNode.getRight();
            }
        }

        if (removedNode == null) {
            return false;
        }

        Node<E> childNode;

        if (removedNode.getLeft() != null && removedNode.getRight() != null) {
            Node<E> minLeftNode = removedNode.getRight();
            Node<E> minLeftNodeParent = null;

            while (minLeftNode.getLeft() != null) {
                minLeftNodeParent = minLeftNode;
                minLeftNode = minLeftNode.getLeft();
            }

            if (minLeftNodeParent != null) {
                if (minLeftNode.getRight() != null) {
                    minLeftNodeParent.setLeft(minLeftNode.getRight());
                }

                minLeftNode.setRight(removedNode.getRight());
            }

            minLeftNode.setLeft(removedNode.getLeft());

            childNode = minLeftNode;
        } else {
            childNode = removedNode.getLeft() != null ? removedNode.getLeft() : removedNode.getRight();
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

    public void visitBreadth(Consumer<E> consumer) {
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

    public void visitDepth(Consumer<E> consumer) {
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

    public void visitDepthRecursive(Consumer<E> consumer) {
        visitDepthRecursive(root, consumer);
    }

    private void visitDepthRecursive(Node<E> node, Consumer<E> consumer) {
        if (node == null) {
            return;
        }

        consumer.accept(node.getData());

        visitDepthRecursive(node.getLeft(), consumer);

        visitDepthRecursive(node.getRight(), consumer);
    }
}
