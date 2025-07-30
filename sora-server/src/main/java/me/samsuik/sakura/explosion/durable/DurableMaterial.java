package me.samsuik.sakura.explosion.durable;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record DurableMaterial(int durability, float resistance) {
}
