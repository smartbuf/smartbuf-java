package com.github.sisyphsu.canoe.benchmark.small;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Date;

/**
 * Simple bean for test
 *
 * @author sulin
 * @since 2019-10-28 17:27:09
 */
@Data
public class UserModel {

    private long    id         = 1385434576456830976L;
    private Boolean blocked    = false;
    private String  nickname   = "1uOlvFfhZuda";
    private String  portrait   = "OCCCAcnrKtKmbOkzRNYbWLnR";
    private float   score      = 2.7177553E38f;
    private int     loginTimes = 1543;
    private Date    createTime = new Date();

    public static UserModel random() {
        UserModel user = new UserModel();
        user.id = RandomUtils.nextLong();
        user.blocked = RandomUtils.nextBoolean();
        user.nickname = RandomStringUtils.randomAlphanumeric(12);
        user.portrait = RandomStringUtils.randomAlphabetic(24);
        user.score = RandomUtils.nextFloat();
        user.loginTimes = RandomUtils.nextInt(10, 10000);
        user.createTime = new Date();
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
            .setCreateTime(createTime.getTime());
        return builder.build();
    }

}
