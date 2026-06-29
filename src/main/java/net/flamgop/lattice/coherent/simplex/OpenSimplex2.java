package net.flamgop.lattice.coherent.simplex;

import net.flamgop.lattice.NoiseSource;
import org.jetbrains.annotations.Contract;

public final class OpenSimplex2 {

    /// This is a "3D first" noise. Its primary usage is for heightmaps (2D), for Y-up noise (3D), or for time-last animated noise.
    public static final class StandardLatticeNoiseSource implements NoiseSource {
        private final long seed;

        /// Simplex noise scales with O(n^2) for n dimensions
        public StandardLatticeNoiseSource(long seed) {
            this.seed = seed;
        }

        @Override
        @Contract(pure = true, value = "_ -> fail")
        public double sample(double x) {
            throw new UnsupportedOperationException("OpenSimplex2 does not support 1 dimensional noise.");
        }

        @Override
        @Contract(pure = true)
        public double sample(double x, double y) {
            return KDotJPGOpenSimplex2.noise2(seed, x, y);
        }

        @Override
        @Contract(pure = true)
        public double sample(double x, double y, double z) {
            return KDotJPGOpenSimplex2.noise3_ImproveXZ(seed, x, y, z);
        }

        @Override
        @Contract(pure = true)
        public double sample(double x, double y, double z, double w) {
            return KDotJPGOpenSimplex2.noise4_ImproveXYZ_ImproveXZ(seed, x, y, z, w);
        }
    }
}
