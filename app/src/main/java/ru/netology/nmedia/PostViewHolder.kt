package ru.netology.nmedia

import android.widget.PopupMenu
import ru.netology.nmedia.databinding.CardPostBinding
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class PostViewHolder (private val binding: CardPostBinding, private val interactiveCommands: PostInteractionCommands) : RecyclerView.ViewHolder(binding.root){

    fun bind (post: Post) = with(binding) {
        textViewAuthorName.text = post.author
        textViewAuthorDate.text = post.publishedDate
        textViewContent.text = post.content
        buttonFavorite.text = compactDecimalFormat(post.countLikes)
        buttonShare.text = compactDecimalFormat(post.countShare)
        buttonVisibility.text = compactDecimalFormat(post.countVisibility)

        //binding.buttonFavorite.setImageResource(if (post.likeByMe) R.drawable.ic_baseline_favorite_24_red else R.drawable.ic_baseline_favorite_border_24)
        buttonFavorite.isChecked =  post.likeByMe

        buttonFavorite.setOnClickListener { interactiveCommands.onLike(post) }
        buttonShare.setOnClickListener { interactiveCommands.onShare(post) }

        imageButtonMore.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.options_post)

                setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.remove_button -> {
                            interactiveCommands.onRemove(post)
                            true
                        }
                        R.id.edit_button -> {
                            interactiveCommands.onEditPost(post)
                            true
                        }
                        else -> false
                    }
                }

            }.show()
        }
    }

    private fun compactDecimalFormat(number: Int): String {
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')

        val numValue = number.toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3

        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(
                numValue / 10.0.pow((base * 3).toDouble())
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(numValue)
        }
    }

}
