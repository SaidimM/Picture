package com.example.picture.photo.ui.state

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.picture.downloader.DownloadMain
import com.example.picture.photo.UnsplashPhotoPicker
import com.example.picture.photo.data.UnsplashPhoto
import com.example.picture.photo.domain.Repository
import com.example.picture.photo.utils.ConcurrentDownLoad
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import java.util.concurrent.TimeUnit

/**
 * View model for the picker screen.
 * This will use the repository to fetch the photos depending on the search criteria.
 * This is using rx binding.
 */
class UnsplashPickerViewModel constructor(private val repository: Repository) : BaseViewModel() {

    private val mPhotosLiveData = MutableLiveData<PagedList<UnsplashPhoto>>()
    val photosLiveData: LiveData<PagedList<UnsplashPhoto>> get() = mPhotosLiveData

    private val mOpenDrawer = MutableLiveData<Boolean>()
    var openDrawer: LiveData<Boolean> = mOpenDrawer

    override fun getTag(): String {
        return UnsplashPickerViewModel::class.java.simpleName
    }

    /**
     * Binds the edit text using rx binding to listen to text change.
     *
     * @param editText the edit text to listen to
     */
    fun bindSearch(editText: EditText) {
        RxTextView.textChanges(editText)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                mLoadingLiveData.postValue(true)
            }
            .observeOn(Schedulers.io())
            .switchMap { text ->
                if (TextUtils.isEmpty(text)) repository.loadPhotos(UnsplashPhotoPicker.getPageSize())
                else repository.searchPhotos(text.toString(), UnsplashPhotoPicker.getPageSize())
            }
            .subscribe(object : BaseObserver<PagedList<UnsplashPhoto>>() {
                override fun onSuccess(data: PagedList<UnsplashPhoto>?) {
                    mPhotosLiveData.postValue(data)
                }
            })
    }

    /**
     * To abide by the API guidelines,
     * you need to trigger a GET request to this endpoint every time your application performs a download of a photo
     *
     * @param photos the list of selected photos
     */
    fun trackDownloads(photos: ArrayList<UnsplashPhoto>) {
        for (photo in photos) {
            repository.trackDownload(photo.links.download_location)
        }
    }

    /**
     * To abide by the API guidelines,
     * you need to trigger a GET request to this endpoint every time your application performs a download of a photo
     *
     * @param photo photos
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    fun download(photo: UnsplashPhoto) {
        repository.trackDownload(photo.links.download_location)
        doAsync {
//            DownloadMain.start(photo.urls.raw)
            ConcurrentDownLoad
                .builder()
                // 设置URL
                .setUrl(photo.urls.raw)
                // 设置线程每次请求的块大小 (5M)
                .setBlockSize(1024L * 5)
                // 设置线程数量
                .setThreadCount(5)
                // 设置保存路径
                .setPath(Environment.getExternalStorageDirectory().absolutePath +
                        "/Download/picture" + photo.user.name + photo.description + ".jpg")
                // 设置存在是否删除(如果设置 setKeepOnIfDisconnect(true) 则失效)
                .setDeleteIfExist(true)
                // 是否支持断点下载
                .setKeepOnIfDisconnect(true)
                // 创建
                .build()
                // 开始
                .start()

        }
    }

    fun openDrawer(){
        mOpenDrawer.value = true
    }
}
