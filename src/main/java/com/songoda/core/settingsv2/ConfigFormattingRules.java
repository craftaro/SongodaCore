package com.songoda.core.settingsv2;

public class ConfigFormattingRules {

    int spacesBetweenMainCategories;
    int spacesBetweenValues;
    CommentStyle rootCommentStyle = CommentStyle.BLOCKSPACED;
    CommentStyle mainCategoryCommentStyle = CommentStyle.SPACED;

    public static enum CommentStyle {

        /**
         * # Comment
         */
        SIMPLE,
        /**
         * #           <br />
         * # Comment   <br />
         * #           <br />
         */
        SPACED,
        /**
         * ########### <br />
         * # Comment # <br />
         * ########### <br />
         */
        BLOCKED,
        /**
         * ############# <br />
         * #|¯¯¯¯¯¯¯¯¯|# <br />
         * #| Comment |# <br />
         * #|_________|# <br />
         * ############# <br />
         */
        BLOCKSPACED
    }
}
