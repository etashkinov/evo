package com.sgene.evo;

import java.util.Map;

/**
 * Created by etashkinov on 10.11.2017.
 */
public interface NeuralNetworkAPI {
    Map<String, Double> predict(double[][] image);
}
