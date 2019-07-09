package com.sk.dididemo.event;

/**
 * @author sk on 2019-07-09.
 */
public class RefreshTitleBarEvent {

    private boolean hideTitleBar;
    public RefreshTitleBarEvent(boolean hideTitleBar) {
        this.hideTitleBar = hideTitleBar;
    }

    public boolean isHideTitleBar() {
        return hideTitleBar;
    }
}
