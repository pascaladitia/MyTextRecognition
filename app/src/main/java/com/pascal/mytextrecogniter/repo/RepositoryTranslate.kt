package com.pascal.mytextrecogniter.repo

import com.pascal.mytextrecogniter.model.ResponseTranslate
import com.pascal.mytextrecogniter.network.ConfigNetwork
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class RepositoryTranslate {

    fun repoTranslate(engine: String, text: String, to: String,
                      responseHandler: (ResponseTranslate) -> Unit, errorHandler: (Throwable) -> Unit) {
        ConfigNetwork.getNetwork().getTranslate(engine, text, to).subscribeOn((Schedulers.io()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                responseHandler(it)
            }, {
                errorHandler(it)
            })
    }
}