package com.tarena.fanly.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by tarena on 2017/6/21.
 */

public class PinYinUtil {
    /**
     * 利用pinyin4j，将参数的内容转换成对应的汉语拼音
     * @param name
     * @return
     */
    public static String getPinYin(String name){
        String result= null;
        try {
            result = "";
            // 1.设置汉语拼音的格式BEIJIN(UV)
            HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 大写
            format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);// 不要语音语调

            // 2.根据设定好的格式逐字进行汉字到拼音的转换
            StringBuilder sb=new StringBuilder();
            for (int i=0;i<name.length();i++){
                char c =name.charAt(i);
                String[] strings = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (strings.length>0) {
                    sb.append(strings[0]);
                }
            }
            result=sb.toString();
            return result;

        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
            throw new RuntimeException("不正确的汉语格式");
        }



    }
}
