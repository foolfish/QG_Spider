package com.lkb.util.httpclient.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**实现Comparable 接口
 * @author fastw
 * @date	2014-10-4 下午11:29:31
 * @param <V>
 */
public class ComparableFutureTask<V> extends FutureTask<V> implements
		Comparable<ComparableFutureTask<V>> {
	private Object object;

	public ComparableFutureTask(Callable<V> callable) {
		super(callable);
		object = callable;
	}

	public ComparableFutureTask(Runnable runnable, V result) {
		super(runnable, result);
		object = runnable;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(ComparableFutureTask<V> o) {
		if (this == o) {
			return 0;
		}
		if (o == null) {
			return -1; // high priority
		}
		if (object != null && o.object != null) {
			if (object.getClass().equals(o.object.getClass())) {
				if (object instanceof Comparable) {
					return ((Comparable) object).compareTo(o.object);
				}
			}
		}
		return 0;
	}
}
