package com.clinic.appointment.clinicappointmentsystem.entity.appointment;

/*
*
*
*
* */

import com.clinic.appointment.clinicappointmentsystem.entity.doctor.DoctorEntity;

import java.util.HashMap;

import static java.lang.Math.max;

public class AppointmentTree {

    private int interval = 15;
    private int day;
    private HashMap<DoctorEntity, SegmentTree> doctorList;

    public AppointmentTree() {
        SegmentTree s = new SegmentTree();
        System.out.println(s.makeReservation(15, 30));
        System.out.println(s.makeReservation(20, 25));
        System.out.println(s.cancelReservation(15, 30));
        System.out.println(s.makeReservation(20, 25));
        System.out.println(s.makeReservation(10, 35));

    }

    /*
    * process the time slots of each on-duty doctor
    * Assume some rest time during the day when the doctor does not accept appointments
    * */
    private class SegmentTree {

        private int len = 48; // 8:00 am - 8:00 pm
        private Node root;

        public SegmentTree() {
            root = new Node();
        }

        private class Node {
            Node left, right;
            boolean booked = false; // Node value
            boolean add = false; // lazy mark
        }

        public boolean makeReservation(int start, int end) {

            if(query(root, 0, len - 1, start, end)) {
                return false;
            }

            update(root, 0, len - 1, start, end, true);

            return true;

        }

        public boolean cancelReservation(int start, int end) {

            if(!query(root, 0, len - 1, start, end)) {
                return false;
            }
            update(root, 0, len - 1, start, end, false);
            return true;

        }

        // push down lazy mark
        private void pushDown(Node node) {

            if (node.left == null) {
                node.left = new Node();
            }
            if (node.right == null) {
                node.right = new Node();
            }

            if(node.add) {
                node.left.booked = node.booked;
                node.right.booked = node.booked;
                node.left.add = true;
                node.right.add = true;
            }

            node.add = false;
        }

        private void pushUp(Node node) {
            node.booked = node.left.booked | node.right.booked;
        }

//        public boolean check() {
//            System.out.println("Check: " + root.left.right.left.right.booked);
//            return root.left.right.left.right.booked;
//        }

        // update the state of reservation of interval [l, r] in interval [start, end] to be book
        private void update(Node node, int start, int end, int l, int r, boolean booked) {
            // find the valid interval
            if(l <= start && end <= r) {
                node.booked = booked;
                node.add = true;
                return;
            }

            int mid = (start + end) >> 1;
            pushDown(node);

            if (l <= mid) update(node.left, start, mid, l, r, booked);
            if (r > mid) update(node.right, mid + 1, end, l, r, booked);

            pushUp(node);
        }

        private boolean query(Node node, int start, int end, int l, int r) {

            if(l <= start && end <= r) {
//                System.out.println("query: start: " + start + " end: " + end + " booked: " + node.booked);
                return node.booked;
            }
            pushDown(node);
            int mid = (start + end) >> 1;
            boolean ans = false;
            if (l <= mid) ans = query(node.left, start, mid, l, r);
            if (r > mid) ans |= query(node.right, mid + 1, end, l, r);

            return ans;
        }

    }

    public static void main(String[] args) {
        AppointmentTree a = new AppointmentTree();
    }

}
