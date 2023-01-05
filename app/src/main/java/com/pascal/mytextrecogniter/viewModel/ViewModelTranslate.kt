package com.pascal.mytextrecogniter.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pascal.mytextrecogniter.model.ResponseTranslate
import com.pascal.mytextrecogniter.repo.RepositoryTranslate

class ViewModelTranslate: ViewModel() {
    val repo = RepositoryTranslate()

    var responseTranslate = MutableLiveData<ResponseTranslate>()
    var isError = MutableLiveData<Throwable>()

    fun getTranslateView(engine: String, text: String, to: String) {
        repo.repoTranslate(engine, text, to, {
            responseTranslate.value = it
        }, {
            isError.value = it
        })
    }
}