package net.flamgop.lattice.coherent;

import net.flamgop.lattice.MathHelpers;
import net.flamgop.lattice.NoiseSource;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("DuplicatedCode")
public final class PerlinNoiseSource implements NoiseSource {

    private final long seed;

    /// Perlin noise, as originally described by Ken Perlin in his 1982 paper.
    /// This method does not use a permutation table but instead calculates hashes on the fly.
    /// As far as I'm aware, this method provides better performance on modern computers as it uses the cache instead of getting from memory.
    /// This is speculative as I have not directly tested using a permutation table vs. computing the hash.
    /// Perlin noise scales with O(2^n) for n dimensions.
    /// @see NoiseSource
    public PerlinNoiseSource(long seed) {
        this.seed = seed;
    }

    @Override
    @Contract(pure = true)
    public double sample(double x) {
        int x0 = MathHelpers.fastFloor(x);
        int x1 = x0 + 1;

        double dx0 = x - x0;
        double dx1 = x - x1;

        double u = MathHelpers.perlinFade(dx0);

        int h0 = MathHelpers.splitMix64(x0, seed);
        int h1 = MathHelpers.splitMix64(x1, seed);

        double v0 = MathHelpers.grad1D(h0, dx0);
        double v1 = MathHelpers.grad1D(h1, dx1);

        return MathHelpers.lerp(u, v0, v1);
    }

    @Override
    @Contract(pure = true)
    public double sample(double x, double y) {
        int x0 = MathHelpers.fastFloor(x); int y0 = MathHelpers.fastFloor(y);
        int x1 = x0 + 1; int y1 = y0 + 1;

        double dx0 = x - x0; double dy0 = y - y0;
        double dx1 = x - x1; double dy1 = y - y1;

        double u = MathHelpers.perlinFade(dx0); double v = MathHelpers.perlinFade(dy0);

        int h00 = MathHelpers.hash(x0, y0, seed);
        int h10 = MathHelpers.hash(x1, y0, seed);
        int h01 = MathHelpers.hash(x0, y1, seed);
        int h11 = MathHelpers.hash(x1, y1, seed);

        double v00 = MathHelpers.grad2D(h00, dx0, dy0);
        double v10 = MathHelpers.grad2D(h10, dx1, dy0);
        double v01 = MathHelpers.grad2D(h01, dx0, dy1);
        double v11 = MathHelpers.grad2D(h11, dx1, dy1);

        double nx0 = MathHelpers.lerp(u, v00, v10);
        double nx1 = MathHelpers.lerp(u, v01, v11);
        return MathHelpers.lerp(v, nx0, nx1);
    }

    @Override
    @Contract(pure = true)
    public double sample(double x, double y, double z) {
        int x0 = MathHelpers.fastFloor(x); int y0 = MathHelpers.fastFloor(y); int z0 = MathHelpers.fastFloor(z);
        int x1 = x0 + 1; int y1 = y0 + 1; int z1 = z0 + 1;

        double dx0 = x - x0; double dy0 = y - y0; double dz0 = z - z0;
        double dx1 = x - x1; double dy1 = y - y1; double dz1 = z - z1;

        double u = MathHelpers.perlinFade(dx0); double v = MathHelpers.perlinFade(dy0); double w = MathHelpers.perlinFade(dz0);

        int h000 = MathHelpers.hash(x0, y0, z0, seed);
        int h100 = MathHelpers.hash(x1, y0, z0, seed);
        int h010 = MathHelpers.hash(x0, y1, z0, seed);
        int h110 = MathHelpers.hash(x1, y1, z0, seed);
        int h001 = MathHelpers.hash(x0, y0, z1, seed);
        int h101 = MathHelpers.hash(x1, y0, z1, seed);
        int h011 = MathHelpers.hash(x0, y1, z1, seed);
        int h111 = MathHelpers.hash(x1, y1, z1, seed);

        double v000 = MathHelpers.grad3D(h000, dx0, dy0, dz0);
        double v100 = MathHelpers.grad3D(h100, dx1, dy0, dz0);
        double v010 = MathHelpers.grad3D(h010, dx0, dy1, dz0);
        double v110 = MathHelpers.grad3D(h110, dx1, dy1, dz0);
        double v001 = MathHelpers.grad3D(h001, dx0, dy0, dz1);
        double v101 = MathHelpers.grad3D(h101, dx1, dy0, dz1);
        double v011 = MathHelpers.grad3D(h011, dx0, dy1, dz1);
        double v111 = MathHelpers.grad3D(h111, dx1, dy1, dz1);

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

        double dx0 = x - x0; double dy0 = y - y0; double dz0 = z - z0; double dw0 = w - w0;
        double dx1 = x - x1; double dy1 = y - y1; double dz1 = z - z1; double dw1 = w - w1;

        double u = MathHelpers.perlinFade(dx0); double v = MathHelpers.perlinFade(dy0); double m = MathHelpers.perlinFade(dz0); double n = MathHelpers.perlinFade(dw0);

        int h0000 = MathHelpers.hash(x0, y0, z0, w0, seed);
        int h1000 = MathHelpers.hash(x1, y0, z0, w0, seed);
        int h0100 = MathHelpers.hash(x0, y1, z0, w0, seed);
        int h1100 = MathHelpers.hash(x1, y1, z0, w0, seed);
        int h0010 = MathHelpers.hash(x0, y0, z1, w0, seed);
        int h1010 = MathHelpers.hash(x1, y0, z1, w0, seed);
        int h0110 = MathHelpers.hash(x0, y1, z1, w0, seed);
        int h1110 = MathHelpers.hash(x1, y1, z1, w0, seed);

        int h0001 = MathHelpers.hash(x0, y0, z0, w1, seed);
        int h1001 = MathHelpers.hash(x1, y0, z0, w1, seed);
        int h0101 = MathHelpers.hash(x0, y1, z0, w1, seed);
        int h1101 = MathHelpers.hash(x1, y1, z0, w1, seed);
        int h0011 = MathHelpers.hash(x0, y0, z1, w1, seed);
        int h1011 = MathHelpers.hash(x1, y0, z1, w1, seed);
        int h0111 = MathHelpers.hash(x0, y1, z1, w1, seed);
        int h1111 = MathHelpers.hash(x1, y1, z1, w1, seed);

        double v0000 = MathHelpers.grad4D(h0000, dx0, dy0, dz0, dw0);
        double v1000 = MathHelpers.grad4D(h1000, dx1, dy0, dz0, dw0);
        double v0100 = MathHelpers.grad4D(h0100, dx0, dy1, dz0, dw0);
        double v1100 = MathHelpers.grad4D(h1100, dx1, dy1, dz0, dw0);
        double v0010 = MathHelpers.grad4D(h0010, dx0, dy0, dz1, dw0);
        double v1010 = MathHelpers.grad4D(h1010, dx1, dy0, dz1, dw0);
        double v0110 = MathHelpers.grad4D(h0110, dx0, dy1, dz1, dw0);
        double v1110 = MathHelpers.grad4D(h1110, dx1, dy1, dz1, dw0);

        double v0001 = MathHelpers.grad4D(h0001, dx0, dy0, dz0, dw1);
        double v1001 = MathHelpers.grad4D(h1001, dx1, dy0, dz0, dw1);
        double v0101 = MathHelpers.grad4D(h0101, dx0, dy1, dz0, dw1);
        double v1101 = MathHelpers.grad4D(h1101, dx1, dy1, dz0, dw1);
        double v0011 = MathHelpers.grad4D(h0011, dx0, dy0, dz1, dw1);
        double v1011 = MathHelpers.grad4D(h1011, dx1, dy0, dz1, dw1);
        double v0111 = MathHelpers.grad4D(h0111, dx0, dy1, dz1, dw1);
        double v1111 = MathHelpers.grad4D(h1111, dx1, dy1, dz1, dw1);

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

        double nz0 = MathHelpers.lerp(m, ny00, ny10);
        double nz1 = MathHelpers.lerp(m, ny01, ny11);

        return MathHelpers.lerp(n, nz0, nz1);
    }
}
