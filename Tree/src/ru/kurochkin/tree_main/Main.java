package ru.kurochkin.tree_main;

import ru.kurochkin.tree.BinaryTree;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Integer[] numbers = {10, 25, 11, 20, 30, 33, 31, 11, 8, 7, 9, 27, 28};
        BinaryTree<Integer> tree = new BinaryTree<>(numbers);

        List<Integer> numbersList = new ArrayList<>();
        tree.visitBreadth(numbersList::add);

        System.out.println("Обход в ширину:");
        System.out.println(numbersList);

        numbersList.clear();
        tree.visitDepth(numbersList::add);

        System.out.println();
        System.out.println("Обход в глубину");
        System.out.println(numbersList);

        tree.remove(25);

        numbersList.clear();
        tree.visitDepthRecursive(numbersList::add);

        System.out.println();
        System.out.println("Рекурсивный обход в глубину после удаления узла со значением 25");
        System.out.println(numbersList);
    }
}