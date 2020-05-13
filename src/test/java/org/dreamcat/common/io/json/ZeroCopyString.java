package org.dreamcat.common.io.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by tuke on 2020/5/7
 */
class ZeroCopyString implements CharSequence {
    public static ZeroCopyString EMPTY = new ZeroCopyString("");
    private final char[] value;
    private final int offset;
    private final int length;

    public ZeroCopyString(char[] value, int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count < 0) {
            throw new StringIndexOutOfBoundsException(count);
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > value.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }

        this.value = value;
        this.offset = offset;
        this.length = count;
    }

    public ZeroCopyString(String expression) {
        this.value = expression.toCharArray();
        this.offset = 0;
        this.length = this.value.length;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        if ((index < 0) || (index >= length)) {
            throw new StringIndexOutOfBoundsException(index);
        }

        index += offset;
        return value[index];
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return this.substring(beginIndex, endIndex);
    }

    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public String toString() {
        return new String(value, offset, length);
    }

    public ZeroCopyString substring(int beginIndex) {
        return substring(beginIndex, length);
    }

    public ZeroCopyString substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        int subLen = endIndex - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }

        return ((beginIndex == 0) && (subLen == length)) ? this
                : new ZeroCopyString(value, beginIndex + offset, subLen);
    }

    public ZeroCopyString replace(CharSequence target, CharSequence replacement) {
        String s = Pattern.compile(target.toString(), Pattern.LITERAL).matcher(
                this).replaceAll(Matcher.quoteReplacement(replacement.toString()));
        return new ZeroCopyString(s.toCharArray(), 0, s.length());
    }

    public boolean startsWith(String prefix, int toffset) {
        int to = toffset;
        char[] pa = prefix.toCharArray();
        int po = 0;
        int pc = pa.length;
        // Note: toffset might be near -1>>>1.
        if ((toffset < 0) || (toffset > length - pc)) {
            return false;
        }
        while (--pc >= 0) {
            if (value[offset + to++] != pa[po++]) {
                return false;
            }
        }
        return true;
    }

    public boolean startsWith(String prefix) {
        return startsWith(prefix, 0);
    }

    public boolean endsWith(String suffix) {
        return startsWith(suffix, length - suffix.length());
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    public int indexOf(String str, int fromIndex) {
        char[] target = str.toCharArray();
        return indexOf(value, 0, value.length,
                target, 0, target.length, fromIndex);
    }

    public ZeroCopyString trim() {
        int len = length;
        int st = 0;
        char[] val = value;    /* avoid getfield opcode */

        while ((st < len) && (val[offset + st] <= ' ')) {
            st++;
        }
        while ((st < len) && (val[offset + len - 1] <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < value.length)) ? substring(st, len) : this;
    }


    int indexOf(char[] source, int sourceOffset, int sourceCount,
                char[] target, int targetOffset, int targetCount,
                int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        char first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source[offset + i] != first) {
                while (++i <= max && source[offset + i] != first) ;
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[offset + j]
                        == target[k]; j++, k++)
                    ;

                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

}
