package exceptions;

import java.util.List;

public class LoopedFriendChainException extends FriendsChainException {
    public LoopedFriendChainException(List<Integer> friendsList) {
        super(friendsList);
    }
}
