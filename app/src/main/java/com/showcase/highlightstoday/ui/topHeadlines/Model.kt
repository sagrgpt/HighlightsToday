package com.showcase.highlightstoday.ui.topHeadlines

import com.showcase.highlightstoday.Article

sealed class ViewEffects {
    object RefreshCompleted : ViewEffects()
    object NetworkError: ViewEffects()
    object ClearCacheDialog: ViewEffects()
    data class OpenNewsDetails(
        val newsUrl: String
    ) : ViewEffects()
}

sealed class ClickEvent {
    data class CardClicked(
        val articleLink: String
    ) : ClickEvent()

    data class AddToFavourite(
        val article: Article
    ) : ClickEvent()

    data class RemoteFavourite(
        val article: Article
    ) : ClickEvent()

    object ToggleFavouriteFilter : ClickEvent()

    object ClearCache : ClickEvent()

    object HardReset: ClickEvent()
}