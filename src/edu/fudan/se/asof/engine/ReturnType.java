package edu.fudan.se.asof.engine;

import java.util.ArrayList;

/**
 * Created by Dawnwords on 2014/4/16.
 */
public class ReturnType {
    private ArrayList<NVPair> nvPairs;

    public ReturnType() {
        nvPairs = new ArrayList<NVPair>();
    }

    public void put(String name, Object value) {
        nvPairs.add(new NVPair(name, value));
    }

    public Object get(String name) {
        for (NVPair nvPair : nvPairs) {
            if (nvPair.name.equals(name)) {
                return nvPair.value;
            }
        }
        return null;
    }

    ReturnType getMatched(int[] outputMatch, String[] originParaName) {
        if (outputMatch != null) {
            ReturnType result = new ReturnType();
            for (int i = 0; i < outputMatch.length; i++) {
                result.put(originParaName[i], result.nvPairs.get(outputMatch[i]).value);
            }
            return result;
        } else {
            return this;
        }
    }

    class NVPair {
        String name;
        Object value;

        NVPair(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
