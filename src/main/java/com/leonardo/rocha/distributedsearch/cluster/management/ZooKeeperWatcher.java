package com.leonardo.rocha.distributedsearch.cluster.management;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ZooKeeperWatcher implements Watcher {
    private final ZooKeeper zooKeeper;

    public ZooKeeperWatcher(@Value("${zookeeper.address}") String zookeeperAddress,
                            @Value("${zookeeper.port}") String zookeeperPort,
                            @Value("${zookeeper.sessionTimeout}") int sessionTimeout) throws IOException {
        this.zooKeeper = new ZooKeeper(zookeeperAddress + ":" + zookeeperPort, sessionTimeout, this);
    }

    public ZooKeeper getZooKeeper(){
        return this.zooKeeper;
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("Successfully connected to Zookeeper");
                } else {
                    synchronized (zooKeeper) {
                        System.out.println("Disconnected from Zookeeper event");
                        zooKeeper.notifyAll();
                    }
                }
        }
    }
}
