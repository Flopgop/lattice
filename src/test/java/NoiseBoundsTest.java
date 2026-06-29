import net.flamgop.lattice.NoiseSource;
import net.flamgop.lattice.coherent.PerlinNoiseSource;
import net.flamgop.lattice.coherent.ValueNoiseSource;
import net.flamgop.lattice.coherent.simplex.OpenSimplex2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NoiseBoundsTest {
    void testNoiseSourceStaysWithinBounds(NoiseSource source) {
        for (double x = -100; x < 100; x += 0.5) {
            for (double y = -100; y < 100; y += 0.5) {
                double noise = source.sample(x, y);
                assertTrue(noise >= -1 && noise <= 1, String.format("Noise value %f out of bounds at (%f, %f)", noise, x, y));
            }
        }
    }

    @Test
    void testCoherentNoiseStaysWithinBounds() {
        long seed = 1337;
        testNoiseSourceStaysWithinBounds(new ValueNoiseSource(seed));
        testNoiseSourceStaysWithinBounds(new PerlinNoiseSource(seed));
        testNoiseSourceStaysWithinBounds(new OpenSimplex2.StandardLatticeNoiseSource(seed));
    }
}
