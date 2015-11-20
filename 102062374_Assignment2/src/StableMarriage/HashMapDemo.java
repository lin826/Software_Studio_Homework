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

        // 單純取出學生的成績
        printGrade("Alex");
        printGrade("Bibby");
        printGrade("Joice"); //沒資料會傳回null
        System.out.println();

        // 想印出HashMap裡的全部資料
        // 注意這裡印出的順序不按姓名、成績，也不按加入順序
        // 因為HashMap有自己的排序方式，若要按姓名排序可用TreeMap但效能較差
        for (String name:transcript.keySet()) {
            printGrade(name);
        }
        System.out.println();

        // 想依照姓名或成績牌列印出所有資料，先將所有HashMap裡的entry放入List
        List<Map.Entry<String, Integer>> list_Data =
            new ArrayList<Map.Entry<String, Integer>>(transcript.entrySet());

        // 依姓名排序並列印
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

        // 依成績排序並列印
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