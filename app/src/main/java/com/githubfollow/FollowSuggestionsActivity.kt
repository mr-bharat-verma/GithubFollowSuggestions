package com.githubfollow

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.githubfollow.databinding.ActivityFollowSuggestionsBinding
import com.githubfollow.databinding.ItemGithubUserBinding
import com.githubfollow.github.GithubClient
import com.githubfollow.model.GithubUser
import io.reactivex.android.schedulers.AndroidSchedulers

class FollowSuggestionsActivity : AppCompatActivity() {

    private val activityBinding: ActivityFollowSuggestionsBinding by lazy {
        DataBindingUtil.setContentView<ActivityFollowSuggestionsBinding>(this,
                R.layout.activity_follow_suggestions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding.dataPresent = false
        loadImage("https://cdn-images-1.medium.com/max/800/1*uHTSBqrRff8xv3UcNL82eQ.jpeg", activityBinding.placeHolder)
    }


    override fun onResume() {
        super.onResume()
        GithubClient().getUsers(100)
                .take(3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { users ->
                    activityBinding.dataPresent = true
                    setData(activityBinding.item1!!, users[0])
                    setData(activityBinding.item2!!, users[1])
                    setData(activityBinding.item3!!, users[2])
                }
    }

    private fun setData(item1: ItemGithubUserBinding, user: GithubUser) {
        item1.run {
            loadImage(user.avatarUrl!!, imageUser)
            tvUsername.text = user.userName
            val strBuilder = SpannableStringBuilder(Html.fromHtml(getString(R.string.follow_link, user.profileUrl)))
            tvFollow.text = strBuilder
            tvFollow.movementMethod = LinkMovementMethod.getInstance()
        }
    }


    fun loadImage(url: String, ivImg: ImageView) {
        Glide.with(this)
                .load(url)
                .override(60, 60)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(android.R.drawable.stat_notify_error)
                .into(ivImg)
    }
}
