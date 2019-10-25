package com.github.sisyphsu.datatube.node;

import com.github.sisyphsu.datatube.convertor.CodecFactory;
import com.github.sisyphsu.datatube.exception.CircleReferenceException;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-10-22 21:38:20
 */
public class CircleRefTest {

    private BeanNodeCodec codec = new BeanNodeCodec();

    @BeforeEach
    void setUp() {
        CodecFactory.Instance.installCodec(BasicNodeCodec.class);
        CodecFactory.Instance.installCodec(ArrayNodeCodec.class);
        CodecFactory.Instance.installCodec(BeanNodeCodec.class);

        codec.setFactory(CodecFactory.Instance);
    }

    @Test
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
