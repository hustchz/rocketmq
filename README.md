# rocketmq transaction

如果将业务和消息投递放在一个事务，是错误的，原因有两个：
1）发送消息投递到MQ中的过程是一个网络调用，因此如果网络出现阻塞或者延迟，会导致DB长事务的产生，这是不可取的
2）二将军问题，当投递到MQ失败，有两种情况，一种是投递过程就超时或者失败了，还有一种情况是投递成功但是response失败，而发送方无法确认是哪一种



在不利用RocketMQ的事务消息的条件下完成分布式事务（生产端将消息生产(写表)和业务做一个事务，消费端做幂等）
利用RocketMQ集群实现分布式事务