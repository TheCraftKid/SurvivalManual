package org.ligi.survivalmanual.ui

import android.content.Context
import android.support.design.widget.NavigationView
import android.util.AttributeSet
import android.view.Menu
import org.ligi.survivalmanual.model.NavigationEntryMap
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.model.VisitedURLStore

class MyNavigationView(context: Context, attrs: AttributeSet) : NavigationView(context, attrs) {

    init {
        refresh()
    }

    fun refresh() {
        menu.clear()

        val listedItems = NavigationEntryMap.filter { it.entry.isListed }

        listedItems.filter { !it.entry.isAppendix }.forEach {
            menu.add(0, it.id, Menu.NONE, it.entry.titleRes.asStringWithMarkingWhenRead(it.entry.url)).apply {
                it.entry.iconRes?.let { setIcon(it) }
            }
        }

        val submenu = menu.addSubMenu("Appendix")

        listedItems.filter { it.entry.isAppendix }.forEach {
            submenu.add(0, it.id, Menu.NONE, it.entry.titleRes.asStringWithMarkingWhenRead(it.entry.url))
        }
    }

    fun Int.asStringWithMarkingWhenRead(url: String) = context.getString(this).appendIf(State.markVisited() && VisitedURLStore.getAll().contains(url), "👁")

    fun String.appendIf(bool: Boolean, suffix: String) = if (bool) this + suffix else this
}
