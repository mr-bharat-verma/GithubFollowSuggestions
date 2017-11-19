package com.githubfollow

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.githubfollow.databinding.ActivityFollowSuggestionsBinding
import com.githubfollow.databinding.ItemUserBinding
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
        activityBinding.dataPresent = true
        loadImage("https://cdn-images-1.medium.com/max/800/1*uHTSBqrRff8xv3UcNL82eQ.jpeg", activityBinding.placeHolder)

        activityBinding.item1?.run {
            btnCancel.setOnClickListener {
                removeRow(activityBinding.item1!!)
            }
        }

        activityBinding.item2?.run {
            btnCancel.setOnClickListener {
                removeRow(activityBinding.item2!!)
            }
        }

        activityBinding.item3?.run {
            btnCancel.setOnClickListener {
                removeRow(activityBinding.item3!!)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        GithubClient().getUsers(100)
                .take(3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { users ->
                    activityBinding.dataPresent = true
                    setRow(activityBinding.item1!!, users[0])
                    setRow(activityBinding.item2!!, users[1])
                    setRow(activityBinding.item3!!, users[2])
                }
    }

    private fun setRow(userBinding: ItemUserBinding, user: GithubUser) {
        userBinding.run {
            hasData = true
            loadImage(user.avatarUrl!!, imageUser)
            tvUsername.text = user.userName
            val strBuilder = SpannableStringBuilder(Html.fromHtml(getString(R.string.follow_link, user.profileUrl)))
            tvFollow.text = strBuilder
            tvFollow.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun removeRow(userBinding: ItemUserBinding) {
        userBinding.hasData = false
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
