package com.songoda.core.configuration;

public class ConfigFormattingRules {

    int spacesBetweenMainCategories;
    int spacesBetweenValues;
    CommentStyle rootCommentStyle = CommentStyle.BLOCKSPACED;
    CommentStyle mainCategoryCommentStyle = CommentStyle.SPACED;

    public static enum CommentStyle {

        /**
         * # Comment
         */
        SIMPLE(false, false, " ", ""),
        /**
         * #           <br />
         * # Comment   <br />
         * #           <br />
         */
        SPACED(false, true, " ", ""),
        /**
         * ########### <br />
         * # Comment # <br />
         * ########### <br />
         */
        BLOCKED(true, false, " ", " "),
        /**
         * ############# <br />
         * #|¯¯¯¯¯¯¯¯¯|# <br />
         * #| Comment |# <br />
         * #|_________|# <br />
         * ############# <br />
         */
        BLOCKSPACED(true, true, "|\u00AF", '\u00AF', "\u00AF|", "| ", " |", "|_", '_', "_|");

        final boolean drawBorder, drawSpace;
        final String commentPrefix, spacePrefixTop, spacePrefixBottom;
        final String commentSuffix, spaceSuffixTop, spaceSuffixBottom;
        final char spaceCharTop, spaceCharBottom;

        private CommentStyle(boolean drawBorder, boolean drawSpace,
                String spacePrefixTop, char spaceCharTop, String spaceSuffixTop,
                String commentPrefix, String commentSuffix,
                String spacePrefixBottom, char spaceCharBottom, String spaceSuffixBottom) {
            this.drawBorder = drawBorder;
            this.drawSpace = drawSpace;
            this.commentPrefix = commentPrefix;
            this.spacePrefixTop = spacePrefixTop;
            this.spacePrefixBottom = spacePrefixBottom;
            this.commentSuffix = commentSuffix;
            this.spaceSuffixTop = spaceSuffixTop;
            this.spaceSuffixBottom = spaceSuffixBottom;
            this.spaceCharTop = spaceCharTop;
            this.spaceCharBottom = spaceCharBottom;
        }

        private CommentStyle(boolean drawBorder, boolean drawSpace, String commentPrefix, String commentSuffix) {
            this.drawBorder = drawBorder;
            this.drawSpace = drawSpace;
            this.commentPrefix = commentPrefix;
            this.commentSuffix = commentSuffix;
            this.spacePrefixTop = this.spacePrefixBottom = "";
            this.spaceCharTop = this.spaceCharBottom = ' ';
            this.spaceSuffixTop = this.spaceSuffixBottom = "";
        }

    }
}
