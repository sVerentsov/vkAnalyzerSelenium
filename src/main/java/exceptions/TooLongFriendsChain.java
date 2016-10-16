package exceptions;

import java.util.List;

/**
 * Created by Олег on 16.10.2016.
 */
public class TooLongFriendsChain extends FriendsChainException {

    public TooLongFriendsChain(List<Integer> friendsList) {
        super(friendsList);
    }
}
