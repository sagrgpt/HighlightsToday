package com.showcase.highlightstoday.ui.topHeadlines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.showcase.highlightstoday.Article
import com.showcase.highlightstoday.R

class ArticleAdapter(
    private val context: Context,
    private val lastItemReachedTrigger: () -> Unit,
    private val clickListener: (ClickEvent) -> Unit
) : ListAdapter<Article, ArticleViewHolder>(DiffCallback())  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(context, parent.inflate(R.layout.article_layout), clickListener)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        if(position == itemCount-1)
            lastItemReachedTrigger()
        holder.bindTo(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
                && oldItem.publishedAt == newItem.publishedAt
                && oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
                && oldItem.publishedAt == newItem.publishedAt
                && oldItem.category == newItem.category
        }
    }

}

private fun ViewGroup.inflate(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
