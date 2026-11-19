package com.carmatch.service;

import com.carmatch.model.enums.BodyStyle;

public class BodyStyleCompatibility {
    public static int getBodyStyleScore(BodyStyle bodyStyle, BodyStyle targetBodyStyle) {
        if (targetBodyStyle == null || targetBodyStyle == bodyStyle) {
            return 100;
        }

        return 70;
    }
}
