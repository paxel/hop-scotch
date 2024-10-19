package paxel.hopscotch.impl.data;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

public sealed interface Value permits Value.ShortValue, Value.IntegerValue, Value.LongValue, Value.FloatValue, Value.DoubleValue, Value.StringValue,
        Value.BooleanValue, Value.BigIntegerValue, Value.BigDecimalValue, Value.ObjectValue, Value.CollectionValue, Value.MapValue {

    enum Type {
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        STRING,
        BOOLEAN,
        BIG_INTEGER,
        BIG_DECIMAL,
        OBJECT,
        COLLECTION,
        MAP;
    }

    record FloatValue(float value) implements Value {
    }

    record DoubleValue(double value) implements Value {
    }

    record ShortValue(short value) implements Value {
    }

    record IntegerValue(int value) implements Value {
    }

    record LongValue(long value) implements Value {
    }

    record StringValue(String value) implements Value {
    }

    record BooleanValue(boolean value) implements Value {
    }

    record BigDecimalValue(BigDecimal value) implements Value {
    }

    record BigIntegerValue(BigInteger value) implements Value {
    }

    record ObjectValue<V>(V value) implements Value {
    }

    record CollectionValue<V>(Collection<V> value) implements Value {
    }

    record MapValue<K, V>(Map<K, V> value) implements Value {
    }
}
