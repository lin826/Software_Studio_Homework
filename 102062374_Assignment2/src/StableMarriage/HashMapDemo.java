package StableMarriage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapDemo {

    private HashMap<String, Integer> transcript;

    public static void main(String[] args) {
        new HashMapDemo();
    }

    public HashMapDemo() {

        transcript = new HashMap<String, Integer>();
        transcript.put("Alex", 95);
        transcript.put("Bibby", 70);
        transcript.put("Charlie", 85);
        transcript.put("David", 75);

        // ��¨��X�ǥͪ����Z
        printGrade("Alex");
        printGrade("Bibby");
        printGrade("Joice"); //�S��Ʒ|�Ǧ^null
        System.out.println();

        // �Q�L�XHashMap�̪��������
        // �`�N�o�̦L�X�����Ǥ����m�W�B���Z�A�]�����[�J����
        // �]��HashMap���ۤv���ƧǤ覡�A�Y�n���m�W�Ƨǥi��TreeMap���į���t
        for (String name:transcript.keySet()) {
            printGrade(name);
        }
        System.out.println();

        // �Q�̷өm�W�Φ��Z�P�C�L�X�Ҧ���ơA���N�Ҧ�HashMap�̪�entry��JList
        List<Map.Entry<String, Integer>> list_Data =
            new ArrayList<Map.Entry<String, Integer>>(transcript.entrySet());

        // �̩m�W�ƧǨæC�L
        Collections.sort(list_Data, new Comparator<Map.Entry<String, Integer>>(){
            public int compare(Map.Entry<String, Integer> entry1,
                               Map.Entry<String, Integer> entry2){
                return (entry1.getKey().compareTo(entry2.getKey()));
            }
        });
        for (Map.Entry<String, Integer> entry:list_Data) {
            printGrade(entry.getKey());
        }
        System.out.println();

        // �̦��Z�ƧǨæC�L
        Collections.sort(list_Data, new Comparator<Map.Entry<String, Integer>>(){
            public int compare(Map.Entry<String, Integer> entry1,
                               Map.Entry<String, Integer> entry2){
                return (entry2.getValue() - entry1.getValue());
            }
        });
        for (Map.Entry<String, Integer> entry:list_Data) {
            printGrade(entry.getKey());
        }
    }

    private void printGrade(String name) {
        System.out.println(name + "\t" + transcript.get(name));
    }
}