package net.flamgop.lattice;

public final class MathHelpers {
    public static double lerp(double t, double a, double b) { return a + t * (b - a); }
    public static double perlinFade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
    public static int fastFloor(double n) {
        int xi = (int)n;
        return n < xi ? xi - 1 : xi;
    }
    public static int fastRound(double n) {
        return n < 0 ? (int)(n - 0.5) : (int)(n + 0.5);
    }

    public static double murmurHash3Finalizer(int prime1, int prime2, long seed) {
        long h = seed ^ (prime1 * 11117L) ^ (prime2 * 34387L);
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return (h & 0x1FFFFFFFFFFFFFL) / (double) 0x20000000000000L;
    }

    public static int splitMix64(int x, long seed) {
        long h = (seed ^ x) * 0xbf58476d1ce4e5b9L;
        h ^= h >>> 30;
        h *= 0x94d049bb133111ebL;
        h ^= h >>> 27;
        return Long.hashCode(h);
    }
    public static int hash(int x, int y, long seed) {
        return splitMix64(x ^ (y * 397), seed);
    }
    public static int hash(int x, int y, int z, long seed) {
        return splitMix64(x ^ (y * 397) ^ (z * 967), seed);
    }
    public static int hash(int x, int y, int z, int w, long seed) {
        return splitMix64(x ^ (y * 397) ^ (z * 967) ^ (w * 1543), seed);
    }

    public static double hash1D(double x, long seed) {
        int h = splitMix64(fastFloor(x), seed);
        h += (h >>> 31);
        return (double) h / Integer.MAX_VALUE;
    }
    public static double hash2D(double x, double y, long seed) {
        int h = hash(fastFloor(x), fastFloor(y), seed);
        h += (h >>> 31);
        return (double) h / Integer.MAX_VALUE;
    }
    public static double hash3D(double x, double y, double z, long seed) {
        int h = hash(fastFloor(x), fastFloor(y), fastFloor(z), seed);
        h += (h >>> 31);
        return (double) h / Integer.MAX_VALUE;
    }
    public static double hash4D(double x, double y, double z, double w, long seed) {
        int h = hash(fastFloor(x), fastFloor(y), fastFloor(z), fastFloor(w), seed);
        h += (h >>> 31);
        return (double) h / Integer.MAX_VALUE;
    }

    public static double grad1D(int hash, double dx) {
        return (hash & 1) == 0 ? dx : -dx;
    }
    public static double grad2D(int hash, double dx, double dy) {
        double u = (hash & 4) == 0 ? dx : 0.0;
        double v = (hash & 4) == 0 ? dy : ((hash & 1) == 0 ? dx : dy);

        double xSign = (hash & 1) == 0 ? u : -u;
        double ySign = (hash & 2) == 0 ? v : -v;

        return xSign + ySign;
    }
    public static double grad3D(int hash, double dx, double dy, double dz) {
        int h = hash & 15;
        double u = h < 8 ? dx : dy;
        double v = h < 4 ? dy : (h == 12 || h == 14 ? dx : dz);
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
    public static double grad4D(int hash, double dx, double dy, double dz, double dw) {
        int h = hash & 31;

        double u = h < 24 ? dx : dy;
        double v = h < 16 ? dy : dz;
        double w = h < 8  ? dz : dw;

        return ((h & 1) == 0 ? u : -u) +
                ((h & 2) == 0 ? v : -v) +
                ((h & 4) == 0 ? w : -w);
    }

    public static double map(double val, double currentMin, double currentMax, double targetMin, double targetMax) {
        double normalized = (val - currentMin) / (currentMax - currentMin);
        return targetMin + normalized * (targetMax - targetMin);
    }
}
