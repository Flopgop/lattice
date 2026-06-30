package net.flamgop;

import net.flamgop.lattice.*;
import net.flamgop.lattice.coherent.*;
import net.flamgop.lattice.coherent.simplex.OpenSimplex2;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(3)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class NoiseBenchmark {

    @State(Scope.Thread)
    public static class SequenceState {
        public double x = 0.0;
        public double y = 0.0;
        public double z = 0.0;
        public double w = 0.0;

        public void increment() {
            x += 0.01; y += 0.01; z += 0.01; w += 0.01;
        }
    }

    @Param({"VALUE", "PERLIN", "SIMPLEX", "WORLEY", "GABOR"})
    public String noiseType;

    private NoiseSource source;

    @Setup
    public void setup() {
        source = switch (noiseType) {
            case "VALUE"   -> new ValueNoiseSource(1337);
            case "PERLIN"  -> new PerlinNoiseSource(1337);
            case "SIMPLEX" -> new OpenSimplex2.StandardLatticeNoiseSource(1337);
            case "WORLEY"  -> new WorleyNoiseSource(1337);
            case "GABOR"   -> new GaborNoiseSource(1337, 1.0, 8, 1.0, 1.0, 1.0, 1.0);
            default -> throw new IllegalStateException("Unexpected value: " + noiseType);
        };
    }

    @Benchmark
    public double test2DConstant() {
        return source.sample(0.5, 0.5);
    }

    @Benchmark
    public double test3DConstant() {
        return source.sample(0.5, 0.5, 0.5);
    }

    @Benchmark
    public double test4DConstant() {
        return source.sample(0.5, 0.5, 0.5, 0.5);
    }

    @Benchmark
    public double test2DThreadLocalRandom() {
        return source.sample(ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble());
    }

    @Benchmark
    public double test3DThreadLocalRandom() {
        return source.sample(ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble());
    }

    @Benchmark
    public double test4DThreadLocalRandom() {
        return source.sample(ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble());
    }

    @Benchmark
    public double test2DSequential(SequenceState state) {
        state.increment();
        return source.sample(state.x, state.y);
    }

    @Benchmark
    public double test3DSequential(SequenceState state) {
        state.increment();
        return source.sample(state.x, state.y, state.z);
    }

    @Benchmark
    public double test4DSequential(SequenceState state) {
        state.increment();
        return source.sample(state.x, state.y, state.z, state.w);
    }
}