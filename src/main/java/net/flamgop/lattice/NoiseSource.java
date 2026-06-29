package net.flamgop.lattice;

import java.util.function.DoubleUnaryOperator;

/// Provides noise with double precision up to 4 dimensions
/// @apiNote A NoiseSource that provides a pseudorandom noise value should always output the noise value in the range of -1 to 1
public interface NoiseSource {
    double sample(double x);
    double sample(double x, double y);
    double sample(double x, double y, double z);
    double sample(double x, double y, double z, double w);

    /// Operate on the output of this noise source
    /// @param operator a {@link DoubleUnaryOperator} which transforms the output of the sample functions.
    /// @return a new {@link NoiseSource} yielding the transformed output.
    default NoiseSource then(DoubleUnaryOperator operator) {
        return new NoiseSource() {
            @Override public double sample(double x) { return operator.applyAsDouble(NoiseSource.this.sample(x)); }
            @Override public double sample(double x, double y) { return operator.applyAsDouble(NoiseSource.this.sample(x, y)); }
            @Override public double sample(double x, double y, double z) { return operator.applyAsDouble(NoiseSource.this.sample(x, y, z)); }
            @Override public double sample(double x, double y, double z, double w) { return operator.applyAsDouble(NoiseSource.this.sample(x, y, z, w)); }
        };
    }

    /// Scale the input coordinates of this noise source by some constant factor
    /// @param factor the aforementioned constant factor
    /// @return a new {@link NoiseSource} fed with the transformed coordinates.
    default NoiseSource frequency(double factor) {
        return frequency(factor, factor, factor, factor);
    }

    /// Scale the input coordinates of this noise source by some constant factor
    /// @param sx the scaling factor in the x dimension
    /// @param sy the scaling factor in the y dimension
    /// @param sz the scaling factor in the z dimension
    /// @param sw the scaling factor in the w dimension
    /// @return a new {@link NoiseSource} fed with the transformed coordinates.
    default NoiseSource frequency(double sx, double sy, double sz, double sw) {
        return new NoiseSource() {
            @Override public double sample(double x) { return NoiseSource.this.sample(x * sx); }
            @Override public double sample(double x, double y) { return NoiseSource.this.sample(x * sx, y * sy); }
            @Override public double sample(double x, double y, double z) { return NoiseSource.this.sample(x * sx, y * sy, z * sz); }
            @Override public double sample(double x, double y, double z, double w) { return NoiseSource.this.sample(x * sx, y * sy, z * sz, w * sw); }
        };
    }

    /// Scale the output of this noise source by some constant factor
    /// @param scale the scaling factor
    /// @return a new {@link NoiseSource} scaled by the factor.
    default NoiseSource scale(double scale) {
        return this.then(v -> v * scale);
    }

    /// Maps the output of the noise source from the specified current range into the given target range
    /// @param currentMin the minimum value of the current range
    /// @param currentMax the maximum value of the current range
    /// @param targetMin the minimum value of the target range
    /// @param targetMax the maximum value of the target range
    /// @return a new {@link NoiseSource} producing an output mapped into the target range.
    default NoiseSource map(double currentMin, double currentMax, double targetMin, double targetMax) {
        return this.then(v -> MathHelpers.map(v, currentMin, currentMax, targetMin, targetMax));
    }

    /// Clamps the output of the noise source into the given range
    /// @param min the minimum value of the clamp
    /// @param max the maximum value of the clamp
    /// @return a new {@link NoiseSource} producing an output in the clamped range.
    default NoiseSource clamp(double min, double max) {
        return this.then(v -> Math.clamp(v, min, max));
    }

    /// Applies the absolute operator to the noise source
    /// @return a new {@link NoiseSource} providing an always positive output (as produced by {@link Math#abs(double)}).
    default NoiseSource abs() {
        return this.then(Math::abs);
    }

    /// Adds this NoiseSource to another NoiseSource
    /// Note: the output is not normalized. The sum of two NoiseSources is not necessarily in the range of -1 to 1. (in most cases, it is in the range of -2 to 2)
    /// @return a new {@link NoiseSource} which is the sum of this and the other NoiseSource.
    default NoiseSource add(NoiseSource other) {
        return new NoiseSource() {
            @Override public double sample(double x) { return NoiseSource.this.sample(x) + other.sample(x); }
            @Override public double sample(double x, double y) { return NoiseSource.this.sample(x, y) + other.sample(x, y); }
            @Override public double sample(double x, double y, double z) { return NoiseSource.this.sample(x, y, z) + other.sample(x, y, z); }
            @Override public double sample(double x, double y, double z, double w) { return NoiseSource.this.sample(x, y, z, w) + other.sample(x, y, z, w); }
        };
    }

    /// Multiplies this NoiseSource with another NoiseSource
    /// @return a new {@link NoiseSource} which is the sum of this and the other NoiseSource.
    default NoiseSource mul(NoiseSource other) {
        return new NoiseSource() {
            @Override public double sample(double x) { return NoiseSource.this.sample(x) * other.sample(x); }
            @Override public double sample(double x, double y) { return NoiseSource.this.sample(x, y) * other.sample(x, y); }
            @Override public double sample(double x, double y, double z) { return NoiseSource.this.sample(x, y, z) * other.sample(x, y, z); }
            @Override public double sample(double x, double y, double z, double w) { return NoiseSource.this.sample(x, y, z, w) * other.sample(x, y, z, w); }
        };
    }

    /// Adds terraces to the noise source, this may also be called "posterization"
    /// This requires the noise source to first be normalized to the range of 0 - 1
    /// @param steps the number of times to terrace the noise source
    /// @return a new {@link NoiseSource} which is the terraced version of this NoiseSource.
    default NoiseSource terrace(int steps) {
        return this.then(val -> Math.floor(val * steps) / steps);
    }

    /// Warps the domain of this noise source to a new coordinate system
    /// Note: if you want the warp to be applied at lower intensity, use {@link #scale(double)} on the warp.
    /// @param warp the NoiseSource to warp this NoiseSource's domain by
    /// @return a new {@link NoiseSource} which is the warped version of this NoiseSource.
    default NoiseSource warp(NoiseSource warp) {
        return new NoiseSource() {
            @Override
            public double sample(double x) {
                double d = warp.sample(x);
                return NoiseSource.this.sample(x + d);
            }

            @Override
            public double sample(double x, double y) {
                double d = warp.sample(x, y);
                return NoiseSource.this.sample(x + d, y + d);
            }

            @Override
            public double sample(double x, double y, double z) {
                double d = warp.sample(x, y, z);
                return NoiseSource.this.sample(x + d, y + d, z + d);
            }

            @Override
            public double sample(double x, double y, double z, double w) {
                double d = warp.sample(x, y, z, w);
                return NoiseSource.this.sample(x + d, y + d, z + d, w + d);
            }
        };
    }

    /// Independent warps for each domain
    default NoiseSource warp(NoiseSource warpX, NoiseSource warpY, NoiseSource warpZ, NoiseSource warpW) {
        return new NoiseSource() {
            @Override
            public double sample(double x) {
                double d = warpX.sample(x);
                return NoiseSource.this.sample(x + d);
            }

            @Override
            public double sample(double x, double y) {
                double dx = warpX.sample(x, y);
                double dy = warpY.sample(x, y);
                return NoiseSource.this.sample(x + dx, y + dy);
            }

            @Override
            public double sample(double x, double y, double z) {
                double dx = warpX.sample(x, y, z);
                double dy = warpY.sample(x, y, z);
                double dz = warpZ.sample(x, y, z);
                return NoiseSource.this.sample(x + dx, y + dy, z + dz);
            }

            @Override
            public double sample(double x, double y, double z, double w) {
                double dx = warpX.sample(x, y, z, w);
                double dy = warpY.sample(x, y, z, w);
                double dz = warpZ.sample(x, y, z, w);
                double dw = warpW.sample(x, y, z, w);
                return NoiseSource.this.sample(x + dx, y + dy, z + dz, w + dw);
            }
        };
    }

    /// Synthesizes Fractal Brownian Motion (fBm) by layering multiple octaves of this noise source.
    /// Each successive layer increases in frequency and decreases in amplitude
    /// Note: this breaks down if the initial noise range is not within -1 and 1
    ///
    /// @param octaves the number of noise layers to stack
    /// @param lacunarity the frequency scalar per octave (typically 2.0)
    /// @param persistence the amplitude scalar per octave (typically 0.5) (sometimes called gain)
    /// @return a new {@link NoiseSource} representing the multi-octave fBm fractal
    default NoiseSource fBm(int octaves, double lacunarity, double persistence) {
        return new NoiseSource() {
            @Override
            public double sample(double x) {
                double total = 0.0;
                double amplitude = 1.0;
                double frequency = 1.0;
                double maxValue = 0.0;

                for (int i = 0; i < octaves; i++) {
                    total += NoiseSource.this.sample(x * frequency) * amplitude;
                    maxValue += amplitude;
                    amplitude *= persistence;
                    frequency *= lacunarity;
                }
                return total / maxValue;
            }

            @Override
            public double sample(double x, double y) {
                double total = 0.0;
                double amplitude = 1.0;
                double frequency = 1.0;
                double maxValue = 0.0;

                for (int i = 0; i < octaves; i++) {
                    total += NoiseSource.this.sample(x * frequency, y * frequency) * amplitude;
                    maxValue += amplitude;
                    amplitude *= persistence;
                    frequency *= lacunarity;
                }
                return total / maxValue;
            }

            @Override
            public double sample(double x, double y, double z) {
                double total = 0.0;
                double amplitude = 1.0;
                double frequency = 1.0;
                double maxValue = 0.0;

                for (int i = 0; i < octaves; i++) {
                    total += NoiseSource.this.sample(x * frequency, y * frequency, z * frequency) * amplitude;
                    maxValue += amplitude;
                    amplitude *= persistence;
                    frequency *= lacunarity;
                }
                return total / maxValue;
            }

            @Override
            public double sample(double x, double y, double z, double w) {
                double total = 0.0;
                double amplitude = 1.0;
                double frequency = 1.0;
                double maxValue = 0.0;

                for (int i = 0; i < octaves; i++) {
                    total += NoiseSource.this.sample(x * frequency, y * frequency, z * frequency, w * frequency) * amplitude;
                    maxValue += amplitude;
                    amplitude *= persistence;
                    frequency *= lacunarity;
                }
                return total / maxValue;
            }
        };
    }

    default NoiseSource threshold(double cutoff) {
        return this.then(v -> v > cutoff ? 1.0 : -1.0);
    }

    default NoiseSource pow(double exponent) {
        return this.then(v -> Math.pow(v, exponent));
    }
}
