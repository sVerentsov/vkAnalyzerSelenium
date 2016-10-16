package exceptions;

import java.util.List;

public abstract class FriendsChainException extends Exception {
    private List<Integer> friendsList;

    protected FriendsChainException(List<Integer> friendsList) {
        this.friendsList = friendsList;
    }

    public List<Integer> getFriendsList() {
        return friendsList;
    }
}
