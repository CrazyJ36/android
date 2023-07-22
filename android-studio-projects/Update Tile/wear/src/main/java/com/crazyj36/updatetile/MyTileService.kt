package com.crazyj36.updatetile

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.viewinterop.AndroidView
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
const val RESOURCES_VERSION = "1"

@OptIn(ExperimentalHorologistApi::class)
class MyTileService: SuspendingTileService() {

    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        super.onTileEnterEvent(requestParams)

    }
    override fun onTileLeaveEvent(requestParams: EventBuilders.TileLeaveEvent) {
        super.onTileLeaveEvent(requestParams)
    }
    override fun onTileAddEvent(requestParams: EventBuilders.TileAddEvent) {
        super.onTileAddEvent(requestParams)
    }
    override fun onTileRemoveEvent(requestParams: EventBuilders.TileRemoveEvent) {
        super.onTileRemoveEvent(requestParams)
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
        val button = CompactChip.Builder(
            this@MyTileService,
            "",
            Clickable.Builder()
                .setId("buttonId")
                .setOnClick(LoadAction.Builder()
                    .build()
                ).build(),
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
            ).build()
        when (requestParams.currentState.lastClickableId) {
            "buttonId" -> {
                Log.d("UPDATETILE", "buttonId clicked")
                // toast creates an accessibilityevent
                Toast.makeText(this@MyTileService, "button clicked", Toast.LENGTH_SHORT).show()
            }
            "boxId" -> {
                Log.d("UPDATETILE", "boxId clicked")
                Toast.makeText(this@MyTileService, "box clicked", Toast.LENGTH_SHORT).show()
            }
        }
        val timeline = TimelineBuilders.Timeline.Builder()
        timeline.addTimelineEntry(
            TimelineBuilders.TimelineEntry.fromLayoutElement(box)
        )
        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(timeline.build())
        return tile.build()
    }

    companion object {
        var count = 0
    }
}

