package com.githubfollow.model

import com.google.gson.annotations.SerializedName

class GithubUser {

    @SerializedName("id") var id: String? = null
    @SerializedName("login") var userName: String? = null
    @SerializedName("avatar_url") var avatarUrl: String? = null
    @SerializedName("html_url") var profileUrl: String? = null
}
