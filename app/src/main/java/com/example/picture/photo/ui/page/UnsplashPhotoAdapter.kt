package com.example.picture.photo.ui.page

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.picture.R
import com.example.picture.base.utils.SPUtils
import com.example.picture.main.data.MyDatabase
import com.example.picture.main.data.bean.Download
import com.example.picture.main.data.repository.DownloadRepository
import com.example.picture.main.ui.MainActivity
import com.example.picture.photo.data.UnsplashPhoto
import com.example.picture.photo.ui.service.DownloadService
import com.example.picture.photo.ui.state.UnsplashPickerViewModel
import com.google.android.material.snackbar.Snackbar
import com.unsplash.pickerandroid.photopicker.presentation.AspectRatioImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_unsplash_photo.view.*
import org.jetbrains.anko.doAsync

/**
 * The photos recycler view adapter.
 * This is using the Android paging library to display an infinite list of photos.
 * This deals with either a single or multiple selection list.
 */
class UnsplashPhotoAdapter constructor(
    private val context: Context,
    private val isMultipleSelection: Boolean,
    private val viewModel: UnsplashPickerViewModel
) :
    PagedListAdapter<UnsplashPhoto, UnsplashPhotoAdapter.PhotoViewHolder>(COMPARATOR) {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    private val mSelectedIndexes = ArrayList<Int>()

    private val mSelectedImages = ArrayList<UnsplashPhoto>()

    private var mOnPhotoSelectedListener: OnPhotoSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(mLayoutInflater.inflate(R.layout.item_unsplash_photo, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        // item
        getItem(position)?.let { photo ->
            // image
            holder.imageView.aspectRatio = photo.height.toDouble() / photo.width.toDouble()
            holder.itemView.setBackgroundColor(Color.parseColor(photo.color))
            val requestOptions = RequestOptions()
                .error(ColorDrawable(Color.GRAY))
            Glide.with(context).load(photo.urls.regular).apply(requestOptions).into(holder.imageView)
            // photograph name
            holder.txtView.text = photo.user.name
            // click listener
            holder.itemView.setOnClickListener {
                // selected index(es) management
                if (mSelectedIndexes.contains(holder.adapterPosition)) {
                    mSelectedIndexes.remove(holder.adapterPosition)
                } else {
                    if (!isMultipleSelection) mSelectedIndexes.clear()
                    mSelectedIndexes.add(holder.adapterPosition)
                }
                if (isMultipleSelection) {
                    notifyDataSetChanged()
                }
                mOnPhotoSelectedListener?.onPhotoSelected(mSelectedIndexes.size)
                // change title text
            }
            holder.itemView.setOnLongClickListener {
                photo.urls.regular?.let {
                    mOnPhotoSelectedListener?.onPhotoLongPress(holder.imageView, it)
                }
                false
            }
            holder.download.setOnClickListener {
                viewModel.download(photo)
                val path = MainActivity.PICTURE_DOWNLOAD_PATH + photo.user.name + " " + if (photo.description == null) photo.id else photo.description + ".jpg"
                val downloadBean = Download()
                downloadBean.apply {
                    type = 1
                    link = photo.urls.raw!!
                    state = 1
                    this.path = path
                    userID = SPUtils.getInstance().getInt("user_id")
                    fileID = photo.id
                    createdTime = System.currentTimeMillis().toString()
                }
                val intent = Intent(context, DownloadService::class.java)
                intent.action = DownloadService.DOWNLOAD_FILE
                intent.putExtra("bean", downloadBean)
                DownloadRepository.get().add(downloadBean)
                context.startService(intent)
                (context as MainActivity).downloadBinder.startDownload(downloadBean, object: DownloadService.DownloadStatus{
                    override fun state(bean: Download, msg: String, total: Long, current: Long, speed: Long) {}

                    override fun finish(bean: Download) {
                        Snackbar.make(context.coordinator, "completed..", 2000).setAction("check") {
                            val intent2 = Intent(Intent.ACTION_VIEW)
                            val uri = Uri.parse(
                                "file://" + downloadBean.path
                            )
                            intent2.setDataAndType(uri, "image/*")
                            intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent2.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            intent2.setDataAndType(uri, "application/vnd.android.package-archive")
                            context.startActivity(intent2)
                        }.show()
                    }
                })
            }
        }
    }

    companion object {
        // diff util comparator
        val COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areContentsTheSame(
                oldItem: UnsplashPhoto,
                newItem: UnsplashPhoto
            ): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean =
                oldItem == newItem
        }
    }

    /**
     * UnsplashPhoto view holder.
     */
    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: AspectRatioImageView = view.item_unsplash_photo_image_view
        val txtView: TextView = view.item_unsplash_photo_text_view
        val download: FrameLayout = view.download
    }
}