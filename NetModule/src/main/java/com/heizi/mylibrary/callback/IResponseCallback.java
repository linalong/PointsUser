package com.heizi.mylibrary.callback;


import com.heizi.mylibrary.model.ErrorModel;

public abstract class IResponseCallback<T> {

	public abstract void onSuccess(T data);

	public abstract void onFailure(ErrorModel errorModel);

	public abstract void onStart();

	public void onLoading(long total, long current, boolean isUploading) {
	}

}
