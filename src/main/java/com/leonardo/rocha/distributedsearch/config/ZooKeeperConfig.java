package com.leonardo.rocha.distributedsearch.config;

import com.leonardo.rocha.distributedsearch.OnElectionAction;
import com.leonardo.rocha.distributedsearch.cluster.management.ServiceRegistry;
import com.leonardo.rocha.distributedsearch.cluster.management.ZooKeeperWatcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZooKeeperConfig {
    @Value("${server.port}")
    private int currentServerPort;

    @Autowired
    private ZooKeeperWatcher zooKeeperWatcher;

    @Bean
    public ZooKeeper zooKeeper(){
        return zooKeeperWatcher.getZooKeeper();
    }

    @Bean
    public ServiceRegistry workersServiceRegistry(ZooKeeper zooKeeper){
        return new ServiceRegistry(zooKeeper, ServiceRegistry.WORKERS_REGISTRY_ZNODE);
    }

    @Bean
    public ServiceRegistry coordinatorsServiceRegistry(ZooKeeper zooKeeper){
        return new ServiceRegistry(zooKeeper, ServiceRegistry.WORKERS_REGISTRY_ZNODE);
    }

    @Bean
    public OnElectionAction onElectionAction(ServiceRegistry workersServiceRegistry, ServiceRegistry coordinatorsServiceRegistry){
        return new OnElectionAction(workersServiceRegistry, coordinatorsServiceRegistry, currentServerPort);
    }
}
