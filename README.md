# EasySp
Just a simple SharePreference operation

### Step1
创建一个普通的Java Bean,使用`@SharePreference @StringKey @IntKey @BooleanKey @FloatKey @LongKey`.
```
@SharePreference
public class Cat {

    @StringKey
    private String name;

    @IntKey(name = "sp_age",value = 5)
    private int age;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
