package org.dreamcat.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Jerry Will
 * @since 2020-08-26
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public final class CastUtil {

    private CastUtil() {
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * @param source      source object
     * @param targetClass require not null
     * @return null if cannot cast and targetClass is not a primitive/boxed class
     */
    public static Object cast(Object source, Class<?> targetClass) {
        Objects.requireNonNull(targetClass);
        if (source == null) {
            return ReflectUtil.getZeroValue(targetClass);
        }

        Class<?> sourceClass = source.getClass();
        // target is same or a super class of source
        if (targetClass.isAssignableFrom(sourceClass)) {
            return targetClass.cast(source);
        }
        if (targetClass.isPrimitive()) {
            if (targetClass.equals(byte.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).byteValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? 0b1 : 0b0;
                } else {
                    return 0b0;
                }
            } else if (targetClass.equals(short.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).shortValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? (short) 1 : (short) 0;
                } else {
                    return (short) 0;
                }
            } else if (targetClass.equals(char.class)) {
                // Note that avoid unexpected actions
                return (char) 0;
            } else if (targetClass.equals(int.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).intValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? 1 : 0;
                } else {
                    return 0;
                }
            } else if (targetClass.equals(long.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).longValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? 1L : 0L;
                } else {
                    return 0L;
                }
            } else if (targetClass.equals(float.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).floatValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? (float) 1 : (float) 0;
                } else {
                    return (float) 0;
                }
            } else if (targetClass.equals(double.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).doubleValue();
                } else if (sourceClass.equals(Boolean.class)) {
                    return (boolean) source ? (double) 1 : (double) 0;
                } else {
                    return (double) 0;
                }
            } else if (targetClass.equals(boolean.class)) {
                if (Number.class.isAssignableFrom(sourceClass)) {
                    return ((Number) source).longValue() != 0;
                } else {
                    return source.toString().equalsIgnoreCase("true");
                }
            } else {
                throw new RuntimeException("Primitive types are not enumerated completely");
            }
        }
        // boxed class
        if (targetClass.equals(Byte.class)) {
            if (Number.class.isAssignableFrom(sourceClass)) {
                return ((Number) source).byteValue();
            } else if (sourceClass.equals(Boolean.class)) {
                return (boolean) source ? 0b1 : 0b0;
            }
        } else if (targetClass.equals(Short.class)) {
            if (Number.class.isAssignableFrom(sourceClass)) {
                return ((Number) source).shortValue();
            } else if (sourceClass.equals(Boolean.class)) {
                return (boolean) source ? (short) 1 : (short) 0;
            }
        } else if (targetClass.equals(Integer.class)) {
            if (Number.class.isAssignableFrom(sourceClass)) {
                return ((Number) source).intValue();
            } else if (sourceClass.equals(Boolean.class)) {
                return (boolean) source ? 1 : 0;
            }
        } else if (targetClass.equals(Long.class)) {
            if (Number.class.isAssignableFrom(sourceClass)) {
                return ((Number) source).longValue();
            } else if (sourceClass.equals(Boolean.class)) {
                return (boolean) source ? 1L : 0L;
            }
        } else if (targetClass.equals(Float.class)) {
            if (Number.class.isAssignableFrom(sourceClass)) {
                return ((Number) source).floatValue();
            } else if (sourceClass.equals(Boolean.class)) {
                return (boolean) source ? (float) 1 : (float) 0;
            }
        } else if (targetClass.equals(Double.class)) {
            if (Number.class.isAssignableFrom(sourceClass)) {
                return ((Number) source).doubleValue();
            } else if (sourceClass.equals(Boolean.class)) {
                return (boolean) source ? (double) 1 : (double) 0;
            }
        } else if (targetClass.equals(Boolean.class)) {
            if (Number.class.isAssignableFrom(sourceClass)) {
                return ((Number) source).longValue() != 0;
            }
        }
        return null;
    }

    public static Object[] castToArray(Object a) {
        if (a == null) return null;
        Class<?> clazz = a.getClass();
        if (Collection.class.isAssignableFrom(clazz)) {
            return ((Collection<?>) a).toArray();
        }
        if (!clazz.isArray()) {
            throw new ClassCastException(clazz + " is neither a Array or a Collection");
        }
        return castArrayToArray(a, clazz);
    }

    public static <T> List<T> castToList(Object a) {
        if (a == null) return null;
        Class<?> clazz = a.getClass();
        if (Collection.class.isAssignableFrom(clazz)) {
            return (List<T>) new ArrayList<>((Collection<?>) a);
        }
        if (!clazz.isArray()) {
            throw new ClassCastException(clazz + " is neither a Array or a Collection");
        }
        return (List<T>) Arrays.asList(castArrayToArray(a, clazz));
    }

    private static Object[] castArrayToArray(Object a, Class<?> clazz) {
        Class<?> componentType = clazz.getComponentType();
        if (!componentType.isPrimitive()) {
            return (Object[]) a;
        }

        if (clazz == boolean[].class) {
            return ArrayUtil.boxed((boolean[]) a);
        } else if (clazz == byte[].class) {
            return ArrayUtil.boxed((byte[]) a);
        } else if (clazz == short[].class) {
            return ArrayUtil.boxed((short[]) a);
        } else if (clazz == char[].class) {
            return ArrayUtil.boxed((char[]) a);
        } else if (clazz == int[].class) {
            return ArrayUtil.boxed((int[]) a);
        } else if (clazz == long[].class) {
            return ArrayUtil.boxed((long[]) a);
        } else if (clazz == float[].class) {
            return ArrayUtil.boxed((float[]) a);
        } else if (clazz == double[].class) {
            return ArrayUtil.boxed((double[]) a);
        } else {
            throw new IllegalStateException("primitive types are not enumerated completely");
        }
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    /**
     * string to object,
     *
     * @param s           string
     * @param targetClass target class
     * @return object
     * @throws NumberFormatException           not a number
     * @throws StringIndexOutOfBoundsException empty string to char
     * @throws IllegalArgumentException        the enum is not found by name
     */
    public static <T> T parse(String s, Class<T> targetClass)
            throws NumberFormatException, StringIndexOutOfBoundsException {
        if (targetClass == null) return null;
        if (s == null) return (T) ReflectUtil.getZeroValue(targetClass);

        // target is same or a super class of CharSequence
        if (targetClass.isAssignableFrom(String.class)) {
            return (T) s;
        }
        // target is same or a sub class of Enum
        if (Enum.class.isAssignableFrom(targetClass)) {
            return (T) Enum.valueOf((Class<? extends Enum>) targetClass, s);
        }
        if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
            return (T) Byte.valueOf(s);
        } else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
            return (T) Short.valueOf(s);
        } else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
            return (T) Integer.valueOf(s);
        } else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
            return (T) Long.valueOf(s);
        } else if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
            return (T) Float.valueOf(s);
        } else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
            return (T) Double.valueOf(s);
        } else if (targetClass.equals(boolean.class) || targetClass.equals(Boolean.class)) {
            return (T) Boolean.valueOf(s);
        } else if (targetClass.equals(char.class)) {
            // a string cannot be cast to a char normally, so ignore it
            return (T) Character.valueOf((char) 0);
        } else if (targetClass.equals(Date.class)) {
            return (T) DateUtil.parseDate(s);
        } else if (targetClass.equals(LocalDate.class)) {
            return (T) DateUtil.parseLocalDate(s);
        } else if (targetClass.equals(LocalTime.class)) {
            return (T) DateUtil.parseLocalTime(s);
        } else if (targetClass.equals(LocalDateTime.class)) {
            return (T) DateUtil.parseLocalDateTime(s);
        }
        return null;
    }

    public static Object parse(boolean b, Class<?> targetClass) {
        if (targetClass == null) return null;
        if (targetClass.equals(Boolean.class) || targetClass.equals(boolean.class)) {
            return b;
        }
        if (targetClass.equals(String.class)) {
            return Boolean.toString(b);
        }
        if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
            return b ? 0b1 : 0b0;
        } else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
            return b ? (short) 1 : (short) 0;
        } else if (targetClass.equals(char.class) || targetClass.equals(Character.class)) {
            return b ? (char) 1 : (char) 0;
        } else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
            return b ? (int) 1 : (int) 0;
        } else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
            return b ? (long) 1 : (long) 0;
        } else if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
            return b ? (float) 1 : (float) 0;
        } else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
            return b ? (double) 1 : (double) 0;
        } else if (targetClass.equals(BigInteger.class)) {
            return b ? BigInteger.ONE : BigInteger.ZERO;
        } else if (targetClass.equals(BigDecimal.class)) {
            return b ? BigDecimal.ONE : BigDecimal.ZERO;
        }
        return null;
    }

    public static Object parse(long n, Class<?> targetClass) {
        return parse(n, targetClass, ZoneId.systemDefault());
    }

    // helpful for Data-Access-Object
    public static Object parse(long n, Class<?> targetClass, ZoneId zoneId) {
        if (targetClass == null) return null;
        if (targetClass.equals(String.class)) {
            return Long.toString(n);
        } else if (targetClass.equals(Date.class)) {
            return new Date(n);
        } else if (targetClass.equals(LocalDateTime.class)) {
            return DateUtil.ofEpochMilli(n, zoneId);
        } else if (targetClass.equals(LocalDate.class)) {
            return DateUtil.ofEpochMilli(n, zoneId).toLocalDate();
        } else if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
            return (byte) n;
        } else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
            return (short) n;
        } else if (targetClass.equals(char.class) || targetClass.equals(Character.class)) {
            return (char) n;
        } else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
            return (int) n;
        } else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
            return n;
        } else if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
            return (float) n;
        } else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
            return (double) n;
        } else if (targetClass.equals(boolean.class) || targetClass.equals(Boolean.class)) {
            return n != 0;
        } else if (targetClass.equals(BigInteger.class)) {
            return BigInteger.valueOf(n);
        } else if (targetClass.equals(BigDecimal.class)) {
            return BigDecimal.valueOf(n);
        }
        return null;
    }

    public static Object parse(double n, Class<?> targetClass) {
        if (targetClass == null) return null;
        if (targetClass.isAssignableFrom(String.class)) {
            return Double.toString(n);
        } else if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
            return (byte) n;
        } else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
            return (short) n;
        } else if (targetClass.equals(char.class) || targetClass.equals(Character.class)) {
            return (char) n;
        } else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
            return (int) n;
        } else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
            return (long) n;
        } else if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
            return (float) n;
        } else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
            return n;
        } else if (targetClass.equals(boolean.class) || targetClass.equals(Boolean.class)) {
            return n != 0;
        } else if (targetClass.equals(BigInteger.class)) {
            return BigInteger.valueOf((long) n);
        } else if (targetClass.equals(BigDecimal.class)) {
            return BigDecimal.valueOf(n);
        }
        return null;
    }

}
