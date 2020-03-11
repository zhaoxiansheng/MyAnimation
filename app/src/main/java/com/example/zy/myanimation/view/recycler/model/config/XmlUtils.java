package com.example.zy.myanimation.view.recycler.model.config;

import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class XmlUtils {

    private static final String TAG = "XmlUtils";
    private static final int VALUE_TYPE_NO = -1;
    private static final int VALUE_TYPE_PACKAGE = 1;
    private static final int VALUE_TYPE_ICON = 2;
    private static final int VALUE_TYPE_BACKGROUND = 3;

    public static void parseXml(XmlResourceParser xmlParser) {
        try {
            int mValueType = VALUE_TYPE_NO;
            int event = xmlParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "parseXml: START_DOCUMENT");
                        AppsConfig.listIcon.clear();
                        AppsConfig.listPackage.clear();
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlParser.getName().equals("icon")) {
                            mValueType = VALUE_TYPE_ICON;
                            Log.i(TAG, "parseXml: icon");
                        } else if (xmlParser.getName().equals("package")) {
                            mValueType = VALUE_TYPE_PACKAGE;
                            Log.i(TAG, "parseXml: package");
                        } else if (xmlParser.getName().equals("background")) {
                            mValueType = VALUE_TYPE_BACKGROUND;
                            Log.i(TAG, "parseXml: background");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (mValueType) {
                            case VALUE_TYPE_ICON:
                                Log.i(TAG, "parseXml: xmlParser.getText()=" + xmlParser.getText());
                                AppsConfig.listIcon.add(xmlParser.getText());
                                break;
                            case VALUE_TYPE_PACKAGE:
                                AppsConfig.listPackage.add(xmlParser.getText());
                                Log.i(TAG, "parseXml: xmlParser.getText()=" + xmlParser.getText());
                                break;
                            case VALUE_TYPE_BACKGROUND:
                                AppsConfig.setBackground(xmlParser.getText());
                                Log.i(TAG, "parseXml: xmlParser.getText()=" + xmlParser.getText());
                                break;
                            default:
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        mValueType = VALUE_TYPE_NO;
                        break;
                    default:
                        break;
                }
                event = xmlParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
