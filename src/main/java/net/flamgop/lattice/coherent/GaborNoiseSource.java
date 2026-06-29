package net.flamgop.lattice.coherent;

import net.flamgop.lattice.MathHelpers;
import net.flamgop.lattice.NoiseSource;

public final class GaborNoiseSource implements NoiseSource {

    private final long seed;
    private final double cosOrientation, sinOrientation;
    private final double bandwidth;
    private final int impulsesPerCell;

    /// Gabor noise's dimensional complexity is constant, but it is extremely expensive to compute regardless.
    /// @param seed the noise seed
    /// @param orientation the wave propagation angle in radians
    /// @param bandwidth the radius of the Gaussian envelope (lower values mean wider kernels)
    /// @param impulsesPerCell the density of sparse impulses to convolute per lattice unit
    public GaborNoiseSource(long seed, double orientation, double bandwidth, int impulsesPerCell) {
        this.seed = seed;
        this.cosOrientation = Math.cos(orientation);
        this.sinOrientation = Math.sin(orientation);
        this.bandwidth = bandwidth;
        this.impulsesPerCell = impulsesPerCell;
    }

    @Override
    public double sample(double x) {
        int cellX = MathHelpers.fastFloor(x);
        double totalNoise = 0.0;

        for (int tx = cellX - 1; tx <= cellX + 1; tx++) {
            for (int k = 0; k < impulsesPerCell; k++) {
                long impulseSeed = seed ^ ((long) k * 73856093L);
                double impulseX = tx + MathHelpers.murmurHash3Finalizer(tx, k, impulseSeed);
                double weight = (MathHelpers.murmurHash3Finalizer(tx ^ k, 0, impulseSeed) * 2.0) - 1.0;

                double dx = x - impulseX;
                double distSq = dx * dx;

                double gaussian = Math.exp(-Math.PI * (bandwidth * bandwidth) * distSq);
                double wave = Math.cos(2.0 * Math.PI * dx);

                totalNoise += weight * gaussian * wave;
            }
        }
        return clampAndNormalize(totalNoise);
    }

    @Override
    public double sample(double x, double y) {
        int cellX = MathHelpers.fastFloor(x);
        int cellY = MathHelpers.fastFloor(y);
        double totalNoise = 0.0;

        for (int ty = cellY - 1; ty <= cellY + 1; ty++) {
            for (int tx = cellX - 1; tx <= cellX + 1; tx++) {
                for (int k = 0; k < impulsesPerCell; k++) {
                    long impulseSeed = seed ^ ((long) k * 73856093L);
                    double impulseX = tx + MathHelpers.murmurHash3Finalizer(tx, ty ^ k, impulseSeed);
                    double impulseY = ty + MathHelpers.murmurHash3Finalizer(ty, tx ^ k, impulseSeed);
                    double weight = (MathHelpers.murmurHash3Finalizer(tx ^ k, ty ^ k, impulseSeed) * 2.0) - 1.0;

                    double dx = x - impulseX;
                    double dy = y - impulseY;
                    double distSq = (dx * dx) + (dy * dy);

                    double gaussian = Math.exp(-Math.PI * (bandwidth * bandwidth) * distSq);

                    double projection = (dx * cosOrientation) + (dy * sinOrientation);
                    double wave = Math.cos(2.0 * Math.PI * projection);

                    totalNoise += weight * gaussian * wave;
                }
            }
        }
        return clampAndNormalize(totalNoise);
    }

    @Override
    public double sample(double x, double y, double z) {
        int cellX = MathHelpers.fastFloor(x);
        int cellY = MathHelpers.fastFloor(y);
        int cellZ = MathHelpers.fastFloor(z);
        double totalNoise = 0.0;

        for (int tz = cellZ - 1; tz <= cellZ + 1; tz++) {
            for (int ty = cellY - 1; ty <= cellY + 1; ty++) {
                for (int tx = cellX - 1; tx <= cellX + 1; tx++) {
                    for (int k = 0; k < impulsesPerCell; k++) {
                        long impulseSeed = seed ^ ((long) k * 73856093L);
                        double impulseX = tx + MathHelpers.murmurHash3Finalizer(tx, ty ^ tz ^ k, impulseSeed);
                        double impulseY = ty + MathHelpers.murmurHash3Finalizer(ty, tx ^ tz ^ k, impulseSeed);
                        double impulseZ = tz + MathHelpers.murmurHash3Finalizer(tz, tx ^ ty ^ k, impulseSeed);
                        double weight = (MathHelpers.murmurHash3Finalizer(tx ^ k, ty ^ tz ^ k, impulseSeed) * 2.0) - 1.0;

                        double dx = x - impulseX;
                        double dy = y - impulseY;
                        double dz = z - impulseZ;
                        double distSq = (dx * dx) + (dy * dy) + (dz * dz);

                        double gaussian = Math.exp(-Math.PI * (bandwidth * bandwidth) * distSq);

                        double projection = (dx * cosOrientation) + (dy * sinOrientation) + dz;
                        double wave = Math.cos(2.0 * Math.PI * projection);

                        totalNoise += weight * gaussian * wave;
                    }
                }
            }
        }
        return clampAndNormalize(totalNoise);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        int cellX = MathHelpers.fastFloor(x);
        int cellY = MathHelpers.fastFloor(y);
        int cellZ = MathHelpers.fastFloor(z);
        int cellW = MathHelpers.fastFloor(w);
        double totalNoise = 0.0;

        for (int tw = cellW - 1; tw <= cellW + 1; tw++) {
            for (int tz = cellZ - 1; tz <= cellZ + 1; tz++) {
                for (int ty = cellY - 1; ty <= cellY + 1; ty++) {
                    for (int tx = cellX - 1; tx <= cellX + 1; tx++) {
                        for (int k = 0; k < impulsesPerCell; k++) {
                            long impulseSeed = seed ^ ((long) k * 73856093L);
                            double impulseX = tx + MathHelpers.murmurHash3Finalizer(tx, ty ^ tz ^ tw ^ k, impulseSeed);
                            double impulseY = ty + MathHelpers.murmurHash3Finalizer(ty, tx ^ tz ^ tw ^ k, impulseSeed);
                            double impulseZ = tz + MathHelpers.murmurHash3Finalizer(tz, tx ^ ty ^ tw ^ k, impulseSeed);
                            double impulseW = tw + MathHelpers.murmurHash3Finalizer(tw, tx ^ ty ^ tz ^ k, impulseSeed);
                            double weight = (MathHelpers.murmurHash3Finalizer(tx ^ k, ty ^ tz ^ tw, impulseSeed) * 2.0) - 1.0;

                            double dx = x - impulseX;
                            double dy = y - impulseY;
                            double dz = z - impulseZ;
                            double dw = w - impulseW;
                            double distSq = (dx * dx) + (dy * dy) + (dz * dz) + (dw * dw);

                            double gaussian = Math.exp(-Math.PI * (bandwidth * bandwidth) * distSq);

                            double projection = (dx * cosOrientation) + (dy * sinOrientation) + dz + dw;
                            double wave = Math.cos(2.0 * Math.PI * projection);

                            totalNoise += weight * gaussian * wave;
                        }
                    }
                }
            }
        }
        return clampAndNormalize(totalNoise);
    }

    private double clampAndNormalize(double value) {
        double normScale = 1.0 / Math.sqrt(impulsesPerCell);
        double result = value * normScale;
        return Math.clamp(result, -1.0, 1.0);
    }
}
