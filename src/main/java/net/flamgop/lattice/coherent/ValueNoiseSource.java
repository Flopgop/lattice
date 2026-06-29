package net.flamgop.lattice.coherent;

import net.flamgop.lattice.MathHelpers;
import net.flamgop.lattice.NoiseSource;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("DuplicatedCode")
public final class ValueNoiseSource implements NoiseSource {
    private final long seed;

    /// A noise source that generates value noise.
    /// <p/>
    /// Value noise works by assigning random values to points on a grid (in this case, with a hash function) and interpolating between them.
    /// Value noise scales with O(2^n) for n dimensions.
    /// @see NoiseSource
    public ValueNoiseSource(long seed) { this.seed = seed; }

    @Override
    @Contract(pure = true)
    public double sample(double x) {
        int x0 = MathHelpers.fastFloor(x);
        int x1 = x0 + 1;

        double tx = x - Math.floor(x);
        double u = MathHelpers.perlinFade(tx);

        double v0 = MathHelpers.hash1D(x0, seed);
        double v1 = MathHelpers.hash1D(x1, seed);
        return MathHelpers.lerp(u, v0, v1);
    }

    @Override
    @Contract(pure = true)
    public double sample(double x, double y) {
        int x0 = MathHelpers.fastFloor(x); int y0 = MathHelpers.fastFloor(y);
        int x1 = x0 + 1; int y1 = y0 + 1;

        double tx = x - Math.floor(x); double ty = y - Math.floor(y);
        double u = MathHelpers.perlinFade(tx); double v = MathHelpers.perlinFade(ty);

        double v00 = MathHelpers.hash2D(x0, y0, seed);
        double v10 = MathHelpers.hash2D(x1, y0, seed);
        double v01 = MathHelpers.hash2D(x0, y1, seed);
        double v11 = MathHelpers.hash2D(x1, y1, seed);

        double nx0 = MathHelpers.lerp(u, v00, v10);
        double nx1 = MathHelpers.lerp(u, v01, v11);
        return MathHelpers.lerp(v, nx0, nx1);
    }

    @Override
    @Contract(pure = true)
    public double sample(double x, double y, double z) {
        int x0 = MathHelpers.fastFloor(x); int y0 = MathHelpers.fastFloor(y); int z0 = MathHelpers.fastFloor(z);
        int x1 = x0 + 1; int y1 = y0 + 1; int z1 = z0 + 1;

        double tx = x - Math.floor(x); double ty = y - Math.floor(y); double tz = z - Math.floor(z);
        double u = MathHelpers.perlinFade(tx); double v = MathHelpers.perlinFade(ty); double w = MathHelpers.perlinFade(tz);

        double v000 = MathHelpers.hash3D(x0, y0, z0, seed);
        double v100 = MathHelpers.hash3D(x1, y0, z0, seed);
        double v010 = MathHelpers.hash3D(x0, y1, z0, seed);
        double v110 = MathHelpers.hash3D(x1, y1, z0, seed);
        double v001 = MathHelpers.hash3D(x0, y0, z1, seed);
        double v101 = MathHelpers.hash3D(x1, y0, z1, seed);
        double v011 = MathHelpers.hash3D(x0, y1, z1, seed);
        double v111 = MathHelpers.hash3D(x1, y1, z1, seed);

        double nx00 = MathHelpers.lerp(u, v000, v100);
        double nx10 = MathHelpers.lerp(u, v010, v110);
        double nx01 = MathHelpers.lerp(u, v001, v101);
        double nx11 = MathHelpers.lerp(u, v011, v111);

        double ny0 = MathHelpers.lerp(v, nx00, nx10);
        double ny1 = MathHelpers.lerp(v, nx01, nx11);
        return MathHelpers.lerp(w, ny0, ny1);
    }

    @Override
    @Contract(pure = true)
    public double sample(double x, double y, double z, double w) {
        int x0 = MathHelpers.fastFloor(x); int y0 = MathHelpers.fastFloor(y); int z0 = MathHelpers.fastFloor(z); int w0 = MathHelpers.fastFloor(w);
        int x1 = x0 + 1; int y1 = y0 + 1; int z1 = z0 + 1; int w1 = w0 + 1;

        double tx = x - Math.floor(x); double ty = y - Math.floor(y); double tz = z - Math.floor(z); double tw = w - Math.floor(w);
        double u = MathHelpers.perlinFade(tx); double v = MathHelpers.perlinFade(ty); double s = MathHelpers.perlinFade(tz); double t = MathHelpers.perlinFade(tw);

        double v0000 = MathHelpers.hash4D(x0, y0, z0, w0, seed);
        double v1000 = MathHelpers.hash4D(x1, y0, z0, w0, seed);
        double v0100 = MathHelpers.hash4D(x0, y1, z0, w0, seed);
        double v1100 = MathHelpers.hash4D(x1, y1, z0, w0, seed);
        double v0010 = MathHelpers.hash4D(x0, y0, z1, w0, seed);
        double v1010 = MathHelpers.hash4D(x1, y0, z1, w0, seed);
        double v0110 = MathHelpers.hash4D(x0, y1, z1, w0, seed);
        double v1110 = MathHelpers.hash4D(x1, y1, z1, w0, seed);

        double v0001 = MathHelpers.hash4D(x0, y0, z0, w1, seed);
        double v1001 = MathHelpers.hash4D(x1, y0, z0, w1, seed);
        double v0101 = MathHelpers.hash4D(x0, y1, z0, w1, seed);
        double v1101 = MathHelpers.hash4D(x1, y1, z0, w1, seed);
        double v0011 = MathHelpers.hash4D(x0, y0, z1, w1, seed);
        double v1011 = MathHelpers.hash4D(x1, y0, z1, w1, seed);
        double v0111 = MathHelpers.hash4D(x0, y1, z1, w1, seed);
        double v1111 = MathHelpers.hash4D(x1, y1, z1, w1, seed);

        double nx000 = MathHelpers.lerp(u, v0000, v1000);
        double nx100 = MathHelpers.lerp(u, v0100, v1100);
        double nx010 = MathHelpers.lerp(u, v0010, v1010);
        double nx110 = MathHelpers.lerp(u, v0110, v1110);
        double nx001 = MathHelpers.lerp(u, v0001, v1001);
        double nx101 = MathHelpers.lerp(u, v0101, v1101);
        double nx011 = MathHelpers.lerp(u, v0011, v1011);
        double nx111 = MathHelpers.lerp(u, v0111, v1111);

        double ny00 = MathHelpers.lerp(v, nx000, nx100);
        double ny10 = MathHelpers.lerp(v, nx010, nx110);
        double ny01 = MathHelpers.lerp(v, nx001, nx101);
        double ny11 = MathHelpers.lerp(v, nx011, nx111);

        double nz0 = MathHelpers.lerp(s, ny00, ny10);
        double nz1 = MathHelpers.lerp(s, ny01, ny11);
        return MathHelpers.lerp(t, nz0, nz1);
    }
}
