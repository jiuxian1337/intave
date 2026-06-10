package de.jpx3.intave.packet.reader;

import com.comphenix.protocol.events.PacketContainer;

public abstract class AbstractPacketReader implements PacketReader {
  private PacketContainer packet;

  @Override
  public void enter(PacketContainer packet) {
    if (this.packet != null) {
      release();
    }
    this.packet = packet;
  }

  @Override
  public void flush() {
  }

  @Override
  public void release() {
    packet = null;
  }

  @Override
  public void releaseSafe() {
    if (packet == null) {
      return;
    }
    release();
  }

  public PacketContainer packet() {
    return packet;
  }
}
