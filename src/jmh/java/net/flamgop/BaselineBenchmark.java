package net.flamgop;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 8)
@Measurement(iterations = 5)
public class BaselineBenchmark {
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

    @Benchmark
    public double baseline() {
        return 0.0;
    }

    @Benchmark
    public double threadLocalRandomBaseline() {
        return ThreadLocalRandom.current().nextDouble();
    }

    @Benchmark
    public SequenceState sequentialBaseline(SequenceState state) {
        state.increment();
        return state;
    }
}
