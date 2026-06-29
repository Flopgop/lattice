package net.flamgop.lattice;

public final class ConstantNoiseSource implements NoiseSource {

    private final double value;

    /// A noise source that always returns a constant value.
    public ConstantNoiseSource(double value) {
        this.value = value;
    }

    @Override
    public double sample(double x) {
        return value;
    }

    @Override
    public double sample(double x, double y) {
        return value;
    }

    @Override
    public double sample(double x, double y, double z) {
        return value;
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return value;
    }
}
