package com.android.tigerhelp.banner2;

import android.support.v4.view.ViewPager;

import com.android.tigerhelp.banner2.transformer.AccordionTransformer;
import com.android.tigerhelp.banner2.transformer.BackgroundToForegroundTransformer;
import com.android.tigerhelp.banner2.transformer.CubeInTransformer;
import com.android.tigerhelp.banner2.transformer.CubeOutTransformer;
import com.android.tigerhelp.banner2.transformer.DefaultTransformer;
import com.android.tigerhelp.banner2.transformer.DepthPageTransformer;
import com.android.tigerhelp.banner2.transformer.FlipHorizontalTransformer;
import com.android.tigerhelp.banner2.transformer.FlipVerticalTransformer;
import com.android.tigerhelp.banner2.transformer.ForegroundToBackgroundTransformer;
import com.android.tigerhelp.banner2.transformer.RotateDownTransformer;
import com.android.tigerhelp.banner2.transformer.RotateUpTransformer;
import com.android.tigerhelp.banner2.transformer.ScaleInOutTransformer;
import com.android.tigerhelp.banner2.transformer.StackTransformer;
import com.android.tigerhelp.banner2.transformer.TabletTransformer;
import com.android.tigerhelp.banner2.transformer.ZoomInTransformer;
import com.android.tigerhelp.banner2.transformer.ZoomOutSlideTransformer;
import com.android.tigerhelp.banner2.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends ViewPager.PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
