package com.github.sisyphsu.smartbuf.benchmark.small;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.msgpack.annotation.Message;

/**
 * Simple bean for test
 *
 * @author sulin
 * @since 2019-10-28 17:27:09
 */
@Data
@Message
public class UserModel {

    private long    id;
    private Boolean blocked;
    private String  nickname;
    private String  portrait;
    private float   score;
    private int     loginTimes;
    private long    createTime;

    public static UserModel random() {
        UserModel user = new UserModel();
        user.id = RandomUtils.nextLong();
        user.blocked = RandomUtils.nextBoolean();
        user.nickname = RandomStringUtils.randomAlphanumeric(12);
        user.portrait = RandomStringUtils.randomAlphabetic(24);
        user.score = RandomUtils.nextFloat();
        user.loginTimes = RandomUtils.nextInt(10, 10000);
        user.createTime = System.currentTimeMillis();
        return user;
    }

    public Small.User toPB() {
        Small.User.Builder builder = Small.User.newBuilder()
            .setId(id)
            .setBlocked(blocked)
            .setNickname(nickname)
            .setPortrait(portrait)
            .setScore(score)
            .setLoginTimes(loginTimes)
            .setCreateTime(createTime);
        return builder.build();
    }

}
