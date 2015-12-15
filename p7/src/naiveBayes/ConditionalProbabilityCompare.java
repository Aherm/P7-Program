package naiveBayes;

import java.util.Comparator;

public class ConditionalProbabilityCompare implements Comparator<ConditionalProbability> {

    public int compare(ConditionalProbability condProb1, ConditionalProbability condProb2){
        if(condProb2.getProbability() < condProb1.getProbability()) return -1;
        if(condProb2.getProbability() == condProb1.getProbability()) return 0;
        return 1;
    }

}
