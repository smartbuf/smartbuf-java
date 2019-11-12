package com.github.sisyphsu.smartbuf.benchmark.large;

import lombok.Data;
import org.msgpack.annotation.Message;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sulin
 * @since 2019-11-10 15:28:14
 */
@Data
@Message
public class TrendModel {

    public Long         id;
    public String       id_str;
    public String       name;
    public String       screen_name;
    public String       location;
    public String       description;
    public String       url;
    public Entities     entities;
    public Boolean      protect;
    public Long         followers_count;
    public Long         fast_followers_count;
    public Long         normal_followers_count;
    public Long         friends_count;
    public Long         listed_count;
    public String       created_at;
    public Long         favourites_count;
    public String       utc_offset;
    public String       time_zone;
    public Boolean      geo_enabled;
    public Boolean      verified;
    public Long         statuses_count;
    public Long         media_count;
    public String       lang;
    public Boolean      contributors_enabled;
    public Boolean      is_translator;
    public Boolean      is_translation_enabled;
    public String       profile_background_color;
    public String       profile_background_image_url;
    public String       profile_background_image_url_https;
    public Boolean      profile_background_tile;
    public String       profile_image_url;
    public String       profile_image_url_https;
    public String       profile_banner_url;
    public String       profile_link_color;
    public String       profile_sidebar_border_color;
    public String       profile_sidebar_fill_color;
    public String       profile_text_color;
    public Boolean      profile_use_background_image;
    public Boolean      has_extended_profile;
    public Boolean      default_profile;
    public Boolean      default_profile_image;
    public List<Long>   pinned_tweet_ids;
    public List<String> pinned_tweet_ids_str;
    public Boolean      has_custom_timelines;
    public Boolean      can_dm;
    public Boolean      can_media_tag;
    public Boolean      following;
    public Boolean      follow_request_sent;
    public Boolean      notifications;
    public Boolean      muting;
    public Boolean      blocking;
    public Boolean      blocked_by;
    public Boolean      want_retweets;
    public String       advertiser_account_type;
    public List<String> advertiser_account_service_levels;
    public String       profile_interstitial_type;
    public String       business_profile_state;
    public String       translator_type;
    public Boolean      followed_by;
    public Boolean      require_some_consent;

    @Data
    @Message
    public static class Entities {
        public Urls        url;
        public Description description;
    }

    @Data
    @Message
    public static class Description {
        public List<Url> urls;

        public Large.Description toPB() {
            return Large.Description.newBuilder()
                .addAllUrls(urls.stream().map(Url::toPB).collect(Collectors.toList()))
                .build();
        }
    }

    @Data
    @Message
    public static class Urls {
        public List<Url> urls;

        public Large.Urls toPB() {
            return Large.Urls.newBuilder()
                .addAllUrls(urls.stream().map(Url::toPB).collect(Collectors.toList()))
                .build();
        }
    }

    @Data
    @Message
    public static class Url {
        public String     url;
        public String     expanded_url;
        public String     display_url;
        public List<Long> indices;

        public Large.Url toPB() {
            return Large.Url.newBuilder()
                .setUrl(url)
                .setExpandedUrl(expanded_url)
                .setDisplayUrl(display_url)
                .addAllIndices(indices)
                .build();
        }
    }

    public Large.Trend toPB() {
        return Large.Trend.newBuilder()
            .setId(id)
            .setIdStr(id_str)
            .setName(name)
            .setScreenName(screen_name)
            .setLocation(location)
            .setDescription(description)
            .setUrl(url)
            .setEntities(Large.Entities.newBuilder()
                .setDescription(entities.description == null ? Large.Description.getDefaultInstance() : entities.description.toPB())
                .setUrl(entities.url == null ? Large.Urls.getDefaultInstance() : entities.url.toPB())
                .build())
            .setProtect(protect)
            .setFollowersCount(followers_count)
            .setFastFollowersCount(fast_followers_count)
            .setNormalFollowersCount(normal_followers_count)
            .setFriendsCount(friends_count)
            .setListedCount(listed_count)
            .setCreatedAt(created_at)
            .setFavouritesCount(favourites_count)
            .setUtcOffset(utc_offset)
            .setTimeZone(time_zone)
            .setGeoEnabled(geo_enabled)
            .setVerified(verified)
            .setStatusesCount(statuses_count)
            .setMediaCount(media_count)
            .setLang(lang)
            .setContributorsEnabled(contributors_enabled)
            .setIsTranslator(is_translator)
            .setIsTranslationEnabled(is_translation_enabled)
            .setProfileBackgroundColor(profile_background_color)
            .setProfileBackgroundImageUrl(profile_background_image_url)
            .setProfileBackgroundImageUrlHttps(profile_background_image_url_https)
            .setProfileBackgroundTile(profile_background_tile)
            .setProfileImageUrl(profile_image_url)
            .setProfileImageUrlHttps(profile_image_url_https)
            .setProfileBannerUrl(profile_banner_url)
            .setProfileLinkColor(profile_link_color)
            .setProfileSidebarBorderColor(profile_sidebar_border_color)
            .setProfileSidebarFillColor(profile_sidebar_fill_color)
            .setProfileTextColor(profile_text_color)
            .setProfileUseBackgroundImage(profile_use_background_image)
            .setHasExtendedProfile(has_extended_profile)
            .setDefaultProfile(default_profile)
            .setDefaultProfileImage(default_profile_image)
            .addAllPinnedTweetIds(pinned_tweet_ids)
            .addAllPinnedTweetIdsStr(pinned_tweet_ids_str)
            .setHasCustomTimelines(has_custom_timelines)
            .setCanDm(can_dm)
            .setCanMediaTag(can_media_tag)
            .setFollowing(following)
            .setFollowRequestSent(follow_request_sent)
            .setNotifications(notifications)
            .setMuting(muting)
            .setBlocking(blocking)
            .setBlockedBy(blocked_by)
            .setWantRetweets(want_retweets)
            .setAdvertiserAccountType(advertiser_account_type)
            .addAllAdvertiserAccountServiceLevels(advertiser_account_service_levels)
            .setProfileInterstitialType(profile_interstitial_type)
            .setBusinessProfileState(business_profile_state)
            .setTranslatorType(translator_type)
            .setFollowedBy(followed_by)
            .setRequireSomeConsent(require_some_consent)
            .build();
    }

}
