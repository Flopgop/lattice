import net.flamgop.lattice.NoiseSource;
import net.flamgop.lattice.coherent.ValueNoiseSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoiseTransformationTest {
    @Test
    void testThenOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);

        NoiseSource transformed = base.then(Math::abs);

        // arbitrary
        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(Math.abs(base.sample(x)), transformed.sample(x));
        assertEquals(Math.abs(base.sample(x,y)), transformed.sample(x,y));
        assertEquals(Math.abs(base.sample(x,y,z)), transformed.sample(x,y,z));
        assertEquals(Math.abs(base.sample(x,y,z,w)), transformed.sample(x,y,z,w));
    }

    @Test
    void testScaleOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        NoiseSource scaled = base.scale(2);

        assertEquals(base.sample(1) * 2, scaled.sample(1));
        assertEquals(base.sample(1, 2) * 2, scaled.sample(1, 2));
        assertEquals(base.sample(1, 2, 3) * 2, scaled.sample(1, 2, 3));
        assertEquals(base.sample(1, 2, 3, 4) * 2, scaled.sample(1, 2, 3, 4));
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    @Test
    void testFrequencyOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        NoiseSource scaled = base.frequency(2, 3, 4, 5);

        assertEquals(base.sample(1 * 2), scaled.sample(1));
        assertEquals(base.sample(1 * 2, 2 * 3), scaled.sample(1, 2));
        assertEquals(base.sample(1 * 2, 2 * 3, 3 * 4), scaled.sample(1, 2, 3));
        assertEquals(base.sample(1 * 2, 2 * 3, 3 * 4, 4 * 5), scaled.sample(1, 2, 3, 4));
    }
}
