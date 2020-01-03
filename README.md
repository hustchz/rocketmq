# rocketmq transaction

如果将业务和消息投递放在一个事务，是错误的，原因有两个：
1）发送消息投递到MQ中的过程是一个网络调用，因此如果网络出现阻塞或者延迟，会导致DB长事务的产生，这是不可取的
2）二将军问题，当投递到MQ失败，有两种情况，一种是投递过程就超时或者失败了，还有一种情况是投递成功但是response失败，而发送方无法确认是哪一种

在不利用RocketMQ的事务消息的条件下完成分布式事务（生产端将消息生产(写表)和业务做一个事务，消费端做幂等）
利用RocketMQ集群实现分布式事务

springboot 使用 @Transactional失效的坑：
1)开启事务@EnableTransactional
2)当使用了多数据源时，在事务中需要指定使用哪一种事务(
当只有一个数据源的时候默认是使用的transactionManager),需要在不同的DataSource中配置不同的事务管理器DataSourceTransactionManager
3)Spring 的本质是AOP，因此若在事务中使用类的内部方法不一定会生效，除非该内部方法也使用了@Transactional
4)在业务层中不要try-catch 应该在上游服务中进行捕捉
5) 默认@Transactional 只会捕捉unchecked异常，也就是虚拟机自动处理的异常，如NullPointerException RuntimeException
可以指定rollBackFor = Exception.class 表示所有异常均回滚




