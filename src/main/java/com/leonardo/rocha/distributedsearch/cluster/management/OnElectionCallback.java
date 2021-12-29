package com.leonardo.rocha.distributedsearch.cluster.management;

public interface OnElectionCallback {

    void onElectedToBeLeader();

    void onWorker();

}