package com.ggx.easysp;

import com.ggx.annotation.BooleanKey;
import com.ggx.annotation.SharePreference;
import com.ggx.annotation.StringKey;

/**
 * @author jerry.Guan
 *         created by 2017/9/3
 */
@SharePreference
public class Cat {

    @StringKey
    private String name;
    @BooleanKey
    private boolean healthy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }
}
