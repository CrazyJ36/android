package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.app.ActivityCompat
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.TypeBuilders.Int32Prop
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicInstant
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicString
import androidx.wear.protolayout.expression.DynamicDataKey
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.protolayout.expression.proto.DynamicProto
import androidx.wear.protolayout.expression.proto.DynamicProto.DynamicInt32
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.protolayout.material.Text
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService

const val RESOURCES_VERSION = "1"

@OptIn(ExperimentalHorologistApi::class)
class MyTileService: SuspendingTileService() {

    companion object {
        var count = 0
    }

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        val systemTime = DynamicInstant.platformTimeWithSecondsPrecision().toDynamicInstantByteArray()
        when (requestParams.currentState.lastClickableId) {
            "buttonId" -> {
            }
            "boxId" -> {
            }
        }
        val text1: Text
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            text1 =
                Text.Builder(
                    this,
                    TypeBuilders.StringProp.Builder("--")
                        .setDynamicValue(
                            DynamicBuilders.DynamicString.constant("bpm: ")
                                .concat(PlatformHealthSources.heartRateBpm().format())
                        )
                        .build(),
                    StringLayoutConstraint.Builder("000").build()
                ).setMaxLines(3).build()
        } else {
            text1 = Text.Builder(this, "need permission").build()
        }
        val button = CompactChip.Builder(
            this@MyTileService,
            "",
            Clickable.Builder()
                .setId("buttonId")
                .setOnClick(
                    LoadAction.Builder()
                        .build()
                ).build(),
            requestParams.deviceConfiguration
        ).setIconContent("button_icon").build()
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
                    .setClickable(
                        Clickable.Builder()
                            .setId("boxId")
                            .setOnClick(
                                LoadAction.Builder()
                                    .build()
                            ).build()
                    ).build()
            ).build()
        val timeline = TimelineBuilders.Timeline.Builder()
        timeline.addTimelineEntry(
            TimelineBuilders.TimelineEntry.fromLayoutElement(box)
        )
        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(timeline.build())
        return tile.build()
    }

    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
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
    }
}

