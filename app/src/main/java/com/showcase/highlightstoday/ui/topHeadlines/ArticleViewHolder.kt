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
    private val clickListener: (ClickEvent) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private lateinit var article: Article

    private val articleContainer = itemView.findViewById<MaterialCardView>(R.id.card)
    private val imageView = itemView.findViewById<ImageView>(R.id.newsImage)
    private val title = itemView.findViewById<TextView>(R.id.articleTitle)
    private val desc = itemView.findViewById<TextView>(R.id.articleDesc)
    private val source = itemView.findViewById<TextView>(R.id.articleSource)
    private val author = itemView.findViewById<TextView>(R.id.author)
    private val date = itemView.findViewById<TextView>(R.id.articleDate)
    private val favBtn = itemView.findViewById<ImageButton>(R.id.favouriteBtn)

    fun bindTo(article: Article) {
        this.article = article
        setImage()
        setTitle()
        setDesc()
        setAuthor()
        setSource()
        setDate()
        setFavBtn()
        setClickListener()
    }

    private fun setImage() {
        Glide.with(context)
            .load(article.urlToImage)
            .placeholder(ContextCompat.getDrawable(context, R.drawable.gradient))
            .into(imageView)
    }

    private fun setTitle() {
        title?.text = article.title
    }

    private fun setDesc() {
        article.description.takeUnless { it == "Unknown" }
            ?.also {
                desc?.visibility = View.VISIBLE
                desc?.text = it
            }
            ?: run { desc?.visibility = View.GONE }
    }

    private fun setAuthor() {
        (article.author.takeIf { it != "Unknown" }
            ?.also {
                author?.visibility = View.VISIBLE
                author?.text = it
            }
            ?: run { author?.visibility = View.GONE })
    }

    private fun setSource() {
        (article.source.name.takeIf { it != "Unknown" }
            ?.also {
                source?.visibility = View.VISIBLE
                source?.text = it
            }
            ?: run { source?.visibility = View.GONE })
    }

    private fun setDate() {
        Calendar.getInstance()
            .apply { timeInMillis = article.publishedAt }
            .time
            .let { SimpleDateFormat("hh:mm a dd-MM-yyyy", Locale.ROOT).format(it) }
            .also { date?.text = it }
    }

    private fun setFavBtn() {
        if (article.isFavourite)
            favBtn.setImageResource(R.drawable.ic_favorite_selected)
        else
            favBtn.setImageResource(R.drawable.ic_favorite_unselected)
    }


    private fun setClickListener() {
        articleContainer.setOnClickListener {
            clickListener(ClickEvent.CardClicked(article.url))
        }

        favBtn.setOnClickListener {
            if (article.isFavourite)
                clickListener(ClickEvent.AddToFavourite(article))
            else
                clickListener(ClickEvent.AddToFavourite(article))
        }
    }

    fun updateFavourite(favourite: Boolean) {
        article = article.copy(isFavourite = favourite)
        setFavBtn()
    }

}