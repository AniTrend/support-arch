package co.anitrend.arch.extension.ext

import android.view.Menu
import android.view.MenuItem
import androidx.core.view.iterator


/**
 * Add [menu] items to [this] menu
 *
 * @param menu Menu containing items to add
 * @param group Group that [menu] belongs to
 * @param flagFor Delegate to dictate which action flags are applied
 */
inline fun Menu.addItems(
    menu: Menu,
    group: Int = Menu.NONE,
    flagFor: (MenuItem) -> Int = { MenuItem.SHOW_AS_ACTION_IF_ROOM }
) {
    menu.iterator().forEach { item ->
        add(group, item.itemId, item.order, item.title)
            .setIcon(item.icon)
            .setShowAsActionFlags(
                flagFor(item)
            )
    }
}

/**
 * Remove [menu] items from [this] menu
 *
 * @param menu Menu containing items to remove
 * @param group Group that [menu] belongs to
 */
fun Menu.removeItems(
    menu: Menu,
    group: Int = Menu.NONE
) {
    menu.iterator().forEach { item ->
        removeItem(item.itemId)
    }
    if (group != Menu.NONE)
        removeGroup(group)
}

/**
 * Change visibility of all items for [this] menu
 *
 * @param shouldShow Visibility
 * @param filter Predicate for which menu items to hid
 */
fun Menu.setVisibilityForAllItems(
    shouldShow: Boolean,
    filter: (MenuItem) -> Boolean
) {
    iterator().asSequence()
        .filter(filter)
        .forEach {
            it.isVisible = shouldShow
        }
}