package paxel.hopscotch.impl.enrichment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paxel.hopscotch.api.Creator;
import paxel.hopscotch.api.Stage;
import paxel.hopscotch.api.enrichment.Key;
import paxel.hopscotch.api.enrichment.ValueBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class KeyBuilderImplTest {

    public static final Stage STAGE = new Stage(1, "Stage-1");


    public static final Creator CREATOR = new Creator("test");

    private final ArrayList<EnrichmentImpl> values = new ArrayList<>();

    @BeforeEach
    void setUp() {
    }

    @Test
    void string() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        ValueBuilder valueBuilder = classUnderTest.forString("a.b.c.d");
        valueBuilder.add(1);
        assertThat(values).size().isEqualTo(1);
        assertThat(values.getFirst().key().toString()).isEqualTo("a.b.c.d");
    }

    @Test
    void path() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        ValueBuilder valueBuilder = classUnderTest.path("a", "b", "c", "d");
        valueBuilder.add(1);
        assertThat(values).size().isEqualTo(1);
        assertThat(values.getFirst().key().toString()).isEqualTo("a.b.c.d");
    }

    @Test
    void collection() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        ValueBuilder valueBuilder = classUnderTest.collection(List.of("a", "b", "c", "d"));
        valueBuilder.add(1);
        assertThat(values).size().isEqualTo(1);
        assertThat(values.getFirst().key().toString()).isEqualTo("a.b.c.d");
    }

    @Test
    void dottedPath() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        Assertions.assertThatThrownBy(() -> classUnderTest.path("Miami.Beach", "2.7")).hasMessage("Invalid Path string: 'Miami.Beach'");
    }

    @Test
    void dottedCollection() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        Assertions.assertThatThrownBy(() -> classUnderTest.collection(List.of("Miami.Beach", "2.7"))).hasMessage("Path strings cannot contain a dot char: 'Miami.Beach'");
    }

    @Test
    void nullString() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        Assertions.assertThatThrownBy(() -> classUnderTest.forString(null)).hasMessage("Key cannot be null or empty");
    }

    @Test
    void emptyString() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        Assertions.assertThatThrownBy(() -> classUnderTest.forString("")).hasMessage("Key cannot be null or empty");
    }


    @Test
    void nullPath() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        Assertions.assertThatThrownBy(() -> classUnderTest.path(null)).hasMessage("Key cannot be null or empty");
    }

    @Test
    void emptyPath() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        Assertions.assertThatThrownBy(() -> classUnderTest.path("")).hasMessage("Invalid Path string: ''");
    }

    @Test
    void nullCollection() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        Assertions.assertThatThrownBy(() -> classUnderTest.collection(null)).hasMessage("Key cannot be null or empty");
    }

    @Test
    void emptyCollection() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        Assertions.assertThatThrownBy(() -> classUnderTest.collection(List.of())).hasMessage("Key cannot be null or empty");
    }

    @Test
    void wrongKey() {
        KeyBuilderImpl classUnderTest = new KeyBuilderImpl(values::add, STAGE, CREATOR);
        Assertions.assertThatThrownBy(() -> classUnderTest.forKey(new Key() {
        })).hasMessage("Unknown key type class paxel.hopscotch.impl.enrichment.KeyBuilderImplTest$1");
    }
}