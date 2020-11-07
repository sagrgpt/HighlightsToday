package com.showcase.highlightstoday.ui.topHeadlines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.showcase.highlightstoday.Article
import com.showcase.highlightstoday.R
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(
    private val context: Context,
    private val lastItemReachedTrigger: () -> Unit
) : ListAdapter<Article, ArticleViewHolder>(DiffCallback())  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(context, parent.inflate(R.layout.article_layout))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        if(position == itemCount-1)
            lastItemReachedTrigger()
        holder.bindTo(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title && oldItem.publishedAt == newItem.publishedAt
        }
    }

}

private fun ViewGroup.inflate(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
