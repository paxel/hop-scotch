package paxel.hopscotch.impl.data;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

/**
 * A typesafe definition of user data.
 */
public sealed interface Value permits Value.ShortValue, Value.IntegerValue, Value.LongValue, Value.FloatValue, Value.DoubleValue, Value.StringValue,
        Value.BooleanValue, Value.BigIntegerValue, Value.BigDecimalValue, Value.ObjectValue, Value.CollectionValue, Value.MapValue {

    /**
     * The enum describing all supported types
     */
    enum Type {
        /**
         * For Short and short
         */
        SHORT,
        /**
         * For int and Integer
         */
        INTEGER,
        /**
         * For long and Long
         */
        LONG,
        /**
         * for float and Float
         */
        FLOAT,
        /**
         * For double and Double
         */
        DOUBLE,
        /**
         * For String
         */
        STRING,
        /**
         * For boolean and Boolean
         */
        BOOLEAN,
        /**
         * For BigInteger
         */
        BIG_INTEGER,
        /**
         * For BigDecimal
         */
        BIG_DECIMAL,
        /**
         * For single objects
         */
        OBJECT,
        /**
         * For Lists, Sets and Collections. Arrays are not directly supported. Use Object
         */
        COLLECTION,
        /**
         * For Maps
         */
        MAP
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record FloatValue(float value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record DoubleValue(double value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record ShortValue(short value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record IntegerValue(int value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record LongValue(long value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record StringValue(String value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record BooleanValue(boolean value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record BigDecimalValue(BigDecimal value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record BigIntegerValue(BigInteger value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record ObjectValue<V>(V value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record CollectionValue<V>(Collection<V> value) implements Value {
    }

    /**
     * Constructs a typesafe instance
     *
     * @param value The user generated value
     */
    record MapValue<K, V>(Map<K, V> value) implements Value {
    }
}
