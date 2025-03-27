package paxel.hopscotch.impl.enrichment;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import paxel.hopscotch.api.Creator;
import paxel.hopscotch.api.Stage;
import paxel.hopscotch.api.enrichment.Enrichment;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UsabilityTest {


    public static final Stage STAGE = new Stage(1, "Stage-1");


    public static final Creator CREATOR = new Creator("test");

    private final ArrayList<EnrichmentImpl> values = new ArrayList<>();

    @BeforeEach
    void setUp() {
    }

    @Test
    void test() {
        KeyQueryBuilderImpl keyQueryBuilder = new KeyQueryBuilderImpl(List.of());
        List<Enrichment> myKey = keyQueryBuilder.matchExact("my Key")
                .queryCreator()
                .matchExact(CREATOR)
                .queryStage()
                .matchExact(STAGE)
                .queryValue()
                .stream().toList();

        assertThat(myKey).asInstanceOf(InstanceOfAssertFactories.LIST).isEmpty();
    }

}
