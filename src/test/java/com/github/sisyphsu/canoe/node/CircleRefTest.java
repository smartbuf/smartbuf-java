package com.github.sisyphsu.canoe.node;

import com.github.sisyphsu.canoe.convertor.CodecFactory;
import com.github.sisyphsu.canoe.exception.CircleReferenceException;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author sulin
 * @since 2019-10-22 21:38:20
 */
public class CircleRefTest {


    @BeforeEach
    void setUp() {
        CodecFactory.Instance.installCodec(NodeCodec.class);
    }

    //    @Test
    public void test() {
        Post post = new Post();
        Comment comment = new Comment();
        post.comment = comment;
        comment.post = post;
        try {
            CodecFactory.Instance.convert(post, Node.class);
            assert false;
        } catch (Exception e) {
            assert e instanceof CircleReferenceException;
        }
    }

    @Data
    public static class Post {
        private Comment comment;
    }

    @Data
    public static class Comment {
        private Post post;
    }

}
