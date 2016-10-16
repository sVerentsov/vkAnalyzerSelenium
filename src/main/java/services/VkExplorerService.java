package services;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import models.CrawledUserInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VkExplorerService {
    private final IdService idService;
    private final Crawler crawler;

    public VkExplorerService(IdService idService, Crawler crawler) {
        this.idService = idService;
        this.crawler = crawler;
    }

    public List<Integer> getFriendsChain(String from, String to) throws InterruptedException, ClientException, ApiException {
        ArrayList<Integer> friendsChain = new ArrayList<>();
        String currentUser = from;
        while (true) {
            try {
                CrawledUserInfo userInfo = crawler.getUserInfo(currentUser);
                Integer friendsCount = userInfo.getFriendsCount();
                friendsChain.add(friendsCount);
//                if (userInfo.hasCommonFriends()) {
//                    friendsChain.add(friendsCount);
//                    break;
//                }

                String firstFriendUrl = userInfo.getFirstFriendUrl();
                currentUser = idService.extractIdFromUserUrl(firstFriendUrl);
                if(currentUser.equals(to))
                {
                    break;
                }
            }
            catch (org.openqa.selenium.NoSuchElementException e)
            {
                return new LinkedList<>();
            }
        }
        return friendsChain;
    }
}
