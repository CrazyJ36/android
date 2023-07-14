package com.crazyj36.wearphonesynctile

import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Text
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ActionBuilders.LaunchAction
import androidx.wear.protolayout.ActionBuilders.LoadAction
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Layout
import androidx.wear.protolayout.ModifiersBuilders

import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ModifiersBuilders.Modifiers
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.ResourceBuilders.ImageResource
import androidx.wear.protolayout.TimelineBuilders.Timeline
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.EventBuilders
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import com.google.common.io.Resources

private const val RESOURCES_VERSION = "0"
private val messagePath = "/messagepath"

@OptIn(ExperimentalHorologistApi::class)
class MyTileService : SuspendingTileService(), OnDataChangedListener {


    override suspend fun resourcesRequest(requestParams: ResourcesRequest):
        ResourceBuilders.Resources = ResourceBuilders.Resources.Builder()
        .setVersion(RESOURCES_VERSION)
        .addIdToImageMapping(
            "button_icon",
        ImageResource.Builder()
            .setAndroidResourceByResId(
                ResourceBuilders.AndroidImageResourceByResId.Builder()
                .setResourceId(R.drawable.button_icon)
                    .build())
            .build())
        .build()


    override suspend fun tileRequest(requestParams: TileRequest): Tile {
        val deviceParameters = requestParams.deviceConfiguration
        val text1: Text = Text.Builder(this@MyTileService, message).build()

        val button = CompactChip.Builder(
            this@MyTileService, "",
            Clickable.Builder().setId("buttonId").setOnClick(LoadAction.Builder().build()).build(),
            deviceParameters)
            .setIconContent("button_icon")
            .build()

        when(requestParams.currentState.lastClickableId) {
            "buttonId" -> Toast.makeText(this@MyTileService, "toast", Toast.LENGTH_SHORT).show()
        }
        val primaryLayout = PrimaryLayout.Builder(
            deviceParameters)
            .setPrimaryLabelTextContent(text1)
            .setPrimaryChipContent(button)
            .build()
        val box: LayoutElementBuilders.Box = LayoutElementBuilders.Box.Builder()
            .setVerticalAlignment(LayoutElementBuilders.VERTICAL_ALIGN_CENTER)
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .addContent(primaryLayout).build()
        //val newButton = Button.fromLayoutElement(box.contents[2])
        //newButton.let { Toast.makeText(applicationContext, "toast", Toast.LENGTH_SHORT).show() }
        return Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                Timeline.Builder().addTimelineEntry(
                    TimelineBuilders.TimelineEntry.Builder().setLayout(
                        Layout.Builder().setRoot(
                            box
                        ).build()
                    ).build()
                ).build()
            ).build()
    }


    override fun onTileEnterEvent(requestParams: EventBuilders.TileEnterEvent) {
        super.onTileEnterEvent(requestParams)
        Wearable.getDataClient(this).addListener(this)
    }
    override fun onTileLeaveEvent(requestParams: EventBuilders.TileLeaveEvent) {
        super.onTileLeaveEvent(requestParams)
        Wearable.getDataClient(this).removeListener(this)
    }


    override fun onDataChanged(dataEventBuffer: DataEventBuffer) {
        dataEventBuffer.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED) {
                event.dataItem.also { dataItem ->
                    if (dataItem.uri.path?.compareTo(messagePath) == 0) {
                        val dataMap: DataMap = DataMapItem.fromDataItem(dataItem).dataMap
                        // setting message or count here doesn't seem to get back to onTileRequest
                        message = dataMap.getInt("message").toString()
                        LoadAction.Builder()
                        Toast.makeText(this@MyTileService, "message: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        private var message: String = "message"
    }
}