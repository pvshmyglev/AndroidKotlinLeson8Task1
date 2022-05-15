package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.util.showKeyboard

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val adapter =  PostAdapter (viewModel)

        binding.listOfPosts.adapter = adapter

        viewModel.data.observe(this) { postsList ->

            adapter.submitList(postsList)

        }

        binding.buttonNewPost.setOnClickListener {

            with(binding.textNewPost) {

                viewModel.onSaveContent(text.toString())

                clearFocus()
                hideKeyboard()

            }

        }

        binding.buttonCancelEdit.setOnClickListener {

            with(binding.textNewPost) {

                viewModel.onCancelEdit()

                clearFocus()
                hideKeyboard()

            }

        }

        viewModel.editedPost.observe(this) {post ->

           with(binding.textNewPost) {

               val thisNewPost = post.id != 0

               if (thisNewPost) {
                   requestFocus()
                   showKeyboard()
                   binding.groupCancelEditPost.visibility = View.VISIBLE
               } else {
                   binding.groupCancelEditPost.visibility = View.GONE
               }

               setText(post.content)

           }

            binding.textViewAuthorName.text = post.author
            binding.textViewAuthorDate.text = post.publishedDate

        }

    }

}