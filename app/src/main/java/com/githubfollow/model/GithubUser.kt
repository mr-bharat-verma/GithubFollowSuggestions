package com.githubfollow.model

import com.google.gson.annotations.SerializedName

class GithubUser {

    var id: String? = null

    @SerializedName("avatar_url")
    var avatarUrl: String? = null


}