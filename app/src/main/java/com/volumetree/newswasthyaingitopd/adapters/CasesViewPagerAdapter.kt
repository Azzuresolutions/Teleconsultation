package com.volumetree.newswasthyaingitopd.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CasesViewPagerAdapter(
    fa: FragmentActivity,
    private var fragments: MutableList<Fragment>
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun add(index: Int, fragment: Fragment) {
        fragments.add(index, fragment)
        notifyItemChanged(index)
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.find { it.hashCode().toLong() == itemId } != null
    }
}
