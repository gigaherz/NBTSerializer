package gigaherz.util.nbt.serialization.mappers;

import net.minecraft.nbt.CompoundNBT;

public class StringMapper extends MapperBase
{
    public StringMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return clazz == String.class;
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return false;
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object) throws ReflectiveOperationException
    {
        parent.putString(fieldName, (String) object);
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz) throws ReflectiveOperationException
    {
        return parent.getString(fieldName);
    }

    @Override
    public CompoundNBT serializeCompound(Object object) throws ReflectiveOperationException
    {
        return null;
    }

    @Override
    public Object deserializeCompound(CompoundNBT self, Class<?> clazz) throws ReflectiveOperationException
    {
        return null;
    }
}
