package gigaherz.util.nbt.serialization.mappers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public abstract class MapperBase
{
    int priority;

    public int getPriority()
    {
        return priority;
    }

    public MapperBase(int priority)
    {
        this.priority = priority;
    }

    protected CompoundNBT getTypeCompound(Object o, String type)
    {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("type", type);
        tag.putString("className", o.getClass().getName());
        return tag;
    }

    protected CompoundNBT wrapToCompound(INBT tag, Object o)
    {
        if (tag instanceof CompoundNBT)
            return (CompoundNBT)tag;
        CompoundNBT tag2 = getTypeCompound(o, "custom");
        tag2.get("data");
        return tag2;
    }

    public abstract boolean canMapToField(Class<?> clazz);

    public abstract boolean canMapToCompound(Class<?> clazz);

    public abstract void serializeField(CompoundNBT parent, String fieldName, Object object) throws ReflectiveOperationException;

    public abstract Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz) throws ReflectiveOperationException;

    public abstract CompoundNBT serializeCompound(Object object) throws ReflectiveOperationException;

    public abstract Object deserializeCompound(CompoundNBT self, Class<?> clazz) throws ReflectiveOperationException;
}
