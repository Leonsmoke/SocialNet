package socialNet.other;

import socialNet.Entity.Post;

import java.util.Comparator;

public class PostComparator implements Comparator<Post> {

    @Override
    public int compare(Post o1, Post o2) {
        return o2.getTimeForSort().compareTo(o1.getTimeForSort());
    }
}
