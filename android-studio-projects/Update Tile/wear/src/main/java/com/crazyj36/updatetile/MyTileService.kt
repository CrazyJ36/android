package com.crazyj36.updatetile

import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.widget.Toast
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.protolayout.material.CompactChip
import kotlinx.coroutines.delay

val RESOURCES_VERSION = "1"
var count = 0

@OptIn(ExperimentalHorologistApi::class)
class MyTileService: SuspendingTileService() {
    override fun onCreate() {
        super.onCreate()
        MyAccessibilityService().pressScreen()
    }
    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
        return ResourceBuilders.Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .addIdToImageMapping("button_icon", ResourceBuilders.ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(R.drawable.button_icon)
                        .build())
                .build())
            .build()
    }

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        when (requestParams.currentState.lastClickableId) {
            "buttonId" -> {
                for (i in 1..5) {
                    count++
                    delay(1000)
                    MyAccessibilityService().pressScreen()
                }

                Toast.makeText(this@MyTileService, "button clicked", Toast.LENGTH_SHORT).show()
            }
            "boxId" -> {
                Toast.makeText(this@MyTileService, "box clicked", Toast.LENGTH_SHORT).show()
            }
        }
        val button = CompactChip.Builder(
            this@MyTileService,
            "",
            Clickable.Builder().setId("buttonId").setOnClick(LoadAction.Builder().build()).build(),
            requestParams.deviceConfiguration
        ).setIconContent("button_icon").build()
        val text1 = LayoutElementBuilders.Text.Builder()
            .setText(count.toString())
            .build()
        val primaryLayout = PrimaryLayout.Builder(requestParams.deviceConfiguration)
            .setPrimaryLabelTextContent(text1)
            .setPrimaryChipContent(button)
            .build()
        val box: LayoutElementBuilders.Box = LayoutElementBuilders.Box.Builder()
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(primaryLayout)
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(Clickable.Builder()
                        .setId("boxId")
                        .setOnClick(LoadAction.Builder()
                            .build()
                        ).build()
                    ).build()
            )
            .build()
        val timeline = TimelineBuilders.Timeline.Builder()
        timeline.addTimelineEntry(
            TimelineBuilders.TimelineEntry.fromLayoutElement(box)
        )
        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(timeline.build())
        return tile.build()
    }

}

