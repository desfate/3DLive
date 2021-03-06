package com.futrtch.live.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import androidx.appcompat.widget.Toolbar;

import com.futrtch.live.R;
import com.google.android.material.appbar.AppBarLayout;

public class EdgeToEdgeUtils {

    public static void setUpRoot(ViewGroup root) {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public static void setUpAppBar(AppBarLayout appBar, Toolbar toolbar) {
        int toolbarPadding = toolbar.getResources().getDimensionPixelSize(R.dimen.spacing_medium);
        appBar.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                appBar.setPadding(0,
                        insets.getSystemWindowInsetTop()
                ,0,0);

                toolbar.setPadding(
                        toolbarPadding + insets.getSystemWindowInsetLeft(),
                        0,
                        insets.getSystemWindowInsetRight(),
                        0

                );
                return insets;
            }
        });
    }

    public static void setUpScrollingContent(ViewGroup scrollingContent) {
        int originalPaddingLeft = scrollingContent.getPaddingLeft();
        int originalPaddingRight = scrollingContent.getPaddingRight();
        int originalPaddingBottom = scrollingContent.getPaddingBottom();
        scrollingContent.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                scrollingContent.setPadding(originalPaddingLeft + insets.getSystemWindowInsetLeft(),
                        0,
                        originalPaddingRight + insets.getSystemWindowInsetRight(),
                        originalPaddingBottom + insets.getSystemWindowInsetBottom());
                return insets;
            }
        });
    }

   public interface EdgeToEdgeImpl {
        /**
         * Configures a root view of an Activity in edge-to-edge display.
         * @param root A root view of an Activity.
         */
        void setUpRoot(ViewGroup root);

        /**
         * Configures an app bar and a toolbar for edge-to-edge display.
         * @param appBar An [AppBarLayout].
         * @param toolbar A [Toolbar] in the [appBar].
         */
        void setUpAppBar(AppBarLayout appBar, Toolbar toolbar);

        /**
         * Configures a scrolling content for edge-to-edge display.
         * @param scrollingContent A scrolling ViewGroup. This is typically a RecyclerView or a
         * ScrollView. It should be as wide as the screen, and should touch the bottom edge of
         * the screen.
         */
        void setUpScrollingContent(ViewGroup scrollingContent);
    }
}
