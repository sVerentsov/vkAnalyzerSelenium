import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import exceptions.FriendsChainException;
import exceptions.LoopedFriendChainException;
import exceptions.TooLongFriendsChain;
import models.UserCredentials;
import services.Crawler;
import services.IdService;
import services.VkExplorerService;

import java.io.PrintWriter;
import java.util.*;

public class VkAnalyzer {
    private static List<String> generateIds(int count)
    {
        Random r = new Random();
        LinkedList<String> ids = new LinkedList<>();
        for(int i = 0;i<count;i++) {
            Integer next = r.nextInt(20000000);
            ids.add("id"+next.toString());
        }
        return ids;
    }
    public static void main(String[] args) throws InterruptedException, ClientException, ApiException {

        UserCredentials userCredentials = new UserCredentials(id, "email or phone", "password");
        IdService idService = new IdService();
        Crawler crawler = new Crawler(userCredentials.getLogin(), userCredentials.getPassword());
        VkExplorerService explorerService = new VkExplorerService(idService, crawler);
        String to = "<short name>";

        HashMap<Integer,List<Integer>> sumFriendsCounts = new HashMap<>();
        HashMap<Integer,Integer> chainsLengthCountMap = new HashMap<>();//count of chains with different lengths
        int idsCount = 2;
        List<String> ids = generateIds(idsCount);
        int c = 0;
        for(String id : ids) {
            System.out.println(c++ +"\\"+idsCount);
            List<Integer> friendsCounts = null;
            try {
                friendsCounts = explorerService.getFriendsChain(id,to);
            } catch (FriendsChainException friendsChain) {
                friendsCounts = friendsChain.getFriendsList();
            }
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
            writer.print(";");
            for (Integer i = 0; i < 15;i++ )
                writer.print(";"+i.toString());
            writer.println();
            for (Integer chainLength : sumFriendsCounts.keySet()) {
                writer.print("length"+chainLength.toString()+";");
                writer.print( chainsLengthCountMap.get(chainLength)+";");
                for(int i = 0;i<chainLength;i++) {
                    List<Integer> sumFriendsCount = sumFriendsCounts.get(chainLength);
                    writer.print(sumFriendsCount.get(i) / chainsLengthCountMap.get(chainLength) + ";");
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
