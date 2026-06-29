package com.joon.polapola.presentation.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import platform.Foundation.NSData
import platform.Foundation.NSItemProvider
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual class ImagePickerLauncher private constructor(
    private val pickerDelegate: ImagePickerDelegate,
) {
    internal constructor(onImagesPicked: (List<PickedImage>) -> Unit) : this(
        pickerDelegate = ImagePickerDelegate(onImagesPicked),
    )

    actual fun launchGallery() {
        pickerDelegate.presentGallery()
    }

    actual fun launchCamera() {
        pickerDelegate.presentCamera()
    }
}

@Composable
actual fun rememberImagePickerLauncher(onImagesPicked: (List<PickedImage>) -> Unit): ImagePickerLauncher =
    remember(onImagesPicked) {
        ImagePickerLauncher(onImagesPicked)
    }

private class ImagePickerDelegate(
    private val onImagesPicked: (List<PickedImage>) -> Unit,
) : NSObject(),
    PHPickerViewControllerDelegateProtocol,
    UIImagePickerControllerDelegateProtocol,
    UINavigationControllerDelegateProtocol {
    private var pickerViewController: PHPickerViewController? = null
    private var cameraViewController: UIImagePickerController? = null

    fun presentGallery() {
        val configuration =
            PHPickerConfiguration().apply {
                filter = PHPickerFilter.imagesFilter()
                selectionLimit = MAX_IMAGE_PICK_COUNT.toLong()
            }
        val picker = PHPickerViewController(configuration)
        picker.delegate = this
        pickerViewController = picker

        UIApplication
            .sharedApplication
            .topMostViewController()
            ?.presentViewController(picker, animated = true, completion = null)
    }

    fun presentCamera() {
        val cameraSourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera

        if (!UIImagePickerController.isSourceTypeAvailable(cameraSourceType)) return

        val camera = UIImagePickerController()
        camera.sourceType = cameraSourceType
        camera.delegate = this
        cameraViewController = camera

        UIApplication
            .sharedApplication
            .topMostViewController()
            ?.presentViewController(camera, animated = true, completion = null)
    }

    override fun picker(
        picker: PHPickerViewController,
        didFinishPicking: List<*>,
    ) {
        picker.dismissViewControllerAnimated(true, completion = null)

        val results = didFinishPicking.mapNotNull { result -> result as? PHPickerResult }
        if (results.isEmpty()) return

        val pickedImages = mutableListOf<PickedImage>()
        var remainingCount = results.size

        results.forEach { result ->
            result.itemProvider.loadImageData { pickedImage ->
                dispatch_async(dispatch_get_main_queue()) {
                    if (pickedImage != null) {
                        pickedImages += pickedImage
                    }
                    remainingCount -= 1
                    if (remainingCount == 0 && pickedImages.isNotEmpty()) {
                        onImagesPicked(pickedImages)
                    }
                }
            }
        }
    }

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>,
    ) {
        picker.dismissViewControllerAnimated(true, completion = null)

        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage ?: return
        image
            .toJpegPickedImage()
            ?.let { pickedImage -> onImagesPicked(listOf(pickedImage)) }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSItemProvider.loadImageData(onImagePicked: (PickedImage?) -> Unit) {
    loadDataRepresentationForTypeIdentifier("public.image") { data, _ ->
        val pickedImage = data?.toPickedImage(suggestedName)

        onImagePicked(pickedImage)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.toJpegPickedImage(): PickedImage? {
    val data = UIImageJPEGRepresentation(this, 0.92) ?: return null

    return data.toPickedImage(
        fileName = "camera-photo.jpg",
        mimeType = "image/jpeg",
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toPickedImage(fileName: String?): PickedImage? =
    toPickedImage(
        fileName = fileName,
        mimeType = null,
    )

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toPickedImage(
    fileName: String?,
    mimeType: String?,
): PickedImage? {
    val bytesPointer = bytes ?: return null

    return PickedImage(
        bytes = bytesPointer.readBytes(length.toInt()),
        fileName = fileName,
        mimeType = mimeType,
    )
}

private fun UIApplication.topMostViewController(): UIViewController? =
    keyWindow
        ?.rootViewController
        ?.topMostViewController()

private fun UIViewController.topMostViewController(): UIViewController =
    when (this) {
        is UINavigationController -> visibleViewController?.topMostViewController() ?: this
        is UITabBarController -> selectedViewController?.topMostViewController() ?: this
        else -> presentedViewController?.topMostViewController() ?: this
    }

private const val MAX_IMAGE_PICK_COUNT = 10
