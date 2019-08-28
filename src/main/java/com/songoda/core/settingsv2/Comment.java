package com.songoda.core.settingsv2;

import com.songoda.core.settingsv2.ConfigFormattingRules.CommentStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A comment for a configuration key
 *
 * @since 2019-08-28
 * @author jascotty2
 */
public class Comment {

    final List<String> lines = new ArrayList();
    CommentStyle commentStyle = null;

    public Comment() {
    }

    public Comment(String... lines) {
        this.lines.addAll(Arrays.asList(lines));
    }

    public Comment(List<String> lines) {
        this.lines.addAll(lines);
    }

    public CommentStyle getCommentStyle() {
        return commentStyle;
    }

    public void setCommentStyle(CommentStyle commentStyle) {
        this.commentStyle = commentStyle;
    }

    public List<String> getLines() {
        return lines;
    }

}
