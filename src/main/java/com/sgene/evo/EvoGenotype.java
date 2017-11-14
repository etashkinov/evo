package com.sgene.evo;

import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Created by etashkinov on 10.11.2017.
 */
@Component
public class EvoGenotype {

    static final String FIT_FIGURE = "line";

    private static final int IMAGE_SIZE = 28;

    private final Engine<DoubleGene, Double> engine;

    private final NeuralNetworkAPI api;

    @Autowired
    public EvoGenotype(NeuralNetworkAPI api) {
        this.api = api;
        Genotype<DoubleGene> gtf = Genotype.of(DoubleChromosome.of(0, 1, IMAGE_SIZE), IMAGE_SIZE);
        engine = Engine
                    .builder(this::eval, gtf)
                    .alterers(new GaussianMutator<>(0.2), new UniformCrossover<>(0.6))
                .build();
    }

    private double eval(Genotype<DoubleGene> genotype) {
        double[][] image = getImage(genotype);
        Map<String, Double> prediction = api.predict(image);
        return prediction.get(FIT_FIGURE);
    }

    private double[][] getImage(Genotype<DoubleGene> genotype) {
        Stream<double[]> stream = genotype.stream().map(c -> c.stream().mapToDouble(NumericGene::doubleValue).toArray());
        return stream.toArray(i -> new double[IMAGE_SIZE][]);
    }

    public void evolve(int limit, BiConsumer<Long, double[][]> listener) {
        EvolutionStatistics<Double, ?> statistics = EvolutionStatistics.ofNumber();
        Genotype<DoubleGene> result = engine.stream()
                .limit(limit)
                .peek(er -> {
                    Phenotype<DoubleGene, Double> bestPhenotype = er.getBestPhenotype();
                    double[][] image = getImage(bestPhenotype.getGenotype());
                    long generation = er.getGeneration();
                    listener.accept(generation, image);
                    statistics.accept(er);
                    System.out.println("Generation " + generation + ": " + bestPhenotype.getFitness());
                })
                .collect(EvolutionResult.toBestGenotype());

        System.out.println(statistics);
        System.out.println(result);
    }
}
