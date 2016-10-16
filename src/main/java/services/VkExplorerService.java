package services;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import exceptions.LoopedFriendChainException;
import exceptions.TooLongFriendsChain;
import models.CrawledUserInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VkExplorerService {
    private final IdService idService;
    private final Crawler crawler;
    private static final int maxChainSize = 10;

    public VkExplorerService(IdService idService, Crawler crawler) {
        this.idService = idService;
        this.crawler = crawler;

    }

    public List<Integer> getFriendsChain(String from, String to) throws InterruptedException, ClientException, ApiException, TooLongFriendsChain, LoopedFriendChainException {
        ArrayList<Integer> friendsChain = new ArrayList<>();
        String currentUser = from;
        while (true) {
            if (friendsChain.size() > maxChainSize) {
                throw new TooLongFriendsChain(friendsChain);
            }
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
                if (friendsChain.contains(currentUser)) {
                    throw new LoopedFriendChainException(friendsChain);
                }
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
