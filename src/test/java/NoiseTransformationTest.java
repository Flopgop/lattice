import net.flamgop.lattice.MathHelpers;
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

    @Test
    void testSingleFrequencyOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        double factor = 2.5;
        NoiseSource scaled = base.frequency(factor);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(base.sample(x * factor), scaled.sample(x));
        assertEquals(base.sample(x * factor, y * factor), scaled.sample(x, y));
        assertEquals(base.sample(x * factor, y * factor, z * factor), scaled.sample(x, y, z));
        assertEquals(base.sample(x * factor, y * factor, z * factor, w * factor), scaled.sample(x, y, z, w));
    }

    @Test
    void testMapOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        double cMin = -1.0, cMax = 1.0, tMin = 0.0, tMax = 100.0;
        NoiseSource mapped = base.map(cMin, cMax, tMin, tMax);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(MathHelpers.map(base.sample(x), cMin, cMax, tMin, tMax), mapped.sample(x));
        assertEquals(MathHelpers.map(base.sample(x, y), cMin, cMax, tMin, tMax), mapped.sample(x, y));
        assertEquals(MathHelpers.map(base.sample(x, y, z), cMin, cMax, tMin, tMax), mapped.sample(x, y, z));
        assertEquals(MathHelpers.map(base.sample(x, y, z, w), cMin, cMax, tMin, tMax), mapped.sample(x, y, z, w));
    }

    @Test
    void testClampOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        double min = -0.5, max = 0.5;
        NoiseSource clamped = base.clamp(min, max);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(Math.clamp(base.sample(x), min, max), clamped.sample(x));
        assertEquals(Math.clamp(base.sample(x, y), min, max), clamped.sample(x, y));
        assertEquals(Math.clamp(base.sample(x, y, z), min, max), clamped.sample(x, y, z));
        assertEquals(Math.clamp(base.sample(x, y, z, w), min, max), clamped.sample(x, y, z, w));
    }

    @Test
    void testAbsOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        NoiseSource absNoise = base.abs();

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(Math.abs(base.sample(x)), absNoise.sample(x));
        assertEquals(Math.abs(base.sample(x, y)), absNoise.sample(x, y));
        assertEquals(Math.abs(base.sample(x, y, z)), absNoise.sample(x, y, z));
        assertEquals(Math.abs(base.sample(x, y, z, w)), absNoise.sample(x, y, z, w));
    }

    @Test
    void testAddOperatorTransformsCorrectly() {
        NoiseSource n1 = new ValueNoiseSource(1337);
        NoiseSource n2 = new ValueNoiseSource(42);
        NoiseSource added = n1.add(n2);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(n1.sample(x) + n2.sample(x), added.sample(x));
        assertEquals(n1.sample(x, y) + n2.sample(x, y), added.sample(x, y));
        assertEquals(n1.sample(x, y, z) + n2.sample(x, y, z), added.sample(x, y, z));
        assertEquals(n1.sample(x, y, z, w) + n2.sample(x, y, z, w), added.sample(x, y, z, w));
    }

    @Test
    void testMulOperatorTransformsCorrectly() {
        NoiseSource n1 = new ValueNoiseSource(1337);
        NoiseSource n2 = new ValueNoiseSource(42);
        NoiseSource mulled = n1.mul(n2);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(n1.sample(x) * n2.sample(x), mulled.sample(x));
        assertEquals(n1.sample(x, y) * n2.sample(x, y), mulled.sample(x, y));
        assertEquals(n1.sample(x, y, z) * n2.sample(x, y, z), mulled.sample(x, y, z));
        assertEquals(n1.sample(x, y, z, w) * n2.sample(x, y, z, w), mulled.sample(x, y, z, w));
    }

    @Test
    void testTerraceOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        int steps = 5;
        NoiseSource terraced = base.terrace(steps);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(Math.floor(base.sample(x) * steps) / steps, terraced.sample(x));
        assertEquals(Math.floor(base.sample(x, y) * steps) / steps, terraced.sample(x, y));
        assertEquals(Math.floor(base.sample(x, y, z) * steps) / steps, terraced.sample(x, y, z));
        assertEquals(Math.floor(base.sample(x, y, z, w) * steps) / steps, terraced.sample(x, y, z, w));
    }

    @Test
    void testSingleWarpOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        NoiseSource warp = new ValueNoiseSource(42);
        NoiseSource warped = base.warp(warp);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(base.sample(x + warp.sample(x)), warped.sample(x));

        double d2 = warp.sample(x, y);
        assertEquals(base.sample(x + d2, y + d2), warped.sample(x, y));

        double d3 = warp.sample(x, y, z);
        assertEquals(base.sample(x + d3, y + d3, z + d3), warped.sample(x, y, z));

        double d4 = warp.sample(x, y, z, w);
        assertEquals(base.sample(x + d4, y + d4, z + d4, w + d4), warped.sample(x, y, z, w));
    }

    @Test
    void testMultiWarpOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        NoiseSource warpX = new ValueNoiseSource(11);
        NoiseSource warpY = new ValueNoiseSource(22);
        NoiseSource warpZ = new ValueNoiseSource(33);
        NoiseSource warpW = new ValueNoiseSource(44);
        NoiseSource warped = base.warp(warpX, warpY, warpZ, warpW);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(base.sample(x + warpX.sample(x)), warped.sample(x));

        assertEquals(
                base.sample(x + warpX.sample(x, y), y + warpY.sample(x, y)),
                warped.sample(x, y)
        );

        assertEquals(
                base.sample(x + warpX.sample(x, y, z), y + warpY.sample(x, y, z), z + warpZ.sample(x, y, z)),
                warped.sample(x, y, z)
        );

        assertEquals(
                base.sample(x + warpX.sample(x, y, z, w), y + warpY.sample(x, y, z, w), z + warpZ.sample(x, y, z, w), w + warpW.sample(x, y, z, w)),
                warped.sample(x, y, z, w)
        );
    }

    @Test
    void testFbmOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        int octaves = 3;
        double lacunarity = 2.0;
        double persistence = 0.5;
        NoiseSource fbm = base.fBm(octaves, lacunarity, persistence);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        double expected1D = 0.0, amp = 1.0, freq = 1.0, max = 0.0;
        for (int i = 0; i < octaves; i++) {
            expected1D += base.sample(x * freq) * amp; max += amp;
            amp *= persistence; freq *= lacunarity;
        }
        assertEquals(expected1D / max, fbm.sample(x));

        double expected2D = 0.0; amp = 1.0; freq = 1.0; max = 0.0;
        for (int i = 0; i < octaves; i++) {
            expected2D += base.sample(x * freq, y * freq) * amp; max += amp;
            amp *= persistence; freq *= lacunarity;
        }
        assertEquals(expected2D / max, fbm.sample(x, y));

        double expected3D = 0.0; amp = 1.0; freq = 1.0; max = 0.0;
        for (int i = 0; i < octaves; i++) {
            expected3D += base.sample(x * freq, y * freq, z * freq) * amp; max += amp;
            amp *= persistence; freq *= lacunarity;
        }
        assertEquals(expected3D / max, fbm.sample(x, y, z));

        double expected4D = 0.0; amp = 1.0; freq = 1.0; max = 0.0;
        for (int i = 0; i < octaves; i++) {
            expected4D += base.sample(x * freq, y * freq, z * freq, w * freq) * amp; max += amp;
            amp *= persistence; freq *= lacunarity;
        }
        assertEquals(expected4D / max, fbm.sample(x, y, z, w));
    }

    @Test
    void testThresholdOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        double cutoff = 0.2;
        NoiseSource thresholded = base.threshold(cutoff);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(base.sample(x) > cutoff ? 1.0 : -1.0, thresholded.sample(x));
        assertEquals(base.sample(x, y) > cutoff ? 1.0 : -1.0, thresholded.sample(x, y));
        assertEquals(base.sample(x, y, z) > cutoff ? 1.0 : -1.0, thresholded.sample(x, y, z));
        assertEquals(base.sample(x, y, z, w) > cutoff ? 1.0 : -1.0, thresholded.sample(x, y, z, w));
    }

    @Test
    void testPowOperatorTransformsCorrectly() {
        NoiseSource base = new ValueNoiseSource(1337);
        double exponent = 2.0;
        NoiseSource powered = base.pow(exponent);

        double x = 2.5, y = -1.5, z = 0.5, w = 9.1;

        assertEquals(Math.pow(base.sample(x), exponent), powered.sample(x));
        assertEquals(Math.pow(base.sample(x, y), exponent), powered.sample(x, y));
        assertEquals(Math.pow(base.sample(x, y, z), exponent), powered.sample(x, y, z));
        assertEquals(Math.pow(base.sample(x, y, z, w), exponent), powered.sample(x, y, z, w));
    }
}
