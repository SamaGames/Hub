package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class VirtualPlayer {

	protected final Player player;
	protected final UUID uuid;

	public VirtualPlayer(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
	}

	public Player getPlayer() {
		return player;
	}

	public UUID getUuid() {
		return uuid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VirtualPlayer))
			return false;

		VirtualPlayer that = (VirtualPlayer) o;

		return ! (uuid != null ? ! uuid.equals(that.uuid) : that.uuid != null);
	}
}
