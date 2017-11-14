//package com.sgene.evo;
//
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.stream.DoubleStream;
//
///**
// * Created by etashkinov on 10.11.2017.
// */
//@Component
//public class NeuralNetworkAPIStub implements NeuralNetworkAPI {
//    @Override
//    public double[] predict(double[][] image) {
//        double[] result = new double[10];
//        Arrays.fill(result, 0.0);
//        result[EvoGenotype.FIT_FIGURE] = DoubleStream.of(image[0]).limit(14).sum() -  DoubleStream.of(image[0]).skip(14).sum();
//        return result;
//    }
//}
