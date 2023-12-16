package ttbrowne.c323.finalandroidproject

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

class AvatarPopup(
    private val onCameraPressed: () -> Unit,
    private val onGalleryPressed: () -> Unit
) : DialogFragment() {

    //popup for user to choose type of photo to get
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Choose Option")
            .setMessage("Enable access so you can take photos")
            .setNegativeButton("Choose from Gallery") { _, _ -> onGalleryPressed() }
            .setPositiveButton("Camera") { _, _ ->  onCameraPressed() }
            .create()

    companion object {
        const val TAG = "AvatarPopup"
    }
}