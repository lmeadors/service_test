package com.example.servicetest;

import android.app.Service;
import android.os.Binder;

public class SimpleBinder<T extends Service> extends Binder {
	private final T service;

	public SimpleBinder(T service) {
		this.service = service;
	}

	public T getService() {
		return service;
	}
}
