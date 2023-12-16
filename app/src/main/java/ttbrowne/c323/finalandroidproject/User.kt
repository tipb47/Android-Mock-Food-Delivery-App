package ttbrowne.c323.finalandroidproject

import android.net.Uri

data class User(
    val email: String,
    val password: String,
    val name: String,
    var avatarUri: Uri? = Uri.parse("android.resource://ttbrowne.c323.finalandroidproject/${R.drawable.default_pfp}") //init as default pic
)