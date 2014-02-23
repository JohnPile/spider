package pile.spider.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This allows the insertion of normalized interpretive guesses with accumulating confidence scores.
 * The idea is to make as many guesses as you can with a variety of techniques, and keep the one with the highest score.
 */
public class InterpretationMap {

    Map<String, Interpretation> interpretationMap;

    public InterpretationMap() {
        this.interpretationMap = new HashMap<String, Interpretation>();
    }

    public void addInterpretation(String name, int confidence) {
        addInterpretation(new Interpretation(name, confidence));
    }

    public void addInterpretation(Interpretation interpretation) {
        String lc = interpretation.getName().toLowerCase();
        if (interpretationMap.containsKey(lc)) {
            Interpretation existing = interpretationMap.get(lc);
            interpretation = new Interpretation(interpretation.getName(), interpretation.getConfidence() + existing.getConfidence());
        }
        interpretationMap.put(lc, interpretation);
    }

    public Interpretation topInterpretation() {
        int max = 0;
        Interpretation topInterpretation = null;
        for (Interpretation interpretation : interpretationMap.values()) {
            if (interpretation.getConfidence() > max) {
                topInterpretation = interpretation;
                max = interpretation.getConfidence();
            }
        }
        if (topInterpretation==null) {
            return new Interpretation(null, 0);
        }
        return topInterpretation;
    }

    public void clear() {
        interpretationMap.clear();
    }
}
