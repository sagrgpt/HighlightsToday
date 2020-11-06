package com.showcase.highlightstoday.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.showcase.highlightstoday.Article
import com.showcase.highlightstoday.R
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(DiffCallback())  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(parent.inflate(R.layout.article_layout))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
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

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView = itemView.findViewById<ImageView>(R.id.newsImage)
        private val title = itemView.findViewById<TextView>(R.id.articleTitle)
        private val desc = itemView.findViewById<TextView>(R.id.articleDesc)
        private val source= itemView.findViewById<TextView>(R.id.articleSource)
        private val author = itemView.findViewById<TextView>(R.id.author)
        private val date = itemView.findViewById<TextView>(R.id.articleDate)
        private val favBtn = itemView.findViewById<ImageButton>(R.id.favouriteBtn)

        fun bindTo(article: Article) {
            title?.text = article.title
            article.description.takeUnless { it=="Unknown" }
                ?.also { setDesc(it) }
                ?: run { desc?.visibility = View.GONE }

            article.author.takeIf { it!="Unknown" }
                ?.also { setAuthor(it) }
                ?: run { author?.visibility = View.GONE }

            article.source.name.takeIf { it!="Unknown" }
                ?.also { setSource(it) }
                ?: run { source?.visibility = View.GONE }

            Calendar.getInstance()
                .apply { timeInMillis = article.publishedAt }
                .time
                .let { SimpleDateFormat("hh:mm a dd-MM-yyyy", Locale.ROOT).format(it) }
                .also { date?.text = it }
        }

        private fun setDesc(text: String) {
            desc?.visibility = View.VISIBLE
            desc?.text = text
        }

        private fun setAuthor(text: String) {
            author?.visibility = View.VISIBLE
            author?.text = text
        }

        private fun setSource(text: String) {
            source?.visibility = View.VISIBLE
            source?.text = text
        }

    }
}

private fun ViewGroup.inflate(
    @LayoutRes layoutRes: Int,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
