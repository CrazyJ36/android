package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicString
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.protolayout.material.Text
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

const val RESOURCES_VERSION = "1"

class MyTileService: TileService() {

    /*override fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
        return ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .addIdToImageMapping(
                "button_icon", ResourceBuilders.ImageResource.Builder()
                    .setAndroidResourceByResId(
                        ResourceBuilders.AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.button_icon)
                            .build()
                    )
                    .build()
            )
            .build()
    }*/

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) =
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Futures.immediateFuture(
                TileBuilders.Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.fromLayoutElement(
                            Text.Builder(this, "permission")
                                .build()
                        )
                    ).build()
            )
        } else {
            Futures.immediateFuture(
                TileBuilders.Tile.Builder()
                    .setResourcesVersion(RESOURCES_VERSION)
                    .setTileTimeline(
                        TimelineBuilders.Timeline.fromLayoutElement(
                            Text.Builder(
                                this,
                                TypeBuilders.StringProp.Builder("--")
                                    .setDynamicValue(
                                        DynamicString.constant("bpm: ")
                                            .concat(PlatformHealthSources.heartRateBpm().format())
                                    )
                                    .build(),
                                StringLayoutConstraint.Builder("000").build()
                            ).build()
                        )
                    ).build()
            )
        }
}

