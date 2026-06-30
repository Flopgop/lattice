import net.flamgop.lattice.NoiseSource;
import net.flamgop.lattice.coherent.GaborNoiseSource;
import net.flamgop.lattice.coherent.PerlinNoiseSource;
import net.flamgop.lattice.coherent.ValueNoiseSource;
import net.flamgop.lattice.coherent.WorleyNoiseSource;
import net.flamgop.lattice.coherent.simplex.OpenSimplex2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoiseConsistencyTest {

    void testNoiseSourceIsDeterministic(NoiseSource source1, NoiseSource source2) {
        assertEquals(source1.getClass(), source2.getClass());
        double x = 12.34;
        double y = -5.67;

        assertEquals(source1.sample(x,y), source2.sample(x,y), 1e-9);
        assertEquals(source1.sample(x,y), source1.sample(x,y));
    }

    @Test
    void testCoherentNoiseIsDeterministic() {
        long seed = 1337;
        testNoiseSourceIsDeterministic(new ValueNoiseSource(seed), new ValueNoiseSource(seed));
        testNoiseSourceIsDeterministic(new PerlinNoiseSource(seed), new PerlinNoiseSource(seed));
        testNoiseSourceIsDeterministic(new OpenSimplex2.StandardLatticeNoiseSource(seed), new OpenSimplex2.StandardLatticeNoiseSource(seed));
        testNoiseSourceIsDeterministic(new GaborNoiseSource(1337, 1.0, 8, 1.0, 1.0, 1.0, 1.0), new GaborNoiseSource(1337, 1.0, 8, 1.0, 1.0, 1.0, 1.0));
        testNoiseSourceIsDeterministic(new WorleyNoiseSource(1337), new WorleyNoiseSource(1337));
    }
}
