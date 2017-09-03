package com.ggx.easysp;

import com.ggx.annotation.BooleanKey;
import com.ggx.annotation.IntKey;
import com.ggx.annotation.SharePreference;
import com.ggx.annotation.StringKey;

/**
 * @author jerry.Guan
 *         created by 2017/9/3
 */
@SharePreference(name = "person")
public class Person {

    @StringKey(name = "sp_name")
    private String name;

    @IntKey
    public int age;

    @BooleanKey
    public boolean mark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }
}
