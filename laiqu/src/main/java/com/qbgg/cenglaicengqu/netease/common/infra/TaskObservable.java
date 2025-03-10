package com.qbgg.cenglaicengqu.netease.common.infra;

public class TaskObservable extends Observable<TaskObserver> implements TaskObserver {
	@Override
	public void onTaskResult(Task task, Object[] results) {
		for (TaskObserver observer : getObservers()) {
			observer.onTaskResult(task, results);
		}
	}

	@Override
	public void onTaskProgress(Task task, Object[] params) {
		for (TaskObserver observer : getObservers()) {
			observer.onTaskProgress(task, params);
		}
	}
}
