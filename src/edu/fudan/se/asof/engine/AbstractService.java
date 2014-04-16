package edu.fudan.se.asof.engine;

import android.content.Context;

/**
 * Created by Dawnwords on 2014/4/6.
 */
public abstract class AbstractService {
    private int[] inputMatch, outputMatch;
    private Context context;

    public ReturnType invokeService(Object... input) {
        ReturnType type = invoke(getInputAfterMatching(input));
        return type == null ? null : type.getMatched(outputMatch);
    }

    protected abstract ReturnType invoke(Object... input);

    protected Context getContext() {
        return context;
    }

    void setInputMatch(int[] inputMatch) {
        this.inputMatch = inputMatch;
    }

    void setOutputMatch(int[] outputMatch) {
        this.outputMatch = outputMatch;
    }

    private Object[] getInputAfterMatching(Object[] input) {
        if (inputMatch != null) {
            Object[] inputAfterMatching = new Object[input.length];
            for (int i = 0; i < input.length; i++) {
                inputAfterMatching[i] = input[inputMatch[i]];
            }
            return inputAfterMatching;
        } else {
            return input;
        }
    }
}

