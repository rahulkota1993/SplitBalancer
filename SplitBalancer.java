import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HelloWorld {
        public static void main(String[] args) {

        try {
            
             BufferedReader bReader =
                   new BufferedReader(new InputStreamReader(System.in));
            String line;
            double validInput = 0;

            Map<String, Double> positiveBalances = new TreeMap<>();
            Map<String, Double> negativeBalances = new TreeMap<>();

            while ((line = bReader.readLine()) != null) {
                String[] info = line.split("\t");
                String name = info[0];
                String value = info[1];
                double balance = Double.parseDouble(value);

                if (balance > 0.0) {
                    positiveBalances.put(name, balance);
                } else if (balance < 0.0){
                    negativeBalances.put(name, balance);
                }
                validInput = validInput + balance;
            }

            if (validInput != 0) {
                System.out.println("Not a valid input, diff is: " + validInput);
                throw new Exception();
            }

            positiveBalances = sortByNegativeValues(positiveBalances);
            negativeBalances = sortByValues(negativeBalances);
            System.out.println(positiveBalances);
            System.out.println(negativeBalances);
            transact(positiveBalances, negativeBalances);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void transact(Map<String, Double> positiveBalances, Map<String, Double> negativeBalances) {
        for (String person : positiveBalances.keySet()) {
            Double balance = positiveBalances.get(person);

            List<Double> tempList = new ArrayList<>(negativeBalances.size());
            int i = -1;
            for (String tenant : negativeBalances.keySet()) {
                Double owedBalance = -negativeBalances.get(tenant);
                if (owedBalance >= balance) {
                    tempList.add(owedBalance - balance);
                    i++;
                    System.out.println("Balance " + person + " gets from: " + tenant + " is : " + balance);
                    break;
                }
                    balance = balance - owedBalance;
                    tempList.add(0.0);
                    i++;
                    if (owedBalance != 0.0) {
                        System.out.println("Balance " + person + " gets from " + tenant + " is : " + owedBalance);
                    }

            }

            int j = 0;
            for (String tenant : negativeBalances.keySet()) {
                if (i > -1) {
                    negativeBalances.put(tenant, -tempList.get(j));
                    i--;
                    j++;
                }
            }

        }
    }

    // Generic function to sort Map by values using TreeMap
    public static <K,V> Map<K,V> sortByNegativeValues(Map<K,V> map)
    {
        Comparator<K> comparator = new NegativeCustomComparator(map);

        Map<K,V> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(map);

        return sortedMap;
    }

    // Generic function to sort Map by values using TreeMap
    public static <K,V> Map<K,V> sortByValues(Map<K,V> map)
    {
        Comparator<K> comparator = new CustomComparator(map);

        Map<K,V> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(map);

        return sortedMap;
    }

    // Custom Comparator to sort the map according to the natural
// ordering of its values
    static class CustomComparator<K,V extends Comparable> implements Comparator<K>
    {
        private Map<K,V> map;

        public CustomComparator(Map<K,V> map) {
            this.map = new HashMap<>(map);
        }

        @Override
        public int compare(K s1, K s2) {
            return map.get(s1).compareTo(map.get(s2));
        }
    }

    // Custom Comparator to sort the map according to the natural
// ordering of its values
    static class NegativeCustomComparator<K,V extends Comparable> implements Comparator<K>
    {
        private Map<K,V> map;

        public NegativeCustomComparator(Map<K,V> map) {
            this.map = new HashMap<>(map);
        }

        @Override
        public int compare(K s1, K s2) {
            return map.get(s2).compareTo(map.get(s1));
        }
    }
}
