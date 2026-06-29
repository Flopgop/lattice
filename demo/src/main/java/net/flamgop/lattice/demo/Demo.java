package net.flamgop.lattice.demo;

import net.flamgop.lattice.*;
import net.flamgop.lattice.coherent.GaborNoiseSource;
import net.flamgop.lattice.coherent.PerlinNoiseSource;
import net.flamgop.lattice.coherent.ValueNoiseSource;
import net.flamgop.lattice.coherent.WorleyNoiseSource;
import net.flamgop.lattice.coherent.simplex.OpenSimplex2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Demo {
    static void main() {
        NoiseSource value = new ValueNoiseSource(1337);
        NoiseSource perlin = new PerlinNoiseSource(1337);
        NoiseSource simplex = new OpenSimplex2.StandardLatticeNoiseSource(1337);
        NoiseSource worley = new WorleyNoiseSource(1337);
        NoiseSource gabor = new GaborNoiseSource(1337, 0.785, 1.0, 8);

        NoiseSource complex = new OpenSimplex2.StandardLatticeNoiseSource(1337)
                .warp(perlin
                        .map(-1, 1, 0, 1)
                        .scale(3)
                )
                .fBm(4, 2.0, 0.5);

        NoiseSource complex2 = new WorleyNoiseSource(1337)
                .fBm(5, 2.0, 0.5);

        //noinspection ResultOfMethodCallIgnored
        new File("noise").mkdirs();
        generateNoiseImage(256, 256, "noise/value.png", value);
        generateNoiseImage(256, 256, "noise/perlin.png", perlin);
        generateNoiseImage(256, 256, "noise/simplex.png", simplex);
        generateNoiseImage(256, 256, "noise/worley.png", worley);
        generateNoiseImage(256, 256, "noise/gabor.png", gabor);
        generateNoiseImage(256, 256, "noise/complex.png", complex);
        generateNoiseImage(256, 256, "noise/complex2.png", complex2);
    }

    private static void generateNoiseImage(int width, int height, String name, NoiseSource source) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double noise = source.sample(x * (1.0 / 8.0), y * (1.0 / 8.0));
                noise = (noise + 1) / 2;

                int gray = (int) (noise * 255);
                image.setRGB(x, y, gray << 16 | gray << 8 | gray);
            }
        }

        try {
            ImageIO.write(image, "png", new File(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
