package net.flamgop.lattice.coherent;

import net.flamgop.lattice.MathHelpers;
import net.flamgop.lattice.NoiseSource;

public final class WorleyNoiseSource implements NoiseSource {

    private final long seed;

    /// Worley noise source, also known as "cellular" or "Voronoi" noise.
    /// Worley noise scales with O(2^n) for n dimensions.
    /// @see NoiseSource
    public WorleyNoiseSource(long seed) {
        this.seed = seed;
    }

    @Override
    public double sample(double x) {
        int cell = MathHelpers.fastFloor(x);
        double min_dist_sq = Double.MAX_VALUE;

        for (int i = -1; i <= 1; i++) {
            int targetX = cell + i;
            double pointX = targetX + MathHelpers.murmurHash3Finalizer(targetX, 0, seed);

            double dx = x - pointX;
            double dist_sq = dx * dx;

            min_dist_sq = Math.min(min_dist_sq, dist_sq);
        }

        double dist = Math.sqrt(min_dist_sq);
        return Math.min(dist, 1.0) * 2.0 - 1.0;
    }

    @Override
    public double sample(double x, double y) {
        int cellX = MathHelpers.fastFloor(x);
        int cellY = MathHelpers.fastFloor(y);
        double min_dist_sq = Double.MAX_VALUE;

        for (int ty = cellY - 1; ty <= cellY + 1; ty++) {
            for (int tx = cellX - 1; tx <= cellX + 1; tx++) {
                double pointX = tx + MathHelpers.murmurHash3Finalizer(tx, ty, seed);
                double pointY = ty + MathHelpers.murmurHash3Finalizer(ty, tx, seed);

                double dx = x - pointX;
                double dy = y - pointY;
                double dist_sq = dx * dx + dy * dy;

                min_dist_sq = Math.min(min_dist_sq, dist_sq);
            }
        }

        double dist = Math.sqrt(min_dist_sq);
        return Math.min(dist, 1.0) * 2.0 - 1.0;
    }

    @Override
    public double sample(double x, double y, double z) {
        int cellX = MathHelpers.fastFloor(x);
        int cellY = MathHelpers.fastFloor(y);
        int cellZ = MathHelpers.fastFloor(z);
        double min_dist_sq = Double.MAX_VALUE;

        for (int tz = cellZ - 1; tz <= cellZ + 1; tz++) {
            for (int ty = cellY - 1; ty <= cellY + 1; ty++) {
                for (int tx = cellX - 1; tx <= cellX + 1; tx++) {
                    double pointX = tx + MathHelpers.murmurHash3Finalizer(tx, ty ^ tz, seed);
                    double pointY = ty + MathHelpers.murmurHash3Finalizer(ty, tx ^ tz, seed);
                    double pointZ = tz + MathHelpers.murmurHash3Finalizer(tz, tx ^ ty, seed);

                    double dx = x - pointX;
                    double dy = y - pointY;
                    double dz = z - pointZ;
                    double dist_sq = dx * dx + dy * dy + dz * dz;

                    min_dist_sq = Math.min(min_dist_sq, dist_sq);
                }
            }
        }

        double dist = Math.sqrt(min_dist_sq);
        return Math.min(dist, 1.0) * 2.0 - 1.0;
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        int cellX = MathHelpers.fastFloor(x);
        int cellY = MathHelpers.fastFloor(y);
        int cellZ = MathHelpers.fastFloor(z);
        int cellW = MathHelpers.fastFloor(w);
        double min_dist_sq = Double.MAX_VALUE;

        for (int tw = cellW - 1; tw <= cellW + 1; tw++) {
            for (int tz = cellZ - 1; tz <= cellZ + 1; tz++) {
                for (int ty = cellY - 1; ty <= cellY + 1; ty++) {
                    for (int tx = cellX - 1; tx <= cellX + 1; tx++) {
                        double pointX = tx + MathHelpers.murmurHash3Finalizer(tx, ty ^ tz ^ tw, seed);
                        double pointY = ty + MathHelpers.murmurHash3Finalizer(ty, tx ^ tz ^ tw, seed);
                        double pointZ = tz + MathHelpers.murmurHash3Finalizer(tz, tx ^ ty ^ tw, seed);
                        double pointW = tw + MathHelpers.murmurHash3Finalizer(tw, tx ^ ty ^ tz, seed);

                        double dx = x - pointX;
                        double dy = y - pointY;
                        double dz = z - pointZ;
                        double dw = w - pointW;
                        double dist_sq = dx * dx + dy * dy + dz * dz + dw * dw;

                        min_dist_sq = Math.min(min_dist_sq, dist_sq);
                    }
                }
            }
        }

        double dist = Math.sqrt(min_dist_sq);
        return Math.min(dist, 1.0) * 2.0 - 1.0;
    }
}
