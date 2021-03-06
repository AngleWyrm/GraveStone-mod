package GraveStone.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 *
 */
public abstract class TileEntityGSGrave extends TileEntity {

    protected GSGraveStoneItems gSItems;
    protected GSGraveStoneDeathText gSDeathText;
    protected boolean isEditable = true;
    protected byte graveType = 0;
    protected int age = -1;

    public TileEntityGSGrave() {
        gSDeathText = new GSGraveStoneDeathText(this);
    }
    
    public void setGraveType(byte graveType) {
        this.graveType = graveType;
    }

    public byte getGraveType() {
        return graveType;
    }

    protected void readType(NBTTagCompound nbtTag) {
        if (nbtTag.hasKey("GraveType")) {
            graveType = nbtTag.getByte("GraveType");
        }
    }

    protected void saveType(NBTTagCompound nbtTag) {
        nbtTag.setByte("GraveType", graveType);
    }

    public void setGraveContent(Random random) {
        gSDeathText.setRandomDeathText(random, graveType, false);
        gSItems.setRandomGraveContent(random);
        setRandomAge();
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        gSItems.setInventorySlotContents(slot, itemStack);
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName() {
        return "container.gravestone";
    }

    public void setItems(ItemStack[] items) {
        gSItems.setItems(items);
    }

    public void dropAllItems() {
        gSItems.dropAllItems();
    }

    public String getDeathText() {
        return gSDeathText.getDeathText();
    }

    public void setDeathText(String text) {
        gSDeathText.setDeathText(text);
        
        this.onInventoryChanged();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    protected void setRandomAge() {
        age = 10 + (new Random()).nextInt(100);
}
    public boolean isEditable() {
        return isEditable;
    }

    @SideOnly(Side.CLIENT)
    /**
     * Sets the grave's isEditable flag to the specified parameter.
     */
    public void setEditable(boolean par1) {
        isEditable = par1;
    }

    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        readFromNBT(packet.customParam1);
    }
}
