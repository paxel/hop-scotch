package paxel.hopscotch.impl.stage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paxel.hopscotch.api.Creator;
import paxel.hopscotch.api.Stage;
import paxel.hopscotch.impl.data.HopScotchEnrichedData;
import paxel.hopscotch.impl.enrichment.EnrichmentImpl;
import paxel.hopscotch.impl.enrichment.KeyBuilderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;

class DataAggregatorTest {


    @BeforeEach
    void setUp() {

    }

    @Test
    void one() {
        DataAggregator<String> agg = new DataAggregator<>();
        HopScotchEnrichedData<String> a = new HopScotchEnrichedData<>("Pooop");

        UUID uuid = UUID.randomUUID();
        Optional<HopScotchEnrichedData<String>> stringHopScotchEnrichedData = agg.setFragmentCount(uuid, 1);

        assertThat(stringHopScotchEnrichedData).isEmpty();

        Optional<HopScotchEnrichedData<String>> add = agg.add(uuid, a);

        assertThat(add).isNotEmpty();

    }

    @Test
    void two() {
        List<EnrichmentImpl> enrichments = new ArrayList<>();
        KeyBuilderImpl keyBuilder = new KeyBuilderImpl(enrichments::add, new Stage(2, "test"), new Creator("rwe"));
        DataAggregator<String> agg = new DataAggregator<>();
        HopScotchEnrichedData<String> a = new HopScotchEnrichedData<>("Pooop");
        keyBuilder.forString("month").add("may");
        a.add(enrichments.getFirst());
        UUID uuid = UUID.randomUUID();
        Optional<HopScotchEnrichedData<String>> stringHopScotchEnrichedData = agg.setFragmentCount(uuid, 2);

        assertThat(stringHopScotchEnrichedData).isEmpty();

        Optional<HopScotchEnrichedData<String>> addA = agg.add(uuid, a);

        assertThat(addA).isEmpty();

        HopScotchEnrichedData<String> b = new HopScotchEnrichedData<>("Pooop");
        keyBuilder.forString("year").add(1942);
        b.add(enrichments.getLast());

        Optional<HopScotchEnrichedData<String>> addB = agg.add(uuid, b);

        assertThat(addB).isNotEmpty();

        HopScotchEnrichedData<String> result = addB.get();
        // one from a and one from b
        assertThat(result.getEnrichments()).asInstanceOf(LIST).hasSize(2);
    }
}