package gigaherz.util.nbt.serialization.mappers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

public class CustomSerializableMapper extends MapperBase
{
    public CustomSerializableMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return INBTSerializable.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return INBTSerializable.class.isAssignableFrom(clazz);
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object) throws ReflectiveOperationException
    {
        parent.put(fieldName, ((INBTSerializable) object).serializeNBT());
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz) throws ReflectiveOperationException
    {
        INBT tag2 = parent.get(fieldName);
        INBTSerializable o = (INBTSerializable) clazz.newInstance();
        o.deserializeNBT(tag2);
        return o;
    }

    @Override
    public CompoundNBT serializeCompound(Object object) throws ReflectiveOperationException
    {
        return wrapToCompound(((INBTSerializable) object).serializeNBT(), object);
    }

    @Override
    public Object deserializeCompound(CompoundNBT self, Class<?> clazz) throws ReflectiveOperationException
    {
        INBTSerializable o = (INBTSerializable) clazz.newInstance();
        o.deserializeNBT(self);
        return o;
    }
}
