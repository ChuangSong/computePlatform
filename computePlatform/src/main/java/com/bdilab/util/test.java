package com.bdilab.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @ClassName test
 * @Description TODO
 * @Author ChuangSong_Zheng
 * @Date 2019/11/4 17:32
 * @Version 1.0
 */
class TreeNode{
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int val){this.val=val;}
}
public class test {
    public void levelTraversal(TreeNode root){
        Queue<TreeNode> queue = new LinkedList<>();
        List<Integer> list = new ArrayList<>();
        if (root!=null){
            queue.add(root);
        }
        while (!queue.isEmpty()){
            TreeNode poll = queue.poll();
            list.add(poll.val);
            if (poll.left!=null){
                queue.add(poll.left);
            }
            if (poll.right!=null){
                queue.add(poll.right);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int[] data = new int[]{-1,2,1,-4};

        System.out.println();

    }
}
