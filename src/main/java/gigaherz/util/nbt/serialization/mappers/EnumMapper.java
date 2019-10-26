package gigaherz.util.nbt.serialization.mappers;

import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.SerializationException;

@SuppressWarnings("unchecked")
public class EnumMapper extends MapperBase
{
    public EnumMapper(int priority)
    {
        super(priority);
    }

    @Override
    public boolean canMapToField(Class<?> clazz)
    {
        return clazz.isEnum();
    }

    @Override
    public boolean canMapToCompound(Class<?> clazz)
    {
        return false;
    }

    @Override
    public CompoundNBT serializeCompound(Object object)
            throws ReflectiveOperationException
    {
        return null;
    }

    @Override
    public Object deserializeCompound(CompoundNBT self, Class<?> clazz)
            throws ReflectiveOperationException
    {
        return null;
    }

    @Override
    public void serializeField(CompoundNBT parent, String fieldName, Object object)
            throws ReflectiveOperationException
    {
        CompoundNBT tag2 = new CompoundNBT();
        serializeEnum(tag2, ((Enum) object));
        parent.put(fieldName, tag2);
    }

    @Override
    public Object deserializeField(CompoundNBT parent, String fieldName, Class<?> clazz)
            throws ReflectiveOperationException
    {
        CompoundNBT tag2 = (CompoundNBT) parent.get(fieldName);
        return deserializeEnum(tag2, (Class<? extends Enum>) clazz);
    }

    private static void serializeEnum(CompoundNBT tag, Enum o)
    {
        tag.putString("type", "enum");
        tag.putString("className", o.getClass().getName());
        tag.putString("valueName", o.name());
    }

    private static Object deserializeEnum(CompoundNBT tag, Class<? extends Enum> clazz)
    {
        if (!tag.getString("type").equals("enum"))
            throw new SerializationException();
        if (!tag.getString("className").equals(clazz.getName()))
            throw new SerializationException();

        return Enum.valueOf(clazz, tag.getString("value"));
    }
}
