package com.github.sisyphsu.smartbuf.benchmark.medium;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.msgpack.annotation.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sulin
 * @since 2019-10-31 19:58:41
 */
@Data
@Message
public class UserModel {

    private long    id;
    private String  nickname;
    private String  portrait;
    private float   score;
    private String  mail;
    private String  mobile;
    private String  token;
    private Integer type;
    private Integer source;
    private Boolean blocked;
    private int     loginTimes;
    private long    updateTime;
    private long    createTime;

    private List<Message> msgs;
    private List<Tag>     tags;

    public static UserModel random() {
        UserModel user = new UserModel();
        user.id = RandomUtils.nextLong();
        user.nickname = RandomStringUtils.randomAlphanumeric(12);
        user.portrait = RandomStringUtils.randomAlphabetic(24);
        user.score = RandomUtils.nextFloat();
        user.mail = RandomStringUtils.randomAlphanumeric(16) + "@gmail.com";
        user.mobile = RandomStringUtils.randomNumeric(12);
        user.token = UUID.randomUUID().toString();
        user.type = RandomUtils.nextInt(1, 10);
        user.source = RandomUtils.nextInt(1, 10);
        user.blocked = RandomUtils.nextBoolean();
        user.loginTimes = RandomUtils.nextInt(10, 10000);
        user.updateTime = System.currentTimeMillis();
        user.createTime = System.currentTimeMillis();
        user.msgs = new ArrayList<>();
        user.tags = new ArrayList<>();

        long toUUID = RandomUtils.nextInt(1000000, 9999999);
        for (int i = 0; i < 10; i++) {
            Message m = new Message();
            m.id = (long) i;
            m.from = RandomUtils.nextLong(1000000, 9999999);
            m.to = toUUID;
            m.msg = RandomStringUtils.randomAlphabetic(40);
            m.timestamp = System.currentTimeMillis();
            user.msgs.add(m);
        }
        for (int i = 0; i < 16; i++) {
            user.tags.add(new Tag(i, "Tag" + i));
        }
        return user;
    }

    public Medium.User toUser() {
        Medium.User.Builder builder = Medium.User.newBuilder()
            .setId(id)
            .setNickname(nickname)
            .setPortrait(portrait)
            .setScore(score)
            .setMail(mail)
            .setMobile(mobile)
            .setToken(token)
            .setType(type)
            .setSource(source)
            .setBlocked(blocked)
            .setLoginTimes(loginTimes)
            .setUpdateTime(updateTime)
            .setCreateTime(createTime);

        for (Message msg : msgs) {
            builder.addMsgs(Medium.Message.newBuilder()
                .setId(msg.id)
                .setFrom(msg.from)
                .setTo(msg.to)
                .setMsg(msg.msg)
                .setTimestamp(msg.timestamp)
                .build());
        }

        for (Tag tag : tags) {
            builder.addTags(Medium.Tag.newBuilder()
                .setCode(tag.code)
                .setName(tag.name)
                .build());
        }

        return builder.build();
    }

    @Data
    @org.msgpack.annotation.Message
    public static class Message {
        private Long   id;
        private Long   from;
        private Long   to;
        private String msg;
        private Long   timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @org.msgpack.annotation.Message
    public static class Tag {
        private int    code;
        private String name;
    }

}
