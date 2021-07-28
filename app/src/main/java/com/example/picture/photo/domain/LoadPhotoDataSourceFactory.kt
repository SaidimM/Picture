package com.example.picture.photo.domain

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.picture.photo.data.NetworkEndpoints
import com.example.picture.photo.data.UnsplashPhoto

/**
 * Android paging library data source factory.
 * This will create the load photo data source.
 */
class LoadPhotoDataSourceFactory constructor(private val networkEndpoints: NetworkEndpoints) :
    DataSource.Factory<Int, UnsplashPhoto>() {

    val sourceLiveData = MutableLiveData<LoadPhotoDataSource>()

    override fun create(): DataSource<Int, UnsplashPhoto> {
        val source = LoadPhotoDataSource(networkEndpoints)
        sourceLiveData.postValue(source)
        return source
    }
}
