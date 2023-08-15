package com.crazyj36.updatetile

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.TypeBuilders
import androidx.wear.protolayout.TypeBuilders.StringLayoutConstraint
import androidx.wear.protolayout.TypeBuilders.StringProp
import androidx.wear.protolayout.expression.AppDataKey
import androidx.wear.protolayout.expression.DynamicBuilders
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicInstant
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicInt32
import androidx.wear.protolayout.expression.DynamicBuilders.DynamicString
import androidx.wear.protolayout.expression.DynamicBuilders.dynamicStringFromProto
import androidx.wear.protolayout.expression.DynamicDataBuilders
import androidx.wear.protolayout.expression.PlatformHealthSources
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.protolayout.material.Text
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.StateBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import kotlinx.coroutines.delay

const val RESOURCES_VERSION = "1"

@OptIn(ExperimentalHorologistApi::class)
class MyTileService: SuspendingTileService() {

    companion object {
        var count = 0
    }

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        val systemTime = DynamicInstant.platformTimeWithSecondsPrecision()

        when (requestParams.currentState.lastClickableId) {
            "buttonId" -> {
            }
            "boxId" -> {
            }
        }
        var text1: Text
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            text1 =
                Text.Builder(
                    this,
                    TypeBuilders.StringProp.Builder("--")
                        .setDynamicValue(
                            PlatformHealthSources.dailySteps()
                                .format()
                        ).build(),
                    StringLayoutConstraint.Builder("000").build()
                ).build()
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

