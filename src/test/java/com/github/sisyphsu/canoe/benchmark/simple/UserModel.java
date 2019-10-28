package com.github.sisyphsu.canoe.benchmark.simple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Simple bean for test
 *
 * @author sulin
 * @since 2019-10-28 17:27:09
 */
@Data
public class UserModel {

    private long      id;
    private Boolean   blocked;
    private String    nickname;
    private String    portrait;
    private float     score;
    private int       loginTimes;
    private Date      createTime;
    private List<Tag> tags;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        private int    code;
        private String name;
    }

    public static UserModel random() {
        UserModel user = new UserModel();
        user.id = RandomUtils.nextLong();
        user.blocked = RandomUtils.nextBoolean();
        user.nickname = RandomStringUtils.randomAlphanumeric(12);
        user.portrait = RandomStringUtils.randomAlphabetic(24);
        user.score = RandomUtils.nextFloat();
        user.loginTimes = RandomUtils.nextInt(10, 10000);
        user.createTime = new Date();
        user.tags = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            user.tags.add(new Tag(1, "Tag1"));
            user.tags.add(new Tag(2, "Tag2"));
            user.tags.add(new Tag(3, "Tag3"));
        }
        return user;
    }

    public UserModel toModel() {
        UserModel user = new UserModel();
        user.id = id;
        user.blocked = blocked;
        user.nickname = nickname;
        user.portrait = portrait;
        user.score = score;
        user.loginTimes = loginTimes;
        user.createTime = createTime;
        user.tags = new ArrayList<>();
        for (Tag tag : tags) {
            user.tags.add(new Tag(tag.code, tag.name));
        }
        return user;
    }

    public Simple.User toPB() {
        Simple.User.Builder builder = Simple.User.newBuilder()
            .setId(id)
            .setBlocked(blocked)
            .setNickname(nickname)
            .setPortrait(portrait)
            .setScore(score)
            .setLoginTimes(loginTimes)
            .setCreateTime(createTime.getTime());
        for (Tag tag : tags) {
            builder.addTags(Simple.Tag.newBuilder().setCode(tag.code).setName(tag.name).build());
        }
        return builder.build();
    }

}
