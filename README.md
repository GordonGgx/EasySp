# EasySp
Just a simple SharePreference operation

[![](https://jitpack.io/v/guanguoxiang/EasySp.svg)](https://jitpack.io/#guanguoxiang/EasySp)

### add dependencies 
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
	compile 'com.github.guanguoxiang.EasySp:annotations:1.0.1'
	compile 'com.github.guanguoxiang.EasySp:sharepreference:1.0.1'
	annotationProcessor 'com.github.guanguoxiang.EasySp:compiler:1.0.1'
}
```

创建一个普通的JavaBean 添加@SharePreference注解，默认情况下属性默认被添加持久化，也可以指定类型注解`@StringKey @IntKey @BooleanKey @FloatKey @LongKey`更改存储key的name名字，以及添加默认值。如果不想被持久化可使用@Transient注解即可。

### Step1
创建一个普通的Java Bean,使用`@SharePreference @StringKey @IntKey @BooleanKey @FloatKey @LongKey @Transient`.
```
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
```

### step2
使用AppConfig类去创建Java Bean 的实例对象，如下:
```
 //获取对象实例
Cat cat=AppConfig.create(getApplicationContext(),Cat.class);
cat.getName();
cat.getAge();
```

### step3
保存数据只需要调用AppConfig类的save方法即可：
```
//保存Cat数据
boolean result=AppConfig.save(Cat.class);
```
