package com.example.picture.photo.domain

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.picture.photo.data.NetworkEndpoints
import com.example.picture.photo.data.UnsplashPhoto
import com.example.picture.photo.domain.SearchPhotoDataSource

/**
 * Android paging library data source factory.
 * This will create the search photo data source.
 */
class SearchPhotoDataSourceFactory constructor(
    private val networkEndpoints: NetworkEndpoints,
    private val criteria: String
) :
    DataSource.Factory<Int, UnsplashPhoto>() {

    val sourceLiveData = MutableLiveData<SearchPhotoDataSource>()

    override fun create(): DataSource<Int, UnsplashPhoto> {
        val source = SearchPhotoDataSource(networkEndpoints, criteria)
        sourceLiveData.postValue(source)
        return source
    }
}
