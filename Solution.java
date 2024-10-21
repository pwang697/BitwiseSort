import java.util.ArrayList;

class Node {
    ArrayList<Integer> arr;
    Node next;

    public Node() {
        this.arr = null;
        this.next = null;
    }

    public Node(ArrayList<Integer> arr) {
        this.arr = arr;
        this.next = null;
    }
}

class LinkedNode {
    Node head;
    int suffixLength;

    public LinkedNode() {
        this.head = null;
        this.suffixLength = 0;
    }

    public LinkedNode(Node head) {
        this.head = head;
        this.suffixLength = 0;
    }

    public void insertNextNode(Node node, ArrayList<Integer> arr) {
        Node newNode = new Node(arr);
        newNode.next = node.next;
        node.next = newNode;
        this.suffixLength++;
    }

    public ArrayList<Integer> traverseLinkedNodeExceptHead() {
        Node current = head.next;
        ArrayList<Integer> output = new ArrayList<>();
        while (current != null) {
            output.addAll(current.arr);
            current = current.next;
        }
        return output;
    }
}

class Solution {
    public int[] sortArray(int[] nums) {
        int digit = 16;
        boolean ascending = true;

        Node negativeHead = new Node();
        Node unsignedHead = new Node();
        LinkedNode negativeLinkedNode = new LinkedNode(negativeHead);
        LinkedNode unsignedLinkedNode = new LinkedNode(unsignedHead);

        ArrayList<ArrayList<Integer>> dividedArrayBySign = divideArrayToArrayListBySign(nums);
        ArrayList<Integer> negativeArray = dividedArrayBySign.get(0);
        ArrayList<Integer> unsignedArray = dividedArrayBySign.get(1);
        if (negativeArray.size() > 0) {
            negativeLinkedNode.insertNextNode(negativeLinkedNode.head, negativeArray);
        }
        if (unsignedArray.size() > 0) {
            unsignedLinkedNode.insertNextNode(unsignedLinkedNode.head, unsignedArray);
        }

        while (digit-- > 0 && negativeLinkedNode.suffixLength + unsignedLinkedNode.suffixLength != nums.length) {
            Node negativeCurrent = negativeLinkedNode.head.next;
            while (negativeCurrent != null) {
                if (negativeCurrent.arr.size() > 1) {
                    ArrayList<Integer> firstArray = new ArrayList<>();
                    ArrayList<Integer> secondArray = new ArrayList<>();
                    ArrayList<ArrayList<Integer>> dividedArrayByDigit = divideArrayListByDigit(negativeCurrent.arr, digit);
                    if (ascending) {
                        firstArray = dividedArrayByDigit.get(0);
                        secondArray = dividedArrayByDigit.get(1);
                    }
                    else {
                        firstArray = dividedArrayByDigit.get(1);
                        secondArray = dividedArrayByDigit.get(0);
                    }
                    if (firstArray.size() > 0 && secondArray.size() > 0) {
                        negativeLinkedNode.insertNextNode(negativeCurrent, secondArray);
                        negativeCurrent.arr = firstArray;
                    }
                }
                negativeCurrent = negativeCurrent.next;
            }
            Node unsignedCurrent = unsignedLinkedNode.head.next;
            while (unsignedCurrent != null) {
                if (unsignedCurrent.arr.size() > 1) {
                    ArrayList<Integer> firstArray = new ArrayList<>();
                    ArrayList<Integer> secondArray = new ArrayList<>();
                    ArrayList<ArrayList<Integer>> dividedArrayByDigit = divideArrayListByDigit(unsignedCurrent.arr, digit);
                    if (ascending) {
                        firstArray = dividedArrayByDigit.get(0);
                        secondArray = dividedArrayByDigit.get(1);
                    }
                    else {
                        firstArray = dividedArrayByDigit.get(1);
                        secondArray = dividedArrayByDigit.get(0);
                    }
                    if (firstArray.size() > 0 && secondArray.size() > 0) {
                        unsignedLinkedNode.insertNextNode(unsignedCurrent, secondArray);
                        unsignedCurrent.arr = firstArray;
                    }
                }
                unsignedCurrent = unsignedCurrent.next;
            }
        }
        if (ascending) {
            return mergeArrayListsToArray(
                negativeLinkedNode.traverseLinkedNodeExceptHead(), 
                unsignedLinkedNode.traverseLinkedNodeExceptHead()
                );
        }
        else {
            return mergeArrayListsToArray(
                unsignedLinkedNode.traverseLinkedNodeExceptHead(), 
                negativeLinkedNode.traverseLinkedNodeExceptHead()
                );
        }
    }

    private static ArrayList<ArrayList<Integer>> divideArrayToArrayListBySign(int[] nums) {
        ArrayList<Integer> negativeArrayList = new ArrayList<>();
        ArrayList<Integer> unsignedArrayList = new ArrayList<>();
        for (int num : nums) {
            if (num < 0) {
                negativeArrayList.add(num);
            } else {
                unsignedArrayList.add(num);
            }
        }
        return new ArrayList<ArrayList<Integer>>() {{add(negativeArrayList); add(unsignedArrayList);}};
    }

    private static ArrayList<ArrayList<Integer>> divideArrayListByDigit(ArrayList<Integer> nums, int digit) {
        ArrayList<Integer> firstArrayList = new ArrayList<>();
        ArrayList<Integer> secondArrayList = new ArrayList<>();
        for (int num : nums) {
            if ((num & 1 << digit) == 0) {
                firstArrayList.add(num);
            } else {
                secondArrayList.add(num);
            }
        }
        return new ArrayList<ArrayList<Integer>>() {{add(firstArrayList); add(secondArrayList);}};
    }

    private static int[] mergeArrayListsToArray(ArrayList<Integer> firstArrayList, ArrayList<Integer> secondArrayList) {
        int[] output = new int[firstArrayList.size() + secondArrayList.size()];
        int index = 0;
        for (int num : firstArrayList) {
            output[index++] = num;
        }
        for (int num : secondArrayList) {
            output[index++] = num;
        }
        return output;
    }
}
