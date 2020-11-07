package com.showcase.highlightstoday.ui.topHeadlines

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.showcase.highlightstoday.Article
import com.showcase.highlightstoday.R
import java.text.SimpleDateFormat
import java.util.*

class ArticleViewHolder(
    private val context: Context,
    itemView: View,
    private val clickListener: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val articleContainer = itemView.findViewById<MaterialCardView>(R.id.card)
    private val imageView = itemView.findViewById<ImageView>(R.id.newsImage)
    private val title = itemView.findViewById<TextView>(R.id.articleTitle)
    private val desc = itemView.findViewById<TextView>(R.id.articleDesc)
    private val source= itemView.findViewById<TextView>(R.id.articleSource)
    private val author = itemView.findViewById<TextView>(R.id.author)
    private val date = itemView.findViewById<TextView>(R.id.articleDate)
    private val favBtn = itemView.findViewById<ImageButton>(R.id.favouriteBtn)

    fun bindTo(article: Article) {
        setImage(article)
        setTitle(article)
        setDesc(article)
        setAuthor(article)
        setSource(article)
        setDate(article)
        setClickListener(article)
    }

    private fun setImage(article: Article) {
        Glide.with(context)
            .load(article.urlToImage)
            .placeholder(ContextCompat.getDrawable(context, R.drawable.gradient))
            .into(imageView)
    }

    private fun setTitle(article: Article) {
        title?.text = article.title
    }

    private fun setDesc(article: Article) {
        article.description.takeUnless { it=="Unknown" }
            ?.also {
                desc?.visibility = View.VISIBLE
                desc?.text = it
            }
            ?: run { desc?.visibility = View.GONE }
    }

    private fun setAuthor(article: Article) {
        (article.author.takeIf { it != "Unknown" }
            ?.also {
                author?.visibility = View.VISIBLE
                author?.text = it
            }
            ?: run { author?.visibility = View.GONE })
    }

    private fun setSource(article: Article) {
        (article.source.name.takeIf { it != "Unknown" }
            ?.also {
                source?.visibility = View.VISIBLE
                source?.text = it
            }
            ?: run { source?.visibility = View.GONE })
    }

    private fun setDate(article: Article) {
        Calendar.getInstance()
            .apply { timeInMillis = article.publishedAt }
            .time
            .let { SimpleDateFormat("hh:mm a dd-MM-yyyy", Locale.ROOT).format(it) }
            .also { date?.text = it }
    }

    private fun setClickListener(article: Article) {
        articleContainer.setOnClickListener { clickListener(article.url) }
    }

}