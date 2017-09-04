package com.ggx.easysp;

import com.ggx.annotation.BooleanKey;
import com.ggx.annotation.FloatKey;
import com.ggx.annotation.IntKey;
import com.ggx.annotation.SharePreference;
import com.ggx.annotation.StringKey;
import com.ggx.annotation.Transient;

/**
 * @author jerry.Guan
 *         created by 2017/9/3
 */
@SharePreference(name = "Sp_Cat")
public class Cat {

    @StringKey
    private String name;

    @IntKey(name = "sp_age",value = 5)
    private Float age;

    @BooleanKey
    private boolean healthy;

    private float weight;

    private int sex;

    @Transient
    private Boolean hury;

    public Boolean getHury() {
        return hury;
    }

    public void setHury(Boolean hury) {
        this.hury = hury;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

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

    public Float getAge() {
        return age;
    }

    public void setAge(Float age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
