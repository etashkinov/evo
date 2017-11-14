package com.sgene.evo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by etashkinov on 10.11.2017.
 */
@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    private EvoGenotype genotype;

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.setProperty("java.awt.headless","false");
        int nGenerations = Integer.parseInt(args.getSourceArgs()[0]);
        genotype.evolve(nGenerations, (g, genome) -> {
            if (g % 50 == 0) {
                ImageIcon imageIcon = new ImageIcon(getImage(genome));
                JFrame frame = new JFrame();
                frame.setTitle("Generation " + g);
                frame.setSize(700, 100);
                frame.setContentPane(new JLabel(imageIcon));
                frame.setVisible(true);
            }
        });
    }

    private BufferedImage getImage(double[][] imageArray) {
        BufferedImage bufferedImage = new BufferedImage(imageArray.length, imageArray[0].length, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < imageArray.length; x++) {
            for (int y = 0; y < imageArray[0].length; y++) {
                int value = 255 - (int) (imageArray[x][y] * 255);
                bufferedImage.setRGB(x, y, new Color(value, value, value).getRGB());
            }
        }
        return bufferedImage;
    }
}
