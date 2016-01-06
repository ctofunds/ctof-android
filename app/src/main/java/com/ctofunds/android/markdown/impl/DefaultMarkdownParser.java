package com.ctofunds.android.markdown.impl;

import com.commonsware.cwac.anddown.AndDown;
import com.ctofunds.android.markdown.MarkdownParser;

/**
 * Created by qianhao.zhou on 1/6/16.
 */
public class DefaultMarkdownParser implements MarkdownParser {

    private final AndDown parser;

    public DefaultMarkdownParser() {
        this.parser = new AndDown();
    }

    @Override
    public String toHtml(String markdown) {
        return parser.markdownToHtml(markdown);
    }
}
