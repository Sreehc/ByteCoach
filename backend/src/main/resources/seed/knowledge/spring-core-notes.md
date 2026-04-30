# Spring IOC

Spring IOC 的核心是把对象创建和依赖装配交给容器管理。常见面试点包括 Bean 生命周期、循环依赖处理和三级缓存。

## Bean 生命周期

Bean 从实例化、属性填充、初始化到销毁会经历完整生命周期。常见扩展点包括 BeanPostProcessor、InitializingBean 和自定义 init-method。

# Spring AOP

Spring AOP 的底层实现通常依赖 JDK 动态代理和 CGLIB。接口代理优先选择 JDK 动态代理，目标类没有接口时通常退回到 CGLIB。

## AOP 追问

常见追问包括：为什么 Spring AOP 是运行时织入、JDK 动态代理和 CGLIB 的适用边界、事务为什么会失效以及自调用为什么绕不过代理。

# Spring 事务

声明式事务本质上是通过 AOP 在方法前后织入事务开启、提交和回滚逻辑。传播行为、隔离级别和只读事务是高频考点。
