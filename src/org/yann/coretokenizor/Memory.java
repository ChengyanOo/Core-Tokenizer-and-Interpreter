package org.yann.coretokenizor;

import java.util.HashMap;
import java.util.Map;

public class Memory {
    private Map<String, Integer> variables = new HashMap<>();

    public void set(String id, int value) {
        variables.put(id, value);
    }

    public int get(String id) {
        return variables.getOrDefault(id, 0);
    }

    public boolean has(String id) {
        return variables.containsKey(id);
    }
}
