package com.vacry.vatty.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具集 全部为静态方法
 * 
 * @author smartlv
 */
public final class StringUtil
{

    /** UTF-8编码常量 */
    public static final String ENC_UTF8 = "UTF-8";

    /** GBK编码常量 */
    public static final String ENC_GBK = "GBK";

    /** GBK的Charset */
    public static final Charset GBK = Charset.forName("GBK");

    /** UTF-8的Charset */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /** 精确到秒的日期时间格式化的格式字符串 */
    public static final String FMT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 如果字符串太长了 则截取某一段 后面是省略号replaceStr(...)
     * @param str
     * @param length
     * @param replaceStr
     * @return
     */
    public static String ellipsisStrByLength(String str, int length, String replaceStr)
    {
        if(!isEmpty(str) && str.length() > length)
        {
            return str.substring(0, length) + replaceStr;
        }
        return str;
    }

    /**
     * 将字符串转为WML编码,用于wml页面显示 根据unicode编码规则Blocks.txt：E000..F8FF; Private Use Area
     * 
     * @param str
     * @return String
     */
    public static String encodeWML(String str)
    {
        if(str == null)
        {
            return "";
        }
        // 不用正则表达式替换，直接通过循环，节省cpu时间
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); ++i)
        {
            char c = str.charAt(i);
            switch (c)
            {
                case '\u00FF':
                case '\u200B':// ZERO WIDTH SPACE
                case '\uFEFF':// ZERO WIDTH NO-BREAK SPACE
                case '\u0024':
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\t':
                    sb.append("  ");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '\n':
                    sb.append("<br/>");
                    break;
                default:
                    if(c >= '\u0000' && c <= '\u001F')
                    {
                        break;
                    }
                    if(c >= '\uE000' && c <= '\uF8FF')
                    {
                        break;
                    }
                    if(c >= '\uFFF0' && c <= '\uFFFF')
                    {
                        break;
                    }
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 转换&#123;这种编码为正常字符<br/>
     * 有些手机会将中文转换成&#123;这种编码,这个函数主要用来转换成正常字符.
     * 
     * @param str
     * @return String
     */
    public static String decodeNetUnicode(String str)
    {
        if(str == null)
        {
            return null;
        }

        String pStr = "&#(\\d+);";
        Pattern p = Pattern.compile(pStr);
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find())
        {
            String mcStr = m.group(1);
            int charValue = StringUtil.convertInt(mcStr, -1);
            String s = charValue > 0 ? (char)charValue + "" : "";
            m.appendReplacement(sb, Matcher.quoteReplacement(s));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 过滤SQL字符串,防止SQL inject
     * 
     * @param sql
     * @return String
     */
    public static String encodeSQL(String sql)
    {
        if(sql == null)
        {
            return "";
        }
        // 不用正则表达式替换，直接通过循环，节省cpu时间
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < sql.length(); ++i)
        {
            char c = sql.charAt(i);
            switch (c)
            {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\'':
                    sb.append("\'\'");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\u200B':// ZERO WIDTH SPACE
                case '\uFEFF':// ZERO WIDTH NO-BREAK SPACE
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 删除在wml下不能正确处理的字符 根据unicode编码规则Blocks.txt：E000..F8FF; Private Use Area
     * 
     * @param str
     *        要处理的字符串
     * @return 结果
     */
    public static String removeInvalidWML(String str)
    {
        if(str == null)
        {
            return null;
        }
        // * 不用正则表达式替换，直接通过循环，节省cpu时间
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); ++i)
        {
            char c = str.charAt(i);
            if(c >= '\u0000' && c <= '\u001F')
            {
                continue;
            }
            if(c >= '\uE000' && c <= '\uF8FF')
            {
                continue;
            }
            if(c >= '\uFFF0' && c <= '\uFFFF')
            {
                continue;
            }
            switch (c)
            {
                case '\u00FF':
                case '\u200B':// ZERO WIDTH SPACE
                case '\uFEFF':// ZERO WIDTH NO-BREAK SPACE
                case '\u0024':
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\t':
                    sb.append("  ");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '^':
                case '`':
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 新增一个可以多次执行去除非法字符以及进行xml转义的方法，将unicode私有区域的合法xml字符也移除了
     * 
     * @param str
     * @return 如果字符串中有已经转义的实体字符串，则跳过，否则转义避免amp;amp;这样的情形出现
     * @author quickli
     */

    public static String safeRemoveInvalidWML(String str)
    {
        return StringUtil.safeRemoveInvalidWML(str, true);
    }

    /**
     * 新增一个可以多次执行去除非法字符以及进行xml转义的方法<br/>
     * 与removeInvalidWML区别如下：<br/>
     * 1.本方法严格按照xml规范进行过滤，removeInvalidWML则还过滤了$,^,`,\u00FF,\UE000-\UF8FF( Unicode私有保留区Private Use Area)区间的字符<br/>
     * 2.本方法按照xml规范对5个公共转义字符做了转义，removeInvalidWML未对单引号转义<br/>
     * 3.本方法对$,\r,\n,\t字符使用了&#加unicode值的方式进行表示，removeInvalidWML则将$,\r\,\n直接去除，将\ t转换为两个空格<br/>
     * 4.本方法针对超过\uFFFF的Unicode字符做了高代理判断，支持将非法高代理或低代理字符去除，removeInvalidWML没有做过滤<br/>
     * 测试用例为：<br/>
     * String s = "0\u00031&2&amp;3&amp;amp;4&gt;5&lt;6&apos;7&quot;"<br/>
     * + "8<9>10\'11\"12\n13\r14\t15&#37;16&#;17&#y;18&#7654321;19"<br/>
     * + "&amp;lt;20&amp;gt;21&amp;quot;22&amp;apos;23$\uD860\uDEE224\uDEE2\uD860aaa" ;<br/>
     * System.out.println("safeRemoveInvalidWML=" + safeRemoveInvalidWML(s));<br/>
     * System.out.println("removeInvalidWML=" + removeInvalidWML(s));<br/>
     * 
     * @param str
     *        需要进行过滤xml非法字符并进行xml转义的字符串
     * @param removePrivateUseArea
     *        是否移除虽然是xml合法字符但却是在unicode里私有保留区里的字符
     * @return 如果字符串中有已经转义的实体字符串，则跳过，否则转义避免amp;amp;这样的情形出现
     * @see
     * @author quickli
     */
    public static String safeRemoveInvalidWML(String str, boolean removePrivateUseArea)
    {
        if(str == null || str.isEmpty())
        {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length() + 48);
        for(int i = 0, len = str.length(); i < len; i++)
        {
            char c = str.charAt(i);
            if(Character.isHighSurrogate(c))
            {// 如果已经是高代理字符，则可能是超过\uFFFF的unicode了
                int codePoint = str.codePointAt(i);// 进行代码点解析
                if(codePoint == c)
                {// 解析后的值与单个字符想通，说明只有单个高代理字符，则编码有问题，需要过滤该字符
                    continue;
                }
                else if(!StringUtil.isXMLCharacter(codePoint))
                {// 非法xml字符滤掉
                 // System.err.println(codePoint + "|"
                 // + Integer.toHexString(codePoint)
                 // + " is not xml char a,i=" + i + ",len=" + len);
                    i++;
                    continue;
                }
                else if(removePrivateUseArea && ((codePoint >= 0xF0000 && codePoint <= 0xFFFFD)
                                                 || (codePoint >= 0x100000 && codePoint <= 0x10FFFD)))
                {
                    // 过滤高代理的PrivateUseArea区的字符,
                    // Supplementary Private Use Area-A Range: F0000–FFFFD
                    // Supplementary Private Use Area-B Range: 100000–10FFFD
                    i++;
                    continue;
                }
                else
                {
                    i++;
                    sb.appendCodePoint(codePoint);
                    continue;
                }
            }
            if(!StringUtil.isXMLCharacter(c))
            {// 跳过非法xml字符
             // System.err.println((int) c + "|" + Integer.toHexString(c)
             // + " is not xml char");
                continue;
            }
            if(removePrivateUseArea && c >= '\uE000' && c <= '\uF8FF')
            {// 过滤PrivateUseArea区的字符
                continue;
            }
            if(removePrivateUseArea && c == '\u202E')
            {// 过滤RIGHT-TO-LEFT
             // OVERRIDE转义字符
             // http://www.fileformat.info/info/unicode/char/202e/index.htm
                continue;
            }
            switch (c)
            {
                case '&':
                    if(str.startsWith("&amp;amp;", i))
                    {// 把两个amp;的兼容掉
                        sb.append("&amp;");
                        i = i + 8;
                    }
                    else if(str.startsWith("&amp;gt;", i))
                    {// 把多encode了一次的导致amp;的兼容掉
                        sb.append("&gt;");
                        i = i + 7;
                    }
                    else if(str.startsWith("&amp;lt;", i))
                    {// 把多encode了一次的导致amp;的兼容掉
                        sb.append("&lt;");
                        i = i + 7;
                    }
                    else if(str.startsWith("&amp;apos;", i))
                    {// 把多encode了一次的导致amp;的兼容掉
                        sb.append("&apos;");
                        i = i + 9;
                    }
                    else if(str.startsWith("&amp;quot;", i))
                    {// 把多encode了一次的导致amp;的兼容掉
                        sb.append("&quot;");
                        i = i + 9;
                    }
                    else if(str.startsWith("&amp;", i))
                    {// 把已经encode的amp;的兼容掉
                        sb.append("&amp;");
                        i = i + 4;
                    }
                    else if(str.startsWith("&gt;", i))
                    {
                        sb.append("&gt;");
                        i = i + 3;
                    }
                    else if(str.startsWith("&lt;", i))
                    {
                        sb.append("&lt;");
                        i = i + 3;
                    }
                    else if(str.startsWith("&apos;", i))
                    {
                        sb.append("&apos;");
                        i = i + 5;
                    }
                    else if(str.startsWith("&quot;", i))
                    {
                        sb.append("&quot;");
                        i = i + 5;
                    }
                    else if(str.startsWith("&#", i))
                    {// 检测已经是&#37;这样编码字符串
                        int index = -1;
                        for(int j = i + 2; j < i + 10 && j < len; j++)
                        {// xml字符用数字转义方式表示的最大值是&#111411;,因此往前最多检测到10位即可
                            char cc = str.charAt(j);
                            if(cc == ';')
                            {
                                index = j;
                                break;
                            }
                        }
                        // System.out.println("index=" + index + ",i==" + i);
                        if(index > i + 2)
                        {// 说明&#和;之间有字符存在，则尝试反解析
                            String unicodeVal = str.substring(i + 2, index);
                            // System.out.println("index=" + index + ",i==" + i
                            // + ",unicodeVal=" + unicodeVal);
                            try
                            {
                                int val = Integer.parseInt(unicodeVal.substring(1),
                                                           'x' == unicodeVal.charAt(0) ? 16 : 10);
                                // System.out.println("val==" + val);
                                if(!StringUtil.isXMLCharacter(val))
                                {
                                    sb.append("&amp;");// &#后面的字符无法反解析为合法xml字符，因此继续转义
                                }
                                else
                                {// 否则原样拼接
                                    sb.append("&#").append(unicodeVal).append(';');
                                    i = i + 2 + unicodeVal.length();
                                }
                            }
                            catch (Exception e)
                            {
                                sb.append("&amp;");
                            }
                        }
                        else
                        {
                            sb.append("&amp;");
                        }
                    }
                    else
                    {
                        sb.append("&amp;");
                    }
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                case '$':// wml中$在postfield的value中表示变量定义，因此需要展示真实的$时，需要转义
                case '\n':
                case '\r':
                case '\t':
                    sb.append("&#").append((int)c).append(';');
                    break;
                // 利用两个特殊字符做xss和sql注入的预防
                // @see http://www.cs.tut.fi/~jkorpela/chars/spaces.html
                case '\u200B':// ZERO WIDTH SPACE
                case '\uFEFF':// ZERO WIDTH NO-BREAK SPACE
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 返回移除非法xml字符后的字符串，确保json和xml中的字符串能被正常解析
     * 
     * @param str
     * @return
     */
    public static String removeInvalidXmlChar(String str)
    {
        if(str == null || str.length() < 1)
        {
            return str;
        }
        for(int k = 0, len = str.length(); k < len; k++)
        {
            char c = str.charAt(k);
            if(!StringUtil.isXMLCharacter(c))
            {
                StringBuilder sb = new StringBuilder(str.length() + 48);
                sb.append(str, 0, k);
                for(int i = k; i < len; i++)
                {
                    c = str.charAt(i);
                    if(Character.isHighSurrogate(c))
                    {// 如果已经是高代理字符，则可能是超过\uFFFF的unicode了
                        int codePoint = str.codePointAt(i);// 进行代码点解析
                        if(codePoint == c)
                        {// 解析后的值与单个字符相同，说明只有单个高代理字符，则编码有问题，需要过滤该字符
                            continue;
                        }
                        else if(!StringUtil.isXMLCharacter(codePoint))
                        {// 非法xml字符滤掉
                         // System.err.println(codePoint + "|"
                         // + Integer.toHexString(codePoint)
                         // + " is not xml char a,i=" + i + ",len=" + len);
                            i++;
                            continue;
                        }
                        else
                        {
                            i++;
                            sb.appendCodePoint(codePoint);
                            continue;
                        }
                    }
                    else if(StringUtil.isXMLCharacter(c))
                    {
                        sb.append(c);
                    }
                }
                return sb.toString();
            }
        }
        return str;
    }

    /**
     * 新增一个可以多次执行去除非法字符以及进行xml转义反解析的方法<br/>
     * 
     * @param str
     *        需要反解析xml的字符串
     * @return 返回将xml转义字符反解析后的字符串，默认过滤掉xml字符允许但是是unicode私有区域的字符
     * @author quickli
     */
    public static String decodeWML(String str)
    {
        return StringUtil.decodeWML(str, true);
    }

    /**
     * 新增一个可以多次执行去除非法字符以及进行xml转义反解析的方法<br/>
     * 
     * @param str
     *        需要反解析xml的字符串
     * @param removePrivateUseArea
     *        是否去除unicode私有区的字符
     * @return 返回反解析后的字符串
     * @author quickli
     */
    public static String decodeWML(String str, boolean removePrivateUseArea)
    {
        if(str == null || str.isEmpty())
        {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());
        for(int i = 0, len = str.length(); i < len; i++)
        {
            char c = str.charAt(i);
            if(Character.isHighSurrogate(c))
            {// 如果已经是高代理字符，则可能是超过\uFFFF的unicode了
                int codePoint = str.codePointAt(i);// 进行代码点解析
                if(codePoint == c)
                {// 解析后的值与单个字符想通，说明只有单个高代理字符，则编码有问题，需要过滤该字符
                    continue;
                }
                else if(!StringUtil.isXMLCharacter(codePoint))
                {// 非法xml字符滤掉
                    i++;
                    continue;
                }
                else if(removePrivateUseArea && ((codePoint >= 0xF0000 && codePoint <= 0xFFFFD)
                                                 || (codePoint >= 0x100000 && codePoint <= 0x10FFFD)))
                {
                    // 过滤高代理的PrivateUseArea区的字符,
                    // Supplementary Private Use Area-A Range: F0000–FFFFD
                    // Supplementary Private Use Area-B Range: 100000–10FFFD
                    i++;
                    continue;
                }
                else
                {
                    i++;
                    sb.appendCodePoint(codePoint);
                    continue;
                }
            }
            if(!StringUtil.isXMLCharacter(c))
            {// 跳过非法xml字符
                continue;
            }
            if(removePrivateUseArea && c >= '\uE000' && c <= '\uF8FF')
            {// 过滤PrivateUseArea区的字符
                continue;
            }
            if(removePrivateUseArea && c == '\u202E')
            {// 过滤RIGHT-TO-LEFT
             // OVERRIDE转义字符
             // http://www.fileformat.info/info/unicode/char/202e/index.htm
                continue;
            }
            switch (c)
            {
                case '&':
                    if(str.startsWith("&amp;amp;", i))
                    {// 把两个amp;的兼容还原
                        sb.append("&");
                        i = i + 8;
                    }
                    else if(str.startsWith("&amp;gt;", i))
                    {// 把多encode了一次的导致amp;的兼容还原
                        sb.append(">");
                        i = i + 7;
                    }
                    else if(str.startsWith("&amp;lt;", i))
                    {// 把多encode了一次的导致amp;的兼容兼容还原
                        sb.append("<");
                        i = i + 7;
                    }
                    else if(str.startsWith("&amp;apos;", i))
                    {// 把多encode了一次的导致amp;的兼容兼容还原
                        sb.append("'");
                        i = i + 9;
                    }
                    else if(str.startsWith("&amp;quot;", i))
                    {// 把多encode了一次的导致amp;的兼容兼容还原
                        sb.append("\"");
                        i = i + 9;
                    }
                    else if(str.startsWith("&amp;nbsp;", i))
                    {// 把多encode了一次的导致amp;的兼容兼容还原
                        sb.append(" ");
                        i = i + 9;
                    }
                    else if(str.startsWith("&amp;", i))
                    {// 把已经encode的amp;的兼容兼容还原
                        sb.append("&");
                        i = i + 4;
                    }
                    else if(str.startsWith("&gt;", i))
                    {
                        sb.append(">");
                        i = i + 3;
                    }
                    else if(str.startsWith("&lt;", i))
                    {
                        sb.append("<");
                        i = i + 3;
                    }
                    else if(str.startsWith("&apos;", i))
                    {
                        sb.append("'");
                        i = i + 5;
                    }
                    else if(str.startsWith("&quot;", i))
                    {
                        sb.append("\"");
                        i = i + 5;
                    }
                    else if(str.startsWith("&nbsp;", i))
                    {
                        sb.append(" ");
                        i = i + 5;
                    }
                    else if(str.startsWith("&#", i))
                    {// 检测已经是&#37;这样编码字符串
                        int index = -1;
                        for(int j = i + 2; j < i + 10 && j < len; j++)
                        {// xml字符用数字转义方式表示的最大值是&#111411;,因此往前最多检测到10位即可
                            char cc = str.charAt(j);
                            if(cc == ';')
                            {
                                index = j;
                                break;
                            }
                        }
                        if(index > i + 2)
                        {// 说明&#和;之间有字符存在，则尝试反解析
                            String unicodeVal = str.substring(i + 2, index);
                            try
                            {
                                boolean hex = 'x' == unicodeVal.charAt(0);
                                int val = hex ? Integer.parseInt(unicodeVal.substring(1), 16)
                                    : Integer.parseInt(unicodeVal, 10);
                                if(!StringUtil.isXMLCharacter(val))
                                {
                                    sb.append("&");// &#后面的字符无法反解析为合法xml字符，因此继续保持转义
                                }
                                else
                                {// 否则还原成unicode字符
                                    if(removePrivateUseArea)
                                    {// 反解析后再次过滤文字反向和私有区域字符
                                        if(!((val == '\u202E') || (val >= '\uE000' && val <= '\uF8FF')
                                             || (val >= 0xF0000 && val <= 0xFFFFD)
                                             || (val >= 0x100000 && val <= 0x10FFFD)))
                                        {
                                            sb.appendCodePoint(val);
                                        }
                                        else
                                        {
                                            // System.err.println(" error for:"
                                            // + unicodeVal);
                                        }
                                    }
                                    else
                                    {
                                        sb.appendCodePoint(val);

                                    }
                                    i = i + 2 + unicodeVal.length();
                                }
                            }
                            catch (Exception e)
                            {
                                // System.err.println("error for :" + unicodeVal);//
                                // 继续原始编码方式
                                sb.append("&");
                            }
                        }
                        else
                        {
                            sb.append("&");
                        }
                    }
                    else
                    {
                        sb.append("&");
                    }
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 判断一个unicode值是否为合法的xml字符，从org.jdom.Verifier复制过来的
     * 
     * @param c
     * @return
     * @author quickli
     */
    public static boolean isXMLCharacter(int c)
    {
        if(c == '\n')
        {
            return true;
        }
        if(c == '\r')
        {
            return true;
        }
        if(c == '\t')
        {
            return true;
        }

        if(c < 0x20)
        {
            return false;
        }
        if(c <= 0xD7FF)
        {
            return true;
        }
        if(c < 0xE000)
        {
            return false;
        }
        if(c <= 0xFFFD)
        {
            return true;
        }
        if(c < 0x10000)
        {
            return false;
        }
        if(c <= 0x10FFFF)
        {
            return true;
        }

        return false;
    }

    /**
     * 获取字符型参数，若输入字符串为null，则返回设定的默认值
     * 
     * @param str
     *        输入字符串
     * @param defaults
     *        默认值
     * @return 字符串参数
     */
    public static String convertString(String str, String defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        else
        {
            return str;
        }
    }

    /**
     * 获取int参数，若输入字符串为null或不能转为int，则返回设定的默认值
     * 
     * @param str
     *        输入字符串
     * @param defaults
     *        默认值
     * @return int参数
     */
    public static int convertInt(String str, int defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Integer.parseInt(str);
        }
        catch (Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取long型参数，若输入字符串为null或不能转为long，则返回设定的默认值
     * 
     * @param str
     *        输入字符串
     * @param defaults
     *        默认值
     * @return long参数
     */
    public static long convertLong(String str, long defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Long.parseLong(str);
        }
        catch (Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取double型参数，若输入字符串为null或不能转为double，则返回设定的默认值
     * 
     * @param str
     *        输入字符串
     * @param defaults
     *        默认值
     * @return double型参数
     */
    public static double convertDouble(String str, double defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Double.parseDouble(str);
        }
        catch (Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取short型参数，若输入字符串为null或不能转为short，则返回设定的默认值
     * 
     * @param str
     *        输入字符串
     * @param defaults
     *        默认值
     * @return short型参数
     */
    public static short convertShort(String str, short defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Short.parseShort(str);
        }
        catch (Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取float型参数，若输入字符串为null或不能转为float，则返回设定的默认值
     * 
     * @param str
     *        输入字符串
     * @param defaults
     *        默认值
     * @return float型参数
     */
    public static float convertFloat(String str, float defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Float.parseFloat(str);
        }
        catch (Exception e)
        {
            return defaults;
        }
    }

    /**
     * 获取boolean型参数，若输入字符串为null或不能转为boolean，则返回设定的默认值
     * 
     * @param str
     *        输入字符串
     * @param defaults
     *        默认值
     * @return boolean型参数
     */
    public static boolean convertBoolean(String str, boolean defaults)
    {
        if(str == null)
        {
            return defaults;
        }
        try
        {
            return Boolean.parseBoolean(str);
        }
        catch (Exception e)
        {
            return defaults;
        }
    }

    /**
     * 分割字符串
     * 
     * @param line
     *        原始字符串
     * @param seperator
     *        分隔符
     * @return 分割结果
     */
    public static String[] split(String line, String seperator)
    {
        if(line == null || seperator == null || seperator.length() == 0)
        {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        int pos1 = 0;
        int pos2;
        for(;;)
        {
            pos2 = line.indexOf(seperator, pos1);
            if(pos2 < 0)
            {
                list.add(line.substring(pos1));
                break;
            }
            list.add(line.substring(pos1, pos2));
            pos1 = pos2 + seperator.length();
        }
        // 去掉末尾的空串，和String.split行为保持一致
        for(int i = list.size() - 1; i >= 0 && list.get(i).length() == 0; --i)
        {
            list.remove(i);
        }
        return list.toArray(new String[0]);
    }

    /**
     * 分割字符串，并转换为int
     * 
     * @param line
     *        原始字符串
     * @param seperator
     *        分隔符
     * @param def
     *        默认值
     * @return 分割结果
     */
    public static int[] splitInt(String line, String seperator, int def)
    {
        String[] ss = StringUtil.split(line, seperator);
        int[] r = new int[ss.length];
        for(int i = 0; i < r.length; ++i)
        {
            r[i] = StringUtil.convertInt(ss[i], def);
        }
        return r;
    }

    /**
     * 分割字符串，并转换为long
     * 
     * @param line
     *        原始字符串
     * @param seperator
     *        分隔符
     * @param def
     *        默认值
     * @return 分割结果
     */
    public static long[] splitLong(String line, String seperator, long def)
    {
        String[] ss = StringUtil.split(line, seperator);
        long[] r = new long[ss.length];
        for(int i = 0; i < r.length; ++i)
        {
            r[i] = StringUtil.convertLong(ss[i], def);
        }
        return r;
    }

    @SuppressWarnings(
    {"rawtypes"})
    public static String join(String separator, Collection c)
    {
        if(c.isEmpty())
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator i = c.iterator();
        sb.append(i.next());
        while (i.hasNext())
        {
            sb.append(separator);
            sb.append(i.next());
        }
        return sb.toString();
    }

    public static String join(String separator, String[] s)
    {
        return StringUtil.joinArray(separator, s);
    }

    public static String joinArray(String separator, Object[] s)
    {
        if(s == null || s.length == 0)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(s[0]);
        for(int i = 1; i < s.length; ++i)
        {
            if(s[i] != null)
            {
                sb.append(separator);
                sb.append(s[i].toString());
            }
        }
        return sb.toString();
    }

    public static String joinArray(String separator, int[] s)
    {
        if(s == null || s.length == 0)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(s[0]);
        for(int i = 1; i < s.length; ++i)
        {
            sb.append(separator);
            sb.append(s[i]);
        }
        return sb.toString();
    }

    public static String joinArray(String separator, long[] s)
    {
        if(s == null || s.length == 0)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(s[0]);
        for(int i = 1; i < s.length; ++i)
        {
            sb.append(separator);
            sb.append(s[i]);
        }
        return sb.toString();
    }

    public static String join(String separator, Object... c)
    {
        return StringUtil.joinArray(separator, c);
    }

    /**
     * 字符串全量替换,不适应于正则表达式替换
     * 
     * @param s
     *        原始字符串
     * @param src
     *        要替换的字符串
     * @param dest
     *        替换目标
     * @return 结果
     */
    public static String replaceAll(String s, String src, String dest)
    {
        if(s == null || src == null || dest == null || src.length() == 0)
        {
            return s;
        }
        int pos = s.indexOf(src); // 查找第一个替换的位置
        if(pos < 0)
        {
            return s;
        }
        int capacity = dest.length() > src.length() ? s.length() * 2 : s.length();
        StringBuilder sb = new StringBuilder(capacity);
        int writen = 0;
        for(; pos >= 0;)
        {
            sb.append(s, writen, pos); // append 原字符串不需替换部分
            sb.append(dest); // append 新字符串
            writen = pos + src.length(); // 忽略原字符串需要替换部分
            pos = s.indexOf(src, writen); // 查找下一个替换位置
        }
        sb.append(s, writen, s.length()); // 替换剩下的原字符串
        return sb.toString();
    }

    /**
     * 只替换第一个,不适应于正则表达式替换
     * 
     * @param s
     * @param src
     * @param dest
     * @return
     */
    public static String replaceFirst(String s, String src, String dest)
    {
        if(s == null || src == null || dest == null || src.length() == 0)
        {
            return s;
        }
        int pos = s.indexOf(src);
        if(pos < 0)
        {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() - src.length() + dest.length());

        sb.append(s, 0, pos);
        sb.append(dest);
        sb.append(s, pos + src.length(), s.length());
        return sb.toString();
    }

    /**
     * Returns <tt>true</tt> if s is null or <code>s.trim().length()==0<code>.
     * 
     * @see String#isEmpty()
     * @author isaacdong
     */
    public static boolean isEmpty(String s)
    {
        if(s == null)
        {
            return true;
        }
        return s.trim().isEmpty();
    }

    public static boolean isNotEmpty(String s)
    {
        return !StringUtil.isEmpty(s);
    }

    /**
     * @see String#trim()
     */
    public static String trim(String s)
    {
        if(s == null)
        {
            return null;
        }
        return s.trim();
    }

    public static String removeAll(String s, String src)
    {
        return StringUtil.replaceAll(s, src, "");
    }

    /**
     * 将字符串截短,功能与abbreviate()类似 全角字符算一个字,半角字符算半个字,这样做的目的是为了显示的时候排版整齐,因为全角字占的位置要比半角字小
     * 
     * @param str
     * @param maxLen
     * @return String
     */
    public static String toShort(String str, int maxLen, String replacement)
    {
        if(str == null)
        {
            return "";
        }
        if(str.length() <= maxLen)
        {
            return str;
        }
        StringBuilder dest = new StringBuilder();
        double len = 0;
        for(int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if(c >= '\u0000' && c <= '\u00FF')
            {// 半角字算半个字
                len += 0.5;
            }
            else
            {
                len += 1;
            }
            if(len > maxLen)
            {
                return dest.toString() + replacement;
            }
            else
            {
                dest.append(c);
            }
        }
        return dest.toString();
    }

    public static String toShort(String str, int maxLen)
    {
        return StringUtil.toShort(str, maxLen, "...");
    }

    /**
     * 计算字符串的显示长度，半角算１个长度，全角算两个长度
     * 
     * @param s
     * @return
     */
    public static int computeDisplayLen(String s)
    {
        int len = 0;
        if(s == null)
        {
            return len;
        }

        for(int i = 0; i < s.length(); ++i)
        {
            char c = s.charAt(i);
            if(c >= '\u0000' && c <= '\u00FF')
            {
                len = len + 1;
            }
            else
            {
                len = len + 2;
            }
        }
        return len;
    }

    /**
     * 获取字符串的UTF-8编码字节数组
     * 
     * @param s
     * @author quickli
     * @return
     */
    public static byte[] getUTF8Bytes(String s)
    {
        if(s != null && s.length() >= 0)
        {
            return s.getBytes(StringUtil.UTF_8);
        }
        return null;
    }

    /**
     * 获取字符串的GBK编码字节数组
     * 
     * @param s
     * @author quickli
     * @return
     */
    public static byte[] getGBKBytes(String s)
    {
        if(s != null && s.length() >= 0)
        {
            return s.getBytes(StringUtil.GBK);
        }
        return null;
    }

    /**
     * 获取字节数组的UTF-8编码字符串
     * 
     * @param b
     * @author quickli
     * @return
     */
    public static String getUTF8String(byte[] b)
    {
        if(b != null)
        {
            return new String(b, StringUtil.UTF_8);
        }
        return null;
    }

    /**
     * 获取字节数组的GBK编码字符串
     * 
     * @param b
     * @author quickli
     * @return
     */
    public static String getGBKString(byte[] b)
    {
        if(b != null)
        {
            return new String(b, StringUtil.GBK);
        }
        return null;
    }

    /**
     * 对字符串以 GBK编码方式进行URLEncode
     * 
     * @param s
     * @author quickli
     * @return
     */
    public static String urlEncodeGBK(String s)
    {
        if(s != null && s.length() > 0)
        {
            try
            {
                return URLEncoder.encode(s, StringUtil.ENC_GBK);
            }
            catch (UnsupportedEncodingException e)
            {
            }
        }
        return s;
    }

    /**
     * 对字符串以 UTF-8编码方式进行URLEncode
     * 
     * @param s
     * @author quickli
     * @return
     */
    public static String urlEncodeUTF8(String s)
    {
        if(s != null && s.length() > 0)
        {
            try
            {
                return URLEncoder.encode(s, StringUtil.ENC_UTF8);
            }
            catch (UnsupportedEncodingException e)
            {
            }
        }
        return s;
    }

    /**
     * 对字符串以 GBK编码方式进行URLDecode
     * 
     * @param s
     * @author quickli
     * @return
     */
    public static String urlDecodeGBK(String s)
    {
        if(s != null && s.length() > 0)
        {
            try
            {
                return URLDecoder.decode(s, StringUtil.ENC_GBK);
            }
            catch (UnsupportedEncodingException e)
            {
            }
        }
        return s;
    }

    /**
     * 对字符串以 UTF-8编码方式进行URLDecode
     * 
     * @param s
     * @author quickli
     * @return
     */
    public static String urlDecodeUTF8(String s)
    {
        if(s != null && s.length() > 0)
        {
            try
            {
                return URLDecoder.decode(s, StringUtil.ENC_UTF8);
            }
            catch (UnsupportedEncodingException e)
            {
            }
        }
        return s;
    }

    /**
     * 数字转换成字母
     * 
     * @param i
     * @return
     */
    public static char getChar(int i)
    {
        return (char)(64 + i);
    }

    /**
     * 用字符补左边空位，
     * 
     * @param character
     *        需要进行补位的字符
     * @param size
     *        字符位数
     * @return
     */
    public static String charsFillSeats(Long character, int size)
    {
        StringBuffer sb = new StringBuffer();
        int num = Integer.parseInt(character + "");
        sb.append(num);
        for(int i = String.valueOf(num).length(); i < size; i++)
        {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    /**
     * 两个字符串比较
     * 
     * @param str1
     * @param str2
     * @return 正数 str1>str2 负数str1<str2 相等str1==str2
     */
    public static int compare(String str1, String str2)
    {
        if(StringUtil.isEmpty(str1) && StringUtil.isEmpty(str2))
        {
            return 0;
        }
        else if(StringUtil.isEmpty(str1))
        {
            return -1;
        }
        else if(StringUtil.isEmpty(str2))
        {
            return 1;
        }
        int len1 = str1.length();
        int len2 = str2.length();
        int n = Math.min(len1, len2);
        char v1[] = str1.toCharArray();
        char v2[] = str2.toCharArray();
        int k = 0;
        while (k < n)
        {
            char c1 = v1[k];
            char c2 = v2[k];
            if(c1 != c2)
            {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
    }

    /**
     * 将字符串中的多个连续出现的空白字符删除，只保留一个。如"a   b            c d"会被处理为"a b c d"
     * 
     * @param s
     * @return
     * @throws
     */
    public static String removeRepeatedBlankChar(String s)
    {
        if(s == null || s.length() == 0)
        {
            return s;
        }
        return s.replaceAll("\\s+", " ");
    }

    /**
     * 移除targetStrBuf中字符串右侧subStr字符串，且把右侧空格移除
     * 
     * @param targetStrBuf
     * @param subStr
     * @throws
     */
    public static void rTrim(StringBuffer targetStrBuf, String subStr)
    {
        while (targetStrBuf.lastIndexOf(" ") > -1 && targetStrBuf.lastIndexOf(" ") == targetStrBuf.length() - 1
               || targetStrBuf.lastIndexOf("   ") > -1 && targetStrBuf.lastIndexOf("   ") == targetStrBuf.length() - 1)
        {

            targetStrBuf.delete(targetStrBuf.length() - 1, targetStrBuf.length());
        }

        while (targetStrBuf.lastIndexOf(subStr) > 0
               && targetStrBuf.lastIndexOf(subStr) == targetStrBuf.length() - subStr.length())
        {
            targetStrBuf.delete(targetStrBuf.lastIndexOf(subStr), targetStrBuf.length());
        }
    }

    /**
     * 将一个字符串数组，用特定的连接符拼接起来
     * 
     * @param arr
     *        待拼接的字符串数组
     * @param spliter
     *        连接符
     * @param ignoreBlankStringInArr
     *        是否忽略掉数组中的空字符串（即如果某个元素是个空字符串，则不把它拼接到结果字符串中。空字符串是指：null,"", 或者全部字符均为空白字符的字符串。）
     * @return
     */
    public static String concatStringArray(String[] arr, String spliter, boolean ignoreBlankStringInArr)
    {
        if(arr != null && spliter != null)
        {
            StringBuilder sb = new StringBuilder("");
            boolean flag = false;

            for(int i = 0; i < arr.length; i++)
            {
                if(StringUtil.isNotEmpty(arr[i]) || !ignoreBlankStringInArr)
                {
                    if(flag)
                    {
                        sb.append(spliter);
                    }
                    else
                    {
                        flag = true;
                    }
                    sb.append(arr[i]);
                }
            }

            return sb.toString();
        }
        else
        {
            throw new IllegalArgumentException("Invalid argument.");
        }
    }

    /**
     * 判断字符串是否存在
     * 
     * @param str
     * @return
     */
    public static boolean isExist(String str)
    {
        if(str != null && str.trim().length() > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * 非null字符串去前后空格，把null字符串转换为空。
     * 
     * @param s
     *        字符串
     * @return 去前后空格的字符串
     */
    public static String parseNullStr(String s)
    {
        return s == null ? "" : s.trim();
    }

    /**
     * 使用KPM算法模式匹配字符串，使用方法类似于indexOf
     * 
     * @param text
     *        待匹配字符串
     * @param pattern
     *        匹配模式
     * @return 成功返回模式所在位置
     */
    public static int patternMatch(String text, String pattern)
    {
        if(text != null && pattern != null)
        {
            KMP kmp = new KMP(pattern);
            int pos = kmp.match(text);
            if(pos == text.length())
            {
                pos = -1;
            }
            return pos;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * KMP模式匹配算法实现
     */
    private static class KMP
    {
        private final String pattern;

        private final int[] next;

        // create Knuth-Morris-Pratt NFA from pattern
        public KMP(String pattern)
        {
            this.pattern = pattern;
            int M = pattern.length();
            this.next = new int[M];
            int j = -1;
            for(int i = 0; i < M; i++)
            {
                if(i == 0)
                {
                    this.next[i] = -1;
                }
                else if(pattern.charAt(i) != pattern.charAt(j))
                {
                    this.next[i] = j;
                }
                else
                {
                    this.next[i] = this.next[j];
                }
                while (j >= 0 && pattern.charAt(i) != pattern.charAt(j))
                {
                    j = this.next[j];
                }
                j++;
            }
        }

        // 返回pattern在text中第一次出现的位置，否则返回的值等于text的长度
        // simulate the NFA to find match
        public int match(String text)
        {
            int M = this.pattern.length();
            int N = text.length();
            int i, j;
            for(i = 0, j = 0; i < N && j < M; i++)
            {
                while (j >= 0 && text.charAt(i) != this.pattern.charAt(j))
                {
                    j = this.next[j];
                }
                j++;
            }
            if(j == M)
            {
                return i - M;
            }
            return N;
        }
    }

    /**
     * 判断字符是否为中文字符
     * 
     * @param c
     * @return
     * @date: 2014年1月15日上午11:02:50
     */
    public static boolean isChinese(char c)
    {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
           || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
           || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
           || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
           || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
           || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
        {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为中文
     * 
     * @param str
     * @return
     * @date: 2014年1月15日上午11:02:33
     */
    public static boolean isChinese(String str)
    {
        if(null == str)
        {
            return false;
        }

        char[] ch = str.toCharArray();
        for(int i = 0; i < ch.length; i++)
        {
            char c = ch[i];
            if(StringUtil.isChinese(c) == false)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 过滤文本中的html转义字符
     * 
     * @param content
     * @return
     * @date: 2014年1月15日上午11:02:15
     */
    public static String html(String content)
    {
        if(content == null)
        {
            return "";
        }
        String html = content;
        html = html.replace("&apos;", "'");
        html = html.replace("&amp;", "&");
        html = html.replace("&quot;", "\""); // "
        html = html.replace("&nbsp;", " ");// 替换空格
        html = html.replace("&lt;", "<");
        html = html.replace("&gt;", ">");
        return html;
    }

}