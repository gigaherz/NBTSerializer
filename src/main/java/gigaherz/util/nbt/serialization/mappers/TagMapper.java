package gigaherz.util.nbt.serialization.mappers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class TagMapper extends MapperBase
{
    public TagMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return INBT.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return INBT.class.isAssignableFrom(clazz);
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object) throws ReflectiveOperationException
    {
        parent.put(fieldName, ((INBT) object).copy());
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz) throws ReflectiveOperationException
    {
        return parent.get(fieldName).copy();
    }

    @Override
    public CompoundNBT serializeCompound(Object object) throws ReflectiveOperationException
    {
        INBT obj = (INBT)object;
        if (obj instanceof CompoundNBT)
            return (CompoundNBT)obj;

        CompoundNBT tag = new CompoundNBT();
        tag.putString("type", "compound");
        tag.put("value", obj);
        return tag;
    }

    @Override
    public Object deserializeCompound(CompoundNBT tag, Class<?> clazz) throws ReflectiveOperationException
    {
        if (tag.getString("type").equals("compound"))
            return tag.get("value");

        return tag;
    }
}
