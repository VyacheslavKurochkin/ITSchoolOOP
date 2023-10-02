package ru.kurochkin.tree;

import java.util.*;
import java.util.function.Consumer;

public class BinaryTree<E extends Comparable<E>> {
    private Node<E> root;
    private int nodesCount;

    public BinaryTree() {

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

    private Node<E> createNewNode(E data) {
        nodesCount++;

        return new Node<>(data);
    }

    public int size(){
        return nodesCount;
    }

    public void add(E data) {
        if (root == null) {
            root = createNewNode(data);

            return;
        }

        Node<E> currentNode = root;

        while (currentNode != null) {
            if (data.compareTo(currentNode.getData()) < 0) {
                if (currentNode.getLeft() == null) {
                    currentNode.setLeft(createNewNode(data));

                    return;
                }

                currentNode = currentNode.getLeft();
            } else {
                if (currentNode.getRight() == null) {
                    currentNode.setRight(createNewNode(data));

                    return;
                }

                currentNode = currentNode.getRight();
            }
        }
    }

     private Node<E> getNode(E data) {
        Node<E> currentNode = root;

        while (currentNode != null) {
            if (Objects.equals(currentNode.getData(), data)) {
                return currentNode;
            }

            if (data.compareTo(currentNode.getData()) < 0) {
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

    public boolean isExists(E data) {
        return getNode(data) == null;
    }

    private void removeRoot() {
        Node<E> leftNode = root.getLeft();

        root = root.getRight();

        nodesCount--;

        if (leftNode == null) {
            return;
        }

        Node<E> minLeftNode = root;

        while (minLeftNode.getLeft() != null) {
            minLeftNode = minLeftNode.getLeft();
        }

        minLeftNode.setLeft(leftNode);
    }

    private void removeTwoChildNode(Node<E> deletedNodeParent, Node<E> deletedNode) {
        Node<E> minLeftNode = deletedNode.getRight();
        Node<E> minLeftNodeParent = null;

        while (minLeftNode.getLeft() != null) {
            minLeftNodeParent = minLeftNode;
            minLeftNode = minLeftNode.getLeft();
        }

        if (minLeftNodeParent != null) {
            minLeftNodeParent.setLeft(minLeftNode.getRight());

            minLeftNode.setRight(deletedNode.getRight());
        }

        minLeftNode.setLeft(deletedNode.getLeft());

        if (deletedNodeParent.getRight() == deletedNode) {
            deletedNodeParent.setRight(minLeftNode);
        } else {
            deletedNodeParent.setLeft(minLeftNode);
        }

        deletedNode.setRight(null);
        deletedNode.setLeft(null);

        nodesCount--;
    }

    public boolean remove(E data) {
        Node<E> currentNode = root;
        Node<E> parentNode = null;

        while (currentNode != null) {
            if (Objects.equals(currentNode.getData(), data)) {
                break;
            }

            parentNode = currentNode;

            if (data.compareTo(currentNode.getData()) < 0) {
                currentNode = currentNode.getLeft();
            } else {
                currentNode = currentNode.getRight();
            }
        }

        if (currentNode == null) {
            return false;
        }

        if (parentNode == null) {
            removeRoot();

            return true;
        }

        if (currentNode.getLeft() != null && currentNode.getRight() != null) {
            removeTwoChildNode(parentNode, currentNode);

            return true;
        }

        Node<E> nextNode = currentNode.getLeft() != null ? currentNode.getLeft() : currentNode.getRight();

        if (parentNode.getRight() == currentNode) {
            parentNode.setRight(nextNode);
        } else {
            parentNode.setLeft(nextNode);
        }

        nodesCount--;

        return true;
    }

    public void breadthVisit(Consumer<E> consumer) {
        if(root == null){
            return;
        }

        Deque<Node<E>> queue = new LinkedList<>();
        Node<E> currentNode;

        queue.add(root);

        while (!queue.isEmpty()) {
            currentNode = queue.pollLast();

            consumer.accept(currentNode.getData());

            if (currentNode.getLeft() != null) {
                queue.push(currentNode.getLeft());
            }

            if (currentNode.getRight() != null) {
                queue.push(currentNode.getRight());
            }
        }
    }

    public void depthVisit(Consumer<E> consumer) {
        Deque<Node<E>> stack = new LinkedList<>();
        Node<E> currentNode;

        stack.push(root);

        while (!stack.isEmpty()) {
            currentNode = stack.pop();

            consumer.accept(currentNode.getData());

            if (currentNode.getRight() != null) {
                stack.push(currentNode.getRight());
            }

            if (currentNode.getLeft() != null) {
                stack.push(currentNode.getLeft());
            }
        }
    }

    public void depthRecursiveVisit(Consumer<E> consumer) {
        depthRecursiveVisit(root, consumer);
    }

    private void depthRecursiveVisit(Node<E> node, Consumer<E> consumer) {
        if (node == null) {
            return;
        }

        consumer.accept(node.getData());

        depthRecursiveVisit(node.getLeft(), consumer);

        depthRecursiveVisit(node.getRight(), consumer);
    }
}
