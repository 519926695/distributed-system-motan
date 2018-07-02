package io.lzz.common.utils;

/**
 * @author longzanzheng
 * @create 2018-07-02 14:36
 **/


import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Miscellaneous string utility methods. Mainly for internal use within the
 * framework; consider Jakarta's Commons Lang for a more comprehensive suite of
 * string utilities.
 *
 * <p>
 * This class delivers some simple functionality that should really be provided
 * by the core Java String and StringBuilder classes, such as the ability to
 * replace all occurrences of a given substring in a target string. It also
 * provides easy-to-use methods to convert between delimited strings, such as
 * CSV strings, and collections and arrays.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @see org.apache.commons.lang.StringUtils
 * @since 16 April 2001
 */
public abstract class StringUtils {

    public static interface GroupHandler {

        public String handler(String groupStr);
    }

    public static interface GroupsHandler {

        public String handler(String[] groupStrs);
    }

    public static interface Hander {
        public String[] handler(String[] groupStr);
    }

    private static final String FOLDER_SEPARATOR = "/";

    private static final char EXTENSION_SEPARATOR = '.';

    // ---------------------------------------------------------------------
    // General convenience methods for working with Strings
    // ---------------------------------------------------------------------

    /**
     * Check that the given String is neither <code>null</code> nor of length 0.
     * Note: Will return <code>true</code> for a String that purely consists of
     * whitespace.
     * <p>
     *
     * <pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength(&quot;&quot;) = false
     * StringUtils.hasLength(&quot; &quot;) = true
     * StringUtils.hasLength(&quot;Hello&quot;) = true
     * </pre>
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not null and has length
     * @see #hasText(String)
     */
    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check whether the given String has actual text. More specifically,
     * returns <code>true</code> if the string not <code>null<code>,
     * its length is greater than 0, and it contains at least one non-whitespace character.
     * <p><pre>
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not <code>null</code>, its
     * length is greater than 0, and is does not contain whitespace only
     * @see java.lang.Character#isWhitespace
     */
    public static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return !hasText(str);
    }

    public static boolean isNotEmpty(String str) {
        return hasText(str);
    }

    public static boolean isNotBlank(String str) {
        return hasText(str);
    }

    /**
     * 判断字符串是否全部为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        String reg = "\\d+";
        if (str.matches(reg)) {
            return true;
        } else
            return false;
    }

    /**
     * Test if the given String starts with the specified prefix, ignoring
     * upper/lower case.
     *
     * @param str    the String to check
     * @param prefix the prefix to look for
     * @see java.lang.String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {

        return org.apache.commons.lang.StringUtils.startsWithIgnoreCase(str,
                prefix);
    }


    /**
     * @param srcStr
     * @param regexp
     * @param ignoreCase
     * @return
     */

    public static int indexOf(String srcStr, String regexp, boolean ignoreCase) {

        Pattern p = null;
        if (ignoreCase) {
            p = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        } else {
            p = Pattern.compile(regexp);
        }
        Matcher m = p.matcher(srcStr);
        while (m.find()) {
            // System.out.println(m.group()+":"+m.start()+":"+m.end());
            return m.start();
        }
        return -1;
        // sql3.regionMatches(ignoreCase, toffset, other, ooffset, len)
        // System.out.println(m.matches());
    }

    /**
     * 根据正则表达式查找
     *
     * @param srcStr
     * @param regexp
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    public static int lastIndexOf(String srcStr, String regexp,
                                  boolean ignoreCase) {

        Pattern p = null;
        if (ignoreCase) {
            p = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        } else {
            p = Pattern.compile(regexp);
        }
        Matcher m = p.matcher(srcStr);
        int end = -1;
        while (m.find()) {
            // System.out.println(m.group()+":"+m.start()+":"+m.end());
            end = m.start();

        }
        return end;
        // sql3.regionMatches(ignoreCase, toffset, other, ooffset, len)
        // System.out.println(m.matches());
    }

    /**
     * Check whether the given String contains any whitespace characters.
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not empty and contains at
     * least 1 whitespace character
     * @see java.lang.Character#isWhitespace
     */
    public static boolean containsWhitespace(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断s是否为空，如果为空，返回""；如果不为空，返回调用s.trim()后的字符串
     *
     * @param s
     * @return
     */
    public static String getNotNullString(String s) {
        if (!StringUtils.hasText(s)) {
            return "";
        } else {
            return s.trim();
        }
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {

        return org.apache.commons.lang.StringUtils.containsIgnoreCase(str,
                searchStr);

    }

    /**
     * Checks if the String contains any character in the given set of
     * characters.
     * <p>
     * A null String will return false. A null or zero length search array will
     * return false.
     * <p><pre>
     * StringUtils.containsAny(null, *) = false
     * StringUtils.containsAny("", *) =false
     * StringUtils.containsAny(*, null) = false
     * StringUtils.containsAny(*, []) = false
     * StringUtils.containsAny("zzabyycdxx",['z','a']) = true
     * StringUtils.containsAny("zzabyycdxx",['b','y']) = true
     * StringUtils.containsAny("aba", ['z']) = false
     * </pre>
     *
     * @param str
     * @param searchChars
     * @return
     */
    public static boolean containsAny(String str, char[] searchChars) {
        return org.apache.commons.lang.StringUtils
                .containsAny(str, searchChars);

    }

    public static boolean containsAny(String str, String[] searchSubStrs,
                                      boolean ignoreCase) {
        String regexp = StringUtils.arrayToDelimitedString(searchSubStrs, "|");
        int i = StringUtils.indexOf(str, regexp, ignoreCase);
        if (i == -1)
            return false;
        else
            return true;

    }

    // public static String printStringArray(String[] strs) {
    // String s = "";
    // for (int n = 0; n < strs.length; n++) {
    // String b = (String) strs[n];
    // s = s + " " + b;
    // }
    // return s;
    // }

    public static String trim(String str, String defaultValue) {
        return trimWhitespace(str, defaultValue);
    }

    /**
     * <pre>
     * StringUtils.trimToEmpty(null) = "" StringUtils.trimToEmpty("") = ""
     * StringUtils.trimToEmpty("     ") = "" StringUtils.trimToEmpty("abc") =
     * "abc" StringUtils.trimToEmpty("    abc    ") = "abc"
     * </pre>
     */

    public static String trim(String str) {
        return org.apache.commons.lang.StringUtils.trimToEmpty(str);
    }

    /**
     * 如果str为空或者长度为0，返回缺省值
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static String trimWhitespace(String str, String defaultValue) {

        if (!hasLength(str)) {
            return defaultValue;
        }
        StringBuilder buf = new StringBuilder(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        while (buf.length() > 0
                && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * Trim leading and trailing whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuilder buf = new StringBuilder(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        while (buf.length() > 0
                && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * Trim leading whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuilder buf = new StringBuilder(str);
        while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
            buf.deleteCharAt(0);
        }
        return buf.toString();
    }

    public static String trimLeadingString(String str, String trimStr) {

        return trimLeadingStrings(str, new String[]{trimStr});

    }

    public static String trimLeadingStrings(String str, String[] trimStrings) {
        if (str == null)
            return null;
        // String tempStr=str;
        StringBuilder sb = new StringBuilder(str);

        while (sb.length() > 0) {

            boolean flag = true;// 可以退出，false表明不能退出
            for (int i = 0; i < trimStrings.length; i++) {

                int index = trimStrings[i].length();
                String ss = "";
                if (index <= sb.length()) {
                    ss = sb.substring(0, index);
                }

                if (ss.equals(trimStrings[i])) {
                    sb.delete(0, index);
                    flag = false;
                }
            }
            // System.out.println(flag);
            if (flag)
                break;

        }
        return sb.toString();
    }

    public static String trimTailStrings(String str, String[] trimStrings) {
        if (str == null)
            return null;
        // String tempStr=str;
        StringBuilder sb = new StringBuilder(str);

        while (sb.length() > 0) {

            boolean flag = true;// 可以退出，false表明不能退出
            for (int i = 0; i < trimStrings.length; i++) {

                int index = sb.length() - trimStrings[i].length();
                String ss = "";
                if (index >= 0 && index < sb.length()) {
                    ss = sb.substring(index, sb.length());
                }
                if (ss.equals(trimStrings[i])) {

                    sb.delete(index, sb.length());
                    flag = false;
                }
            }
            // System.out.println(flag);
            if (flag)
                break;

        }
        return sb.toString();
    }

    public static String trimTailString(String str, String trimStr) {

        return trimTailStrings(str, new String[]{trimStr});

    }

    public static String trimString(String str, String trimStr) {
        if (!hasLength(str)) {
            return str;
        }

        return trimTailString(trimLeadingString(str, trimStr), trimStr);

    }

    /**
     * Count the occurrences of the substring in string s.
     *
     * @param str string to search in. Return 0 if this is null.
     * @param sub string to search for. Return 0 if this is null.
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (str == null || sub == null || str.length() == 0
                || sub.length() == 0) {
            return 0;
        }
        int count = 0, pos = 0, idx = 0;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * Delete any character in a given string.
     *
     * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's,
     *                      'z's and new lines.
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (inString == null || charsToDelete == null) {
            return inString;
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                out.append(c);
            }
        }
        return out.toString();
    }

    // ---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    // ---------------------------------------------------------------------

    /**
     * Quote the given String with single quotes.
     *
     * @param str the input String (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), or
     * <code>null<code> if the input was <code>null</code>
     */
    public static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * Turn the given Object into a String with single quotes if it is a String;
     * keeping the Object as-is else.
     *
     * @param obj the input Object (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), or the input object as-is
     * if not a String
     */
    public static Object quoteIfString(Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * Capitalize a <code>String</code>, changing the first letter to upper case
     * as per {@link Character#toUpperCase(char)}. No other letters are changed.
     *
     * @param str the String to capitalize, may be <code>null</code>
     * @return the capitalized String, <code>null</code> if null
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    private static String changeFirstCharacterCase(String str,
                                                   boolean capitalize) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuilder buf = new StringBuilder(str.length());
        if (capitalize) {
            buf.append(Character.toUpperCase(str.charAt(0)));
        } else {
            buf.append(Character.toLowerCase(str.charAt(0)));
        }
        buf.append(str.substring(1));
        return buf.toString();
    }

    /**
     * Extract the filename from the given path, e.g. "mypath/myfile.txt" ->
     * "myfile.txt".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the extracted filename, or <code>null</code> if none
     */
    public static String getFilename(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1)
                : path);
    }

    /**
     * Extract the filename extension from the given path, e.g.
     * "mypath/myfile.txt" -> "txt".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the extracted filename extension, or <code>null</code> if none
     */
    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        }
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
    }

    /**
     * Strip the filename extension from the given path, e.g.
     * "mypath/myfile.txt" -> "mypath/myfile".
     *
     * @param path the file path (may be <code>null</code>)
     * @return the path with stripped filename extension, or <code>null</code>
     * if none
     */
    public static String stripFilenameExtension(String path) {
        if (path == null) {
            return null;
        }
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
    }

    /**
     * Apply the given relative path to the given path, assuming standard Java
     * folder separation (i.e. "/" separators);
     *
     * @param path         the path to start from (usually a full file path)
     * @param relativePath the relative path to apply (relative to the full file path
     *                     above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath += FOLDER_SEPARATOR;
            }
            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    /**
     * Parse the given locale string into a <code>java.util.Locale</code>. This
     * is the inverse operation of Locale's <code>toString</code>.
     *
     * @param localeString the locale string, following <code>java.util.Locale</code>'s
     *                     toString format ("en", "en_UK", etc). Also accepts spaces as
     *                     separators, as alternative to underscores.
     * @return a corresponding Locale instance
     */
    public static Locale parseLocaleString(String localeString) {
        String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
        String language = (parts.length > 0 ? parts[0] : "");
        String country = (parts.length > 1 ? parts[1] : "");
        String variant = (parts.length > 2 ? parts[2] : "");
        return (language.length() > 0 ? new Locale(language, country, variant)
                : null);
    }

    // ---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    // ---------------------------------------------------------------------

    /**
     * Append the given String to the given String array, returning a new array
     * consisting of the input array contents plus the given String.
     *
     * @param array the array to append to (can be <code>null</code>)
     * @param str   the String to append
     * @return the new array (never <code>null</code>)
     */
    public static String[] addStringToArray(String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[]{str};
        }
        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * Concatenate the given String arrays into one, with overlapping array
     * elements included twice.
     * <p>
     * The order of elements in the original arrays is preserved.
     *
     * @param array1 the first array (can be <code>null</code>)
     * @param array2 the second array (can be <code>null</code>)
     * @return the new array (<code>null</code> if both given arrays were
     * <code>null</code>)
     */
    public static String[] concatenateStringArrays(String[] array1,
                                                   String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        }
        if (ObjectUtils.isEmpty(array2)) {
            return array1;
        }
        String[] newArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArr, 0, array1.length);
        System.arraycopy(array2, 0, newArr, array1.length, array2.length);
        return newArr;
    }

    /**
     * Merge the given String arrays into one, with overlapping array elements
     * only included once.
     * <p>
     * The order of elements in the original arrays is preserved (with the
     * exception of overlapping elements, which are only included on their first
     * occurence).
     *
     * @param array1 the first array (can be <code>null</code>)
     * @param array2 the second array (can be <code>null</code>)
     * @return the new array (<code>null</code> if both given arrays were
     * <code>null</code>)
     */
    public static String[] mergeStringArrays(String[] array1, String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        }
        if (ObjectUtils.isEmpty(array2)) {
            return array1;
        }
        List result = new ArrayList();
        result.addAll(Arrays.asList(array1));
        for (int i = 0; i < array2.length; i++) {
            String str = array2[i];
            if (!result.contains(str)) {
                result.add(str);
            }
        }
        return toStringArray(result);
    }

    /**
     * Turn given source String array into sorted array.
     *
     * @param array the source array
     * @return the sorted array (never <code>null</code>)
     */
    public static String[] sortStringArray(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[0];
        }
        Arrays.sort(array);
        return array;
    }

    public static String[] toStringArray(List<String> list) {

        return list.toArray(new String[0]);
        // if (list == null) {
        // return null;
        // }
        // String[] strs = new String[list.size()];
        // for (int i = 0; i < list.size(); i++) {
        // strs[i] = (String) list.get(i);
        // }
        // return strs;
    }

    /**
     * Copy the given Collection into a String array. The Collection must
     * contain String elements only.
     *
     * @param collection the Collection to copy
     * @return the String array (<code>null</code> if the passed-in Collection
     * was <code>null</code>)
     */
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return (String[]) collection.toArray(new String[collection.size()]);
    }

    /**
     * Remove duplicate Strings from the given array. Also sorts the array, as
     * it uses a TreeSet.
     *
     * @param array the String array
     * @return an array without duplicates, in natural sort order
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        }
        Set set = new TreeSet();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }
        return toStringArray(set);
    }

    /**
     * Split a String at the first occurrence of the delimiter. Does not include
     * the delimiter in the result.
     *
     * @param toSplit   the string to split
     * @param delimiter to split the string up with
     * @return a two element array with index 0 being before the delimiter, and
     * index 1 being after the delimiter (neither element includes the
     * delimiter); or <code>null</code> if the delimiter wasn't found in
     * the given input String
     */
    public static String[] split(String toSplit, String delimiter) {
        if (!hasLength(toSplit) || !hasLength(delimiter)) {
            return null;
        }
        int offset = toSplit.indexOf(delimiter);
        if (offset < 0) {
            return null;
        }
        String beforeDelimiter = toSplit.substring(0, offset);
        String afterDelimiter = toSplit.substring(offset + delimiter.length());
        return new String[]{beforeDelimiter, afterDelimiter};
    }

    /**
     * Take an array Strings and split each element based on the given
     * delimiter. A <code>Properties</code> instance is then generated, with the
     * left of the delimiter providing the key, and the right of the delimiter
     * providing the value.
     * <p>
     * Will trim both the key and value before adding them to the
     * <code>Properties</code> instance.
     *
     * @param array     the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @return a <code>Properties</code> instance representing the array
     * contents, or <code>null</code> if the array to process was null
     * or empty
     */
    public static Properties splitArrayElementsIntoProperties(String[] array,
                                                              String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    /**
     * Take an array Strings and split each element based on the given
     * delimiter. A <code>Properties</code> instance is then generated, with the
     * left of the delimiter providing the key, and the right of the delimiter
     * providing the value.
     * <p>
     * Will trim both the key and value before adding them to the
     * <code>Properties</code> instance.
     *
     * @param array         the array to process
     * @param delimiter     to split each element using (typically the equals symbol)
     * @param charsToDelete one or more characters to remove from each element prior to
     *                      attempting the split operation (typically the quotation mark
     *                      symbol), or <code>null</code> if no removal should occur
     * @return a <code>Properties</code> instance representing the array
     * contents, or <code>null</code> if the array to process was
     * <code>null</code> or empty
     */
    public static Properties splitArrayElementsIntoProperties(String[] array,
                                                              String delimiter, String charsToDelete) {

        if (ObjectUtils.isEmpty(array)) {
            return null;
        }
        Properties result = new Properties();
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            if (charsToDelete != null) {
                element = deleteAny(array[i], charsToDelete);
            }
            String[] splittedElement = split(element, delimiter);
            if (splittedElement == null) {
                continue;
            }
            result.setProperty(splittedElement[0].trim(), splittedElement[1]
                    .trim());
        }
        return result;
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * Trims tokens and omits empty tokens.
     * <p>
     * The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     *
     * @param str        the String to tokenize
     * @param delimiters the delimiter characters, assembled as String (each of those
     *                   characters is individually considered as delimiter).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see java.lang.String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>
     * The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using <code>delimitedListToStringArray</code>
     *
     * @param str               the String to tokenize
     * @param delimiters        the delimiter characters, assembled as String (each of those
     *                          characters is individually considered as delimiter)
     * @param trimTokens        trim the tokens via String's <code>trim</code>
     * @param ignoreEmptyTokens omit empty tokens from the result array (only applies to
     *                          tokens that are empty after trimming; StringTokenizer will not
     *                          consider subsequent delimiters as token in the first place).
     * @return an array of the tokens (<code>null</code> if the input String was
     * <code>null</code>)
     * @see java.util.StringTokenizer
     * @see java.lang.String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters,
                                                 boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }


    /**
     * Take a String which is a delimited list and convert it to a String array.
     * <p>
     * A single delimiter can consists of more than one character: It will still
     * be considered as single delimiter string, rather than as bunch of
     * potential delimiter characters - in contrast to
     * <code>tokenizeToStringArray</code>.
     *
     * @param str       the input String
     * @param delimiter the delimiter between elements (this is a single delimiter,
     *                  rather than a bunch individual delimiter characters)
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str,
                                                      String delimiter) {
        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[]{str};
        }
        List result = new ArrayList();
        if ("".equals(delimiter)) {
            for (int i = 0; i < str.length(); i++) {
                result.add(str.substring(i, i + 1));
            }
        } else {
            int pos = 0;
            int delPos = 0;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(str.substring(pos, delPos));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(str.substring(pos));
            }
        }
        return toStringArray(result);
    }

    /**
     * Convert a CSV list into an array of Strings.
     *
     * @param str the input String
     * @return an array of Strings, or the empty array in case of empty input
     */
    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * Convenience method to convert a CSV string list to a set. Note that this
     * will suppress duplicates.
     *
     * @param str the input String
     * @return a Set of String entries in the list
     */
    public static Set commaDelimitedListToSet(String str) {
        Set set = new TreeSet();
        String[] tokens = commaDelimitedListToStringArray(str);
        for (int i = 0; i < tokens.length; i++) {
            set.add(tokens[i]);
        }
        return set;
    }


    /**
     * Convenience method to return a String array as a delimited (e.g. CSV)
     * String. E.g. useful for <code>toString()</code> implementations.
     *
     * @param arr   the array to display
     * @param delim the delimiter to use (probably a ",")
     */
    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (ObjectUtils.isEmpty(arr)) {
            return "";
        }
        return ArrayUtils.toString(arr, delim);
    }


    /**
     * 查找src字符串里匹配于regex的模式串，然后对模式串里的子模式串（通过组号来识别）进行处理(handler)，
     * 处理后的结果替换掉原来模式串(regex匹配的字符串)的位置
     *
     * @param src
     * @param regex            匹配的模式子串的正则表达式 如：saa(\\d+)bb
     * @param handleGroupIndex regex里的组号
     * @param hander
     * @return
     */
    public static String replaceAll(String src, String regex,
                                    int handleGroupIndex, GroupHandler hander) {

        if (src == null || src.trim().length() == 0) {
            return "";
        }
        Matcher m = Pattern.compile(regex).matcher(src);

        StringBuffer sbuf = new StringBuffer();

        // perform the replacements:
        while (m.find()) {
            String value = m.group(handleGroupIndex);
            // int l = Integer.valueOf(value, 16).intValue();
            // char c=(char)(0x0ffff&l);
            // System.out.println(m.group(0));
            String handledStr = hander.handler(value);
            m.appendReplacement(sbuf, handledStr);

        }
        // Put in the remainder of the text:
        m.appendTail(sbuf);
        return sbuf.toString();
        // return null;
    }

    public static String searchFirstSubStrByReg(String src, String regex,
                                                int index) {
        Matcher m = Pattern.compile(regex).matcher(src);
        String value = "";
        while (m.find()) {
            value = m.group(index);
            break;
            // System.out.println(value);
        }
        return value;
    }

    /**
     * 根据正则表达式的组号返回查询到的匹配字符串，因为可能返回多个匹配的字符串，所以返回的是数组
     *
     * @param src
     * @param regex
     * @param index 组号
     * @return
     */
    public static String[] searchSubStrByReg(String src, String regex, int index) {
        Matcher m = Pattern.compile(regex).matcher(src);

        List<String> list = new ArrayList<String>();
        while (m.find()) {
            String value = m.group(index);
            list.add(value);

            // System.out.println(value);
        }

        return StringUtils.toStringArray(list);
    }

    public static String[] searchSubStrByReg(String src, String regex,
                                             int index, GroupHandler hander) {
        Matcher m = Pattern.compile(regex).matcher(src);

        List<String> list = new ArrayList<String>();
        while (m.find()) {
            String value = m.group(index);
            value = hander.handler(value);
            list.add(value);

            // System.out.println(value);
        }

        return StringUtils.toStringArray(list);
    }

    /**
     * 根据正则表达式的组号返回查询到的匹配字符串
     *
     * @param src
     * @param regex
     * @param indexs
     * @param filters 如果出现filters里指定的字符串，则过滤
     * @param hander  对提取的字符串数组进行处理
     * @return
     */
    public static String[][] searchSubStrByReg(String src, String regex,
                                               int[] indexs, String[] filters, Hander hander) {
        Matcher m = Pattern.compile(regex).matcher(src);

        ArrayList<String[]> result = new ArrayList<String[]>();
        boolean flag = false;
        while (m.find()) {
            flag = false;
            String[] strs = new String[indexs.length];
            for (int i = 0; i < indexs.length; i++) {
                String value = m.group(indexs[i]);
                String filter = filters[i];
                if (StringUtils.hasText(filter) && value.contains(filter)) {
                    flag = true;
                    break;
                }
                strs[i] = value;

            }
            if (!flag) {
                strs = hander.handler(strs);
                result.add(strs);
            }
            // System.out.println("==="+ArrayUtils.toString(results[i]));

            // System.out.println(value);
        }

        return (String[][]) result.toArray(new String[0][0]);
    }

    /**
     * 根据正则表达式的组号返回查询到的匹配字符串
     *
     * @param src
     * @param regex
     * @param indexs 存放多个组号
     * @return
     */
    public static String[][] searchSubStrByReg(String src, String regex,
                                               int[] indexs) {
        Matcher m = Pattern.compile(regex).matcher(src);

        ArrayList<String[]> result = new ArrayList<String[]>();
        while (m.find()) {
            String[] strs = new String[indexs.length];
            for (int i = 0; i < indexs.length; i++) {
                String value = m.group(indexs[i]);
                strs[i] = value;

            }
            result.add(strs);
            // System.out.println("==="+ArrayUtils.toString(results[i]));

            // System.out.println(value);
        }

        return (String[][]) result.toArray(new String[0][0]);
    }

    /**
     * 查找src字符串里匹配于regex的模式串，然后对模式串里的子模式串（通过组号来识别）进行处理(handler)，
     * 处理后的结果替换掉原来模式串（以regex识别）的位置
     *
     * @param src
     * @param regex            匹配的模式子串的正则表达式 如：saa(\\d+)bb
     * @param handleGroupIndex regex里的组号
     * @param hander           处理的回调函数
     * @return
     */
    public static String replaceAll(String src, String regex,
                                    int[] handleGroupIndex, GroupsHandler hander) {

        if (src == null || src.trim().length() == 0) {
            return "";
        }
        Matcher m = Pattern.compile(regex).matcher(src);

        StringBuffer sbuf = new StringBuffer();

        String[] groupStrs = new String[handleGroupIndex.length];
        // perform the replacements:
        while (m.find()) {
            for (int i = 0; i < handleGroupIndex.length; i++) {
                String value = m.group(handleGroupIndex[i]);
                // int l = Integer.valueOf(value, 16).intValue();
                // char c=(char)(0x0ffff&l);
                // System.out.println(m.group(0));
                groupStrs[i] = value;
                // m.appendReplacement(sbuf, handledStr);
            }
            String handledStr = hander.handler(groupStrs);
            m.appendReplacement(sbuf, handledStr);
        }
        // Put in the remainder of the text:
        m.appendTail(sbuf);
        return sbuf.toString();

        // return null;
    }

    /**
     * @param src    源串
     * @param c      添加的字符
     * @param num    添加c的次数
     * @param ishead 如果为true，在头部添加，否则在尾部添加
     * @return
     */
    public static String fill(String src, char c, int num, boolean ishead) {

        StringBuilder sb = new StringBuilder(src);
        char[] cs = new char[num];
        Arrays.fill(cs, c);
        if (ishead)
            sb.insert(0, cs);
        else {
            sb.append(cs);
        }
        return sb.toString();
    }

    /**
     * Compares two Strings, and returns the portion where they differ. (More
     * precisely, return the remainder of the second String, starting from where
     * it's different from the first.) or example, difference("i am a machine",
     * "i am a robot") -> "robot".
     * <p><blockquote><pre>
     * StringUtils.difference(null, null) = null
     * StringUtils.difference("", "") = "" StringUtils.difference("", "abc") = "abc"
     * StringUtils.difference("abc", "") = ""
     * StringUtils.difference("abc", "abc") = ""
     * StringUtils.difference("ab", "abxyz") = "xyz"
     * StringUtils.difference("abcde", "abxyz") = "xyz"
     * StringUtils.difference("abcde", "xyz") = "xyz"
     * </pre></blockquote>
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String difference(String str1, String str2) {
        return org.apache.commons.lang.StringUtils.difference(str1, str2);
    }

    public static String repeat(String str, int repeat) {
        return org.apache.commons.lang.StringUtils.repeat(str, repeat);
    }

    /**
     * 向src的头部或尾部重复添加c字符，添加到finxLen固定长度
     *
     * @param src
     * @param c
     * @param fixLen
     * @param isHead
     * @return
     */
    public static String paddingToFixedString(String src, char c, int fixLen,
                                              boolean isHead) {
        String s = src;
        if (s.length() > fixLen) {
            int begin = s.length() - fixLen;
            s = s.substring(begin);
            return s;
        } else if (s.length() == fixLen) {
            return src;
        } else {
            return fill(src, c, fixLen - src.length(), isHead);
        }
    }

    /**
     * 查找src字符串里匹配于regex的模式串，然后对模式串里的子模式串（通过组号来识别） 进行处理(handler)，
     * 处理后的结果替换模式串里的相应的子串
     *
     * @param src
     * @param regex            匹配的正则表达式，如:&(\\d+;)([a-z)+)
     * @param handleGroupIndex 要处理的组号
     * @param hander           处理回调函数
     * @param reservesGroups   保留的字模式串的组号,如果为空，则表明只用hander返回的字符串替换handleGroupIndex指定的组；
     *                         如果不为空，则表明用reservesGroups组里组号指定的字符串和hander返回的字符串替换regex匹配 的字符串
     * @return
     */
    public static String replaceAll(String src, String regex,
                                    int handleGroupIndex, GroupHandler hander, int[] reservesGroups) {

        if (src == null || src.trim().length() == 0) {
            return "";
        }
        Matcher m = Pattern.compile(regex).matcher(src);

        StringBuffer sbuf = new StringBuffer();
        String replacementFirst = "";
        String replacementTail = "";
        if (reservesGroups != null && reservesGroups.length > 0) {
            Arrays.sort(reservesGroups);
            for (int i = 0; i < reservesGroups.length; i++) {
                if (reservesGroups[i] < handleGroupIndex) {
                    replacementFirst = replacementFirst + "$"
                            + reservesGroups[i];
                } else {
                    replacementTail = replacementTail + "$" + reservesGroups[i];
                }
            }
        }

        // perform the replacements:
        while (m.find()) {
            String value = m.group(handleGroupIndex);

            String group = m.group();

            String handledStr = hander.handler(value);
            String replacement = "";
            if (reservesGroups == null) {
                int start0 = m.start();
                int end0 = m.end();
                int start = m.start(handleGroupIndex);
                int end = m.end(handleGroupIndex);
                int relativeStart = start - start0;
                int relativeEnd = end - start0;
                StringBuilder sbgroup = new StringBuilder(group);
                sbgroup = sbgroup.replace(relativeStart, relativeEnd,
                        handledStr);
                replacement = sbgroup.toString();
            } else {
                replacement = replacementFirst + handledStr + replacementTail;
            }

            m.appendReplacement(sbuf, replacement);

        }
        // Put in the remainder of the text:
        m.appendTail(sbuf);
        return sbuf.toString();
        // return null;
    }

    /**
     * Convenience method to return a String array as a CSV String. E.g. useful
     * for <code>toString()</code> implementations.
     *
     * @param arr the array to display
     */
    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

    public static String arrayToCommaDelimitedString(int[] arr) {
        String result = "";
        for (int i = 0; i < arr.length; i++) {
            if (i < arr.length - 1)
                result = result + arr[i] + ",";
            else
                result = result + arr[i];
        }
        return result;
    }

    /**
     * Abbreviates a String using ellipses. This will turn "Now is the time for
     * all good men" into "Now is the time for..."
     * <p><blockquote><pre>
     * Specifically:
     *
     * •If str is less than maxWidth characters long, return it.
     * •Else abbreviate it to (substring(str, 0, max-3) + "...").
     * •If maxWidth is less than 4, throw an IllegalArgumentException.
     * •In no case will it return a String of length greater than maxWidth.
     *
     * StringUtils.abbreviate(null, *) = null StringUtils.abbreviate("", 4) = ""
     * StringUtils.abbreviate("abcdefg", 6) = "abc..."
     * StringUtils.abbreviate("abcdefg", 7) = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 8) = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 4) = "a..."
     * StringUtils.abbreviate("abcdefg", 3) = IllegalArgumentException
     * </pre></blockquote></p>
     *
     * @param str
     * @param maxWidth
     * @return
     */
    public static java.lang.String abbreviate(java.lang.String str, int maxWidth) {
        return org.apache.commons.lang.StringUtils.abbreviate(str, maxWidth);
    }

    public static String[] mysplit(String s, String split) {
        try {
            String[] ss = s.split(split);
            String[] temp = new String[ss.length];
            for (int i = 0; i < ss.length; i++) {
                temp[i] = new String(ss[i]);
            }
            return temp;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public static String mysubString(String s, int begin) {
        try {
            return mysubString(s, begin, s.length());
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public static String mysubString(String s, int begin, int end) {
        try {
            String ss = s.substring(begin, end);
            return new String(ss);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public static void main(String[] args) {

        // GroupHandler group = new GroupHandler() {
        // public String handler(String groupStr) {
        // return "";
        // }
        // };
        // String s = "ssss&24;&45;dddd&67;";
        // int[] is=new int[]{5,3,1};
        // Arrays.sort(is);
        // //System.out.println(StringUtils.arrayToCommaDelimitedString(is));
        // System.out.println(StringUtils.replaceAllOnlyGroup(s, "ss(&)(\\d+;)",
        // 1, group,new int[]{2}));
        // String src="12344ab";
        // System.out.println(StringUtils.startsWith(src, "2",true));
        // // int[] is=new int[]{1,2,3};
        // //
        // // System.out.println(arrayToCommaDelimitedString(is));
        // System.out.println(StringUtils.printStringArray(StringUtils.searchSubStrByReg("sunchaojichaon",
        // "chao", 0)));
        // System.out.println(StringUtils.fill("jdddd",'0',4,false));
        // System.out.println(paddingAndFixString("1234",'0',100,false));
        // Object[] s2=new String[]{"111","333","444"};
        // String[] s3=(String[])s2;

        // Map map = urlQueryStrToMap("name=12&name=23&name=44&code=2");
        // System.out.println(ArrayUtils.toString(map.get("name")) + "-- "
        // + ArrayUtils.toString(map.get("code")));
        // List list = new ArrayList();
        // list.add("111");
        // list.add(22);
        // System.out.println(CollectionUtils.toString(list));
        // List<String> list2 = new ArrayList<String>();
        // // list2.add("123");
        // // list2.add("34a");
        // System.out.println(collectionToCommaDelimitedString(list2));

        // String sss = " l<y>e/>j";
        // System.out.println(StringUtils.trimLeadingStrings(sss, new String[] {
        // "<br/>","<br/>"," " }));
        // System.out.println(StringUtils.trimTailStrings(sss, new String[] {
        // "<br/>","<br/>" }));
        // // System.out.println("yy=".split("=").length);
        // System.out.println(StringUtils.containsAny(sss, new
        // String[]{"br","er","jk"}, true));
//		String s = "SELECT  top 100 percent * from test  order by id";
//		String reg = "order\\s+by\\s+\\w+(\\s+(asc|desc))?";
//		TInteger end = new TInteger();
//		boolean b = StringUtils.endsWith(s, reg, true, end);
//
//		boolean c = StringUtils.startsWith(s, "select", true, end);
//		int i = 0;
//		i = 3;
//		System.out.println(end);
//
//		System.out.println("start="+StringUtils.indexOf(s, "top", true,end));
//		System.out.println("end="+end);
        String ss = ";;fyu;fsadfuf;tuyuu;;;";
        String[] s = split(ss, ";");
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }
        System.out.println(mysubString(ss, 2));
    }

}

