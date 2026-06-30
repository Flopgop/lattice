package net.flamgop.lattice.coherent;

import net.flamgop.lattice.MathHelpers;
import net.flamgop.lattice.NoiseSource;

public final class GaborNoiseSource implements NoiseSource {

    private final long seed;
    private final double dir1X;
    private final double dir2X, dir2Y;
    private final double dir3X, dir3Y, dir3Z;
    private final double dir4X, dir4Y, dir4Z, dir4W;
    private final double piBandwidthSqr;
    private final int impulsesPerCell;


    /// Gabor noise's dimensional complexity is constant, but it is extremely expensive to compute regardless.
    /// Note: the direction vector is normalized upon construction to ensure uniform wave frequency across all axes
    /// Note: by "normalized", it is independently normalized into 4 separated vectors, one for each level of dimensionality.
    /// Note: this might be a bit weird, but it's the best solution I could come up with for coherent N-dimensional gabor noise.
    /// @param seed the noise seed
    /// @param bandwidth the radius of the Gaussian envelope (lower values mean wider kernels)
    /// @param impulsesPerCell the density of sparse impulses to convolute per lattice unit
    /// @param dirX the X-component of the wave orientation vector.
    /// @param dirY the Y-component of the wave orientation vector.
    /// @param dirZ the Z-component of the wave orientation vector.
    /// @param dirW the W-component of the wave orientation vector.
    public GaborNoiseSource(long seed, double bandwidth, int impulsesPerCell, double dirX, double dirY, double dirZ, double dirW) {
        this.seed = seed;

        this.dir1X = Math.signum(dirX);

        double len2D = Math.sqrt(dirX * dirX + dirY * dirY);
        this.dir2X = dirX / len2D;
        this.dir2Y = dirY / len2D;
        
        double len3D = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        this.dir3X = dirX / len3D;
        this.dir3Y = dirY / len3D;
        this.dir3Z = dirZ / len3D;
        
        double len4D = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ + dirW * dirW);
        this.dir4X = dirX / len4D;
        this.dir4Y = dirY / len4D;
        this.dir4Z = dirZ / len4D;
        this.dir4W = dirW / len4D;

        this.piBandwidthSqr = -Math.PI * (bandwidth * bandwidth);
        this.impulsesPerCell = impulsesPerCell;
    }

    @Override
    public double sample(double x) {
        int cellX = MathHelpers.fastFloor(x);
        double totalNoise = 0.0;

        for (int k = 0; k < impulsesPerCell; k++) {
            long impulseSeed = seed ^ ((long) k * 73856093L);
            for (int trx = -1; trx <= 1; trx++) {
                int tx = cellX + trx;
                double impulseX = tx + MathHelpers.murmurHash3Finalizer(tx, k, impulseSeed);

                double dx = x - impulseX;;
                totalNoise +=
                        ((MathHelpers.murmurHash3Finalizer(tx ^ k, 0, impulseSeed) * 2.0) - 1.0)
                        * Math.exp(piBandwidthSqr * (dx * dx))
                        * Math.cos(Math.TAU * (dx * dir1X));
            }
        }

        return clampAndNormalize(totalNoise);
    }

    @Override
    public double sample(double x, double y) {
        int cellX = MathHelpers.fastFloor(x);
        int cellY = MathHelpers.fastFloor(y);
        double totalNoise = 0.0;

        for (int k = 0; k < impulsesPerCell; k++) {
            long impulseSeed = seed ^ ((long) k * 73856093L);
            for (int tyr = -1; tyr <= 1; tyr++) {
                int ty = cellY + tyr;
                for (int trx = -1; trx <= 1; trx++) {
                    int tx = cellX + trx;
                    double impulseX = tx + MathHelpers.murmurHash3Finalizer(tx, ty ^ k, impulseSeed);
                    double impulseY = ty + MathHelpers.murmurHash3Finalizer(ty, tx ^ k, impulseSeed);

                    double dx = x - impulseX; double dy = y - impulseY;
                    totalNoise +=
                            ((MathHelpers.murmurHash3Finalizer(tx ^ k, ty, impulseSeed) * 2.0) - 1.0)
                            * Math.exp(piBandwidthSqr * (dx * dx + dy * dy))
                            * Math.cos(Math.TAU * (dx * dir2X + dy * dir2Y));
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

        for (int k = 0; k < impulsesPerCell; k++) {
            long impulseSeed = seed ^ ((long) k * 73856093L);

            for (int trz = -1; trz <= 1; trz++) {
                int tz = cellZ + trz;
                for (int tyr = -1; tyr <= 1; tyr++) {
                    int ty = cellY + tyr;
                    for (int trx = -1; trx <= 1; trx++) {
                        int tx = cellX + trx;
                        double impulseX = tx + MathHelpers.murmurHash3Finalizer(tx, ty ^ tz ^ k, impulseSeed);
                        double impulseY = ty + MathHelpers.murmurHash3Finalizer(ty, tx ^ tz ^ k, impulseSeed);
                        double impulseZ = tz + MathHelpers.murmurHash3Finalizer(tz, tx ^ ty ^ k, impulseSeed);

                        double dx = x - impulseX; double dy = y - impulseY; double dz = z - impulseZ;
                        totalNoise +=
                                ((MathHelpers.murmurHash3Finalizer(tx ^ k, ty ^ tz, impulseSeed) * 2.0) - 1.0)
                                * Math.exp(piBandwidthSqr * (dx * dx + dy * dy + dz * dz))
                                * Math.cos(Math.TAU * (dx * dir3X + dy * dir3Y + dz * dir3Z));
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

        for (int k = 0; k < impulsesPerCell; k++) {
            long impulseSeed = seed ^ ((long) k * 73856093L);
            for (int trw = -1; trw <= 1; trw++) {
                int tw = cellW + trw;
                for (int trz = -1; trz <= 1; trz++) {
                    int tz = cellZ + trz;
                    for (int tyr = -1; tyr <= 1; tyr++) {
                        int ty = cellY + tyr;
                        for (int trx = -1; trx <= 1; trx++) {
                            int tx = cellX + trx;
                            double impulseX = tx + MathHelpers.murmurHash3Finalizer(tx, ty ^ tz ^ tw ^ k, impulseSeed);
                            double impulseY = ty + MathHelpers.murmurHash3Finalizer(ty, tx ^ tz ^ tw ^ k, impulseSeed);
                            double impulseZ = tz + MathHelpers.murmurHash3Finalizer(tz, tx ^ ty ^ tw ^ k, impulseSeed);
                            double impulseW = tw + MathHelpers.murmurHash3Finalizer(tw, tx ^ ty ^ tz ^ k, impulseSeed);

                            double dx = x - impulseX; double dy = y - impulseY; double dz = z - impulseZ; double dw = w - impulseW;
                            totalNoise +=
                                    ((MathHelpers.murmurHash3Finalizer(tx ^ k, ty ^ tz ^ tw, impulseSeed) * 2.0) - 1.0)
                                    * Math.exp(piBandwidthSqr * (dx * dx + dy * dy + dz * dz + dw * dw))
                                    * Math.cos(Math.TAU * (dx * dir4X + dy * dir4Y + dz * dir4Z + dw * dir4W));
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
