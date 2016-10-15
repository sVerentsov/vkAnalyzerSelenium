import java.io.PrintWriter;
import java.util.*;

public class VkAnalyzer {
    public static List<String> generateIds(int count)
    {
        Random r = new Random();
        LinkedList<String> ids = new LinkedList<>();
        for(int i = 0;i<count;i++) {
            Integer next = r.nextInt(200000000);
            ids.add(next.toString());
        }
        return ids;
    }
    public static void main(String[] args) {
        HashMap<Integer,List<Integer>> sumFriendsCounts = new HashMap<>();
        HashMap<Integer,Integer> chainsLengthCountMap = new HashMap<>();//count of chains with different lengths
        List<String> ids = generateIds(2000);
        for(String id : ids) {
            List<Integer> friendsCounts = getFriendChainToMe(id);
            int chainLength = friendsCounts.size();
            if(sumFriendsCounts.containsKey(chainLength)) {
                List<Integer> currentFriendCounts = sumFriendsCounts.get(chainLength);
                for (int i = 0; i < chainLength;i++)
                    currentFriendCounts.set(i,currentFriendCounts.get(i)+friendsCounts.get(i));
                sumFriendsCounts.put(chainLength,currentFriendCounts);
            }
            else
                sumFriendsCounts.put(chainLength,friendsCounts);
            if(chainsLengthCountMap.containsKey(chainLength))
                chainsLengthCountMap.put(chainLength,chainsLengthCountMap.get(chainLength)+1);
            else
                chainsLengthCountMap.put(chainLength,1);
        }
        try {
            PrintWriter writer = new PrintWriter("output.csv", "UTF-8");
            for (Integer i = 0; i < 15;i++ )
                writer.print(";"+i.toString());
            writer.println();
            for (Integer chainLength : sumFriendsCounts.keySet()) {
                writer.print("length"+chainLength.toString()+";");
                for(int i = 0;i<chainLength;i++) {
                    List<Integer> sumFriendsCount = sumFriendsCounts.get(chainLength);
                    writer.print((double) sumFriendsCount.get(i) / chainsLengthCountMap.get(chainLength) + ";");
                }
                writer.println();
            }
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
