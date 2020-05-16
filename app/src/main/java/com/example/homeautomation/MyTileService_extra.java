package com.example.homeautomation;


import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTileService_extra extends TileService {

    boolean isEnabled = false;
    Tile tile;

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        tile = getQsTile();
        final DatabaseReference switch_status_extra_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_extra");
        switch_status_extra_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")){
                    isEnabled = true;
                    tile.setState(Tile.STATE_ACTIVE);
                    tile.updateTile();
                }
                else{
                    isEnabled = false;
                    tile.setState(Tile.STATE_INACTIVE);
                    tile.updateTile();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        tile = getQsTile();
        final DatabaseReference switch_status_extra_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_extra");
        switch_status_extra_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")){
                    isEnabled = true;
                    tile.setState(Tile.STATE_ACTIVE);
                    tile.updateTile();
                }
                else{
                    isEnabled = false;
                    tile.setState(Tile.STATE_INACTIVE);
                    tile.updateTile();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onClick() {
        tile = getQsTile();
        final DatabaseReference switch_status_extra_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_extra");
        if (isEnabled){
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
            switch_status_extra_firebase.setValue("OFF");
            isEnabled = false;
        }
        else{
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
            switch_status_extra_firebase.setValue("ON");
            isEnabled = true;
        }
    }
}