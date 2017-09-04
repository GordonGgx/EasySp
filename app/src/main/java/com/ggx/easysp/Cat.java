package com.ggx.easysp;

import com.ggx.annotation.BooleanKey;
import com.ggx.annotation.FloatKey;
import com.ggx.annotation.IntKey;
import com.ggx.annotation.SharePreference;
import com.ggx.annotation.StringKey;

/**
 * @author jerry.Guan
 *         created by 2017/9/3
 */
@SharePreference(name = "Sp_Cat")
public class Cat {

    @StringKey
    private String name;

    @IntKey(name = "sp_age",value = 5)
    private int age;

    @BooleanKey
    private boolean healthy;

    @FloatKey
    private float weight;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
